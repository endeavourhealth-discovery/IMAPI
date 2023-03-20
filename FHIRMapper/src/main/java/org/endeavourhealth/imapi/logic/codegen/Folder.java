package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents folder.
* Folder containing subfolders or entities
*/
public class Folder extends IMDMBase<Folder> {


	/**
	* Folder constructor with identifier
	*/
	public Folder(UUID id) {
		super("Folder", id);
	}

	/**
	* Gets the label of this folder
	* The preferred full name of the entity
	* @return label
	*/
	public String getLabel() {
		return getProperty("label");
	}


	/**
	* Changes the label of this Folder
	* @param label The new label to set
	* @return Folder
	*/
	public Folder setLabel(String label) {
		setProperty("label", label);
		return this;
	}


	/**
	* Gets the comment of this folder
	* The description of the entity
	* @return comment
	*/
	public String getComment() {
		return getProperty("comment");
	}


	/**
	* Changes the comment of this Folder
	* @param comment The new comment to set
	* @return Folder
	*/
	public Folder setComment(String comment) {
		setProperty("comment", comment);
		return this;
	}


	/**
	* Gets the type of this folder
	* The base type of the entity i.e. one of the high level entity types
	* @return type
	*/
	public String getType() {
		return getProperty("type");
	}


	/**
	* Changes the type of this Folder
	* @param type The new type to set
	* @return Folder
	*/
	public Folder setType(String type) {
		setProperty("type", type);
		return this;
	}


	/**
	* Gets the status of this folder
	* The status of the entity being active, inactive, draft or unassigned
	* @return status
	*/
	public String getStatus() {
		return getProperty("status");
	}


	/**
	* Changes the status of this Folder
	* @param status The new status to set
	* @return Folder
	*/
	public Folder setStatus(String status) {
		setProperty("status", status);
		return this;
	}


	/**
	* Gets the scheme of this folder
	* The code scheme or graph which the entity belongs to
	* @return scheme
	*/
	public String getScheme() {
		return getProperty("scheme");
	}


	/**
	* Changes the scheme of this Folder
	* @param scheme The new scheme to set
	* @return Folder
	*/
	public Folder setScheme(String scheme) {
		setProperty("scheme", scheme);
		return this;
	}


	/**
	* Gets the is contained in of this folder
	* An entity that this entity inherits from, both semantically and structurally, this is also used for sub properties as properties are classes of properties
	* @return isContainedIn
	*/
	public UUID getIsContainedIn() {
		return getProperty("isContainedIn");
	}


	/**
	* Changes the is contained in of this Folder
	* @param isContainedIn The new is contained in to set
	* @return Folder
	*/
	public Folder setIsContainedIn(UUID isContainedIn) {
		setProperty("isContainedIn", isContainedIn);
		return this;
	}


	/**
	* Gets the REPLACED BY (attribute) of this folder
	* When a status is inactive, the entity that replaces this entity (if any)
	* @return replacedByAttribute
	*/
	public UUID getReplacedByAttribute() {
		return getProperty("replacedByAttribute");
	}


	/**
	* Changes the REPLACED BY (attribute) of this Folder
	* @param replacedByAttribute The new REPLACED BY (attribute) to set
	* @return Folder
	*/
	public Folder setReplacedByAttribute(UUID replacedByAttribute) {
		setProperty("replacedByAttribute", replacedByAttribute);
		return this;
	}
}

