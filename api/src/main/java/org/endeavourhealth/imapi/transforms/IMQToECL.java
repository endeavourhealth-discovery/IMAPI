package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.imq.EclType;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

public class IMQToECL {

  private Map<String, String> names = new HashMap<>();
  private Prefixes prefixes;

  /**
   * Takes a IM ECL compliant definition of a set and returns is ECL language
   *
   * @param query       a node representing a class expression e.g. value of im:Definition
   * @param includeName flag to include the concept term is the output
   * @return String of ECL
   * @throws DataFormatException invalid or unsupported ECL syntax
   */
  public String getECLFromQuery(Query query, Boolean includeName) throws QueryException {
    StringBuilder ecl = new StringBuilder();
    if (query.getPrefixes() != null) {
      prefixes = query.getPrefixes();
      for (Prefix prefix : prefixes) {
        ecl.append("prefix ").append(prefix.getPrefix()).append(": ").append(prefix.getNamespace()).append("\n");
      }
    }
    expressionMatch(query, ecl, includeName,false);
    return ecl.toString().trim();
  }

  public EclType getEclType(Match match) {
    if (match.getWhere() != null)
      return EclType.refined;
    if (match.getAnd() != null||match.getOr() != null) {
      return EclType.compound;
    }

    if (match.getInstanceOf() != null)
      return EclType.simple;
    else throw new IllegalArgumentException("Invalid IMQ for ECL conversion");
  }

  public String getECLFromQuery(Query query) throws QueryException {
    return getECLFromQuery(query, false);
  }


  private void expressionMatch(Match match, StringBuilder ecl, boolean includeNames, boolean isNested) throws QueryException {
    EclType matchType = getEclType(match);
    boolean isExclusion= match.getNot() != null;
    if (matchType == null)
      return;
    if (matchType == EclType.simple) {
      matchInstanceOf(match, ecl, includeNames);
    } else if (matchType == EclType.refined) {
      match(match,ecl,includeNames,true);
      addRefinementsToMatch(match, ecl, includeNames, false);
      ecl.append("\n");
    } else if (matchType == EclType.compound) {
      if (isNested||isExclusion)
        ecl.append("(");
      compound(match,ecl,includeNames);
      if (isNested||isExclusion)
        ecl.append(")");
      ecl.append("\n");
    }
    if (match.getNot() != null) {
      ecl.append(" MINUS ");
      boolean first = true;
      for (Match subMatch : match.getNot()) {
        if (!first){
          ecl.append("OR ");
        }
        first=false;
        expressionMatch(subMatch, ecl, includeNames,true);
        ecl.append("\n");
      }
    }
  }

  private void match(Match match, StringBuilder ecl, boolean includeNames, boolean isNested) throws QueryException {
    boolean isWild= false;
    if (match.getInstanceOf()==null&&match.getOr()==null&&match.getAnd()==null) {
      ecl.append("*");
    } else if (match.getInstanceOf() != null) {
      if (match.getInstanceOf().size() > 1) {
        ecl.append("(");
      }
      matchInstanceOf(match, ecl, includeNames);
      if (match.getInstanceOf().size() > 1) {
        ecl.append(")");
      }
    } else {
      if (isNested)
        ecl.append("(");
      compound(match,ecl,includeNames);
      if (isNested)
        ecl.append(")");
    }
    if (isWild) ecl.append(")");
  }
  private boolean bracketNeeded(Match match,boolean first) {
    if (match.getInstanceOf()==null&&match.getOr()==null&&match.getAnd()==null) return true;
    if (match.getWhere()!=null&&!first) return true;
    return false;
  }

  private void compound(Match match, StringBuilder ecl, boolean includeNames) throws QueryException {
    boolean first = true;
    if (match.getAnd() != null) {
      boolean isConjunction= match.getAnd().size()>1;
      for (Match subMatch : match.getAnd()) {
        if (!first) {
          ecl.append(" AND ");
        }
        if (bracketNeeded(subMatch,first)) ecl.append("(");
        expressionMatch(subMatch, ecl, includeNames, isConjunction);
        if (bracketNeeded(subMatch,first)) ecl.append(")");
        first = false;
      }
    }
    if (match.getOr() != null) {
      boolean isDisjunction= match.getOr().size()>1;
      for (Match subMatch : match.getOr()) {
        if (!first) {
          ecl.append(" OR ");
        }
        if (bracketNeeded(subMatch,first)) ecl.append("(");
        expressionMatch(subMatch, ecl, includeNames, isDisjunction);
        if (bracketNeeded(subMatch,first)) ecl.append(")");
        first=false;
      }
    }
  }

