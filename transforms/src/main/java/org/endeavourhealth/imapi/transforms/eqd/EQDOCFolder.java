//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for EQDOC.Folder complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EQDOC.Folder"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.e-mis.com/emisopen}dt.dbo"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.e-mis.com/emisopen}dt.uid"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="parentFolder" type="{http://www.e-mis.com/emisopen}dt.uid" minOccurs="0"/&gt;
 *         &lt;element name="sequence" type="{http://www.w3.org/2001/XMLSchema}short" minOccurs="0"/&gt;
 *         &lt;element name="enterpriseReportingLevel" type="{http://www.e-mis.com/emisopen}voc.EnterpriseReportingLevel" minOccurs="0"/&gt;
 *         &lt;element name="association" type="{http://www.e-mis.com/emisopen}EQDOC.FolderAssociation" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="author" type="{http://www.e-mis.com/emisopen}EQDOC.Author" minOccurs="0"/&gt;
 *         &lt;element name="schedule" type="{http://www.e-mis.com/emisopen}EQDOC.Schedule" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="PopulationTypeId" type="{http://www.e-mis.com/emisopen}voc.PopulationType" minOccurs="0"/&gt;
 *         &lt;element name="IsEnterpriseSearchOverride" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.Folder", propOrder = {
    "id",
    "name",
    "parentFolder",
    "sequence",
    "enterpriseReportingLevel",
    "association",
    "author",
    "schedule",
    "populationTypeId",
    "isEnterpriseSearchOverride"
})
public class EQDOCFolder
    extends DtDbo
{

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String id;
    @XmlElement(required = true)
    protected String name;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String parentFolder;
    protected Short sequence;
    @XmlSchemaType(name = "string")
    protected VocEnterpriseReportingLevel enterpriseReportingLevel;
    protected List<EQDOCFolderAssociation> association;
    protected EQDOCAuthor author;
    protected List<EQDOCSchedule> schedule;
    @XmlElement(name = "PopulationTypeId")
    @XmlSchemaType(name = "token")
    protected VocPopulationType populationTypeId;
    @XmlElement(name = "IsEnterpriseSearchOverride")
    protected boolean isEnterpriseSearchOverride;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

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
     * Gets the value of the parentFolder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentFolder() {
        return parentFolder;
    }

    /**
     * Sets the value of the parentFolder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentFolder(String value) {
        this.parentFolder = value;
    }

    /**
     * Gets the value of the sequence property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setSequence(Short value) {
        this.sequence = value;
    }

    /**
     * Gets the value of the enterpriseReportingLevel property.
     * 
     * @return
     *     possible object is
     *     {@link VocEnterpriseReportingLevel }
     *     
     */
    public VocEnterpriseReportingLevel getEnterpriseReportingLevel() {
        return enterpriseReportingLevel;
    }

    /**
     * Sets the value of the enterpriseReportingLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link VocEnterpriseReportingLevel }
     *     
     */
    public void setEnterpriseReportingLevel(VocEnterpriseReportingLevel value) {
        this.enterpriseReportingLevel = value;
    }

    /**
     * Gets the value of the association property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the association property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssociation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EQDOCFolderAssociation }
     * 
     * 
     */
    public List<EQDOCFolderAssociation> getAssociation() {
        if (association == null) {
            association = new ArrayList<EQDOCFolderAssociation>();
        }
        return this.association;
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
     * Gets the value of the schedule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the schedule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSchedule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EQDOCSchedule }
     * 
     * 
     */
    public List<EQDOCSchedule> getSchedule() {
        if (schedule == null) {
            schedule = new ArrayList<EQDOCSchedule>();
        }
        return this.schedule;
    }

    /**
     * Gets the value of the populationTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link VocPopulationType }
     *     
     */
    public VocPopulationType getPopulationTypeId() {
        return populationTypeId;
    }

    /**
     * Sets the value of the populationTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link VocPopulationType }
     *     
     */
    public void setPopulationTypeId(VocPopulationType value) {
        this.populationTypeId = value;
    }

    /**
     * Gets the value of the isEnterpriseSearchOverride property.
     * 
     */
    public boolean isIsEnterpriseSearchOverride() {
        return isEnterpriseSearchOverride;
    }

    /**
     * Sets the value of the isEnterpriseSearchOverride property.
     * 
     */
    public void setIsEnterpriseSearchOverride(boolean value) {
        this.isEnterpriseSearchOverride = value;
    }

}
