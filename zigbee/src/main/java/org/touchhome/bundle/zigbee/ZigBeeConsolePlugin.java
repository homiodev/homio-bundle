package org.touchhome.bundle.zigbee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.console.ConsolePluginTable;
import org.touchhome.bundle.api.measure.State;
import org.touchhome.bundle.api.model.HasEntityIdentifier;
import org.touchhome.bundle.api.model.Status;
import org.touchhome.bundle.api.setting.BundleSettingPlugin;
import org.touchhome.bundle.api.ui.field.UIField;
import org.touchhome.bundle.api.ui.field.color.UIFieldColorBooleanMatch;
import org.touchhome.bundle.api.ui.field.color.UIFieldColorStatusMatch;
import org.touchhome.bundle.api.ui.field.selection.UIFieldSelectValueOnEmpty;
import org.touchhome.bundle.api.ui.field.selection.UIFieldSelection;
import org.touchhome.bundle.api.ui.method.UIMethodAction;
import org.touchhome.bundle.zigbee.model.ZigBeeDeviceEntity;
import org.touchhome.bundle.zigbee.setting.ZigbeeDiscoveryButtonSetting;
import org.touchhome.bundle.zigbee.setting.ZigbeeStatusSetting;
import org.touchhome.bundle.zigbee.workspace.ZigBeeDeviceUpdateValueListener;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ZigBeeConsolePlugin implements ConsolePluginTable<ZigBeeConsolePlugin.ZigbeeConsoleDescription> {

    private final ZigBeeBundleEntryPoint zigbeeBundleContext;
    private final EntityContext entityContext;

    @Override
    public int order() {
        return 500;
    }

    @Override
    public boolean isEnabled() {
        return entityContext.setting().getValue(ZigbeeStatusSetting.class).isOnline();
    }

    @Override
    public Collection<ZigbeeConsoleDescription> getValue() {
        List<ZigbeeConsoleDescription> res = new ArrayList<>();
        for (ZigBeeDevice zigBeeDevice : zigbeeBundleContext.getCoordinatorHandler().getZigBeeDevices().values()) {
            ZigBeeNodeDescription desc = zigBeeDevice.getZigBeeNodeDescription();
            ZigBeeDeviceEntity entity = entityContext.getEntity(ZigBeeDeviceEntity.PREFIX + zigBeeDevice.getNodeIeeeAddress().toString());
            res.add(new ZigbeeConsoleDescription(
                    entity.getName(),
                    zigBeeDevice.getNodeIeeeAddress().toString(),
                    desc.getDeviceStatus(),
                    desc.getDeviceStatusMessage(),
                    desc.getModelIdentifier(),
                    desc.getFetchInfoStatus(),
                    !zigBeeDevice.getZigBeeConverterEndpoints().isEmpty(),
                    zigBeeDevice.getZigBeeNodeDescription().isNodeInitialized() && !zigBeeDevice.getZigBeeConverterEndpoints().isEmpty(),
                    entity.getEntityID()
            ));
        }
        return res;
    }

    @Override
    public Map<String, Class<? extends BundleSettingPlugin<?>>> getHeaderActions() {
        return Collections.singletonMap("zigbee.start_discovery", ZigbeeDiscoveryButtonSetting.class);
    }

    @Override
    public Class<ZigbeeConsoleDescription> getEntityClass() {
        return ZigbeeConsoleDescription.class;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ZigbeeConsoleDescription implements HasEntityIdentifier {

        @UIField(order = 1, inlineEdit = true)
        private String name;

        @UIField(order = 1)
        private String ieeeAddress;

        @UIField(order = 2)
        @UIFieldColorStatusMatch
        private Status deviceStatus;

        @UIField(order = 3, color = "#B22020")
        private String errorMessage;

        @UIField(order = 4)
        @UIFieldSelection(SelectModelIdentifierDynamicLoader.class)
        @UIFieldSelectValueOnEmpty(label = "zigbee.action.selectModelIdentifier", color = "#A7D21E")
        private String model;

        @UIField(order = 5)
        private ZigBeeNodeDescription.FetchInfoStatus fetchInfoStatus;

        @UIField(order = 6)
        @UIFieldColorBooleanMatch
        private boolean channelsInitialized;

        @UIField(order = 7)
        @UIFieldColorBooleanMatch
        private boolean initialized;

        private String entityID;

        @UIMethodAction("ACTION.INITIALIZE_ZIGBEE_NODE")
        public String initializeZigbeeNode(ZigBeeDeviceEntity zigBeeDeviceEntity) {
            return zigBeeDeviceEntity.initializeZigbeeNode();
        }

        @UIMethodAction(value = "ACTION.SHOW_NODE_DESCRIPTION", responseAction = UIMethodAction.ResponseAction.ShowJson)
        public ZigBeeNodeDescription showNodeDescription(ZigBeeDeviceEntity zigBeeDeviceEntity) {
            return zigBeeDeviceEntity.getZigBeeNodeDescription();
        }

        @UIMethodAction(value = "ACTION.SHOW_LAST_VALUES", responseAction = UIMethodAction.ResponseAction.ShowJson)
        public Map<ZigBeeDeviceStateUUID, State> showLastValues(ZigBeeDeviceEntity zigBeeDeviceEntity, ZigBeeDeviceUpdateValueListener zigBeeDeviceUpdateValueListener) {
            return zigBeeDeviceEntity.showLastValues(zigBeeDeviceEntity, zigBeeDeviceUpdateValueListener);
        }

        @UIMethodAction("ACTION.REDISCOVERY")
        public String rediscoveryNode(ZigBeeDeviceEntity zigBeeDeviceEntity) {
            return zigBeeDeviceEntity.rediscoveryNode();
        }

        @UIMethodAction("ACTION.PERMIT_JOIN")
        public String permitJoin(ZigBeeDeviceEntity zigBeeDeviceEntity, EntityContext entityContext) {
            return zigBeeDeviceEntity.permitJoin(entityContext);
        }

        @UIMethodAction("ACTION.ZIGBEE_PULL_CHANNELS")
        public String pullChannels(ZigBeeDeviceEntity zigBeeDeviceEntity) {
            return zigBeeDeviceEntity.pullChannels();
        }

        @Override
        public Integer getId() {
            return 0;
        }
    }
}
