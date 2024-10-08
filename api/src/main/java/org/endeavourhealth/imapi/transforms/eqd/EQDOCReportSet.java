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
 * <p>Java class for EQDOC.ReportSet complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.ReportSet"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="guid" type="{http://www.e-mis.com/emisopen}dt.uid"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="instanceType" type="{http://www.e-mis.com/emisopen}voc.InstanceType" minOccurs="0"/&gt;
 *         &lt;element name="schedule" type="{http://www.e-mis.com/emisopen}EQDOC.Schedule" minOccurs="0"/&gt;
 *         &lt;element name="reportCollectionGuid" type="{http://www.e-mis.com/emisopen}dt.uid" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.ReportSet", propOrder = {
  "guid",
  "name",
  "description",
  "instanceType",
  "schedule",
  "reportCollectionGuid"
})
public class EQDOCReportSet {

  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String guid;
  @XmlElement(required = true)
  protected String name;
  protected String description;
  @XmlSchemaType(name = "string")
  protected VocInstanceType instanceType;
  protected EQDOCSchedule schedule;
  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected List<String> reportCollectionGuid;

  /**
   * Gets the value of the guid property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getGuid() {
    return guid;
  }

  /**
   * Sets the value of the guid property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setGuid(String value) {
    this.guid = value;
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
   * Gets the value of the instanceType property.
   *
   * @return possible object is
   * {@link VocInstanceType }
   */
  public VocInstanceType getInstanceType() {
    return instanceType;
  }

  /**
   * Sets the value of the instanceType property.
   *
   * @param value allowed object is
   *              {@link VocInstanceType }
   */
  public void setInstanceType(VocInstanceType value) {
    this.instanceType = value;
  }

  /**
   * Gets the value of the schedule property.
   *
   * @return possible object is
   * {@link EQDOCSchedule }
   */
  public EQDOCSchedule getSchedule() {
    return schedule;
  }

  /**
   * Sets the value of the schedule property.
   *
   * @param value allowed object is
   *              {@link EQDOCSchedule }
   */
  public void setSchedule(EQDOCSchedule value) {
    this.schedule = value;
  }

  /**
   * Gets the value of the reportCollectionGuid property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the reportCollectionGuid property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getReportCollectionGuid().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link String }
   */
  public List<String> getReportCollectionGuid() {
    if (reportCollectionGuid == null) {
      reportCollectionGuid = new ArrayList<String>();
    }
    return this.reportCollectionGuid;
  }

}
