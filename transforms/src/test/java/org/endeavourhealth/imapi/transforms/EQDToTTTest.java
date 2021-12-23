package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.query.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.zip.DataFormatException;

class EQDToTTTest {

	@Test
	void convert() throws IOException, DataFormatException, XPathExpressionException, ParserConfigurationException, SAXException {

		currentReg();
		/*
		EQDToTT converter= new EQDToTT();
		String xml="G:\\Shared drives\\Discovery Data Service\\InformationModel\\ImportData\\CEG\\UCLP-CEG SMI EMIS v5.xml";
		Properties dataMap= new Properties();
		dataMap.load(new FileInputStream("G:\\Shared drives\\Discovery Data Service\\InformationModel\\ImportData\\EMIS\\EqdDataMap.properties"));
		TTDocument document= converter.convert(
			new FileInputStream(xml),
				dataMap,"http://qmul/ceg/query","http://org.endhealth.info/im#QMUL/CEG");
		new TTManager().setDocument(document).saveDocument(new File("c:/temp/Query.json"));

		 */
	}

	private void currentReg() throws JsonProcessingException {
		TTEntity entity= new TTEntity()
		.setIri(IM.NAMESPACE+"Q_RegisteredGMS")
		.setName("Patients registered for GMS services on the reference date")
		.setDescription("For any registration period,a registration start date before the reference date and no end date,"+
			"or an end date after the reference date.")
			.addType(IM.QUERY);
		Query qry= new Query();
		qry.setMainSubject(IM.NAMESPACE+"Patient");
		QueryStep step= qry.addStep();
		step.setInclude(Include.MUST);
		MatchClause gpReg= step.addMatch();
		gpReg.setSubject(TTIriRef.iri(IM.NAMESPACE+"GPRegistration"),"reg")
			.setPredicate(IM.NAMESPACE+"patientType")
			.setObject(Comparison.memberOf,IM.GMS_PATIENT);

		MatchClause gpRegDate= step.addMatch()
			.setSubject("reg")
				.setPredicate(IM.NAMESPACE+"effectiveDate")
			.setObject(Comparison.lessThanOrEqual,"$ReferenceDate");
		MatchClause noEnd= step.addOr()
			.setNot(true)
			.setPredicate(IM.NAMESPACE+"endDate");
		MatchClause endAfter= step.addOr()
			.setPredicate(TTIriRef.iri(IM.NAMESPACE+"endDate"));
			endAfter.setObject(Comparison.greaterThan,"$ReferenceDate");

			entity.set(IM.DEFINITION,TTLiteral.literal(qry.toJson()));
			//TTManager manager= new TTManager();
		//TTDocument document= manager.createDocument();
		//document.addEntity(entity);
		//System.out.println(manager.getJson(document));
		System.out.println(qry.toJson());

	}
}