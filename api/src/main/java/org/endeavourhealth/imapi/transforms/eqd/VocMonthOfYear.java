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
 * <p>Java class for voc.MonthOfYear.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.MonthOfYear"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="JAN"/&gt;
 *     &lt;enumeration value="FEB"/&gt;
 *     &lt;enumeration value="MAR"/&gt;
 *     &lt;enumeration value="APR"/&gt;
 *     &lt;enumeration value="MAY"/&gt;
 *     &lt;enumeration value="JUN"/&gt;
 *     &lt;enumeration value="JUL"/&gt;
 *     &lt;enumeration value="AUG"/&gt;
 *     &lt;enumeration value="SEP"/&gt;
 *     &lt;enumeration value="OCT"/&gt;
 *     &lt;enumeration value="NOV"/&gt;
 *     &lt;enumeration value="DEC"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "voc.MonthOfYear")
@XmlEnum
public enum VocMonthOfYear {

    JAN,
    FEB,
    MAR,
    APR,
    MAY,
    JUN,
    JUL,
    AUG,
    SEP,
    OCT,
    NOV,
    DEC;

    public String value() {
        return name();
    }

    public static VocMonthOfYear fromValue(String v) {
        return valueOf(v);
    }

}