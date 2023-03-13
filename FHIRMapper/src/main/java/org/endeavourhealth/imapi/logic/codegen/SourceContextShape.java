package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents source context shape
* Data model for source context for a concept e.g. source system, field etc
*/
public class SourceContextShape extends IMDMBase<SourceContextShape> {


	/**
	* Source context shape constructor 
	*/
	public SourceContextShape() {
		super("SourceContextShape");
	}

	/**
	* Source context shape constructor with identifier
	*/
	public SourceContextShape(String id) {
		super("SourceContextShape", id);
	}

	/**
	* Gets the label of this source context shape
	* The preferred full name of the entity
	* @return label
	*/
	public String getLabel() {
		return getProperty("label");
	}


	/**
	* Changes the label of this SourceContextShape
	* @param label The new label to set
	* @return SourceContextShape
	*/
	public SourceContextShape setLabel(String label) {
		setProperty("label", label);
		return this;
	}


	/**
	* Gets the source system of this source context shape
	* the name of the system that was responsible for sending the message or extract, usually referring to the health care record system
	* @return sourceSystem
	*/
	public ComputerSystem getSourceSystem() {
		return getProperty("sourceSystem");
	}


	/**
	* Changes the source system of this SourceContextShape
	* @param sourceSystem The new source system to set
	* @return SourceContextShape
	*/
	public SourceContextShape setSourceSystem(ComputerSystem sourceSystem) {
		setProperty("sourceSystem", sourceSystem);
		return this;
	}


	/**
	* Gets the comment of this source context shape
	* The description of the entity
	* @return comment
	*/
	public String getComment() {
		return getProperty("comment");
	}


	/**
	* Changes the comment of this SourceContextShape
	* @param comment The new comment to set
	* @return SourceContextShape
	*/
	public SourceContextShape setComment(String comment) {
		setProperty("comment", comment);
		return this;
	}


	/**
	* Gets the source publisher of this source context shape
	* The organisation (usually a provider) that was the source of this message or extract
	* @return sourcePublisher
	*/
	public Organisation getSourcePublisher() {
		return getProperty("sourcePublisher");
	}


	/**
	* Changes the source publisher of this SourceContextShape
	* @param sourcePublisher The new source publisher to set
	* @return SourceContextShape
	*/
	public SourceContextShape setSourcePublisher(Organisation sourcePublisher) {
		setProperty("sourcePublisher", sourcePublisher);
		return this;
	}


	/**
	* Gets the type of this source context shape
	* The base type of the entity i.e. one of the high level entity types
	* @return type
	*/
	public String getType() {
		return getProperty("type");
	}


	/**
	* Changes the type of this SourceContextShape
	* @param type The new type to set
	* @return SourceContextShape
	*/
	public SourceContextShape setType(String type) {
		setProperty("type", type);
		return this;
	}


	/**
	* Gets the source extract of this source context shape
	* The schema or data set or other identifier that identifies the format of this extract
	* @return sourceExtract
	*/
	public String getSourceExtract() {
		return getProperty("sourceExtract");
	}


	/**
	* Changes the source extract of this SourceContextShape
	* @param sourceExtract The new source extract to set
	* @return SourceContextShape
	*/
	public SourceContextShape setSourceExtract(String sourceExtract) {
		setProperty("sourceExtract", sourceExtract);
		return this;
	}


	/**
	* Gets the status of this source context shape
	* The status of the entity being active, inactive, draft or unassigned
	* @return status
	*/
	public String getStatus() {
		return getProperty("status");
	}


	/**
	* Changes the status of this SourceContextShape
	* @param status The new status to set
	* @return SourceContextShape
	*/
	public SourceContextShape setStatus(String status) {
		setProperty("status", status);
		return this;
	}


	/**
	* Gets the source table of this source context shape
	* If a table or message the name of the source table or source message type
	* @return sourceTable
	*/
	public String getSourceTable() {
		return getProperty("sourceTable");
	}


	/**
	* Changes the source table of this SourceContextShape
	* @param sourceTable The new source table to set
	* @return SourceContextShape
	*/
	public SourceContextShape setSourceTable(String sourceTable) {
		setProperty("sourceTable", sourceTable);
		return this;
	}


