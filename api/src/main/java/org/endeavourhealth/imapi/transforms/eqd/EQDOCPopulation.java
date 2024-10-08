//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for EQDOC.Match complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.Match"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="criteriaGroup" type="{http://www.e-mis.com/emisopen}EQDOC.CriteriaGroup" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="dynamicColumn" type="{http://www.e-mis.com/emisopen}EQDOC.DynamicColumn" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="contractInformation" type="{http://www.e-mis.com/emisopen}EQDOC.ContractInformation" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.Match", propOrder = {
  "criteriaGroup",
  "dynamicColumn",
  "contractInformation"
})
public class EQDOCPopulation {

  protected List<EQDOCCriteriaGroup> criteriaGroup;
  protected List<EQDOCDynamicColumn> dynamicColumn;
  protected EQDOCContractInformation contractInformation;

  /**
   * Gets the value of the criteriaGroup property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the criteriaGroup property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getCriteriaGroup().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCCriteriaGroup }
   */
  public List<EQDOCCriteriaGroup> getCriteriaGroup() {
    if (criteriaGroup == null) {
      criteriaGroup = new ArrayList<EQDOCCriteriaGroup>();
    }
    return this.criteriaGroup;
  }

  /**
   * Gets the value of the dynamicColumn property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the dynamicColumn property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getDynamicColumn().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCDynamicColumn }
   */
  public List<EQDOCDynamicColumn> getDynamicColumn() {
    if (dynamicColumn == null) {
      dynamicColumn = new ArrayList<EQDOCDynamicColumn>();
    }
    return this.dynamicColumn;
  }

  /**
   * Gets the value of the contractInformation property.
   *
   * @return possible object is
   * {@link EQDOCContractInformation }
   */
  public EQDOCContractInformation getContractInformation() {
    return contractInformation;
  }

  /**
   * Sets the value of the contractInformation property.
   *
   * @param value allowed object is
   *              {@link EQDOCContractInformation }
   */
  public void setContractInformation(EQDOCContractInformation value) {
    this.contractInformation = value;
  }

}
