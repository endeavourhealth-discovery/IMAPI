package org.endeavourhealth.imapi.model.sets;

import org.endeavourhealth.imapi.model.tripletree.TTIri;

public class Select extends TTIri {

	private DataSet object;
	private boolean sum;
	private boolean average;
	private boolean max;
	private boolean count;

	public boolean isCount() {
		return count;
	}

	public Select setCount(boolean count) {
		this.count = count;
		return this;
	}

	public boolean isSum() {
		return sum;
	}

	public Select setSum(boolean sum) {
		this.sum = sum;
		return this;
	}

	public boolean isAverage() {
		return average;
	}

	public Select setAverage(boolean average) {
		this.average = average;
		return this;
	}

	public boolean isMax() {
		return max;
	}

	public Select setMax(boolean max) {
		this.max = max;
		return this;
	}

	public DataSet getObject() {
		return object;
	}

	public Select setObject(DataSet object) {
		this.object = object;
		return this;
	}

	public Select setVar(String var){
		super.setVar(var);
		return this;
	}

	public Select setAlias(String alias){
		super.setAlias(alias);
		return this;
	}

	public Select setName(String name){
		super.setName(name);
		return this;
	}
}
