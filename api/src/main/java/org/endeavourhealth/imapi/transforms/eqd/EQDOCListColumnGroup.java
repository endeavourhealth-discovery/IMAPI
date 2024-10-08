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
 * <p>Java class for EQDOC.ListColumnGroup complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.ListColumnGroup"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.e-mis.com/emisopen}dt.uid"/&gt;
 *         &lt;element name="logicalTableName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="columnar" type="{http://www.e-mis.com/emisopen}EQDOC.ListColumns"/&gt;
 *           &lt;element name="summary" type="{http://www.e-mis.com/emisopen}voc.ListGroupSummary"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="summarise" type="{http://www.e-mis.com/emisopen}EQDOC.Summarise" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="criteria" type="{http://www.e-mis.com/emisopen}EQDOC.Criteria" minOccurs="0"/&gt;
 *         &lt;element name="populationFilter" type="{http://www.e-mis.com/emisopen}EQDOC.CriteriaGroup" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.ListColumnGroup", propOrder = {
  "id",
  "logicalTableName",
  "displayName",
  "columnar",
  "summary",
  "summarise",
  "criteria",
  "populationFilter"
})
public class EQDOCListColumnGroup {

  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String id;
  @XmlElement(required = true)
  protected String logicalTableName;
  protected String displayName;
  protected EQDOCListColumns columnar;
  @XmlSchemaType(name = "token")
  protected VocListGroupSummary summary;
  protected List<EQDOCSummarise> summarise;
  protected EQDOCCriteria criteria;
  protected List<EQDOCCriteriaGroup> populationFilter;

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
   * Gets the value of the logicalTableName property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getLogicalTableName() {
    return logicalTableName;
  }

  /**
   * Sets the value of the logicalTableName property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setLogicalTableName(String value) {
    this.logicalTableName = value;
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
   * Gets the value of the columnar property.
   *
   * @return possible object is
   * {@link EQDOCListColumns }
   */
  public EQDOCListColumns getColumnar() {
    return columnar;
  }

  /**
   * Sets the value of the columnar property.
   *
   * @param value allowed object is
   *              {@link EQDOCListColumns }
   */
  public void setColumnar(EQDOCListColumns value) {
    this.columnar = value;
  }

  /**
   * Gets the value of the summary property.
   *
   * @return possible object is
   * {@link VocListGroupSummary }
   */
  public VocListGroupSummary getSummary() {
    return summary;
  }

  /**
   * Sets the value of the summary property.
   *
   * @param value allowed object is
   *              {@link VocListGroupSummary }
   */
  public void setSummary(VocListGroupSummary value) {
    this.summary = value;
  }

  /**
   * Gets the value of the summarise property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the summarise property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getSummarise().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCSummarise }
   */
  public List<EQDOCSummarise> getSummarise() {
    if (summarise == null) {
      summarise = new ArrayList<EQDOCSummarise>();
    }
    return this.summarise;
  }

  /**
   * Gets the value of the criteria property.
   *
   * @return possible object is
   * {@link EQDOCCriteria }
   */
  public EQDOCCriteria getCriteria() {
    return criteria;
  }

  /**
   * Sets the value of the criteria property.
   *
   * @param value allowed object is
   *              {@link EQDOCCriteria }
   */
  public void setCriteria(EQDOCCriteria value) {
    this.criteria = value;
  }

  /**
   * Gets the value of the populationFilter property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the populationFilter property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getPopulationFilter().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCCriteriaGroup }
   */
  public List<EQDOCCriteriaGroup> getPopulationFilter() {
    if (populationFilter == null) {
      populationFilter = new ArrayList<EQDOCCriteriaGroup>();
    }
    return this.populationFilter;
  }

}
