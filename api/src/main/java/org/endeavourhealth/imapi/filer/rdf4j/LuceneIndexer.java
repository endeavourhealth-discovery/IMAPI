package org.endeavourhealth.imapi.filer.rdf4j;

import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuceneIndexer {
	private static final Logger LOG = LoggerFactory.getLogger(LuceneIndexer.class);

	public void buildIndexes(){
		try (RepositoryConnection conn= ConnectionManager.getIMConnection()){
			dropIndex(conn);
			String sql="PREFIX con: <http://www.ontotext.com/connectors/lucene#>\n" +
				"PREFIX con-inst: <http://www.ontotext.com/connectors/lucene/instance#>\n" +
				"\n" +
				"INSERT DATA {\n" +
				"    con-inst:im_fts con:createConnector '''\n" +
				"    {\n" +
				"      \"fields\": [\n" +
				"        {\n" +
				"          \"fieldName\": \"label\",  \n" +
				"          \"propertyChain\": [\n" +
				"            \"http://www.w3.org/2000/01/rdf-schema#label\"\n" +
				"          \n" +
				"          ]\n" +
				"        },\n" +
				"{\n" +
				"    \"fieldName\": \"l code\",  \n" +
				"          \"propertyChain\": [\n" +
				"             \"http://endhealth.info/im#code\"\n" +
				"          ]\n" +
				"\n" +
				"     }\n" +
				"      ],\n" +
				"\"types\" : [\"http://endhealth.info/im#Concept\",\n" +
				"            \"http://endhealth.info/im#Folder\",\n" +
				"          \"http://endhealth.info/im#FormGenerator\",\n" +
				"        \"http://endhealth.info/im#Function\",\n" +
				"\t\t\"http://www.w3.org/ns/shacl#NodeShape\",\n" +
				"        \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Class\",\n" +
				"        \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Property\",\n" +
				"         \"http://www.w3.org/ns/shacl#Query\"]\n" +
				"    }\n" +
				"''' .\n" +
				"}";
			LOG.info("Building lucene index... This will take an hour or so...");
			Update upd= conn.prepareUpdate(sql);
			upd.execute();
		}
	}

	private void dropIndex(RepositoryConnection conn) {
		String spq="PREFIX con: <http://www.ontotext.com/connectors/lucene#>\n" +
			"PREFIX con-inst: <http://www.ontotext.com/connectors/lucene/instance#>\n" +
			"\n" +
			"INSERT DATA {\n" +
			"    con-inst:im_fts con:dropConnector [].}";
		LOG.info("Dropping lucene index...");
		Update upd= conn.prepareUpdate(spq);
		upd.execute();

	}

}
