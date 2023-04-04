package org.endeavourhealth.imapi.model.imq;

import java.util.List;

public interface Whereable {
	Whereable setBool(Bool bool);
	Bool getBool();
	List<Where> getWhere();
	Whereable setWhere(List<Where> where);
	Whereable addWhere(Where where);


}
