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

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for EQDOC.LibraryItem complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.LibraryItem"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.e-mis.com/emisopen}dt.dbo"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.e-mis.com/emisopen}dt.uid"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="version" type="{http://www.e-mis.com/emisopen}dt.Version"/&gt;
 *         &lt;element name="creationTime" type="{http://www.e-mis.com/emisopen}dt.DateTime"/&gt;
 *         &lt;element name="author" type="{http://www.e-mis.com/emisopen}EQDOC.Author" minOccurs="0"/&gt;
 *         &lt;element name="readOnly" type="{http://www.e-mis.com/emisopen}dt.bool"/&gt;
 *         &lt;element name="populationType" type="{http://www.e-mis.com/emisopen}voc.PopulationType"/&gt;
 *         &lt;element name="obsolete" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="criterion" type="{http://www.e-mis.com/emisopen}EQDOC.Criterion"/&gt;
 *           &lt;element name="criteriaGroup" type="{http://www.e-mis.com/emisopen}EQDOC.CriteriaGroupDefinition"/&gt;
 *           &lt;element name="listReport" type="{http://www.e-mis.com/emisopen}EQDOC.ListReport"/&gt;
 *           &lt;element name="aggregateReport" type="{http://www.e-mis.com/emisopen}EQDOC.AggregateReport"/&gt;
 *           &lt;element name="listColumnGroup" type="{http://www.e-mis.com/emisopen}EQDOC.ListColumnGroup" maxOccurs="unbounded"/&gt;
 *           &lt;element name="valueSet" type="{http://www.e-mis.com/emisopen}EQDOC.ValueSet" maxOccurs="unbounded"/&gt;
 *           &lt;element name="population" type="{http://www.e-mis.com/emisopen}EQDOC.PopulationDefinition"/&gt;
 *           &lt;element name="aggregateBanding" type="{http://www.e-mis.com/emisopen}EQDOC.AggregateLibraryBanding"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="metaTags" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.LibraryItem", propOrder = {
  "id",
  "name",
  "description",
  "version",
  "creationTime",
  "author",
  "readOnly",
  "populationType",
  "obsolete",
  "criterion",
  "criteriaGroup",
  "listReport",
  "aggregateReport",
  "listColumnGroup",
  "valueSet",
  "population",
  "aggregateBanding",
  "metaTags"
})
public class EQDOCLibraryItem
  extends DtDbo {

  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String id;
  @XmlElement(required = true)
  protected String name;
  @XmlElement(required = true)
  protected String description;
  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String version;
  @XmlElement(required = true)
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar creationTime;
  protected EQDOCAuthor author;
  protected boolean readOnly;
  @XmlElement(required = true)
  @XmlSchemaType(name = "token")
  protected VocPopulationType populationType;
  @XmlElement(defaultValue = "false")
  protected Boolean obsolete;
  protected EQDOCCriterion criterion;
  protected EQDOCCriteriaGroupDefinition criteriaGroup;
  protected EQDOCListReport listReport;
  protected EQDOCAggregateReport aggregateReport;
  protected List<EQDOCListColumnGroup> listColumnGroup;
  protected List<EQDOCValueSet> valueSet;
  protected EQDOCPopulationDefinition population;
  protected EQDOCAggregateLibraryBanding aggregateBanding;
  protected List<String> metaTags;

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
   * Gets the value of the name property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the value of the name property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setName(String value) {
    this.name = value;
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
   * Gets the value of the version property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getVersion() {
    return version;
  }

  /**
   * Sets the value of the version property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setVersion(String value) {
    this.version = value;
  }

  /**
   * Gets the value of the creationTime property.
   *
   * @return possible object is
   * {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getCreationTime() {
    return creationTime;
  }

  /**
   * Sets the value of the creationTime property.
   *
   * @param value allowed object is
   *              {@link XMLGregorianCalendar }
   */
  public void setCreationTime(XMLGregorianCalendar value) {
    this.creationTime = value;
  }

  /**
   * Gets the value of the author property.
   *
   * @return possible object is
   * {@link EQDOCAuthor }
   */
  public EQDOCAuthor getAuthor() {
    return author;
  }

  /**
   * Sets the value of the author property.
   *
   * @param value allowed object is
   *              {@link EQDOCAuthor }
   */
  public void setAuthor(EQDOCAuthor value) {
    this.author = value;
  }

  /**
   * Gets the value of the readOnly property.
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * Sets the value of the readOnly property.
   */
  public void setReadOnly(boolean value) {
    this.readOnly = value;
  }

  /**
   * Gets the value of the populationType property.
   *
   * @return possible object is
   * {@link VocPopulationType }
   */
  public VocPopulationType getPopulationType() {
    return populationType;
  }

  /**
   * Sets the value of the populationType property.
   *
   * @param value allowed object is
   *              {@link VocPopulationType }
   */
  public void setPopulationType(VocPopulationType value) {
    this.populationType = value;
  }

  /**
   * Gets the value of the obsolete property.
   *
   * @return possible object is
   * {@link Boolean }
   */
  public Boolean isObsolete() {
    return obsolete;
  }

  /**
   * Sets the value of the obsolete property.
   *
   * @param value allowed object is
   *              {@link Boolean }
   */
  public void setObsolete(Boolean value) {
    this.obsolete = value;
  }

  /**
   * Gets the value of the criterion property.
   *
   * @return possible object is
   * {@link EQDOCCriterion }
   */
  public EQDOCCriterion getCriterion() {
    return criterion;
  }

  /**
   * Sets the value of the criterion property.
   *
   * @param value allowed object is
   *              {@link EQDOCCriterion }
   */
  public void setCriterion(EQDOCCriterion value) {
    this.criterion = value;
  }

  /**
   * Gets the value of the criteriaGroup property.
   *
   * @return possible object is
   * {@link EQDOCCriteriaGroupDefinition }
   */
  public EQDOCCriteriaGroupDefinition getCriteriaGroup() {
    return criteriaGroup;
  }

  /**
   * Sets the value of the criteriaGroup property.
   *
   * @param value allowed object is
   *              {@link EQDOCCriteriaGroupDefinition }
   */
  public void setCriteriaGroup(EQDOCCriteriaGroupDefinition value) {
    this.criteriaGroup = value;
  }

  /**
   * Gets the value of the listReport property.
   *
   * @return possible object is
   * {@link EQDOCListReport }
   */
  public EQDOCListReport getListReport() {
    return listReport;
  }

  /**
   * Sets the value of the listReport property.
   *
   * @param value allowed object is
   *              {@link EQDOCListReport }
   */
  public void setListReport(EQDOCListReport value) {
    this.listReport = value;
  }

  /**
   * Gets the value of the aggregateReport property.
   *
   * @return possible object is
   * {@link EQDOCAggregateReport }
   */
  public EQDOCAggregateReport getAggregateReport() {
    return aggregateReport;
  }

  /**
   * Sets the value of the aggregateReport property.
   *
   * @param value allowed object is
   *              {@link EQDOCAggregateReport }
   */
  public void setAggregateReport(EQDOCAggregateReport value) {
    this.aggregateReport = value;
  }

  /**
   * Gets the value of the listColumnGroup property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the listColumnGroup property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getListColumnGroup().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCListColumnGroup }
   */
  public List<EQDOCListColumnGroup> getListColumnGroup() {
    if (listColumnGroup == null) {
      listColumnGroup = new ArrayList<EQDOCListColumnGroup>();
    }
    return this.listColumnGroup;
  }

  /**
   * Gets the value of the valueSet property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the valueSet property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getValueSet().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCValueSet }
   */
  public List<EQDOCValueSet> getValueSet() {
    if (valueSet == null) {
      valueSet = new ArrayList<EQDOCValueSet>();
    }
    return this.valueSet;
  }

  /**
   * Gets the value of the population property.
   *
   * @return possible object is
   * {@link EQDOCPopulationDefinition }
   */
  public EQDOCPopulationDefinition getPopulation() {
    return population;
  }

  /**
   * Sets the value of the population property.
   *
   * @param value allowed object is
   *              {@link EQDOCPopulationDefinition }
   */
  public void setPopulation(EQDOCPopulationDefinition value) {
    this.population = value;
  }

  /**
   * Gets the value of the aggregateBanding property.
   *
   * @return possible object is
   * {@link EQDOCAggregateLibraryBanding }
   */
  public EQDOCAggregateLibraryBanding getAggregateBanding() {
    return aggregateBanding;
  }

  /**
   * Sets the value of the aggregateBanding property.
   *
   * @param value allowed object is
   *              {@link EQDOCAggregateLibraryBanding }
   */
  public void setAggregateBanding(EQDOCAggregateLibraryBanding value) {
    this.aggregateBanding = value;
  }

  /**
   * Gets the value of the metaTags property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the metaTags property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getMetaTags().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link String }
   */
  public List<String> getMetaTags() {
    if (metaTags == null) {
      metaTags = new ArrayList<String>();
    }
    return this.metaTags;
  }

}
