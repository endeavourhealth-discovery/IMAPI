package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.iml.FunctionClause;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"id","iri","@id","alias","case","aggregate",
	"where","orderBy","direction","limit","groupBy","having","select"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Delete{
	private Where where;
	private TTAlias subject;
	private boolean inverse;
	private TTAlias predicate;
	private TTAlias object;
	private List<Delete> delete;


	public TTAlias getSubject() {
		return subject;
	}

	public Delete setSubject(TTAlias subject) {
		this.subject = subject;
		return this;
	}

	public boolean isInverse() {
		return inverse;
	}

	public Delete setInverse(boolean inverse) {
		this.inverse = inverse;
		return this;
	}

	public TTAlias getPredicate() {
		return predicate;
	}

	public Delete setPredicate(TTAlias predicate) {
		this.predicate = predicate;
		return this;
	}

	public TTAlias getObject() {
		return object;
	}

	public Delete setObject(TTAlias object) {
		this.object = object;
		return this;
	}

	public Where getWhere() {
		return where;
	}

	@JsonSetter
	public Delete setWhere(Where match) {
		this.where = match;
		return this;
	}


	public Delete where(Consumer<Where> builder){
		this.where= new Where();
		builder.accept(this.where);
		return this;
	}







	public List<Delete> getDelete() {
		return delete;
	}

	public Delete setDelete(List<Delete> select) {
		this.delete = select;
		return this;
	}



	public Delete select(Consumer<Delete> builder) {
		if (this.delete == null)
			this.delete = new ArrayList<>();
		Delete select = new Delete();
		this.delete.add(select);
		builder.accept(select);
		return this;
	}


	public Delete addDelete(Delete select) {
		if (this.delete == null)
			this.delete = new ArrayList<>();
		this.delete.add(select);
		return this;
	}







}



