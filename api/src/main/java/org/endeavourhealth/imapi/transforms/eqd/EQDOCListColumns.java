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
 * <p>Java class for EQDOC.ListColumns complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.ListColumns"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="listColumn" type="{http://www.e-mis.com/emisopen}EQDOC.ListColumn" maxOccurs="unbounded"/&gt;
 *         &lt;element name="sort" type="{http://www.e-mis.com/emisopen}EQDOC.ListSortColumn" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.ListColumns", propOrder = {
  "listColumn",
  "sort"
})
public class EQDOCListColumns {

  @XmlElement(required = true)
  protected List<EQDOCListColumn> listColumn;
  protected List<EQDOCListSortColumn> sort;

  /**
   * Gets the value of the listColumn property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the listColumn property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getListColumn().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCListColumn }
   */
  public List<EQDOCListColumn> getListColumn() {
    if (listColumn == null) {
      listColumn = new ArrayList<EQDOCListColumn>();
    }
    return this.listColumn;
  }

  /**
   * Gets the value of the sort property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the sort property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getSort().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link EQDOCListSortColumn }
   */
  public List<EQDOCListSortColumn> getSort() {
    if (sort == null) {
      sort = new ArrayList<EQDOCListSortColumn>();
    }
    return this.sort;
  }

}
