package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IMQ;

import java.util.zip.DataFormatException;

/**
 * A Select clause for a query consisting of an array of expressions which may be variables, variables with aliases or exprerssions
 */
public class Select {
	private String var;
	private String expression;
	private String alias;

	public String getVar() {
		return var;
	}

	public Select setVar(String var) {
		this.var = var;
		return this;
	}

	public String getExpression() {
		return expression;
	}

	public Select setExpression(String expression) {
		this.expression = expression;
		return this;
	}

	public String getAlias() {
		return alias;
	}

	public Select setAlias(String alias) {
		this.alias = alias;
		return this;
	}
}
