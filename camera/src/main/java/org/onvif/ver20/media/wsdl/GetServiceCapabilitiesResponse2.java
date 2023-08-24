package org.onvif.ver20.media.wsdl;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"capabilities"})
@XmlRootElement(name = "GetServiceCapabilitiesResponse2")
public class GetServiceCapabilitiesResponse2 {

    /**
     * -- GETTER --
     *  Ruft den Wert der capabilities-Eigenschaft ab.
     *
     * @return possible object is {@link Capabilities2 }
     */
    @XmlElement(name = "Capabilities", required = true)
    protected Capabilities2 capabilities;

    /**
     * Legt den Wert der capabilities-Eigenschaft fest.
     *
     * @param value allowed object is {@link Capabilities2 }
     */
    public void setCapabilities(Capabilities2 value) {
        this.capabilities = value;
    }
}
