package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
	* A serializable query object containing a sequence of clauses resulting in the definition of a  data set output.
	* <p> In effect a high level process model taylored to the health query ues cases</p>
	*/
@JsonPropertyOrder({"prefix","mainSubject","steps"})
public class Query {
	private static final Map<String,String> nsPrefix= new HashMap<>();
	private static final Map<String,String> prefix= new HashMap<>();
	private List<QueryStep> steps;
	private IriVar mainSubject;

	public Query(){
		addPrefix(IM.NAMESPACE,"im");
		addPrefix(RDFS.NAMESPACE,"rdfs");
		addPrefix(RDF.NAMESPACE,"rdf");
	}

	public static void addPrefix(String ns,String px){
		nsPrefix.put(ns,px);
		prefix.put(px,ns);
	}

	@JsonIgnore
	public String toJson() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}

	public List<QueryStep> getSteps() {
		return steps;
	}

	public Query setSteps(List<QueryStep> steps) {
		this.steps = steps;
		return this;
	}
	public QueryStep addStep(QueryStep step){
		if (this.steps==null)
			this.steps= new ArrayList<>();
		this.steps.add(step);
		return step;
	}
	public QueryStep addStep(){
		return addStep(new QueryStep());
	}

	public IriVar getMainSubject() {
		return mainSubject;
	}

	public Query setMainSubject(IriVar mainSubject) {
		this.mainSubject = mainSubject;
		return this;
	}
	public Query setMainSubject(TTIriRef mainSubject) {
		this.mainSubject = new IriVar(mainSubject.getIri());
		return this;
	}
	public Query setMainSubject(String mainSubject) {
		this.mainSubject = new IriVar(mainSubject);
		return this;
	}



	public Map<String,String> getPrefix(){
		return prefix;
	}


	public static String getShort(String iri) {
		if (iri==null)
			return null;
		int end = Integer.max(iri.lastIndexOf("/"), iri.lastIndexOf("#"));
		String path = iri.substring(0, end + 1);
		String prefix = nsPrefix.get(path);
		if (prefix == null)
			return iri;
		else
		if (end<iri.length()-1)
			return prefix + ":" + iri.substring(end + 1);
		else if(end == iri.length()-1)
			return prefix + ":";
		else return iri;

	}

}
