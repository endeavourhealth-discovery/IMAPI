package org.endeavourhealth.imapi.transforms;

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
		if (query.getFrom()!=null) {
			fromWhere(query.getFrom(), ecl, includeName);
		}
		return ecl.toString();
	}

	private static boolean isList(From from){
		if (null!=from.getFrom())
				return true;
		return false;
	}






	private static void fromWhere(From fromWhere, StringBuilder ecl, Boolean includeName) throws DataFormatException {
		if (null!=fromWhere) from(fromWhere, ecl, includeName,null!=fromWhere.getWhere());
	}
	private static void from(From from,StringBuilder ecl, boolean includeName,boolean isRefined) throws DataFormatException {
		if (null != from.getIri()) {
			addClass(from, ecl, includeName);
		} else if (null != from.getFrom()) {
			boolean bracketFrom = isRefined;
			if (bracketFrom)
				ecl.append("(");

			boolean first = true;
			for (From subFrom : from.getFrom()) {
				fromAppendBracket(from, ecl, includeName, first, subFrom);
				first = false;
			}
			if (bracketFrom) {
				ecl.append(")");
			}
		}
		if (null != from.getWhere()) {
			if (null == from.getIri() && null == from.getFrom())
				ecl.append("*");
			addRefinements(from.getWhere(), ecl, includeName);
		}
	}

	private static void fromAppendBracket(From from, StringBuilder ecl, boolean includeName, boolean first, From subFrom) throws DataFormatException {
		if (subFrom.getBool() == Bool.not) {
			ecl.append(" MINUS ");
		} else {
			fromAppendBool(from, ecl, first);
		}
		boolean bracket = false;
		if (null != subFrom.getWhere() && isList(from)) {
			bracket = true;
		}
		if (null != subFrom.getFrom() && subFrom.getFrom().size() > 1) {
			bracket = true;
		}
		if (subFrom.getBool() == Bool.not && null != subFrom.getFrom() && subFrom.getFrom().size() > 1)
			bracket = true;

		if (bracket)
			ecl.append("(");
		from(subFrom, ecl, includeName, null != subFrom.getWhere());
		if (bracket) {
			ecl.append(")\n");
		}
	}

	private static void fromAppendBool(From from, StringBuilder ecl, boolean first) {
		if (!first) {
			if (from.getBool() == Bool.and) {
				ecl.append(" AND ");
			} else if (from.getBool() == Bool.or) {
				ecl.append("  OR ");
			} else
				ecl.append(" OR ");
		}
	}

	private static void addRefinements(Where where,StringBuilder ecl,boolean includeName) throws DataFormatException {
		ecl.append(":");
		addRefinedGroup(where, ecl, includeName);
	}

	private static void addRefinedGroup(Where where,StringBuilder ecl,Boolean includeName) throws DataFormatException {
		if (null == where.getWhere()) {
			addRefined(where, ecl, includeName);
		} else {
			boolean grouped = null != where.getIri() && where.getIri().equals(IM.ROLE_GROUP.getIri());
			if (grouped)
				ecl.append("{");
			if (where.getWhere().size() == 1) {
				addRefinedGroup(where.getWhere().get(0), ecl, includeName);
			} else for (int i = 0; i < where.getWhere().size(); i++) {
				addRefinedGroupAppendBool(where, ecl, includeName, i);
			}
			if (grouped)
				ecl.append("}");
		}
	}

	private static void addRefinedGroupAppendBool(Where where, StringBuilder ecl, Boolean includeName, int i) throws DataFormatException {
		if (i > 0) {
			if (where.getBool() == Bool.or)
				ecl.append(" OR ");
			else if (where.getBool() == Bool.and)
				ecl.append(" , ");
			else if (where.getBool() == Bool.not) {
				ecl.append(" MINUS (");
			}
		}
		addRefinedGroup(where.getWhere().get(i), ecl, includeName);
		if (where.getBool() == Bool.not)
			ecl.append(")");
	}

	private static void addRefined(Where where, StringBuilder ecl, Boolean includeName) throws DataFormatException {
		try {
			TTAlias property = where;
			try {
				TTAlias value = where.getIn().get(0);
				addClass(property, ecl, includeName);
				ecl.append(" = ");
				addClass(value, ecl, includeName);
			} catch (Exception e) {
				throw new DataFormatException("Where clause must contain 'is' value");
			}
		} catch (Exception e) {
			throw new DataFormatException("Where clause inside a role group clause must contain a property");
		}
	}



	private static void addClass(TTAlias exp, StringBuilder ecl, boolean includeName) {
		if (exp.getIri().equals(IM.CONCEPT.getIri()))
			ecl.append("* ");
		else {
			String subsumption="";
			if (exp.isIncludeSubtypes())
				subsumption="<<";
			if (exp.isExcludeSelf())
				subsumption="<";
			String iri = checkMember(exp.asIriRef().getIri());
			String pipe = " | ";
			if (includeName && null!=exp.asIriRef().getName()) {
				ecl.append(subsumption).append(iri).append(pipe).append(exp.asIriRef().getName()).append(pipe);
			} else {
				ecl.append(subsumption).append(iri);
			}
		}
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
			addClass(new TTAlias(iriRef.asIriRef()), ecl, true);
			first = false;
		}
		return ecl.toString();
	}

}
