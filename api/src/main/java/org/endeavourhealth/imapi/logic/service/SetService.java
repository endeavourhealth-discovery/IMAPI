package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.model.imq.Query;
import org.springframework.stereotype.Component;

@Component
public class SetService {
    private final SetRepository setRepository = new SetRepository();
    private final QueryRepository queryRepository = new QueryRepository();

    public Query setQueryLabels(Query query) {
        queryRepository.labelQuery(query);
        return query;
    }
}
