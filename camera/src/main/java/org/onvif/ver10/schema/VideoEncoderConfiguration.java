package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import org.w3c.dom.Element;

import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Java-Klasse f�r VideoEncoderConfiguration complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="VideoEncoderConfiguration">
 *   <complexContent>
 *     <extension base="{http://www.onvif.org/ver10/schema}ConfigurationEntity">
 *       <sequence>
 *         <element name="Encoding" type="{http://www.onvif.org/ver10/schema}VideoEncoding"/>
 *         <element name="Resolution" type="{http://www.onvif.org/ver10/schema}VideoResolution"/>
 *         <element name="Quality" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         <element name="RateControl" type="{http://www.onvif.org/ver10/schema}VideoRateControl" minOccurs="0"/>
 *         <element name="MPEG4" type="{http://www.onvif.org/ver10/schema}Mpeg4Configuration" minOccurs="0"/>
 *         <element name="H264" type="{http://www.onvif.org/ver10/schema}H264Configuration" minOccurs="0"/>
 *         <element name="Multicast" type="{http://www.onvif.org/ver10/schema}MulticastConfiguration"/>
 *         <element name="SessionTimeout" type="{http://www.w3.org/2001/XMLSchema}duration"/>
 *         <any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax'/>
 *     </extension>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "VideoEncoderConfiguration",
        propOrder = {
                "encoding",
                "resolution",
                "quality",
                "rateControl",
                "mpeg4",
                "h264",
                "multicast",
                "sessionTimeout",
                "any"
        })
public class VideoEncoderConfiguration extends ConfigurationEntity {

    /**
     * -- GETTER --
     *  Ruft den Wert der encoding-Eigenschaft ab.
     *
     * @return possible object is {@link VideoEncoding }
     */
    @Getter @XmlElement(name = "Encoding", required = true)
    protected VideoEncoding encoding;

    /**
     * -- GETTER --
     *  Ruft den Wert der resolution-Eigenschaft ab.
     *
     * @return possible object is {@link VideoResolution }
     */
    @Getter @XmlElement(name = "Resolution", required = true)
    protected VideoResolution resolution;

    /**
     * -- GETTER --
     *  Ruft den Wert der quality-Eigenschaft ab.
     */
    @Getter @XmlElement(name = "Quality")
    protected float quality;

    /**
     * -- GETTER --
     *  Ruft den Wert der rateControl-Eigenschaft ab.
     *
     * @return possible object is {@link VideoRateControl }
     */
    @Getter @XmlElement(name = "RateControl")
    protected VideoRateControl rateControl;

    @XmlElement(name = "MPEG4")
    protected Mpeg4Configuration mpeg4;

    /**
     * -- GETTER --
     *  Ruft den Wert der h264-Eigenschaft ab.
     *
     * @return possible object is {@link H264Configuration }
     */
    @Getter @XmlElement(name = "H264")
    protected H264Configuration h264;

    /**
     * -- GETTER --
     *  Ruft den Wert der multicast-Eigenschaft ab.
     *
     * @return possible object is {@link MulticastConfiguration }
     */
    @Getter @XmlElement(name = "Multicast", required = true)
    protected MulticastConfiguration multicast;

    /**
     * -- GETTER --
     *  Ruft den Wert der sessionTimeout-Eigenschaft ab.
     *
     * @return possible object is {@link Duration }
     */
    @Getter @XmlElement(name = "SessionTimeout", required = true)
    protected Duration sessionTimeout;

    @XmlAnyElement(lax = true)
    protected List<java.lang.Object> any;

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
     * Legt den Wert der encoding-Eigenschaft fest.
     *
     * @param value allowed object is {@link VideoEncoding }
     */
    public void setEncoding(VideoEncoding value) {
        this.encoding = value;
    }

    /**
     * Legt den Wert der resolution-Eigenschaft fest.
     *
     * @param value allowed object is {@link VideoResolution }
     */
    public void setResolution(VideoResolution value) {
        this.resolution = value;
    }

    /**
     * Legt den Wert der quality-Eigenschaft fest.
     */
    public void setQuality(float value) {
        this.quality = value;
    }

    /**
     * Legt den Wert der rateControl-Eigenschaft fest.
     *
     * @param value allowed object is {@link VideoRateControl }
     */
    public void setRateControl(VideoRateControl value) {
        this.rateControl = value;
    }

    /**
     * Ruft den Wert der mpeg4-Eigenschaft ab.
     *
     * @return possible object is {@link Mpeg4Configuration }
     */
    public Mpeg4Configuration getMPEG4() {
        return mpeg4;
    }

    /**
     * Legt den Wert der mpeg4-Eigenschaft fest.
     *
     * @param value allowed object is {@link Mpeg4Configuration }
     */
    public void setMPEG4(Mpeg4Configuration value) {
        this.mpeg4 = value;
    }

    /**
     * Legt den Wert der h264-Eigenschaft fest.
     *
     * @param value allowed object is {@link H264Configuration }
     */
    public void setH264(H264Configuration value) {
        this.h264 = value;
    }

    /**
     * Legt den Wert der multicast-Eigenschaft fest.
     *
     * @param value allowed object is {@link MulticastConfiguration }
     */
    public void setMulticast(MulticastConfiguration value) {
        this.multicast = value;
    }

    /**
     * Legt den Wert der sessionTimeout-Eigenschaft fest.
     *
     * @param value allowed object is {@link Duration }
     */
    public void setSessionTimeout(Duration value) {
        this.sessionTimeout = value;
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

}
