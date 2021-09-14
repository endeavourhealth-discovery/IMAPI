package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.parser.turtle.TurtliteBaseVisitor;
import org.endeavourhealth.imapi.parser.turtle.TurtliteLexer;
import org.endeavourhealth.imapi.parser.turtle.TurtliteParser;


public class TurtleToTT extends TurtliteBaseVisitor<TTDocument> {
	private TTEntity entity;
	private TTDocument document;
	private TTManager manager;
	private final TurtliteParser parser;
	private final TurtliteLexer lexer;
	private String turtle;


	public TurtleToTT() {
		this.lexer = new TurtliteLexer(null);
		this.parser = new TurtliteParser(null);
		this.parser.removeErrorListeners();
		this.parser.addErrorListener(new ECLErrorListener());
	}

	public TTDocument getDocument(String turtle, TTIriRef graph) {
		this.turtle = turtle;
		lexer.setInputStream(CharStreams.fromString(turtle));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		parser.setTokenStream(tokens);
		TurtliteParser.TurtleDocContext tdoc= parser.turtleDoc();
		manager= new TTManager();
		document= manager.createDocument(graph.getIri());
		visitTurtleDoc(tdoc);

		return document;
	}
	@Override
	public TTDocument visitTurtleDoc(TurtliteParser.TurtleDocContext tdoc){
		return document;
	}
}
