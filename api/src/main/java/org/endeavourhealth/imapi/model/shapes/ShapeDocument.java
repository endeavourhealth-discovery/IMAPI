package org.endeavourhealth.imapi.model.shapes;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class ShapeDocument {
	private List<TTIriRef> source;
	private List<NodeShape> shape;

	public List<TTIriRef> getSource() {
		return source;
	}

	public ShapeDocument setSource(List<TTIriRef> source) {
		this.source = source;
		return this;
	}

	public ShapeDocument addSource(TTIriRef source){
		if (this.source==null)
			this.source= new ArrayList<>();
		this.source.add(source);
		return this;
	}

	public List<NodeShape> getShape() {
		return shape;
	}

	public ShapeDocument setShape(List<NodeShape> shape) {
		this.shape = shape;
		return this;
	}

	public ShapeDocument addShape(NodeShape shape){
		if (this.shape==null)
			this.shape= new ArrayList<>();
		this.shape.add(shape);
		return this;
	}
}
