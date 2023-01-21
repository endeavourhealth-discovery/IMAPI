package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.iml.Where;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;
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

		where(query.getWhere(), ecl, includeName);
		return ecl.toString();
	}


	private static void where(Where where, StringBuilder ecl, Boolean includeName) throws DataFormatException {
        if (null == where)
            return;
        if (where.getNotExist() != null)
            ecl.append("(");
        if (where.getFrom() != null) {
            whereFrom(where, ecl, includeName);
        } else if (where.getPathTo() != null) {
            ecl.append("* ");
        }
        if (where.getPathTo() != null) {
            wherePathTo(where, ecl, includeName);
        } else {
            if (where.getAnd() != null) {
                addConjunction(where.getAnd(), ecl, includeName);
            }
            if (where.getOr() != null) {
                addDisjunction(where.getOr(), ecl, includeName);
            }
        }
        if (where.getNotExist() != null) {
            whereNotExists(where, ecl, includeName);
        }
    }

    private static void whereFrom(Where where, StringBuilder ecl, Boolean includeName) {
        boolean first = true;
        for (TTAlias from : where.getFrom()) {
            if (!first)
                ecl.append(" OR ");
            else
                first = false;
            addClass(from, ecl, includeName);
        }
    }

    private static void wherePathTo(Where where, StringBuilder ecl, Boolean includeName) throws DataFormatException {
        if (where.getPathTo().equals(IM.ROLE_GROUP.getIri())) {
            if (where.getAnd() != null) {
                addRefinedConjunction(where.getAnd(), ecl, includeName);
            } else if (where.getOr() != null) {
                addRefinedDisjunction(where.getOr(), ecl, includeName);
            } else {
                if (where.getProperty()!=null) {
                    addRefined(where, ecl, includeName,true);
                }
                else
                    addRefined(where.getWhere(), ecl, includeName,true);
            }
        }
        else
            throw new DataFormatException("Unrecognised property path. Only im:roleGroup is supported");
    }

    private static void whereNotExists(Where where, StringBuilder ecl, Boolean includeName) throws DataFormatException {
        ecl.append(" MINUS (");
        where(where.getNotExist(), ecl, includeName);
        ecl.append(" ) )"); //has to have brackets in a clause with a MINUS
    }


    private static void addRefinedDisjunction(List<Where> ors, StringBuilder ecl, Boolean includeName) throws DataFormatException {
		boolean first= true;
		for (Where or:ors){
			if (!first)
				ecl.append(" OR ");
			else
				first= false;
			if (or.getProperty()!=null) {
				addRefined(or, ecl, includeName,first);
			}
			else if (or.getAnd()!=null) {
				ecl.append("\n( ");
				addRefinedConjunction(or.getAnd(),ecl,includeName);
				ecl.append(")");
			}
			else if (or.getOr()!=null){
				ecl.append("\n( ");
				addRefinedDisjunction(or.getOr(),ecl,includeName);
				ecl.append(")");
			}
		}
	}

	private static void addRefinedConjunction(List<Where> ands, StringBuilder ecl, Boolean includeName) throws DataFormatException {
		boolean first= true;
		for (Where and:ands){
			if (!first)
				ecl.append(" , ");
			if (and.getProperty()!=null) {
				addRefined(and, ecl, includeName,first);
			}
			else if (and.getAnd()!=null) {
				ecl.append("\n( ");
				addRefinedConjunction(and.getAnd(),ecl,includeName);
				ecl.append(")");
			}
			else if (and.getOr()!=null){
				ecl.append("\n( ");
				addRefinedDisjunction(and.getOr(),ecl,includeName);
				ecl.append(")");
			}
			first= false;
		}
	}


	private static void addDisjunction(List<Where> ors, StringBuilder ecl, Boolean includeName) throws DataFormatException {
		boolean first = true;
		for (Where or : ors) {
			if (!first)
				ecl.append(" OR ");
			else
				first = false;
			if (or.getPathTo()!=null || or.getProperty()!=null || or.getAnd()!=null)
				ecl.append(" (");
			where(or, ecl, includeName);
			if (or.getPathTo()!=null || or.getProperty()!=null || or.getAnd()!=null)
				ecl.append(")");
		}
	}

	private static void addConjunction(List<Where> ands, StringBuilder ecl, Boolean includeName) throws DataFormatException {
		boolean first = true;
		for (Where and : ands) {
			if (!first)
				ecl.append(" AND ");
			else
				first = false;
			if (and.getPathTo()!=null || and.getProperty()!=null || and.getOr()!=null)
				ecl.append(" (");
			where(and, ecl, includeName);
			if (and.getPathTo()!=null || and.getProperty()!=null || and.getOr()!=null)
				ecl.append(")");
		}
	}


	private static void addRefined(Where where, StringBuilder ecl, Boolean includeName, boolean first) throws DataFormatException {
		if (first)
		  ecl.append(" : ");
		try {
			TTAlias property = where.getProperty();
			try {
				TTAlias value = where.getIs();
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
			if (includeName && exp.asIriRef().getName() != null) {
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
