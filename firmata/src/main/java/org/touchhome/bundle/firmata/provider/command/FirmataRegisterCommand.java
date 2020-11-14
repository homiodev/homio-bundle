package org.touchhome.bundle.firmata.provider.command;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.model.Status;
import org.touchhome.bundle.api.workspace.BroadcastLockManager;
import org.touchhome.bundle.firmata.model.FirmataBaseEntity;
import org.touchhome.bundle.firmata.provider.IODeviceWrapper;
import org.touchhome.bundle.firmata.provider.util.THUtil;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.touchhome.bundle.firmata.provider.command.FirmataCommand.SYSEX_REGISTER;

@Log4j2
@Component
public class FirmataRegisterCommand implements FirmataCommandPlugin {

    private final EntityContext entityContext;

    @Getter
    private final Map<Short, PendingRegistrationContext> pendingRegistrations = new HashMap<>();

    public FirmataRegisterCommand(EntityContext entityContext) {
        this.entityContext = entityContext;
    }

    @Override
    public FirmataCommand getCommand() {
        return SYSEX_REGISTER;
    }

    @Override
    public boolean isHandleBroadcastEvents() {
        return true;
    }

    @Override
    public boolean hasTH() {
        return true;
    }

    @Override
    public void handle(IODeviceWrapper device, FirmataBaseEntity entity, byte messageID, ByteBuffer payload) {
        log.warn("Found firmata device with target <{}>", entity.getTarget());
        long uniqueID = entity.getUniqueID() == 0 ? device.generateUniqueIDOnRegistrationSuccess() : entity.getUniqueID();
        String boardType = THUtil.getString(payload, null);

        entityContext.updateDelayed(entity, t -> t.setBoardType(boardType).setUniqueID(uniqueID).setJoined(Status.ONLINE).setStatus(Status.ONLINE));
        entityContext.getBean(BroadcastLockManager.class).signalAll("firmata-ready-" + entity.getTarget());
        device.sendMessage(SYSEX_REGISTER, uniqueID);
    }

    @Override
    public void broadcastHandle(IODeviceWrapper device, FirmataBaseEntity entity, byte messageID, short target, ByteBuffer payload) {
        if (entity.getTarget() == target) {
            handle(device, entity, messageID, payload);
        } else {
            log.info("Got registering slave device: <{}>", target);
            pendingRegistrations.put(target, new PendingRegistrationContext(entity, target, THUtil.getString(payload, null)));
        }
    }
}
