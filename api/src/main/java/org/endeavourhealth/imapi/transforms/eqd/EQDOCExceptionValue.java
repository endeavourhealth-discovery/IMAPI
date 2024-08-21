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
 * <p>Java class for EQDOC.ExceptionValue complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.ExceptionValue"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="includeChildren" type="{http://www.e-mis.com/emisopen}dt.bool"/&gt;
 *         &lt;element name="qualifier" type="{http://www.e-mis.com/emisopen}dt.Qualifier" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="legacyValue" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.ExceptionValue", propOrder = {
  "value",
  "displayName",
  "includeChildren",
  "qualifier",
  "legacyValue"
})
public class EQDOCExceptionValue {

  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String value;
  protected String displayName;
  protected boolean includeChildren;
  protected List<DtQualifier> qualifier;
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String legacyValue;

  /**
   * Gets the value of the value property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value of the value property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Gets the value of the displayName property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets the value of the displayName property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setDisplayName(String value) {
    this.displayName = value;
  }

  /**
   * Gets the value of the includeChildren property.
   */
  public boolean isIncludeChildren() {
    return includeChildren;
  }

  /**
   * Sets the value of the includeChildren property.
   */
  public void setIncludeChildren(boolean value) {
    this.includeChildren = value;
  }

  /**
   * Gets the value of the qualifier property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the qualifier property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getQualifier().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link DtQualifier }
   */
  public List<DtQualifier> getQualifier() {
    if (qualifier == null) {
      qualifier = new ArrayList<DtQualifier>();
    }
    return this.qualifier;
  }

  /**
   * Gets the value of the legacyValue property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getLegacyValue() {
    return legacyValue;
  }

  /**
   * Sets the value of the legacyValue property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setLegacyValue(String value) {
    this.legacyValue = value;
  }

}
