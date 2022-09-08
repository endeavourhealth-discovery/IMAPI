package org.endeavourhealth.imapi.model.iml;

import java.util.ArrayList;
import java.util.List;

public class Select {
	private String path;
	private List<String> property;
	private List<String> groupBy;
	private boolean count;


	public List<String> getGroupBy() {
		return groupBy;
	}

	public Select setGroupBy(List<String> groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public Select addGroupBy(String groupBy){
		if (this.groupBy==null)
			this.groupBy= new ArrayList<>();
		this.groupBy.add(groupBy);
		return this;
	}

	public String getPath() {
		return path;
	}

	public Select setPath(String path) {
		this.path = path;
		return this;
	}

	public List<String> getProperty() {
		return property;
	}

	public Select setProperty(List<String> property) {
		this.property = property;
		return this;
	}

	public Select addProperty(String property){
		if (this.property==null)
			this.property= new ArrayList<>();
		this.property.add(property);
		return this;
	}


	public boolean isCount() {
		return count;
	}

	public Select setCount(boolean count) {
		this.count = count;
		return this;
	}
}
