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
 * <p>Java class for EQDOC.AuditReport complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EQDOC.AuditReport"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element name="standard" type="{http://www.e-mis.com/emisopen}voc.StandardAuditReportType"/&gt;
 *           &lt;element name="existingAggregate"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                   &lt;attribute name="id" use="required" type="{http://www.e-mis.com/emisopen}dt.uid" /&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *           &lt;element name="customAggregate" type="{http://www.e-mis.com/emisopen}EQDOC.AggregateReport"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="population" type="{http://www.e-mis.com/emisopen}dt.uid" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.AuditReport", propOrder = {
    "standard",
    "existingAggregate",
    "customAggregate",
    "population"
})
public class EQDOCAuditReport {

    @XmlSchemaType(name = "token")
    protected VocStandardAuditReportType standard;
    protected ExistingAggregate existingAggregate;
    protected EQDOCAggregateReport customAggregate;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected List<String> population;

    /**
     * Gets the value of the standard property.
     *
     * @return
     *     possible object is
     *     {@link VocStandardAuditReportType }
     *
     */
    public VocStandardAuditReportType getStandard() {
        return standard;
    }

    /**
     * Sets the value of the standard property.
     *
     * @param value
     *     allowed object is
     *     {@link VocStandardAuditReportType }
     *
     */
    public void setStandard(VocStandardAuditReportType value) {
        this.standard = value;
    }

    /**
     * Gets the value of the existingAggregate property.
     *
     * @return
     *     possible object is
     *     {@link ExistingAggregate }
     *
     */
    public ExistingAggregate getExistingAggregate() {
        return existingAggregate;
    }

    /**
     * Sets the value of the existingAggregate property.
     *
     * @param value
     *     allowed object is
     *     {@link ExistingAggregate }
     *
     */
    public void setExistingAggregate(ExistingAggregate value) {
        this.existingAggregate = value;
    }

    /**
     * Gets the value of the customAggregate property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCAggregateReport }
     *     
     */
    public EQDOCAggregateReport getCustomAggregate() {
        return customAggregate;
    }

    /**
     * Sets the value of the customAggregate property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCAggregateReport }
     *     
     */
    public void setCustomAggregate(EQDOCAggregateReport value) {
        this.customAggregate = value;
    }

    /**
     * Gets the value of the population property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the population property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPopulation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPopulation() {
        if (population == null) {
            population = new ArrayList<String>();
        }
        return this.population;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="id" use="required" type="{http://www.e-mis.com/emisopen}dt.uid" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class ExistingAggregate {

        @XmlAttribute(name = "id", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String id;

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

    }

}