  private void matchInstanceOf(Match match, StringBuilder ecl, boolean includeNames) {
    if (match.getInstanceOf().size() == 1) {
      addClass(match.getInstanceOf().get(0), ecl, includeNames);
    } else {
      ecl.append("(");
      boolean first = true;
      for (Node instance : match.getInstanceOf()) {
        if (!first) {
          ecl.append(" OR ");
        }
        first = false;
        addClass(instance, ecl, includeNames);
        ecl.append("\n");
      }
      ecl.append(")");
    }
  }


  private void addRefinementsToMatch(Match match, StringBuilder ecl, boolean includeNames, boolean ignoreColon) throws QueryException {
    if (!ignoreColon) ecl.append(": ");
    addRefined(match.getWhere(), ecl, includeNames,false);
  }

  private void addRefinementsToWhere(Where property, StringBuilder ecl, boolean includeNames,boolean nested) throws QueryException {
    if (nested) ecl.append("(");
    boolean first = true;
      if (property.getAnd() != null) {
        for (Where subProperty : property.getAnd()) {
          if (!first) {
            ecl.append("\n");
            ecl.append(" , ");
          }
          first = false;
          addRefined(subProperty, ecl, includeNames,true);
        }
      }
    if (property.getOr() != null) {
      for (Where subProperty : property.getOr()) {
        if (!first) {
          ecl.append("\n");
          ecl.append(" OR ");
        }
        first = false;
        addRefined(subProperty, ecl, includeNames,true);
      }
    }
    if (nested) ecl.append(")");
  }


  private void addRefined(Where where, StringBuilder ecl, Boolean includeNames,boolean nested) throws QueryException {
    try {
      if (where.getIri()!=null && where.getIri().equals(IM.ROLE_GROUP)) {
        ecl.append("{");
        addRefinementsToWhere(where, ecl, includeNames,false);
        ecl.append("}");
      } else {
        if (where.getAnd()==null&&where.getOr()==null) {
          if (null == where.getIs())
            throw new QueryException("Where clause must contain a value or sub expressionMatch clause");
          boolean first = true;
          for (Node value : where.getIs()) {
            if (!first)
              ecl.append("\n OR ");
            first = false;
            addProperty(where, ecl, includeNames);
            ecl.append(" = ");
            addClass(value, ecl, includeNames);
          }
        } else {
          addRefinementsToWhere(where, ecl, includeNames,nested);
        }
      }
    } catch (Exception e) {
      throw new QueryException("Where clause inside a role group clause must contain a where");
    }
  }


  private void addProperty(Where exp, StringBuilder ecl, boolean includeName) {
    if (exp.isInverse())
      ecl.append(" R ");
    addConcept(ecl, includeName, getSubsumption(exp), exp.getIri(), exp.getName());
  }

  private void addConcept(StringBuilder ecl, boolean includeName, String subsumption, String id, String name) {
    String iriRef = checkMember(id, name, includeName);
    ecl.append(subsumption).append(iriRef);
  }

  private void addClass(Node exp, StringBuilder ecl, boolean includeName) {
    addConcept(ecl, includeName, getSubsumption(exp), exp.getIri(), exp.getName());
  }


  private String getSubsumption(Entailment exp) {
    if (exp == null)
      return "";

    String subsumption = "";
    if (exp.isDescendantsOrSelfOf())
      subsumption = "<< ";
    else if (exp.isDescendantsOf())
      subsumption = "< ";
    else if (exp.isMemberOf())
      subsumption = "^";
    return subsumption;
  }


  private String checkMember(String iri, String name, boolean includeNames) {
    if (iri == null || iri.isEmpty())
      return "*";
    if (name == null && includeNames) {
      if (names.get(iri) == null) {
        EntityRepository entityRepository = new EntityRepository();
        name = entityRepository.getEntityReferenceByIri(iri).getName();
        names.put(iri, name);
      }
      name = names.get(iri);
    }
    if (iri.startsWith(SNOMED.NAMESPACE)) {
      iri = iri.substring(iri.lastIndexOf("#") + 1);
    } else if (iri.contains("#")) {
      if (prefixes != null) {
        String prefix = prefixes.getPrefix(iri.substring(0, iri.lastIndexOf("#") + 1));
        if (prefix != null)
          iri = prefix + ":" + iri.substring(iri.lastIndexOf("#") + 1);
      }
    }
    if (includeNames && name != null) return iri + "|" + name + "|";
    else
      return iri;
  }

  /**
   * Gets simple iri set as ECL for compatibility with definition
   *
   * @param members a TTArray or TTNNode containing an iri set
   * @return ECL String
   */

  public String getMembersAsECL(TTArray members) {
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
