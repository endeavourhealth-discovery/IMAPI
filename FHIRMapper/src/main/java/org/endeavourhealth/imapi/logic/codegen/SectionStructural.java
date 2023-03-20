package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents section structural.
* Section of a document or encounter with a heading
*/
public class SectionStructural extends IMDMBase<SectionStructural> {


	/**
	* Section structural constructor with identifier
	*/
	public SectionStructural(UUID id) {
		super("SectionStructural", id);
	}

	/**
	* Gets the is subsection of of this section structural
	* @return isSubsectionOf
	*/
	public UUID getIsSubsectionOf() {
		return getProperty("isSubsectionOf");
	}


	/**
	* Changes the is subsection of of this SectionStructural
	* @param isSubsectionOf The new is subsection of to set
	* @return SectionStructural
	*/
	public SectionStructural setIsSubsectionOf(UUID isSubsectionOf) {
		setProperty("isSubsectionOf", isSubsectionOf);
		return this;
	}


	/**
	* Gets the has heading of this section structural
	* @return hasHeading
	*/
	public String getHasHeading() {
		return getProperty("hasHeading");
	}


	/**
	* Changes the has heading of this SectionStructural
	* @param hasHeading The new has heading to set
	* @return SectionStructural
	*/
	public SectionStructural setHasHeading(String hasHeading) {
		setProperty("hasHeading", hasHeading);
		return this;
	}
}

