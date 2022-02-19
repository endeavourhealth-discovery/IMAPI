package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.IntervalSet;

import java.util.List;
import java.util.UnknownFormatConversionException;


public class ECLErrorListener extends BaseErrorListener{
   private String partialToken;
   private List<String> errorMessages;
   private String errorOffset;


   @Override
public void syntaxError(Recognizer<?,?> recognizer,
                        Object offendingSymbol, int line, int charPositionInLine,
                        String msg, RecognitionException e) {
   CommonToken badToken;
   if (offendingSymbol instanceof CommonToken)
      badToken = (CommonToken) offendingSymbol;
   if (recognizer instanceof Lexer) {
      throw new UnknownFormatConversionException(msg + " line " + line + " offset " + charPositionInLine);
   } else {
      Parser parser = (Parser) recognizer;
      IntervalSet expectedTokens = parser.getExpectedTokens();
      String message = "Expecting " + expectedTokens.toString(parser.getVocabulary());
      throw new UnknownFormatConversionException("ECL Syntax error : " + message + " line " + line
          + " offset " + charPositionInLine);
   }
}
}

