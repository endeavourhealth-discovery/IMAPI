package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class EQDToTTTest {

	@Test
	void convert() throws IOException, DataFormatException, XPathExpressionException, ParserConfigurationException, SAXException {

		EQDToTT converter= new EQDToTT();
		String xml="G:\\Shared drives\\Discovery Data Service\\InformationModel\\ImportData\\CEG\\UCLP-CEG SMI EMIS v5.xml";
		Properties dataMap= new Properties();
		dataMap.load(new FileInputStream("G:\\Shared drives\\Discovery Data Service\\InformationModel\\ImportData\\EMIS\\EqdDataMap.properties"));
		TTDocument document= converter.convert(
			new FileInputStream(xml),
				dataMap,"http://qmul/ceg/query","http://org.endhealth.info/im#QMUL/CEG");
		new TTManager().setDocument(document).saveDocument(new File("c:/temp/Query.json"));


	}
}