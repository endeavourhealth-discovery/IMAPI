package org.endeavourhealth.imapi.model.cdm;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

public abstract class Entry extends TTEntity {

	public TTIriRef getDataController() {

		return (TTIriRef) TTUtil.get(this,TTIriRef.iri("dataController"),TTIriRef.class);
	}

	public Entry setDataController(TTIriRef dataController) {
		set(TTIriRef.iri("dataController"),dataController);
		return this;
	}

	public String getDateOfEntry() {

		return (String) TTUtil.get(this,TTIriRef.iri(IM.NAMESPACE+"dateOfEntry"),String.class);
	}

	public Entry setDateOfEntry(String dateOfEntry) {
		set(TTIriRef.iri(IM.NAMESPACE+"dateOfEntry"), TTLiteral.literal("dateOfEntry"));
		return this;
	}
}
