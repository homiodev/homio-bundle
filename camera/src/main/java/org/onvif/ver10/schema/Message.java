package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Java-Klasse f�r anonymous complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Source" type="{http://www.onvif.org/ver10/schema}ItemList" minOccurs="0"/>
 *         <element name="Key" type="{http://www.onvif.org/ver10/schema}ItemList" minOccurs="0"/>
 *         <element name="Data" type="{http://www.onvif.org/ver10/schema}ItemList" minOccurs="0"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}MessageExtension" minOccurs="0"/>
 *       </sequence>
 *       <attribute name="UtcTime" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       <attribute name="PropertyOperation" type="{http://www.onvif.org/ver10/schema}PropertyOperation" />
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"source", "key", "data", "extension"})
@XmlRootElement(name = "Message")
public class Message {

    /**
     * -- GETTER --
     *  Ruft den Wert der source-Eigenschaft ab.
     *
     * @return possible object is {@link ItemList }
     */
    @XmlElement(name = "Source")
    protected ItemList source;

    /**
     * -- GETTER --
     *  Ruft den Wert der key-Eigenschaft ab.
     *
     * @return possible object is {@link ItemList }
     */
    @XmlElement(name = "Key")
    protected ItemList key;

    /**
     * -- GETTER --
     *  Ruft den Wert der data-Eigenschaft ab.
     *
     * @return possible object is {@link ItemList }
     */
    @XmlElement(name = "Data")
    protected ItemList data;

    /**
     * -- GETTER --
     *  Ruft den Wert der extension-Eigenschaft ab.
     *
     * @return possible object is {@link MessageExtension }
     */
    @XmlElement(name = "Extension")
    protected MessageExtension extension;

    /**
     * -- GETTER --
     *  Ruft den Wert der utcTime-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    @XmlAttribute(name = "UtcTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar utcTime;

    /**
     * -- GETTER --
     *  Ruft den Wert der propertyOperation-Eigenschaft ab.
     *
     * @return possible object is {@link PropertyOperation }
     */
    @XmlAttribute(name = "PropertyOperation")
    protected PropertyOperation propertyOperation;

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
     * Legt den Wert der source-Eigenschaft fest.
     *
     * @param value allowed object is {@link ItemList }
     */
    public void setSource(ItemList value) {
        this.source = value;
    }

    /**
     * Legt den Wert der key-Eigenschaft fest.
     *
     * @param value allowed object is {@link ItemList }
     */
    public void setKey(ItemList value) {
        this.key = value;
    }

    /**
     * Legt den Wert der data-Eigenschaft fest.
     *
     * @param value allowed object is {@link ItemList }
     */
    public void setData(ItemList value) {
        this.data = value;
    }

    /**
     * Legt den Wert der extension-Eigenschaft fest.
     *
     * @param value allowed object is {@link MessageExtension }
     */
    public void setExtension(MessageExtension value) {
        this.extension = value;
    }

    /**
     * Legt den Wert der utcTime-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setUtcTime(XMLGregorianCalendar value) {
        this.utcTime = value;
    }

    /**
     * Legt den Wert der propertyOperation-Eigenschaft fest.
     *
     * @param value allowed object is {@link PropertyOperation }
     */
    public void setPropertyOperation(PropertyOperation value) {
        this.propertyOperation = value;
    }

}
