package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.iml.Path;
import org.endeavourhealth.imapi.model.tripletree.TTTypedRef;

import java.util.ArrayList;
import java.util.List;


@JsonPropertyOrder({"source","paths","target"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PathDocument {
	private List<Where> where;

	public List<Where> getWhere() {
		return where;
	}

	public PathDocument setWhere(List<Where> where) {
		this.where = where;
		return this;
	}

	public PathDocument addWhere(Where where){
		if (this.where==null)
			this.where= new ArrayList<>();
		this.where.add(where);
		return this;
	}
}
