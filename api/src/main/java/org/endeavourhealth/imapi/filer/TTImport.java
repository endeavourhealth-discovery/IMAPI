package org.endeavourhealth.imapi.filer;

import org.endeavourhealth.imapi.model.imq.QueryException;

import java.io.IOException;

/**
 * An Interface that handles the import of a variety of data sources such as Classifications and supplier look ups
 */
public interface TTImport extends AutoCloseable {
  void importData(TTImportConfig config) throws IOException, TTFilerException, QueryException;

  void validateFiles(String inFolder) throws TTFilerException;
}
