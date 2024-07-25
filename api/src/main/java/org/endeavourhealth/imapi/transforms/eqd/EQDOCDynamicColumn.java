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
 * <p>Java class for EQDOC.DynamicColumn complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.DynamicColumn"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dataType" type="{http://www.e-mis.com/emisopen}EQDOC.DataType"/&gt;
 *         &lt;element name="dynamicStructure"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *               &lt;enumeration value="function"/&gt;
 *               &lt;enumeration value="evaluate"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="parameter" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="dataType" type="{http://www.e-mis.com/emisopen}EQDOC.DataType"/&gt;
 *                   &lt;choice&gt;
 *                     &lt;element name="constant" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                     &lt;element name="column" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="2"/&gt;
 *                     &lt;element name="targetDate" type="{http://www.e-mis.com/emisopen}voc.SearchDateType"/&gt;
 *                     &lt;element name="isNull" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                   &lt;/choice&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;choice&gt;
 *           &lt;element name="evaluate"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                   &lt;sequence&gt;
 *                     &lt;choice&gt;
 *                       &lt;element name="column" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                       &lt;element name="function" type="{http://www.e-mis.com/emisopen}EQDOC.DynamicFunction"/&gt;
 *                     &lt;/choice&gt;
 *                     &lt;element name="comparator"&gt;
 *                       &lt;simpleType&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *                           &lt;enumeration value="greater"/&gt;
 *                           &lt;enumeration value="equal"/&gt;
 *                           &lt;enumeration value="less"/&gt;
 *                           &lt;enumeration value="lessOrEqual"/&gt;
 *                           &lt;enumeration value="greaterOrEqual"/&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/simpleType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="testvalue" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                     &lt;element name="assignTrue" type="{http://www.e-mis.com/emisopen}EQDOC.DynamicFunction"/&gt;
 *                     &lt;element name="assignFalse" type="{http://www.e-mis.com/emisopen}EQDOC.DynamicFunction"/&gt;
 *                   &lt;/sequence&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *           &lt;element name="function" type="{http://www.e-mis.com/emisopen}EQDOC.DynamicFunction"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.DynamicColumn", propOrder = {
  "displayName",
  "dataType",
  "dynamicStructure",
  "parameter",
  "evaluate",
  "function"
})
public class EQDOCDynamicColumn {

  @XmlElement(required = true)
  protected String displayName;
  @XmlElement(required = true)
  @XmlSchemaType(name = "token")
  protected EQDOCDataType dataType;
  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  protected String dynamicStructure;
  protected List<Parameter> parameter;
  protected Evaluate evaluate;
  protected EQDOCDynamicFunction function;

  /**
   * Gets the value of the displayName property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets the value of the displayName property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setDisplayName(String value) {
    this.displayName = value;
  }

  /**
   * Gets the value of the dataType property.
   *
   * @return possible object is
   * {@link EQDOCDataType }
   */
  public EQDOCDataType getDataType() {
    return dataType;
  }

  /**
   * Sets the value of the dataType property.
   *
   * @param value allowed object is
   *              {@link EQDOCDataType }
   */
  public void setDataType(EQDOCDataType value) {
    this.dataType = value;
  }

  /**
   * Gets the value of the dynamicStructure property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getDynamicStructure() {
    return dynamicStructure;
  }

  /**
   * Sets the value of the dynamicStructure property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setDynamicStructure(String value) {
    this.dynamicStructure = value;
  }

  /**
   * Gets the value of the parameter property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the parameter property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getParameter().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link Parameter }
   */
  public List<Parameter> getParameter() {
    if (parameter == null) {
      parameter = new ArrayList<Parameter>();
    }
    return this.parameter;
  }

  /**
   * Gets the value of the evaluate property.
   *
   * @return possible object is
   * {@link Evaluate }
   */
  public Evaluate getEvaluate() {
    return evaluate;
  }

  /**
   * Sets the value of the evaluate property.
   *
   * @param value allowed object is
   *              {@link Evaluate }
   */
  public void setEvaluate(Evaluate value) {
    this.evaluate = value;
  }

  /**
   * Gets the value of the function property.
   *
   * @return possible object is
   * {@link EQDOCDynamicFunction }
   */
  public EQDOCDynamicFunction getFunction() {
    return function;
  }

  /**
   * Sets the value of the function property.
   *
   * @param value allowed object is
   *              {@link EQDOCDynamicFunction }
   */
  public void setFunction(EQDOCDynamicFunction value) {
    this.function = value;
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
   *         &lt;choice&gt;
   *           &lt;element name="column" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
   *           &lt;element name="function" type="{http://www.e-mis.com/emisopen}EQDOC.DynamicFunction"/&gt;
   *         &lt;/choice&gt;
   *         &lt;element name="comparator"&gt;
   *           &lt;simpleType&gt;
   *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
   *               &lt;enumeration value="greater"/&gt;
   *               &lt;enumeration value="equal"/&gt;
   *               &lt;enumeration value="less"/&gt;
   *               &lt;enumeration value="lessOrEqual"/&gt;
   *               &lt;enumeration value="greaterOrEqual"/&gt;
   *             &lt;/restriction&gt;
   *           &lt;/simpleType&gt;
   *         &lt;/element&gt;
   *         &lt;element name="testvalue" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *         &lt;element name="assignTrue" type="{http://www.e-mis.com/emisopen}EQDOC.DynamicFunction"/&gt;
   *         &lt;element name="assignFalse" type="{http://www.e-mis.com/emisopen}EQDOC.DynamicFunction"/&gt;
   *       &lt;/sequence&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {
    "column",
    "function",
    "comparator",
    "testvalue",
    "assignTrue",
    "assignFalse"
  })
  public static class Evaluate {

