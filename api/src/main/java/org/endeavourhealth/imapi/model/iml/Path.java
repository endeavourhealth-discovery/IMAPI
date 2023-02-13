package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTTypedRef;

import java.util.ArrayList;
import java.util.List;

public class Path {
	private TTTypedRef source;
	private List<TTTypedRef> items = new ArrayList<>();
	private TTTypedRef target;

	public List<TTTypedRef> getItems() {
		return items;
	}

	public Path setItems(List<TTTypedRef> items) {
		this.items = items;
		return this;
	}
	public Path addItem(TTTypedRef item){
		if (this.items ==null)
			this.items = new ArrayList<>();
		this.items.add(item);
		return this;
	}

	public TTTypedRef getSource() {
		return source;
	}

	public Path setSource(TTTypedRef source) {
		this.source = source;
		return this;
	}

	public TTTypedRef getTarget() {
		return target;
	}

	public Path setTarget(TTTypedRef target) {
		this.target = target;
		return this;
	}
}
