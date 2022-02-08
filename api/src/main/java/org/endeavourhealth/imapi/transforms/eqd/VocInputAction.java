//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for voc.InputAction.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.InputAction"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="number"/&gt;
 *     &lt;enumeration value="date"/&gt;
 *     &lt;enumeration value="readcode"/&gt;
 *     &lt;enumeration value="drugcode"/&gt;
 *     &lt;enumeration value="age"/&gt;
 *     &lt;enumeration value="time"/&gt;
 *     &lt;enumeration value="user"/&gt;
 *     &lt;enumeration value="sessionholder"/&gt;
 *     &lt;enumeration value="appointment"/&gt;
 *     &lt;enumeration value="location"/&gt;
 *     &lt;enumeration value="values"/&gt;
 *     &lt;enumeration value="freetext"/&gt;
 *     &lt;enumeration value="usertype"/&gt;
 *     &lt;enumeration value="locationtype"/&gt;
 *     &lt;enumeration value="allergycode"/&gt;
 *     &lt;enumeration value="speciality"/&gt;
 *     &lt;enumeration value="organisation"/&gt;
 *     &lt;enumeration value="ethnicorigin"/&gt;
 *     &lt;enumeration value="durationtime"/&gt;
 *     &lt;enumeration value="durationdatetine"/&gt;
 *     &lt;enumeration value="sc_commissioner"/&gt;
 *     &lt;enumeration value="sc_provider"/&gt;
 *     &lt;enumeration value="sc_hrg"/&gt;
 *     &lt;enumeration value="sc_consultant"/&gt;
 *     &lt;enumeration value="sc_speciality"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "voc.InputAction")
@XmlEnum
public enum VocInputAction {

    @XmlEnumValue("number")
    NUMBER("number"),
    @XmlEnumValue("date")
    DATE("date"),
    @XmlEnumValue("readcode")
    READCODE("readcode"),
    @XmlEnumValue("drugcode")
    DRUGCODE("drugcode"),
    @XmlEnumValue("age")
    AGE("age"),
    @XmlEnumValue("time")
    TIME("time"),
    @XmlEnumValue("user")
    USER("user"),
    @XmlEnumValue("sessionholder")
    SESSIONHOLDER("sessionholder"),
    @XmlEnumValue("appointment")
    APPOINTMENT("appointment"),
    @XmlEnumValue("location")
    LOCATION("location"),
    @XmlEnumValue("values")
    VALUES("values"),
    @XmlEnumValue("freetext")
    FREETEXT("freetext"),
    @XmlEnumValue("usertype")
    USERTYPE("usertype"),
    @XmlEnumValue("locationtype")
    LOCATIONTYPE("locationtype"),
    @XmlEnumValue("allergycode")
    ALLERGYCODE("allergycode"),
    @XmlEnumValue("speciality")
    SPECIALITY("speciality"),
    @XmlEnumValue("organisation")
    ORGANISATION("organisation"),
    @XmlEnumValue("ethnicorigin")
    ETHNICORIGIN("ethnicorigin"),
    @XmlEnumValue("durationtime")
    DURATIONTIME("durationtime"),
    @XmlEnumValue("durationdatetine")
    DURATIONDATETINE("durationdatetine"),
    @XmlEnumValue("sc_commissioner")
    SC_COMMISSIONER("sc_commissioner"),
    @XmlEnumValue("sc_provider")
    SC_PROVIDER("sc_provider"),
    @XmlEnumValue("sc_hrg")
    SC_HRG("sc_hrg"),
    @XmlEnumValue("sc_consultant")
    SC_CONSULTANT("sc_consultant"),
    @XmlEnumValue("sc_speciality")
    SC_SPECIALITY("sc_speciality");
    private final String value;

    VocInputAction(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VocInputAction fromValue(String v) {
        for (VocInputAction c: VocInputAction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}