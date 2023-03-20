package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents record entry.
* A abstract class or something whose subject is an entity
*/
public class RecordEntry extends IMDMBase<RecordEntry> {


	/**
	* Record entry constructor with identifier
	*/
	public RecordEntry(UUID id) {
		super("RecordEntry", id);
	}

	/**
	* Gets the record owner of this record entry
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this RecordEntry
	* @param recordOwner The new record owner to set
	* @return RecordEntry
	*/
	public RecordEntry setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}
}

