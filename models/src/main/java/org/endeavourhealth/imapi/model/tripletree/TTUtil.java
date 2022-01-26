package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Static methods for casting TT classes to business objects for use in builders
 */
public class TTUtil {
	public static Object get(TTNode node,TTIriRef predicate,Class clazz) {
			if (node.get(predicate) == null)
				return null;
			TTArray value = node.get(predicate);
			if (value.isIriRef())
				return clazz.cast(value.asIriRef());
			else
				if (value.isLiteral())
					return clazz.cast(value.asLiteral().getValue());
				else
				  return clazz.cast(value.asNode());
	}

	public static void add(TTNode node, TTIriRef predicate, TTValue value){
		if (value.isIriRef()| value.isLiteral())
			node.addObject(predicate,value);
		else {
			int order=0;
			if (node.get(predicate)!=null)
				order= node.get(predicate).size();
			value.asNode().set(IM.ORDER,TTLiteral.literal(order));
			node.addObject(predicate,value);

		}

	}


	public static <T> List<T> getList(TTNode node, TTIriRef predicate,Class clazz){
		if (node.get(predicate)==null)
			return null;
		List<T> result= new ArrayList();
		boolean areNodes=false;
		for (TTValue v:node.get(predicate).getElements()){
			if (v.isIriRef())
				result.add((T) v);
			else if (v.isLiteral())
				result.add((T) v.asLiteral());
			else {
				result.add((T) clazz.cast(v.asNode()));
			}
		}
		return result;
	}

	public static <T> List<T> getOrderedList(TTNode node,TTIriRef predicate,Class clazz){
		List<T> result = getList(node,predicate,clazz);
		try {
			Collections.sort(result, Comparator.comparing(o -> ((TTNode) o).get(IM.ORDER).asLiteral().intValue()));
			return result;
		} catch (Exception e) {
			return result;
		}
	}
	public static TTContext getDefaultContext(){
		TTContext ctx=new TTContext();
		ctx.add(IM.NAMESPACE,"");
		ctx.add(RDFS.NAMESPACE,"rdfs");
		ctx.add(RDF.NAMESPACE,"rdf");
		ctx.add(SNOMED.NAMESPACE,"sn");
		return ctx;
	}

	/**
	 * Wraps a predicates object node into a json literal
	 * @param node the node whose predicate needs wrapping
	 * @param predicate the predicate whose object needs wrapping
	 * @return the node wrapped
	 * @throws JsonProcessingException
	 */
	public static TTNode wrapRDFAsJson(TTNode node,TTIriRef predicate) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		String json = objectMapper.writeValueAsString(node.get(predicate).asNode());
		node.set(predicate, TTLiteral.literal(json));
		return node;
	}

	public static TTNode unwrapRDFfromJson(TTNode node, TTIriRef predicate) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		TTNode result = objectMapper.readValue(node.get(predicate).asLiteral().getValue(), TTDocument.class);
		return result;
	}
	public static void populate (TTNode source, TTNode target){

	}
}
