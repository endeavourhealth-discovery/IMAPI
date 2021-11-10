package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;

public interface XMLToTTListener {
	void startElement(String path, TTEntity entity, TTDocument document);
	void endElement(String path, TTEntity entity, TTDocument document);
}
