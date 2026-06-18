package org.endeavourhealth.imapi.filer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.library.model.imq.QueryException;
import org.endeavourhealth.library.model.tripletree.TTDocument;

public interface TTDocumentFiler extends AutoCloseable {
  void fileDocument(TTDocument document) throws TTFilerException, JsonProcessingException, QueryException;

  @Override
  void close();
}
