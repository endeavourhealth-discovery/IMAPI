//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for voc.SingleValueConstant.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.SingleValueConstant"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="SEARCHDATE"/&gt;
 *     &lt;enumeration value="TODAY"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "voc.SingleValueConstant")
@XmlEnum
public enum VocSingleValueConstant {


    /**
     * Actual search date
     * 
     */
    SEARCHDATE,

    /**
     * Todays date
     * 
     */
    TODAY;

    public String value() {
        return name();
    }

    public static VocSingleValueConstant fromValue(String v) {
        return valueOf(v);
    }

}
