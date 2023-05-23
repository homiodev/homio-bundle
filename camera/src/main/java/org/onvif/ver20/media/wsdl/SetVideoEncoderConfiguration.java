package org.onvif.ver20.media.wsdl;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.onvif.ver10.schema.VideoEncoder2Configuration;

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
 *         &lt;element name="Configuration" type="{http://www.onvif.org/ver10/schema}VideoEncoder2Configuration"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"configuration"})
@XmlRootElement(name = "SetVideoEncoderConfiguration")
public class SetVideoEncoderConfiguration {

    @XmlElement(name = "Configuration", required = true)
    protected VideoEncoder2Configuration configuration;

    /**
     * Ruft den Wert der configuration-Eigenschaft ab.
     *
     * @return possible object is {@link VideoEncoder2Configuration }
     */
    public VideoEncoder2Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Legt den Wert der configuration-Eigenschaft fest.
     *
     * @param value allowed object is {@link VideoEncoder2Configuration }
     */
    public void setConfiguration(VideoEncoder2Configuration value) {
        this.configuration = value;
    }
}
