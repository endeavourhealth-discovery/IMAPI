//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for EQDOC.GpesAggregateFromReport complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.GpesAggregateFromReport"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="AID" use="required" type="{http://www.e-mis.com/emisopen}dt.uid" /&gt;
 *       &lt;attribute name="reportGuid" use="required" type="{http://www.e-mis.com/emisopen}dt.uid" /&gt;
 *       &lt;attribute name="description" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.GpesAggregateFromReport")
public class EQDOCGpesAggregateFromReport {

  @XmlAttribute(name = "AID", required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  protected String aid;
  @XmlAttribute(name = "reportGuid", required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  protected String reportGuid;
  @XmlAttribute(name = "description", required = true)
  protected String description;

  /**
   * Gets the value of the aid property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getAID() {
    return aid;
  }

  /**
   * Sets the value of the aid property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setAID(String value) {
    this.aid = value;
  }

  /**
   * Gets the value of the reportGuid property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getReportGuid() {
    return reportGuid;
  }

  /**
   * Sets the value of the reportGuid property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setReportGuid(String value) {
    this.reportGuid = value;
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

}
