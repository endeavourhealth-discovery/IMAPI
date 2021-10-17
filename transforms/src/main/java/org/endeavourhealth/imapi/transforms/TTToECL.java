package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;

import java.util.Map;
import java.util.zip.DataFormatException;

public class TTToECL {

	public static String getConceptSetECL(TTEntity entity, TTDocument document, Boolean includeName) throws DataFormatException {
		StringBuilder ecl = new StringBuilder();
		if (entity.get(IM.NOT_MEMBER) != null)
			exclusionExpression(entity, ecl,includeName);
		else if (entity.get(IM.HAS_SUBSET) != null)
			subExpression(entity, IM.HAS_SUBSET, ecl,includeName);
		else
			subExpression(entity, IM.HAS_MEMBER, ecl,includeName);
		return ecl.toString();
	}

	private static void exclusionExpression(TTEntity entity, StringBuilder ecl,Boolean includeName) throws DataFormatException {
		if (entity.get(IM.HAS_MEMBER).asArray().size()>1)
			ecl.append("(");
		subExpression(entity,IM.HAS_MEMBER,ecl,includeName);
		if (entity.get(IM.HAS_MEMBER).asArray().size()>1)
			ecl.append(")");
		ecl.append(" MINUS ");
		if (entity.get(IM.NOT_MEMBER).asArray().size()>1)
			ecl.append("( ");
		subExpression(entity,IM.NOT_MEMBER,ecl,includeName);
		if (entity.get(IM.NOT_MEMBER).asArray().size()>1)
			ecl.append(" )");
	}
	private static void subExpression(TTEntity entity,TTIriRef memberPredicate,StringBuilder ecl,Boolean includeName) throws DataFormatException {
		String iri;
		if (entity.get(memberPredicate) != null) {
				boolean first = true;
				for (TTValue member : entity.get(memberPredicate).asArray().getElements()) {
					if (!first)
						ecl.append(" OR ");
					first = false;
					if (member.isIriRef()) {
						iri=checkMember(member.asIriRef().getIri());
						if(includeName){
							ecl.append("<<" + iri + " | " + member.asIriRef().getName());
						} else {
							ecl.append("<<" + iri);
						}
					} else {
						ecl.append("(");
						TTNode expression = member.asNode();
						if (expression.get(OWL.INTERSECTIONOF) != null) {
							boolean intFirst=true;
							for (TTValue inter : expression.get(OWL.INTERSECTIONOF).asArray().getElements()) {
								if (inter.isIriRef()) {
									if (!intFirst)
										ecl.append(" + ");
									iri = checkMember(inter.asIriRef().getIri());
									if (includeName) {
										ecl.append("<<" + iri + " | " + inter.asIriRef().getName());
									} else {
										ecl.append("<<" + iri);
									}
								}
							}
							intFirst=true;
							for (TTValue inter : expression.get(OWL.INTERSECTIONOF).asArray().getElements()) {
								if (!inter.isIriRef()) {
									if (!intFirst)
										ecl.append(" , ");
									intFirst=false;
									for (Map.Entry<TTIriRef, TTValue> entry : inter.asNode().getPredicateMap().entrySet()) {
										String predIri= checkMember(entry.getKey().getIri());
										iri= checkMember(entry.getValue().asIriRef().getIri());
										if(includeName){
											ecl.append(" : <<" + predIri + " | " + entry.getKey().getName());
											ecl.append(" = <<" + iri + " | " + entry.getValue().asIriRef().getName());
										}else {
											ecl.append(" : <<" + predIri);
											ecl.append(" = <<" + iri);
										}
									}
								}
							}
							ecl.append(" )");
						}

					}

				}
			}
	}

	private static String checkMember(String iri) throws DataFormatException {
		if (iri.contains("/sct#")|(iri.contains("/im#")))
			return iri.split("#")[1];
		else
				return iri;

	}
}

