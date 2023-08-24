package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;

import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Java-Klasse f�r PTZPresetTourStartingCondition complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="PTZPresetTourStartingCondition">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="RecurringTime" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         <element name="RecurringDuration" type="{http://www.w3.org/2001/XMLSchema}duration" minOccurs="0"/>
 *         <element name="Direction" type="{http://www.onvif.org/ver10/schema}PTZPresetTourDirection" minOccurs="0"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}PTZPresetTourStartingConditionExtension" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "PTZPresetTourStartingCondition",
        propOrder = {"recurringTime", "recurringDuration", "direction", "extension"})
public class PTZPresetTourStartingCondition {

    /**
     * -- GETTER --
     *  Ruft den Wert der recurringTime-Eigenschaft ab.
     *
     * @return possible object is {@link Integer }
     */
    @XmlElement(name = "RecurringTime")
    protected Integer recurringTime;

    /**
     * -- GETTER --
     *  Ruft den Wert der recurringDuration-Eigenschaft ab.
     *
     * @return possible object is {@link Duration }
     */
    @XmlElement(name = "RecurringDuration")
    protected Duration recurringDuration;

    /**
     * -- GETTER --
     *  Ruft den Wert der direction-Eigenschaft ab.
     *
     * @return possible object is {@link PTZPresetTourDirection }
     */
    @XmlElement(name = "Direction")
    protected PTZPresetTourDirection direction;

    /**
     * -- GETTER --
     *  Ruft den Wert der extension-Eigenschaft ab.
     *
     * @return possible object is {@link PTZPresetTourStartingConditionExtension }
     */
    @XmlElement(name = "Extension")
    protected PTZPresetTourStartingConditionExtension extension;

    /**
     * -- GETTER --
     *  Gets a map that contains attributes that aren't bound to any typed property on this class.
     *  <p>the map is keyed by the name of the attribute and the value is the string value of the
     *  attribute.
     *  <p>the map returned by this method is live, and you can add new attribute by updating the map
     *  directly. Because of this design, there's no setter.
     *
     * @return always non-null
     */
    @XmlAnyAttribute
    private final Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Legt den Wert der recurringTime-Eigenschaft fest.
     *
     * @param value allowed object is {@link Integer }
     */
    public void setRecurringTime(Integer value) {
        this.recurringTime = value;
    }

    /**
     * Legt den Wert der recurringDuration-Eigenschaft fest.
     *
     * @param value allowed object is {@link Duration }
     */
    public void setRecurringDuration(Duration value) {
        this.recurringDuration = value;
    }

    /**
     * Legt den Wert der direction-Eigenschaft fest.
     *
     * @param value allowed object is {@link PTZPresetTourDirection }
     */
    public void setDirection(PTZPresetTourDirection value) {
        this.direction = value;
    }

    /**
     * Legt den Wert der extension-Eigenschaft fest.
     *
     * @param value allowed object is {@link PTZPresetTourStartingConditionExtension }
     */
    public void setExtension(PTZPresetTourStartingConditionExtension value) {
        this.extension = value;
    }

}
