







package org.onvif.ver10.media.wsdl;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;


@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"videoSourceToken"})
@XmlRootElement(name = "GetVideoSourceModes")
public class GetVideoSourceModes {


    @XmlElement(name = "VideoSourceToken", required = true)
    protected String videoSourceToken;


    public void setVideoSourceToken(String value) {
        this.videoSourceToken = value;
    }
}
