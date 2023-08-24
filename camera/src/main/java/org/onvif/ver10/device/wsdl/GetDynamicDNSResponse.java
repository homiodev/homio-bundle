







package org.onvif.ver10.device.wsdl;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import org.onvif.ver10.schema.DynamicDNSInformation;


@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"dynamicDNSInformation"})
@XmlRootElement(name = "GetDynamicDNSResponse")
public class GetDynamicDNSResponse {


    @XmlElement(name = "DynamicDNSInformation", required = true)
    protected DynamicDNSInformation dynamicDNSInformation;


    public void setDynamicDNSInformation(DynamicDNSInformation value) {
        this.dynamicDNSInformation = value;
    }
}
