package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents irirref
* A reference to resource with an iri and optional name to enable readability
*/
public class Irirref extends IMDMBase<Irirref> {


	/**
	* Irirref constructor 
	*/
	public Irirref() {
		super("Irirref");
	}

	/**
	* Irirref constructor with identifier
	*/
	public Irirref(String id) {
		super("Irirref", id);
	}

	/**
	* Gets the label of this irirref
	* The preferred full name of the entity
	* @return label
	*/
	public String getLabel() {
		return getProperty("label");
	}


	/**
	* Changes the label of this Irirref
	* @param label The new label to set
	* @return Irirref
	*/
	public Irirref setLabel(String label) {
		setProperty("label", label);
		return this;
	}


	/**
	* Gets the name of this irirref
	* The name of the entity
	* @return name
	*/
	public String getName() {
		return getProperty("name");
	}


	/**
	* Changes the name of this Irirref
	* @param name The new name to set
	* @return Irirref
	*/
	public Irirref setName(String name) {
		setProperty("name", name);
		return this;
	}


	/**
	* Gets the comment of this irirref
	* The description of the entity
	* @return comment
	*/
	public String getComment() {
		return getProperty("comment");
	}


	/**
	* Changes the comment of this Irirref
	* @param comment The new comment to set
	* @return Irirref
	*/
	public Irirref setComment(String comment) {
		setProperty("comment", comment);
		return this;
	}


	/**
	* Gets the type of this irirref
	* The base type of the entity i.e. one of the high level entity types
	* @return type
	*/
	public String getType() {
		return getProperty("type");
	}


	/**
	* Changes the type of this Irirref
	* @param type The new type to set
	* @return Irirref
	*/
	public Irirref setType(String type) {
		setProperty("type", type);
		return this;
	}


	/**
	* Gets the status of this irirref
	* The status of the entity being active, inactive, draft or unassigned
	* @return status
	*/
	public String getStatus() {
		return getProperty("status");
	}


	/**
	* Changes the status of this Irirref
	* @param status The new status to set
	* @return Irirref
	*/
	public Irirref setStatus(String status) {
		setProperty("status", status);
		return this;
	}


	/**
	* Gets the scheme of this irirref
	* The code scheme or graph which the entity belongs to
	* @return scheme
	*/
	public String getScheme() {
		return getProperty("scheme");
	}


	/**
	* Changes the scheme of this Irirref
	* @param scheme The new scheme to set
	* @return Irirref
	*/
	public Irirref setScheme(String scheme) {
		setProperty("scheme", scheme);
		return this;
	}


	/**
	* Gets the is contained in of this irirref
	* An entity that this entity inherits from, both semantically and structurally, this is also used for sub properties as properties are classes of properties
	* @return isContainedIn
	*/
	public Folder getIsContainedIn() {
		return getProperty("isContainedIn");
	}


	/**
	* Changes the is contained in of this Irirref
	* @param isContainedIn The new is contained in to set
	* @return Irirref
	*/
	public Irirref setIsContainedIn(Folder isContainedIn) {
		setProperty("isContainedIn", isContainedIn);
		return this;
	}


	/**
	* Gets the REPLACED BY (attribute) of this irirref
	* When a status is inactive, the entity that replaces this entity (if any)
	* @return replacedByAttribute
	*/
	public Entity getReplacedByAttribute() {
		return getProperty("replacedByAttribute");
	}


	/**
	* Changes the REPLACED BY (attribute) of this Irirref
	* @param replacedByAttribute The new REPLACED BY (attribute) to set
	* @return Irirref
	*/
	public Irirref setReplacedByAttribute(Entity replacedByAttribute) {
		setProperty("replacedByAttribute", replacedByAttribute);
		return this;
	}
}

