package org.endeavourhealth.imapi.model.tripletree;

import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;

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
		if (!value.isIriRef() && !value.isLiteral()) {
			int order=0;
			if (node.get(predicate)!=null)
				order= node.get(predicate).size();
			value.asNode().set(IM.ORDER,TTLiteral.literal(order));

		}
		node.addObject(predicate,value);

	}

	public static <T> List<T> getList(TTNode node, TTIriRef predicate,Class clazz){
		if (node.get(predicate)==null)
			return null;
		List<T> result= new ArrayList();
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

}
