package org.endeavourhealth.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.rdf4j.query.algebra.Str;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;

import java.sql.Connection;

public interface IRuntimeService {

    String getConceptIdForSchemeCode(String scheme, String code);
    String getMappedCoreCodeForSchemeCode(String scheme, String code, boolean snomedOnly);
    String getCodeForTypeTerm(String scheme, String context, String term, boolean autoCreate);
    Integer getConceptDbidForSchemeCode(String scheme, String code);
    Integer getMappedCoreConceptDbidForSchemeCode(String scheme, String code);
    String getCodeForConceptDbid(Integer dbid);
    Integer getConceptDbidForTypeTerm(String type, String term, Boolean autoCreate);
    Integer getMappedCoreConceptDbidForTypeTerm(String type, String term);
    Boolean isInVSet(String code, String scheme, String vSet) throws JsonProcessingException;


}
