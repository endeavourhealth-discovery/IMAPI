package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;
import java.util.Set;

public interface InstanceRepository {
    List<Tpl> getTriplesRecursive(String iri, Set<String> predicates);

    List<TTIriRef> search(String request, Set<String> types);

    List<SimpleCount> getTypesCount();

}
