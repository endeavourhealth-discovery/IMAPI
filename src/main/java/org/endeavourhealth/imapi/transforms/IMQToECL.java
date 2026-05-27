package org.endeavourhealth.imapi.transforms;

import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.interfacemanager.model.NAMESPACE;
import org.endeavourhealth.interfacemanager.model.ECLType;

import java.util.*;

public class IMQToECL {

  private final EntityRepository entityRepository = new EntityRepository();
  private final Map<String, String> names = new HashMap<>();
  private Prefixes prefixes;
  @Getter
  @Setter
  private ECLStatus eclStatus;

  /**
   * Takes a IM ECL compliant definition of a set and returns is ECL language
   *
   * @param eclQuery An object containing a 'query' and 'showNames'
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
      eclStatus.setMessage(ex.getMessage());
    }
  }

  private void cleanMatch(Match match) {
    match.setOr(cleanSubMatches(match.getOr()));
    match.setAnd(cleanSubMatches(match.getAnd()));
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
    if (where.getIri() == null && where.getOr() == null && where.getAnd() == null)
      return true;
    if (where.getAnd() != null || where.getOr() != null) return false;
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
    return match.getIs() != null && match.getIs().getFirst().getIri() == null && match.getWhere() == null;
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

  private boolean isExclusion(Match match) {
    for (List<Match> matches : Arrays.asList(match.getOr(), match.getAnd())) {
      if (matches != null) {
        for (Match m : matches) {
          if (m.notExists())
            return true;
        }
      }
    }
    return false;
  }


  private void expressionMatch(Match match, StringBuilder ecl, boolean includeNames, boolean isNested) throws QueryException {
    ECLType matchType = getEclType(match);
    if (matchType == null)
      return;
    if (matchType == ECLType.simple) {
      matchInstanceOf(match, ecl, includeNames);
    } else if (matchType == ECLType.refined) {
      match(match, ecl, includeNames, true);
      addRefinementsToMatch(match, ecl, includeNames, false);
      ecl.append("\n");
    } else if (matchType == ECLType.compound) {
      if (isNested) ecl.append("(");
      compound(match, ecl, includeNames);
      if (isNested) ecl.append(")");
      ecl.append("\n");
    }
    ecl.append("\n");
  }

  private void match(Match match, StringBuilder ecl, boolean includeNames, boolean isNested) throws QueryException {
    if ((match.getIs() == null || (match.getIs().getFirst().getIri() == null && match.getIs().getFirst().getMatch() == null)) && match.getOr() == null && match.getAnd() == null) {
      if (match.getWhere() != null)
        ecl.append("*");
      else throw new QueryException("Must have concept if no refinement");
    } else if (match.getIs() != null) {
      if (match.getIs().size() > 1) {
        ecl.append("(");
      }
      matchInstanceOf(match, ecl, includeNames);
      if (match.getIs().size() > 1) {
        ecl.append(")");
      }
    } else {
      if (isNested)
        ecl.append("(");
      compound(match, ecl, includeNames);
      if (isNested)
        ecl.append(")");
    }
  }

  private Map<String, List<Match>> sortInclusions(Match match) {
    Map<String, List<Match>> includeExclude = new HashMap<>();
    for (List<Match> matches : Arrays.asList(match.getOr(), match.getAnd())) {
      if (matches != null) {
        for (Match subMatch : matches) {
          if (subMatch.notExists()) {
            includeExclude.computeIfAbsent("out", m -> new ArrayList<>()).add(subMatch);
          } else includeExclude.computeIfAbsent("in", m -> new ArrayList<>()).add(subMatch);
        }
      }
    }
    return includeExclude;
  }

  private boolean needsExcludeBracket(List<Match> matches) {
    if (matches.size() > 1) return true;
    for (Match m : matches) {
      if (m.getWhere() != null) return true;
      if (m.getAnd() != null || m.getOr() != null) return true;
    }
    return false;
  }

  private boolean needsBracket(List<Match> matches,
                               Match match) {
    return matches.size() > 1 && match.getWhere() != null && match.getAnd() == null && match.getOr() == null;
  }

  private boolean needsIncludeBracket(Map<String, List<Match>> includeExclude) {
    if (includeExclude.containsKey("out")) {
      List<Match> matches = includeExclude.get("in");
      if (matches.size() > 1)
        return true;
      if (matches.get(0).getWhere() != null)
        return true;
    }
    return false;
  }

  private void compound(Match match, StringBuilder ecl, boolean includeNames) throws QueryException {
    Map<String, List<Match>> includeExclude = sortInclusions(match);
    if (includeExclude.get("out") != null && includeExclude.get("out").size() > 1)
      throw new QueryException("Only one exclusion allowed in ecl");

    boolean first = true;
    if (match.getAnd() != null) {
      boolean outerBracket = needsIncludeBracket(includeExclude);
      if (outerBracket) ecl.append("(");
      List<Match> matches = includeExclude.get("in");
      for (Match subMatch : matches) {
        if (!first) {
          ecl.append(" AND ");
        }
        boolean compoundRefined = needsBracket(matches, subMatch);
        if (compoundRefined)
          ecl.append("(");
        expressionMatch(subMatch, ecl, includeNames, true);
        if (compoundRefined)
          ecl.append(")");
        first = false;
      }
      if (outerBracket) ecl.append(")");
    }
    if (match.getOr() != null) {
      boolean outerBracket = needsIncludeBracket(includeExclude);
      if (outerBracket) ecl.append("(");
      for (Match subMatch : includeExclude.get("in")) {
        if (!first) {
          ecl.append(" OR ");
        }
        boolean compoundRefined = needsBracket(includeExclude.get("in"), subMatch);
        if (compoundRefined)
          ecl.append("(");
        expressionMatch(subMatch, ecl, includeNames, true);
        if (compoundRefined)
          ecl.append(")");
        first = false;
      }
      if (outerBracket) ecl.append(")");
    }

    if (includeExclude.containsKey("out")) {
      ecl.append(" MINUS ");
      boolean outerBracket = needsExcludeBracket(includeExclude.get("out"));
      if (outerBracket)
        ecl.append("(");
      expressionMatch(includeExclude.get("out").getFirst(), ecl, includeNames, false);
      if (outerBracket)
        ecl.append(")");
    }
  }

  private void matchInstanceOf(Match match, StringBuilder ecl, boolean includeNames) throws QueryException {
    if (match.getIs().size() == 1) {
      if (match.getIs().getFirst().getIri() == null && match.getIs().getFirst().getMatch() == null && match.getWhere() == null)
        throw new QueryException("Must have concept if no refinement");
      if (match.getIs().getFirst().isInvalid())
        setErrorStatus(ecl, "unknown concept");
      if (match.getIs().getFirst().getMatch() != null) {
        ecl.append(getSubsumption(match.getIs().getFirst()));
        ecl.append("(");
        expressionMatch(match.getIs().getFirst().getMatch(), ecl, includeNames, false);
        ecl.append(")");
      } else
        addClass(match.getIs().getFirst(), ecl, includeNames);
    } else {
      ecl.append("(");
      boolean first = true;
      for (Node instance : match.getIs()) {
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
    if (where.isRoleGroup()) ecl.append("{");
    if (where.getAnd() == null && where.getOr() == null) {
      if (null == where.getIs() || where.getIs().getFirst().getIri() == null)
        throw new QueryException("Where clause must contain a value or sub expressionMatch clause");
      addProperty(where, ecl, includeNames);
      if (where.getIs() != null && where.getIs().get(0) == null)
        throw new QueryException("Where clause must contain a value or sub expressionMatch clause");
      ecl.append(where.getIs() != null ? " = " : " != ");
      boolean first = true;
      for (List<Node> nodes : Arrays.asList(where.getIs())) {
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
    else if (exp.isAncestorsOf())
      subsumption = ">>";
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
    if (iri.startsWith(NAMESPACE.SNOMED.toString())) {
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
