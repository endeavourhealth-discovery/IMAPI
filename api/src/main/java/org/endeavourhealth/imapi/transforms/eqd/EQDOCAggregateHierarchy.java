//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.*;


/**
 * <p>Java class for EQDOC.AggregateHierarchy complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.AggregateHierarchy"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="hierarchyType" type="{http://www.e-mis.com/emisopen}voc.BandingHierarchyType"/&gt;
 *         &lt;element name="level" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.AggregateHierarchy", propOrder = {
  "hierarchyType",
  "level"
})
public class EQDOCAggregateHierarchy {

  @XmlElement(required = true)
  @XmlSchemaType(name = "string")
  protected VocBandingHierarchyType hierarchyType;
  protected int level;

  /**
   * Gets the value of the hierarchyType property.
   *
   * @return possible object is
   * {@link VocBandingHierarchyType }
   */
  public VocBandingHierarchyType getHierarchyType() {
    return hierarchyType;
  }

  /**
   * Sets the value of the hierarchyType property.
   *
   * @param value allowed object is
   *              {@link VocBandingHierarchyType }
   */
  public void setHierarchyType(VocBandingHierarchyType value) {
    this.hierarchyType = value;
  }

  /**
   * Gets the value of the level property.
   */
  public int getLevel() {
    return level;
  }

  /**
   * Sets the value of the level property.
   */
  public void setLevel(int value) {
    this.level = value;
  }

}
