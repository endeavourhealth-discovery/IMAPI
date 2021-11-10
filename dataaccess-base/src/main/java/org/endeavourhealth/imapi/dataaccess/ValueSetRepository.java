package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;

import java.util.List;

public interface ValueSetRepository {
    List<ValueSetMember> expandMember(String iri);

    List<ValueSetMember> expandMember(String iri, Integer limit) throws DALException;
}
