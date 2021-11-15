package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class XMLToTTShapesTest {

	@Test
	void parseFromStream() throws XMLStreamException, JsonProcessingException {



	}

	@Test
	void parseFromFile() throws XMLStreamException, IOException {
/*
		XMLToTTShapes importer= new XMLToTTShapes();
		TTDocument document= 	importer.parseFromFile("C:\\CodeMaps\\ODS\\HSCOrgRefData_Full_20210920.xml",IM.SOURCE_TRUD_ODS.getIri(),IM.NAMESPACE+"DiscoveryCommonDataModel");
		TTManager.Grammar grammar= TTManager.Grammar.JSON;
		//TTManager.Grammar grammar= TTManager.Grammar.TURTLE;
		TTManager.saveDocument(document,"G:\\Shared drives\\Discovery Data Service\\InformationModel\\ImportData\\ODS\\TRUD-ODSDataModel.json", grammar);

 */

	}
}