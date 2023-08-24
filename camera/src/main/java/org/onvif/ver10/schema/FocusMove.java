package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;

/**
 * Java-Klasse f�r FocusMove complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="FocusMove">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Absolute" type="{http://www.onvif.org/ver10/schema}AbsoluteFocus" minOccurs="0"/>
 *         <element name="Relative" type="{http://www.onvif.org/ver10/schema}RelativeFocus" minOccurs="0"/>
 *         <element name="Continuous" type="{http://www.onvif.org/ver10/schema}ContinuousFocus" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "FocusMove",
        propOrder = {"absolute", "relative", "continuous"})
public class FocusMove {

    /**
     * -- GETTER --
     *  Ruft den Wert der absolute-Eigenschaft ab.
     *
     * @return possible object is {@link AbsoluteFocus }
     */
    @XmlElement(name = "Absolute")
    protected AbsoluteFocus absolute;

    /**
     * -- GETTER --
     *  Ruft den Wert der relative-Eigenschaft ab.
     *
     * @return possible object is {@link RelativeFocus }
     */
    @XmlElement(name = "Relative")
    protected RelativeFocus relative;

    /**
     * -- GETTER --
     *  Ruft den Wert der continuous-Eigenschaft ab.
     *
     * @return possible object is {@link ContinuousFocus }
     */
    @XmlElement(name = "Continuous")
    protected ContinuousFocus continuous;

    /**
     * Legt den Wert der absolute-Eigenschaft fest.
     *
     * @param value allowed object is {@link AbsoluteFocus }
     */
    public void setAbsolute(AbsoluteFocus value) {
        this.absolute = value;
    }

    /**
     * Legt den Wert der relative-Eigenschaft fest.
     *
     * @param value allowed object is {@link RelativeFocus }
     */
    public void setRelative(RelativeFocus value) {
        this.relative = value;
    }

    /**
     * Legt den Wert der continuous-Eigenschaft fest.
     *
     * @param value allowed object is {@link ContinuousFocus }
     */
    public void setContinuous(ContinuousFocus value) {
        this.continuous = value;
    }
}
