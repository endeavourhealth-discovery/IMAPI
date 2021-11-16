package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.Arrays;
import java.util.Map;
import java.util.zip.DataFormatException;

public class TTToSCG {
	boolean refinedSet;
	public TTIriRef[] corePredicates= {RDF.TYPE,IM.IS_A,IM.HAS_SCHEME,IM.IS_CONTAINED_IN,
	IM.HAS_STATUS,IM.DEFINITIONAL_STATUS};


	public String getSCG(TTEntity entity, Boolean includeName) throws DataFormatException {
		StringBuilder scg = new StringBuilder();
		if (entity.get(IM.IS_A) != null) {
			boolean first = true;
			for (TTValue parent : entity.get(IM.IS_A).asArray().getElements()) {
				if (parent.isIriRef()) {
					if (!first)
						scg.append(" +");
					first = false;
					addClass(parent.asIriRef(), scg, includeName);
				} else
					throw new DataFormatException("ecl not supported with complex superclasses");
			}
		}
		convertRoles(entity,scg,includeName);
		return scg.toString();
	}
	private void convertRoles(TTNode node,StringBuilder scg, boolean includeName) throws DataFormatException {
		if (node.get(IM.ROLE_GROUP)!=null){
			scg.append(":");
			this.refinedSet=true;
			boolean first=true;
			for (TTValue group:node.get(IM.ROLE_GROUP).asArray().getElements()){
				if (!first)
					scg.append(" ,");
				scg.append("{");
				refined(group.asNode(),scg,includeName);
				scg.append("}");
				first = false;
			}
		} else {
			refined(node, scg, includeName);
		}

	}

	private void refined(TTNode node, StringBuilder scg, Boolean includeName) throws DataFormatException {
		boolean first= true;
		for (Map.Entry<TTIriRef,TTValue> entry:node.getPredicateMap().entrySet()){
			if (!excludeCorePredicates(entry.getKey())){
				if (!entry.getValue().isLiteral())
				if (!refinedSet) {
					scg.append(": ");
					refinedSet=true;
				}
				if (!first)
					scg.append(" , ");
				first=false;
				if (entry.getValue().isIriRef()){
					 addClass(entry.getKey(),scg,includeName);
					 scg.append(" = ");
					 addClass(entry.getValue().asIriRef(),scg,includeName);
				} else {
					addClass(entry.getKey(),scg,includeName);
					scg.append("(");
					refined(entry.getValue().asNode(),scg,includeName);
					scg.append(")");
				}


			}


		}
	}

	private boolean excludeCorePredicates(TTIriRef predicate){
		if (Arrays.asList(corePredicates).contains(predicate))
			return true;
		return false;
	}


	private static void addClass(TTIriRef exp, StringBuilder scg, boolean includeName) throws DataFormatException {
		String iri=checkMember(exp.asIriRef().getIri());
		if(includeName){
			scg.append(iri + " |" + exp.asIriRef().getName() +" |");
		} else {
			scg.append(iri);
		}
	}

	private static String checkMember(String iri) throws DataFormatException {
		if (iri.contains("/sct#") || (iri.contains("/im#")))
			return iri.split("#")[1];
		else
			return iri;

	}
	


}
