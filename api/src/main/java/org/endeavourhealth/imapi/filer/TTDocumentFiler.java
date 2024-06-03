package org.endeavourhealth.imapi.filer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;

public interface TTDocumentFiler extends AutoCloseable{
	void fileDocument(TTDocument document) throws TTFilerException, JsonProcessingException, QueryException;
	void writeLog(TTDocument document) throws Exception;
	void fileDeltas(String deltaPath) throws Exception;
}
