package org.endeavourhealth.imapi.filer;

import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;

public interface TTDocumentFiler extends AutoCloseable{
	void fileDocument(TTDocument document) throws TTFilerException;
	void writeLog(TTDocument document) throws Exception;
	void fileDeltas(String deltaPath) throws Exception;
}
