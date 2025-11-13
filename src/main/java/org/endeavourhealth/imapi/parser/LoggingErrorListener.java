package org.endeavourhealth.imapi.parser;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

@Slf4j
public class LoggingErrorListener extends BaseErrorListener {

  public static final LoggingErrorListener INSTANCE = new LoggingErrorListener();

  @Override
  public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) throws ParseCancellationException {
    log.error("line {}:{} {}", line, charPositionInLine, msg);
    throw new ParseCancellationException(msg, e);
  }
}