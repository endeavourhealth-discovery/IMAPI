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
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

public class IMQtoSQLConverterTest {
  private static Logger LOG = LoggerFactory.getLogger(IMQtoSQLConverterTest.class);
  private String db_url = System.getenv("DB_URL");
  private String db_user = System.getenv("DB_USER");
  private String db_password = System.getenv("DB_PASSWORD");
  private String db_driver = System.getenv("DB_DRIVER");

  //  @Test
  public void IMQtoSQL(List<Graph> graphs) {
    // Get list of queries from GraphDb
    EntityRepository entityRepository = new EntityRepository();
    List<TTIriRef> cohortQueryIris = entityRepository.findEntitiesByType(EntityType.QUERY, graphs);
    LOG.info("Found {} queries", cohortQueryIris.size());

    // Prepare
    ObjectMapper om = new ObjectMapper();
    try {
      Class.forName(db_driver);
    } catch (ClassNotFoundException e) {
      LOG.error("Postgres driver not found", e);
      throw new RuntimeException(e);
    }
    try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {

      int passed = 0;
      // For each query iri
      for (TTIriRef cohortQueryIri : cohortQueryIris) {
        // Get the definition
        LOG.info("Checking [{} | {}]", cohortQueryIri.getIri(), cohortQueryIri.getName());
        TTBundle bundle = entityRepository.getBundle(cohortQueryIri.getIri(), asHashSet(IM.DEFINITION), graphs);

        if (bundle == null || bundle.getEntity() == null || !bundle.getEntity().has(IM.DEFINITION.asIri())) {
          LOG.error("Entity or definition not found!");
          continue;
        }

        String definition = bundle.getEntity().get(IM.DEFINITION.asIri()).asLiteral().getValue();
        LOG.info("Definition found");
        try {
          // convert it
          Query query = om.readValue(definition, Query.class);
          IMQtoSQLConverter imq2sql = new IMQtoSQLConverter(new QueryRequest().setQuery(query));
          SqlWithSubqueries sqlWithSubqueries = imq2sql.IMQtoSQL();

          // Replace variables
          String sql = sqlWithSubqueries.getSql().replace("$referenceDate", "NOW()");

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
          LOG.error("Failed to convert query: {}", cohortQueryIri.getIri(), e);
          LOG.error(definition);
        }
      }

      LOG.info("Passed {} of {} tests", passed, cohortQueryIris.size());
    } catch (SQLException e) {
      LOG.error("Failed to connect!", e);
    }
  }
}