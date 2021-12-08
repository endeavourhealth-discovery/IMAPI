package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.Map;
import java.util.zip.DataFormatException;

public class TTToECL {

	public static String getExpressionConstraint(TTValue exp, Boolean includeName) throws DataFormatException {
		StringBuilder ecl = new StringBuilder();
		subExpression(exp,ecl,includeName);
		return ecl.toString();
	}


	private static void subExpression(TTValue exp,StringBuilder ecl,Boolean includeName) throws DataFormatException {
		if (exp.isIriRef()){
			addClass(exp.asIriRef(),ecl,includeName);
		} else if (exp.isNode()) {
			if (exp.asNode().get(SHACL.OR) != null) {
				addJunction(exp.asNode(), SHACL.OR,ecl, includeName);
			} else if (exp.asNode().get(SHACL.AND) != null) {
				addJunction(exp.asNode(), SHACL.AND,ecl, includeName);
			} else if (exp.asNode().get(SHACL.NOT)!=null) {
				ecl.append(" MINUS ");
				if (!exp.asNode().get(SHACL.NOT).isIriRef())
					ecl.append("(");
				subExpression(exp.asNode().get(SHACL.NOT),ecl,includeName);
				if (!exp.asNode().get(SHACL.NOT).isIriRef())
				ecl.append(")");
			} else if (exp.asNode().get(IM.DEFINITION) != null) {
				subExpression(exp.asNode().get(IM.DEFINITION), ecl, includeName);
			}
			else {
				addRefined(exp.asNode(), ecl, includeName);
			}
		} else {
			for (TTValue subExp:exp.asArray().getElements()){
				subExpression(subExp,ecl,includeName);
			}
		}

	}


	private static void addJunction(TTNode exp, TTIriRef junction,StringBuilder ecl, Boolean includeName) throws DataFormatException {
		boolean first = true;
		for (TTValue member : exp.asNode().get(junction).asArray().getElements()) {
			if (member.isIriRef()) {
				if (!first)
					ecl.append((junction.equals(SHACL.OR) ? (" OR\n") : " AND\n"));
				addClass(member.asIriRef(), ecl, includeName);
			} else if (member.asNode().get(SHACL.NOT) != null) {
				ecl.append(" MINUS ");
				subExpression(member.asNode().get(SHACL.NOT), ecl, includeName);
			} else if (member.asNode().get(SHACL.AND)!=null){
				if (!first)
					ecl.append((junction.equals(SHACL.OR) ? (" OR\n") : " AND\n"));
				ecl.append("(");
				subExpression(member.asNode().get(SHACL.AND), ecl, includeName);
				ecl.append(")");
			}  else if (member.asNode().get(SHACL.OR)!=null) {
				if (!first)
					ecl.append((junction.equals(SHACL.OR) ? (" OR\n") : " AND\n"));
				ecl.append("(");
				subExpression(member.asNode().get(SHACL.OR), ecl, includeName);
				ecl.append(")");
			}
			else {
				addRefined(member.asNode(), ecl, includeName);
			}
			first = false;
		}
	}




	private static void addRefined(TTNode exp, StringBuilder ecl, Boolean includeName) throws DataFormatException {

		if (exp.getPredicateMap()!=null){
			ecl.append(" : ");
			boolean first=true;
			for (Map.Entry<TTIriRef,TTValue> entry:exp.getPredicateMap().entrySet()){
				if (!first)
					ecl.append(" , ");
				first=false;
				if (entry.getValue().isIriRef()) {
					addClass(entry.getKey(),ecl,includeName);
					ecl.append(" = ");
					addClass(entry.getValue().asIriRef(),ecl,includeName);
				} else if (entry.getKey().equals(IM.ROLE_GROUP)){
						ecl.append(" {");
						addRefined(entry.getValue().asNode(),ecl,includeName);
						ecl.append("}");
				} else
					throw new DataFormatException("Unknown expression constraint format. Expecting role group");
			}
		}
	}

	private static void addClass(TTIriRef exp,StringBuilder ecl,boolean includeName) throws DataFormatException {
		String iri=checkMember(exp.asIriRef().getIri());
		if(includeName){
			ecl.append("<< " + iri + " | " + exp.asIriRef().getName()+" |");
		} else {
			ecl.append("<< " + iri);
		}
	}

	private static String checkMember(String iri) throws DataFormatException {
		if (iri.contains("/sct#") || (iri.contains("/im#")))
			return iri.split("#")[1];
		else
				return iri;

	}
}

