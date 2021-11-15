package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;
import java.util.Set;

public class InstanceRepositoryImpl implements InstanceRepository {

    @Override
    public List<Tpl> getTriplesRecursive(String iri, Set<String> predicates) {
        return null;
    }

    @Override
    public List<TTIriRef> search(String request, Set<String> types) {
        return null;
    }

    @Override
    public List<SimpleCount> getTypesCount() {
        return null;
    }
}
