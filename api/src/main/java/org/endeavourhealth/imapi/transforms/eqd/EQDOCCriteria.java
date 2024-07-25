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


/**
 * <p>Java class for EQDOC.Criteria complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.Criteria"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="criterion" type="{http://www.e-mis.com/emisopen}EQDOC.Criterion"/&gt;
 *         &lt;element name="procedure" type="{http://www.e-mis.com/emisopen}EQDOC.MethodCall"/&gt;
 *         &lt;element name="libraryItem" type="{http://www.e-mis.com/emisopen}EQDOC.LibraryItemCriterion"/&gt;
 *         &lt;element name="populationCriterion" type="{http://www.e-mis.com/emisopen}EQDOC.SearchIdentifier"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.Criteria", propOrder = {
  "criterion",
  "procedure",
  "libraryItem",
  "populationCriterion"
})
public class EQDOCCriteria {

  protected EQDOCCriterion criterion;
  protected EQDOCMethodCall procedure;
  protected EQDOCLibraryItemCriterion libraryItem;
  protected EQDOCSearchIdentifier populationCriterion;

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
   * Gets the value of the procedure property.
   *
   * @return possible object is
   * {@link EQDOCMethodCall }
   */
  public EQDOCMethodCall getProcedure() {
    return procedure;
  }

  /**
   * Sets the value of the procedure property.
   *
   * @param value allowed object is
   *              {@link EQDOCMethodCall }
   */
  public void setProcedure(EQDOCMethodCall value) {
    this.procedure = value;
  }

  /**
   * Gets the value of the libraryItem property.
   *
   * @return possible object is
   * {@link EQDOCLibraryItemCriterion }
   */
  public EQDOCLibraryItemCriterion getLibraryItem() {
    return libraryItem;
  }

  /**
   * Sets the value of the libraryItem property.
   *
   * @param value allowed object is
   *              {@link EQDOCLibraryItemCriterion }
   */
  public void setLibraryItem(EQDOCLibraryItemCriterion value) {
    this.libraryItem = value;
  }

  /**
   * Gets the value of the populationCriterion property.
   *
   * @return possible object is
   * {@link EQDOCSearchIdentifier }
   */
  public EQDOCSearchIdentifier getPopulationCriterion() {
    return populationCriterion;
  }

  /**
   * Sets the value of the populationCriterion property.
   *
   * @param value allowed object is
   *              {@link EQDOCSearchIdentifier }
   */
  public void setPopulationCriterion(EQDOCSearchIdentifier value) {
    this.populationCriterion = value;
  }

}
