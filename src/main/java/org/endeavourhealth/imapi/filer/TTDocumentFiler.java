package org.endeavourhealth.imapi.filer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.Graph;

import java.util.List;

public interface TTDocumentFiler extends AutoCloseable {
  void fileDocument(TTDocument document, List<Graph> graphs) throws TTFilerException, JsonProcessingException, QueryException;

  @Override
  void close();
}
