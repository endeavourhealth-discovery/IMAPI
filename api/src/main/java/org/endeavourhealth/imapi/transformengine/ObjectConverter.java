package org.endeavourhealth.imapi.transformengine;

import java.util.zip.DataFormatException;

public interface ObjectConverter {
	Object convert(Object from) throws DataFormatException;
}
