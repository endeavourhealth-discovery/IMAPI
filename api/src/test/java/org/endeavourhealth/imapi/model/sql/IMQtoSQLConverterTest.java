package org.endeavourhealth.imapi.model.sql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class IMQtoSQLConverterTest {
  private static Logger LOG = LoggerFactory.getLogger(IMQtoSQLConverterTest.class);

  // @Test
  public void IMQtoSQL() {
    // Get list of queries from GraphDb
    EntityRepository entityRepository = new EntityRepository();
    List<TTIriRef> cohortQueryIris = entityRepository.findEntitiesByType(IM.COHORT_QUERY);
    LOG.info("Found {} queries", cohortQueryIris.size());

    // Prepare
    ObjectMapper om = new ObjectMapper();
    IMQtoSQLConverter imq2sql = new IMQtoSQLConverter();
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      LOG.error("Postgres driver not found", e);
      throw new RuntimeException(e);
    }
    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/postgres?currentSchema=lair", "postgres", "Taz123")) {

      int passed = 0;
      // For each query iri
      for (TTIriRef cohortQueryIri : cohortQueryIris) {
        // Get the definition
        LOG.info("Checking [{} | {}]", cohortQueryIri.getIri(), cohortQueryIri.getName());
        TTBundle bundle = entityRepository.getBundle(cohortQueryIri.getIri(), Set.of(IM.DEFINITION));

        if (bundle == null || bundle.getEntity() == null || !bundle.getEntity().has(iri(IM.DEFINITION))) {
          LOG.error("Entity or definition not found!");
          continue;
        }

        String definition = bundle.getEntity().get(iri(IM.DEFINITION)).asLiteral().getValue();
        LOG.info("Definition found");
        try {
          // convert it
          Query query = om.readValue(definition, Query.class);
          String sql = imq2sql.IMQtoSQL(query);

          // Replace variables
          sql = sql.replace("$referenceDate", "NOW()");

          // run on postgres
          try (PreparedStatement preparedStatement = connection.prepareStatement("EXPLAIN " + sql)) {
            preparedStatement.execute();
            passed++;
            LOG.info("Passed!");
          } catch (SQLException e) {
            LOG.error("Failed!", e);
            LOG.debug(sql);
          }

        } catch (JsonProcessingException e) {
          LOG.error("Error parsing query", e);
        } catch (SQLConversionException e) {
          LOG.error("Failed to convert query", e);
          LOG.error(definition);
        }
      }

      LOG.info("Passed {} of {} tests", passed, cohortQueryIris.size());
    } catch (SQLException e) {
      LOG.error("Failed to connect!", e);
    }
  }
}