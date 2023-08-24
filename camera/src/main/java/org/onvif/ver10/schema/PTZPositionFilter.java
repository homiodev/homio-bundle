package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Java-Klasse f�r PTZPositionFilter complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="PTZPositionFilter">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="MinPosition" type="{http://www.onvif.org/ver10/schema}PTZVector"/>
 *         <element name="MaxPosition" type="{http://www.onvif.org/ver10/schema}PTZVector"/>
 *         <element name="EnterOrExit" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "PTZPositionFilter",
        propOrder = {"minPosition", "maxPosition", "enterOrExit", "any"})
public class PTZPositionFilter {

    /**
     * -- GETTER --
     *  Ruft den Wert der minPosition-Eigenschaft ab.
     *
     * @return possible object is {@link PTZVector }
     */
    @Getter @XmlElement(name = "MinPosition", required = true)
    protected PTZVector minPosition;

    /**
     * -- GETTER --
     *  Ruft den Wert der maxPosition-Eigenschaft ab.
     *
     * @return possible object is {@link PTZVector }
     */
    @Getter @XmlElement(name = "MaxPosition", required = true)
    protected PTZVector maxPosition;

    /**
     * -- GETTER --
     *  Ruft den Wert der enterOrExit-Eigenschaft ab.
     */
    @Getter @XmlElement(name = "EnterOrExit")
    protected boolean enterOrExit;

    @XmlAnyElement(lax = true)
    protected List<java.lang.Object> any;

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
    @Getter @XmlAnyAttribute
    private final Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Legt den Wert der minPosition-Eigenschaft fest.
     *
     * @param value allowed object is {@link PTZVector }
     */
    public void setMinPosition(PTZVector value) {
        this.minPosition = value;
    }

    /**
     * Legt den Wert der maxPosition-Eigenschaft fest.
     *
     * @param value allowed object is {@link PTZVector }
     */
    public void setMaxPosition(PTZVector value) {
        this.maxPosition = value;
    }

    /**
     * Legt den Wert der enterOrExit-Eigenschaft fest.
     */
    public void setEnterOrExit(boolean value) {
        this.enterOrExit = value;
    }

    /**
     * Gets the value of the any property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the any
     * property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getAny().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link Element } {@link
     * java.lang.Object }
     */
    public List<java.lang.Object> getAny() {
        if (any == null) {
            any = new ArrayList<java.lang.Object>();
        }
        return this.any;
    }

}
