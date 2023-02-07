package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ECLToIMTest {


	private String testDefinitions;
	private String testResults;
//@Test
	void convertConceptSet() throws DataFormatException, JsonProcessingException, IOException {
		testDefinitions= System.getenv("folder")+"\\ECLDefinitions";
		testResults= System.getenv("folder")+"\\ECLResults";

		for (String ecl:getTestEcl()) {
			String name= ecl.split("\\*")[1];
			System.out.println("Testing "+ name);
			try (FileWriter wr = new FileWriter(testDefinitions+ "\\"  + name+ "_definition.ecl")) {
				wr.write(ecl);;

			}
			ECLToIML iml = new ECLToIML();
			Query query = iml.getQueryFromECL(ecl);
			try (FileWriter wr = new FileWriter(testDefinitions+ "\\"  + name+ "_definition.json")) {
				wr.write(new ObjectMapper().writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(query));
			}
		//	System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(query));
			String ecl2 = IMLToECL.getECLFromQuery(query, true);
			String tstecl=ecl.split("\\*/")[1];
			tstecl= tstecl.replaceAll(" ","").replaceAll("\n","").replaceAll("\t","").toLowerCase();
			String tstecl2= ecl2.replaceAll(" ","").replaceAll("\n","").toLowerCase();

			assertEquals(tstecl,tstecl2);
			//System.out.println(ecl);
			//System.out.println(ecl2);
		}

	}

	private List<String> getTestEcl() {
		return List.of(
			"/*oral nsaids*/<<763158003:<<127489000= <<372665008,<<411116001= 385268001",

			"/*minus a wild card refined*/\t((<<298705000 |Finding of region of thorax (finding)| "+
				"and <<301366005 |Pain of truncal structure (finding)| ):"+"" +
				" { <<762705008 =  <<51185008 |Thoracic structure (body structure)| })"+
				"minus (*: { 363698007 |Finding site (attribute)| = 722725008 |Structure of right sternoclavicular joint (body structure)| })",

			"/* or group minus or group*/ (<<386725007 | Body temperature (observable entity) |   OR <<431314004 | Peripheral oxygen saturation (observable entity) |)   \n" +
				"MINUS (<<838441000000103 | Target body mass index (observable entity) |  OR <<838451000000100 | Target body mass index  | ) ",



			"/*minus a concept*/\t((<<298705000 |Finding of region of thorax (finding)| and "+
				"<<301366005 |Pain of truncal structure (finding)| ): { <<762705008 =  <<51185008 |Thoracic structure (body structure)| })"+
				"minus <<426396005 |Cardiac chest pain (finding)|",

			"/*union with refinements*/	(<<116536008 or <<350312004)" +
				":<<127489000 = <<372665008, <<411116001 = <<385268001",

			"/*two attribute groups*/	<<225399009 |Pain assessment (procedure)|" +
				":{ <<260686004 |Method (attribute)| =  <<129265001 |Evaluation - action (qualifier value)| }," +
				"{ <<363702006 |Has focus (attribute)| = <<29857009 |Chest pain (finding)| }",

			"/*and no attribute group*/	(<<298705000 |Finding of region of thorax (finding)| " +
				"and <<301366005 |Pain of truncal structure (finding)| ): <<762705008 =  <<51185008 |Thoracic structure (body structure)|",


			"/*and grouped with subsumption attribute value*/	<<298705000 |Finding of region of thorax (finding)| " +
				"and (<<301366005 |Pain of truncal structure (finding)| :" +
				"{ 363698007 |Finding site (attribute)| =  <<51185008 |Thoracic structure (body structure)| })",


			"/*bracketed and*/	(<<298705000 |Finding of region 2of thorax (finding)| " +
				"and <<301366005 |Pain of truncal structure (finding)| ):"+
				"{ <<762705008 =  <<51185008 |Thoracic structure (body structure)| }",

			"/*and with refinement of second concept*/	<<298705000 |Finding of region of thorax (finding)| " +
				"and (<<301366005 |Pain of truncal structure (finding)| :" +
				"{ 363698007 |Finding site (attribute)| = 51185008 |Thoracic structure (body structure)| })",

			"/*simple and should be 0*/	298705000 and 301366005 |Pain of truncal structure (finding)|",

			"/*Single concept*/	29857009",

			"/*Descendants not self*/	<29857009",

			"/*Descendants and self*/	<<29857009",

			"/*simple and descendants*/	<298705000 and <301366005 |Pain of truncal structure (finding)|",


			"/*and with subsumption property and value*/	<<298705000 |Finding of region of thorax (finding)| " +
				"and (<<301366005 |Pain of truncal structure (finding)| : { <<762705008 =  <<51185008 |Thoracic structure (body structure)| })",


			"/*merged group error*/	<<225399009 |Pain assessment (procedure)| " +
				":{ <<260686004 |Method (attribute)| =  <<129265001 |Evaluation - action (qualifier value)| ," +
				"<<363702006 |Has focus (attribute)| = <<29857009 |Chest pain (finding)| }",

			"/*ungrouped but seperate groups*/	<<225399009 |Pain assessment (procedure)|" +
				":<<260686004 |Method (attribute)| =  <<129265001 |Evaluation - action (qualifier value)| ," +
				"<<363702006 |Has focus (attribute)| = <<29857009 |Chest pain (finding)|",



			"/*allergy to penicillins or cephasporins with causative lactams*/	(<<91936005 | Allergy to Penicillin| or <<294532003)" +
				":  << 246075003 |Causative agent (attribute)| = <<771577000| lactam (substance)|"

		);
	}

}
