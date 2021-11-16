package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class InstanceRepositoryImpl implements InstanceRepository {

    @Override
    public List<Tpl> getTriplesRecursive(String iri, Set<String> predicates) {
        return Collections.emptyList();
    }

    @Override
    public List<TTIriRef> search(String request, Set<String> types) {
        return Collections.emptyList();
    }

    @Override
    public List<SimpleCount> getTypesCount() {
        return Collections.emptyList();
    }
}
