package org.endeavourhealth.imapi.model.imq;

import java.util.List;

public interface Whereable {
	Whereable setBoolWhere(Bool boolMatch);
	Bool getBoolWhere();
	List<Where> getWhere();
	Whereable setWhere(List<Where> where);
	Whereable addWhere(Where where);


}
