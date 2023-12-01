//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.23 at 09:36:56 AM GMT 
//


package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for voc.AttachmentType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="voc.AttachmentType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="WORD"/&gt;
 *     &lt;enumeration value="JPEG"/&gt;
 *     &lt;enumeration value="GIF"/&gt;
 *     &lt;enumeration value="TIF"/&gt;
 *     &lt;enumeration value="BMP"/&gt;
 *     &lt;enumeration value="XML"/&gt;
 *     &lt;enumeration value="RTF"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "voc.AttachmentType")
@XmlEnum
public enum VocAttachmentType {


    /**
     * Word Document
     * 
     */
    WORD,

    /**
     * JPeg File
     * 
     */
    JPEG,

    /**
     * GIF File
     * 
     */
    GIF,

    /**
     * TIF File
     * 
     */
    TIF,

    /**
     * BMP File
     * 
     */
    BMP,

    /**
     * XML Document
     * 
     */
    XML,

    /**
     * RTF Document
     * 
     */
    RTF;

    public String value() {
        return name();
    }

    public static VocAttachmentType fromValue(String v) {
        return valueOf(v);
    }

}
