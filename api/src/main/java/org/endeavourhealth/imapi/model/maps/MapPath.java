package org.endeavourhealth.imapi.model.maps;

public class MapPath {
	private String path;
	private String variable;
	private String listMode;

	public String getPath() {
		return path;
	}

	public MapPath setPath(String path) {
		this.path = path;
		return this;
	}

	public String getVariable() {
		return variable;
	}

	public MapPath setVariable(String variable) {
		this.variable = variable;
		return this;
	}

	public String getListMode() {
		return listMode;
	}

	public MapPath setListMode(String listMode) {
		this.listMode = listMode;
		return this;
	}
}
