//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation,
// v2.2.5-2 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2014.02.19 um 02:35:56 PM CET
//

package org.onvif.ver10.media.wsdl;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import org.onvif.ver10.schema.VideoSourceConfigurationOptions;

/**
 * Java-Klasse f�r anonymous complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <pre>
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Options" type="{http://www.onvif.org/ver10/schema}VideoSourceConfigurationOptions"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 */
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"options"})
@XmlRootElement(name = "GetVideoSourceConfigurationOptionsResponse")
public class GetVideoSourceConfigurationOptionsResponse {

    /**
     * -- GETTER --
     *  Ruft den Wert der options-Eigenschaft ab.
     *
     * @return possible object is {@link VideoSourceConfigurationOptions }
     */
    @XmlElement(name = "Options", required = true)
    protected VideoSourceConfigurationOptions options;

    /**
     * Legt den Wert der options-Eigenschaft fest.
     *
     * @param value allowed object is {@link VideoSourceConfigurationOptions }
     */
    public void setOptions(VideoSourceConfigurationOptions value) {
        this.options = value;
    }
}
