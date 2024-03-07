//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for voc.EnterpriseReportingLevel.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.EnterpriseReportingLevel"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NO_DATA"/&gt;
 *     &lt;enumeration value="AGGREGATE"/&gt;
 *     &lt;enumeration value="PSEUDO_IDENTIFYING"/&gt;
 *     &lt;enumeration value="PATIENT_LEVEL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "voc.EnterpriseReportingLevel")
@XmlEnum
public enum VocEnterpriseReportingLevel {

    NO_DATA,
    AGGREGATE,
    PSEUDO_IDENTIFYING,
    PATIENT_LEVEL;

    public String value() {
        return name();
    }

    public static VocEnterpriseReportingLevel fromValue(String v) {
        return valueOf(v);
    }

}
