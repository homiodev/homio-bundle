package org.onvif.ver10.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyAttribute;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;

/**
 * Java-Klasse f�r GetRecordingsResponseItem complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="GetRecordingsResponseItem">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="RecordingToken" type="{http://www.onvif.org/ver10/schema}RecordingReference"/>
 *         <element name="Configuration" type="{http://www.onvif.org/ver10/schema}RecordingConfiguration"/>
 *         <element name="Tracks" type="{http://www.onvif.org/ver10/schema}GetTracksResponseList"/>
 *         <any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "GetRecordingsResponseItem",
    propOrder = {"recordingToken", "configuration", "tracks", "any"})
public class GetRecordingsResponseItem {

    @XmlElement(name = "RecordingToken", required = true)
    protected String recordingToken;

    @XmlElement(name = "Configuration", required = true)
    protected RecordingConfiguration configuration;

    @XmlElement(name = "Tracks", required = true)
    protected GetTracksResponseList tracks;

    @XmlAnyElement(lax = true)
    protected List<java.lang.Object> any;

    @XmlAnyAttribute private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Ruft den Wert der recordingToken-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getRecordingToken() {
        return recordingToken;
    }

    /**
     * Legt den Wert der recordingToken-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setRecordingToken(String value) {
        this.recordingToken = value;
    }

    /**
     * Ruft den Wert der configuration-Eigenschaft ab.
     *
     * @return possible object is {@link RecordingConfiguration }
     */
    public RecordingConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Legt den Wert der configuration-Eigenschaft fest.
     *
     * @param value allowed object is {@link RecordingConfiguration }
     */
    public void setConfiguration(RecordingConfiguration value) {
        this.configuration = value;
    }

    /**
     * Ruft den Wert der tracks-Eigenschaft ab.
     *
     * @return possible object is {@link GetTracksResponseList }
     */
    public GetTracksResponseList getTracks() {
        return tracks;
    }

    /**
     * Legt den Wert der tracks-Eigenschaft fest.
     *
     * @param value allowed object is {@link GetTracksResponseList }
     */
    public void setTracks(GetTracksResponseList value) {
        this.tracks = value;
    }

    /**
     * Gets the value of the any property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the any
     * property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getAny().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link Element } {@link
     * java.lang.Object }
     */
    public List<java.lang.Object> getAny() {
        if (any == null) {
            any = new ArrayList<java.lang.Object>();
        }
        return this.any;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     *
     * <p>the map is keyed by the name of the attribute and the value is the string value of the
     * attribute.
     *
     * <p>the map returned by this method is live, and you can add new attribute by updating the map
     * directly. Because of this design, there's no setter.
     *
     * @return always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }
}
