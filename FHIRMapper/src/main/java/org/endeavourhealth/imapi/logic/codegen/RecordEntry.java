package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents record entry
* A abstract class or something whose subject is an entity
*/
public class RecordEntry extends IMDMBase<RecordEntry> {


	/**
	* Record entry constructor 
	*/
	public RecordEntry() {
		super("RecordEntry");
	}

	/**
	* Record entry constructor with identifier
	*/
	public RecordEntry(String id) {
		super("RecordEntry", id);
	}

	/**
	* Gets the record owner of this record entry
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this RecordEntry
	* @param recordOwner The new record owner to set
	* @return RecordEntry
	*/
	public RecordEntry setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}
}

