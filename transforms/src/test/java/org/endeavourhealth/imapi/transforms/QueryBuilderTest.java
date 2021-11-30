package org.endeavourhealth.imapi.transforms;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.query.Query;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;

public class QueryBuilderTest {
	@Test
	void buildQuery() throws JsonProcessingException {
		TTEntity entity= new TTEntity()
			.addType(IM.QUERY)
			.setIri("http://example/query#1")
			.setName("Patient Test query")
			.setDescription("A small test query");
		QueryBuilder qb= new QueryBuilder();
		Query query= qb.createQuery();
		qb.setMainEntity(TTIriRef.iri(IM.NAMESPACE+"Patient"));

		entity.set(IM.DEFINITION, TTLiteral.literal(query.toJson()));
		TTToTurtle cnv= new TTToTurtle();
		System.out.println(cnv.transformEntity(entity));

	}
}
