package org.endeavourhealth.imapi.query;

public class QuerySort {
	private Sort sort;
	private Integer count= 1;
	private String field;

	public Integer getCount() {
		return count;
	}

	public QuerySort setCount(Integer count) {
		this.count = count;
		return this;
	}

	public String getField() {
		return field;
	}

	public QuerySort setField(String field) {
		this.field = field;
		return this;
	}

	public Sort getSort() {
		return sort;
	}

	public QuerySort setSort(Sort sort) {
		this.sort = sort;
		return this;
	}
}
