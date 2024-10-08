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
 * <p>Java class for voc.UserIdentifierType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.UserIdentifierType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="GP"/&gt;
 *     &lt;enumeration value="NAT"/&gt;
 *     &lt;enumeration value="PRES"/&gt;
 *     &lt;enumeration value="UKCC"/&gt;
 *     &lt;enumeration value="RSBP"/&gt;
 *     &lt;enumeration value="GDC"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "voc.UserIdentifierType")
@XmlEnum
public enum VocUserIdentifierType {


  /**
   * GMC Number
   */
  GP,

  /**
   * Doctor Index Number
   */
  NAT,

  /**
   * GMP PPD Code
   */
  PRES,

  /**
   * UKCC
   */
  UKCC,

  /**
   * RPSGP Code
   */
  RSBP,

  /**
   * GDC Number
   */
  GDC;

  public String value() {
    return name();
  }

  public static VocUserIdentifierType fromValue(String v) {
    return valueOf(v);
  }

}
