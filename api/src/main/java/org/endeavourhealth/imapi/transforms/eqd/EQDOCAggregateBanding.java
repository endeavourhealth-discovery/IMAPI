//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for EQDOC.AggregateBanding complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.AggregateBanding"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="groupingUnit" type="{http://www.e-mis.com/emisopen}voc.BandingUnit"/&gt;
 *         &lt;element name="subgroup" type="{http://www.e-mis.com/emisopen}EQDOC.AggregateSubgroup" maxOccurs="unbounded"/&gt;
 *         &lt;element name="hierarchy" type="{http://www.e-mis.com/emisopen}EQDOC.AggregateHierarchy"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.AggregateBanding", propOrder = {
  "groupingUnit",
  "subgroup",
  "hierarchy"
})
public class EQDOCAggregateBanding {

  @XmlSchemaType(name = "token")
  protected VocBandingUnit groupingUnit;
  protected List<EQDOCAggregateSubgroup> subgroup;
  protected EQDOCAggregateHierarchy hierarchy;

  /**
   * Gets the value of the groupingUnit property.
   *
   * @return possible object is
   * {@link VocBandingUnit }
   */
  public VocBandingUnit getGroupingUnit() {
    return groupingUnit;
  }

  /**
   * Sets the value of the groupingUnit property.
   *
   * @param value allowed object is
   *              {@link VocBandingUnit }
   */
  public void setGroupingUnit(VocBandingUnit value) {
    this.groupingUnit = value;
  }

  /**
   * Gets the value of the subgroup property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the subgroup property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getSubgroup().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCAggregateSubgroup }
   */
  public List<EQDOCAggregateSubgroup> getSubgroup() {
    if (subgroup == null) {
      subgroup = new ArrayList<EQDOCAggregateSubgroup>();
    }
    return this.subgroup;
  }

  /**
   * Gets the value of the hierarchy property.
   *
   * @return possible object is
   * {@link EQDOCAggregateHierarchy }
   */
  public EQDOCAggregateHierarchy getHierarchy() {
    return hierarchy;
  }

  /**
   * Sets the value of the hierarchy property.
   *
   * @param value allowed object is
   *              {@link EQDOCAggregateHierarchy }
   */
  public void setHierarchy(EQDOCAggregateHierarchy value) {
    this.hierarchy = value;
  }

}
