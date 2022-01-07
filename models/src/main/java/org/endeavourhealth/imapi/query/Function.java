package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class Function {
	private TTIriRef name;
	List<Argument> argument;

	public TTIriRef getName() {
		return name;
	}

	public Function setName(TTIriRef name) {
		this.name = name;
		return this;
	}

	public List<Argument> getArgument() {
		return argument;
	}

	public Function setArgument(List<Argument> argument) {
		this.argument = argument;
		return this;
	}

	public Function addArgument(Argument argument){
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}
}
