package org.endeavourhealth.imapi.model.imq;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Update {
	private String name;
	private String description;
	private From from;
	private List<Delete> delete;

	public String getName() {
		return name;
	}

	public Update setName(String name) {
		this.name = name;
		return this;
	}

	public From getFrom() {
		return from;
	}

	public Update setFrom(From from) {
		this.from = from;
		return this;
	}

	public Update from(Consumer<From> builder){
		this.from= new From();
		builder.accept(this.from);
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Update setDescription(String description) {
		this.description = description;
		return this;
	}

	public List<Delete> getDelete() {
		return delete;
	}

	public Update setDelete(List<Delete> delete) {
		this.delete = delete;
		return this;
	}

	public Update addDelete(Delete delete){
		if (this.delete==null)
			this.delete= new ArrayList<>();
		this.delete.add(delete);
		return this;
	}

	public Update delete(Consumer<Delete> builder){
		Delete delete= new Delete();
		addDelete(delete);
		builder.accept(delete);
		return this;
	}
}
