package org.touchhome.bundle.arduino.setting;

import com.fazecast.jSerialComm.SerialPort;
import org.touchhome.bundle.api.setting.BundleSettingPluginPort;
import org.touchhome.bundle.api.setting.header.BundleHeaderSettingPlugin;

public class ArduinoPortSetting implements BundleHeaderSettingPlugin<SerialPort>, BundleSettingPluginPort {

    @Override
    public Integer getMaxWidth() {
        return 155;
    }

    @Override
    public String getIcon() {
        return "fas fa-project-diagram";
    }

    @Override
    public int order() {
        return 100;
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
