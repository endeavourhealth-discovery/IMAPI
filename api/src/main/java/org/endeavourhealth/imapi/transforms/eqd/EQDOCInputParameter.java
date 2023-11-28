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
 * <p>Java class for EQDOC.InputParameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EQDOC.InputParameter"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ReportGuid" type="{http://www.e-mis.com/emisopen}dt.uid" maxOccurs="unbounded"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="rangeValue" type="{http://www.e-mis.com/emisopen}EQDOC.RangeValue"/&gt;
 *           &lt;element name="singleValue" type="{http://www.e-mis.com/emisopen}EQDOC.SingleValue"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.InputParameter", propOrder = {
    "name",
    "reportGuid",
    "rangeValue",
    "singleValue"
})
public class EQDOCInputParameter {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(name = "ReportGuid", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected List<String> reportGuid;
    protected EQDOCRangeValue rangeValue;
    protected EQDOCSingleValue singleValue;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the reportGuid property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reportGuid property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReportGuid().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getReportGuid() {
        if (reportGuid == null) {
            reportGuid = new ArrayList<String>();
        }
        return this.reportGuid;
    }

    /**
     * Gets the value of the rangeValue property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCRangeValue }
     *     
     */
    public EQDOCRangeValue getRangeValue() {
        return rangeValue;
    }

    /**
     * Sets the value of the rangeValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCRangeValue }
     *     
     */
    public void setRangeValue(EQDOCRangeValue value) {
        this.rangeValue = value;
    }

    /**
     * Gets the value of the singleValue property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCSingleValue }
     *     
     */
    public EQDOCSingleValue getSingleValue() {
        return singleValue;
    }

    /**
     * Sets the value of the singleValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCSingleValue }
     *     
     */
    public void setSingleValue(EQDOCSingleValue value) {
        this.singleValue = value;
    }

}
