package org.touchhome.bundle.camera.service;

import static org.touchhome.bundle.api.util.TouchHomeUtils.FFMPEG_LOCATION;
import static org.touchhome.bundle.api.video.ffmpeg.FFMPEGFormat.GENERAL;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.model.ActionResponseModel;
import org.touchhome.bundle.api.ui.UI.Color;
import org.touchhome.bundle.api.util.TouchHomeUtils;
import org.touchhome.bundle.api.video.BaseVideoService;
import org.touchhome.bundle.api.video.BaseVideoStreamServerHandler;
import org.touchhome.bundle.api.video.ffmpeg.FFMPEG;
import org.touchhome.bundle.api.video.ffmpeg.FfmpegInputDeviceHardwareRepository;
import org.touchhome.bundle.camera.entity.UsbCameraEntity;

@Log4j2
public class UsbCameraService extends BaseVideoService<UsbCameraEntity> {

    private final List<String> outputs = new ArrayList<>();
    private FFMPEG ffmpegUsbStream;

    public UsbCameraService(UsbCameraEntity entity, EntityContext entityContext) {
        super(entity, entityContext);
    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean testService() {
        return false;
    }

    @Override
    public String getRtspUri(String profile) {
        return "udp://@" + outputs.get(0);
    }

    @Override
    public String getFFMPEGInputOptions(String profile) {
        return "";
    }

    @Override
    public void afterInitialize() {
        updateNotificationBlock();
    }

    @Override
    public void afterDispose() {
        updateNotificationBlock();
    }

    public void updateNotificationBlock() {
        entityContext.ui().addNotificationBlock(entityID, getEntity().getTitle(), "fas fa-usb", "#02B07C", builder -> {
            builder.setStatus(getEntity().getSourceStatus());
            if (!getEntity().isStart()) {
                builder.addButtonInfo("video.not_started", Color.RED, "fas fa-stop", null, "fas fa-play", "Start", null, (entityContext, params) -> {
                    entityContext.save(getEntity().setStart(true));
                    return ActionResponseModel.success();
                });
            } else {
                builder.setStatusMessage(getEntity().getSourceStatusMessage());
            }
        });
    }

    @Override
    protected void initialize0() throws Exception {
        UsbCameraEntity entity = getEntity();
        String url = "video=\"" + entity.getIeeeAddress() + "\"";
        if (StringUtils.isNotEmpty(entity.getAudioSource())) {
            url += ":audio=\"" + entity.getAudioSource() + "\"";
        }
        Set<String> outputParams = new LinkedHashSet<>(entity.getStreamOptions());
        outputParams.add("-f tee");
        outputParams.add("-map 0:v");
        if (StringUtils.isNotEmpty(entity.getAudioSource())) {
            url += ":audio=\"" + entity.getAudioSource() + "\"";
            outputParams.add("-map 0:a");
        }

        outputs.add(TouchHomeUtils.MACHINE_IP_ADDRESS + ":" + entity.getStreamStartPort());
        outputs.add(TouchHomeUtils.MACHINE_IP_ADDRESS + ":" + (entity.getStreamStartPort() + 1));

        ffmpegUsbStream = new FFMPEG(getEntityID(), "FFmpeg usb udp re streamer", this, log,
            GENERAL, "-loglevel warning " + (SystemUtils.IS_OS_LINUX ? "-f v4l2" : "-f dshow"), url,
            String.join(" ", outputParams),
            outputs.stream().map(o -> "[f=mpegts]udp://" + o + "?pkt_size=1316").collect(Collectors.joining("|")),
            "", "", null);
        ffmpegUsbStream.startConverting();

        super.initialize0();
    }

    @Override
    protected void dispose0() {
        super.dispose0();
        ffmpegUsbStream.stopConverting();

    }

    @Override
    protected void testVideoOnline() {
        FfmpegInputDeviceHardwareRepository repository = entityContext.getBean(FfmpegInputDeviceHardwareRepository.class);
        Set<String> aliveVideoDevices = repository.getVideoDevices(FFMPEG_LOCATION);
        if (!aliveVideoDevices.contains(getEntity().getIeeeAddress())) {
            throw new RuntimeException("Camera not available");
        }
    }

    @Override
    protected String createHlsRtspUri() {
        return "udp://@" + outputs.get(1);
    }

    @Override
    protected BaseVideoStreamServerHandler createVideoStreamServerHandler() {
        return new UsbCameraStreamHandler(this);
    }

    @Override
    protected void streamServerStarted() {

    }

    @Override
    protected boolean hasAudioStream() {
        return super.hasAudioStream() || StringUtils.isNotEmpty(getEntity().getAudioSource());
    }

    private static class UsbCameraStreamHandler extends BaseVideoStreamServerHandler<UsbCameraService> {

        public UsbCameraStreamHandler(UsbCameraService usbCameraService) {
            super(usbCameraService);
        }

        @Override
        protected void handleLastHttpContent(byte[] incomingJpeg) {
        }

        @Override
        protected boolean streamServerReceivedPostHandler(HttpRequest httpRequest) {
            return false;
        }

        @Override
        protected void handlerChildRemoved(ChannelHandlerContext ctx) {
        }
    }
}
