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


/**
 * Produces a set of data items on which to base the owning criterion. A least one criterion within the group must ultimately share the same logical table name as the owning criterion.
 *
 * <p>Java class for EQDOC.BaseCriteriaGroup complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.BaseCriteriaGroup"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="definition" type="{http://www.e-mis.com/emisopen}EQDOC.CriteriaGroupDefinition"/&gt;
 *         &lt;element name="libraryItem" type="{http://www.e-mis.com/emisopen}dt.uid"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.BaseCriteriaGroup", propOrder = {
  "definition",
  "libraryItem"
})
@XmlSeeAlso({
  EQDOCCriteriaGroup.class
})
public class EQDOCBaseCriteriaGroup {

  protected EQDOCCriteriaGroupDefinition definition;
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String libraryItem;

  /**
   * Gets the value of the definition property.
   *
   * @return possible object is
   * {@link EQDOCCriteriaGroupDefinition }
   */
  public EQDOCCriteriaGroupDefinition getDefinition() {
    return definition;
  }

  /**
   * Sets the value of the definition property.
   *
   * @param value allowed object is
   *              {@link EQDOCCriteriaGroupDefinition }
   */
  public void setDefinition(EQDOCCriteriaGroupDefinition value) {
    this.definition = value;
  }

  /**
   * Gets the value of the libraryItem property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getLibraryItem() {
    return libraryItem;
  }

  /**
   * Sets the value of the libraryItem property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setLibraryItem(String value) {
    this.libraryItem = value;
  }

}
