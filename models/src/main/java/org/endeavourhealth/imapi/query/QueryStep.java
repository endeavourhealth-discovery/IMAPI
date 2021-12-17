package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;

/**
 * A specialised TTNode containing the model of a single feature for a query
 */

public class QueryStep extends TTNode {

	/**
	 * What happens to the matches if matched or not matched
	 * @param action action if matched or not matched
	 */
	public QueryStep setInclusionAction(TTIriRef action){
		this.set(IM.INCLUSION_ACTION,action);
		return this;
	}

	/**
	 * Gets the result set inclusion action when matches are found or not found
	 * @return
	 */
	public TTIriRef getInclusionAction(){
		return this.get(IM.INCLUSION_ACTION).asIriRef();
	}

	/**Adds one match pattern to the step
	 *
	 * @param match the match pattern with filter and optional setting
	 * @return Query step for chaining purposes
	 */
	public QueryStep addMatch(MatchTriple match){
		this.addObject(IM.MATCH,match);
		return this;
	}

}











