package org.touchhome.bundle.camera.handler;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.EntityContextBGP;
import org.touchhome.bundle.api.model.Status;
import org.touchhome.bundle.api.netty.HasBootstrapServer;
import org.touchhome.bundle.api.state.State;
import org.touchhome.bundle.api.ui.field.action.v1.UIInputBuilder;
import org.touchhome.bundle.api.util.TouchHomeUtils;
import org.touchhome.bundle.api.workspace.BroadcastLockManager;
import org.touchhome.bundle.camera.entity.BaseVideoCameraEntity;
import org.touchhome.bundle.camera.ffmpeg.Ffmpeg;
import org.touchhome.bundle.camera.ffmpeg.FfmpegInputDeviceHardwareRepository;
import org.touchhome.bundle.camera.setting.FFMPEGInstallPathSetting;
import org.touchhome.bundle.camera.ui.CameraActionsContext;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

@Log4j2
public abstract class BaseCameraHandler<T extends BaseVideoCameraEntity> implements HasBootstrapServer, CameraActionsContext {

    @Getter
    protected final EntityContext entityContext;
    protected final BroadcastLockManager broadcastLockManager;
    @Getter
    protected final int serverPort;
    protected final FfmpegInputDeviceHardwareRepository ffmpegInputDeviceHardwareRepository;
    public ReentrantLock lockCurrentSnapshot = new ReentrantLock();
    @Getter
    protected byte[] latestSnapshot = new byte[0];
    @Setter
    @Getter
    protected T cameraEntity;
    protected String cameraEntityID;
    protected String ffmpegLocation;
    @Getter
    protected Map<String, State> attributes = new ConcurrentHashMap<>();
    @Getter
    protected Map<String, State> requestAttributes = new ConcurrentHashMap<>();
    protected long lastAnswerFromCamera;
    @Getter
    private Path ffmpegGifOutputPath;
    @Getter
    private Path ffmpegMP4OutputPath;
    @Getter
    private Path ffmpegHLSOutputPath;
    @Getter
    private Path ffmpegImageOutputPath;
    @Getter
    private boolean isCameraOnline = false; // Used so only 1 error is logged when a network issue occurs.
    @Getter
    private boolean isHandlerInitialized = false;
    private EntityContextBGP.ThreadContext<Void> cameraConnectionJob;
    private EntityContextBGP.ThreadContext<Void> pollCameraJob;
    private Map<String, Consumer<Status>> stateListeners = new HashMap<>();

