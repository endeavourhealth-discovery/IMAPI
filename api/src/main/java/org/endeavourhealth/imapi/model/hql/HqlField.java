package org.endeavourhealth.imapi.model.hql;

import org.apache.poi.ss.formula.functions.T;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

public class HqlField<T extends String & HqlInteger> {
	TTIriRef sourceField;
	String fieldAlias;
	boolean includeChildFields;
}
