//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for voc.PopulationFolderSource.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.PopulationFolderSource"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="ORG"/&gt;
 *     &lt;enumeration value="QOF"/&gt;
 *     &lt;enumeration value="EMIS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "voc.PopulationFolderSource")
@XmlEnum
public enum VocPopulationFolderSource {


    /**
     * Organisation based source
     * 
     */
    ORG,

    /**
     * QOF searches (ie nGMS Searches)
     * 
     */
    QOF,

    /**
     * Emis provided searches
     * 
     */
    EMIS;

    public String value() {
        return name();
    }

    public static VocPopulationFolderSource fromValue(String v) {
        return valueOf(v);
    }

}
