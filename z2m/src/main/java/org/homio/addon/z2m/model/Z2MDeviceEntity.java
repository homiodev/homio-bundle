package org.homio.addon.z2m.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.homio.addon.z2m.service.Z2MDeviceService;
import org.homio.addon.z2m.service.endpoints.Z2MDeviceEndpointFirmwareUpdate;
import org.homio.addon.z2m.setting.ZigBeeEntityCompactModeSetting;
import org.homio.addon.z2m.util.ApplianceModel;
import org.homio.addon.z2m.util.ApplianceModel.Z2MDeviceDefinition;
import org.homio.addon.z2m.util.ApplianceModel.Z2MDeviceDefinition.Options;
import org.homio.api.entity.zigbee.ZigBeeDeviceBaseEntity;
import org.homio.api.model.Icon;
import org.homio.api.model.Status;
import org.homio.api.model.Status.EntityStatus;
import org.homio.api.model.WebAddress;
import org.homio.api.model.device.ConfigDeviceDefinition;
import org.homio.api.model.endpoint.DeviceEndpoint;
import org.homio.api.optionProvider.SelectPlaceOptionLoader;
import org.homio.api.ui.UI.Color;
import org.homio.api.ui.field.UIField;
import org.homio.api.ui.field.UIFieldGroup;
import org.homio.api.ui.field.UIFieldIgnore;
import org.homio.api.ui.field.UIFieldInlineEditConfirm;
import org.homio.api.ui.field.UIFieldNoReadDefaultValue;
import org.homio.api.ui.field.UIFieldSlider;
import org.homio.api.ui.field.action.v1.UIInputBuilder;
import org.homio.api.ui.field.action.v1.layout.UIFlexLayoutBuilder;
import org.homio.api.ui.field.color.UIFieldColorStatusMatch;
import org.homio.api.ui.field.condition.UIFieldShowOnCondition;
import org.homio.api.ui.field.selection.UIFieldSelectConfig;
import org.homio.api.ui.field.selection.dynamic.UIFieldDynamicSelection;
import org.homio.api.widget.template.WidgetDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.homio.addon.z2m.service.Z2MDeviceService.CONFIG_DEVICE_SERVICE;
import static org.homio.addon.z2m.service.endpoints.Z2MDeviceEndpointFirmwareUpdate.ENDPOINT_FIRMWARE_UPDATE;
import static org.homio.api.ui.UI.Color.ERROR_DIALOG;
import static org.homio.api.ui.field.UIFieldType.SelectBox;
import static org.homio.api.util.CommonUtils.splitNameToReadableFormat;

@Log4j2
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unused")
public final class Z2MDeviceEntity extends ZigBeeDeviceBaseEntity {

  public static final String PREFIX = "z2m";

  @JsonIgnore
  private transient Z2MDeviceService deviceService;

  public Z2MDeviceEntity(Z2MDeviceService deviceService, String ieeeAddress) {
    super();
    this.deviceService = deviceService;

    setEntityID(getEntityPrefix() + ieeeAddress);
    setIeeeAddress(ieeeAddress);
  }

  private static Float toFloat(Integer value) {
    return value == null ? null : value.floatValue();
  }

  @Override
  public @NotNull String getDeviceFullName() {
    return deviceService.getDeviceFullName();
  }

  @UIField(order = 10, disableEdit = true, hideInEdit = true, hideOnEmpty = true)
  @UIFieldColorStatusMatch
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup(value = "STATUS", order = 3, borderColor = "#7ACC2D")
  public @NotNull Status getStatus() {
    String availability = deviceService.getAvailability();
    Status status = Status.UNKNOWN;
    if (availability != null) {
      switch (availability) {
        case "offline" -> status = Status.OFFLINE;
        case "online" -> status = Status.ONLINE;
      }
    }
    if (deviceService.getCoordinatorEntity().getStatus() != Status.ONLINE) {
      status = deviceService.getCoordinatorEntity().getStatus();
    }
    return status;
  }

