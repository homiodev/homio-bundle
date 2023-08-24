package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * Java-Klasse f�r NetworkZeroConfiguration complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="NetworkZeroConfiguration">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="InterfaceToken" type="{http://www.onvif.org/ver10/schema}ReferenceToken"/>
 *         <element name="Enabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="Addresses" type="{http://www.onvif.org/ver10/schema}IPv4Address" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}NetworkZeroConfigurationExtension" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "NetworkZeroConfiguration",
        propOrder = {"interfaceToken", "enabled", "addresses", "extension"})
public class NetworkZeroConfiguration {

    /**
     * -- GETTER --
     *  Ruft den Wert der interfaceToken-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    @Getter @XmlElement(name = "InterfaceToken", required = true)
    protected String interfaceToken;

    /**
     * -- GETTER --
     *  Ruft den Wert der enabled-Eigenschaft ab.
     */
    @Getter @XmlElement(name = "Enabled")
    protected boolean enabled;

    @XmlElement(name = "Addresses")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected List<String> addresses;

    /**
     * -- GETTER --
     *  Ruft den Wert der extension-Eigenschaft ab.
     *
     * @return possible object is {@link NetworkZeroConfigurationExtension }
     */
    @Getter @XmlElement(name = "Extension")
    protected NetworkZeroConfigurationExtension extension;

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
     * Legt den Wert der interfaceToken-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setInterfaceToken(String value) {
        this.interfaceToken = value;
    }

    /**
     * Legt den Wert der enabled-Eigenschaft fest.
     */
    public void setEnabled(boolean value) {
        this.enabled = value;
    }

    /**
     * Gets the value of the addresses property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the addresses
     * property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getAddresses().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link String }
     */
    public List<String> getAddresses() {
        if (addresses == null) {
            addresses = new ArrayList<String>();
        }
        return this.addresses;
    }

    /**
     * Legt den Wert der extension-Eigenschaft fest.
     *
     * @param value allowed object is {@link NetworkZeroConfigurationExtension }
     */
    public void setExtension(NetworkZeroConfigurationExtension value) {
        this.extension = value;
    }

}
