package org.touchhome.bundle.camera;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.touchhome.bundle.api.BundleEntryPoint;
import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.EntityContextBGP;
import org.touchhome.bundle.api.model.Status;
import org.touchhome.bundle.api.netty.NettyUtils;
import org.touchhome.bundle.api.setting.SettingPluginStatus;
import org.touchhome.bundle.api.util.TouchHomeUtils;
import org.touchhome.bundle.api.video.BaseFFMPEGVideoStreamEntity;
import org.touchhome.bundle.api.video.BaseFFMPEGVideoStreamHandler;
import org.touchhome.bundle.api.video.ffmpeg.FFMPEG;
import org.touchhome.bundle.camera.handler.impl.OnvifCameraHandler;
import org.touchhome.bundle.camera.onvif.brand.BaseOnvifCameraBrandHandler;
import org.touchhome.bundle.camera.onvif.brand.CameraBrandHandlerDescription;
import org.touchhome.bundle.camera.scanner.OnvifCameraHttpScanner;
import org.touchhome.bundle.camera.setting.CameraStatusSetting;

@Log4j2
@Component
@RequiredArgsConstructor
public class CameraEntryPoint implements BundleEntryPoint {

  private final EntityContext entityContext;
  private EntityContextBGP.ThreadContext<Void> cameraRunPerMinuteSchedule;

  @Override
  public int order() {
    return 300;
  }

  public void init() {
    entityContext.bgp().registerThreadsPuller("camera-ffmpeg", threadPuller -> {
      for (Map.Entry<String, FFMPEG> threadEntry : FFMPEG.ffmpegMap.entrySet()) {
        FFMPEG ffmpeg = threadEntry.getValue();
        if (ffmpeg.getIsAlive()) {
          threadPuller.addThread(threadEntry.getKey(), ffmpeg.getDescription(), ffmpeg.getCreationDate(),
              "working", null,
              "Command: " + String.join(" ", ffmpeg.getCommandArrayList())
          );
        }
      }
    });

    // fulfill camera brands
    for (Class<? extends BaseOnvifCameraBrandHandler> brandHandlerClass :
        entityContext.getClassesWithParent(BaseOnvifCameraBrandHandler.class, "org.touchhome.bundle.camera.onvif")) {
      CameraCoordinator.cameraBrands.put(brandHandlerClass.getSimpleName(),
          new CameraBrandHandlerDescription(brandHandlerClass));
    }

    // listen if camera entity removed
    entityContext.event().addEntityRemovedListener(BaseFFMPEGVideoStreamEntity.class, "camera-remove-listener", cameraEntity -> {
      CameraCoordinator.removeSpdMessage(cameraEntity.getIeeeAddress());
      // remove camera handler
      BaseFFMPEGVideoStreamHandler handler = NettyUtils.removeBootstrapServer(cameraEntity.getEntityID());
      if (handler != null) {
        handler.dispose();
        handler.deleteDirectories();
      }
    });

    // lister start/stop status and any changes that require restart camera handler
    entityContext.event().addEntityUpdateListener(BaseFFMPEGVideoStreamEntity.class, "camera-change-listener",
        (cameraEntity, oldCameraEntity) -> {
          BaseFFMPEGVideoStreamHandler cameraHandler = cameraEntity.getVideoHandler();
          cameraHandler.updateVideoStreamEntity(cameraEntity);

          if (cameraEntity.isStart() && !cameraHandler.isHandlerInitialized()) {
            cameraHandler.initialize();
          } else if (!cameraEntity.isStart() && cameraHandler.isHandlerInitialized()) {
            cameraHandler.disposeAndSetStatus(Status.OFFLINE, "Camera not started");
          } else if (/*TODO: cameraHandler.isHandlerInitialized() && */
              TouchHomeUtils.isRequireRestartHandler(oldCameraEntity, cameraEntity)) {
            cameraHandler.restart("Restart camera handler", true);
          }
          // change camera name if possible
          if (oldCameraEntity != null && !Objects.equals(cameraEntity.getName(), oldCameraEntity.getName())) {
            if (cameraHandler instanceof OnvifCameraHandler) {
              ((OnvifCameraHandler) cameraHandler).getOnvifDeviceState().getInitialDevices().setName(cameraEntity.getName());
            }
          }
        });

    entityContext.event().runOnceOnInternetUp("scan-cameras", () -> {
      // fire rescan whole possible items to see if ip address has been changed
      entityContext.getBean(OnvifCameraHttpScanner.class).executeScan(entityContext, null, null, true);
    });

    cameraRunPerMinuteSchedule = entityContext.bgp().builder("Camera bundle schedule").interval(Duration.ofMinutes(5))
        .execute(this::fireStartCamera);
  }

  private void fireStartCamera() {
    for (BaseFFMPEGVideoStreamEntity cameraEntity : entityContext.findAll(BaseFFMPEGVideoStreamEntity.class)) {
      BaseFFMPEGVideoStreamHandler cameraHandler = cameraEntity.getVideoHandler();
      if ((cameraEntity.isStart() || cameraEntity.isAutoStart()) && !cameraHandler.isHandlerInitialized()) {
        if (!cameraEntity.isStart()) {
          entityContext.save(cameraEntity.setStart(true)); // start=true is a trigger to start camera
        } else {
          BaseFFMPEGVideoStreamHandler handler = cameraEntity.getVideoHandler();
          handler.updateVideoStreamEntity(cameraEntity);
          handler.initialize();
        }
      }
    }
  }

  @Override
  public void destroy() {
    Optional.ofNullable(cameraRunPerMinuteSchedule).ifPresent(EntityContextBGP.ThreadContext::cancel);
  }

  @Override
  public Class<? extends SettingPluginStatus> getBundleStatusSetting() {
    return CameraStatusSetting.class;
  }
}
