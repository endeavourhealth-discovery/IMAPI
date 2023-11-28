//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for EQDOC.PopulationDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EQDOC.PopulationDefinition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="searchDate" type="{http://www.e-mis.com/emisopen}voc.SearchDateType"/&gt;
 *         &lt;element name="parent" type="{http://www.e-mis.com/emisopen}EQDOC.PopulationParent"/&gt;
 *         &lt;element name="criteriaGroup" type="{http://www.e-mis.com/emisopen}EQDOC.CriteriaGroup" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.PopulationDefinition", propOrder = {
    "searchDate",
    "parent",
    "criteriaGroup"
})
public class EQDOCPopulationDefinition {

    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected VocSearchDateType searchDate;
    @XmlElement(required = true)
    protected EQDOCPopulationParent parent;
    protected List<EQDOCCriteriaGroup> criteriaGroup;

    /**
     * Gets the value of the searchDate property.
     * 
     * @return
     *     possible object is
     *     {@link VocSearchDateType }
     *     
     */
    public VocSearchDateType getSearchDate() {
        return searchDate;
    }

    /**
     * Sets the value of the searchDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link VocSearchDateType }
     *     
     */
    public void setSearchDate(VocSearchDateType value) {
        this.searchDate = value;
    }

    /**
     * Gets the value of the parent property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCPopulationParent }
     *     
     */
    public EQDOCPopulationParent getParent() {
        return parent;
    }

    /**
     * Sets the value of the parent property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCPopulationParent }
     *     
     */
    public void setParent(EQDOCPopulationParent value) {
        this.parent = value;
    }

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
     * 
     * 
     */
    public List<EQDOCCriteriaGroup> getCriteriaGroup() {
        if (criteriaGroup == null) {
            criteriaGroup = new ArrayList<EQDOCCriteriaGroup>();
        }
        return this.criteriaGroup;
    }

}
