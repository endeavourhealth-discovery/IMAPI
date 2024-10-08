//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.*;


/**
 * <p>Java class for EQDOC.SchedulePattern complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EQDOC.SchedulePattern"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pattern" type="{http://www.e-mis.com/emisopen}voc.SchedulePattern"/&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;element name="daily" type="{http://www.e-mis.com/emisopen}EQDOC.SchedulePatternDaily"/&gt;
 *           &lt;element name="weekly" type="{http://www.e-mis.com/emisopen}EQDOC.SchedulePatternWeekly"/&gt;
 *           &lt;element name="monthly" type="{http://www.e-mis.com/emisopen}EQDOC.SchedulePatternMonthly"/&gt;
 *           &lt;element name="yearly" type="{http://www.e-mis.com/emisopen}EQDOC.SchedulePatternYearly"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.SchedulePattern", propOrder = {
  "pattern",
  "daily",
  "weekly",
  "monthly",
  "yearly"
})
public class EQDOCSchedulePattern {

  @XmlElement(required = true)
  @XmlSchemaType(name = "token")
  protected VocSchedulePattern pattern;
  protected EQDOCSchedulePatternDaily daily;
  protected EQDOCSchedulePatternWeekly weekly;
  protected EQDOCSchedulePatternMonthly monthly;
  protected EQDOCSchedulePatternYearly yearly;

  /**
   * Gets the value of the pattern property.
   *
   * @return possible object is
   * {@link VocSchedulePattern }
   */
  public VocSchedulePattern getPattern() {
    return pattern;
  }

  /**
   * Sets the value of the pattern property.
   *
   * @param value allowed object is
   *              {@link VocSchedulePattern }
   */
  public void setPattern(VocSchedulePattern value) {
    this.pattern = value;
  }

  /**
   * Gets the value of the daily property.
   *
   * @return possible object is
   * {@link EQDOCSchedulePatternDaily }
   */
  public EQDOCSchedulePatternDaily getDaily() {
    return daily;
  }

  /**
   * Sets the value of the daily property.
   *
   * @param value allowed object is
   *              {@link EQDOCSchedulePatternDaily }
   */
  public void setDaily(EQDOCSchedulePatternDaily value) {
    this.daily = value;
  }

  /**
   * Gets the value of the weekly property.
   *
   * @return possible object is
   * {@link EQDOCSchedulePatternWeekly }
   */
  public EQDOCSchedulePatternWeekly getWeekly() {
    return weekly;
  }

  /**
   * Sets the value of the weekly property.
   *
   * @param value allowed object is
   *              {@link EQDOCSchedulePatternWeekly }
   */
  public void setWeekly(EQDOCSchedulePatternWeekly value) {
    this.weekly = value;
  }

  /**
   * Gets the value of the monthly property.
   *
   * @return possible object is
   * {@link EQDOCSchedulePatternMonthly }
   */
  public EQDOCSchedulePatternMonthly getMonthly() {
    return monthly;
  }

  /**
   * Sets the value of the monthly property.
   *
   * @param value allowed object is
   *              {@link EQDOCSchedulePatternMonthly }
   */
  public void setMonthly(EQDOCSchedulePatternMonthly value) {
    this.monthly = value;
  }

  /**
   * Gets the value of the yearly property.
   *
   * @return possible object is
   * {@link EQDOCSchedulePatternYearly }
   */
  public EQDOCSchedulePatternYearly getYearly() {
    return yearly;
  }

  /**
   * Sets the value of the yearly property.
   *
   * @param value allowed object is
   *              {@link EQDOCSchedulePatternYearly }
   */
  public void setYearly(EQDOCSchedulePatternYearly value) {
    this.yearly = value;
  }

}
