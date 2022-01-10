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


/**
 * <p>Java class for EQDOC.FilterRestriction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EQDOC.FilterRestriction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="columnOrder" type="{http://www.e-mis.com/emisopen}EQDOC.ColumnOrder"/&gt;
 *         &lt;element name="testAttribute" type="{http://www.e-mis.com/emisopen}EQDOC.TestAttribute" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.FilterRestriction", propOrder = {
    "columnOrder",
    "testAttribute"
})
public class EQDOCFilterRestriction {

    @XmlElement(required = true)
    protected EQDOCColumnOrder columnOrder;
    protected EQDOCTestAttribute testAttribute;

    /**
     * Gets the value of the columnOrder property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCColumnOrder }
     *     
     */
    public EQDOCColumnOrder getColumnOrder() {
        return columnOrder;
    }

    /**
     * Sets the value of the columnOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCColumnOrder }
     *     
     */
    public void setColumnOrder(EQDOCColumnOrder value) {
        this.columnOrder = value;
    }

    /**
     * Gets the value of the testAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCTestAttribute }
     *     
     */
    public EQDOCTestAttribute getTestAttribute() {
        return testAttribute;
    }

    /**
     * Sets the value of the testAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCTestAttribute }
     *     
     */
    public void setTestAttribute(EQDOCTestAttribute value) {
        this.testAttribute = value;
    }

}
