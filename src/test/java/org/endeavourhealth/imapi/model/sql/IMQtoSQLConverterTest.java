package org.endeavourhealth.imapi.model.sql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.EntityType;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

public class IMQtoSQLConverterTest {
  private static final Logger LOG = LoggerFactory.getLogger(IMQtoSQLConverterTest.class);

//  @Test
  public void IMQtoSQL() {
    // Get list of queries from GraphDb
    EntityRepository entityRepository = new EntityRepository();
    List<TTIriRef> cohortQueryIris = entityRepository.findEntitiesByType(EntityType.QUERY);
    LOG.info("Found {} queries", cohortQueryIris.size());

    // Prepare
    ObjectMapper om = new ObjectMapper();
    int passed = 0;
    Set<String> errors = new HashSet<>();
    // For each query iri
    for (TTIriRef cohortQueryIri : cohortQueryIris) {
      TTBundle bundle = entityRepository.getBundle(cohortQueryIri.getIri(), asHashSet(IM.DEFINITION));
      if (bundle == null || bundle.getEntity() == null || !bundle.getEntity().has(IM.DEFINITION.asIri())) {
        LOG.error("Entity or definition not found!");
        continue;
      }

      String definition = bundle.getEntity().get(IM.DEFINITION.asIri()).asLiteral().getValue();
      try {
        // convert it
        Query query = om.readValue(definition, Query.class);
        IMQtoSQLConverter imq2sql = new IMQtoSQLConverter(new QueryRequest().setQuery(query));
        passed++;
      } catch (JsonProcessingException e) {
        LOG.error("Error parsing query", e);
      } catch (SQLConversionException e) {
        LOG.error("Failed to convert query:{} - error:{}", cohortQueryIri.getIri(), e.getMessage());
        errors.add(e.getMessage());
      }
    }
    LOG.info("Passed {} of {} tests", passed, cohortQueryIris.size());
    LOG.info("Errors: {}", errors);
  }
}