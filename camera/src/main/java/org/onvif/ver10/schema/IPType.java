package org.onvif.ver10.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java-Klasse f�r IPType.
 *
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 *
 * <pre>
 * <simpleType name="IPType">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="IPv4"/>
 *     <enumeration value="IPv6"/>
 *   </restriction>
 * </simpleType>
 * </pre>
 *
 */
@XmlType(name = "IPType")
@XmlEnum
public enum IPType {

	@XmlEnumValue("IPv4")
	I_PV_4("IPv4"), @XmlEnumValue("IPv6")
	I_PV_6("IPv6");
	private final String value;

	IPType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static IPType fromValue(String v) {
		for (IPType c : IPType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
