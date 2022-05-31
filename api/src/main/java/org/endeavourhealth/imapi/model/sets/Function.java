package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Function {
	TTIriRef id;
	String name;
	List<Argument> argument;

	public String getName() {
		return name;
	}

	public Function setName(String name) {
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

	public Function addArgument(Argument argument) {
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}



	public Argument addArgument() {
		if (this.argument==null)
			this.argument= new ArrayList<>();
		Argument newArg= new Argument();
		this.argument.add(newArg);
		return newArg;
	}

	public TTIriRef getId() {
		return id;
	}

	public Function setId(TTIriRef id) {
		this.id = id;
		return this;
	}
}
