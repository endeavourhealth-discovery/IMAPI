package org.endeavourhealth.imapi.cdm;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;

public abstract class Entry extends TTEntity {

	public TTIriRef getDataController() {
		return Utils.getIriValue(this,"dataController");
	}

	public Entry setDataController(TTIriRef dataController) {
		Utils.setTriple(this,"dataController",dataController);
		return this;
	}

	public String getDateOfEntry() {
		return Utils.getStringValue(this,"dateOfEntry");
	}

	public Entry setDateOfEntry(String dateOfEntry) {
		Utils.setTriple(this,"dateOfEntry", TTLiteral.literal("dateOfEntry"));
		return this;
	}
}
