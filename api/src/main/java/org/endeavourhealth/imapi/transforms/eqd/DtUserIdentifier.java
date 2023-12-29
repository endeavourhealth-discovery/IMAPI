//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.*;


/**
 * <p>Java class for dt.UserIdentifier complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dt.UserIdentifier"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.e-mis.com/emisopen}dt.dbo"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identifierType" type="{http://www.e-mis.com/emisopen}voc.UserIdentifierType"/&gt;
 *         &lt;element name="value"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="15"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dt.UserIdentifier", propOrder = {
    "identifierType",
    "value"
})
public class DtUserIdentifier
    extends DtDbo
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected VocUserIdentifierType identifierType;
    @XmlElement(required = true)
    protected String value;

    /**
     * Gets the value of the identifierType property.
     * 
     * @return
     *     possible object is
     *     {@link VocUserIdentifierType }
     *     
     */
    public VocUserIdentifierType getIdentifierType() {
        return identifierType;
    }

    /**
     * Sets the value of the identifierType property.
     * 
     * @param value
     *     allowed object is
     *     {@link VocUserIdentifierType }
     *     
     */
    public void setIdentifierType(VocUserIdentifierType value) {
        this.identifierType = value;
    }

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

}
