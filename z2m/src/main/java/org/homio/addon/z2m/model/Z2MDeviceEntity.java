package org.homio.addon.z2m.model;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.homio.addon.z2m.service.Z2MDeviceService;
import org.homio.addon.z2m.service.Z2MProperty;
import org.homio.addon.z2m.setting.ZigBeeEntityCompactModeSetting;
import org.homio.addon.z2m.util.Z2MDeviceModel;
import org.homio.addon.z2m.util.Z2MDeviceModel.Z2MDeviceDefinition;
import org.homio.addon.z2m.util.Z2MDeviceModel.Z2MDeviceDefinition.Options;
import org.homio.addon.z2m.util.ZigBeeUtil;
import org.homio.api.EntityContext;
import org.homio.api.entity.BaseEntity;
import org.homio.api.entity.DeviceBaseEntity;
import org.homio.api.entity.DisableCacheEntity;
import org.homio.api.entity.HasJsonData;
import org.homio.api.entity.HasStatusAndMsg;
import org.homio.api.entity.zigbee.ZigBeeDeviceBaseEntity;
import org.homio.api.entity.zigbee.ZigBeeProperty;
import org.homio.api.model.ActionResponseModel;
import org.homio.api.model.Icon;
import org.homio.api.model.Status;
import org.homio.api.optionProvider.SelectPlaceOptionLoader;
import org.homio.api.ui.UI;
import org.homio.api.ui.UI.Color;
import org.homio.api.ui.action.UIActionHandler;
import org.homio.api.ui.field.UIField;
import org.homio.api.ui.field.UIFieldGroup;
import org.homio.api.ui.field.UIFieldIgnore;
import org.homio.api.ui.field.UIFieldSlider;
import org.homio.api.ui.field.UIFieldTitleRef;
import org.homio.api.ui.field.UIFieldType;
import org.homio.api.ui.field.action.HasDynamicContextMenuActions;
import org.homio.api.ui.field.action.v1.UIInputBuilder;
import org.homio.api.ui.field.action.v1.UIInputEntity;
import org.homio.api.ui.field.action.v1.layout.UIFlexLayoutBuilder;
import org.homio.api.ui.field.color.UIFieldColorBgRef;
import org.homio.api.ui.field.color.UIFieldColorStatusMatch;
import org.homio.api.ui.field.condition.UIFieldShowOnCondition;
import org.homio.api.ui.field.inline.UIFieldInlineEntities;
import org.homio.api.ui.field.inline.UIFieldInlineEntityWidth;
import org.homio.api.ui.field.selection.UIFieldSelectValueOnEmpty;
import org.homio.api.ui.field.selection.UIFieldSelection;
import org.homio.api.ui.field.selection.UIFieldTreeNodeSelection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