    public BaseCameraHandler(T cameraEntity, EntityContext entityContext) {
        setCameraEntity(cameraEntity);
        this.cameraEntityID = cameraEntity.getEntityID();

        this.entityContext = entityContext;
        this.ffmpegInputDeviceHardwareRepository = entityContext.getBean(FfmpegInputDeviceHardwareRepository.class);
        this.broadcastLockManager = entityContext.getBean(BroadcastLockManager.class);
        this.serverPort = cameraEntity.getServerPort();

        Path ffmpegOutputPath = TouchHomeUtils.getMediaPath().resolve("camera").resolve(cameraEntityID);
        ffmpegImageOutputPath = TouchHomeUtils.createDirectoriesIfNotExists(ffmpegOutputPath.resolve("images"));
        ffmpegGifOutputPath = TouchHomeUtils.createDirectoriesIfNotExists(ffmpegOutputPath.resolve("gif"));
        ffmpegMP4OutputPath = TouchHomeUtils.createDirectoriesIfNotExists(ffmpegOutputPath.resolve("mp4"));
        ffmpegHLSOutputPath = TouchHomeUtils.createDirectoriesIfNotExists(ffmpegOutputPath.resolve("hls"));
        try {
            FileUtils.cleanDirectory(ffmpegHLSOutputPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Unable to clean path: " + ffmpegHLSOutputPath);
        }

        // for custom ffmpeg path
        entityContext.setting().listenValueAndGet(FFMPEGInstallPathSetting.class, "listen-ffmpeg-path-" + cameraEntityID,
                path -> {
                    this.ffmpegLocation = path.toString();
                    this.restart("ffmpeg location changed", null, false);
                });
    }

    protected abstract void pollingCameraConnection();

    protected abstract void pollCameraRunnable();

    public final void initialize(T cameraEntity) {
        if (isHandlerInitialized) {
            return;
        }
        isHandlerInitialized = true;
        try {
            if (cameraEntity == null) {
                if (this.cameraEntity == null) {
                    throw new RuntimeException("Unable to init camera with id: " + cameraEntityID);
                }
            } else if (!cameraEntity.getEntityID().equals(cameraEntityID)) {
                throw new RuntimeException("Unable to init camera <" + cameraEntity + "> with different id than: " + cameraEntityID);
            } else {
                this.cameraEntity = cameraEntity;
            }

            initialize0();
            cameraConnectionJob = entityContext.bgp().schedule("poll-camera-connection-" + cameraEntityID,
                    60, TimeUnit.SECONDS, this::pollingCameraConnection, true, true);
        } catch (Exception ex) {
            disposeAndSetStatus(Status.ERROR, "Error while init camera: " + TouchHomeUtils.getErrorMessage(ex));
        }
    }

    protected abstract void initialize0();

    public final void disposeAndSetStatus(Status status, String reason) {
        if (isHandlerInitialized) {
            isHandlerInitialized = false;
            // set it before to avoid recursively disposing from listeners
            log.warn("Set camera <{}> to status <{}>. Msg: <{}>", cameraEntity.getTitle(), status, reason);

            cameraEntity.setStatus(status, reason);
            entityContext.updateDelayed(this.cameraEntity, e -> e.setStart(false));
            entityContext.ui().sendEntityUpdated(this.cameraEntity);
            this.stateListeners.values().forEach(h -> h.accept(status));
            dispose();
        }
    }

    public final void dispose() {
        if (isHandlerInitialized) {
            isHandlerInitialized = false;
            disposeCameraConnectionJob();
            disposePollCameraJob();
            try {
                dispose0();
            } catch (Exception ex) {
                log.error("Error while dispose camera: <{}>", cameraEntity.getTitle(), ex);
            }
            isCameraOnline = false;
        }
    }

    protected abstract void dispose0();

    public abstract void recordMp4(String fileName, int secondsToRecord);

    public abstract void recordGif(String fileName, int secondsToRecord);

    public final void bringCameraOnline() {
        lastAnswerFromCamera = System.currentTimeMillis();
        if (!isCameraOnline && isHandlerInitialized) {
            isCameraOnline = true;
            updateStatus(Status.ONLINE, "Success");
            bringCameraOnline0();

            disposeCameraConnectionJob();
            pollCameraJob = entityContext.bgp().schedule("poll-camera-runnable-" + cameraEntityID,
                    8, TimeUnit.SECONDS, this::pollCameraRunnable, true, true);
        }
    }

    private void disposeCameraConnectionJob() {
        if (cameraConnectionJob != null) {
            cameraConnectionJob.cancel();
            cameraConnectionJob = null;
        }
    }

    private void disposePollCameraJob() {
        if (pollCameraJob != null) {
            pollCameraJob.cancel();
            pollCameraJob = null;
        }
    }

    protected void bringCameraOnline0() {

    }

    public final void restart(String reason, T cameraEntity, boolean force) {
        if (force && !this.isHandlerInitialized) {
            initialize(cameraEntity);
        } else if (isCameraOnline) { // if already offline dont try reconnecting in 6 seconds, we want 30sec wait.
            updateStatus(Status.OFFLINE, reason); // will try to reconnect again as camera may be rebooting.
            dispose();
            initialize(cameraEntity);
        }
    }

    protected final void updateStatus(Status status, String message) {
        if (message != null) {
            log.info("Camera update status: <{}>. <{}>", status, message);
        } else {
            log.info("Camera update status: <{}>", status);
        }
        cameraEntity.setStatus(status, message);
    }

    public void assembleActions(UIInputBuilder uiInputBuilder, boolean fetchValues) {
        assembleAdditionalCameraActions(uiInputBuilder);
        if (fetchValues) {
            uiInputBuilder.fireFetchValues();
            /* TODO: for (StatefulContextMenuAction action : actions) {
                for (Consumer<StatefulContextMenuAction> updateHandler : action.getUpdateHandlers().values()) {
                    updateHandler.accept(action);
                }
            }*/
        }
    }

    /*TODO: public Set<StatefulContextMenuAction> getCameraActions(boolean fetchValues) {
        if (actions == null) {
            // additional actions should be first
            actions = new TreeSet<>(getAdditionalCameraActions());
            actions.addAll(CameraActionBuilder.assemble(this));
        }
        if (fetchValues) {
            for (StatefulContextMenuAction action : actions) {
                for (Consumer<StatefulContextMenuAction> updateHandler : action.getUpdateHandlers().values()) {
                    updateHandler.accept(action);
                }
            }
        }
        return actions;
    }*/

    protected void assembleAdditionalCameraActions(UIInputBuilder uiInputBuilder) {

    }

    public void setAttribute(String key, State state) {
        attributes.put(key, state);
        broadcastLockManager.signalAll(key + ":" + cameraEntityID, state);
    }

    @Override
    public State getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttributeRequest(String key, State state) {
        requestAttributes.put(key, state);
    }

    protected final void fireFfmpeg(Ffmpeg ffmpeg, Consumer<Ffmpeg> handler) {
        if (ffmpeg != null) {
            handler.accept(ffmpeg);
        }
    }

    public abstract void startSnapshot();

    public void deleteDirectories() {
        TouchHomeUtils.deleteDirectory(ffmpegGifOutputPath);
        TouchHomeUtils.deleteDirectory(ffmpegMP4OutputPath);
        TouchHomeUtils.deleteDirectory(ffmpegImageOutputPath);
    }

    @Override
    public String getName() {
        return cameraEntity.getTitle();
    }

    public void addCameraChangeState(String key, Consumer<Status> handler) {
        this.stateListeners.put(key, handler);
    }

    public void removeCameraChangeState(String key) {
        this.stateListeners.remove(key);
    }

    public abstract void testOnline();
}
