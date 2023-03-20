package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents terminology concept.
* data model for terminology concepts
*/
public class TerminologyConcept extends IMDMBase<TerminologyConcept> {


	/**
	* Terminology concept constructor with identifier
	*/
	public TerminologyConcept(UUID id) {
		super("TerminologyConcept", id);
	}

	/**
	* Gets the label of this terminology concept
	* The preferred full name of the entity
	* @return label
	*/
	public String getLabel() {
		return getProperty("label");
	}


	/**
	* Changes the label of this TerminologyConcept
	* @param label The new label to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setLabel(String label) {
		setProperty("label", label);
		return this;
	}


	/**
	* Gets the subClassOf of this terminology concept
	* The concept this concept is a sub type of
	* @return subclassof
	*/
	public UUID getSubclassof() {
		return getProperty("subclassof");
	}


	/**
	* Changes the subClassOf of this TerminologyConcept
	* @param subclassof The new subClassOf to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setSubclassof(UUID subclassof) {
		setProperty("subclassof", subclassof);
		return this;
	}


	/**
	* Gets the code of this terminology concept
	* The code for this concept, which may be a local code if local concept, or a core code such as a snomed identifier
	* @return code
	*/
	public String getCode() {
		return getProperty("code");
	}


	/**
	* Changes the code of this TerminologyConcept
	* @param code The new code to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setCode(String code) {
		setProperty("code", code);
		return this;
	}


	/**
	* Gets the comment of this terminology concept
	* The description of the entity
	* @return comment
	*/
	public String getComment() {
		return getProperty("comment");
	}


	/**
	* Changes the comment of this TerminologyConcept
	* @param comment The new comment to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setComment(String comment) {
		setProperty("comment", comment);
		return this;
	}


	/**
	* Gets the type of this terminology concept
	* The base type of the entity i.e. one of the high level entity types
	* @return type
	*/
	public String getType() {
		return getProperty("type");
	}


	/**
	* Changes the type of this TerminologyConcept
	* @param type The new type to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setType(String type) {
		setProperty("type", type);
		return this;
	}


	/**
	* Gets the has term code of this terminology concept
	* Alternative terms or synonyms for this concept. A term may also have a description id.
	* @return hasTermCode
	*/
	public UUID getHasTermCode() {
		return getProperty("hasTermCode");
	}


	/**
	* Changes the has term code of this TerminologyConcept
	* @param hasTermCode The new has term code to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setHasTermCode(UUID hasTermCode) {
		setProperty("hasTermCode", hasTermCode);
		return this;
	}


	/**
	* Gets the status of this terminology concept
	* The status of the entity being active, inactive, draft or unassigned
	* @return status
	*/
	public String getStatus() {
		return getProperty("status");
	}


	/**
	* Changes the status of this TerminologyConcept
	* @param status The new status to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setStatus(String status) {
		setProperty("status", status);
		return this;
	}


	/**
	* Gets the weighting of this terminology concept
	* A number indicating a weighting for this concept which can be used in free text search
	* @return weighting
	*/
	public Integer getWeighting() {
		return getProperty("weighting");
	}


	/**
	* Changes the weighting of this TerminologyConcept
	* @param weighting The new weighting to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setWeighting(Integer weighting) {
		setProperty("weighting", weighting);
		return this;
	}


	/**
	* Gets the scheme of this terminology concept
	* The code scheme or graph which the entity belongs to
	* @return scheme
	*/
	public String getScheme() {
		return getProperty("scheme");
	}


	/**
	* Changes the scheme of this TerminologyConcept
	* @param scheme The new scheme to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setScheme(String scheme) {
		setProperty("scheme", scheme);
		return this;
	}


	/**
	* Gets the definitional status of this terminology concept
	* Indicates the authoring status of this concept, whether necessary only, or both necessary and sufficient. OWL would use Equivalent class axiom to indicate this.
	* @return definitionalStatus
	*/
	public String getDefinitionalStatus() {
		return getProperty("definitionalStatus");
	}


