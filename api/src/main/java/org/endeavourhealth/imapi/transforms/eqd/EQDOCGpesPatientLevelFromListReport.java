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
 * <p>Java class for EQDOC.GpesPatientLevelFromListReport complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.GpesPatientLevelFromListReport"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="table" type="{http://www.e-mis.com/emisopen}EQDOC.GpesPatientLevelTableType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="reportGuid" use="required" type="{http://www.e-mis.com/emisopen}dt.uid" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.GpesPatientLevelFromListReport", propOrder = {
  "table"
})
public class EQDOCGpesPatientLevelFromListReport {

  @XmlElement(required = true)
  protected List<EQDOCGpesPatientLevelTableType> table;
  @XmlAttribute(name = "reportGuid", required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  protected String reportGuid;

  /**
   * Gets the value of the table property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the table property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getTable().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCGpesPatientLevelTableType }
   */
  public List<EQDOCGpesPatientLevelTableType> getTable() {
    if (table == null) {
      table = new ArrayList<EQDOCGpesPatientLevelTableType>();
    }
    return this.table;
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

}
