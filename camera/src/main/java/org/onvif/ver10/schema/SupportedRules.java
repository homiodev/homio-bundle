package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * Java-Klasse f�r SupportedRules complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="SupportedRules">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="RuleContentSchemaLocation" type="{http://www.w3.org/2001/XMLSchema}anyURI" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="RuleDescription" type="{http://www.onvif.org/ver10/schema}ConfigDescription" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}SupportedRulesExtension" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "SupportedRules",
        propOrder = {"ruleContentSchemaLocation", "ruleDescription", "extension"})
public class SupportedRules {

    @XmlElement(name = "RuleContentSchemaLocation")
    @XmlSchemaType(name = "anyURI")
    protected List<String> ruleContentSchemaLocation;

    @XmlElement(name = "RuleDescription")
    protected List<ConfigDescription> ruleDescription;

    /**
     * -- GETTER --
     *  Ruft den Wert der extension-Eigenschaft ab.
     *
     * @return possible object is {@link SupportedRulesExtension }
     */
    @Getter @XmlElement(name = "Extension")
    protected SupportedRulesExtension extension;

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
     * Gets the value of the ruleContentSchemaLocation property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the
     * ruleContentSchemaLocation property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getRuleContentSchemaLocation().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link String }
     */
    public List<String> getRuleContentSchemaLocation() {
        if (ruleContentSchemaLocation == null) {
            ruleContentSchemaLocation = new ArrayList<String>();
        }
        return this.ruleContentSchemaLocation;
    }

    /**
     * Gets the value of the ruleDescription property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the
     * ruleDescription property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getRuleDescription().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link ConfigDescription }
     */
    public List<ConfigDescription> getRuleDescription() {
        if (ruleDescription == null) {
            ruleDescription = new ArrayList<ConfigDescription>();
        }
        return this.ruleDescription;
    }

    /**
     * Legt den Wert der extension-Eigenschaft fest.
     *
     * @param value allowed object is {@link SupportedRulesExtension }
     */
    public void setExtension(SupportedRulesExtension value) {
        this.extension = value;
    }

}
