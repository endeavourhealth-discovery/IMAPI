package org.endeavourhealth.informationmanager.model;

import java.util.Set;

public interface DataRange {
    String getExactValue();
    DataRange setExactValue(String value);
    Set<ConceptReference> getOneOf();
    DataRange setOneOf(Set<ConceptReference> oneOf);
    DataRange addOneOf(ConceptReference value);
    DataRange addOneOf(String value);
    ConceptReference getDataType();
    DataRange setDataType(ConceptReference dataType);
    DataRange setDataType(String dataType);

}
