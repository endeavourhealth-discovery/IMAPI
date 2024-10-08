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
 * <p>Java class for voc.CaseloadPatientProperty.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.CaseloadPatientProperty"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="WQ"/&gt;
 *     &lt;enumeration value="WM"/&gt;
 *     &lt;enumeration value="RM"/&gt;
 *     &lt;enumeration value="RDM"/&gt;
 *     &lt;enumeration value="FM"/&gt;
 *     &lt;enumeration value="BSM"/&gt;
 *     &lt;enumeration value="TPHA"/&gt;
 *     &lt;enumeration value="RIL"/&gt;
 *     &lt;enumeration value="BS"/&gt;
 *     &lt;enumeration value="RA"/&gt;
 *     &lt;enumeration value="FZ"/&gt;
 *     &lt;enumeration value="DISP"/&gt;
 *     &lt;enumeration value="MSM"/&gt;
 *     &lt;enumeration value="DQ"/&gt;
 *     &lt;enumeration value="EED"/&gt;
 *     &lt;enumeration value="RD"/&gt;
 *     &lt;enumeration value="RS"/&gt;
 *     &lt;enumeration value="RESS"/&gt;
 *     &lt;enumeration value="AWN"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "voc.CaseloadPatientProperty")
@XmlEnum
public enum VocCaseloadPatientProperty {


  /**
   * Walking Quarters
   */
  WQ,

  /**
   * Water Miles
   */
  WM,

  /**
   * Rural Mileage
   */
  RM,

  /**
   * Road Mileage
   */
  RDM,

  /**
   * Foot Milage
   */
  FM,

  /**
   * Blocked Special Marker
   */
  BSM,

  /**
   * Trading Partner HA
   */
  TPHA,

  /**
   * Residential Institute Location
   */
  RIL,

  /**
   * Branch Surgery Organisation
   */
  BS,

  /**
   * Records At
   */
  RA,

  /**
   * Freeze Flag
   */
  FZ,

  /**
   * Dispensing
   */
  DISP,

  /**
   * Medication Screen Message
   */
  MSM,

  /**
   * Difficult Quarters
   */
  DQ,

  /**
   * Exemption Expiry Date
   */
  EED,

  /**
   * Review Date
   */
  RD,

  /**
   * Reminder Sent
   */
  RS,

  /**
   * Resident Status
   */
  RESS,

  /**
   * Automatic Week Number
   */
  AWN;

  public String value() {
    return name();
  }

  public static VocCaseloadPatientProperty fromValue(String v) {
    return valueOf(v);
  }

}
