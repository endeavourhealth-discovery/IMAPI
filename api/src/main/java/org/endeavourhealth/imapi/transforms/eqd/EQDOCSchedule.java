//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for EQDOC.Schedule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EQDOC.Schedule"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="recurrencePattern" type="{http://www.e-mis.com/emisopen}EQDOC.SchedulePattern"/&gt;
 *         &lt;element name="rangeOfRecurrence" type="{http://www.e-mis.com/emisopen}EQDOC.ScheduleRange"/&gt;
 *         &lt;element name="baselineDate" type="{http://www.e-mis.com/emisopen}EQDOC.ScheduleDate" minOccurs="0"/&gt;
 *         &lt;element name="referenceDate" type="{http://www.e-mis.com/emisopen}EQDOC.ScheduleDate" minOccurs="0"/&gt;
 *         &lt;element name="author" type="{http://www.e-mis.com/emisopen}EQDOC.Author" minOccurs="0"/&gt;
 *         &lt;element name="instanceType" type="{http://www.e-mis.com/emisopen}voc.InstanceType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.Schedule", propOrder = {
    "recurrencePattern",
    "rangeOfRecurrence",
    "baselineDate",
    "referenceDate",
    "author",
    "instanceType"
})
public class EQDOCSchedule {

    @XmlElement(required = true)
    protected EQDOCSchedulePattern recurrencePattern;
    @XmlElement(required = true)
    protected EQDOCScheduleRange rangeOfRecurrence;
    protected EQDOCScheduleDate baselineDate;
    protected EQDOCScheduleDate referenceDate;
    protected EQDOCAuthor author;
    @XmlSchemaType(name = "string")
    protected VocInstanceType instanceType;

    /**
     * Gets the value of the recurrencePattern property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCSchedulePattern }
     *     
     */
    public EQDOCSchedulePattern getRecurrencePattern() {
        return recurrencePattern;
    }

    /**
     * Sets the value of the recurrencePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCSchedulePattern }
     *     
     */
    public void setRecurrencePattern(EQDOCSchedulePattern value) {
        this.recurrencePattern = value;
    }

    /**
     * Gets the value of the rangeOfRecurrence property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCScheduleRange }
     *     
     */
    public EQDOCScheduleRange getRangeOfRecurrence() {
        return rangeOfRecurrence;
    }

    /**
     * Sets the value of the rangeOfRecurrence property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCScheduleRange }
     *     
     */
    public void setRangeOfRecurrence(EQDOCScheduleRange value) {
        this.rangeOfRecurrence = value;
    }

    /**
     * Gets the value of the baselineDate property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCScheduleDate }
     *     
     */
    public EQDOCScheduleDate getBaselineDate() {
        return baselineDate;
    }

    /**
     * Sets the value of the baselineDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCScheduleDate }
     *     
     */
    public void setBaselineDate(EQDOCScheduleDate value) {
        this.baselineDate = value;
    }

    /**
     * Gets the value of the referenceDate property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCScheduleDate }
     *     
     */
    public EQDOCScheduleDate getReferenceDate() {
        return referenceDate;
    }

    /**
     * Sets the value of the referenceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCScheduleDate }
     *     
     */
    public void setReferenceDate(EQDOCScheduleDate value) {
        this.referenceDate = value;
    }

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCAuthor }
     *     
     */
    public EQDOCAuthor getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCAuthor }
     *     
     */
    public void setAuthor(EQDOCAuthor value) {
        this.author = value;
    }

    /**
     * Gets the value of the instanceType property.
     * 
     * @return
     *     possible object is
     *     {@link VocInstanceType }
     *     
     */
    public VocInstanceType getInstanceType() {
        return instanceType;
    }

    /**
     * Sets the value of the instanceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link VocInstanceType }
     *     
     */
    public void setInstanceType(VocInstanceType value) {
        this.instanceType = value;
    }

}