	/**
	* Changes the definitional status of this TerminologyConcept
	* @param definitionalStatus The new definitional status to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setDefinitionalStatus(String definitionalStatus) {
		setProperty("definitionalStatus", definitionalStatus);
		return this;
	}


	/**
	* Gets the has map of this terminology concept
	* Any backward maps from core to legacy. This is limited to complex maps rather than simple matches and are 'backward' matches not suitable for automatic inclusion in subsumption queries
	* @return hasMap
	*/
	public String getHasMap() {
		return getProperty("hasMap");
	}


	/**
	* Changes the has map of this TerminologyConcept
	* @param hasMap The new has map to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setHasMap(String hasMap) {
		setProperty("hasMap", hasMap);
		return this;
	}


	/**
	* Gets the is contained in of this terminology concept
	* An entity that this entity inherits from, both semantically and structurally, this is also used for sub properties as properties are classes of properties
	* @return isContainedIn
	*/
	public UUID getIsContainedIn() {
		return getProperty("isContainedIn");
	}


	/**
	* Changes the is contained in of this TerminologyConcept
	* @param isContainedIn The new is contained in to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setIsContainedIn(UUID isContainedIn) {
		setProperty("isContainedIn", isContainedIn);
		return this;
	}


	/**
	* Gets the usage total of this terminology concept
	* The approximate number of times this concept has been used in around 1 million primary care and acute care records
	* @return usageTotal
	*/
	public Integer getUsageTotal() {
		return getProperty("usageTotal");
	}


	/**
	* Changes the usage total of this TerminologyConcept
	* @param usageTotal The new usage total to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setUsageTotal(Integer usageTotal) {
		setProperty("usageTotal", usageTotal);
		return this;
	}


	/**
	* Gets the im1 id of this terminology concept
	* pointer the original identifer in IM1
	* @return imId
	*/
	public String getImId() {
		return getProperty("imId");
	}


	/**
	* Changes the im1 id of this TerminologyConcept
	* @param imId The new im1 id to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setImId(String imId) {
		setProperty("imId", imId);
		return this;
	}


	/**
	* Gets the matched To of this terminology concept
	* For a legacy concept, one or more concept this concept matches to. A query for the matched to concepts would entail this concept normally, but NOT the children of this concept
	* @return matchedTo
	*/
	public String getMatchedTo() {
		return getProperty("matchedTo");
	}


	/**
	* Changes the matched To of this TerminologyConcept
	* @param matchedTo The new matched To to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setMatchedTo(String matchedTo) {
		setProperty("matchedTo", matchedTo);
		return this;
	}


	/**
	* Gets the source context of this terminology concept
	* The source context object for this concept indicating the system, table, field and organisation that published this legacy concept
	* @return sourceContext
	*/
	public UUID getSourceContext() {
		return getProperty("sourceContext");
	}


	/**
	* Changes the source context of this TerminologyConcept
	* @param sourceContext The new source context to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setSourceContext(UUID sourceContext) {
		setProperty("sourceContext", sourceContext);
		return this;
	}


	/**
	* Gets the REPLACED BY (attribute) of this terminology concept
	* Indicates the concepts that this concept has been replaced by
	* @return replacedByAttribute
	*/
	public UUID getReplacedByAttribute() {
		return getProperty("replacedByAttribute");
	}


	/**
	* Changes the REPLACED BY (attribute) of this TerminologyConcept
	* @param replacedByAttribute The new REPLACED BY (attribute) to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setReplacedByAttribute(UUID replacedByAttribute) {
		setProperty("replacedByAttribute", replacedByAttribute);
		return this;
	}


	/**
	* Gets the is Child Of of this terminology concept
	* points to the parent legacy concept that this concept is in. Note that it may or may not be a true subtype. Reflects the original taxonomy
	* @return isChildOf
	*/
	public UUID getIsChildOf() {
		return getProperty("isChildOf");
	}


	/**
	* Changes the is Child Of of this TerminologyConcept
	* @param isChildOf The new is Child Of to set
	* @return TerminologyConcept
	*/
	public TerminologyConcept setIsChildOf(UUID isChildOf) {
		setProperty("isChildOf", isChildOf);
		return this;
	}
}

