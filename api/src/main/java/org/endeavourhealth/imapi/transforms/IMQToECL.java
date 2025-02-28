package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.eclBuilder.EclType;
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
    match(query, ecl, includeName);
    return ecl.toString().trim();
  }

  public EclType getEclType(Match match) {
    if (match.getMatch() != null) {
      if (match.getMatch().size() > 0) {
        if (match.getWhere() != null)
          return EclType.compoundRefined;
      }
    }
    if (match.getWhere() != null)
      return EclType.refined;
    if (match.getMatch() != null) {
      for (Match subMatch : match.getMatch()) {
        if (subMatch.isExclude())
          return EclType.exclusion;
      }
      return EclType.compound;
    }
    if (match.getInstanceOf() != null)
      return EclType.simple;
    else return null;
  }

  public String getECLFromQuery(Query query) throws QueryException {
    return getECLFromQuery(query, false);
  }

  private boolean isList(Match match) {
    return null != match.getMatch();
  }


  private void match(Match match, StringBuilder ecl, boolean includeNames) throws QueryException {
    EclType matchType = getEclType(match);
    if (matchType == null)
      return;
    if (matchType == EclType.simple) {
      matchInstanceOf(match, ecl, includeNames);
    } else if (matchType == EclType.refined) {
      if (match.getInstanceOf() != null) {
        if (match.getInstanceOf().size() > 1) {
          ecl.append("(");
        }
        matchInstanceOf(match, ecl, includeNames);
      } else
        ecl.append("*");
      addRefinementsToMatch(match, ecl, includeNames, false);
      if (match.getInstanceOf() != null)
        if (match.getInstanceOf().size() > 1)
          ecl.append(")");
      ecl.append("\n");
    } else if (matchType == EclType.compound || matchType == EclType.compoundRefined) {
      if (matchType == EclType.compoundRefined)
        ecl.append("(");
      boolean first = true;
      for (Match subMatch : match.getMatch()) {
        EclType subMatchType = getEclType(subMatch);
        boolean bracket = match.getMatch().size() > 1 && subMatchType == EclType.refined;
        if (!first) {
          ecl.append("\n");
          if (match.getBoolMatch() == Bool.or) {
            ecl.append(" OR ");
          } else
            ecl.append(" AND ");
        }
        if (bracket)
          ecl.append("(");
        match(subMatch, ecl, includeNames);
        if (bracket)
          ecl.append(")");
        first = false;
      }
      if (matchType == EclType.compoundRefined) {
        ecl.append(")");
        addRefinementsToMatch(match, ecl, includeNames, false);
      }
      ecl.append("\n");
    } else {
      for (Match subMatch : match.getMatch()) {
        if (subMatch.isExclude()) {
          ecl.append(" MINUS ");
        }
        if (subMatch.getMatch().size() > 1) {
          ecl.append("(");
        }
        match(subMatch, ecl, includeNames);
        if (subMatch.getMatch().size() > 1) {
          ecl.append(")");
        }
        ecl.append("\n");
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
    boolean first = true;
    for (Where where : match.getWhere()) {
      if (!first) {
        ecl.append("\n AND ");
      }
      first = false;
      if (null != where.getIri() && where.getIri().equals(IM.ROLE_GROUP)) {
        ecl.append(" { ");
        addRefinementsToMatch(where.getMatch(), ecl, includeNames, true);
        ecl.append("}");
      } else {
        addRefined(where, ecl, includeNames);
      }
    }
  }

  private void addRefinementsToWhere(Where property, StringBuilder ecl, boolean includeNames) throws QueryException {
    if (null != property.getIri() && property.getIri().equals(IM.ROLE_GROUP)) {
      ecl.append(" { ");
      addRefinementsToMatch(property.getMatch(), ecl, includeNames, true);
      ecl.append("}");
    } else {
      boolean first = true;
      for (Where subProperty : property.getWhere()) {
        if (!first) {
          ecl.append("\n");
          if (property.getBoolWhere() == Bool.or) {
            ecl.append(" OR ");
          } else
            ecl.append(" , ");
        }
        first = false;
        addRefined(subProperty, ecl, includeNames);
      }
    }
  }


  private void addRefined(Where where, StringBuilder ecl, Boolean includeNames) throws QueryException {
    try {
      if (null != where.getIri() && where.getIri().equals(IM.ROLE_GROUP)) {
        ecl.append(" { ");
        addRefinementsToMatch(where.getMatch(), ecl, includeNames, true);
        ecl.append("}");
      } else {
        if (null == where.getWhere()) {
          if (null == where.getIs() && null == where.getMatch())
            throw new QueryException("Where clause must contain a value or sub match clause");
          if (where.getIs() != null) {
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
            addProperty(where, ecl, includeNames);
            ecl.append(" = (");
            match(where.getMatch(), ecl, includeNames);
            ecl.append(")");
          }
        } else {
          ecl.append("(");
          addRefinementsToWhere(where, ecl, includeNames);
          ecl.append(")");
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
