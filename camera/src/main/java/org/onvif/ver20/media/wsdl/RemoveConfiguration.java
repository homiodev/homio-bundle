package org.onvif.ver20.media.wsdl;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * Java-Klasse f�r anonymous complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProfileToken" type="{http://www.onvif.org/ver10/schema}ReferenceToken"/>
 *         &lt;element name="Configuration" type="{http://www.onvif.org/ver20/media/wsdl}ConfigurationRef" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"profileToken", "configuration"})
@XmlRootElement(name = "RemoveConfiguration")
public class RemoveConfiguration {

    /**
     * -- GETTER --
     *  Ruft den Wert der profileToken-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    @Getter @XmlElement(name = "ProfileToken", required = true)
    protected String profileToken;

    @XmlElement(name = "Configuration", required = true)
    protected List<ConfigurationRef> configuration;

    /**
     * Legt den Wert der profileToken-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setProfileToken(String value) {
        this.profileToken = value;
    }

    /**
     * Gets the value of the configuration property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the
     * configuration property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     *    getConfiguration().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link ConfigurationRef }
     */
    public List<ConfigurationRef> getConfiguration() {
        if (configuration == null) {
            configuration = new ArrayList<ConfigurationRef>();
        }
        return this.configuration;
    }
}
