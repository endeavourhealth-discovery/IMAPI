package org.endeavourhealth.imapi.filer;

import java.io.IOException;
import java.sql.SQLException;

public interface TCGenerator {
	void generateClosure(String outpath, boolean secure) throws Exception;
}
