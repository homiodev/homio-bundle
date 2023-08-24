package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;

/**
 * Java-Klasse f�r PrefixedIPv4Address complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="PrefixedIPv4Address">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Address" type="{http://www.onvif.org/ver10/schema}IPv4Address"/>
 *         <element name="PrefixLength" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "PrefixedIPv4Address",
        propOrder = {"address", "prefixLength"})
public class PrefixedIPv4Address {

    /**
     * -- GETTER --
     *  Ruft den Wert der address-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    @XmlElement(name = "Address", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String address;

    /**
     * -- GETTER --
     *  Ruft den Wert der prefixLength-Eigenschaft ab.
     */
    @XmlElement(name = "PrefixLength")
    protected int prefixLength;

    /**
     * Legt den Wert der address-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Legt den Wert der prefixLength-Eigenschaft fest.
     */
    public void setPrefixLength(int value) {
        this.prefixLength = value;
    }
}
