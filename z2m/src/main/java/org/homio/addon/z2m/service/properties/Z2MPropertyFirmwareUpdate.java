package org.homio.addon.z2m.service.properties;

import com.fasterxml.jackson.databind.JsonNode;
import org.homio.addon.z2m.service.Z2MDeviceService;
import org.homio.addon.z2m.service.Z2MProperty;
import org.homio.addon.z2m.util.ApplianceModel;
import org.homio.api.model.ActionResponseModel;
import org.homio.api.model.Icon;
import org.homio.api.state.JsonType;
import org.homio.api.ui.UI.Color;
import org.homio.api.ui.field.action.v1.UIInputBuilder;
import org.homio.api.util.FlowMap;
import org.homio.api.util.Lang;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Z2MPropertyFirmwareUpdate extends Z2MProperty {

    private boolean wasProgress;

    public Z2MPropertyFirmwareUpdate() {
        super(new Icon("fa fa-fw fa-tablets", "#FF0000"));
        setValue(new JsonType("{}"));
    }

    @Override
    public void init(@NotNull Z2MDeviceService deviceService, @NotNull ApplianceModel.Z2MDeviceDefinition.Options expose, boolean createVariable) {
        expose.setType(ApplianceModel.COMPOSITE_TYPE);
        super.init(deviceService, expose, createVariable);

        // listen for changes
        addChangeListener("internal", ignore -> {
            JsonNode node = ((JsonType) getValue()).getJsonNode();
            String status = node.get("state").asText();
            if ("updating".equals(status)) {
                wasProgress = true;
                String message = Lang.getServerMessage("ZIGBEE.UPDATING",
                    FlowMap.of("NAME", getDeviceService().getDeviceEntity().getTitle(), "VALUE", node.get("remaining").asInt()));
                getEntityContext().ui().progress("upd-" + getDeviceService().getIeeeAddress(),
                    node.get("progress").asDouble(), message, false);
            } else if (wasProgress) {
                getEntityContext().ui().progressDone("upd-" + getDeviceService().getIeeeAddress());
            }
        });
    }

    @Override
    public String getPropertyDefinition() {
        return PROPERTY_FIRMWARE_UPDATE;
    }

    @Override
    public void buildZigbeeAction(UIInputBuilder uiInputBuilder, String entityID) {
        JsonNode node = ((JsonType) getValue()).getJsonNode();
        switch (node.get("state").asText()) {
            case "available" -> {
                String updateTitle = node.get("installed_version").asText() + "=>" + node.get("latest_version").asText();
                uiInputBuilder.addButton(entityID, new Icon("fas fa-retweet", "#FF0000"),
                                  (entityContext, params) -> sendRequest(Request.update))
                              .setText(updateTitle)
                              .setConfirmMessage("W.CONFIRM.ZIGBEE_UPDATE")
                              .setConfirmMessageDialogColor(Color.ERROR_DIALOG);
            }
            case "updating" -> uiInputBuilder.addInfo("UPDATING");
            case "idle" -> uiInputBuilder.addButton(entityID, new Icon("fas fa-check-to-slot", "#72A7A1"),
                                             (entityContext, params) -> sendRequest(Request.check))
                                         .setText("ZIGBEE.CHECK_UPDATES")
                                         .setConfirmMessage("W.CONFIRM.ZIGBEE_CHECK_UPDATES");
            default -> uiInputBuilder.addInfo("-");
        }
    }

    public Boolean isOutdated() {
        JsonNode node = ((JsonType) getValue()).getJsonNode();
        return node.path("state").asText().equals("available");
    }

    public Boolean isUpdating() {
        JsonNode node = ((JsonType) getValue()).getJsonNode();
        return node.path("state").asText().equals("updating");
    }

    private ActionResponseModel sendRequest(Request request) {
        getDeviceService().getCoordinatorService().publish("bridge/request/device/ota_update/" + request.name(),
            new JSONObject().put("id", getDeviceService().getIeeeAddress()));
        return ActionResponseModel.showSuccess(Lang.getServerMessage("ZIGBEE.REQUEST_UPDATE_" + request.name().toUpperCase(),
            getDeviceService().getIeeeAddress()));
    }

    private enum Request {
        update, check
    }
}
