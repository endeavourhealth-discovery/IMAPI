package org.endeavourhealth.imapi.filer.rdf4j;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LuceneIndexer {
  private static final Logger LOG = LoggerFactory.getLogger(LuceneIndexer.class);

  public void buildIndexes() {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      dropIndex(conn);
      String sql = """
        PREFIX con: <http://www.ontotext.com/connectors/lucene#>
        PREFIX con-inst: <http://www.ontotext.com/connectors/lucene/instance#>

        INSERT DATA {
          con-inst:im_fts con:createConnector '''
            {
              "fields": [
                {
                  "fieldName": "label",
                  "propertyChain": [
                    %s
                  ]
                },
                {
                  "fieldName": "code",
                  "propertyChain": [
                     %s
                  ],
                  "datatype": "xsd:string"
                }
              ],
              "types" : [
                %s,
                %s,
                %s,
                %s,
                %s,
                %s,
                %s,
                %s,
                %s,
                %s
              ]
            }
          ''' .
        }
        """.formatted(RDFS.LABEL, IM.CODE, IM.CONCEPT, IM.FOLDER, IM.FORM_GENERATOR, IM.FUNCTION, IM.COHORT_QUERY, IM.DATASET_QUERY, IM.QUERY, SHACL.NODESHAPE, RDFS.CLASS, RDF.PROPERTY);
      LOG.info("Building lucene index... This will take an hour or so...");
      Update upd = conn.prepareUpdate(sql);
      upd.execute();
    }
  }

  private void dropIndex(RepositoryConnection conn) {
    String checkList = """
        PREFIX luc: <http://www.ontotext.com/connectors/lucene#>
            
        SELECT ?cntUri ?cntStr {
          ?cntUri luc:listConnectors ?cntStr .
        }
      """;
    TupleQuery qry = conn.prepareTupleQuery(checkList);
    List<String> connectors = new ArrayList<>();
    try (TupleQueryResult rs = qry.evaluate()) {
      while (rs.hasNext()) {
        BindingSet bs = rs.next();
        connectors.add(bs.getValue("cntUri").stringValue());
      }

      if (connectors.contains("http://www.ontotext.com/connectors/lucene/instance#im_fts")) {

        String spq = """
            PREFIX con: <http://www.ontotext.com/connectors/lucene#>
            PREFIX con-inst: <http://www.ontotext.com/connectors/lucene/instance#>
                  
            INSERT DATA {
              con-inst:im_fts con:dropConnector [].
            }
          """;
        LOG.info("Dropping lucene index...");
        Update upd = conn.prepareUpdate(spq);
        upd.execute();
      }
    }

  }

}
