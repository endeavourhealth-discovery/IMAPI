package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.EntitySummary;
import org.endeavourhealth.imapi.model.IMv2v1Map;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;

import java.util.List;
import java.util.Set;

public interface SetRepository {
    TTEntity getSetDefinition(String setIri);

    Set<IMv2v1Map> getIMv2v1Maps(Set<EntitySummary> members);

    List<ValueSetMember> expandMember(String iri);

    List<ValueSetMember> expandMember(String iri, Integer limit) throws DALException;

    Set<TTEntity> getAllConceptSets(TTIriRef type);

    TTEntity getExpansion(TTEntity conceptSet);

    TTEntity getLegacyExpansion(TTEntity conceptSet);

    TTEntity getIM1Expansion(TTEntity conceptSet);
}
