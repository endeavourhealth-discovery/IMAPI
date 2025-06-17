package org.endeavourhealth.imapi.transforms;

import lombok.Getter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.logic.service.ECLQueryValidator;
import org.endeavourhealth.imapi.logic.service.QueryDescriptor;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.imq.ECLQuery;
import org.endeavourhealth.imapi.model.imq.ECLStatus;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.ValidationLevel;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.parser.imecl.IMECLBaseVisitor;
import org.endeavourhealth.imapi.parser.imecl.IMECLLexer;
import org.endeavourhealth.imapi.parser.imecl.IMECLParser;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.HashSet;
import java.util.Set;

/**
 * Converts ECL to Discovery syntax, supporting commonly used constructs
 */
public class ECLToIMQ extends IMECLBaseVisitor<TTValue> {
  public static final String ROLE_GROUP = IM.ROLE_GROUP;
  private final IMECLLexer lexer;
  private final IMECLParser parser;


  public ECLToIMQ() {
    this.lexer = new IMECLLexer(null);
    this.parser = new IMECLParser(null);
    this.parser.removeErrorListeners();
    this.parser.addErrorListener(new ParserErrorListener());
  }

  /**
   * Converts an ECL string into IM Query definition class. Assumes active and inactive concepts are requested.
   * <p>To include only active concepts use method with boolean activeOnly= true</p>
   *
   * @param eclQuery An object containing an 'ecl' property which is an ecl string
   * @return the object with 'query' and 'status' and ecl  conforming to IM Query model JSON-LD when serialized.
   */
  public void getQueryFromECL(ECLQuery eclQuery){
    eclQuery.setStatus(new ECLStatus());
    eclQuery.getStatus().setValid(true);
    eclQuery.getStatus().setLine(null);
    eclQuery.getStatus().setOffset(null);
    try {
      lexer.setInputStream(CharStreams.fromString(eclQuery.getEcl()));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      parser.setTokenStream(tokens);
      IMECLParser.ImeclContext eclCtx = parser.imecl();
      ECLToIMQVisitor visitor = new ECLToIMQVisitor();
      Query query = visitor.getIMQ(eclCtx);
      eclQuery.setQuery(query);
      ECLQueryValidator validator = new ECLQueryValidator();
      ECLStatus status= validator.validateQuery(query, ValidationLevel.CONCEPT);
      if (!status.isValid()) {
        try {
          new IMQToECL().getECLFromQuery(eclQuery);
        } catch (Exception e){
          eclQuery.getStatus().setValid(false);
          eclQuery.getStatus().setMessage(e.getMessage());
        }
      }
    } catch (ECLSyntaxError error) {
      eclQuery.setStatus(error.getErrorData());
      Query query= new Query();
      query.setInvalid(true);
      eclQuery.setQuery(query);
    }
  }
}