    protected List<String> column;
    protected EQDOCDynamicFunction function;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String comparator;
    @XmlElement(required = true)
    protected String testvalue;
    @XmlElement(required = true)
    protected EQDOCDynamicFunction assignTrue;
    @XmlElement(required = true)
    protected EQDOCDynamicFunction assignFalse;

    /**
     * Gets the value of the column property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the column property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getColumn().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getColumn() {
      if (column == null) {
        column = new ArrayList<String>();
      }
      return this.column;
    }

    /**
     * Gets the value of the function property.
     *
     * @return possible object is
     * {@link EQDOCDynamicFunction }
     */
    public EQDOCDynamicFunction getFunction() {
      return function;
    }

    /**
     * Sets the value of the function property.
     *
     * @param value allowed object is
     *              {@link EQDOCDynamicFunction }
     */
    public void setFunction(EQDOCDynamicFunction value) {
      this.function = value;
    }

    /**
     * Gets the value of the comparator property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getComparator() {
      return comparator;
    }

    /**
     * Sets the value of the comparator property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setComparator(String value) {
      this.comparator = value;
    }

    /**
     * Gets the value of the testvalue property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTestvalue() {
      return testvalue;
    }

    /**
     * Sets the value of the testvalue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTestvalue(String value) {
      this.testvalue = value;
    }

    /**
     * Gets the value of the assignTrue property.
     *
     * @return possible object is
     * {@link EQDOCDynamicFunction }
     */
    public EQDOCDynamicFunction getAssignTrue() {
      return assignTrue;
    }

    /**
     * Sets the value of the assignTrue property.
     *
     * @param value allowed object is
     *              {@link EQDOCDynamicFunction }
     */
    public void setAssignTrue(EQDOCDynamicFunction value) {
      this.assignTrue = value;
    }

    /**
     * Gets the value of the assignFalse property.
     *
     * @return possible object is
     * {@link EQDOCDynamicFunction }
     */
    public EQDOCDynamicFunction getAssignFalse() {
      return assignFalse;
    }

    /**
     * Sets the value of the assignFalse property.
     *
     * @param value allowed object is
     *              {@link EQDOCDynamicFunction }
     */
    public void setAssignFalse(EQDOCDynamicFunction value) {
      this.assignFalse = value;
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
   *         &lt;element name="dataType" type="{http://www.e-mis.com/emisopen}EQDOC.DataType"/&gt;
   *         &lt;choice&gt;
   *           &lt;element name="constant" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *           &lt;element name="column" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="2"/&gt;
   *           &lt;element name="targetDate" type="{http://www.e-mis.com/emisopen}voc.SearchDateType"/&gt;
   *           &lt;element name="isNull" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
   *         &lt;/choice&gt;
   *       &lt;/sequence&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {
    "dataType",
    "constant",
    "column",
    "targetDate",
    "isNull"
  })
  public static class Parameter {

    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected EQDOCDataType dataType;
    protected String constant;
    protected List<String> column;
    @XmlSchemaType(name = "token")
    protected VocSearchDateType targetDate;
    protected Boolean isNull;

    /**
     * Gets the value of the dataType property.
     *
     * @return possible object is
     * {@link EQDOCDataType }
     */
    public EQDOCDataType getDataType() {
      return dataType;
    }

    /**
     * Sets the value of the dataType property.
     *
     * @param value allowed object is
     *              {@link EQDOCDataType }
     */
    public void setDataType(EQDOCDataType value) {
      this.dataType = value;
    }

    /**
     * Gets the value of the constant property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getConstant() {
      return constant;
    }

    /**
     * Sets the value of the constant property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setConstant(String value) {
      this.constant = value;
    }

    /**
     * Gets the value of the column property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the column property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getColumn().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getColumn() {
      if (column == null) {
        column = new ArrayList<String>();
      }
      return this.column;
    }

    /**
     * Gets the value of the targetDate property.
     *
     * @return possible object is
     * {@link VocSearchDateType }
     */
    public VocSearchDateType getTargetDate() {
      return targetDate;
    }

    /**
     * Sets the value of the targetDate property.
     *
     * @param value allowed object is
     *              {@link VocSearchDateType }
     */
    public void setTargetDate(VocSearchDateType value) {
      this.targetDate = value;
    }

    /**
     * Gets the value of the isNull property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isIsNull() {
      return isNull;
    }

    /**
     * Sets the value of the isNull property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setIsNull(Boolean value) {
      this.isNull = value;
    }

  }

}
