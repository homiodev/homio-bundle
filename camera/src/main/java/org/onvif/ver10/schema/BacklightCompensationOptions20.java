package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "BacklightCompensationOptions20",
        propOrder = {"mode", "level"})
public class BacklightCompensationOptions20 {

    @XmlElement(name = "Mode", required = true)
    protected List<BacklightCompensationMode> mode;


    @Getter @XmlElement(name = "Level")
    protected FloatRange level;


    public List<BacklightCompensationMode> getMode() {
        if (mode == null) {
            mode = new ArrayList<BacklightCompensationMode>();
        }
        return this.mode;
    }


    public void setLevel(FloatRange value) {
        this.level = value;
    }
}
