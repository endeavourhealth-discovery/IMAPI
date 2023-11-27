package org.endeavourhealth.imapi.model.ods;

import java.util.ArrayList;
import java.util.List;

public class OrgRoles {
    private List<OrgRole> role = new ArrayList<>();

    public List<OrgRole> getRole() {
        return role;
    }

    public OrgRoles setRole(List<OrgRole> role) {
        this.role = role;
        return this;
    }

    public OrgRoles addRole(OrgRole orgRole) {
        if (role == null)
            role = new ArrayList<>();

        role.add(orgRole);

        return this;
    }
}
