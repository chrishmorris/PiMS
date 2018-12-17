//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB
// 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2011.08.24 at 01:32:15 PM BST
//

package org.pimslims.crystallization.xml.screen;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ConcentrationUnitType.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="ConcentrationUnitType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="M"/>
 *     &lt;enumeration value="mM"/>
 *     &lt;enumeration value="Percent v/v"/>
 *     &lt;enumeration value="Percent w/v"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ConcentrationUnitType")
@XmlEnum
public enum ConcentrationUnitType {

    M("M"), mM("mM"), @XmlEnumValue("Percent v/v")
    PERCENT_V_V("Percent v/v"), @XmlEnumValue("Percent w/v")
    PERCENT_W_V("Percent w/v");
    private final String value;

    ConcentrationUnitType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ConcentrationUnitType fromValue(String v) {
        for (ConcentrationUnitType c : ConcentrationUnitType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
