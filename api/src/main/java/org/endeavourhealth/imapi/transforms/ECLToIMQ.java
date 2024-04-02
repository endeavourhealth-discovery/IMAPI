package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.parser.ecl.ECLBaseVisitor;
import org.endeavourhealth.imapi.parser.ecl.ECLLexer;
import org.endeavourhealth.imapi.parser.ecl.ECLParser;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.util.UnknownFormatConversionException;
import java.util.zip.DataFormatException;

/**
 * Converts ECL to Discovery syntax, supporting commonly used constructs
 */
public class ECLToIMQ extends ECLBaseVisitor<TTValue> {
	private final ECLLexer lexer;
	private final ECLParser parser;
	private String ecl;
	private Query query;
	public static final String ROLE_GROUP = IM.ROLE_GROUP;



	public ECLToIMQ() {
		this.lexer = new ECLLexer(null);
		this.parser = new ECLParser(null);
		this.parser.removeErrorListeners();
		this.parser.addErrorListener(new ParserErrorListener());
		this.query= new Query();
	}

	/**
	 * Converts an ECL string into IM Query definition class. Assumes active and inactive concepts are requested.
	 * <p>To include only active concepts use method with boolean activeOnly= true</p>
	 * @param ecl String compliant with ECL
	 * @return Class conforming to IM Query model JSON-LD when serialized.
	 * @throws DataFormatException for invalid ECL.
	 */
	public Query getQueryFromECL(String ecl) throws DataFormatException {
		return getClassExpression(ecl,false);
	}

	/**
	 * Converts an ECL string into IM Query definition class.
	 * @param ecl String compliant with ECL
	 * @param activeOnly  boolean true if limited to active concepts
	 * @return Class conforming to IM Query model JSON-LD when serialized.
	 * @throws DataFormatException for invalid ECL.
	 */
	public Query getClassExpression(String ecl,boolean activeOnly) throws DataFormatException {
		this.ecl = ecl;
		lexer.setInputStream(CharStreams.fromString(ecl));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		parser.setTokenStream(tokens);
		ECLParser.EclContext eclCtx = parser.ecl();

		Query query= new ECLToIMQVisitor().getIMQ(eclCtx,true);
		return query;
	}
}
