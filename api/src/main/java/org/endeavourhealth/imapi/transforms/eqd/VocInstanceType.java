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
 * <p>Java class for voc.InstanceType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.InstanceType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *     &lt;enumeration value="HOW_AM_I_DRIVING"/&gt;
 *     &lt;enumeration value="LOOK_AHEAD_3"/&gt;
 *     &lt;enumeration value="REGULAR"/&gt;
 *     &lt;enumeration value="INTERIM"/&gt;
 *     &lt;enumeration value="CROSS_DB_CHILD"/&gt;
 *     &lt;enumeration value="LOOK_AHEAD_1"/&gt;
 *     &lt;enumeration value="LOOK_AHEAD_FIANCIAL"/&gt;
 *     &lt;enumeration value="QSURV_DAILY"/&gt;
 *     &lt;enumeration value="QSURV_WEEKLY_INCIDENCE"/&gt;
 *     &lt;enumeration value="QSURV_WEEKLY_TREND"/&gt;
 *     &lt;enumeration value="QSURV_MONTHLY"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "voc.InstanceType")
@XmlEnum
public enum VocInstanceType {

  DEFAULT,
  HOW_AM_I_DRIVING,
  LOOK_AHEAD_3,
  REGULAR,
  INTERIM,
  CROSS_DB_CHILD,
  LOOK_AHEAD_1,
  LOOK_AHEAD_FIANCIAL,
  QSURV_DAILY,
  QSURV_WEEKLY_INCIDENCE,
  QSURV_WEEKLY_TREND,
  QSURV_MONTHLY;

  public String value() {
    return name();
  }

  public static VocInstanceType fromValue(String v) {
    return valueOf(v);
  }

}
