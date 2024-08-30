//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.*;


/**
 * <p>Java class for EQDOC.AggregateLibraryBanding complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.AggregateLibraryBanding"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="inputAction" type="{http://www.e-mis.com/emisopen}voc.InputAction"/&gt;
 *         &lt;element name="banding" type="{http://www.e-mis.com/emisopen}EQDOC.AggregateBanding"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.AggregateLibraryBanding", propOrder = {
  "inputAction",
  "banding"
})
public class EQDOCAggregateLibraryBanding {

  @XmlElement(required = true)
  @XmlSchemaType(name = "string")
  protected VocInputAction inputAction;
  @XmlElement(required = true)
  protected EQDOCAggregateBanding banding;

  /**
   * Gets the value of the inputAction property.
   *
   * @return possible object is
   * {@link VocInputAction }
   */
  public VocInputAction getInputAction() {
    return inputAction;
  }

  /**
   * Sets the value of the inputAction property.
   *
   * @param value allowed object is
   *              {@link VocInputAction }
   */
  public void setInputAction(VocInputAction value) {
    this.inputAction = value;
  }

  /**
   * Gets the value of the banding property.
   *
   * @return possible object is
   * {@link EQDOCAggregateBanding }
   */
  public EQDOCAggregateBanding getBanding() {
    return banding;
  }

  /**
   * Sets the value of the banding property.
   *
   * @param value allowed object is
   *              {@link EQDOCAggregateBanding }
   */
  public void setBanding(EQDOCAggregateBanding value) {
    this.banding = value;
  }

}
