package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.IntervalSet;

import java.util.List;
import java.util.UnknownFormatConversionException;


public class ECLErrorListener extends BaseErrorListener{

   @Override
public void syntaxError(Recognizer<?,?> recognizer,
                        Object offendingSymbol, int line, int charPositionInLine,
                        String msg, RecognitionException e) {
   if (recognizer instanceof Lexer) {
      throw new UnknownFormatConversionException(msg + " line " + line + " offset " + charPositionInLine);
   } else {
      Parser parser = (Parser) recognizer;
      IntervalSet expectedTokens = parser.getExpectedTokens();
      String badSymbol="";
      if (offendingSymbol instanceof CommonToken)
         badSymbol= ((CommonToken) offendingSymbol).getText().split(",")[0];
      String message = "Expecting " + expectedTokens.toString(parser.getVocabulary());
      throw new UnknownFormatConversionException("ECL Syntax error :  line "
          + " offset " + charPositionInLine + " i.e '"+badSymbol+"'  " + message);
   }
}
}

