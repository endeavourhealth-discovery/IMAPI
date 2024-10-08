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
 * <p>Java class for voc.PatientType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.PatientType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="E"/&gt;
 *     &lt;enumeration value="IN"/&gt;
 *     &lt;enumeration value="PRIV"/&gt;
 *     &lt;enumeration value="REG"/&gt;
 *     &lt;enumeration value="TMP"/&gt;
 *     &lt;enumeration value="COMM"/&gt;
 *     &lt;enumeration value="DUM"/&gt;
 *     &lt;enumeration value="OTH"/&gt;
 *     &lt;enumeration value="CON"/&gt;
 *     &lt;enumeration value="MTS"/&gt;
 *     &lt;enumeration value="CHS"/&gt;
 *     &lt;enumeration value="WIP"/&gt;
 *     &lt;enumeration value="MIS"/&gt;
 *     &lt;enumeration value="SEX"/&gt;
 *     &lt;enumeration value="PRE"/&gt;
 *     &lt;enumeration value="YEL"/&gt;
 *     &lt;enumeration value="DER"/&gt;
 *     &lt;enumeration value="DIA"/&gt;
 *     &lt;enumeration value="RHM"/&gt;
 *     &lt;enumeration value="CHR"/&gt;
 *     &lt;enumeration value="CHC"/&gt;
 *     &lt;enumeration value="ULT"/&gt;
 *     &lt;enumeration value="BCG"/&gt;
 *     &lt;enumeration value="VAS"/&gt;
 *     &lt;enumeration value="ACU"/&gt;
 *     &lt;enumeration value="REF"/&gt;
 *     &lt;enumeration value="HYP"/&gt;
 *     &lt;enumeration value="OOH"/&gt;
 *     &lt;enumeration value="RBN"/&gt;
 *     &lt;enumeration value="ANT"/&gt;
 *     &lt;enumeration value="AUD"/&gt;
 *     &lt;enumeration value="GYN"/&gt;
 *     &lt;enumeration value="DOP"/&gt;
 *     &lt;enumeration value="SEC"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "voc.PatientType")
@XmlEnum
public enum VocPatientType {


  /**
   * EMG
   */
  E,

  /**
   * Immediately necessary
   */
  IN,

  /**
   * Private
   */
  PRIV,

  /**
   * Regular
   */
  REG,

  /**
   * Temporary
   */
  TMP,

  /**
   * Community Registered
   */
  COMM,

  /**
   * Dummy
   */
  DUM,

  /**
   * Other
   */
  OTH,

  /**
   * Contraceptive Services
   */
  CON,

  /**
   * Maternity Services
   */
  MTS,

  /**
   * Child Health Services
   */
  CHS,

  /**
   * Walk-In Patient
   */
  WIP,

  /**
   * Minor Surgery
   */
  MIS,

  /**
   * Sexual Health
   */
  SEX,

  /**
   * Pre Registration
   */
  PRE,

  /**
   * Yellow Fever
   */
  YEL,

  /**
   * Dermatology
   */
  DER,

  /**
   * Diabetic
   */
  DIA,

  /**
   * Rheumatology
   */
  RHM,

  /**
   * Chiropody
   */
  CHR,

  /**
   * Coronary Health Checks
   */
  CHC,

  /**
   * Ultrasound
   */
  ULT,

  /**
   * BCG Clinic
   */
  BCG,

  /**
   * Vasectomy
   */
  VAS,

  /**
   * Acupuncture
   */
  ACU,

  /**
   * Reflexology
   */
  REF,

  /**
   * Hypnotherapy
   */
  HYP,

  /**
   * Out of Hours
   */
  OOH,

  /**
   * Rehabilitation
   */
  RBN,

  /**
   * Antenatal
   */
  ANT,

  /**
   * Audiology
   */
  AUD,

  /**
   * Gynaecology
   */
  GYN,

  /**
   * Doppler
   */
  DOP,

  /**
   * Secondary Registration
   */
  SEC;

  public String value() {
    return name();
  }

  public static VocPatientType fromValue(String v) {
    return valueOf(v);
  }

}
