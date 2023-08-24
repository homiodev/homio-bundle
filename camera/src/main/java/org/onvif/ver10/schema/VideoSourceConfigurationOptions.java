package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * Java-Klasse f�r VideoSourceConfigurationOptions complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="VideoSourceConfigurationOptions">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="BoundsRange" type="{http://www.onvif.org/ver10/schema}IntRectangleRange"/>
 *         <element name="VideoSourceTokensAvailable" type="{http://www.onvif.org/ver10/schema}ReferenceToken" maxOccurs="unbounded"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}VideoSourceConfigurationOptionsExtension" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "VideoSourceConfigurationOptions",
        propOrder = {"boundsRange", "videoSourceTokensAvailable", "extension"})
public class VideoSourceConfigurationOptions {

    /**
     * -- GETTER --
     *  Ruft den Wert der boundsRange-Eigenschaft ab.
     *
     * @return possible object is {@link IntRectangleRange }
     */
    @Getter @XmlElement(name = "BoundsRange", required = true)
    protected IntRectangleRange boundsRange;

    @XmlElement(name = "VideoSourceTokensAvailable", required = true)
    protected List<String> videoSourceTokensAvailable;

    /**
     * -- GETTER --
     *  Ruft den Wert der extension-Eigenschaft ab.
     *
     * @return possible object is {@link VideoSourceConfigurationOptionsExtension }
     */
    @Getter @XmlElement(name = "Extension")
    protected VideoSourceConfigurationOptionsExtension extension;

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
     * Legt den Wert der boundsRange-Eigenschaft fest.
     *
     * @param value allowed object is {@link IntRectangleRange }
     */
    public void setBoundsRange(IntRectangleRange value) {
        this.boundsRange = value;
    }

    /**
     * Gets the value of the videoSourceTokensAvailable property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the
     * videoSourceTokensAvailable property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getVideoSourceTokensAvailable().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link String }
     */
    public List<String> getVideoSourceTokensAvailable() {
        if (videoSourceTokensAvailable == null) {
            videoSourceTokensAvailable = new ArrayList<String>();
        }
        return this.videoSourceTokensAvailable;
    }

    /**
     * Legt den Wert der extension-Eigenschaft fest.
     *
     * @param value allowed object is {@link VideoSourceConfigurationOptionsExtension }
     */
    public void setExtension(VideoSourceConfigurationOptionsExtension value) {
        this.extension = value;
    }

}
