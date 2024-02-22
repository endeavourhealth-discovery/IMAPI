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
		if (query.getMatch()!=null) {
			match(query, ecl, includeName);
		}
		return ecl.toString().trim();
	}

	private static eclType getEclType(Match match){
		if (match.getProperty()!=null)
			return eclType.refined;
		if (match.getMatch()!=null) {
			for (Match subMatch : match.getMatch()) {
				if (subMatch.isExclude())
					return eclType.exclusion;
			}
			return eclType.compound;
		}
		return eclType.simple;
	}

	public static String getECLFromQuery(Query query) throws QueryException {
		return getECLFromQuery(query,false);
	}

	private static boolean isList(Match match){
		return null != match.getMatch();
	}


	private static void match(Match match, StringBuilder ecl, boolean includeNames) throws QueryException {
		eclType matchType= getEclType(match);
		if (matchType==eclType.simple){
			addClass(match.getInstanceOf(), ecl, includeNames);
		}
		else if (matchType== eclType.refined){
			if (match.getInstanceOf()!=null)
				addClass(match.getInstanceOf(), ecl, includeNames);
			else
				ecl.append("*");
			addRefinements(match, ecl, includeNames);
		}
		else if (matchType==eclType.compound){
			boolean first = true;
			for (Match subMatch : match.getMatch()) {
				eclType subMatchType=getEclType(subMatch);
				boolean bracket= (match.getMatch().size()>1&&subMatchType== eclType.refined) ?true: false;
				if (!first){
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
			}
		}
	}

	private static boolean needsBracket(Match parent,Match match){
		if (parent.getMatch().size()>1) {
			if (match.getProperty() != null)
				return true;
		}
		if (match.isExclude())
				return true;
		return false;
	}


	private static void addRefinements(Match match, StringBuilder ecl, boolean includeNames) throws QueryException {
		ecl.append(": ");
		boolean first = true;
		if (null != match.getProperty().get(0).getIri() && match.getProperty().get(0).getIri().equals(IM.ROLE_GROUP)){
			ecl.append(" { ");
			addRefinements(match.getProperty().get(0).getMatch(),ecl,includeNames);
			ecl.append("}");
		}
		else {
			if (match.getBool()==null){
				match.setBool(Bool.and);
			}
			for (Property property : match.getProperty()) {
				if (!first) {
					if (match.getBool()==Bool.and) {
						ecl.append(" , ");
					}
					else
						ecl.append(" OR ");
				}
				first = false;
				addRefinedGroup(property, ecl, includeNames);
			}
		}
	}

	private static void addRefinements(Property property, StringBuilder ecl, boolean includeNames) throws QueryException {
		boolean first = true;
		if (null != property.getProperty().get(0).getIri() && property.getProperty().get(0).getIri().equals(IM.ROLE_GROUP)){
			ecl.append(" { ");
			addRefinements(property.getProperty().get(0).getMatch(),ecl,includeNames);
			ecl.append("}");
		}
		else {
			for (Property subProperty : property.getProperty()) {
				if (!first) {
					if (property.getBool()==Bool.or){
						ecl.append(" OR ");
					}
					else
						ecl.append(" , ");
				}
				first = false;
				if (subProperty.getProperty()!=null) {
					ecl.append(" (");
				}
				addRefinedGroup(subProperty, ecl, includeNames);
				if (subProperty.getProperty()!=null){
					ecl.append(")");
				}
			}

		}
	}

	private static void addRefinedGroup(Property property, StringBuilder ecl, Boolean includeName) throws QueryException {
		if (null == property.getMatch()) {
			addRefined(property, ecl, includeName);
		}
		else {
			addProperty(property, ecl, includeName);
			ecl.append(" = (");
			match(property.getMatch(), ecl, includeName);
			ecl.append(")");
		}
	}


	private static void addRefined(Property where, StringBuilder ecl, Boolean includeName) throws QueryException {
		try {
			if (null == where.getBool()) {
				addProperty(where, ecl, includeName);
				ecl.append(" = ");
				if (null == where.getIs()) throw new QueryException("Property clause must contain 'is' value");
				Node value = where.getIs().get(0);
				addClass(value, ecl, includeName);
			} else {
				addRefinements(where,ecl,includeName);
			}
		} catch (Exception e) {
			throw new QueryException("Property clause inside a role group clause must contain a property");
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