  @Override
  public @NotNull EntityStatus getEntityStatus() {
    Status status = getStatus();
    ApplianceModel applianceModel = deviceService.getApplianceModel();
    if (applianceModel.isDisabled()) {
      status = Status.DISABLED;
    } else if (applianceModel.isInterviewing()) {
      status = Status.INITIALIZE;
    } else if (deviceService.getApplianceModel().isInterviewFailed()) {
      status = Status.ERROR;
    } else if (!applianceModel.isInterviewCompleted() || !applianceModel.isSupported() ||
               (StringUtils.isEmpty(applianceModel.getType()) || "UNKNOWN".equalsIgnoreCase(applianceModel.getType()))) {
      status = Status.NOT_READY;
    } else {
      DeviceEndpoint endpoint = getDeviceEndpoint(ENDPOINT_FIRMWARE_UPDATE);
      if (endpoint instanceof Z2MDeviceEndpointFirmwareUpdate firmwareUpdate && firmwareUpdate.isUpdating()) {
        status = Status.UPDATING;
      }
    }
    return new EntityStatus(status);
  }

  @Override
  public @NotNull List<ConfigDeviceDefinition> findMatchDeviceConfigurations() {
    return deviceService.findDevices();
  }

  @Override
  public @NotNull Map<String, ? extends DeviceEndpoint> getDeviceEndpoints() {
    return deviceService.getEndpoints();
  }

  @Override
  public String getDescriptionImpl() {
    return defaultIfEmpty(
      getFirstLevelDescription(),
      deviceService.getApplianceModel().getDefinition().getDescription()
    );
  }

  @Override
  @UIField(order = 2, hideOnEmpty = true, inlineEdit = true)
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup("GENERAL")
  @UIFieldNoReadDefaultValue
  public @NotNull String getName() {
    return defaultIfEmpty(deviceService.getApplianceModel().getName(), "UNKNOWN");
  }

  public void setName(String value) {
    deviceService.sendRequest(
      "device/rename", new JSONObject().put("from", getName()).put("to", value).toString());
  }

  @UIField(order = 3, label = "model")
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup("GENERAL")
  @UIFieldNoReadDefaultValue
  public WebAddress getHrefModel() {
    String model = getModel();
    return new WebAddress("https://www.zigbee2mqtt.io/devices/%s.html".formatted(model), model);
  }

  @Override
  public @Nullable String getModel() {
    return deviceService.getModel();
  }

  @Override
  public String getCompactDescriptionImpl() {
    String description = getFirstLevelDescription();
    if (description == null) {
      description = "ZIGBEE.DESCRIPTION.%s~%s".formatted(getModel(), deviceService.getApplianceModel().getDefinition().getDescription());
    }
    return description;
  }

  @UIField(order = 3, disableEdit = true, label = "ieeeAddress")
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup("GENERAL")
  public String getIeeeAddressLabel() {
    return trimToEmpty(getIeeeAddress()).toUpperCase();
  }

  @Override
  @JsonIgnore
  @UIFieldIgnore
  public String getIeeeAddress() {
    return super.getIeeeAddress();
  }

  @UIField(order = 5)
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup("GENERAL")
  @UIFieldNoReadDefaultValue
  public String getNetworkAddress() {
    return "0x" + Integer.toHexString(deviceService.getApplianceModel().getNetworkAddress()).toUpperCase();
  }

  @UIField(order = 15)
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup("GENERAL")
  @UIFieldNoReadDefaultValue
  public String getModelIdentifier() {
    return deviceService.getApplianceModel().getModelId();
  }

  @UIField(order = 20, hideOnEmpty = true)
  @UIFieldGroup("GENERAL")
  @UIFieldNoReadDefaultValue
  public String getCurrentPowerSource() {
    return isCompactMode() ? null : deviceService.getApplianceModel().getPowerSource();
  }

  @UIField(order = 25)
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup("GENERAL")
  @UIFieldNoReadDefaultValue
  public String getLogicalType() {
    return deviceService.getApplianceModel().getType();
  }

  @UIField(order = 35)
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup("GENERAL")
  @UIFieldNoReadDefaultValue
  public boolean getInterviewCompleted() {
    return deviceService.getApplianceModel().isInterviewCompleted();
  }

  @Override
  public String getFirmwareVersion() {
    return deviceService.getApplianceModel().getFirmwareVersion();
  }

  @UIField(order = 2, hideOnEmpty = true)
  @UIFieldGroup("HARDWARE")
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldNoReadDefaultValue
  public String getFirmwareBuildDate() {
    return deviceService.getApplianceModel().getFirmwareBuildDate();
  }

  @UIField(order = 3)
  @UIFieldGroup("HARDWARE")
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldNoReadDefaultValue
  public WebAddress getManufacturer() {
    String vendor = deviceService.getApplianceModel().getDefinition().getVendor();
    return new WebAddress("https://www.zigbee2mqtt.io/supported-devices/#v=%s".formatted(vendor), vendor);
  }

