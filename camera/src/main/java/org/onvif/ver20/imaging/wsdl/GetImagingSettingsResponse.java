package org.onvif.ver20.imaging.wsdl;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.onvif.ver10.schema.ImagingSettings20;

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
 *         &lt;element name="ImagingSettings" type="{http://www.onvif.org/ver10/schema}ImagingSettings20"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"imagingSettings"})
@XmlRootElement(name = "GetImagingSettingsResponse")
public class GetImagingSettingsResponse {

    @XmlElement(name = "ImagingSettings", required = true)
    protected ImagingSettings20 imagingSettings;

    /**
     * Ruft den Wert der imagingSettings-Eigenschaft ab.
     *
     * @return possible object is {@link ImagingSettings20 }
     */
    public ImagingSettings20 getImagingSettings() {
        return imagingSettings;
    }

    /**
     * Legt den Wert der imagingSettings-Eigenschaft fest.
     *
     * @param value allowed object is {@link ImagingSettings20 }
     */
    public void setImagingSettings(ImagingSettings20 value) {
        this.imagingSettings = value;
    }
}
