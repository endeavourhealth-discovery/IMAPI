package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents document.
* A structure of a document. This is a light weight definition as documents tend to specialise very quickly to many types, even in the context of a single provider.
*/
public class Document extends IMDMBase<Document> {


	/**
	* Document constructor with identifier
	*/
	public Document(UUID id) {
		super("Document", id);
	}

	/**
	* Gets the component type of this document
	* @return componentType
	*/
	public String getComponentType() {
		return getProperty("componentType");
	}


	/**
	* Changes the component type of this Document
	* @param componentType The new component type to set
	* @return Document
	*/
	public Document setComponentType(String componentType) {
		setProperty("componentType", componentType);
		return this;
	}


	/**
	* Gets the document content of this document
	* @return documentContent
	*/
	public String getDocumentContent() {
		return getProperty("documentContent");
	}


	/**
	* Changes the document content of this Document
	* @param documentContent The new document content to set
	* @return Document
	*/
	public Document setDocumentContent(String documentContent) {
		setProperty("documentContent", documentContent);
		return this;
	}


	/**
	* Gets the document Type of this document
	* @return documentType
	*/
	public String getDocumentType() {
		return getProperty("documentType");
	}


	/**
	* Changes the document Type of this Document
	* @param documentType The new document Type to set
	* @return Document
	*/
	public Document setDocumentType(String documentType) {
		setProperty("documentType", documentType);
		return this;
	}


	/**
	* Gets the has section of this document
	* @return hasSection
	*/
	public String getHasSection() {
		return getProperty("hasSection");
	}


	/**
	* Changes the has section of this Document
	* @param hasSection The new has section to set
	* @return Document
	*/
	public Document setHasSection(String hasSection) {
		setProperty("hasSection", hasSection);
		return this;
	}
}

