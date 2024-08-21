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
 * <p>Java class for voc.ConfidentialityPolicyNotificationType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.ConfidentialityPolicyNotificationType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="NO"/&gt;
 *     &lt;enumeration value="REPT"/&gt;
 *     &lt;enumeration value="MESG"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "voc.ConfidentialityPolicyNotificationType")
@XmlEnum
public enum VocConfidentialityPolicyNotificationType {


  /**
   * No Notification
   */
  NO,

  /**
   * Notify by Report
   */
  REPT,

  /**
   * Notify by Message
   */
  MESG;

  public String value() {
    return name();
  }

  public static VocConfidentialityPolicyNotificationType fromValue(String v) {
    return valueOf(v);
  }

}
