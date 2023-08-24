package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * Java-Klasse f�r SupportedAnalyticsModules complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="SupportedAnalyticsModules">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="AnalyticsModuleContentSchemaLocation" type="{http://www.w3.org/2001/XMLSchema}anyURI" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="AnalyticsModuleDescription" type="{http://www.onvif.org/ver10/schema}ConfigDescription" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}SupportedAnalyticsModulesExtension" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "SupportedAnalyticsModules",
        propOrder = {"analyticsModuleContentSchemaLocation", "analyticsModuleDescription", "extension"})
public class SupportedAnalyticsModules {

    @XmlElement(name = "AnalyticsModuleContentSchemaLocation")
    @XmlSchemaType(name = "anyURI")
    protected List<String> analyticsModuleContentSchemaLocation;

    @XmlElement(name = "AnalyticsModuleDescription")
    protected List<ConfigDescription> analyticsModuleDescription;

    /**
     * -- GETTER --
     *  Ruft den Wert der extension-Eigenschaft ab.
     *
     * @return possible object is {@link SupportedAnalyticsModulesExtension }
     */
    @Getter @XmlElement(name = "Extension")
    protected SupportedAnalyticsModulesExtension extension;

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
     * Gets the value of the analyticsModuleContentSchemaLocation property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the
     * analyticsModuleContentSchemaLocation property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getAnalyticsModuleContentSchemaLocation().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link String }
     */
    public List<String> getAnalyticsModuleContentSchemaLocation() {
        if (analyticsModuleContentSchemaLocation == null) {
            analyticsModuleContentSchemaLocation = new ArrayList<String>();
        }
        return this.analyticsModuleContentSchemaLocation;
    }

    /**
     * Gets the value of the analyticsModuleDescription property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the
     * analyticsModuleDescription property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getAnalyticsModuleDescription().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link ConfigDescription }
     */
    public List<ConfigDescription> getAnalyticsModuleDescription() {
        if (analyticsModuleDescription == null) {
            analyticsModuleDescription = new ArrayList<ConfigDescription>();
        }
        return this.analyticsModuleDescription;
    }

    /**
     * Legt den Wert der extension-Eigenschaft fest.
     *
     * @param value allowed object is {@link SupportedAnalyticsModulesExtension }
     */
    public void setExtension(SupportedAnalyticsModulesExtension value) {
        this.extension = value;
    }

}
