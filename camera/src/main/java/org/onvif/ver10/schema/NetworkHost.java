package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Java-Klasse f�r NetworkHost complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="NetworkHost">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Type" type="{http://www.onvif.org/ver10/schema}NetworkHostType"/>
 *         <element name="IPv4Address" type="{http://www.onvif.org/ver10/schema}IPv4Address" minOccurs="0"/>
 *         <element name="IPv6Address" type="{http://www.onvif.org/ver10/schema}IPv6Address" minOccurs="0"/>
 *         <element name="DNSname" type="{http://www.onvif.org/ver10/schema}DNSName" minOccurs="0"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}NetworkHostExtension" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "NetworkHost",
        propOrder = {"type", "iPv4Address", "iPv6Address", "dnSname", "extension"})
public class NetworkHost {

    /**
     * -- GETTER --
     *  Ruft den Wert der type-Eigenschaft ab.
     *
     * @return possible object is {@link NetworkHostType }
     */
    @Getter @XmlElement(name = "Type", required = true)
    protected NetworkHostType type;

    @XmlElement(name = "IPv4Address")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String iPv4Address;

    @XmlElement(name = "IPv6Address")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String iPv6Address;

    @XmlElement(name = "DNSname")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String dnSname;

    /**
     * -- GETTER --
     *  Ruft den Wert der extension-Eigenschaft ab.
     *
     * @return possible object is {@link NetworkHostExtension }
     */
    @Getter @XmlElement(name = "Extension")
    protected NetworkHostExtension extension;

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
     * Legt den Wert der type-Eigenschaft fest.
     *
     * @param value allowed object is {@link NetworkHostType }
     */
    public void setType(NetworkHostType value) {
        this.type = value;
    }

    /**
     * Ruft den Wert der iPv4Address-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getIPv4Address() {
        return iPv4Address;
    }

    /**
     * Legt den Wert der iPv4Address-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setIPv4Address(String value) {
        this.iPv4Address = value;
    }

    /**
     * Ruft den Wert der iPv6Address-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getIPv6Address() {
        return iPv6Address;
    }

    /**
     * Legt den Wert der iPv6Address-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setIPv6Address(String value) {
        this.iPv6Address = value;
    }

    /**
     * Ruft den Wert der dnSname-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getDNSname() {
        return dnSname;
    }

    /**
     * Legt den Wert der dnSname-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setDNSname(String value) {
        this.dnSname = value;
    }

    /**
     * Legt den Wert der extension-Eigenschaft fest.
     *
     * @param value allowed object is {@link NetworkHostExtension }
     */
    public void setExtension(NetworkHostExtension value) {
        this.extension = value;
    }

}
