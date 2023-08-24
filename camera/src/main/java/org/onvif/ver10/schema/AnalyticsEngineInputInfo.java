package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import lombok.Getter;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "AnalyticsEngineInputInfo",
        propOrder = {"inputInfo", "extension"})
public class AnalyticsEngineInputInfo {


    @XmlElement(name = "InputInfo")
    protected Config inputInfo;


    @XmlElement(name = "Extension")
    protected AnalyticsEngineInputInfoExtension extension;


    @XmlAnyAttribute
    private final Map<QName, String> otherAttributes = new HashMap<QName, String>();


    public void setInputInfo(Config value) {
        this.inputInfo = value;
    }


    public void setExtension(AnalyticsEngineInputInfoExtension value) {
        this.extension = value;
    }

}
