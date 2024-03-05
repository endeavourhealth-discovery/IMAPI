package org.endeavourhealth.imapi.model.imq;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Where {
	private Bool bool;
	private List<Where> where;
	private Property property;

	public Bool getBool() {
		return bool;
	}

	public Where setBool(Bool bool) {
		this.bool = bool;
		return this;
	}

	public Where setWhere(List<Where> where) {
		this.where = where;
		return this;
	}
		public Where addWhere (Where where){
			if (this.where == null) {
				this.where = new ArrayList();
			}
			this.where.add(where);
			return this;
		}
		public Where where (Consumer < Where > builder) {
			Where where = new Where();
			addWhere(where);
			builder.accept(where);
			return this;
		}


	public List<Where> getWhere() {
		return where;
	}

	public Where setProperty(Property property) {
		this.property = property;
		return this;
	}
		public Where property (Consumer < Property > builder) {
			Property property = new Property();
			this.property = property;
			builder.accept(property);
			return this;
	}

	public Property getProperty() {
		return property;
	}

}
