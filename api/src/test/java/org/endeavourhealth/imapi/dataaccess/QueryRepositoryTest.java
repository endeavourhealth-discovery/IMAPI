package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.sets.ConceptRef;
import org.endeavourhealth.imapi.model.sets.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class QueryRepositoryTest {

	@Test
	void entityQuery() throws DataFormatException, IOException {
		QueryRequest qr= new QueryRequest()
			.query(q->q
				.select(s->s
					.setEntityId(ConceptRef.iri(IM.NAMESPACE+"QueryShape"))));
		SearchService ss= new SearchService();
		List<TTEntity> result= ss.entityQuery(qr);
		try (FileWriter wr= new FileWriter("c:\\temp\\Shape.json")){
			wr.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result));
		}
	}
}