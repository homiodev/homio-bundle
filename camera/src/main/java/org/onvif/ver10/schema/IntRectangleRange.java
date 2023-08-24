package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;

/**
 * Range of a rectangle. The rectangle itself is defined by lower left corner position and size. Units are pixel.
 *
 * <p>Java-Klasse f�r IntRectangleRange complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="IntRectangleRange">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="XRange" type="{http://www.onvif.org/ver10/schema}IntRange"/>
 *         <element name="YRange" type="{http://www.onvif.org/ver10/schema}IntRange"/>
 *         <element name="WidthRange" type="{http://www.onvif.org/ver10/schema}IntRange"/>
 *         <element name="HeightRange" type="{http://www.onvif.org/ver10/schema}IntRange"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "IntRectangleRange",
        propOrder = {"xRange", "yRange", "widthRange", "heightRange"})
public class IntRectangleRange {

    @XmlElement(name = "XRange", required = true)
    protected IntRange xRange;

    @XmlElement(name = "YRange", required = true)
    protected IntRange yRange;

    /**
     * -- GETTER --
     *  Ruft den Wert der widthRange-Eigenschaft ab.
     *
     * @return possible object is {@link IntRange }
     */
    @Getter @XmlElement(name = "WidthRange", required = true)
    protected IntRange widthRange;

    /**
     * -- GETTER --
     *  Ruft den Wert der heightRange-Eigenschaft ab.
     *
     * @return possible object is {@link IntRange }
     */
    @Getter @XmlElement(name = "HeightRange", required = true)
    protected IntRange heightRange;

    /**
     * Ruft den Wert der xRange-Eigenschaft ab.
     *
     * @return possible object is {@link IntRange }
     */
    public IntRange getXRange() {
        return xRange;
    }

    /**
     * Legt den Wert der xRange-Eigenschaft fest.
     *
     * @param value allowed object is {@link IntRange }
     */
    public void setXRange(IntRange value) {
        this.xRange = value;
    }

    /**
     * Ruft den Wert der yRange-Eigenschaft ab.
     *
     * @return possible object is {@link IntRange }
     */
    public IntRange getYRange() {
        return yRange;
    }

    /**
     * Legt den Wert der yRange-Eigenschaft fest.
     *
     * @param value allowed object is {@link IntRange }
     */
    public void setYRange(IntRange value) {
        this.yRange = value;
    }

    /**
     * Legt den Wert der widthRange-Eigenschaft fest.
     *
     * @param value allowed object is {@link IntRange }
     */
    public void setWidthRange(IntRange value) {
        this.widthRange = value;
    }

    /**
     * Legt den Wert der heightRange-Eigenschaft fest.
     *
     * @param value allowed object is {@link IntRange }
     */
    public void setHeightRange(IntRange value) {
        this.heightRange = value;
    }
}
