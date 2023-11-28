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
 * <p>Java class for voc.SubstitutionValueType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.SubstitutionValueType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="Codes"/&gt;
 *     &lt;enumeration value="Drugs"/&gt;
 *     &lt;enumeration value="CodesOrDrugs"/&gt;
 *     &lt;enumeration value="Date"/&gt;
 *     &lt;enumeration value="List"/&gt;
 *     &lt;enumeration value="BaselineDate"/&gt;
 *     &lt;enumeration value="ReferenceDate"/&gt;
 *     &lt;enumeration value="OpenEndedStartDate"/&gt;
 *     &lt;enumeration value="OpenEndedEndDate"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "voc.SubstitutionValueType")
@XmlEnum
public enum VocSubstitutionValueType {


    /**
     * ReadCode version2 - e.g.ClinicalCodes
     * 
     */
    @XmlEnumValue("Codes")
    CODES("Codes"),
    @XmlEnumValue("Drugs")
    DRUGS("Drugs"),
    @XmlEnumValue("CodesOrDrugs")
    CODES_OR_DRUGS("CodesOrDrugs"),
    @XmlEnumValue("Date")
    DATE("Date"),
    @XmlEnumValue("List")
    LIST("List"),
    @XmlEnumValue("BaselineDate")
    BASELINE_DATE("BaselineDate"),
    @XmlEnumValue("ReferenceDate")
    REFERENCE_DATE("ReferenceDate"),
    @XmlEnumValue("OpenEndedStartDate")
    OPEN_ENDED_START_DATE("OpenEndedStartDate"),
    @XmlEnumValue("OpenEndedEndDate")
    OPEN_ENDED_END_DATE("OpenEndedEndDate");
    private final String value;

    VocSubstitutionValueType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VocSubstitutionValueType fromValue(String v) {
        for (VocSubstitutionValueType c: VocSubstitutionValueType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
