package org.endeavourhealth.imapi.filer.rdf4j;

import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;

public class DBIDGenerator {

	public void generateDBIDs(){
		try (RepositoryConnection conn= ConnectionManager.getIMConnection()){
			String sql= "Select distinct ?entity \nwhere {"+
				"?entity <"+ RDF.TYPE+"> ?type.\n"+
				"filter (?entity not in (<"+ IM.NAMESPACE+"Organisation"+">)) }";
			TupleQuery qry= conn.prepareTupleQuery(sql);

		}

	}
}
