package org.endeavourhealth.imapi.transformengine;

import java.util.zip.DataFormatException;

public class TransformFactory {


	public static SyntaxTranslator createConverter(String format) throws DataFormatException {
		if (format.equalsIgnoreCase("JSON"))
			return new JsonTranslator();
		else	if (format.equalsIgnoreCase("JSON-LD"))
				return new TTTranslator();
		else
		throw new DataFormatException("Source format : "+ format+ " syntax converter Not configured");
		}

}
