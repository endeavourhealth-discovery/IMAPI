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
 * <p>Java class for voc.DatePart.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.DatePart"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="U"/&gt;
 *     &lt;enumeration value="Y"/&gt;
 *     &lt;enumeration value="YM"/&gt;
 *     &lt;enumeration value="YMD"/&gt;
 *     &lt;enumeration value="YMDT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "voc.DatePart")
@XmlEnum
public enum VocDatePart {


  /**
   * Unknown Date
   */
  U,

  /**
   * Year e.g. to indicate 1999 write: 1999-01-01T00:00:00
   */
  Y,

  /**
   * Year and month e.g. to indicate May, 1999 write: 1999-05-01T00:00:00
   */
  YM,

  /**
   * Year, month and day e.g. to indicate May the 31st, 1999 write: 1999-05-31T00:00:00
   */
  YMD,

  /**
   * Year, month, day and time e.g. to indicate 1:20 pm on May the 31st, 1999 write: 1999-05-31T13:20:00
   */
  YMDT;

  public String value() {
    return name();
  }

  public static VocDatePart fromValue(String v) {
    return valueOf(v);
  }

}
