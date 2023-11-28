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
 * <p>Java class for voc.DrugIssueType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.DrugIssueType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="P"/&gt;
 *     &lt;enumeration value="Q"/&gt;
 *     &lt;enumeration value="H"/&gt;
 *     &lt;enumeration value="O"/&gt;
 *     &lt;enumeration value="D"/&gt;
 *     &lt;enumeration value="B"/&gt;
 *     &lt;enumeration value="S"/&gt;
 *     &lt;enumeration value="A"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "voc.DrugIssueType")
@XmlEnum
public enum VocDrugIssueType {


    /**
     * Print
     * 
     */
    P,

    /**
     * Issue Without Script
     * 
     */
    Q,

    /**
     * Handwitten
     * 
     */
    H,

    /**
     * Outside
     * 
     */
    O,

    /**
     * Dispensing
     * 
     */
    D,

    /**
     * Repeat Dispensing
     * 
     */
    B,

    /**
     * Store
     * 
     */
    S,

    /**
     * Automatic
     * 
     */
    A;

    public String value() {
        return name();
    }

    public static VocDrugIssueType fromValue(String v) {
        return valueOf(v);
    }

}
