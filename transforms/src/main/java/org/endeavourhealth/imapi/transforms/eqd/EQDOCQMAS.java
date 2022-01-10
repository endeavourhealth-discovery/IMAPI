//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for EQDOC.QMAS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EQDOC.QMAS"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ReferenceDate" type="{http://www.w3.org/2001/XMLSchema}date" maxOccurs="unbounded"/&gt;
 *         &lt;element name="InternalVersion" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="GPSSRulesetVersion" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="GPSSRevision" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="GPSSMessageSchema" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.QMAS", propOrder = {
    "referenceDate",
    "internalVersion",
    "gpssRulesetVersion",
    "gpssRevision",
    "gpssMessageSchema"
})
public class EQDOCQMAS {

    @XmlElement(name = "ReferenceDate", required = true)
    @XmlSchemaType(name = "date")
    protected List<XMLGregorianCalendar> referenceDate;
    @XmlElement(name = "InternalVersion", required = true)
    protected BigDecimal internalVersion;
    @XmlElement(name = "GPSSRulesetVersion", required = true)
    protected BigDecimal gpssRulesetVersion;
    @XmlElement(name = "GPSSRevision", required = true)
    protected BigDecimal gpssRevision;
    @XmlElement(name = "GPSSMessageSchema", required = true)
    protected BigDecimal gpssMessageSchema;

    /**
     * Gets the value of the referenceDate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the referenceDate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReferenceDate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLGregorianCalendar }
     * 
     * 
     */
    public List<XMLGregorianCalendar> getReferenceDate() {
        if (referenceDate == null) {
            referenceDate = new ArrayList<XMLGregorianCalendar>();
        }
        return this.referenceDate;
    }

    /**
     * Gets the value of the internalVersion property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInternalVersion() {
        return internalVersion;
    }

    /**
     * Sets the value of the internalVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInternalVersion(BigDecimal value) {
        this.internalVersion = value;
    }

    /**
     * Gets the value of the gpssRulesetVersion property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGPSSRulesetVersion() {
        return gpssRulesetVersion;
    }

    /**
     * Sets the value of the gpssRulesetVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGPSSRulesetVersion(BigDecimal value) {
        this.gpssRulesetVersion = value;
    }

    /**
     * Gets the value of the gpssRevision property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGPSSRevision() {
        return gpssRevision;
    }

    /**
     * Sets the value of the gpssRevision property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGPSSRevision(BigDecimal value) {
        this.gpssRevision = value;
    }

    /**
     * Gets the value of the gpssMessageSchema property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGPSSMessageSchema() {
        return gpssMessageSchema;
    }

    /**
     * Sets the value of the gpssMessageSchema property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGPSSMessageSchema(BigDecimal value) {
        this.gpssMessageSchema = value;
    }

}
