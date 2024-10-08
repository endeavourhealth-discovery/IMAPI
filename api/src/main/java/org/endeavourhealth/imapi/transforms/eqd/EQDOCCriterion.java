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
 * <p>Java class for EQDOC.Criterion complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.Criterion"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.e-mis.com/emisopen}dt.uid"/&gt;
 *         &lt;element name="table" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="baseCriteriaGroup" type="{http://www.e-mis.com/emisopen}EQDOC.BaseCriteriaGroup" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="negation" type="{http://www.e-mis.com/emisopen}dt.bool" minOccurs="0"/&gt;
 *         &lt;element name="filterAttribute" type="{http://www.e-mis.com/emisopen}EQDOC.FilterAttribute"/&gt;
 *         &lt;element name="linkedCriterion" type="{http://www.e-mis.com/emisopen}EQDOC.LinkedCriterion" minOccurs="0"/&gt;
 *         &lt;element name="exceptionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.Criterion", propOrder = {
  "id",
  "table",
  "displayName",
  "description",
  "baseCriteriaGroup",
  "negation",
  "filterAttribute",
  "linkedCriterion",
  "exceptionCode"
})
public class EQDOCCriterion {

  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String id;
  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String table;
  @XmlElement(required = true)
  protected String displayName;
  protected String description;
  protected List<EQDOCBaseCriteriaGroup> baseCriteriaGroup;
  @XmlElement(defaultValue = "false")
  protected Boolean negation;
  @XmlElement(required = true)
  protected EQDOCFilterAttribute filterAttribute;
  protected EQDOCLinkedCriterion linkedCriterion;
  protected String exceptionCode;

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
   * Gets the value of the table property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getTable() {
    return table;
  }

  /**
   * Sets the value of the table property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setTable(String value) {
    this.table = value;
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
   * Gets the value of the description property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the value of the description property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setDescription(String value) {
    this.description = value;
  }

  /**
   * Gets the value of the baseCriteriaGroup property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the baseCriteriaGroup property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getBaseCriteriaGroup().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCBaseCriteriaGroup }
   */
  public List<EQDOCBaseCriteriaGroup> getBaseCriteriaGroup() {
    if (baseCriteriaGroup == null) {
      baseCriteriaGroup = new ArrayList<EQDOCBaseCriteriaGroup>();
    }
    return this.baseCriteriaGroup;
  }

  /**
   * Gets the value of the negation property.
   *
   * @return possible object is
   * {@link Boolean }
   */
  public Boolean isNegation() {
    return negation;
  }

  /**
   * Sets the value of the negation property.
   *
   * @param value allowed object is
   *              {@link Boolean }
   */
  public void setNegation(Boolean value) {
    this.negation = value;
  }

  /**
   * Gets the value of the filterAttribute property.
   *
   * @return possible object is
   * {@link EQDOCFilterAttribute }
   */
  public EQDOCFilterAttribute getFilterAttribute() {
    return filterAttribute;
  }

  /**
   * Sets the value of the filterAttribute property.
   *
   * @param value allowed object is
   *              {@link EQDOCFilterAttribute }
   */
  public void setFilterAttribute(EQDOCFilterAttribute value) {
    this.filterAttribute = value;
  }

  /**
   * Gets the value of the linkedCriterion property.
   *
   * @return possible object is
   * {@link EQDOCLinkedCriterion }
   */
  public EQDOCLinkedCriterion getLinkedCriterion() {
    return linkedCriterion;
  }

  /**
   * Sets the value of the linkedCriterion property.
   *
   * @param value allowed object is
   *              {@link EQDOCLinkedCriterion }
   */
  public void setLinkedCriterion(EQDOCLinkedCriterion value) {
    this.linkedCriterion = value;
  }

  /**
   * Gets the value of the exceptionCode property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getExceptionCode() {
    return exceptionCode;
  }

  /**
   * Sets the value of the exceptionCode property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setExceptionCode(String value) {
    this.exceptionCode = value;
  }

}
