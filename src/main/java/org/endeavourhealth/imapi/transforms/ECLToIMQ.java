package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.logic.service.ECLQueryValidator;
import org.endeavourhealth.imapi.model.imq.ECLQueryRequest;
import org.endeavourhealth.imapi.model.imq.ECLStatus;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.ValidationLevel;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.parser.imecl.IMECLBaseVisitor;
import org.endeavourhealth.imapi.parser.imecl.IMECLLexer;
import org.endeavourhealth.imapi.parser.imecl.IMECLParser;
import org.endeavourhealth.imapi.vocabulary.IM;

/**
 * Converts ECL to Discovery syntax, supporting commonly used constructs
 */
public class ECLToIMQ extends IMECLBaseVisitor<TTValue> {
  public static final String ROLE_GROUP = IM.ROLE_GROUP;
  private final IMECLLexer lexer;
  private final IMECLParser parser;
  private boolean validateEntities;


  public ECLToIMQ() {
    this.lexer = new IMECLLexer(null);
    this.parser = new IMECLParser(null);
    this.parser.removeErrorListeners();
    this.parser.addErrorListener(new ParserErrorListener());
  }

  public void getQueryFromECL(ECLQueryRequest eclQueryRequest, boolean validateEntities) {
    this.validateEntities = validateEntities;
    getQueryFromECL(eclQueryRequest);
  }

  /**
   * Converts an ECL string into IM Query definition class. Assumes active and inactive concepts are requested.
   * <p>To include only active concepts use method with boolean activeOnly= true</p>
   *
   * @param eclQueryRequest An object containing an 'ecl' property which is an ecl string
   * @return the object with 'query' and 'status' and ecl  conforming to IM Query model JSON-LD when serialized.
   */
  public void getQueryFromECL(ECLQueryRequest eclQueryRequest) {
    eclQueryRequest.setStatus(new ECLStatus());
    eclQueryRequest.getStatus().setValid(true);
    eclQueryRequest.getStatus().setLine(null);
    eclQueryRequest.getStatus().setOffset(null);
    try {
      lexer.setInputStream(CharStreams.fromString(eclQueryRequest.getEcl()));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      parser.setTokenStream(tokens);
      IMECLParser.ImeclContext eclCtx = parser.imecl();
      ECLToIMQVisitor visitor = new ECLToIMQVisitor();
      Query query = visitor.getIMQ(eclCtx);
      eclQueryRequest.setQuery(query);
      if (validateEntities) {
        ECLQueryValidator validator = new ECLQueryValidator();
        ECLStatus status = validator.validateQuery(query, ValidationLevel.CONCEPT);
        if (!status.isValid()) {
          try {
            new IMQToECL().getECLFromQuery(eclQueryRequest);
          } catch (Exception e) {
            eclQueryRequest.getStatus().setValid(false);
            eclQueryRequest.getStatus().setMessage(e.getMessage());
          }
        }
      }
    } catch (ECLSyntaxError error) {
      eclQueryRequest.setStatus(error.getErrorData());
      Query query = new Query();
      query.setInvalid(true);
      eclQueryRequest.setQuery(query);
    }
  }
}


