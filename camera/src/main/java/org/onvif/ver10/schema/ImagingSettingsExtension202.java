package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * Java-Klasse f�r ImagingSettingsExtension202 complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="ImagingSettingsExtension202">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="IrCutFilterAutoAdjustment" type="{http://www.onvif.org/ver10/schema}IrCutFilterAutoAdjustment" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}ImagingSettingsExtension203" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ImagingSettingsExtension202",
        propOrder = {"irCutFilterAutoAdjustment", "extension"})
public class ImagingSettingsExtension202 {

    @XmlElement(name = "IrCutFilterAutoAdjustment")
    protected List<IrCutFilterAutoAdjustment> irCutFilterAutoAdjustment;

    /**
     * -- GETTER --
     *  Ruft den Wert der extension-Eigenschaft ab.
     *
     * @return possible object is {@link ImagingSettingsExtension203 }
     */
    @Getter @XmlElement(name = "Extension")
    protected ImagingSettingsExtension203 extension;

    /**
     * Gets the value of the irCutFilterAutoAdjustment property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the
     * irCutFilterAutoAdjustment property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getIrCutFilterAutoAdjustment().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link IrCutFilterAutoAdjustment }
     */
    public List<IrCutFilterAutoAdjustment> getIrCutFilterAutoAdjustment() {
        if (irCutFilterAutoAdjustment == null) {
            irCutFilterAutoAdjustment = new ArrayList<IrCutFilterAutoAdjustment>();
        }
        return this.irCutFilterAutoAdjustment;
    }

    /**
     * Legt den Wert der extension-Eigenschaft fest.
     *
     * @param value allowed object is {@link ImagingSettingsExtension203 }
     */
    public void setExtension(ImagingSettingsExtension203 value) {
        this.extension = value;
    }
}
