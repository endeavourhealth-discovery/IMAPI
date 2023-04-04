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

	/**
	 * Takes a IM ECL compliant definition of a set and returns in ECL language
	 * @param query         a node representing a class expression e.g. value of im:Definition
	 * @param includeName flag to include the concept term in the output
	 * @return String of ECL
	 * @throws DataFormatException invalid or unsupported ECL syntax
	 */
	public static String getECLFromQuery(Query query, Boolean includeName) throws DataFormatException {
		StringBuilder ecl = new StringBuilder();
		if (query.getMatch()!=null) {
			boolean first = true;
			for (Match match : query.getMatch()) {
				if (!first)
					ecl.append(" AND ");
				first = false;
				fromWhere(match, ecl, includeName);
			}
		}
		return ecl.toString();
	}

	private static boolean isList(Match match){
		return null != match.getMatch();
	}


	private static void fromWhere(Match matchWhere, StringBuilder ecl, Boolean includeName) throws DataFormatException {
		if (null!= matchWhere) {
			if (matchWhere.isExclude())
				ecl.append(" MINUS (");
			from(matchWhere, ecl, includeName, null != matchWhere.getWhere());
			if (matchWhere.isExclude())
				ecl.append(")");
		}
	}
	private static void from(Match match, StringBuilder ecl, boolean includeName, boolean isRefined) throws DataFormatException {
		if (null != match.getId()) {
			addClass(match, ecl, includeName);
		} else if (null != match.getMatch()) {
			boolean bracketFrom = isRefined;
			if (bracketFrom)
				ecl.append("(");

			boolean first = true;
			for (Match subMatch : match.getMatch()) {
				fromAppendBracket(match, ecl, includeName, first, subMatch);
				first = false;
			}
			if (bracketFrom) {
				ecl.append(")");
			}
		}
		if (null != match.getWhere()) {
			if (null == match.getId() && null == match.getMatch())
				ecl.append("*");
			addFromRefinements(match, ecl, includeName);
		}
	}

	private static void fromAppendBracket(Match match, StringBuilder ecl, boolean includeName, boolean first, Match subMatch) throws DataFormatException {
		if (subMatch.isExclude()) {
			ecl.append(" MINUS ");
		}
		else {
			fromAppendBool(match, ecl, first);
		}
		boolean bracket = false;
		if (null != subMatch.getWhere() && isList(match)) {
			bracket = true;
		}
		if (null != subMatch.getMatch() && subMatch.getMatch().size() > 1) {
			bracket = true;
		}
		if (subMatch.isExclude() && null != subMatch.getMatch() && subMatch.getMatch().size() > 1)
			bracket = true;

		if (bracket)
			ecl.append("(");
		from(subMatch, ecl, includeName, null != subMatch.getWhere());
		if (bracket) {
			ecl.append(")\n");
		}
	}

	private static void fromAppendBool(Match match, StringBuilder ecl, boolean first) {
		if (!first) {
			if (match.getBoolMatch() == Bool.and) {
				ecl.append(" AND ");
			} else if (match.getBoolMatch() == Bool.or) {
				ecl.append("  OR ");
			} else
				ecl.append(" OR ");
		}
	}

	private static void addFromRefinements(Match match, StringBuilder ecl, boolean includeNames) throws DataFormatException {
		ecl.append(": ");
		boolean first = true;
		if (match.getPath()!=null){
			ecl.append(" { ");
		}
		for (Where where : match.getWhere()) {
			if (!first)
				ecl.append(" , ");
			first = false;
			addRefinedGroup(where,ecl,includeNames);
		}
		if (match.getPath()!=null)
			ecl.append("}");
	}



	private static void addRefinedGroup(Where where,StringBuilder ecl,Boolean includeName) throws DataFormatException {
		if (null == where.getWhere()) {
			addRefined(where, ecl, includeName);
		}
		else {
			if (where.getWhere().size() == 1) {
				addRefinedGroup(where.getWhere().get(0), ecl, includeName);
			}
			else for (int i = 0; i < where.getWhere().size(); i++) {
				addRefinedGroupAppendBool(where, ecl, includeName, i);
			}
		}
	}

	private static void addRefinedGroupAppendBool(Where where, StringBuilder ecl, Boolean includeName, int i) throws DataFormatException {
		if (i > 0) {
			if (where.getBool() == Bool.or)
				ecl.append(" OR ");
			else if (where.getBool() == Bool.and)
				ecl.append(" , ");
			}
		addRefinedGroup(where.getWhere().get(i), ecl, includeName);
	}

	private static void addRefined(Where where, StringBuilder ecl, Boolean includeName) throws DataFormatException {
		try {
			try {
				Element value = where.getIn().get(0);
				addClass(where, ecl, includeName);
				ecl.append(" = ");
				addClass(value, ecl, includeName);
			} catch (Exception e) {
				throw new DataFormatException("Where clause must contain 'is' value");
			}
		} catch (Exception e) {
			throw new DataFormatException("Where clause inside a role group clause must contain a property");
		}
	}

	private static void addClass(Element exp, StringBuilder ecl, boolean includeName) {
		if (exp.getType()!=null) {
			if (exp.getType().equals(IM.CONCEPT.getIri()))
				ecl.append("* ");
		}
		else {
			String subsumption = getSubsumption(exp);
			String iri = checkMember(exp.getId());
			String pipe = " | ";
			if (includeName && null!=exp.getName()) {
				ecl.append(subsumption).append(iri).append(pipe).append(exp.getName()).append(pipe);
			}
			else if (includeName){
				EntityRepository entityRepository = new EntityRepository();
				String name = entityRepository.getEntityReferenceByIri(exp.getId()).getName();
				ecl.append(subsumption).append(iri).append(pipe).append(name).append(pipe);
			}
			else
				ecl.append(subsumption).append(iri).append(" ");
		}
	}

	private static String getSubsumption(Entailment exp) {
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
			addClass(new Element().setId(iriRef.asIriRef().getIri()), ecl, true);
			first = false;
		}
		return ecl.toString();
	}

}
