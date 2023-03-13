package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents term code shape
* data model for a term code
*/
public class TermCodeShape extends IMDMBase<TermCodeShape> {


	/**
	* Term code shape constructor 
	*/
	public TermCodeShape() {
		super("TermCodeShape");
	}

	/**
	* Term code shape constructor with identifier
	*/
	public TermCodeShape(String id) {
		super("TermCodeShape", id);
	}

	/**
	* Gets the comment of this term code shape
	* The description of the entity
	* @return comment
	*/
	public String getComment() {
		return getProperty("comment");
	}


	/**
	* Changes the comment of this TermCodeShape
	* @param comment The new comment to set
	* @return TermCodeShape
	*/
	public TermCodeShape setComment(String comment) {
		setProperty("comment", comment);
		return this;
	}


	/**
	* Gets the label of this term code shape
	* The term or synonym for the concept
	* @return label
	*/
	public String getLabel() {
		return getProperty("label");
	}


	/**
	* Changes the label of this TermCodeShape
	* @param label The new label to set
	* @return TermCodeShape
	*/
	public TermCodeShape setLabel(String label) {
		setProperty("label", label);
		return this;
	}


	/**
	* Gets the type of this term code shape
	* The base type of the entity i.e. one of the high level entity types
	* @return type
	*/
	public String getType() {
		return getProperty("type");
	}


	/**
	* Changes the type of this TermCodeShape
	* @param type The new type to set
	* @return TermCodeShape
	*/
	public TermCodeShape setType(String type) {
		setProperty("type", type);
		return this;
	}


	/**
	* Gets the code of this term code shape
	* Descriptionid or code for this particular term
	* @return code
	*/
	public String getCode() {
		return getProperty("code");
	}


	/**
	* Changes the code of this TermCodeShape
	* @param code The new code to set
	* @return TermCodeShape
	*/
	public TermCodeShape setCode(String code) {
		setProperty("code", code);
		return this;
	}


	/**
	* Gets the scheme of this term code shape
	* The code scheme or graph which the entity belongs to
	* @return scheme
	*/
	public String getScheme() {
		return getProperty("scheme");
	}


	/**
	* Changes the scheme of this TermCodeShape
	* @param scheme The new scheme to set
	* @return TermCodeShape
	*/
	public TermCodeShape setScheme(String scheme) {
		setProperty("scheme", scheme);
		return this;
	}


	/**
	* Gets the status of this term code shape
	* The status of this particular term code, may be active or inactive
	* @return status
	*/
	public String getStatus() {
		return getProperty("status");
	}


	/**
	* Changes the status of this TermCodeShape
	* @param status The new status to set
	* @return TermCodeShape
	*/
	public TermCodeShape setStatus(String status) {
		setProperty("status", status);
		return this;
	}


	/**
	* Gets the is contained in of this term code shape
	* An entity that this entity inherits from, both semantically and structurally, this is also used for sub properties as properties are classes of properties
	* @return isContainedIn
	*/
	public Folder getIsContainedIn() {
		return getProperty("isContainedIn");
	}


	/**
	* Changes the is contained in of this TermCodeShape
	* @param isContainedIn The new is contained in to set
	* @return TermCodeShape
	*/
	public TermCodeShape setIsContainedIn(Folder isContainedIn) {
		setProperty("isContainedIn", isContainedIn);
		return this;
	}


	/**
	* Gets the REPLACED BY (attribute) of this term code shape
	* When a status is inactive, the entity that replaces this entity (if any)
	* @return replacedByAttribute
	*/
	public Entity getReplacedByAttribute() {
		return getProperty("replacedByAttribute");
	}


	/**
	* Changes the REPLACED BY (attribute) of this TermCodeShape
	* @param replacedByAttribute The new REPLACED BY (attribute) to set
	* @return TermCodeShape
	*/
	public TermCodeShape setReplacedByAttribute(Entity replacedByAttribute) {
		setProperty("replacedByAttribute", replacedByAttribute);
		return this;
	}
}

