package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.parser.ecl.ECLLexer;
import org.endeavourhealth.imapi.parser.ecl.ECLParser;
import org.endeavourhealth.imapi.parser.scg.SCGLexer;
import org.endeavourhealth.imapi.parser.scg.SCGParser;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.zip.DataFormatException;

public class SCGToTT {
	private TTEntity entity;
	private final SCGLexer lexer;
	private final SCGParser parser;
	private String ecl;

	public SCGToTT() {
		this.lexer = new SCGLexer(null);
		this.parser = new SCGParser(null);
		this.parser.removeErrorListeners();
		this.parser.addErrorListener(new ECLErrorListener());
	}
	public TTEntity getAxiom(TTEntity entity,String scg) throws DataFormatException {
		this.ecl = ecl;
		this.entity= entity;
		lexer.setInputStream(CharStreams.fromString(ecl));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		parser.setTokenStream(tokens);
		SCGParser.ExpressionContext ctx= parser.expression();
		return convertECContext(ctx);
	}

	private TTEntity convertECContext(SCGParser.ExpressionContext ctx) {
		if (ctx.definitionstatus()!=null) {
			if (ctx.definitionstatus().equivalentto() != null) {
				entity.set(IM.DEFINITIONAL_STATUS, IM.SUFFICIENTLY_DEFINED);
			}
		}

		return entity;

	}


}
