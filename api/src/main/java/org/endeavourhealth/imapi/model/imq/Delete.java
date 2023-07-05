package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"id","iri","@id","alias","case","aggregate",
	"where","orderBy","direction","limit","groupBy","having","select"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Delete{
	private Property where;
	private Element subject;
	private boolean inverse;
	private Element predicate;
	private Element object;
	private List<Delete> delete;


	public Property getWhere() {
		return where;
	}

	public Delete setWhere(Property where) {
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



