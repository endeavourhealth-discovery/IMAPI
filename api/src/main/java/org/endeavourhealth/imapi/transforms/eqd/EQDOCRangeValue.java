//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EQDOC.RangeValue complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.RangeValue"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="rangeFrom" type="{http://www.e-mis.com/emisopen}EQDOC.RangeFrom" minOccurs="0"/&gt;
 *         &lt;element name="rangeTo" type="{http://www.e-mis.com/emisopen}EQDOC.RangeTo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="relativeTo" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.RangeValue", propOrder = {
  "rangeFrom",
  "rangeTo"
})
public class EQDOCRangeValue {

  protected EQDOCRangeFrom rangeFrom;
  protected EQDOCRangeTo rangeTo;
  @XmlAttribute(name = "relativeTo")
  protected String relativeTo;

  /**
   * Gets the value of the rangeFrom property.
   *
   * @return possible object is
   * {@link EQDOCRangeFrom }
   */
  public EQDOCRangeFrom getRangeFrom() {
    return rangeFrom;
  }

  /**
   * Sets the value of the rangeFrom property.
   *
   * @param value allowed object is
   *              {@link EQDOCRangeFrom }
   */
  public void setRangeFrom(EQDOCRangeFrom value) {
    this.rangeFrom = value;
  }

  /**
   * Gets the value of the rangeTo property.
   *
   * @return possible object is
   * {@link EQDOCRangeTo }
   */
  public EQDOCRangeTo getRangeTo() {
    return rangeTo;
  }

  /**
   * Sets the value of the rangeTo property.
   *
   * @param value allowed object is
   *              {@link EQDOCRangeTo }
   */
  public void setRangeTo(EQDOCRangeTo value) {
    this.rangeTo = value;
  }

  /**
   * Gets the value of the relativeTo property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getRelativeTo() {
    return relativeTo;
  }

  /**
   * Sets the value of the relativeTo property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setRelativeTo(String value) {
    this.relativeTo = value;
  }

}
