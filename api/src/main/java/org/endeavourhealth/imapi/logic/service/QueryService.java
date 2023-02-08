package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.imq.Query;
import org.springframework.stereotype.Component;

@Component
public class QueryService {
    private final QueryRepository queryRepository = new QueryRepository();

    public Query labelQuery(Query query) {
        queryRepository.labelQuery(query);
        return query;
    }
}
