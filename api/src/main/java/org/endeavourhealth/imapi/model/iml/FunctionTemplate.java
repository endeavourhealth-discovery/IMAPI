package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FunctionTemplate extends Entity {
	private TTIriRef function;
	private List<ParameterTemplate> parameterTemplate;

	public TTIriRef getFunction() {
		return function;
	}

	public FunctionTemplate setFunction(TTIriRef function) {
		this.function = function;
		return this;
	}

	public List<ParameterTemplate> getParameterTemplate() {
		return parameterTemplate;
	}

	public FunctionTemplate setParameterTemplate(List<ParameterTemplate> parameterTemplate) {
		this.parameterTemplate = parameterTemplate;
		return this;
	}
		public FunctionTemplate addParameter (ParameterTemplate parameter){
			if (this.parameterTemplate == null) {
				this.parameterTemplate = new ArrayList<>();
			}
			this.parameterTemplate.add(parameter);
			return this;
		}
		public FunctionTemplate parameter (Consumer< ParameterTemplate > builder) {
			ParameterTemplate parameter = new ParameterTemplate();
			addParameter(parameter);
			builder.accept(parameter);
			return this;
		}


}
