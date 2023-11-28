//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for voc.AggregateSource.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.AggregateSource"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="population"/&gt;
 *     &lt;enumeration value="record"/&gt;
 *     &lt;enumeration value="column"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "voc.AggregateSource")
@XmlEnum
public enum VocAggregateSource {

    @XmlEnumValue("population")
    POPULATION("population"),
    @XmlEnumValue("record")
    RECORD("record"),
    @XmlEnumValue("column")
    COLUMN("column");
    private final String value;

    VocAggregateSource(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VocAggregateSource fromValue(String v) {
        for (VocAggregateSource c: VocAggregateSource.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
