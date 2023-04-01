package org.onvif.ver10.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Java-Klasse f�r StreamType.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <p>
 *
 * <pre>
 * <simpleType name="StreamType">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="RTP-Unicast"/>
 *     <enumeration value="RTP-Multicast"/>
 *   </restriction>
 * </simpleType>
 * </pre>
 */
@XmlType(name = "StreamType")
@XmlEnum
public enum StreamType {
    @XmlEnumValue("RTP-Unicast")
    RTP_UNICAST("RTP-Unicast"),
    @XmlEnumValue("RTP-Multicast")
    RTP_MULTICAST("RTP-Multicast");
    private final String value;

    StreamType(String v) {
        value = v;
    }

    public static StreamType fromValue(String v) {
        for (StreamType c : StreamType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public String value() {
        return value;
    }
}
