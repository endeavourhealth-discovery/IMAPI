package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Select {


	private boolean sum;
	private boolean average;
	private boolean max;
	private boolean count;
	private String binding;
	private String alias;
	private String name;
	private List<Select> object;


	public List<Select> getObject() {
		return object;
	}

	public Select setObject(List<Select> object) {
		this.object = object;
		return this;
	}

	public Select addObject(Select select){
		if (this.object ==null)
			this.object = new ArrayList<>();
		this.object.add(select);
		return this;
	}

	public String getBinding() {
		return binding;
	}

	public String getAlias() {
		return alias;
	}

	public String getName() {
		return name;
	}

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

	public Select setBinding(String binding){
		this.binding = binding;
		return this;
	}

	public Select setAlias(String alias){
		this.alias=alias;
		return this;
	}

	public Select setName(String name){
		this.name=name;
		return this;
	}
}
