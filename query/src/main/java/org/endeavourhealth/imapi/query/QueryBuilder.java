package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IMQ;

/**
 * Builer utilities for an RDF representation of a SPARQL query
 */
public class QueryBuilder {

	/**
	 * selects or creates a select clause
	 * @param outer query or outer clause for sub select
	 * @return the select clause as an array.
	 */
	public TTArray setSelect(TTNode outer){
		if (outer.get(IMQ.SELECT)==null)
			outer.set(IMQ.SELECT,new TTArray());
		return outer.get(IMQ.SELECT).asArray();
	}


	/**
	 * Adds a variable and 'as' to select clause
	 * @param var variable name without ? or $
	 * @param as projection (column) alias wihout ? or $
	 * @return the select clause
	 */
	public TTArray addVarAs(TTArray select,String var,String as){
		return addExpressionAs(select,var,as,null);
	}
	/**
	 * Adds a select variable and/ or expression with optional field name 'as'
	 * @param var  variable name (without the ? or $)
	 * @param as  column name or projection name (wihout the ? or $)
	 * @param expression (expression string)
	 * @return select for chaining
	 */
	public TTArray addExpressionAs(TTArray select,String var, String as, String expression) {
		TTNode expAs= new TTNode();
		select.add(expAs);
		if (var!=null)
			expAs.set(IMQ.VAR, TTLiteral.literal(var));
		if (as!=null)
			expAs.set(IMQ.AS,TTLiteral.literal(as));
		if (expression!=null)
			expAs.set(IMQ.EXPRESSION,TTLiteral.literal(expression));
		return select;
	}

	/**
	 * Sets or creates a where clause
	 * @param outer the query or subquery containing the where clause
	 * @return where clause
	 */
	public TTNode setWhere(TTNode outer){
		if (outer.get(IMQ.WHERE) == null)
			outer.set(IMQ.WHERE, new TTNode());
		return outer.get(IMQ.WHERE).asNode();
	}

	/**
	 * Adds a triple to  clause with a simple triple
	 * @param subject iri or variable without ? or $
	 * @param predicate iri or variable without ? or $
	 * @param object iri variable or data value or blank node
	 * @return the clause containing the triple
	 */

	public TTNode addTriple(TTNode clause,Object subject, Object predicate, Object object){
		if (clause.get(IMQ.TRIPLE)==null)
			clause.set(IMQ.TRIPLE,new TTArray());
		TTNode tpl= new TTNode();
		clause.get(IMQ.TRIPLE).asArray().add(tpl);
		if (subject instanceof TTIriRef)
			tpl.set(IMQ.SUBJECT,(TTIriRef) subject);
		else
			tpl.set(IMQ.SUBJECT,TTLiteral.literal((String) subject));
		if (predicate instanceof TTIriRef)
			tpl.set(IMQ.PREDICATE,(TTIriRef) predicate);
		else
			tpl.set(IMQ.PREDICATE,TTLiteral.literal((String) predicate));
		if (object instanceof TTIriRef)
			tpl.set(IMQ.OBJECT,(TTIriRef) object);
		else
			tpl.set(IMQ.OBJECT,TTLiteral.literal((String) object));
		return clause;
	}


	/**
	 * Adds a union
	 * @param  outer where or minus clause
	 * @return the union
	 */

	public TTNode addUnion(TTNode outer) {
		if (outer.get(IMQ.UNION) == null)
			outer.set(IMQ.UNION, new TTArray());
		TTNode union= new TTNode();
		outer.get(IMQ.UNION).asArray().add(union);
		return union;
	}


	/**
	 * Adds a filter clause
	 * @param clause the group clause to add thefilter to
	 * @return the filter clause
	 */
	public TTNode addFilter(TTNode clause){
		if (clause.get(IMQ.FILTER)==null)
			clause.set(IMQ.FILTER,new TTArray());
		TTNode filter= new TTNode();
		clause.get(IMQ.FILTER).asArray().add(filter);
		return filter;
	
	}

	/**
	 * Adds a variable to any node
	 * @param node filter clause to add the variable to
	 * @param var the name of the variable without ?/$
	 * @return the filter clause
	 */

	public TTNode addVar(TTNode node,String var){
		node.set(IMQ.VAR,TTLiteral.literal(var));
		return node;
	}

	/**
	 * Adds an object to an IN list
	 * @param object the object to add to the in LIST
	 * @return the query or filter clause with the in LIST
	 */
	public TTNode addIn(TTNode node,Object object){
		if (node.get(IMQ.IN_LIST)==null)
			node.set(IMQ.IN_LIST,new TTArray());
		if (object instanceof TTIriRef)
			node.get(IMQ.IN_LIST).asArray().add((TTIriRef) object);
		else
			node.get(IMQ.IN_LIST).asArray().add(TTLiteral.literal((String) object));
		return node;
	}
	public TTNode setMinus(TTNode outer){
		if (outer.get(IMQ.MINUS) == null)
			outer.set(IMQ.MINUS, new TTNode());
		return outer.get(IMQ.MINUS).asNode();

	}


	/**
	 * Creates a new query
	 * @return
	 */
	public TTNode createQuery() {
		TTNode query= new TTNode();
		return query;
	}

}
