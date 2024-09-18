package org.endeavourhealth.imapi.filer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;

import java.io.IOException;
import java.util.Map;

public interface TTDocumentFiler extends AutoCloseable {
  void fileDocument(TTDocument document) throws TTFilerException, JsonProcessingException, QueryException;

  void fileDocument(TTDocument document, String taskId, Map<String, Integer> progressMap) throws TTFilerException, JsonProcessingException, QueryException;

  void writeLog(TTDocument document) throws JsonProcessingException, TTFilerException;

  void fileDeltas(String deltaPath) throws IOException, QueryException, TTFilerException;

  @Override
  void close();
}
