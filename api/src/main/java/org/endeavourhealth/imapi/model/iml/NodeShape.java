package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"iri","name","property"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class NodeShape extends TTIriRef {
	private List<PropertyShape> property;

	public List<PropertyShape> getProperty() {
		return property;
	}

	public NodeShape setProperty(List<PropertyShape> property) {
		this.property = property;
		return this;
	}

	public NodeShape addProperty(PropertyShape property) {
		if (this.property==null)
			this.property= new ArrayList<>();
		this.property.add(property);
		return this;
	}
}
