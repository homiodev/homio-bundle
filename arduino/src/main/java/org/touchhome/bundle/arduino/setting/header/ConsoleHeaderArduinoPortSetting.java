package org.touchhome.bundle.arduino.setting.header;

import com.fazecast.jSerialComm.SerialPort;
import org.touchhome.bundle.api.setting.SettingPluginOptionsPort;
import org.touchhome.bundle.api.setting.console.header.ConsoleHeaderSettingPlugin;

public class ConsoleHeaderArduinoPortSetting implements ConsoleHeaderSettingPlugin<SerialPort>, SettingPluginOptionsPort {

    @Override
    public Integer getMaxWidth() {
        return 155;
    }

    @Override
    public String getIcon() {
        return "fas fa-project-diagram";
    }

    @Override
    public boolean withEmpty() {
        return false;
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
