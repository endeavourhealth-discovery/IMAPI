package org.endeavourhealth.imapi.queryengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryGeneratorTest {
    private static final Logger LOG = LoggerFactory.getLogger(QueryGeneratorTest.class);

    private static final EntityService svc = new EntityService();

    public static void main(String[] argv) throws JsonProcessingException {
        LOG.info("Initializing");

        // String sql = new QueryGenerator().getSelect("http://endhealth.info/im#Q_RegisteredGMS");
        // String sql = new QueryGenerator().getSelect("urn:uuid:5b8b8f6a-fa2c-43d3-bf61-9c01e506710b");
        String sql = new QueryGenerator().getSelect("urn:uuid:6d517466-813b-46a8-b848-aaf5a4fbdcbf").build();

        LOG.info("Replace variables");
        // sql = sql.replace("$ReferenceDate", "'" + DateTime.now()     + "'");
        sql = sql.replace("$ReferenceDate", "'2002-07-10 00:00:00'");

        LOG.info("Final statement:\n=======================================================================\n{}\n=======================================================================", sql);

        LOG.info("Done");
    }
}
