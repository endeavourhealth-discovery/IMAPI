package org.endeavourhealth.imapi.transforms;

import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.Namespace;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMQToECL {

  private final EntityRepository entityRepository = new EntityRepository();
  private Map<String, String> names = new HashMap<>();
  private Prefixes prefixes;
  @Getter
  @Setter
  private ECLStatus eclStatus;

  /**
   * Takes a IM ECL compliant definition of a set and returns is ECL language
   *
   * @param eclQuery An object containing a 'query' and 'showNames'
   * @return the object with a status, ecl and the query
   */
  public void getECLFromQuery(ECLQueryRequest eclQuery) {
    eclStatus = new ECLStatus();
    eclStatus.setValid(true);
    eclQuery.setStatus(eclStatus);
    StringBuilder ecl = new StringBuilder();
    Query query = eclQuery.getQuery();
    cleanMatch(query);
    if (query.getPrefixes() != null) {
      prefixes = query.getPrefixes();
      for (Prefix prefix : prefixes) {
        ecl.append("prefix ").append(prefix.getPrefix()).append(": ").append(prefix.getNamespace()).append("\n");
      }
    }
    try {
      expressionMatch(query, ecl, eclQuery.isShowNames(), false);
      eclQuery.setEcl(ecl.toString().trim());
    } catch (Exception ex) {
      eclStatus.setValid(false);
      query.setInvalid(true);
    }
  }

  private void cleanMatch(Match match) {
    match.setOr(cleanSubMatches(match.getOr()));
    match.setAnd(cleanSubMatches(match.getAnd()));
    match.setNot(cleanSubMatches(match.getNot()));
    if (match.getWhere() != null)
      cleanWhere(match.getWhere());
  }

  private void cleanWhere(Where where) {
    where.setOr(cleanSubWheres(where.getOr()));
    where.setAnd(cleanSubWheres(where.getAnd()));
  }

  private List<Where> cleanSubWheres(List<Where> wheres) {
    if (wheres == null) return null;
    for (int i = wheres.size() - 1; i >= 0; i--) {
      if (isBlankWhere(wheres.get(i))) {
        wheres.remove(i);
      }
    }
    if (wheres.isEmpty()) {
      return null;
    }
    for (Where subWhere : wheres) {
      cleanWhere(subWhere);
    }
    return wheres;
  }

  private boolean isBlankWhere(Where where) {
    if (where.getIri() == null && where.getOr() == null && where.getAnd() == null && where.getNot() == null)
      return true;
    if (where.getAnd() != null || where.getNot() != null || where.getOr() != null) return false;
    if (where.getIs() == null) return true;
    return where.getIs().getFirst().getIri() == null;
  }

  private List<Match> cleanSubMatches(List<Match> matches) {
    if (matches == null) return null;
    for (int i = matches.size() - 1; i >= 0; i--) {
      if (isBlankMatch(matches.get(i))) {
        matches.remove(i);
      }
    }
    if (matches.isEmpty()) {
      return null;
    }
    for (Match m : matches) {
      cleanMatch(m);
    }
    return matches;
  }


  private boolean isBlankMatch(Match match) {
    if (match.getInstanceOf() != null && match.getInstanceOf().getFirst().getIri() == null && match.getWhere() == null)
      return true;
    return false;
  }

  public ECLType getEclType(Match match) {
    if (match.getWhere() != null)
      return ECLType.refined;
    if (match.getAnd() != null || match.getOr() != null) {
      return ECLType.compound;
    } else return ECLType.simple;
  }


  private void setErrorStatus(StringBuilder ecl, String message) {
    eclStatus.setValid(false);
    if (eclStatus.getLine() == null) {
      String[] eclText = ecl.toString().split("\n");
      Integer line = eclText.length;
      Integer offset = eclText[eclText.length - 1].length();
      eclStatus.setLine(line);
      eclStatus.setOffset(offset);
      eclStatus.setMessage(message);
    }
  }


  private void expressionMatch(Match match, StringBuilder ecl, boolean includeNames, boolean isNested) throws QueryException {
    ECLType matchType = getEclType(match);
    boolean isExclusion = match.getNot() != null;
    if (matchType == null)
      return;
    if (matchType == ECLType.simple) {
      matchInstanceOf(match, ecl, includeNames);
    } else if (matchType == ECLType.refined) {
      if (isExclusion) ecl.append("(");
      match(match, ecl, includeNames, true);
      addRefinementsToMatch(match, ecl, includeNames, false);
      if (isExclusion) ecl.append(")");
      ecl.append("\n");
    } else if (matchType == ECLType.compound) {
      if (isNested || isExclusion)
        ecl.append("(");
      compound(match, ecl, includeNames);
      if (isNested || isExclusion)
        ecl.append(")");
      ecl.append("\n");
    }
    if (match.getNot() != null) {
      ecl.append(" MINUS ");
      if (match.getNot().size() > 1)
        ecl.append("(");
      boolean first = true;
      for (Match subMatch : match.getNot()) {
        if (!first) {
          ecl.append("OR ");
        }
        first = false;
        if (getEclType(subMatch) == ECLType.refined) ecl.append("(");
        expressionMatch(subMatch, ecl, includeNames, true);
        if (getEclType(subMatch) == ECLType.refined) ecl.append(")");
        ecl.append("\n");
      }
      if (match.getNot().size() > 1)
        ecl.append(")");
    }
  }

  private void match(Match match, StringBuilder ecl, boolean includeNames, boolean isNested) throws QueryException {
    boolean isWild = false;
    if (match.getInstanceOf() == null && match.getOr() == null && match.getAnd() == null) {
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
      compound(match, ecl, includeNames);
      if (isNested)
        ecl.append(")");
    }
    if (isWild) ecl.append(")");
  }

  private boolean bracketNeeded(Match match, boolean first, boolean multiItems) {
    if (match.getInstanceOf() == null && match.getOr() == null && match.getAnd() == null) return true;
    return match.getWhere() != null && (!first || multiItems);
  }

  private void compound(Match match, StringBuilder ecl, boolean includeNames) throws QueryException {
    boolean first = true;
    if (match.getAnd() != null) {
      boolean isConjunction = match.getAnd().size() > 1;
      for (Match subMatch : match.getAnd()) {
        if (!first) {
          ecl.append(" AND ");
        }
        if (bracketNeeded(subMatch, first, isConjunction)) ecl.append("(");
        expressionMatch(subMatch, ecl, includeNames, isConjunction);
        if (bracketNeeded(subMatch, first, isConjunction)) ecl.append(")");
        first = false;
      }
    }
    if (match.getOr() != null) {
      boolean isDisjunction = match.getOr().size() > 1;
      for (Match subMatch : match.getOr()) {
        if (!first) {
          ecl.append(" OR ");
        }
        if (bracketNeeded(subMatch, first, isDisjunction)) ecl.append("(");
        expressionMatch(subMatch, ecl, includeNames, isDisjunction);
        if (bracketNeeded(subMatch, first, isDisjunction)) ecl.append(")");
        first = false;
      }
    }
  }

  private void matchInstanceOf(Match match, StringBuilder ecl, boolean includeNames) {
    if (match.getInstanceOf().size() == 1) {
      if (match.getInstanceOf().get(0).isInvalid())
        setErrorStatus(ecl, "unknown concept");
      addClass(match.getInstanceOf().get(0), ecl, includeNames);
    } else {
      ecl.append("(");
      boolean first = true;
      for (Node instance : match.getInstanceOf()) {
        if (instance.isInvalid()) setErrorStatus(ecl, "unknown concept");
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
    addRefined(match.getWhere(), ecl, includeNames, false);
  }

  private void addRefinementsToWhere(Where property, StringBuilder ecl, boolean includeNames, boolean nested) throws QueryException {
    if (nested) ecl.append("(");
    boolean first = true;
    if (property.getAnd() != null) {
      for (Where subProperty : property.getAnd()) {
        if (!first) {
          ecl.append("\n");
          ecl.append(" , ");
        }
        first = false;
        addRefined(subProperty, ecl, includeNames, true);
      }
    }
    if (property.getOr() != null) {
      for (Where subProperty : property.getOr()) {
        if (!first) {
          ecl.append("\n");
          ecl.append(" or ");
        }
        first = false;
        addRefined(subProperty, ecl, includeNames, true);
      }
    }
    if (nested) ecl.append(")");
  }

  private void addRefined(Where where, StringBuilder ecl, Boolean includeNames, boolean nested) throws QueryException {
    if (where.isInvalid()) setErrorStatus(ecl, "unknown property concept : ");
    try {
      if (where.isRoleGroup()) ecl.append("{");
      if (where.getAnd() == null && where.getOr() == null) {
        if (null == where.getIs() && null == where.getNotIs())
          throw new QueryException("Where clause must contain a value or sub expressionMatch clause");
        addProperty(where, ecl, includeNames);
        ecl.append(where.getIs() != null ? " = " : " != ");
        boolean first = true;
        for (List<Node> nodes : Arrays.asList(where.getIs(), where.getNotIs())) {
          if (nodes != null) {
            if (nodes.size() > 1)
              ecl.append(" (");
            for (Node value : nodes) {
              if (!first)
                ecl.append("\n or ");
              first = false;
              if (value.isInvalid()) setErrorStatus(ecl, "unknown value concept : ");
              addClass(value, ecl, includeNames);
            }
            if (nodes.size() > 1)
              ecl.append(")");
          }
        }
      } else {
        addRefinementsToWhere(where, ecl, includeNames, nested);
      }
      if (where.isRoleGroup()) ecl.append("}");
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
        name = entityRepository.getEntityReferenceByIri(iri).getName();
        names.put(iri, name);
      }
      name = names.get(iri);
    }
    if (iri.startsWith(Namespace.SNOMED.toString())) {
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
