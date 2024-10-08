//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EQDOC.AggregateBandValue complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.AggregateBandValue"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="range" type="{http://www.e-mis.com/emisopen}EQDOC.BandRange"/&gt;
 *         &lt;element name="value" type="{http://www.e-mis.com/emisopen}EQDOC.BandSingleValue"/&gt;
 *         &lt;element name="unknown"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="unit" type="{http://www.e-mis.com/emisopen}voc.ValueUnit" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.AggregateBandValue", propOrder = {
  "range",
  "value",
  "unknown"
})
public class EQDOCAggregateBandValue {

  protected EQDOCBandRange range;
  protected EQDOCBandSingleValue value;
  protected Unknown unknown;

  /**
   * Gets the value of the range property.
   *
   * @return possible object is
   * {@link EQDOCBandRange }
   */
  public EQDOCBandRange getRange() {
    return range;
  }

  /**
   * Sets the value of the range property.
   *
   * @param value allowed object is
   *              {@link EQDOCBandRange }
   */
  public void setRange(EQDOCBandRange value) {
    this.range = value;
  }

  /**
   * Gets the value of the value property.
   *
   * @return possible object is
   * {@link EQDOCBandSingleValue }
   */
  public EQDOCBandSingleValue getValue() {
    return value;
  }

  /**
   * Sets the value of the value property.
   *
   * @param value allowed object is
   *              {@link EQDOCBandSingleValue }
   */
  public void setValue(EQDOCBandSingleValue value) {
    this.value = value;
  }

  /**
   * Gets the value of the unknown property.
   *
   * @return possible object is
   * {@link Unknown }
   */
  public Unknown getUnknown() {
    return unknown;
  }

  /**
   * Sets the value of the unknown property.
   *
   * @param value allowed object is
   *              {@link Unknown }
   */
  public void setUnknown(Unknown value) {
    this.unknown = value;
  }


  /**
   * <p>Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType&gt;
   *   &lt;complexContent&gt;
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *       &lt;sequence&gt;
   *         &lt;element name="unit" type="{http://www.e-mis.com/emisopen}voc.ValueUnit" minOccurs="0"/&gt;
   *       &lt;/sequence&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {
    "unit"
  })
  public static class Unknown {

    @XmlSchemaType(name = "token")
    protected VocValueUnit unit;

    /**
     * Gets the value of the unit property.
     *
     * @return possible object is
     * {@link VocValueUnit }
     */
    public VocValueUnit getUnit() {
      return unit;
    }

    /**
     * Sets the value of the unit property.
     *
     * @param value allowed object is
     *              {@link VocValueUnit }
     */
    public void setUnit(VocValueUnit value) {
      this.unit = value;
    }

  }

}
