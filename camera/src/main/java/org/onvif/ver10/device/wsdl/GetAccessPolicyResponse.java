//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation,
// v2.2.5-2 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2014.02.04 um 12:22:03 PM CET
//

package org.onvif.ver10.device.wsdl;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import org.onvif.ver10.schema.BinaryData;

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
 *         <element name="PolicyFile" type="{http://www.onvif.org/ver10/schema}BinaryData"/>
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
        propOrder = {"policyFile"})
@XmlRootElement(name = "GetAccessPolicyResponse")
public class GetAccessPolicyResponse {

    /**
     * -- GETTER --
     *  Ruft den Wert der policyFile-Eigenschaft ab.
     *
     * @return possible object is {@link BinaryData }
     */
    @XmlElement(name = "PolicyFile", required = true)
    protected BinaryData policyFile;

    /**
     * Legt den Wert der policyFile-Eigenschaft fest.
     *
     * @param value allowed object is {@link BinaryData }
     */
    public void setPolicyFile(BinaryData value) {
        this.policyFile = value;
    }
}
