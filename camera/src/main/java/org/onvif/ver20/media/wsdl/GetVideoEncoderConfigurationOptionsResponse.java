package org.onvif.ver20.media.wsdl;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.onvif.ver10.schema.VideoEncoder2ConfigurationOptions;

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
 *         &lt;element name="Options" type="{http://www.onvif.org/ver10/schema}VideoEncoder2ConfigurationOptions" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"options"})
@XmlRootElement(name = "GetVideoEncoderConfigurationOptionsResponse")
public class GetVideoEncoderConfigurationOptionsResponse {

    @XmlElement(name = "Options", required = true)
    protected List<VideoEncoder2ConfigurationOptions> options;

    /**
     * Gets the value of the options property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the options
     * property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     *    getOptions().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link
     * VideoEncoder2ConfigurationOptions }
     */
    public List<VideoEncoder2ConfigurationOptions> getOptions() {
        if (options == null) {
            options = new ArrayList<VideoEncoder2ConfigurationOptions>();
        }
        return this.options;
    }
}
