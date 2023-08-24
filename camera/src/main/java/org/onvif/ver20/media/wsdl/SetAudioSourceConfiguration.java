package org.onvif.ver20.media.wsdl;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import org.onvif.ver10.schema.AudioSourceConfiguration;

/**
 * Java-Klasse f�r anonymous complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Configuration" type="{http://www.onvif.org/ver10/schema}AudioSourceConfiguration"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"configuration"})
@XmlRootElement(name = "SetAudioSourceConfiguration")
public class SetAudioSourceConfiguration {

    /**
     * -- GETTER --
     *  Ruft den Wert der configuration-Eigenschaft ab.
     *
     * @return possible object is {@link AudioSourceConfiguration }
     */
    @XmlElement(name = "Configuration", required = true)
    protected AudioSourceConfiguration configuration;

    /**
     * Legt den Wert der configuration-Eigenschaft fest.
     *
     * @param value allowed object is {@link AudioSourceConfiguration }
     */
    public void setConfiguration(AudioSourceConfiguration value) {
        this.configuration = value;
    }
}
