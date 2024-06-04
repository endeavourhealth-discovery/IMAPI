package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.parser.imecl.IMECLBaseVisitor;
import org.endeavourhealth.imapi.parser.imecl.IMECLLexer;
import org.endeavourhealth.imapi.parser.imecl.IMECLParser;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.UnknownFormatConversionException;
import java.util.zip.DataFormatException;

/**
 * Converts ECL to Discovery syntax, supporting commonly used constructs
 */
public class ECLToIMQ extends IMECLBaseVisitor<TTValue> {
	private final IMECLLexer lexer;
	private final IMECLParser parser;
	private String ecl;
	private Query query;
	public static final String ROLE_GROUP = IM.ROLE_GROUP;



	public ECLToIMQ() {
		this.lexer = new IMECLLexer(null);
		this.parser = new IMECLParser(null);
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
	public Query getQueryFromECL(String ecl) throws DataFormatException, EclFormatException {
		return getClassExpression(ecl,false);
	}

	/**
	 * Converts an ECL string into IM Query definition class.
	 * @param ecl String compliant with ECL
	 * @param activeOnly  boolean true if limited to active concepts
	 * @return Class conforming to IM Query model JSON-LD when serialized.
	 * @throws DataFormatException for invalid ECL.
	 */
	public Query getClassExpression(String ecl,boolean activeOnly) throws DataFormatException, EclFormatException {
		this.ecl = ecl;
		try {
			lexer.setInputStream(CharStreams.fromString(ecl));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			parser.setTokenStream(tokens);
			IMECLParser.ImeclContext eclCtx = parser.imecl();
			return query= new ECLToIMQVisitor().getIMQ(eclCtx,true);
		} catch (UnknownFormatConversionException error) {
			throw new EclFormatException("Ecl is invalid",error);
		}
	}

	public Boolean validateEcl(String ecl) {
		try {
			this.ecl = ecl;
			lexer.setInputStream(CharStreams.fromString(ecl));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			parser.setTokenStream(tokens);
			IMECLParser.ImeclContext eclCtx = parser.imecl();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
