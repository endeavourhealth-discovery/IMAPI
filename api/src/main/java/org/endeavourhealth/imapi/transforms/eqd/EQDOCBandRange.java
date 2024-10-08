//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EQDOC.BandRange complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.BandRange"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="rangeFrom" type="{http://www.e-mis.com/emisopen}EQDOC.BandRangeFrom" minOccurs="0"/&gt;
 *         &lt;element name="rangeTo" type="{http://www.e-mis.com/emisopen}EQDOC.BandRangeTo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.BandRange", propOrder = {
  "rangeFrom",
  "rangeTo"
})
public class EQDOCBandRange {

  protected EQDOCBandRangeFrom rangeFrom;
  protected EQDOCBandRangeTo rangeTo;

  /**
   * Gets the value of the rangeFrom property.
   *
   * @return possible object is
   * {@link EQDOCBandRangeFrom }
   */
  public EQDOCBandRangeFrom getRangeFrom() {
    return rangeFrom;
  }

  /**
   * Sets the value of the rangeFrom property.
   *
   * @param value allowed object is
   *              {@link EQDOCBandRangeFrom }
   */
  public void setRangeFrom(EQDOCBandRangeFrom value) {
    this.rangeFrom = value;
  }

  /**
   * Gets the value of the rangeTo property.
   *
   * @return possible object is
   * {@link EQDOCBandRangeTo }
   */
  public EQDOCBandRangeTo getRangeTo() {
    return rangeTo;
  }

  /**
   * Sets the value of the rangeTo property.
   *
   * @param value allowed object is
   *              {@link EQDOCBandRangeTo }
   */
  public void setRangeTo(EQDOCBandRangeTo value) {
    this.rangeTo = value;
  }

}
