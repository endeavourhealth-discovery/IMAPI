package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"id","iri","@id","alias","case","aggregate",
	"where","orderBy","direction","limit","groupBy","having","select"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Delete{
	private Where where;
	private Element subject;
	private boolean inverse;
	private Element predicate;
	private Element object;
	private List<Delete> delete;


	public Where getWhere() {
		return where;
	}

	public Delete setWhere(Where where) {
		this.where = where;
		return this;
	}

	public Element getSubject() {
		return subject;
	}

	public Delete setSubject(Element subject) {
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

	public Element getPredicate() {
		return predicate;
	}

	public Delete setPredicate(Element predicate) {
		this.predicate = predicate;
		return this;
	}

	public Element getObject() {
		return object;
	}

	public Delete setObject(Element object) {
		this.object = object;
		return this;
	}

	public List<Delete> getDelete() {
		return delete;
	}

	public Delete setDelete(List<Delete> delete) {
		this.delete = delete;
		return this;
	}
}



