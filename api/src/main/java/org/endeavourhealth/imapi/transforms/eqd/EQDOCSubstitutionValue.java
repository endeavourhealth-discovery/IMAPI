//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for EQDOC.SubstitutionValue complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EQDOC.SubstitutionValue"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="valueSet" type="{http://www.e-mis.com/emisopen}EQDOC.ValueSet" maxOccurs="unbounded"/&gt;
 *         &lt;element name="singleValue" type="{http://www.e-mis.com/emisopen}EQDOC.SingleValue"/&gt;
 *         &lt;element name="value" type="{http://www.e-mis.com/emisopen}EQDOC.Value"/&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name="guid" use="required" type="{http://www.e-mis.com/emisopen}dt.uid" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.SubstitutionValue", propOrder = {
    "valueSet",
    "singleValue",
    "value"
})
public class EQDOCSubstitutionValue {

    protected List<EQDOCValueSet> valueSet;
    protected EQDOCSingleValue singleValue;
    protected EQDOCValue value;
    @XmlAttribute(name = "guid", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String guid;

    /**
     * Gets the value of the valueSet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valueSet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValueSet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EQDOCValueSet }
     * 
     * 
     */
    public List<EQDOCValueSet> getValueSet() {
        if (valueSet == null) {
            valueSet = new ArrayList<EQDOCValueSet>();
        }
        return this.valueSet;
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

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link EQDOCValue }
     *     
     */
    public EQDOCValue getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link EQDOCValue }
     *     
     */
    public void setValue(EQDOCValue value) {
        this.value = value;
    }

    /**
     * Gets the value of the guid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuid(String value) {
        this.guid = value;
    }

}
