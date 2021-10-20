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
			if (!first)
				ecl.append((junction.equals(SHACL.OR) ? (" OR ") : " AND "));
			first = false;
			if (member.isIriRef()) {
				addClass(member.asIriRef(), ecl, includeName);
			} else if (member.asNode().get(SHACL.NOT) != null) {
				ecl.append(" MINUS ");
				subExpression(member.asNode().get(SHACL.NOT), ecl, includeName);
			} else if (member.asNode().get(SHACL.AND)!=null){
				ecl.append("(");
				subExpression(member.asNode().get(SHACL.AND), ecl, includeName);
				ecl.append(")");
			}  else if (member.asNode().get(SHACL.OR)!=null) {
				ecl.append("(");
				subExpression(member.asNode().get(SHACL.OR), ecl, includeName);
				ecl.append(")");
			}
			else {
				ecl.append("(");
				addRefined(member.asNode(), ecl, includeName);
				ecl.append(")");
			}
		}
	}




	private static void addRefined(TTNode exp, StringBuilder ecl, Boolean includeName) throws DataFormatException {
		if (exp.get(RDFS.SUBCLASSOF)!=null){
			boolean first=true;
			for (TTValue superClass:exp.get(RDFS.SUBCLASSOF).asArray().getElements()){
				if (!first) {
					if (superClass.isIriRef())
						ecl.append(" + ");
				}
				first=false;
				subExpression(superClass,ecl,includeName);
			}
		}
		if (exp.get(IM.PROPERTY_GROUP)!=null){
			ecl.append(" : ");
			Integer groupCount= exp.get(IM.PROPERTY_GROUP).asArray().size();
			boolean first=true;
			for (TTValue group:exp.get(IM.PROPERTY_GROUP).asArray().getElements()){
				if (!first)
					ecl.append(" , ");
				first=false;
				if (groupCount>1){
					ecl.append(" {");
				}
				addAttributeSet(group.asNode(),ecl,includeName);
				if (groupCount>1)
					ecl.append("}");
			}
		}
	}

	private static void addAttributeSet(TTNode group, StringBuilder ecl, Boolean includeName) throws DataFormatException {
		if (group.get(SHACL.PROPERTY)!=null){
			for (TTValue property:group.get(SHACL.PROPERTY).asArray().getElements()){
				if (property.isNode()){
					if (property.asNode().get(SHACL.PATH)!=null){
						addClass(property.asNode().get(SHACL.PATH).asIriRef(),ecl,includeName);
						ecl.append(" = ");
						if (property.asNode().get(SHACL.CLASS)!=null){
							if (property.asNode().get(SHACL.CLASS).isIriRef()) {
								addClass(property.asNode().get(SHACL.CLASS).asIriRef(), ecl, includeName);
							} else
								throw new DataFormatException("Attribute value type not suitable for ecl conversion");
						} else
							throw new DataFormatException("Invalid attribute refinement for ecl conversion");

					} else
						throw new DataFormatException("Property group not suited to ecl conversion");
				}

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
		if (iri.contains("/sct#")|(iri.contains("/im#")))
			return iri.split("#")[1];
		else
				return iri;

	}
}

