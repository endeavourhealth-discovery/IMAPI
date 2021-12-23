package org.endeavourhealth.imapi.query;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"not","subject","predicate","object","or"})
public class MatchClause {
	private IriVar subject;
	private IriVar predicate;
	private Filter object;
	private Boolean not;
	private List<MatchClause> or;

	public IriVar getSubject() {
		return subject;
	}


	public MatchClause setSubject(IriVar subject) {
		this.subject = subject;
		return this;
	}


	public MatchClause setSubject(TTIriRef type, String var) {
		this.subject = new IriVar(type.getIri()).setVar(var);
		return this;
	}


	public MatchClause setSubject(String var) {
		this.subject = new IriVar().setVar(var);
		return this;
	}

	public IriVar getPredicate() {
		return predicate;
	}



	public MatchClause setPredicate(TTIriRef predicate) {
		this.predicate = new IriVar(predicate.getIri());
		return this;
	}
	public MatchClause setPredicate(String predicate) {
		this.predicate = new IriVar(predicate);
		return this;
	}


	public Filter getObject() {
		return object;
	}

	public MatchClause setObject(Filter object) {
		this.object = object;
		return this;
	}


	public MatchClause setObject(Comparison comp, TTIriRef set) {
		this.object = new Filter().setClassTest(comp,set);
		return this;
	}


	public MatchClause setObject(Comparison comp, String value) {
		this.object = new Filter().setValueTest(comp,value);
		return this;
	}

	public Boolean getNot() {
		return not;
	}

	public MatchClause setNot(Boolean not) {
		this.not = not;
		return this;
	}

	public List<MatchClause> getOr() {
		return or;
	}

	public MatchClause setOr(List<MatchClause> or) {
		this.or = or;
		return this;
	}

	public MatchClause addOr(){
		return addOr(new MatchClause());
	}

	public MatchClause addOr(MatchClause match){
		if (this.or==null)
			this.or= new ArrayList<>();
		or.add(match);
		return match;
	}

}










