package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.eclBuilder.BoolGroup;
import org.endeavourhealth.imapi.model.imq.Match;

public class ECLBuilderToIMQ {
    public Match getIMQFromEclBuilder(BoolGroup boolGroup) {
        return createMatch(boolGroup);
    }

    private Match createMatch(BoolGroup boolGroup) {
        Match match = new Match();
        return match;
    }
}
