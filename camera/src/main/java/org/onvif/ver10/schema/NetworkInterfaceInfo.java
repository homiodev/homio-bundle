package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;

/**
 * Java-Klasse f�r NetworkInterfaceInfo complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="NetworkInterfaceInfo">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="HwAddress" type="{http://www.onvif.org/ver10/schema}HwAddress"/>
 *         <element name="MTU" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "NetworkInterfaceInfo",
        propOrder = {"name", "hwAddress", "mtu"})
public class NetworkInterfaceInfo {

    /**
     * -- GETTER --
     *  Ruft den Wert der name-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    @Getter @XmlElement(name = "Name")
    protected String name;

    /**
     * -- GETTER --
     *  Ruft den Wert der hwAddress-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    @Getter @XmlElement(name = "HwAddress", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String hwAddress;

    @XmlElement(name = "MTU")
    protected Integer mtu;

    /**
     * Legt den Wert der name-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Legt den Wert der hwAddress-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setHwAddress(String value) {
        this.hwAddress = value;
    }

    /**
     * Ruft den Wert der mtu-Eigenschaft ab.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getMTU() {
        return mtu;
    }

    /**
     * Legt den Wert der mtu-Eigenschaft fest.
     *
     * @param value allowed object is {@link Integer }
     */
    public void setMTU(Integer value) {
        this.mtu = value;
    }
}
