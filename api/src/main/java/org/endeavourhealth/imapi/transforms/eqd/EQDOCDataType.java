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
 * <p>Java class for EQDOC.DataType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EQDOC.DataType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="integer"/&gt;
 *     &lt;enumeration value="text"/&gt;
 *     &lt;enumeration value="dateTime"/&gt;
 *     &lt;enumeration value="float"/&gt;
 *     &lt;enumeration value="char"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "EQDOC.DataType")
@XmlEnum
public enum EQDOCDataType {

  @XmlEnumValue("integer")
  INTEGER("integer"),
  @XmlEnumValue("text")
  TEXT("text"),
  @XmlEnumValue("dateTime")
  DATE_TIME("dateTime"),
  @XmlEnumValue("float")
  FLOAT("float"),
  @XmlEnumValue("char")
  CHAR("char");
  private final String value;

  EQDOCDataType(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static EQDOCDataType fromValue(String v) {
    for (EQDOCDataType c : EQDOCDataType.values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }

}
