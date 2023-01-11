package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTTypedRef;

import java.util.ArrayList;
import java.util.List;

public class Path {
	private List<TTTypedRef> items;

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



}