  public boolean isCompactMode() {
    return context().setting().getValue(ZigBeeEntityCompactModeSetting.class);
  }

  @UIField(order = 1, type = SelectBox, color = "#7FE486", inlineEdit = true)
  @UIFieldDynamicSelection(SelectPlaceOptionLoader.class)
  @UIFieldSelectConfig(selectOnEmptyLabel = "PLACEHOLDER.SELECT_PLACE")
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup(value = "SETTINGS", order = 30, borderColor = "#22AB84")
  @UIFieldNoReadDefaultValue
  public String getPlace() {
    return deviceService.getConfiguration().path("place").asText();
  }

  @Override
  public void setPlace(String value) {
    if (!Objects.equals(getPlace(), value)) {
      deviceService.updateConfiguration("place", value);
    }
  }

  @UIField(order = 4, inlineEdit = true)
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup("SETTINGS")
  @UIFieldInlineEditConfirm(value = "Z2M_RETAIN", dialogColor = ERROR_DIALOG)
  @UIFieldNoReadDefaultValue
  public boolean isRetainDeviceMessages() {
    return deviceService.getConfiguration().path("retain").asBoolean(false);
  }

  public void setRetainDeviceMessages(boolean value) {
    deviceService.sendRequest(
      "device/options", new JSONObject().put("id", getIeeeAddress()).put("options", new JSONObject().put("retain", value)).toString());
  }

  @UIField(order = 5, inlineEdit = true)
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup("SETTINGS")
  @UIFieldInlineEditConfirm(value = "ZIGBEE_DISABLE", dialogColor = ERROR_DIALOG, showCondition = "return context.get('disabled') == true")
  @UIFieldNoReadDefaultValue
  public boolean isDisabled() {
    return deviceService.getConfiguration().path("disabled").asBoolean(deviceService.getApplianceModel().isDisabled());
  }

  public void setDisabled(boolean value) {
    deviceService.sendRequest(
      "device/options", new JSONObject().put("id", getIeeeAddress()).put("options", new JSONObject().put("disabled", value)).toString());
  }

  @UIField(order = 6, inlineEdit = true)
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup("SETTINGS")
  @UIFieldNoReadDefaultValue
  public boolean isLogEvents() {
    return deviceService.getConfiguration().path("log_event").asBoolean(false);
  }

  public void setLogEvents(boolean value) {
    deviceService.updateConfiguration("log_event", value);
  }

  @UIField(order = 1, inlineEdit = true)
  @UIFieldSlider(min = 0, max = 10, step = 0.5)
  @UIFieldShowOnCondition("return !context.get('compactMode')")
  @UIFieldGroup("ADVANCED")
  @UIFieldInlineEditConfirm(value = "Z2M_DEBOUNCE", dialogColor = ERROR_DIALOG)
  @UIFieldNoReadDefaultValue
  public float getDebounce() {
    if (deviceService != null) {
      JsonNode deviceOptions = deviceService.getConfiguration();
      if (deviceOptions.has("debounce")) {
        return (float) deviceOptions.get("debounce").asDouble();
      }
    }
    return 0;
  }

  public void setDebounce(Float value) {
    if (!Objects.equals(getDebounce(), value)) {
      deviceService.sendRequest(
        "device/options", new JSONObject().put("id", getIeeeAddress()).put("options", new JSONObject().put("debounce", value)).toString());
    }
  }

  @Override
  public String getImageIdentifierImpl() {
    if (deviceService == null) {
      return super.getImageIdentifierImpl();
    }
    JsonNode deviceOptions = deviceService.getConfiguration();
    return deviceOptions.path("image").asText(getModel()).replaceAll("[/ ]", "_");
  }

  @Override
  public String getFallbackImageIdentifier() {
    return "https://www.zigbee2mqtt.io/images/devices/%s.jpg".formatted(getModel());
  }

  public void setImageIdentifier(String value) {
    deviceService.updateConfiguration("image", trimToNull(Objects.equals(value, getModelIdentifier()) ? null : value));
  }