	/**
	* Gets the scheme of this source context shape
	* The code scheme or graph which the entity belongs to
	* @return scheme
	*/
	public String getScheme() {
		return getProperty("scheme");
	}


	/**
	* Changes the scheme of this SourceContextShape
	* @param scheme The new scheme to set
	* @return SourceContextShape
	*/
	public SourceContextShape setScheme(String scheme) {
		setProperty("scheme", scheme);
		return this;
	}


	/**
	* Gets the source field of this source context shape
	* The source field containing this data item
	* @return sourceField
	*/
	public String getSourceField() {
		return getProperty("sourceField");
	}


	/**
	* Changes the source field of this SourceContextShape
	* @param sourceField The new source field to set
	* @return SourceContextShape
	*/
	public SourceContextShape setSourceField(String sourceField) {
		setProperty("sourceField", sourceField);
		return this;
	}


	/**
	* Gets the is contained in of this source context shape
	* An entity that this entity inherits from, both semantically and structurally, this is also used for sub properties as properties are classes of properties
	* @return isContainedIn
	*/
	public Folder getIsContainedIn() {
		return getProperty("isContainedIn");
	}


	/**
	* Changes the is contained in of this SourceContextShape
	* @param isContainedIn The new is contained in to set
	* @return SourceContextShape
	*/
	public SourceContextShape setIsContainedIn(Folder isContainedIn) {
		setProperty("isContainedIn", isContainedIn);
		return this;
	}


	/**
	* Gets the source regex of this source context shape
	* In the case of text, the nature of a regex expression or nlp algorithm used to parse the source text
	* @return sourceRegex
	*/
	public String getSourceRegex() {
		return getProperty("sourceRegex");
	}


	/**
	* Changes the source regex of this SourceContextShape
	* @param sourceRegex The new source regex to set
	* @return SourceContextShape
	*/
	public SourceContextShape setSourceRegex(String sourceRegex) {
		setProperty("sourceRegex", sourceRegex);
		return this;
	}


	/**
	* Gets the REPLACED BY (attribute) of this source context shape
	* When a status is inactive, the entity that replaces this entity (if any)
	* @return replacedByAttribute
	*/
	public Entity getReplacedByAttribute() {
		return getProperty("replacedByAttribute");
	}


	/**
	* Changes the REPLACED BY (attribute) of this SourceContextShape
	* @param replacedByAttribute The new REPLACED BY (attribute) to set
	* @return SourceContextShape
	*/
	public SourceContextShape setReplacedByAttribute(Entity replacedByAttribute) {
		setProperty("replacedByAttribute", replacedByAttribute);
		return this;
	}


	/**
	* Gets the source heading of this source context shape
	* For an item that depends on the context in which it appears, the heading which provides context
	* @return sourceHeading
	*/
	public String getSourceHeading() {
		return getProperty("sourceHeading");
	}


	/**
	* Changes the source heading of this SourceContextShape
	* @param sourceHeading The new source heading to set
	* @return SourceContextShape
	*/
	public SourceContextShape setSourceHeading(String sourceHeading) {
		setProperty("sourceHeading", sourceHeading);
		return this;
	}


	/**
	* Gets the source Text of this source context shape
	* Source text or term for coded or non coded data
	* @return sourceText
	*/
	public String getSourceText() {
		return getProperty("sourceText");
	}


	/**
	* Changes the source Text of this SourceContextShape
	* @param sourceText The new source Text to set
	* @return SourceContextShape
	*/
	public SourceContextShape setSourceText(String sourceText) {
		setProperty("sourceText", sourceText);
		return this;
	}


	/**
	* Gets the source value of this source context shape
	* The value or code for this concept. Note that a scheme for the code can be inferred from the other context information. In the event of a scheme/code pair, use different fields in the source context
	* @return sourceValue
	*/
	public String getSourceValue() {
		return getProperty("sourceValue");
	}


	/**
	* Changes the source value of this SourceContextShape
	* @param sourceValue The new source value to set
	* @return SourceContextShape
	*/
	public SourceContextShape setSourceValue(String sourceValue) {
		setProperty("sourceValue", sourceValue);
		return this;
	}
}

