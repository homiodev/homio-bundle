package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Java-Klasse f�r User complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="User">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Username" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="UserLevel" type="{http://www.onvif.org/ver10/schema}UserLevel"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}UserExtension" minOccurs="0"/>
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
        name = "User",
        propOrder = {"username", "password", "userLevel", "extension"})
public class User {

    /**
     * -- GETTER --
     *  Ruft den Wert der username-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    @XmlElement(name = "Username", required = true)
    protected String username;

    /**
     * -- GETTER --
     *  Ruft den Wert der password-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    @XmlElement(name = "Password")
    protected String password;

    /**
     * -- GETTER --
     *  Ruft den Wert der userLevel-Eigenschaft ab.
     *
     * @return possible object is {@link UserLevel }
     */
    @XmlElement(name = "UserLevel", required = true)
    protected UserLevel userLevel;

    /**
     * -- GETTER --
     *  Ruft den Wert der extension-Eigenschaft ab.
     *
     * @return possible object is {@link UserExtension }
     */
    @XmlElement(name = "Extension")
    protected UserExtension extension;

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
     * Legt den Wert der username-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Legt den Wert der password-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Legt den Wert der userLevel-Eigenschaft fest.
     *
     * @param value allowed object is {@link UserLevel }
     */
    public void setUserLevel(UserLevel value) {
        this.userLevel = value;
    }

    /**
     * Legt den Wert der extension-Eigenschaft fest.
     *
     * @param value allowed object is {@link UserExtension }
     */
    public void setExtension(UserExtension value) {
        this.extension = value;
    }

}
