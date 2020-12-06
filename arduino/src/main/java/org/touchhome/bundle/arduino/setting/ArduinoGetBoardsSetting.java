package org.touchhome.bundle.arduino.setting;

import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.json.Option;
import org.touchhome.bundle.api.setting.header.BundleHeaderSettingPlugin;
import processing.app.BaseNoGui;
import processing.app.debug.TargetBoard;
import processing.app.debug.TargetPackage;
import processing.app.debug.TargetPlatform;
import processing.app.helpers.PreferencesMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ArduinoGetBoardsSetting implements BundleHeaderSettingPlugin<String> {

    @Override
    public Collection<Option> loadAvailableValues(EntityContext entityContext) {
        List<Option> options = new ArrayList<>();
        // Cycle through all packages
        for (TargetPackage targetPackage : BaseNoGui.packages.values()) {
            // For every package cycle through all platform
            for (TargetPlatform targetPlatform : targetPackage.platforms()) {

                // Add a title for each platform
                String platformLabel = targetPlatform.getPreferences().get("name");
                if (platformLabel == null)
                    platformLabel = targetPackage.getId() + "-" + targetPlatform.getId();

                // add an hint that this core lives in sketchbook
                if (targetPlatform.isInSketchbook())
                    platformLabel += " (in sketchbook)";

                Option boardFamily = new Option(targetPlatform.getId(), platformLabel);

                for (TargetBoard board : targetPlatform.getBoards().values()) {
                    if (board.getPreferences().get("hide") != null) {
                        continue;
                    }
                    Option boardType = Option.of(board.getId(), board.getName());
                    boardFamily.addChild(boardType);

                    if (!board.getMenuIds().isEmpty()) {
                        for (String subMenuId : board.getMenuIds()) {
                            PreferencesMap preferencesMap = board.getMenuLabels(subMenuId);
                            for (Map.Entry<String, String> entry : preferencesMap.entrySet()) {
                                boardType.addChild(Option.of(entry.getKey(), entry.getValue()));
                            }
                        }
                    }
                }
                if (boardFamily.getChildren() != null) {
                    options.add(boardFamily);
                }
            }
        }
        return options;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public Integer getMaxWidth() {
        return 150;
    }

    @Override
    public String getIcon() {
        return "fab fa-flipboard";
    }

    @Override
    public SettingType getSettingType() {
        return SettingType.SelectBoxDynamic;
    }

    @Override
    public int order() {
        return 100;
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
