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
 * <p>Java class for EQDOC.FrequencyRangeTo complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.FrequencyRangeTo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="operator" type="{http://www.e-mis.com/emisopen}voc.RangeToOperator" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.FrequencyRangeTo", propOrder = {
  "value",
  "operator"
})
public class EQDOCFrequencyRangeTo {

  protected int value;
  @XmlSchemaType(name = "token")
  protected VocRangeToOperator operator;

  /**
   * Gets the value of the value property.
   */
  public int getValue() {
    return value;
  }

  /**
   * Sets the value of the value property.
   */
  public void setValue(int value) {
    this.value = value;
  }

  /**
   * Gets the value of the operator property.
   *
   * @return possible object is
   * {@link VocRangeToOperator }
   */
  public VocRangeToOperator getOperator() {
    return operator;
  }

  /**
   * Sets the value of the operator property.
   *
   * @param value allowed object is
   *              {@link VocRangeToOperator }
   */
  public void setOperator(VocRangeToOperator value) {
    this.operator = value;
  }

}
