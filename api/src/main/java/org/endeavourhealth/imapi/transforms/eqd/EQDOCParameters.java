//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for EQDOC.Parameters complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.Parameters"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="InputParam" type="{http://www.e-mis.com/emisopen}EQDOC.InputParameter" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="PatientFilter" type="{http://www.e-mis.com/emisopen}EQDOC.PatientFilter" minOccurs="0"/&gt;
 *         &lt;element name="SubstitutionValue" type="{http://www.e-mis.com/emisopen}EQDOC.SubstitutionValue" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.Parameters", propOrder = {
  "inputParam",
  "patientFilter",
  "substitutionValue"
})
public class EQDOCParameters {

  @XmlElement(name = "InputParam")
  protected List<EQDOCInputParameter> inputParam;
  @XmlElement(name = "PatientFilter")
  protected EQDOCPatientFilter patientFilter;
  @XmlElement(name = "SubstitutionValue")
  protected List<EQDOCSubstitutionValue> substitutionValue;

  /**
   * Gets the value of the inputParam property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the inputParam property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getInputParam().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCInputParameter }
   */
  public List<EQDOCInputParameter> getInputParam() {
    if (inputParam == null) {
      inputParam = new ArrayList<EQDOCInputParameter>();
    }
    return this.inputParam;
  }

  /**
   * Gets the value of the patientFilter property.
   *
   * @return possible object is
   * {@link EQDOCPatientFilter }
   */
  public EQDOCPatientFilter getPatientFilter() {
    return patientFilter;
  }

  /**
   * Sets the value of the patientFilter property.
   *
   * @param value allowed object is
   *              {@link EQDOCPatientFilter }
   */
  public void setPatientFilter(EQDOCPatientFilter value) {
    this.patientFilter = value;
  }

  /**
   * Gets the value of the substitutionValue property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the substitutionValue property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getSubstitutionValue().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCSubstitutionValue }
   */
  public List<EQDOCSubstitutionValue> getSubstitutionValue() {
    if (substitutionValue == null) {
      substitutionValue = new ArrayList<EQDOCSubstitutionValue>();
    }
    return this.substitutionValue;
  }

}
