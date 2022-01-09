//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for EQDOC.TestAttribute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EQDOC.TestAttribute"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="columnValue" type="{http://www.e-mis.com/emisopen}EQDOC.ColumnValue" maxOccurs="unbounded"/&gt;
 *         &lt;element name="recordFrequency" type="{http://www.e-mis.com/emisopen}EQDOC.RecordFrequency" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.TestAttribute", propOrder = {
    "columnValue",
    "recordFrequency"
})
public class EQDOCTestAttribute {

    @XmlElement(required = true)
    protected List<EQDOCColumnValue> columnValue;
    protected EQDOCRecordFrequency recordFrequency;

    /**
     * Gets the value of the columnValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the columnValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getColumnValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EQDOCColumnValue }
     * 
     * 
     */
    public List<EQDOCColumnValue> getColumnValue() {
        if (columnValue == null) {
            columnValue = new ArrayList<EQDOCColumnValue>();
        }
        return this.columnValue;
    }

    /**
     * Gets the value of the recordFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCRecordFrequency }
     *     
     */
    public EQDOCRecordFrequency getRecordFrequency() {
        return recordFrequency;
    }

    /**
     * Sets the value of the recordFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCRecordFrequency }
     *     
     */
    public void setRecordFrequency(EQDOCRecordFrequency value) {
        this.recordFrequency = value;
    }

}
