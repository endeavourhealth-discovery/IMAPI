package org.endeavourhealth.imapi.model.sets;

public class Page {
	private Integer pageNumber;
	private Integer pageSize;

	public Integer getPageNumber() {
		return pageNumber;
	}

	public Page setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Page setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}
}
