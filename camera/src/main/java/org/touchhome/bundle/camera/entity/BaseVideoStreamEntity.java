package org.touchhome.bundle.camera.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.data.util.Pair;
import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.entity.DeviceBaseEntity;
import org.touchhome.bundle.api.model.ActionResponseModel;
import org.touchhome.bundle.api.service.scan.BaseBeansItemsDiscovery;
import org.touchhome.bundle.api.ui.UISidebarButton;
import org.touchhome.bundle.api.ui.UISidebarMenu;
import org.touchhome.bundle.api.ui.action.UIActionHandler;
import org.touchhome.bundle.api.ui.field.action.HasDynamicContextMenuActions;
import org.touchhome.bundle.api.ui.field.action.v1.UIInputBuilder;
import org.touchhome.bundle.camera.scanner.VideoStreamScanner;
import org.touchhome.bundle.camera.util.FFMPEGDependencyExecutableInstaller;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Collection;

@Log4j2
@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@UISidebarMenu(icon = "fas fa-video", order = 1, parent = UISidebarMenu.TopSidebarMenu.MEDIA,
        bg = "#5950A7", allowCreateNewItems = true, overridePath = "vstreams")
@UISidebarButton(buttonIcon = "fas fa-qrcode", buttonIconColor = "#ED703E",
        buttonTitle = "TITLE.SCAN_VIDEO_STREAMS",
        handlerClass = BaseVideoStreamEntity.VideoStreamDiscovery.class)
@UISidebarButton(buttonIcon = "fab fa-instalod", buttonIconColor = "#39B84E",
        buttonTitle = "TITLE.INSTALL_FFMPEG",
        handlerClass = FFMPEGDependencyExecutableInstaller.class)
public abstract class BaseVideoStreamEntity<T extends BaseVideoStreamEntity> extends DeviceBaseEntity<T>
        implements HasDynamicContextMenuActions {

    public abstract byte[] getLastSnapshot();

    protected abstract void fireUpdateSnapshot(EntityContext entityContext, JSONObject params);

    public abstract UIInputBuilder assembleActions();

    public abstract Collection<Pair<String, String>> getVideoSources();

    public abstract String getStreamUrl(String key);

    static class VideoStreamDiscovery extends BaseBeansItemsDiscovery {

        public VideoStreamDiscovery() {
            super(VideoStreamScanner.class);
        }
    }

    public static class UpdateSnapshotActionHandler implements UIActionHandler {

        @Override
        public ActionResponseModel handleAction(EntityContext entityContext, JSONObject params) {
            BaseVideoStreamEntity entity = entityContext.getEntity(params.getString("entityID"));
            entity.fireUpdateSnapshot(entityContext, params);
            return null;
        }
    }

    @Override
    public void assembleActions(UIInputBuilder uiInputBuilder) {
        uiInputBuilder.from(assembleActions());
        uiInputBuilder.fireFetchValues();
    }
}