  @Override
  public void assembleActions(UIInputBuilder uiInputBuilder) {
    List<ConfigDeviceDefinition> deviceDefinitionModels = deviceService.findDevices();
    List<WidgetDefinition> widgetDefinitions = CONFIG_DEVICE_SERVICE.getDeviceWidgets(deviceDefinitionModels);
    context().widget().createTemplateWidgetActions(uiInputBuilder, this, widgetDefinitions);

    uiInputBuilder.addOpenDialogSelectableButton("CUSTOM_DESCRIPTION", new Icon("fas fa-comment"), (context, params) -> {
      String description = params.getString("description");

      if (!Objects.equals(description, getCustomDescription())) {
        deviceService.updateConfiguration("customDescription", description);
        context.ui().updateItem(this);
      }
      return null;
    }).editDialog(dialogBuilder -> {
      dialogBuilder.setTitle("CUSTOM_DESCRIPTION", new Icon("fas fa-comment"));
      dialogBuilder.addFlex("main", flex ->
        flex.addTextInput("description", getCustomDescription(), false));
    });

    Z2MDeviceDefinition definition = deviceService.getApplianceModel().getDefinition();
    if (definition != null) {
      for (Options option : definition.getOptions()) {
        JsonNode deviceConfigurationOptions = deviceService.getConfiguration();
        String flexName = "${z2m.setting.%s~%s}".formatted(option.getName(), splitNameToReadableFormat(option.getName()));
        switch (option.getType()) {
          case ApplianceModel.BINARY_TYPE ->
            buildBinaryTypeAction(uiInputBuilder, option, deviceConfigurationOptions, flexName);
          case ApplianceModel.NUMBER_TYPE ->
            buildNumberTypeAction(uiInputBuilder, option, deviceConfigurationOptions, flexName);
        }
      }
    }
  }

  @Override
  public String getDefaultName() {
    return "Z2M";
  }

  @Override
  public boolean isDisableDelete() {
    return true;
  }

  @Override
  public Boolean isOutdated() {
    DeviceEndpoint endpoint = getDeviceEndpoint(ENDPOINT_FIRMWARE_UPDATE);
    if (endpoint instanceof Z2MDeviceEndpointFirmwareUpdate firmwareUpdate) {
      return firmwareUpdate.isOutdated();
    }
    return false;
  }

  @Override
  protected @NotNull String getDevicePrefix() {
    return PREFIX;
  }

  private void buildNumberTypeAction(UIInputBuilder uiInputBuilder, Options option, JsonNode deviceConfigurationOptions, String flexName) {
    JsonNode deviceOptions = CONFIG_DEVICE_SERVICE.getDeviceOptions(deviceService.findDevices());
    Integer minValue = option.getValueMin() == null ? (deviceOptions.has("min") ? deviceOptions.get("min").asInt() : null) : option.getValueMin();
    Integer maxValue = option.getValueMax() == null ? (deviceOptions.has("max") ? deviceOptions.get("max").asInt() : null) : option.getValueMax();

    Integer nValue = deviceConfigurationOptions == null ? null : deviceConfigurationOptions.path(option.getName()).asInt(0);
    uiInputBuilder.addFlex(option.getName() + "_flex", flex -> {
        flex.addNumberInput(option.getName(), toFloat(nValue), toFloat(minValue), toFloat(maxValue),
          (context, params) -> {
            deviceService.updateDeviceConfiguration(deviceService, option.getName(),
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
    boolean defaultValue = Objects.toString(option.getDescription(), "").contains("(default true)");
    boolean bValue = deviceConfigurationOptions == null ? defaultValue : deviceConfigurationOptions.path(option.getName()).asBoolean(defaultValue);

    uiInputBuilder.addFlex(option.getName() + "_flex", flex -> buildCheckbox(option, bValue, flex))
      .columnFlexDirection()
      .setBorderColor(Color.random())
      .setBorderArea(flexName);
  }

  private void buildCheckbox(Options option, boolean bValue, UIFlexLayoutBuilder flex) {
    flex.addCheckbox(option.getName(), bValue, (context, params) -> {
      deviceService.updateDeviceConfiguration(deviceService, option.getName(), params.getBoolean("value"));
      return null;
    });
    flex.addInfo(option.getDescription()).setOuterClass("context-description");
  }

  private String getCustomDescription() {
    return deviceService.getConfiguration().path("customDescription").asText();
  }

  private String getFirstLevelDescription() {
    ApplianceModel applianceModel = deviceService.getApplianceModel();
    if (applianceModel.isInterviewing()) {
      return "ZIGBEE.INTERVIEWING";
    }
    if (applianceModel.isInterviewFailed()) {
      return "ZIGBEE.INTERVIEW_FAILED";
    }
    return defaultIfEmpty(
      getCustomDescription(),
      applianceModel.getDefinition().getDescription()
    );
  }
}
