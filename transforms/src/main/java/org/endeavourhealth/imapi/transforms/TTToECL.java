package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.Map;
import java.util.zip.DataFormatException;

public class TTToECL {

	/**
	 * Takes a TTNode description logic definition of a set and returns in ECL language
	 * @param exp a node representing a class expression e.g. value of im:Definition
	 * @param includeName flag to include the concept term in the output
	 * @return String of ECL
	 * @throws DataFormatException invalid or unsupported ECL syntax
	 */
	public static String getExpressionConstraint(TTArray exp, Boolean includeName) throws DataFormatException {
		StringBuilder ecl = new StringBuilder();

		subExpression(exp,ecl,includeName);
		return ecl.toString();
	}

    public static String getExpressionConstraint(TTValue exp, Boolean includeName) throws DataFormatException {
        StringBuilder ecl = new StringBuilder();

        subExpression(exp,ecl,includeName);
        return ecl.toString();
    }

    private static void subExpression(TTArray exp,StringBuilder ecl,Boolean includeName) throws DataFormatException {
        for (TTValue subExp:exp.iterator()){
            subExpression(subExp,ecl,includeName);
        }
    }

	private static void subExpression(TTValue exp,StringBuilder ecl,Boolean includeName) throws DataFormatException {
		if (exp.isIriRef()){
			addClass(exp.asIriRef(),ecl,includeName);
		} else if (exp.isNode()) {
			if (exp.asNode().get(SHACL.OR) != null) {
				addDisjunction(exp.asNode(),ecl, includeName);
			} else if (exp.asNode().get(SHACL.AND) != null) {
				addConjunction(exp.asNode(),ecl, includeName);
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
				ecl.append("(*");
				addRefined(exp.asNode(), ecl, includeName);
				ecl.append(")");
			}
		}

	}




	private static void addDisjunction(TTNode exp, StringBuilder ecl, Boolean includeName) throws DataFormatException {
		boolean first = true;
		String or = " OR ";

		for (TTValue member : exp.asNode().get(SHACL.OR).iterator()) {
			if (!first)
				ecl.append(or).append("\n");;
			if (member.isIriRef()) {
				addClass(member.asIriRef(), ecl, includeName);
			} else if (member.asNode().get(SHACL.NOT) != null) {
				ecl.append(" MINUS ");
				ecl.append("(");
				subExpression(member.asNode().get(SHACL.NOT), ecl, includeName);
				ecl.append(")");
			} else if (member.asNode().get(SHACL.AND) != null) {
				ecl.append("(");
				addConjunction(member.asNode(),ecl,includeName);
				ecl.append(")");
			} else if (member.asNode().get(SHACL.OR) != null) {
				ecl.append("(");
				addDisjunction(member.asNode(),ecl,includeName);
				ecl.append(")");
			} else {
				ecl.append("(*");
				addRefined(member.asNode(), ecl, includeName);
				ecl.append(")");
			}
			first = false;
		}
	}

	private static void addConjunction(TTNode exp, StringBuilder ecl, Boolean includeName) throws DataFormatException {
		boolean first = true;

		for (TTValue member : exp.asNode().get(SHACL.AND).getElements()) {
			if (member.isIriRef()) {
				if (!first)
					ecl.append(" AND ");
				first = false;
				addClass(member.asIriRef(), ecl, includeName);
			}
		}
		first=true;
		for (TTValue member : exp.asNode().get(SHACL.AND).getElements()) {
			if (!member.isIriRef()) {
				if (member.asNode().get(SHACL.NOT) != null) {
					ecl.append(" MINUS ");
					ecl.append("(");
					subExpression(member.asNode().get(SHACL.NOT), ecl, includeName);
					ecl.append(")");
				} else if (member.asNode().get(SHACL.AND) != null) {
					ecl.append("(");
					addConjunction(member.asNode(), ecl, includeName);
					ecl.append(")");
				} else if (member.asNode().get(SHACL.OR) != null) {
					if (!first)
						ecl.append(" AND ");
					ecl.append("(");
					addDisjunction(member.asNode(), ecl, includeName);
					ecl.append(")");
				} else {
					addRefined(member.asNode(), ecl, includeName);
				}
				first = false;
			}
		}
	}



	private static void addRefined(TTNode exp, StringBuilder ecl, Boolean includeName) throws DataFormatException {

		if (exp.getPredicateMap()!=null){
				ecl.append(" : ");
			boolean first=true;
			for (Map.Entry<TTIriRef,TTArray> entry:exp.getPredicateMap().entrySet()){
				if (!first)
					ecl.append(" , ");
				first=false;
				if (entry.getValue().isIriRef()) {
					addProperty(entry.getKey(),ecl,includeName);
					ecl.append(" = ");
					addValue(entry.getValue().asIriRef(),ecl,includeName);
				} else if (entry.getKey().equals(IM.ROLE_GROUP)){
						ecl.append(" {");
						addRefined(entry.getValue().asNode(),ecl,includeName);
						ecl.append("}");
				} else
					throw new DataFormatException("Unknown expression constraint format. Expecting role group");
			}
		}
	}

	private static void addClass(TTIriRef exp,StringBuilder ecl,boolean includeName) {
		String iri=checkMember(exp.asIriRef().getIri());
		String sameOrSub= "<<";
		String pipe=" | ";
		if(includeName){
			ecl.append(sameOrSub).append(iri).append(pipe).append(exp.asIriRef().getName()).append(pipe);
		} else {
			ecl.append(sameOrSub).append(iri);
		}
	}

	private static void addProperty(TTIriRef exp,StringBuilder ecl,boolean includeName)  {
		String iri=checkMember(exp.asIriRef().getIri());
		if(includeName){
			ecl.append("<<").append(iri).append(" | ").append(exp.asIriRef().getName()).append(" | ");
		} else {
			ecl.append("<<").append(iri);
		}
	}
	private static void addValue(TTIriRef exp,StringBuilder ecl,boolean includeName) {
		String iri=checkMember(exp.asIriRef().getIri());
		if(includeName){
			ecl.append("<<").append(iri).append(" | ").append(exp.asIriRef().getName()).append(" | ");
		} else {
			ecl.append("<<").append(iri);
		}
	}

	private static String checkMember(String iri) {
		if (iri.contains("/sct#") || (iri.contains("/im#")))
			return iri.split("#")[1];
		else
				return iri;

	}
}

