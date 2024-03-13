package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.zip.DataFormatException;

public class IMLToECL {
	private IMLToECL() {
		throw new IllegalStateException("Utility class");
	}

	private enum eclType {
		exclusion,
		refined,
		compound,
		compoundRefined,
		simple
	}
	/**
	 * Takes a IM ECL compliant definition of a set and returns is ECL language
	 * @param query         a node representing a class expression e.g. value of im:Definition
	 * @param includeName flag to include the concept term is the output
	 * @return String of ECL
	 * @throws DataFormatException invalid or unsupported ECL syntax
	 */
	public static String getECLFromQuery(Query query, Boolean includeName) throws QueryException {
		StringBuilder ecl = new StringBuilder();
		match(query, ecl, includeName);
		return ecl.toString().trim();
	}

	private static eclType getEclType(Match match){
		if (match.getMatch()!=null){
			if (match.getMatch().size()>0) {
				if (match.getWhere()!=null)
					return eclType.compoundRefined;
			}
		}
		if (match.getWhere()!=null)
			return eclType.refined;
		if (match.getMatch()!=null) {
			for (Match subMatch : match.getMatch()) {
				if (subMatch.isExclude())
					return eclType.exclusion;
			}
			return eclType.compound;
		}
		if (match.getInstanceOf()!=null)
			return eclType.simple;
		else return null;
	}

	public static String getECLFromQuery(Query query) throws QueryException {
		return getECLFromQuery(query,false);
	}

	private static boolean isList(Match match){
		return null != match.getMatch();
	}


	private static void match(Match match, StringBuilder ecl, boolean includeNames) throws QueryException {
		eclType matchType= getEclType(match);
		if (matchType==null)
			return;
		if (matchType==eclType.simple){
			addClass(match.getInstanceOf(), ecl, includeNames);
		}
		else if (matchType== eclType.refined){
			if (match.getInstanceOf()!=null)
				addClass(match.getInstanceOf(), ecl, includeNames);
			else
				ecl.append("*");
			addRefinementsToMatch(match, ecl, includeNames);
			ecl.append("\n");
		}
		else if (matchType==eclType.compound||matchType==eclType.compoundRefined){
			if (matchType== eclType.compoundRefined)
				ecl.append("(");
			boolean first = true;
			for (Match subMatch : match.getMatch()) {
				eclType subMatchType=getEclType(subMatch);
				boolean bracket= (match.getMatch().size()>1&&subMatchType== eclType.refined) ?true: false;
				if (!first){
					ecl.append("\n");
					if (match.getBool()==Bool.or){
						ecl.append(" OR ");
					}
					else
						ecl.append(" AND ");
				}
				if (bracket)
					ecl.append("(");
				match(subMatch, ecl, includeNames);
				if (bracket)
					ecl.append(")");
				first = false;
			}
			if (matchType== eclType.compoundRefined) {
				ecl.append(")");
				addRefinementsToMatch(match, ecl, includeNames);
			}
			ecl.append("\n");
		}
		else {
			boolean first = true;
			ecl.append("(");
			for (Match subMatch : match.getMatch()) {
				if (subMatch.isExclude()){
					ecl.append(")");
					ecl.append(" MINUS ");
				}
				match(subMatch,ecl,includeNames);
				ecl.append("\n");
			}
		}
	}



	private static void addRefinementsToMatch(Match match, StringBuilder ecl, boolean includeNames) throws QueryException {
		ecl.append(": ");
		boolean first = true;
		for (Where where:match.getWhere()){
			if (!first){
				ecl.append("\n AND ");
			}
			first= false;
			if (null != where.getIri() && where.getIri().equals(IM.ROLE_GROUP)){
				ecl.append(" { ");
				addRefinementsToMatch(where.getMatch(),ecl,includeNames);
				ecl.append("}");
			}
			else {
				addRefined(where,ecl,includeNames);
			}
		}
	}

	private static void addRefinementsToWhere(Where property, StringBuilder ecl, boolean includeNames) throws QueryException {
		if (null != property.getWhere().get(0).getIri() && property.getWhere().get(0).getIri().equals(IM.ROLE_GROUP)){
			ecl.append(" { ");
				addRefinementsToMatch(property.getWhere().get(0).getMatch(),ecl,includeNames);
			ecl.append("}");
		}
		else {
			ecl.append("(");
			boolean first = true;
			for (Where subProperty : property.getWhere()) {
				if (!first) {
					ecl.append("\n");
					if (property.getBool()==Bool.or){
						ecl.append(" OR ");
					}
					else
						ecl.append(" , ");
				}
				first = false;
				addRefined(subProperty, ecl, includeNames);
				}
			ecl.append(")");
			}
	}



	private static void addRefined(Where where, StringBuilder ecl, Boolean includeName) throws QueryException {
		try {
			if (null == where.getWhere()) {
				addProperty(where, ecl, includeName);
				if (where.getMatch()!=null){
					ecl.append(" = (");
					match(where.getMatch(), ecl, includeName);
					ecl.append(")");
				}
				else {
					ecl.append(" = ");
					if (null == where.getIs()) throw new QueryException("Where clause must contain 'is' value");
					Node value = where.getIs().get(0);
					addClass(value, ecl, includeName);
				}
			}
			else {
				addRefinementsToWhere(where,ecl,includeName);
			}
		} catch (Exception e) {
			throw new QueryException("Where clause inside a role group clause must contain a where");
		}
	}


	private static void addProperty(PropertyRef exp, StringBuilder ecl, boolean includeName) {
		addConcept(ecl, includeName, getSubsumption(exp), exp.getIri(), exp.getName());
	}

	private static void addConcept(StringBuilder ecl, boolean includeName, String subsumption2, String id, String name2) {
		String subsumption = subsumption2;
		String iri = checkMember(id);
		String pipe = " | ";
		if (includeName && null!= name2) {
			ecl.append(subsumption).append(iri).append(pipe).append(name2).append(pipe);
		}
		else if (includeName){
			EntityRepository entityRepository = new EntityRepository();
			String name = entityRepository.getEntityReferenceByIri(id).getName();
			ecl.append(subsumption).append(iri).append(pipe).append(name).append(pipe);
		}
		else
			ecl.append(subsumption).append(iri).append(" ");
	}

	private static void addClass(Node exp, StringBuilder ecl, boolean includeName) {
			addConcept(ecl, includeName, getSubsumption(exp), exp.getIri(), exp.getName());
	}


	private static String getSubsumption(Entailment exp) {
        if (exp == null)
            return "";

		String subsumption="";
		if (exp.isDescendantsOrSelfOf())
			subsumption="<< ";
		if (exp.isDescendantsOf())
			subsumption="< ";
		return subsumption;
	}


	private static String checkMember(String iri) {
		if (iri.contains("/sct#") || (iri.contains("/im#")))
			return iri.split("#")[1];
		else
			return iri;

	}

	/**
	 * Gets simple iri set as ECL for compatibility with definition
	 *
	 * @param members a TTArray or TTNNode containing an iri set
	 * @return ECL String
	 */

	public static String getMembersAsECL(TTArray members) {
		StringBuilder ecl = new StringBuilder();
		boolean first = true;
		String or = " OR ";
		for (TTValue iriRef : members.getElements()) {
			if (!first)
				ecl.append(or).append("\n");
			addClass(new Node().setIri(iriRef.asIriRef().getIri()), ecl, true);
			first = false;
		}
		return ecl.toString();
	}

}
