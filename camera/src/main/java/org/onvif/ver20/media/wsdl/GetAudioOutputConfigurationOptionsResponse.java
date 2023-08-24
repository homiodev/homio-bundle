package org.onvif.ver20.media.wsdl;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import org.onvif.ver10.schema.AudioOutputConfigurationOptions;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"options"})
@XmlRootElement(name = "GetAudioOutputConfigurationOptionsResponse")
public class GetAudioOutputConfigurationOptionsResponse {

    /**
     * -- GETTER --
     *  Ruft den Wert der options-Eigenschaft ab.
     *
     * @return possible object is {@link AudioOutputConfigurationOptions }
     */
    @XmlElement(name = "Options", required = true)
    protected AudioOutputConfigurationOptions options;

    /**
     * Legt den Wert der options-Eigenschaft fest.
     *
     * @param value allowed object is {@link AudioOutputConfigurationOptions }
     */
    public void setOptions(AudioOutputConfigurationOptions value) {
        this.options = value;
    }
}
