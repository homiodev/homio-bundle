package org.touchhome.bundle.camera.workspace;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.workspace.BroadcastLockManager;
import org.touchhome.bundle.api.workspace.WorkspaceBlock;
import org.touchhome.bundle.api.workspace.scratch.BlockType;
import org.touchhome.bundle.api.workspace.scratch.MenuBlock;
import org.touchhome.bundle.api.workspace.scratch.Scratch3Block;
import org.touchhome.bundle.api.workspace.scratch.Scratch3ExtensionBlocks;
import org.touchhome.bundle.camera.CameraEntryPoint;
import org.touchhome.bundle.camera.entity.OnvifCameraEntity;

@Log4j2
@Getter
@Component
public class Scratch3OnvifCameraAlarmBlocks extends Scratch3ExtensionBlocks implements Scratch3BaseOnvif {

    private final BroadcastLockManager broadcastLockManager;

    @Getter
    private final MenuBlock.ServerMenuBlock onvifCameraMenu;
    private final Scratch3Block whenFaceDetectionAlarm;
    private final Scratch3Block whenItemLeftAlarm;
    private final Scratch3Block whenItemTakenAlarm;
    private final Scratch3Block whenTamperAlarm;
    private final Scratch3Block whenTooDarkAlarm;
    private final Scratch3Block whenStorageAlarm;
    private final Scratch3Block whenSceneChangeAlarm;
    private final Scratch3Block whenTooBrightAlarm;
    private final Scratch3Block whenTooBlurryAlarm;

    public Scratch3OnvifCameraAlarmBlocks(EntityContext entityContext, BroadcastLockManager broadcastLockManager,
                                          CameraEntryPoint cameraEntryPoint) {
        super("#8F4D77", entityContext, cameraEntryPoint, "onvifalarm");
        setParent("media");
        this.broadcastLockManager = broadcastLockManager;

        // Menu
        this.onvifCameraMenu = MenuBlock.ofServerItems("cameraMenu", OnvifCameraEntity.class, "Onvif camera");

        // Hats
        this.whenFaceDetectionAlarm = withServerOnvif(Scratch3Block.ofHat(10, "alarm_face_detected",
                "Face detected alarm of [VIDEO_STREAM]", this::whenFaceDetectionAlarmHat));

        this.whenItemLeftAlarm = withServerOnvif(Scratch3Block.ofHat(20, "alarm_item_left",
                "Item left alarm of [VIDEO_STREAM]", this::whenItemLeftAlarmHat));

        this.whenItemTakenAlarm = withServerOnvif(Scratch3Block.ofHat(30, "alarm_item_taken",
                "Item taken alarm of [VIDEO_STREAM]", this::whenItemTakenAlarmHat));

        this.whenTamperAlarm = withServerOnvif(Scratch3Block.ofHat(40, "alarm_tamper",
                "Tamper alarm of [VIDEO_STREAM]", this::whenTamperAlarmHat));

        this.whenTooDarkAlarm = withServerOnvif(Scratch3Block.ofHat(50, "alarm_dark",
                "Too dark alarm of [VIDEO_STREAM]", this::whenTooDarkAlarmHat));

        this.whenStorageAlarm = withServerOnvif(Scratch3Block.ofHat(60, "alarm_storage",
                "Storage alarm of [VIDEO_STREAM]", this::whenStorageAlarmHat));

        this.whenSceneChangeAlarm = withServerOnvif(Scratch3Block.ofHat(70, "alarm_scene",
                "Scene change alarm of [VIDEO_STREAM]", this::whenSceneChangeAlarmHat));

        this.whenTooBrightAlarm = withServerOnvif(Scratch3Block.ofHat(80, "alarm_bright",
                "Too bright alarm of [VIDEO_STREAM]", this::whenTooBrightAlarmHat));

        this.whenTooBlurryAlarm = withServerOnvif(Scratch3Block.ofHandler(90, "alarm_blurry",
                BlockType.hat, "Too bright alarm of [VIDEO_STREAM]", this::whenTooBlurryAlarmHat));
    }

    private void whenTooBlurryAlarmHat(WorkspaceBlock workspaceBlock) {

    }

    private void whenTooBrightAlarmHat(WorkspaceBlock workspaceBlock) {

    }

    private void whenSceneChangeAlarmHat(WorkspaceBlock workspaceBlock) {
    }

    private void whenStorageAlarmHat(WorkspaceBlock workspaceBlock) {

    }

    private void whenTooDarkAlarmHat(WorkspaceBlock workspaceBlock) {
    }

    private void whenTamperAlarmHat(WorkspaceBlock workspaceBlock) {

    }

    private void whenItemTakenAlarmHat(WorkspaceBlock workspaceBlock) {

    }

    private void whenItemLeftAlarmHat(WorkspaceBlock workspaceBlock) {

    }

    private void whenFaceDetectionAlarmHat(WorkspaceBlock workspaceBlock) {

    }
}
