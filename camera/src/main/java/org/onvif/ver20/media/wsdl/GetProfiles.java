package org.onvif.ver20.media.wsdl;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"token", "type"})
@XmlRootElement(name = "GetProfiles")
public class GetProfiles {

    /**
     * -- GETTER --
     *  Ruft den Wert der token-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    @Getter @XmlElement(name = "Token")
    protected String token;

    @XmlElement(name = "Type")
    protected List<String> type;

    /**
     * Legt den Wert der token-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Gets the value of the type property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the type
     * property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     *    getType().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link String }
     */
    public List<String> getType() {
        if (type == null) {
            type = new ArrayList<String>();
        }
        return this.type;
    }
}
