package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.query.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.zip.DataFormatException;

class EQDToTTTest {

	@Test
	void convert() throws IOException, DataFormatException, XPathExpressionException, ParserConfigurationException, SAXException {
		currentReg();
		EQDToTT converter= new EQDToTT();
		String xml="G:\\Shared drives\\Discovery Data Service\\InformationModel\\ImportData\\CEG\\UCLP-CEG SMI EMIS v5.xml";
		Properties dataMap= new Properties();
		dataMap.load(new FileInputStream("G:\\Shared drives\\Discovery Data Service\\InformationModel\\ImportData\\EMIS\\EqdDataMap.properties"));
		TTDocument document= converter.convert(
			new FileInputStream(xml),
				dataMap,"http://qmul/ceg/query","http://org.endhealth.info/im#QMUL/CEG");
		new TTManager().setDocument(document).saveDocument(new File("c:/temp/Query.json"));


	}

	private void currentReg() {
		Query reg= new Query();
		reg.setIri(IM.NAMESPACE+"Q_RegisteredGMS");
		reg.setName("Patients registered for GMS services on the reference date");
		reg.setDescription("For any registration period,a registration start date before the reference date and no end date,"+
			"or an end date after the reference date.");
		QueryClause def= reg.addClause();
		QueryClause wrong= reg.addClause();
		def.set(IM.HAS_ORDER, TTLiteral.literal(5));
		wrong.set(IM.HAS_ORDER,TTLiteral.literal(1));
		def.setSubject(TTIriRef.iri(IM.NAMESPACE+"Patient"));
		List<QueryClause> clauses =reg.getClauses();
		for (QueryClause clause:clauses)
			System.out.println(clause.get(IM.HAS_ORDER).asLiteral().getValue());
		/*
		Q ueryStep step= new QueryStep();
		def.addStep(step);
		step.setInclusionAction(IM.INCLUDE_EXCLUDE);
		step.setOperator(IM.OR);
		MatchClause match= step.addMatch();
		MatchPath path = match.addPath();
		path.setProperty(TTIriRef.iri(IM.NAMESPACE+"isSubjectOf"));
		MatchValue matchValue=path.setMatchValue(new MatchValue());
		matchValue.setType(TTIriRef.iri(IM.NAMESPACE+"GPRegistration"));
		matchValue.setMatch(new MatchClause());

		 */


	}
}