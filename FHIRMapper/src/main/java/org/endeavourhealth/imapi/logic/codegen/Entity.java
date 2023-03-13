package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents entity
* The abstract super class and data model for all named entities in the model i.e. the data model of the entities that are labelled with iri label and status
*/
public class Entity extends IMDMBase<Entity> {


	/**
	* Entity constructor 
	*/
	public Entity() {
		super("Entity");
	}

	/**
	* Entity constructor with identifier
	*/
	public Entity(String id) {
		super("Entity", id);
	}

	/**
	* Gets the label of this entity
	* The preferred full name of the entity
	* @return label
	*/
	public String getLabel() {
		return getProperty("label");
	}


	/**
	* Changes the label of this Entity
	* @param label The new label to set
	* @return Entity
	*/
	public Entity setLabel(String label) {
		setProperty("label", label);
		return this;
	}


	/**
	* Gets the type of this entity
	* The base type of the entity i.e. one of the high level entity types
	* @return type
	*/
	public String getType() {
		return getProperty("type");
	}


	/**
	* Changes the type of this Entity
	* @param type The new type to set
	* @return Entity
	*/
	public Entity setType(String type) {
		setProperty("type", type);
		return this;
	}


	/**
	* Gets the comment of this entity
	* The description of the entity
	* @return comment
	*/
	public String getComment() {
		return getProperty("comment");
	}


	/**
	* Changes the comment of this Entity
	* @param comment The new comment to set
	* @return Entity
	*/
	public Entity setComment(String comment) {
		setProperty("comment", comment);
		return this;
	}


	/**
	* Gets the status of this entity
	* The status of the entity being active, inactive, draft or unassigned
	* @return status
	*/
	public String getStatus() {
		return getProperty("status");
	}


	/**
	* Changes the status of this Entity
	* @param status The new status to set
	* @return Entity
	*/
	public Entity setStatus(String status) {
		setProperty("status", status);
		return this;
	}


	/**
	* Gets the scheme of this entity
	* The code scheme or graph which the entity belongs to
	* @return scheme
	*/
	public String getScheme() {
		return getProperty("scheme");
	}


	/**
	* Changes the scheme of this Entity
	* @param scheme The new scheme to set
	* @return Entity
	*/
	public Entity setScheme(String scheme) {
		setProperty("scheme", scheme);
		return this;
	}


	/**
	* Gets the is contained in of this entity
	* An entity that this entity inherits from, both semantically and structurally, this is also used for sub properties as properties are classes of properties
	* @return isContainedIn
	*/
	public Folder getIsContainedIn() {
		return getProperty("isContainedIn");
	}


	/**
	* Changes the is contained in of this Entity
	* @param isContainedIn The new is contained in to set
	* @return Entity
	*/
	public Entity setIsContainedIn(Folder isContainedIn) {
		setProperty("isContainedIn", isContainedIn);
		return this;
	}


	/**
	* Gets the REPLACED BY (attribute) of this entity
	* When a status is inactive, the entity that replaces this entity (if any)
	* @return replacedByAttribute
	*/
	public Entity getReplacedByAttribute() {
		return getProperty("replacedByAttribute");
	}


	/**
	* Changes the REPLACED BY (attribute) of this Entity
	* @param replacedByAttribute The new REPLACED BY (attribute) to set
	* @return Entity
	*/
	public Entity setReplacedByAttribute(Entity replacedByAttribute) {
		setProperty("replacedByAttribute", replacedByAttribute);
		return this;
	}
}

