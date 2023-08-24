package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;

/**
 * Java-Klasse f�r H264Configuration complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="H264Configuration">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="GovLength" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="H264Profile" type="{http://www.onvif.org/ver10/schema}H264Profile"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "H264Configuration",
        propOrder = {"govLength", "h264Profile"})
public class H264Configuration {

    /**
     * -- GETTER --
     *  Ruft den Wert der govLength-Eigenschaft ab.
     */
    @XmlElement(name = "GovLength")
    protected int govLength;

    /**
     * -- GETTER --
     *  Ruft den Wert der h264Profile-Eigenschaft ab.
     *
     * @return possible object is {@link H264Profile }
     */
    @XmlElement(name = "H264Profile", required = true)
    protected H264Profile h264Profile;

    /**
     * Legt den Wert der govLength-Eigenschaft fest.
     */
    public void setGovLength(int value) {
        this.govLength = value;
    }

    /**
     * Legt den Wert der h264Profile-Eigenschaft fest.
     *
     * @param value allowed object is {@link H264Profile }
     */
    public void setH264Profile(H264Profile value) {
        this.h264Profile = value;
    }
}
