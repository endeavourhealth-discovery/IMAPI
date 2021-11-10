package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class XMLToTTShapesTest {

	@Test
	void parseFromStream() throws XMLStreamException, JsonProcessingException {
		/*
		String xml="<root>\n" +
			" <branch attrib=\"1\"/><branch2>\n" +
			"  <branch3>leafdata</branch3></branch2><branch4 attrib=\"2\"></branch4>\n" +
			"</root>\n";
		InputStream input = new ByteArrayInputStream(xml.getBytes());
		XMLToTTShapes importer= new XMLToTTShapes();
		System.out.println("Creating model from file ......");
		TTDocument document = importer.parseFromStream(input,"http://example#example", IM.NAMESPACE+"ExampleModel");
		TTManager manager= new TTManager();
		manager.setDocument(document);
		manager.saveDocument(new File("c:\\temp\\ExampleShapes.json"));

		 */

	}
}