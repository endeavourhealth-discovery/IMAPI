package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.endeavourhealth.imapi.model.imq.ECLStatus;


public class ParserErrorListener extends BaseErrorListener {

  @Override
  public void syntaxError(Recognizer<?, ?> recognizer,
                          Object offendingSymbol, int line, int charPositionInLine,
                          String msg, RecognitionException e) throws ECLSyntaxError{
    ECLStatus errorData= new ECLStatus();
    errorData.setValid(false);
    errorData.setLine(line);
    errorData.setOffset(charPositionInLine);
    errorData.setMessage(msg);
    if (recognizer instanceof Lexer) {
      throw new ECLSyntaxError("Invalid ECL"
        , errorData);

    } else {
      Parser parser = (Parser) recognizer;
      IntervalSet expectedTokens = parser.getExpectedTokens();
      String badSymbol = "";
      if (offendingSymbol instanceof CommonToken) {
        String[] symbols = ((CommonToken) offendingSymbol).getText().split(",");
        if (symbols.length > 0)
          badSymbol = symbols[0];
      }
      String message = "Expecting " + expectedTokens.toString(parser.getVocabulary());
      errorData.setMessage("Symbol '"+badSymbol + "' " + message);
      throw new ECLSyntaxError("Invalid ECL"
        , errorData);

    }
  }
}

