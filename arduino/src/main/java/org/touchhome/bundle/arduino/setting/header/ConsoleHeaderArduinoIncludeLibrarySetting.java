package org.touchhome.bundle.arduino.setting.header;

import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.model.OptionModel;
import org.touchhome.bundle.api.setting.SettingPluginOptions;
import org.touchhome.bundle.api.setting.console.header.ConsoleHeaderSettingPlugin;
import org.touchhome.bundle.arduino.avr.LibraryOfSameTypeComparator;
import processing.app.BaseNoGui;
import processing.app.debug.TargetPlatform;
import processing.app.packages.LibraryList;
import processing.app.packages.UserLibrary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConsoleHeaderArduinoIncludeLibrarySetting implements ConsoleHeaderSettingPlugin<String>,
        SettingPluginOptions<String> {

    @Override
    public Collection<OptionModel> getOptions(EntityContext entityContext) {
        List<OptionModel> options = new ArrayList<>();

        TargetPlatform targetPlatform = BaseNoGui.packages == null ? null : BaseNoGui.getTargetPlatform();
        if (targetPlatform != null) {
            LibraryList libs = getSortedLibraries();
            String lastLibType = null;
            for (UserLibrary lib : libs) {
                String libType = lib.getTypes().get(0);
                if (!libType.equals(lastLibType)) {
                    if (lastLibType != null) {
                        options.add(OptionModel.separator());
                    }
                    lastLibType = libType;
                }
                options.add(OptionModel.key(lib.getName()));
            }
        }

        return options;
    }

    private LibraryList getSortedLibraries() {
        LibraryList installedLibraries = BaseNoGui.librariesIndexer.getInstalledLibraries();
        installedLibraries.sort(new LibraryOfSameTypeComparator());
        return installedLibraries;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String getIcon() {
        return "fas fa-plus";
    }

    @Override
    public SettingType getSettingType() {
        return SettingType.SelectBoxButton;
    }
}
