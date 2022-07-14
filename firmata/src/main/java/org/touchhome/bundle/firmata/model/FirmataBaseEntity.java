package org.touchhome.bundle.firmata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.entity.types.MicroControllerBaseEntity;
import org.touchhome.bundle.api.model.ActionResponseModel;
import org.touchhome.bundle.api.model.FileModel;
import org.touchhome.bundle.api.model.OptionModel;
import org.touchhome.bundle.api.ui.action.DynamicOptionLoader;
import org.touchhome.bundle.api.ui.field.UIField;
import org.touchhome.bundle.api.ui.field.action.UIContextMenuAction;
import org.touchhome.bundle.api.ui.field.selection.UIFieldSelectValueOnEmpty;
import org.touchhome.bundle.api.ui.field.selection.UIFieldSelection;
import org.touchhome.bundle.arduino.ArduinoConsolePlugin;
import org.touchhome.bundle.firmata.provider.FirmataDeviceCommunicator;
import org.touchhome.bundle.firmata.provider.IODeviceWrapper;
import org.touchhome.bundle.firmata.provider.command.FirmataRegisterCommand;
import org.touchhome.bundle.firmata.provider.command.PendingRegistrationContext;
import org.touchhome.common.util.CommonUtils;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Accessors(chain = true)
public abstract class FirmataBaseEntity<T extends FirmataBaseEntity<T>> extends MicroControllerBaseEntity<T> {

    private static final Map<String, FirmataDeviceCommunicator> entityIDToDeviceCommunicator = new HashMap<>();

    @Setter
    @Getter
    @Transient
    @JsonIgnore
    private FirmataDeviceCommunicator firmataDeviceCommunicator;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @UIField(order = 23, readOnly = true)
    public String getBoardType() {
        return getJsonData("boardType");
    }

    public FirmataBaseEntity<T> setBoardType(String boardType) {
        return setJsonData("boardType", boardType);
    }

    @Override
    @UIField(order = 100, inlineEdit = true)
    @UIFieldSelection(SelectTargetFirmataDeviceLoader.class)
    @UIFieldSelectValueOnEmpty(label = "selection.target", color = "#A7D21E")
    public String getIeeeAddress() {
        return super.getIeeeAddress();
    }

    @UIContextMenuAction("RESTART")
    public ActionResponseModel restartCommunicator() {
        if (firmataDeviceCommunicator != null) {
            try {
                String response = firmataDeviceCommunicator.restart();
                return ActionResponseModel.showSuccess(response);
            } catch (Exception ex) {
                return ActionResponseModel.showError(ex);
            }
        }
        return ActionResponseModel.showWarn("ACTION.COMMUNICATOR.NOT_FOUND");
    }

    @UIContextMenuAction("UPLOAD_SKETCH")
    public void uploadSketch(EntityContext entityContext) {

    }

    @UIContextMenuAction("UPLOAD_SKETCH_MANUALLY")
    public void uploadSketchManually(EntityContext entityContext) {
        ArduinoConsolePlugin arduinoConsolePlugin = entityContext.getBean(ArduinoConsolePlugin.class);
        String content = CommonUtils.getResourceAsString("firmata", "arduino_firmata.ino");
        String commName = this.getCommunicatorName();
        String sketch = "#define COMM_" + commName + "\n" + content;
        arduinoConsolePlugin.save(new FileModel("arduino_firmata_" + commName + ".ino", sketch, null, false));
        arduinoConsolePlugin.syncContentToUI();
        entityContext.ui().openConsole(arduinoConsolePlugin);
    }

    protected abstract String getCommunicatorName();

    @JsonIgnore
    public short getTarget() {
        return getIeeeAddress() == null ? -1 : Short.parseShort(getIeeeAddress());
    }

    public void setTarget(short target) {
        setIeeeAddress(Short.toString(target));
    }

    @Override
    public String getDefaultName() {
        return "Firmata";
    }

    @Override
    public int getOrder() {
        return 20;
    }

    public abstract FirmataDeviceCommunicator createFirmataDeviceType(EntityContext entityContext);

    @JsonIgnore
    public final IODeviceWrapper getDevice() {
        return firmataDeviceCommunicator.getDevice();
    }

    public long getUniqueID() {
        return getJsonData("uniqueID", 0L);
    }

    public FirmataBaseEntity<T> setUniqueID(Long uniqueID) {
        setJsonData("uniqueID", uniqueID);
        return this;
    }

    protected abstract boolean allowRegistrationType(PendingRegistrationContext pendingRegistrationContext);

    @Override
    public void afterFetch(EntityContext entityContext) {
        setFirmataDeviceCommunicator(entityIDToDeviceCommunicator.computeIfAbsent(getEntityID(),
                ignore -> createFirmataDeviceType(entityContext)));
    }

    @Override
    protected void beforeDelete() {
        FirmataDeviceCommunicator firmataDeviceCommunicator = entityIDToDeviceCommunicator.remove(getEntityID());
        if (firmataDeviceCommunicator != null) {
            firmataDeviceCommunicator.destroy();
        }
    }

    public static class SelectTargetFirmataDeviceLoader implements DynamicOptionLoader {

        @Override
        public List<OptionModel> loadOptions(DynamicOptionLoaderParameters parameters) {
            return parameters.getEntityContext().getBean(FirmataRegisterCommand.class).getPendingRegistrations().entrySet().stream()
                    .filter(entry -> ((FirmataBaseEntity) parameters.getBaseEntity()).allowRegistrationType(entry.getValue()))
                    .map(entry -> OptionModel.of(Short.toString(entry.getKey()), entry.getKey() + "/" + entry.getValue()))
                    .collect(Collectors.toList());
        }
    }
}
