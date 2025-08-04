package org.endeavourhealth.imapi.filer.rdf4j;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.imapi.vocabulary.types.Graph;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LuceneIndexer {

  public void buildIndexes(Graph graph) {
    try (IMDB conn = IMDB.getConnection(graph)) {
      dropIndex(conn, graph);
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
      log.info("Building lucene index... This will take an hour or so...");
      Update upd = conn.prepareInsertSparql(sql, graph);
      upd.execute();
    }
  }

  private void dropIndex(IMDB conn, Graph graph) {
    String checkList = """
        PREFIX luc: <http://www.ontotext.com/connectors/lucene#>
      
        SELECT ?cntUri ?cntStr {
          ?cntUri luc:listConnectors ?cntStr .
        }
      """;
    TupleQuery qry = conn.prepareTupleSparql(checkList);
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
        log.info("Dropping lucene index...");
        Update upd = conn.prepareInsertSparql(spq, graph);
        upd.execute();
      }
    }

  }

}
