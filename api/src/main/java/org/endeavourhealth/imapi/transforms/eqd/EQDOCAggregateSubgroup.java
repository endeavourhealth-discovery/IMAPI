//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for EQDOC.AggregateSubgroup complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.AggregateSubgroup"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.e-mis.com/emisopen}dt.uid"/&gt;
 *         &lt;element name="display" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="displayNameAutoGenerated" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="bandValue" type="{http://www.e-mis.com/emisopen}EQDOC.AggregateBandValue" maxOccurs="unbounded"/&gt;
 *           &lt;element name="code" type="{http://www.e-mis.com/emisopen}EQDOC.ValueSet" maxOccurs="unbounded"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.AggregateSubgroup", propOrder = {
  "id",
  "display",
  "displayNameAutoGenerated",
  "bandValue",
  "code"
})
public class EQDOCAggregateSubgroup {

  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String id;
  @XmlElement(required = true)
  protected String display;
  protected boolean displayNameAutoGenerated;
  protected List<EQDOCAggregateBandValue> bandValue;
  protected List<EQDOCValueSet> code;

  /**
   * Gets the value of the id property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the value of the id property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setId(String value) {
    this.id = value;
  }

  /**
   * Gets the value of the display property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getDisplay() {
    return display;
  }

  /**
   * Sets the value of the display property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setDisplay(String value) {
    this.display = value;
  }

  /**
   * Gets the value of the displayNameAutoGenerated property.
   */
  public boolean isDisplayNameAutoGenerated() {
    return displayNameAutoGenerated;
  }

  /**
   * Sets the value of the displayNameAutoGenerated property.
   */
  public void setDisplayNameAutoGenerated(boolean value) {
    this.displayNameAutoGenerated = value;
  }

  /**
   * Gets the value of the bandValue property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the bandValue property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getBandValue().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCAggregateBandValue }
   */
  public List<EQDOCAggregateBandValue> getBandValue() {
    if (bandValue == null) {
      bandValue = new ArrayList<EQDOCAggregateBandValue>();
    }
    return this.bandValue;
  }

  /**
   * Gets the value of the code property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the code property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getCode().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCValueSet }
   */
  public List<EQDOCValueSet> getCode() {
    if (code == null) {
      code = new ArrayList<EQDOCValueSet>();
    }
    return this.code;
  }

}
