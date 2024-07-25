package org.endeavourhealth.imapi.filer;

/**
 * An Interface that handles the import of a variety of data sources such as Classifications and supplier look ups
 */
public interface TTImport extends AutoCloseable {
  void importData(TTImportConfig config) throws Exception;

  void validateFiles(String inFolder) throws TTFilerException;
}
