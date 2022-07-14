package org.touchhome.bundle.firmata.workspace;

import com.pivovarit.function.ThrowingBiConsumer;
import com.pivovarit.function.ThrowingBiFunction;
import com.pivovarit.function.ThrowingConsumer;
import com.pivovarit.function.ThrowingFunction;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.firmata4j.Pin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.touchhome.bundle.api.BundleEntryPoint;
import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.model.Status;
import org.touchhome.bundle.api.workspace.BroadcastLock;
import org.touchhome.bundle.api.workspace.BroadcastLockManager;
import org.touchhome.bundle.api.workspace.WorkspaceBlock;
import org.touchhome.bundle.api.workspace.scratch.MenuBlock;
import org.touchhome.bundle.api.workspace.scratch.Scratch3Block;
import org.touchhome.bundle.api.workspace.scratch.Scratch3ExtensionBlocks;
import org.touchhome.bundle.firmata.model.FirmataBaseEntity;

import java.util.concurrent.TimeUnit;

@Log4j2
public abstract class Scratch3FirmataBaseBlock extends Scratch3ExtensionBlocks {
    public static final String FIRMATA_ID_MENU = "firmataIdMenu";
    public static final String REST_PIN = "rest/firmata/pin/";
    public static final String PIN = "PIN";
    static final String FIRMATA = "FIRMATA";
    static final String VALUE = "VALUE";

    final EntityContext entityContext;
    final MenuBlock.ServerMenuBlock firmataIdMenu;
    final BroadcastLockManager broadcastLockManager;

    public Scratch3FirmataBaseBlock(String color, EntityContext entityContext, BundleEntryPoint bundleEntryPoint,
                                    BroadcastLockManager broadcastLockManager, String idSuffix) {
        super(color, entityContext, bundleEntryPoint, idSuffix);
        this.entityContext = entityContext;
        this.broadcastLockManager = broadcastLockManager;
        this.firmataIdMenu = MenuBlock.ofServerItems(FIRMATA_ID_MENU, FirmataBaseEntity.class, "Firmata");
    }

    static Integer getPin(WorkspaceBlock workspaceBlock, MenuBlock.ServerMenuBlock menuBlock) {
        String pinNum = workspaceBlock.getMenuValue(PIN, menuBlock);
        return Integer.valueOf(pinNum);
    }

    Scratch3Block of(Scratch3Block scratch3Block) {
        return of(scratch3Block, null);
    }

    Scratch3Block of(Scratch3Block scratch3Block, String overrideColor) {
        scratch3Block.addArgument(FIRMATA, this.firmataIdMenu);
        scratch3Block.overrideColor(overrideColor);
        return scratch3Block;
    }

    Scratch3Block ofPin(Scratch3Block scratch3Block, MenuBlock.ServerMenuBlock pinMenuBlock) {
        return ofPin(scratch3Block, pinMenuBlock, null);
    }

    Scratch3Block ofPin(Scratch3Block scratch3Block, MenuBlock.ServerMenuBlock pinMenuBlock, String overrideColor) {
        scratch3Block.addArgument(FIRMATA, this.firmataIdMenu);
        scratch3Block.addArgument(PIN, pinMenuBlock);
        scratch3Block.overrideColor(overrideColor);
        return scratch3Block;
    }

    @SneakyThrows
    <T> T execute(WorkspaceBlock workspaceBlock, boolean waitDeviceForReady, ThrowingFunction<FirmataBaseEntity, T, Exception> consumer) {
        FirmataBaseEntity entity = workspaceBlock.getMenuValueEntity(FIRMATA, this.firmataIdMenu);

        if (entity != null && entity.getJoined() == Status.ONLINE) {
            return consumer.apply(entity);
        }
        return null;
    }

    @SneakyThrows
    void execute(WorkspaceBlock workspaceBlock, boolean waitDeviceForReady, ThrowingConsumer<FirmataBaseEntity, Exception> consumer) {
        execute(workspaceBlock, waitDeviceForReady, (ThrowingFunction<FirmataBaseEntity, Void, Exception>) entity -> {
            consumer.accept(entity);
            return null;
        });
    }

    void executeNoResponse(WorkspaceBlock workspaceBlock, boolean waitDeviceForReady, MenuBlock.ServerMenuBlock pinMenuBlock, ThrowingBiConsumer<FirmataBaseEntity, Pin, Exception> consumer) {
        execute(workspaceBlock, waitDeviceForReady, pinMenuBlock, (entity, pin) -> {
            consumer.accept(entity, pin);
            return null;
        });
    }

    @SneakyThrows
    <T> T execute(@NotNull WorkspaceBlock workspaceBlock, boolean waitDeviceForReady, @Nullable MenuBlock.ServerMenuBlock pinMenuBlock, @NotNull ThrowingBiFunction<FirmataBaseEntity, Pin, T, Exception> consumer) {
        Integer pinNum = pinMenuBlock == null ? null : getPin(workspaceBlock, pinMenuBlock);
        String deviceId = workspaceBlock.getMenuValue(FIRMATA, this.firmataIdMenu);
        FirmataBaseEntity entity = entityContext.getEntity(deviceId);
        if (entity == null || entity.getDevice() == null) {
            return null;
        }

        if (waitDeviceForReady && entity.getJoined() != Status.ONLINE) {
            BroadcastLock readyLock = broadcastLockManager.getOrCreateLock(workspaceBlock, "firmata_ready_" + entity.getTarget());
            if (readyLock.await(workspaceBlock, 60, TimeUnit.SECONDS)) {
                // fetch updated entity
                entity = entityContext.getEntity(deviceId);
                if (entity.getJoined() == Status.ONLINE) {
                    return consumer.apply(entity, pinNum == null ? null : entity.getDevice().getIoDevice().getPin(pinNum));
                } else {
                    log.error("Unable to execute step for firmata entity: <{}>. Waited for ready status but got: <{}>", entity.getTitle(), entity.getStatus());
                }
            }
        } else {
            if (entity.getJoined() == Status.ONLINE) {
                return consumer.apply(entity, pinNum == null ? null : entity.getDevice().getIoDevice().getPin(pinNum));
            }
        }
        return null;
    }
}
