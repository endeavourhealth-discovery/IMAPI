package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"operator","fromGraphs","where","resultVariable","resultType"})
public class MatchClause extends TTNode {
	private boolean optional;
	private boolean notExists;
	private List<MatchPath> paths;

	public boolean isOptional() {
		return optional;
	}

	public MatchClause setOptional(boolean optional) {
		this.optional = optional;
		return this;
	}

	public boolean isNotExists() {
		return notExists;
	}

	public MatchClause setNotExists(boolean notExists) {
		this.notExists = notExists;
		return this;
	}

	public List<MatchPath> getPaths() {
		return paths;
	}

	public MatchClause setPaths(List<MatchPath> paths) {
		this.paths = paths;
		return this;
	}

	/**
	 * Adds a property value test match to the match clause
	 * @return the modified match clause
	 */
	public MatchPath addPath(){
		MatchPath path = new MatchPath();
		if (this.paths!=null)
			this.paths= new ArrayList<>();
		this.paths.add(path);
		return path;
	}

}
