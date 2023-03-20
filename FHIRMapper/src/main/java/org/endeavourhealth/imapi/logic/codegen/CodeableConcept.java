package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents codeable concept.
* A coded concept used in a health record system that may or may not be a defined concept in the ontology. It represents the local definition of a code and is of the form used in HL7 messaging
*/
public class CodeableConcept extends IMDMBase<CodeableConcept> {


	/**
	* Codeable concept constructor with identifier
	*/
	public CodeableConcept(UUID id) {
		super("CodeableConcept", id);
	}

	/**
	* Gets the label of this codeable concept
	* The preferred full name of the entity
	* @return label
	*/
	public String getLabel() {
		return getProperty("label");
	}


	/**
	* Changes the label of this CodeableConcept
	* @param label The new label to set
	* @return CodeableConcept
	*/
	public CodeableConcept setLabel(String label) {
		setProperty("label", label);
		return this;
	}


	/**
	* Gets the matched concept of this codeable concept
	* The core concept iri that this codeable concept has been matched to in this instance
	* @return matchedConcept
	*/
	public UUID getMatchedConcept() {
		return getProperty("matchedConcept");
	}


	/**
	* Changes the matched concept of this CodeableConcept
	* @param matchedConcept The new matched concept to set
	* @return CodeableConcept
	*/
	public CodeableConcept setMatchedConcept(UUID matchedConcept) {
		setProperty("matchedConcept", matchedConcept);
		return this;
	}


	/**
	* Gets the comment of this codeable concept
	* The description of the entity
	* @return comment
	*/
	public String getComment() {
		return getProperty("comment");
	}


	/**
	* Changes the comment of this CodeableConcept
	* @param comment The new comment to set
	* @return CodeableConcept
	*/
	public CodeableConcept setComment(String comment) {
		setProperty("comment", comment);
		return this;
	}


	/**
	* Gets the original code of this codeable concept
	* The original code entered in the publication system which may be mapped to a code concept
	* @return originalCode
	*/
	public String getOriginalCode() {
		return getProperty("originalCode");
	}


	/**
	* Changes the original code of this CodeableConcept
	* @param originalCode The new original code to set
	* @return CodeableConcept
	*/
	public CodeableConcept setOriginalCode(String originalCode) {
		setProperty("originalCode", originalCode);
		return this;
	}


	/**
	* Gets the type of this codeable concept
	* The base type of the entity i.e. one of the high level entity types
	* @return type
	*/
	public String getType() {
		return getProperty("type");
	}


	/**
	* Changes the type of this CodeableConcept
	* @param type The new type to set
	* @return CodeableConcept
	*/
	public CodeableConcept setType(String type) {
		setProperty("type", type);
		return this;
	}


	/**
	* Gets the original scheme of this codeable concept
	* The original code scheme identifier, may be a url or a OID or informal identifier
	* @return originalScheme
	*/
	public String getOriginalScheme() {
		return getProperty("originalScheme");
	}


	/**
	* Changes the original scheme of this CodeableConcept
	* @param originalScheme The new original scheme to set
	* @return CodeableConcept
	*/
	public CodeableConcept setOriginalScheme(String originalScheme) {
		setProperty("originalScheme", originalScheme);
		return this;
	}


	/**
	* Gets the status of this codeable concept
	* The status of the entity being active, inactive, draft or unassigned
	* @return status
	*/
	public String getStatus() {
		return getProperty("status");
	}


	/**
	* Changes the status of this CodeableConcept
	* @param status The new status to set
	* @return CodeableConcept
	*/
	public CodeableConcept setStatus(String status) {
		setProperty("status", status);
		return this;
	}


	/**
	* Gets the original code term of this codeable concept
	* The original display term for this code usually as a reference. Should not be confused with original text as seen by the user, which may be different depending on the context
	* @return originalCodeTerm
	*/
	public String getOriginalCodeTerm() {
		return getProperty("originalCodeTerm");
	}


	/**
	* Changes the original code term of this CodeableConcept
	* @param originalCodeTerm The new original code term to set
	* @return CodeableConcept
	*/
	public CodeableConcept setOriginalCodeTerm(String originalCodeTerm) {
		setProperty("originalCodeTerm", originalCodeTerm);
		return this;
	}


	/**
	* Gets the scheme of this codeable concept
	* The code scheme or graph which the entity belongs to
	* @return scheme
	*/
	public String getScheme() {
		return getProperty("scheme");
	}


	/**
	* Changes the scheme of this CodeableConcept
	* @param scheme The new scheme to set
	* @return CodeableConcept
	*/
	public CodeableConcept setScheme(String scheme) {
		setProperty("scheme", scheme);
		return this;
	}


	/**
	* Gets the original qualifier term of this codeable concept
	* A qualifier term in the original system which may result in a different concept map. For Example 'negative' or 'positive' or a block of text
	* @return originalQualifierTerm
	*/
	public String getOriginalQualifierTerm() {
		return getProperty("originalQualifierTerm");
	}


	/**
	* Changes the original qualifier term of this CodeableConcept
	* @param originalQualifierTerm The new original qualifier term to set
	* @return CodeableConcept
	*/
	public CodeableConcept setOriginalQualifierTerm(String originalQualifierTerm) {
		setProperty("originalQualifierTerm", originalQualifierTerm);
		return this;
	}


	/**
	* Gets the is contained in of this codeable concept
	* An entity that this entity inherits from, both semantically and structurally, this is also used for sub properties as properties are classes of properties
	* @return isContainedIn
	*/
	public UUID getIsContainedIn() {
		return getProperty("isContainedIn");
	}


	/**
	* Changes the is contained in of this CodeableConcept
	* @param isContainedIn The new is contained in to set
	* @return CodeableConcept
	*/
	public CodeableConcept setIsContainedIn(UUID isContainedIn) {
		setProperty("isContainedIn", isContainedIn);
		return this;
	}


	/**
	* Gets the REPLACED BY (attribute) of this codeable concept
	* When a status is inactive, the entity that replaces this entity (if any)
	* @return replacedByAttribute
	*/
	public UUID getReplacedByAttribute() {
		return getProperty("replacedByAttribute");
	}


	/**
	* Changes the REPLACED BY (attribute) of this CodeableConcept
	* @param replacedByAttribute The new REPLACED BY (attribute) to set
	* @return CodeableConcept
	*/
	public CodeableConcept setReplacedByAttribute(UUID replacedByAttribute) {
		setProperty("replacedByAttribute", replacedByAttribute);
		return this;
	}


	/**
	* Gets the name of this codeable concept
	* The name of the entity
	* @return name
	*/
	public String getName() {
		return getProperty("name");
	}


	/**
	* Changes the name of this CodeableConcept
	* @param name The new name to set
	* @return CodeableConcept
	*/
	public CodeableConcept setName(String name) {
		setProperty("name", name);
		return this;
	}
}

