package org.endeavourhealth.converters;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.informationmanager.parser.IMLangLexer;
import org.endeavourhealth.informationmanager.parser.IMLangParser;
import org.springframework.stereotype.Component;

@Component
public class ImLangToConcept {

	public Concept translateDefinitionToConcept(String conceptDefinition) {

		System.out.println(conceptDefinition);

		IMLangLexer lexer = new IMLangLexer(CharStreams.fromString(conceptDefinition));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        IMLangParser parser = new IMLangParser(tokens);
        
        parser.setBuildParseTree(true);
        
        ParseTree tree = parser.concept(); // parse the content and get the tree
        IMLangListener listener = new IMLangListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener,tree);

//		// SubclassOf
//		if (!parser.subclassOf().getText().isEmpty()) {
//			String[] subclasses = parser.subclassOf().getText().split(",");
//			for (String subclass : subclasses) {
//				ClassExpression classExpression = new ClassExpression();
//				classExpression.setClazz(subclass);
//				concept.addSubClassOf(classExpression);
//			}
//		}
//
//		// Members
//		if (!parser.members().getText().isEmpty()) {
//			String[] members = parser.members().getText().split(",");
//			for (String member : members) {
//				ClassExpression classExpression = new ClassExpression();
//				classExpression.setClazz(member);
//				concept.addMember(classExpression);
//			}
//		}
//
//		// EquivalentTo
//		if (!parser.equivalentTo().getText().isEmpty()) {
//			String[] equivalentTo = parser.equivalentTo().getText().split(",");
//			for (String equivalent : equivalentTo) {
//				ClassExpression classExpression = new ClassExpression();
//				classExpression.setClazz(equivalent);
//				concept.addEquivalentTo(classExpression);
//			}
//		}

		return listener.getConcept();
	}

}
