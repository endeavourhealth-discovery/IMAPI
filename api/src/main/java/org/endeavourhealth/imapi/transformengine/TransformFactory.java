package org.endeavourhealth.imapi.transformengine;

import java.util.zip.DataFormatException;

public class TransformFactory {

	public static ObjectFiler createFiler(String targetFormat) throws DataFormatException {
		if (targetFormat.equalsIgnoreCase("JSON-LD"))
			return new TripleTreeFiler();
		else
			throw new DataFormatException("Unknown target format : "+ targetFormat);
	}

	public static ObjectReader createReader(String sourceFormat) throws DataFormatException {
		if (sourceFormat.equalsIgnoreCase("JSON")){
			return new JsonReader();
		}
		else
			throw new DataFormatException("Unknown source format : "+ sourceFormat);

	}
}
