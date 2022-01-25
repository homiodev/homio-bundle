package org.touchhome.bundle.arduino.setting.header;

import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.model.OptionModel;
import org.touchhome.bundle.api.setting.SettingPluginOptions;
import org.touchhome.bundle.api.setting.console.header.ConsoleHeaderSettingPlugin;
import org.touchhome.bundle.api.ui.field.UIFieldType;
import processing.app.BaseNoGui;
import processing.app.debug.TargetBoard;
import processing.app.debug.TargetPackage;
import processing.app.debug.TargetPlatform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConsoleHeaderArduinoGetBoardsSetting implements ConsoleHeaderSettingPlugin<String>,
        SettingPluginOptions<String> {

    @Override
    public Collection<OptionModel> getOptions(EntityContext entityContext) {
        List<OptionModel> options = new ArrayList<>();
        // Cycle through all packages
        if (BaseNoGui.packages != null) {
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

                    OptionModel boardFamily = OptionModel.of(targetPackage.getId() + "~~~" + targetPlatform.getId(), platformLabel);

                    for (TargetBoard board : targetPlatform.getBoards().values()) {
                        if (board.getPreferences().get("hide") != null) {
                            continue;
                        }
                        OptionModel boardType = OptionModel.of(board.getId(), board.getName());
                        boardFamily.addChild(boardType);
                    }
                    if (boardFamily.hasChildren()) {
                        options.add(boardFamily);
                    }
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
    public UIFieldType getSettingType() {
        return UIFieldType.SelectBoxDynamic;
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
