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
 * <p>Java class for EQDOC.ReportSetGroup complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.ReportSetGroup"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="guid" type="{http://www.e-mis.com/emisopen}dt.uid"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="reportSet" type="{http://www.e-mis.com/emisopen}EQDOC.ReportSet" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="mapping" type="{http://www.e-mis.com/emisopen}EQDOC.Mapping" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.ReportSetGroup", propOrder = {
  "guid",
  "name",
  "reportSet",
  "mapping"
})
public class EQDOCReportSetGroup {

  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String guid;
  @XmlElement(required = true)
  protected String name;
  protected List<EQDOCReportSet> reportSet;
  protected EQDOCMapping mapping;

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
   * Gets the value of the reportSet property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the reportSet property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getReportSet().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCReportSet }
   */
  public List<EQDOCReportSet> getReportSet() {
    if (reportSet == null) {
      reportSet = new ArrayList<EQDOCReportSet>();
    }
    return this.reportSet;
  }

  /**
   * Gets the value of the mapping property.
   *
   * @return possible object is
   * {@link EQDOCMapping }
   */
  public EQDOCMapping getMapping() {
    return mapping;
  }

  /**
   * Sets the value of the mapping property.
   *
   * @param value allowed object is
   *              {@link EQDOCMapping }
   */
  public void setMapping(EQDOCMapping value) {
    this.mapping = value;
  }

}
