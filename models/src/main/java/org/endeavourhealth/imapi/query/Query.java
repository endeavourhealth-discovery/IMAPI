package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IMQ;

import java.util.zip.DataFormatException;

/**
	* A specialised RDF node that provides methods to build a query.
	* Provides the node as a TTNode thus serializable as JSON-LD or Turtle
	*/
public class Query extends TTNode {

	public Query(){}

	/**
	 * Instantiates a query from an RDF pre-populated node
	 * @param source the pre-populaed RDF query node
	 */
	public Query(TTNode source){
		this.setPredicateMap(source.getPredicateMap());
	}



	/**
	 * selects or creates a select clause
	 * @return the select clause as an array.
	 */
	public TTArray addSelectVar(String var) throws DataFormatException {
		addExpressionAs(var,null,null);
		if (get(IMQ.SELECT)==null)
			set(IMQ.SELECT,new TTArray());
		return get(IMQ.SELECT).asArray();
	}


	/**
	 * Adds a variable and 'as' to select clause
	 * @param var variable name without ? or $
	 * @param as projection (column) alias wihout ? or $
	 * @return the select clause
	 */
	public TTArray addSelectVarAs(String var,String as) throws DataFormatException {
		return addExpressionAs(var,as,null);
	}
	/**
	 * Adds a select variable and/ or expression with optional field name 'as'
	 * @param var  variable name (without the ? or $)
	 * @param as  column name or projection name (wihout the ? or $)
	 * @param expression (expression string)
	 * @return select for chaining
	 */
	public TTArray addExpressionAs(String var, String as, String expression) throws DataFormatException {
		TTArray select = setSelect();
		TTNode expAs= new TTNode();
		if (expression!=null&as==null)
			throw new DataFormatException("Must have column name for expression");
		if (as!=null&var==null&expression==null)
			throw new DataFormatException("Must have variable or expression for column name");
		select.add(expAs);
		if (var!=null)
			expAs.set(IMQ.VAR, TTLiteral.literal(var));
		if (as!=null)
			expAs.set(IMQ.AS,TTLiteral.literal(as));
		if (expression!=null)
			expAs.set(IMQ.EXPRESSION,TTLiteral.literal(expression));
		return get(IMQ.SELECT).asArray();
	}
	public TTArray setSelect(){
		if (get(IMQ.SELECT)==null)
			set(IMQ.SELECT,new TTArray());
		return get(IMQ.SELECT).asArray();

	}

	/**
	 * Sets or creates a where clause
	 * @return where clause
	 */
	public QueryClause setWhere(){
		if (get(IMQ.WHERE) == null)
			set(IMQ.WHERE, new QueryClause());
		return (QueryClause) get(IMQ.WHERE);
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


}
