package org.endeavourhealth.imapi.model.eclBuilder;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

public class SubExpressionConstraint {
    private TTIriRef concept;
    private String operator;
    private Boolean memberOf;
    private List<TTIriRef> members;
}
