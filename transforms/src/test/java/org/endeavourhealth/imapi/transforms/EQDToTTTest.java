package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Properties;
import java.util.zip.DataFormatException;

class EQDToTTTest {

	@Test
	void convert() throws IOException, DataFormatException, XPathExpressionException, ParserConfigurationException, SAXException {
		/*
		currentReg();
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
		Query qry= new Query();
		qry.setIri(IM.NAMESPACE+"Q_RegisteredGMS");
		qry.setName("Patients registered for GMS services on the reference date");
		qry.setDescription("For any registration period,a registration start date before the reference date and no end date,"+
			"or an end date after the reference date.");
		QueryClause clause= qry.setDefinition();
		WhereClause hasReg= clause.addWhere();
		hasReg.setSubject(TTIriRef.iri(IM.NAMESPACE+"Patient"))
		.setPredicate(IM.NAMESPACE+"isSubjectOf")
			.setObjectType(IM.NAMESPACE+"GPRegistration");
		WhereClause gpRegType= clause.addWhere();
		gpRegType.setSubject(hasReg.getObjectVar())
			.setPredicate(IM.NAMESPACE+"patientType");
		gpRegType.setFilter()
				.setCompareTo(IM.EQUAL,IM.GMS_PATIENT);
		WhereClause gpRegDate= clause.addWhere()
			.setSubject(hasReg.getObjectVar())
				.setPredicate(IM.NAMESPACE+"effectiveDate");
		gpRegDate.setFilter()
			.setCompareTo(IM.LESS_THAN_EQUAL, TTLiteral.literal("$ReferenceDate"));
		WhereClause noEnd= clause.addOr()
			.setNot()
			.setPredicate(IM.NAMESPACE+"endDate");
		WhereClause endAfter= clause.addOr()
			.setPredicate(TTIriRef.iri(IM.NAMESPACE+"endDate"));
			endAfter.setFilter()
			.setCompareTo(IM.GREATER_THAN,TTLiteral.literal("$ReferenceDate"));
		TTManager manager= new TTManager();
		TTDocument document= manager.createDocument();
		document.addEntity(qry);
		System.out.println(manager.getJson(document));

	}
}