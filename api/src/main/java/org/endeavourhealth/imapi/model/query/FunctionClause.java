package org.endeavourhealth.imapi.model.query;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;

public class FunctionClause {
	TTIriRef funtionIri;
	List<ArgumentClause> argument;

	public TTIriRef getFuntionIri() {
		return funtionIri;
	}

	public FunctionClause setFuntionIri(TTIriRef funtionIri) {
		this.funtionIri = funtionIri;
		return this;
	}

	public List<ArgumentClause> getArgument() {
		return argument;
	}

	public FunctionClause setArgument(List<ArgumentClause> argument) {
		this.argument = argument;
		return this;
	}

	public FunctionClause addArgument(ArgumentClause argument) {
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}
}
