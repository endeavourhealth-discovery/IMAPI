package org.endeavourhealth.imapi.cdm;


import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

	public static TTIriRef getIriValue(TTNode subject, TTIriRef predicate){
		return subject.get(predicate)==null ?null: subject.get(predicate).asIriRef();

	}
	public static TTIriRef getIriValue(TTNode subject, String imPredicate){
		TTIriRef predicate= TTIriRef.iri(IM.NAMESPACE+imPredicate);
		return subject.get(predicate)==null ?null: subject.get(predicate).asIriRef();

	}
	public static String getStringValue(TTNode subject, TTIriRef predicate){
		return subject.get(predicate)==null ?null: subject.get(predicate).asLiteral().getValue();

	}
	public static String getStringValue(TTNode subject, String imPredicate){
		TTIriRef predicate= TTIriRef.iri(IM.NAMESPACE+imPredicate);
		return subject.get(predicate)==null ?null: subject.get(predicate).asLiteral().getValue();

	}
	public static void setTriple(TTNode subject, String imPredicate, TTValue value){
		subject.set(TTIriRef.iri(IM.NAMESPACE+imPredicate),value);

	}

	public static void setTriple(TTNode subject, String imPredicate, List<TTValue> value){
		for (TTValue entry:value)
		subject.addObject(TTIriRef.iri(IM.NAMESPACE+imPredicate),entry);

	}

	public static List<TTIriRef> getIriList(TTNode subject, String imPredicate){
		TTArray array= subject.get(TTIriRef.iri(IM.NAMESPACE+ imPredicate));
		if (array==null)
			return null;
		List<TTIriRef> list=
		array.getElements().stream().map(e-> e.asIriRef()).collect(Collectors.toList());
		return list;
	}

	public static void addValue(TTNode subject,String imPredicate,TTValue value){
		TTIriRef predicate= TTIriRef.iri(IM.NAMESPACE+imPredicate);
		subject.addObject(predicate,value);
	}

}
