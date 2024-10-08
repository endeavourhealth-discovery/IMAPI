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
 * <p>Java class for voc.ConsultationType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.ConsultationType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="GP"/&gt;
 *     &lt;enumeration value="ASS"/&gt;
 *     &lt;enumeration value="INT"/&gt;
 *     &lt;enumeration value="TFC"/&gt;
 *     &lt;enumeration value="TTC"/&gt;
 *     &lt;enumeration value="TFP"/&gt;
 *     &lt;enumeration value="TTP"/&gt;
 *     &lt;enumeration value="TFO"/&gt;
 *     &lt;enumeration value="TTO"/&gt;
 *     &lt;enumeration value="FFC"/&gt;
 *     &lt;enumeration value="FFP"/&gt;
 *     &lt;enumeration value="FFO"/&gt;
 *     &lt;enumeration value="OC"/&gt;
 *     &lt;enumeration value="HV"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "voc.ConsultationType")
@XmlEnum
public enum VocConsultationType {


  /**
   * GP Consultation
   */
  GP,

  /**
   * Assessment
   */
  ASS,

  /**
   * Intervention
   */
  INT,

  /**
   * Telephone call from client
   */
  TFC,

  /**
   * Telephone call to client
   */
  TTC,

  /**
   * Telephone call from parent/guardian
   */
  TFP,

  /**
   * Telephone call to parent/guardian
   */
  TTP,

  /**
   * Telephone call from other
   */
  TFO,

  /**
   * Telephone call to other
   */
  TTO,

  /**
   * Face to face with client
   */
  FFC,

  /**
   * Face to face with parent/guardian
   */
  FFP,

  /**
   * Face to face with other
   */
  FFO,

  /**
   * Other communications
   */
  OC,

  /**
   * Home Visit
   */
  HV;

  public String value() {
    return name();
  }

  public static VocConsultationType fromValue(String v) {
    return valueOf(v);
  }

}
