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


/**
 * <p>Java class for EQDOC.Relationship complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.Relationship"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="parentColumn" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
 *         &lt;element name="parentColumnDisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="childColumn" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
 *         &lt;element name="childColumnDisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rangeValue" type="{http://www.e-mis.com/emisopen}EQDOC.RangeValue" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.Relationship", propOrder = {
  "parentColumn",
  "parentColumnDisplayName",
  "childColumn",
  "childColumnDisplayName",
  "rangeValue"
})
public class EQDOCRelationship {

  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String parentColumn;
  protected String parentColumnDisplayName;
  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String childColumn;
  protected String childColumnDisplayName;
  protected EQDOCRangeValue rangeValue;

  /**
   * Gets the value of the parentColumn property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getParentColumn() {
    return parentColumn;
  }

  /**
   * Sets the value of the parentColumn property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setParentColumn(String value) {
    this.parentColumn = value;
  }

  /**
   * Gets the value of the parentColumnDisplayName property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getParentColumnDisplayName() {
    return parentColumnDisplayName;
  }

  /**
   * Sets the value of the parentColumnDisplayName property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setParentColumnDisplayName(String value) {
    this.parentColumnDisplayName = value;
  }

  /**
   * Gets the value of the childColumn property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getChildColumn() {
    return childColumn;
  }

  /**
   * Sets the value of the childColumn property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setChildColumn(String value) {
    this.childColumn = value;
  }

  /**
   * Gets the value of the childColumnDisplayName property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getChildColumnDisplayName() {
    return childColumnDisplayName;
  }

  /**
   * Sets the value of the childColumnDisplayName property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setChildColumnDisplayName(String value) {
    this.childColumnDisplayName = value;
  }

  /**
   * Gets the value of the rangeValue property.
   *
   * @return possible object is
   * {@link EQDOCRangeValue }
   */
  public EQDOCRangeValue getRangeValue() {
    return rangeValue;
  }

  /**
   * Sets the value of the rangeValue property.
   *
   * @param value allowed object is
   *              {@link EQDOCRangeValue }
   */
  public void setRangeValue(EQDOCRangeValue value) {
    this.rangeValue = value;
  }

}
