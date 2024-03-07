package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

public class TransformRequest {
	private TTIriRef transformMap;
	private String sourceFormat;
	private String targetFormat;
	private Map<String, List<Object>> source;

	public TTIriRef getTransformMap() {
		return transformMap;
	}

	@JsonSetter
	public TransformRequest setTransformMap(TTIriRef transformMap) {
		this.transformMap = transformMap;
		return this;
	}

	public TransformRequest setTransformMap(String iri) throws DataFormatException {
		if (iri != null && !iri.isEmpty() && !iri.matches("[a-z]+[:].*")) {
			throw new DataFormatException("Invalid iri format : " + iri);
		}
		else {
			this.transformMap = TTIriRef.iri(iri);
			return this;
		}
	}


	public String getSourceFormat() {
		return sourceFormat;
	}

	public TransformRequest setSourceFormat(String sourceFormat) {
		this.sourceFormat = sourceFormat;
		return this;
	}

	public String getTargetFormat() {
		return targetFormat;
	}

	public TransformRequest setTargetFormat(String targetFormat) {
		this.targetFormat = targetFormat;
		return this;
	}

	public Map<String, List<Object>> getSource() {
		return source;
	}

	public TransformRequest setSource(Map<String, List<Object>> source) {
		this.source = source;
		return this;
	}

	public TransformRequest addSource(String type,Object source){
		if (this.source==null)
			this.source= new HashMap<>();
		this.source.computeIfAbsent(type,t-> new ArrayList<>()).add(source);
		return this;
	}
}
