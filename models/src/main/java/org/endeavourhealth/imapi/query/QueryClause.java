package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;

public class QueryClause extends TTNode {

	public WhereClause addWhere(){
		WhereClause where = new WhereClause();
		if (this.get(IM.AND)==null)
			this.set(IM.AND,new TTArray().setList(true));
		this.addObject(IM.AND,where);
		return where;
	}

	public WhereClause addOr(){
		if (this.get(IM.OR)==null)
			this.set(IM.OR,new TTArray().setList(true));
		WhereClause match= new WhereClause();
		this.get(IM.OR).add(match);
		return match;
	}

	public WhereClause addNot(){
		if (this.get(IM.NOT)==null)
			this.set(IM.NOT,new TTArray().setList(true));
		WhereClause match= new WhereClause();
		this.get(IM.NOT).add(match);
		return match;
	}


	public QueryClause setLatest(){
		set(IM.LATEST, TTLiteral.literal(1));
		return this;
	}

	public QueryClause setLatest(Integer count){
		set(IM.LATEST,TTLiteral.literal(count));
		return this;
	}
	public QueryClause setEarliest(){
		set(IM.EARLIEST,TTLiteral.literal(1));
		return this;
	}

	public QueryClause setEarliest(Integer count) {
		set(IM.EARLIEST, TTLiteral.literal(count));
		return this;
	}
}
