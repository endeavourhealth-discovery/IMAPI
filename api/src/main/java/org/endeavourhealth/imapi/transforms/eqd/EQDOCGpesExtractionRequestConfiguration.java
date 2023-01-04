//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for EQDOC.GpesExtractionRequestConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EQDOC.GpesExtractionRequestConfiguration"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element name="aggregateFromReport" type="{http://www.e-mis.com/emisopen}EQDOC.GpesAggregateFromReport"/&gt;
 *         &lt;element name="aggregateFromSubstitution" type="{http://www.e-mis.com/emisopen}EQDOC.GpesAggregateFromSubstitution"/&gt;
 *         &lt;element name="patientLevelFromListReport" type="{http://www.e-mis.com/emisopen}EQDOC.GpesPatientLevelFromListReport"/&gt;
 *         &lt;element name="freeForm" type="{http://www.e-mis.com/emisopen}EQDOC.GpesFreeForm"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.GpesExtractionRequestConfiguration", propOrder = {
    "aggregateFromReportOrAggregateFromSubstitutionOrPatientLevelFromListReport"
})
public class EQDOCGpesExtractionRequestConfiguration {

    @XmlElements({
        @XmlElement(name = "aggregateFromReport", type = EQDOCGpesAggregateFromReport.class),
        @XmlElement(name = "aggregateFromSubstitution", type = EQDOCGpesAggregateFromSubstitution.class),
        @XmlElement(name = "patientLevelFromListReport", type = EQDOCGpesPatientLevelFromListReport.class),
        @XmlElement(name = "freeForm", type = EQDOCGpesFreeForm.class)
    })
    protected List<Object> aggregateFromReportOrAggregateFromSubstitutionOrPatientLevelFromListReport;

    /**
     * Gets the value of the aggregateFromReportOrAggregateFromSubstitutionOrPatientLevelFromListReport property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aggregateFromReportOrAggregateFromSubstitutionOrPatientLevelFromListReport property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAggregateFromReportOrAggregateFromSubstitutionOrPatientLevelFromListReport().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EQDOCGpesAggregateFromReport }
     * {@link EQDOCGpesAggregateFromSubstitution }
     * {@link EQDOCGpesPatientLevelFromListReport }
     * {@link EQDOCGpesFreeForm }
     * 
     * 
     */
    public List<Object> getAggregateFromReportOrAggregateFromSubstitutionOrPatientLevelFromListReport() {
        if (aggregateFromReportOrAggregateFromSubstitutionOrPatientLevelFromListReport == null) {
            aggregateFromReportOrAggregateFromSubstitutionOrPatientLevelFromListReport = new ArrayList<Object>();
        }
        return this.aggregateFromReportOrAggregateFromSubstitutionOrPatientLevelFromListReport;
    }

}
