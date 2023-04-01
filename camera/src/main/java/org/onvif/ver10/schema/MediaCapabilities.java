package org.onvif.ver10.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;

/**
 * Java-Klasse f�r MediaCapabilities complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="MediaCapabilities">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="XAddr" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         <element name="StreamingCapabilities" type="{http://www.onvif.org/ver10/schema}RealTimeStreamingCapabilities"/>
 *         <any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}MediaCapabilitiesExtension" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "MediaCapabilities",
    propOrder = {"xAddr", "streamingCapabilities", "any", "extension"})
public class MediaCapabilities {

    @XmlElement(name = "XAddr", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String xAddr;

    @XmlElement(name = "StreamingCapabilities", required = true)
    protected RealTimeStreamingCapabilities streamingCapabilities;

    @XmlAnyElement(lax = true)
    protected List<java.lang.Object> any;

    @XmlElement(name = "Extension")
    protected MediaCapabilitiesExtension extension;

    @XmlAnyAttribute private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Ruft den Wert der xAddr-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getXAddr() {
        return xAddr;
    }

    /**
     * Legt den Wert der xAddr-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setXAddr(String value) {
        this.xAddr = value;
    }

    /**
     * Ruft den Wert der streamingCapabilities-Eigenschaft ab.
     *
     * @return possible object is {@link RealTimeStreamingCapabilities }
     */
    public RealTimeStreamingCapabilities getStreamingCapabilities() {
        return streamingCapabilities;
    }

    /**
     * Legt den Wert der streamingCapabilities-Eigenschaft fest.
     *
     * @param value allowed object is {@link RealTimeStreamingCapabilities }
     */
    public void setStreamingCapabilities(RealTimeStreamingCapabilities value) {
        this.streamingCapabilities = value;
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

    /**
     * Ruft den Wert der extension-Eigenschaft ab.
     *
     * @return possible object is {@link MediaCapabilitiesExtension }
     */
    public MediaCapabilitiesExtension getExtension() {
        return extension;
    }

    /**
     * Legt den Wert der extension-Eigenschaft fest.
     *
     * @param value allowed object is {@link MediaCapabilitiesExtension }
     */
    public void setExtension(MediaCapabilitiesExtension value) {
        this.extension = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     *
     * <p>the map is keyed by the name of the attribute and the value is the string value of the
     * attribute.
     *
     * <p>the map returned by this method is live, and you can add new attribute by updating the map
     * directly. Because of this design, there's no setter.
     *
     * @return always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }
}
