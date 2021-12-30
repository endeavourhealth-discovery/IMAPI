package org.endeavourhealth.imapi.query;

public class GroupSort {
	private SortBy sortBy;
	private Integer count= 1;
	private String field;
	private String groupBy;

	public Integer getCount() {
		return count;
	}

	public GroupSort setCount(Integer count) {
		this.count = count;
		return this;
	}

	public String getField() {
		return field;
	}

	public GroupSort setField(String field) {
		this.field = field;
		return this;
	}


	public SortBy getSortBy() {
		return sortBy;
	}

	public GroupSort setSortBy(SortBy sortBy) {
		this.sortBy = sortBy;
		return this;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public GroupSort setGroupBy(String groupBy) {
		this.groupBy = groupBy;
		return this;
	}
}
