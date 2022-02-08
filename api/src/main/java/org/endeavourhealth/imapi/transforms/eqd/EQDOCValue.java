//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for EQDOC.Value complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EQDOC.Value"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="unit" type="{http://www.e-mis.com/emisopen}voc.ValueUnit" minOccurs="0"/&gt;
 *         &lt;element name="relation" type="{http://www.e-mis.com/emisopen}voc.Relation" minOccurs="0"/&gt;
 *         &lt;element name="precision" type="{http://www.e-mis.com/emisopen}voc.ValueUnit" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.Value", propOrder = {
    "value",
    "unit",
    "relation",
    "precision"
})
public class EQDOCValue {

    @XmlElement(required = true)
    protected String value;
    @XmlSchemaType(name = "token")
    protected VocValueUnit unit;
    @XmlSchemaType(name = "string")
    protected VocRelation relation;
    @XmlSchemaType(name = "token")
    protected VocValueUnit precision;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link VocValueUnit }
     *     
     */
    public VocValueUnit getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link VocValueUnit }
     *     
     */
    public void setUnit(VocValueUnit value) {
        this.unit = value;
    }

    /**
     * Gets the value of the relation property.
     * 
     * @return
     *     possible object is
     *     {@link VocRelation }
     *     
     */
    public VocRelation getRelation() {
        return relation;
    }

    /**
     * Sets the value of the relation property.
     * 
     * @param value
     *     allowed object is
     *     {@link VocRelation }
     *     
     */
    public void setRelation(VocRelation value) {
        this.relation = value;
    }

    /**
     * Gets the value of the precision property.
     * 
     * @return
     *     possible object is
     *     {@link VocValueUnit }
     *     
     */
    public VocValueUnit getPrecision() {
        return precision;
    }

    /**
     * Sets the value of the precision property.
     * 
     * @param value
     *     allowed object is
     *     {@link VocValueUnit }
     *     
     */
    public void setPrecision(VocValueUnit value) {
        this.precision = value;
    }

}