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
 * <p>Java class for EQDOC.SchedulePatternYearly complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EQDOC.SchedulePatternYearly"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="dayOfMonth"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="monthOfYear" type="{http://www.e-mis.com/emisopen}voc.MonthOfYear"/&gt;
 *                   &lt;element name="dayOfMonth"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *                         &lt;minInclusive value="1"/&gt;
 *                         &lt;maxInclusive value="31"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="dayOfWeek"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="occurrence" type="{http://www.e-mis.com/emisopen}voc.SchedulePatternOccurrence"/&gt;
 *                   &lt;element name="dayOfWeek" type="{http://www.e-mis.com/emisopen}voc.DayOfWeek"/&gt;
 *                   &lt;element name="monthOfYear" type="{http://www.e-mis.com/emisopen}voc.MonthOfYear"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.SchedulePatternYearly", propOrder = {
    "dayOfMonth",
    "dayOfWeek"
})
public class EQDOCSchedulePatternYearly {

    protected DayOfMonth dayOfMonth;
    protected DayOfWeek dayOfWeek;

    /**
     * Gets the value of the dayOfMonth property.
     *
     * @return
     *     possible object is
     *     {@link DayOfMonth }
     *
     */
    public DayOfMonth getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Sets the value of the dayOfMonth property.
     *
     * @param value
     *     allowed object is
     *     {@link DayOfMonth }
     *
     */
    public void setDayOfMonth(DayOfMonth value) {
        this.dayOfMonth = value;
    }

    /**
     * Gets the value of the dayOfWeek property.
     *
     * @return
     *     possible object is
     *     {@link DayOfWeek }
     *
     */
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Sets the value of the dayOfWeek property.
     *
     * @param value
     *     allowed object is
     *     {@link DayOfWeek }
     *
     */
    public void setDayOfWeek(DayOfWeek value) {
        this.dayOfWeek = value;
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
     *       &lt;sequence&gt;
     *         &lt;element name="monthOfYear" type="{http://www.e-mis.com/emisopen}voc.MonthOfYear"/&gt;
     *         &lt;element name="dayOfMonth"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
     *               &lt;minInclusive value="1"/&gt;
     *               &lt;maxInclusive value="31"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "monthOfYear",
        "dayOfMonth"
    })
    public static class DayOfMonth {

        @XmlElement(required = true)
        @XmlSchemaType(name = "token")
        protected VocMonthOfYear monthOfYear;
        protected int dayOfMonth;

        /**
         * Gets the value of the monthOfYear property.
         * 
         * @return
         *     possible object is
         *     {@link VocMonthOfYear }
         *     
         */
        public VocMonthOfYear getMonthOfYear() {
            return monthOfYear;
        }

        /**
         * Sets the value of the monthOfYear property.
         * 
         * @param value
         *     allowed object is
         *     {@link VocMonthOfYear }
         *     
         */
        public void setMonthOfYear(VocMonthOfYear value) {
            this.monthOfYear = value;
        }

        /**
         * Gets the value of the dayOfMonth property.
         * 
         */
        public int getDayOfMonth() {
            return dayOfMonth;
        }

        /**
         * Sets the value of the dayOfMonth property.
         * 
         */
        public void setDayOfMonth(int value) {
            this.dayOfMonth = value;
        }

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
     *       &lt;sequence&gt;
     *         &lt;element name="occurrence" type="{http://www.e-mis.com/emisopen}voc.SchedulePatternOccurrence"/&gt;
     *         &lt;element name="dayOfWeek" type="{http://www.e-mis.com/emisopen}voc.DayOfWeek"/&gt;
     *         &lt;element name="monthOfYear" type="{http://www.e-mis.com/emisopen}voc.MonthOfYear"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "occurrence",
        "dayOfWeek",
        "monthOfYear"
    })
    public static class DayOfWeek {

        @XmlElement(required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "token")
        protected String occurrence;
        @XmlElement(required = true)
        @XmlSchemaType(name = "token")
        protected VocDayOfWeek dayOfWeek;
        @XmlElement(required = true)
        @XmlSchemaType(name = "token")
        protected VocMonthOfYear monthOfYear;

        /**
         * Gets the value of the occurrence property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOccurrence() {
            return occurrence;
        }

        /**
         * Sets the value of the occurrence property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOccurrence(String value) {
            this.occurrence = value;
        }

        /**
         * Gets the value of the dayOfWeek property.
         * 
         * @return
         *     possible object is
         *     {@link VocDayOfWeek }
         *     
         */
        public VocDayOfWeek getDayOfWeek() {
            return dayOfWeek;
        }

        /**
         * Sets the value of the dayOfWeek property.
         * 
         * @param value
         *     allowed object is
         *     {@link VocDayOfWeek }
         *     
         */
        public void setDayOfWeek(VocDayOfWeek value) {
            this.dayOfWeek = value;
        }

        /**
         * Gets the value of the monthOfYear property.
         * 
         * @return
         *     possible object is
         *     {@link VocMonthOfYear }
         *     
         */
        public VocMonthOfYear getMonthOfYear() {
            return monthOfYear;
        }

        /**
         * Sets the value of the monthOfYear property.
         * 
         * @param value
         *     allowed object is
         *     {@link VocMonthOfYear }
         *     
         */
        public void setMonthOfYear(VocMonthOfYear value) {
            this.monthOfYear = value;
        }

    }

}
