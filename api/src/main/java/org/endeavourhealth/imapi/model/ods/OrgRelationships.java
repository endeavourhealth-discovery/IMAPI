package org.endeavourhealth.imapi.model.ods;

import java.util.ArrayList;
import java.util.List;

public class OrgRelationships {
    private List<OrgRelationship> rel = new ArrayList<>();

    public List<OrgRelationship> getRel() {
        return rel;
    }

    public OrgRelationships setRel(List<OrgRelationship> rel) {
        this.rel = rel;
        return this;
    }

    public OrgRelationships addRel(OrgRelationship orgRel) {
        if (rel == null)
            rel = new ArrayList<>();
        rel.add(orgRel);

        return this;
    }
}