@Log4j2
@Getter
@Setter
@NoArgsConstructor
@DisableCacheEntity
public final class Z2MDeviceEntity extends ZigBeeDeviceBaseEntity<Z2MDeviceEntity>
    implements HasJsonData, HasStatusAndMsg<Z2MDeviceEntity>, HasDynamicContextMenuActions {

    public static final String PREFIX = "z2m_";
    @JsonIgnore private transient Z2MDeviceService deviceService;

    public Z2MDeviceEntity(Z2MDeviceService deviceService, String ieeeAddress) {
        super();
        this.deviceService = deviceService;

        setEntityID(PREFIX + ieeeAddress);
        setIeeeAddress(ieeeAddress);
    }

    @Override
    public @NotNull String getDeviceFullName() {
        return deviceService.getDeviceFullName();
    }

    @Override
    public @NotNull Map<String, ZigBeeProperty> getProperties() {
        return deviceService.getProperties().entrySet().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    @Override
    public @Nullable ZigBeeProperty getProperty(@NotNull String property) {
        return deviceService.getProperties().get(property);
    }

    @Override
    @JsonIgnore
    @UIFieldIgnore
    public String getIeeeAddress() {
        return super.getIeeeAddress();
    }

    public Z2MDeviceEntity setName(String value) {
        deviceService.updateConfiguration("friendly_name", value);
        return this;
    }

    @UIField(order = 10)
    @UIFieldColorStatusMatch
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    public Status getStatus() {
        String availability = deviceService.getAvailability();
        Status status = Status.UNKNOWN;
        if (availability != null) {
            switch (availability) {
                case "offline" -> status = Status.OFFLINE;
                case "online" -> status = Status.ONLINE;
            }
        }
        if (deviceService.getCoordinatorService().getEntity().getStatus() != Status.ONLINE) {
            status = deviceService.getCoordinatorService().getEntity().getStatus();
        }
        return status;
    }

    @Override
    public @NotNull Icon getEntityIcon() {
        return new Icon(
            ZigBeeUtil.getDeviceIcon(deviceService.getDevice().getModelId(), "fas fa-server"),
            ZigBeeUtil.getDeviceIconColor(deviceService.getDevice().getModelId(), UI.Color.random()));
    }

    @UIField(order = 1, hideOnEmpty = true, fullWidth = true, color = "#89AA50", inlineEdit = true)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldColorBgRef(value = "statusColor", animate = true)
    @UIFieldGroup(value = "NAME", order = 1, borderColor = "#CDD649")
    public String getDescription() {
        return deviceService.getDevice().getDefinition().getDescription();
    }

    public void setDescription(String value) {
        if (!Objects.equals(getDescription(), value)) {
            deviceService.updateConfiguration("description", value);
        }
    }

    @Override
    @UIField(order = 2, hideOnEmpty = true, inlineEdit = true)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup("NAME")
    public String getName() {
        return deviceService.getConfiguration().path("friendly_name").asText(deviceService.getDevice().getName());
    }

    @UIField(order = 3)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup("NAME")
    public String getModel() {
        return deviceService.getDevice().getDefinition().getModel();
    }

    @UIField(order = 1, fullWidth = true, color = "#89AA50", type = UIFieldType.HTML, style = "height: 32px;")
    @UIFieldShowOnCondition("return context.get('compactMode')")
    @UIFieldColorBgRef(value = "statusColor", animate = true)
    @UIFieldGroup(value = "NAME", order = 1, borderColor = "#CDD649")
    public String getCompactDescription() {
        return format("<div class=\"inline-2row_d\">"
                + "<div>%s <span style=\"color:%s\">${%s}</span><span style=\"float:right\" class=\"color-primary\">%s</span>"
                + "</div><div>${%s~%s}</div></div>",
            getIeeeAddressLabel(), getStatus().getColor(), getStatus(), getModel(), getName(), getDescription());
    }

    @UIField(order = 3, disableEdit = true, label = "ieeeAddress")
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup(value = "GENERAL", order = 5)
    public String getIeeeAddressLabel() {
        return getIeeeAddress().toUpperCase();
    }

    @UIField(order = 5)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup("GENERAL")
    public String getNetworkAddress() {
        return "0x" + Integer.toHexString(deviceService.getDevice().getNetworkAddress()).toUpperCase();
    }

    @UIField(order = 15)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup("GENERAL")
    public String getModelIdentifier() {
        return deviceService.getDevice().getModelId();
    }

    @UIField(order = 20, hideOnEmpty = true)
    @UIFieldGroup("GENERAL")
    public String getCurrentPowerSource() {
        return isCompactMode() ? null : deviceService.getDevice().getPowerSource();
    }

    @UIField(order = 25)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup("GENERAL")
    public String getLogicalType() {
        return deviceService.getDevice().getType();
    }

    @UIField(order = 35)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup("GENERAL")
    public boolean getInterviewCompleted() {
        return deviceService.getDevice().isInterviewCompleted();
    }

    @UIField(order = 1)
    @UIFieldGroup(value = "HARDWARE", order = 10, borderColor = Color.RED)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    public String getFirmwareVersion() {
        return deviceService.getDevice().getFirmwareVersion();
    }

    @UIField(order = 2)
    @UIFieldGroup("HARDWARE")
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    public String getFirmwareBuildDate() {
        return deviceService.getDevice().getFirmwareBuildDate();
    }

    @UIField(order = 3)
    @UIFieldGroup("HARDWARE")
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    public String getManufacturer() {
        return deviceService.getDevice().getDefinition().getVendor();
    }

    public boolean isCompactMode() {
        return getEntityContext().setting().getValue(ZigBeeEntityCompactModeSetting.class);
    }

    @UIField(order = 1, type = UIFieldType.SelectBox, color = "#7FE486", inlineEdit = true)
    @UIFieldSelection(SelectPlaceOptionLoader.class)
    @UIFieldSelectValueOnEmpty(label = "SELECT_PLACE")
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup(value = "SETTINGS", order = 30, borderColor = "#22AB84")
    public String getPlace() {
        return deviceService.getConfiguration().path("place").asText();
    }

    @Override
    public DeviceBaseEntity<Z2MDeviceEntity> setPlace(String value) {
        if (!Objects.equals(getPlace(), value)) {
            deviceService.updateConfiguration("place", value);
        }
        return this;
    }

    @UIField(order = 4, inlineEdit = true)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup("SETTINGS")
    public boolean isRetainDeviceMessages() {
        return deviceService.getConfiguration().path("retain").asBoolean(false);
    }

    public void setRetainDeviceMessages(boolean value) {
        deviceService.updateConfiguration("retain", value);
    }

    @UIField(order = 5, inlineEdit = true)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup("SETTINGS")
    public boolean isDisabled() {
        return deviceService.getConfiguration().path("disabled").asBoolean(deviceService.getDevice().isDisabled());
    }

    public void setDisabled(boolean value) {
        deviceService.updateConfiguration("disabled", value);
    }

    @UIField(order = 6, inlineEdit = true)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup("SETTINGS")
    public boolean isLogEvents() {
        return deviceService.getConfiguration().path("log").asBoolean(false);
    }

    public void setLogEvents(boolean value) {
        deviceService.updateConfiguration("log", value);
    }

    @UIField(order = 1, inlineEdit = true)
    @UIFieldSlider(min = 0, max = 10, step = 0.5)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup("ADVANCED")
    public Float getDebounce() {
        JsonNode deviceOptions = deviceService.getConfiguration();
        if (deviceOptions.has("debounce")) {
            return (float) deviceOptions.get("debounce").asDouble();
        }
        return null;
    }

    public void setDebounce(Float value) {
        if (!Objects.equals(getDebounce(), value)) {
            deviceService.updateConfiguration("debounce", value);
        }
    }

    @UIField(order = 2, inlineEdit = true, type = UIFieldType.Chips)
    @UIFieldShowOnCondition("return !context.get('compactMode')")
    @UIFieldGroup("ADVANCED")
    public Set<String> getDebounceIgnore() {
        JsonNode deviceOptions = deviceService.getConfiguration();
        if (deviceOptions.has("debounce_ignore")) {
            JsonNode debounceIgnoreList = deviceOptions.get("debounce_ignore");
            Set<String> set = new HashSet<>();
            for (JsonNode jsonNode : debounceIgnoreList) {
                if (StringUtils.isNotEmpty(jsonNode.asText())) {
                    set.add(jsonNode.asText());
                }
            }
            return set;
        }
        return Collections.emptySet();
    }

    public void setDebounceIgnore(List<String> list) {
        deviceService.updateConfiguration("debounce_ignore", list.isEmpty() ? null : list);
    }

    @Override
    protected String getImageIdentifierImpl() {
        JsonNode deviceOptions = deviceService.getConfiguration();
        return deviceOptions.path("image").asText(getModelIdentifier());
    }

    public void setImageIdentifier(String value) {
        deviceService.updateConfiguration("image", trimToNull(Objects.equals(value, getModelIdentifier()) ? null : value));
    }

    @UIField(order = 9999)
    @UIFieldInlineEntities(bg = "#27FF0005")
    public List<Z2MPropertyEntity> getEndpointClusters() {
        return deviceService.getProperties().values().stream()
                            .filter(Z2MProperty::isVisible)
                            .map(Z2MPropertyEntity::new)
                            .sorted()
                            .collect(Collectors.toList());
    }

    public String getStatusColor() {
        return getStatus().isOnline() ? "" : "#FF000030";
    }

    @Override
    public @NotNull String getEntityPrefix() {
        return PREFIX;
    }

    @Override
    public String getDefaultName() {
        return "Z2M";
    }

    @Override
    public boolean isDisableEdit() {
        return true;
    }

    @Override
    public boolean isDisableDelete() {
        return true;
    }

    @Override
    public void assembleActions(UIInputBuilder uiInputBuilder) {
        Z2MActionsBuilder.createWidgetActions(uiInputBuilder, getEntityContext(), this);

        Z2MDeviceDefinition definition = deviceService.getDevice().getDefinition();
        if (definition != null) {
            for (Options option : definition.getOptions()) {
                JsonNode deviceConfigurationOptions = deviceService.getConfiguration();
                String flexName = format("${z2m.setting.%s~%s}", option.getName(), ZigBeeUtil.splitNameToReadableFormat(option.getName()));
                switch (option.getType()) {
                    case Z2MDeviceModel.BINARY_TYPE -> buildBinaryTypeAction(uiInputBuilder, option, deviceConfigurationOptions, flexName);
                    case Z2MDeviceModel.NUMBER_TYPE -> buildNumberTypeAction(uiInputBuilder, option, deviceConfigurationOptions, flexName);
                }
            }
        }
    }

    public @NotNull Date getUpdateTime() {
        return new Date(deviceService.getProperties().values()
                                     .stream()
                                     .max(Comparator.comparingLong(Z2MProperty::getUpdated))
                                     .map(Z2MProperty::getUpdated).orElse(0L));
    }

    @Override
    public ActionResponseModel handleAction(EntityContext entityContext, String actionID, JSONObject params) throws Exception {
        for (Z2MPropertyEntity endpointCluster : getEndpointClusters()) {
            if (actionID.startsWith(endpointCluster.getEntityID())) {
                UIActionHandler actionHandler = endpointCluster.buildAction().findActionHandler(actionID);
                if (actionHandler != null) {
                    return actionHandler.handleAction(entityContext, params);
                }
            }
        }
        return HasDynamicContextMenuActions.super.handleAction(entityContext, actionID, params);
    }

    @Override
    public int compareTo(@NotNull BaseEntity o) {
        if (o instanceof Z2MDeviceEntity other) {
            return ((getStatus().isOnline() ? 0 : 1) + getName()).compareTo((other.getStatus().isOnline() ? 0 : 1) + o.getName());
        }
        return super.compareTo(o);
    }

    private static Float toFloat(Integer value) {
        return value == null ? null : value.floatValue();
    }

    private void buildNumberTypeAction(UIInputBuilder uiInputBuilder, Options option, JsonNode deviceConfigurationOptions, String flexName) {
        JsonNode deviceOptions =
            ZigBeeUtil.getDeviceOptions(deviceService.getDevice().getModelId());
        Integer minValue = option.getValueMin() == null ? (deviceOptions.has("min") ? deviceOptions.get("min").asInt() : null) : option.getValueMin();
        Integer maxValue = option.getValueMax() == null ? (deviceOptions.has("max") ? deviceOptions.get("max").asInt() : null) : option.getValueMax();

        Integer nValue = deviceConfigurationOptions == null ? null : deviceConfigurationOptions.path(option.getName()).asInt(0);
        uiInputBuilder.addFlex(option.getName() + "_flex", flex -> {
                          flex.addNumberInput(option.getName(), toFloat(nValue), toFloat(minValue), toFloat(maxValue),
                              (entityContext, params) -> {
                                  this.deviceService.getCoordinatorService().updateDeviceConfiguration(deviceService, option.getName(),
                                      params.has("value") ? params.getInt("value") : null);
                                  return null;
                              });
                          flex.addInfo(option.getDescription()).setOuterClass("context-description");
                      })
                      .columnFlexDirection()
                      .setBorderColor(Color.random())
                      .setBorderArea(flexName);
    }

    private void buildBinaryTypeAction(UIInputBuilder uiInputBuilder, Options option, JsonNode deviceConfigurationOptions, String flexName) {
        boolean defaultValue = StringUtils.defaultString(option.getDescription(), "").contains("(default true)");
        boolean bValue = deviceConfigurationOptions == null ? defaultValue : deviceConfigurationOptions.path(option.getName()).asBoolean(defaultValue);

        uiInputBuilder.addFlex(option.getName() + "_flex", flex -> buildCheckbox(option, bValue, flex))
                      .columnFlexDirection()
                      .setBorderColor(Color.random())
                      .setBorderArea(flexName);
    }

    private void buildCheckbox(Options option, boolean bValue, UIFlexLayoutBuilder flex) {
        flex.addCheckbox(option.getName(), bValue, (entityContext, params) -> {
            this.deviceService.getCoordinatorService().updateDeviceConfiguration(deviceService, option.getName(), params.getBoolean("value"));
            return null;
        });
        flex.addInfo(option.getDescription()).setOuterClass("context-description");
    }

    @Getter
    @NoArgsConstructor
    public static class Z2MPropertyEntity implements Comparable<Z2MPropertyEntity> {

        private String entityID;

        @UIField(order = 2, type = UIFieldType.HTML)
        private String title;

        @JsonIgnore private Z2MProperty property;
        private String valueTitle;

        public Z2MPropertyEntity(Z2MProperty property) {
            this.entityID = property.getEntityID();
            this.title = format("<div class=\"inline-2row_d\"><div style=\"color:%s;\"><i class=\"mr-1 %s\"></i>%s</div><span>%s</div></div>",
                property.getIcon().getColor(), property.getIcon().getIcon(), property.getName(false), property.getDescription());
            this.property = property;
            this.valueTitle = property.getValue().toString();
            if (Z2MDeviceModel.ENUM_TYPE.equals(property.getExpose().getType())) {
                this.valueTitle = "Values: " + String.join(", ", getProperty().getExpose().getValues());
            }
        }

        @UIField(order = 4, style = "margin-left: auto; margin-right: 8px;")
        @UIFieldInlineEntityWidth(30)
        @UIFieldTitleRef("valueTitle")
        public UIInputEntity getValue() {
            return buildAction().buildAll().iterator().next();
        }

        @Override
        public int compareTo(@NotNull Z2MDeviceEntity.Z2MPropertyEntity o) {
            return ZigBeeUtil.compareProperty(this.property.getExpose().getName(), o.getProperty().getExpose().getName());
        }

        private @NotNull UIInputBuilder buildAction() {
            return ZigBeeUtil.buildZigbeeActions(property, entityID);
        }
    }
}
