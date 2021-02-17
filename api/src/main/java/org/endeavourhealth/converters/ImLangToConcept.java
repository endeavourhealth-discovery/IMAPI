package org.endeavourhealth.converters;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.ClassExpression;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptStatus;
import org.endeavourhealth.imapi.model.ConceptType;
import org.endeavourhealth.imapi.model.PropertyConstraint;
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

		// SubclassOf
		if (parser.subclassOf().getText() != null) {
			String[] subclasses = parser.subclassOf().getText().split(",");
			for (String subclass : subclasses) {
				ClassExpression classExpression = new ClassExpression();
				classExpression.setClazz(subclass);
				concept.addSubClassOf(classExpression);
			}
		}

		// Members
		if (parser.members().getText() != null) {
			String[] members = parser.members().getText().split(",");
			for (String member : members) {
				ClassExpression classExpression = new ClassExpression();
				classExpression.setClazz(member);
				concept.addMember(classExpression);
			}
		}

		// EquivalentTo
		if (parser.equivalentTo().getText() != null) {
			String[] equivalentTo = parser.equivalentTo().getText().split(",");
			for (String equivalent : equivalentTo) {
				ClassExpression classExpression = new ClassExpression();
				classExpression.setClazz(equivalent);
				concept.addEquivalentTo(classExpression);
			}
		}

		return concept;
	}

}
