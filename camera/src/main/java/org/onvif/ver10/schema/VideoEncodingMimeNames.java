package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Java-Klasse f�r VideoEncodingMimeNames.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten
 * ist.
 *
 * <p>
 *
 * <pre>
 * &lt;simpleType name="VideoEncodingMimeNames">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="JPEG"/>
 *     &lt;enumeration value="MPV4-ES"/>
 *     &lt;enumeration value="H264"/>
 *     &lt;enumeration value="H265"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "VideoEncodingMimeNames")
@XmlEnum
public enum VideoEncodingMimeNames {
    JPEG("JPEG"),
    @XmlEnumValue("MPV4-ES")
    MPV_4_ES("MPV4-ES"),
    @XmlEnumValue("H264")
    H_264("H264"),
    @XmlEnumValue("H265")
    H_265("H265");
    private final String value;

    VideoEncodingMimeNames(String v) {
        value = v;
    }

    public static VideoEncodingMimeNames fromValue(String v) {
        for (VideoEncodingMimeNames c : VideoEncodingMimeNames.values()) {
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
