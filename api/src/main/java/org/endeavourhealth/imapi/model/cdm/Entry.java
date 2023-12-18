package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

public abstract class Entry extends TTEntity {

	public TTIriRef getDataController() {

		return (TTIriRef) TTUtil.get(this,TTIriRef.iri("dataController"),TTIriRef.class);
	}

	@JsonSetter
	public Entry setDataController(TTIriRef dataController) {
		set(TTIriRef.iri("dataController"),dataController);
		return this;
	}
	public Entry setDataController(Vocabulary dataController) {
		return setDataController(dataController.asTTIriRef());
	}

	public String getDateOfEntry() {

		return (String) TTUtil.get(this,IM.DATE_OF_ENTRY,String.class);
	}

	public Entry setDateOfEntry(String dateOfEntry) {
		set(IM.DATE_OF_ENTRY, TTLiteral.literal("dateOfEntry"));
		return this;
	}
}
