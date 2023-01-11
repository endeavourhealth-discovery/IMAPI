package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTTypedRef;

import java.util.ArrayList;
import java.util.List;


@JsonPropertyOrder({"source","paths","target"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PathDocument {
	private TTTypedRef source;
	private TTTypedRef target;
	private List<Path> paths;

	public TTTypedRef getSource() {
		return source;
	}

	public PathDocument setSource(TTTypedRef source) {
		this.source = source;
		return this;
	}

	public TTTypedRef getTarget() {
		return target;
	}

	public PathDocument setTarget(TTTypedRef target) {
		this.target = target;
		return this;
	}

	public List<Path> getPaths() {
		return paths;
	}

	public PathDocument setPaths(List<Path> paths) {
		this.paths = paths;
		return this;
	}
	public PathDocument addPath(Path path){
		if (this.paths==null)
			this.paths= new ArrayList<>();
		this.paths.add(path);
		return this;
	}
}
