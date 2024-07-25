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


/**
 * Database Object
 *
 * <p>Java class for dt.dbo complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="dt.dbo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="updateMode" type="{http://www.e-mis.com/emisopen}voc.UpdateMode" default="none" /&gt;
 *       &lt;attribute name="auditDeleteDate" type="{http://www.e-mis.com/emisopen}dt.DateTime" /&gt;
 *       &lt;attribute name="auditDeleteUserInRole" type="{http://www.e-mis.com/emisopen}dt.uid" /&gt;
 *       &lt;attribute name="auditDeleteInfo" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dt.dbo")
@XmlSeeAlso({
  DtContact.class,
  DtPatientIdentifier.class,
  DtUserIdentifier.class,
  EQDOCFolder.class,
  EQDOCReport.class,
  EQDOCLibraryItem.class,
  EQDOCMailMerge.class
})
public class DtDbo {

  @XmlAttribute(name = "updateMode")
  protected VocUpdateMode updateMode;
  @XmlAttribute(name = "auditDeleteDate")
  protected XMLGregorianCalendar auditDeleteDate;
  @XmlAttribute(name = "auditDeleteUserInRole")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  protected String auditDeleteUserInRole;
  @XmlAttribute(name = "auditDeleteInfo")
  protected String auditDeleteInfo;

  /**
   * Gets the value of the updateMode property.
   *
   * @return possible object is
   * {@link VocUpdateMode }
   */
  public VocUpdateMode getUpdateMode() {
    if (updateMode == null) {
      return VocUpdateMode.NONE;
    } else {
      return updateMode;
    }
  }

  /**
   * Sets the value of the updateMode property.
   *
   * @param value allowed object is
   *              {@link VocUpdateMode }
   */
  public void setUpdateMode(VocUpdateMode value) {
    this.updateMode = value;
  }

  /**
   * Gets the value of the auditDeleteDate property.
   *
   * @return possible object is
   * {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getAuditDeleteDate() {
    return auditDeleteDate;
  }

  /**
   * Sets the value of the auditDeleteDate property.
   *
   * @param value allowed object is
   *              {@link XMLGregorianCalendar }
   */
  public void setAuditDeleteDate(XMLGregorianCalendar value) {
    this.auditDeleteDate = value;
  }

  /**
   * Gets the value of the auditDeleteUserInRole property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getAuditDeleteUserInRole() {
    return auditDeleteUserInRole;
  }

  /**
   * Sets the value of the auditDeleteUserInRole property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setAuditDeleteUserInRole(String value) {
    this.auditDeleteUserInRole = value;
  }

  /**
   * Gets the value of the auditDeleteInfo property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getAuditDeleteInfo() {
    return auditDeleteInfo;
  }

  /**
   * Sets the value of the auditDeleteInfo property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setAuditDeleteInfo(String value) {
    this.auditDeleteInfo = value;
  }

}
