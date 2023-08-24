package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * Java-Klasse f�r RotateOptions complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType name="RotateOptions">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Mode" type="{http://www.onvif.org/ver10/schema}RotateMode" maxOccurs="unbounded"/>
 *         <element name="DegreeList" type="{http://www.onvif.org/ver10/schema}IntList" minOccurs="0"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}RotateOptionsExtension" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "RotateOptions",
        propOrder = {"mode", "degreeList", "extension"})
public class RotateOptions {

    @XmlElement(name = "Mode", required = true)
    protected List<RotateMode> mode;

    /**
     * -- GETTER --
     *  Ruft den Wert der degreeList-Eigenschaft ab.
     *
     * @return possible object is {@link IntList }
     */
    @Getter @XmlElement(name = "DegreeList")
    protected IntList degreeList;

    /**
     * -- GETTER --
     *  Ruft den Wert der extension-Eigenschaft ab.
     *
     * @return possible object is {@link RotateOptionsExtension }
     */
    @Getter @XmlElement(name = "Extension")
    protected RotateOptionsExtension extension;

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
     * Gets the value of the mode property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the mode
     * property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getMode().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link RotateMode }
     */
    public List<RotateMode> getMode() {
        if (mode == null) {
            mode = new ArrayList<RotateMode>();
        }
        return this.mode;
    }

    /**
     * Legt den Wert der degreeList-Eigenschaft fest.
     *
     * @param value allowed object is {@link IntList }
     */
    public void setDegreeList(IntList value) {
        this.degreeList = value;
    }

    /**
     * Legt den Wert der extension-Eigenschaft fest.
     *
     * @param value allowed object is {@link RotateOptionsExtension }
     */
    public void setExtension(RotateOptionsExtension value) {
        this.extension = value;
    }

}
