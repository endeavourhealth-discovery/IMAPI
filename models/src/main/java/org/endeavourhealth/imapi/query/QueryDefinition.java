package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The node that defined the query to be run, consisting of a main entity and a set of sequential steps
 * <p>Query steps run sequentially. Branching logic such as rules are performed by the steps which access outputs from other steps via the step variable name</p>
 */
public class QueryDefinition extends TTNode {

	/**
	 * Queries should have a main entity iri which is used to ensure that all resources are related in some way to the main entity
	 * <p> Typically this may be a Patient but might be events or organisations or locations (households) </p>
	 * @param mainEntity the iri of the main entity e.g. http://endhealth.info/im#Patient
	 * @return the query definition as amended
	 */
	public QueryDefinition setMainEntity(TTIriRef mainEntity){
		this.set(IM.MAIN_ENTITY,mainEntity);
		return this;
	}

	/**
	 * Queries should have a main entity iri which is used to ensure that all resources are related in some way to the main entity
	 * <p> Typically this may be a Patient but might be events or organisations or locations (households) </p>
	 * @param mainEntity the short name  of the main entity which is resolved via the graph IRI of the data model as a static set in the main Query
	 * @return the query definition as amended
	 */
	public QueryDefinition setMainEntity(String mainEntity){
		this.set(IM.MAIN_ENTITY,TTIriRef.iri(Query.dataModelGraph+mainEntity));
		return this;
	}

	/**
	 * gets the main entity associated with the query definition
	 * @return The iri of the main entity
	 */
	public TTIriRef getMainEntity(){
		return get(IM.MAIN_ENTITY).asIriRef();
	}

	public QueryStep addStep(){
		QueryStep step = new QueryStep();
		if (this.get(IM.QUERY_STEP)==null)
			step.set(IM.SEQUENCE_NUMBER, TTLiteral.literal(1));
		else
			step.set(IM.SEQUENCE_NUMBER, TTLiteral.literal(this.get(IM.QUERY_STEP).size()+1));
		this.addObject(IM.QUERY_STEP,step);
		return step;
	}
	public QueryStep insertStep(int insertBefore){
		QueryStep step = new QueryStep();
		if (this.get(IM.QUERY_STEP)==null)
			return addStep();
		else {
			for (TTValue value:this.get(IM.QUERY_STEP).getElements())
			{
				Integer sequence= value.asNode().get(IM.SEQUENCE_NUMBER).asLiteral().intValue();
				if (!(sequence<insertBefore)){
					value.asNode().set(IM.SEQUENCE_NUMBER,TTLiteral.literal(sequence+1));
				}
			}
		}
		step.set(IM.SEQUENCE_NUMBER, TTLiteral.literal(insertBefore-1));
		this.addObject(IM.QUERY_STEP,step);
		return step;
	}

	public List<QueryStep> getSteps(){
		if (this.get(IM.QUERY_STEP)==null)
			return null;
		return this.get(IM.QUERY_STEP).getElements().stream()
			.map(v-> (QueryStep) v )
			.collect(Collectors.toList());
	}

}
