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
 * <p>Java class for voc.EventType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.EventType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="OBS"/&gt;
 *     &lt;enumeration value="MED"/&gt;
 *     &lt;enumeration value="TR"/&gt;
 *     &lt;enumeration value="INV"/&gt;
 *     &lt;enumeration value="VAL"/&gt;
 *     &lt;enumeration value="ISS"/&gt;
 *     &lt;enumeration value="ATT"/&gt;
 *     &lt;enumeration value="REF"/&gt;
 *     &lt;enumeration value="DRY"/&gt;
 *     &lt;enumeration value="ALT"/&gt;
 *     &lt;enumeration value="ALL"/&gt;
 *     &lt;enumeration value="FH"/&gt;
 *     &lt;enumeration value="IMM"/&gt;
 *     &lt;enumeration value="PR"/&gt;
 *     &lt;enumeration value="REP"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "voc.EventType")
@XmlEnum
public enum VocEventType {


  /**
   * Observation
   */
  OBS,

  /**
   * Medication
   */
  MED,

  /**
   * Test Request
   */
  TR,

  /**
   * Investigation
   */
  INV,

  /**
   * Value
   */
  VAL,

  /**
   * Medication Issue
   */
  ISS,

  /**
   * Attachment
   */
  ATT,

  /**
   * Referral
   */
  REF,

  /**
   * Diary
   */
  DRY,

  /**
   * Alert
   */
  ALT,

  /**
   * Allergy
   */
  ALL,

  /**
   * Family history
   */
  FH,

  /**
   * Immunisation
   */
  IMM,

  /**
   * Problem Rating
   */
  PR,

  /**
   * Report
   */
  REP;

  public String value() {
    return name();
  }

  public static VocEventType fromValue(String v) {
    return valueOf(v);
  }

}
