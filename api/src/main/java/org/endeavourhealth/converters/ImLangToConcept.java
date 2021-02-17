package org.endeavourhealth.converters;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptStatus;
import org.endeavourhealth.imapi.model.ConceptType;
import org.endeavourhealth.informationmanager.parser.IMLangLexer;
import org.endeavourhealth.informationmanager.parser.IMLangParser;
import org.springframework.stereotype.Component;

@Component
public class ImLangToConcept {


	public Concept translateDefinitionToConcept(String conceptDefinition) {
		
		System.out.println(conceptDefinition);
		
		CharStream in = CharStreams.fromString(conceptDefinition);
		IMLangLexer lexer = new IMLangLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		IMLangParser parser = new IMLangParser(tokens);
		
		Concept concept = new Concept();
		concept.setIri(parser.iri().getText());
		concept.setName(parser.name().getText());
		concept.setDescription(parser.description().getText());
		concept.setCode(parser.code().getText());
		concept.setConceptType(ConceptType.byName(parser.type().getText()));
		concept.setStatus(ConceptStatus.byName(parser.status().getText()));
		
		return concept;
	}

}
