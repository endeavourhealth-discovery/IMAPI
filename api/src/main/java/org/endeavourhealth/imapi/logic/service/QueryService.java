package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryService {
    private final QueryRepository queryRepository = new QueryRepository();

    public Query labelQuery(Query query) {
        queryRepository.labelQuery(query);
        return query;
    }

    public List<TTIriRef> getAllQueries() {
        return queryRepository.getAllQueries();
    }
}
