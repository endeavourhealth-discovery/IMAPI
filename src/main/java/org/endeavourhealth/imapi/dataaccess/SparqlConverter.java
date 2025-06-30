package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.queryengine.QueryValidator;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;
import java.util.stream.Collectors;

public class SparqlConverter {
  private final QueryRequest queryRequest;
  private final String tabs = "";
  int o = 0;
  String mainEntity;
  private Query query;
  private Update update;


  public SparqlConverter(QueryRequest queryRequest) throws QueryException {
    this.queryRequest = queryRequest;
    if (queryRequest.getQuery() != null) {
      this.query = queryRequest.getQuery();
      new QueryValidator().validateQuery(this.query);
    } else
      this.update = queryRequest.getUpdate();

  }

  public static String iriFromString(String iri) {
    if (iri.contains(",")) {
      iri = iri.replace(",", ">,<");
      iri = "<" + iri + ">";
      return iri;
    }
    if (iri.startsWith("http") || iri.startsWith("urn:"))
      return "<" + iri + ">";
    else return iri;
  }

  /**
   * Resolves a query $ vaiable value using the query request argument map
   *
   * @param value        the $alias is the query definition
   * @param queryRequest the Query request object submitted via the API
   * @return the value of the variable as a String
   * @throws QueryException if the variable is unresolvable
   */
  public String resolveReference(String value, QueryRequest queryRequest) throws QueryException {
    if (value.equalsIgnoreCase("$referenceDate") && null != queryRequest.getReferenceDate())
      return queryRequest.getReferenceDate();

    value = value.replace("$", "");
    if (null != queryRequest.getArgument()) {
      for (Argument argument : queryRequest.getArgument()) {
        String res = processArgument(queryRequest, argument, value);
        if (res != null) return res;
      }
    }
    return value;
  }

  private String processArgument(QueryRequest queryRequest, Argument argument, String value) throws QueryException {
    if (argument.getParameter().equals(value)) {
      if (null != argument.getValueData())
        return argument.getValueData();
      else if (null != argument.getValueIri()) {
        if (argument.getValueIri().getIri() != null)
          return "<" + argument.getValueIri().getIri() + ">";
        else throw new QueryException("Argument parameter " + value + " valueIri cannot be null or set requestAskIri");
      } else if (null != argument.getValueIriList()) {
        if (argument.getValueIriList().isEmpty())
          throw new QueryException("Argument parameter " + value + " valueIriList cannot be empty");
        return argument.getValueIriList().stream().map(iri -> "<" + iri.getIri() + ">").collect(Collectors.joining(" "));
      } else if (null != argument.getValueVariable()) {
        return argument.getValueVariable();
      } else if (null != argument.getValueDataList()) {
        if (argument.getValueDataList().isEmpty())
          throw new QueryException("Argument parameter " + value + " valueDataList cannot be empty");
        return String.join(",", argument.getValueDataList());
      } else if (null != argument.getValueObject()) {
        return argument.getValueObject().toString();
      }
    }
    return null;
  }

  /**
   * Takes an IMQ select query model and converts to SPARQL
   *
   * @return String of SPARQL
   **/
  public String getSelectSparql(Set<TTIriRef> statusFilter) throws QueryException {
    return getFullSelectSparql(statusFilter, false, false);
  }

  public String getSelectSparql(Query query, Set<TTIriRef> statusFilter, boolean countOnly, boolean highestUsage) throws QueryException {
    this.query = query;
    return getFullSelectSparql(statusFilter, countOnly, highestUsage);
  }

  private String getFullSelectSparql(Set<TTIriRef> statusFilter, boolean countOnly, boolean highestUsage) throws QueryException {
    StringBuilder fullSelectQl = new StringBuilder();
    fullSelectQl.append(getSelectSparql(statusFilter, countOnly, highestUsage));
    orderGroupLimit(fullSelectQl, query, countOnly, highestUsage);
    return fullSelectQl.toString();
  }

  public String subQuery(Set<TTIriRef> statusFilter, boolean countOnly, boolean highestUsage) throws QueryException {
    StringBuilder subSelectQl = new StringBuilder();
    if (query.getSubquery() != null) {
      o++;
      Query rootQuery = query;
      query = rootQuery.getSubquery();
      subSelectQl.append("{\n");
      subSelectQl.append(getSelectSparql(statusFilter, countOnly, highestUsage));
      subSelectQl.append("}\n");
      subSelectQl.append("{\n BIND (?").append(query.getVariable()).append(" AS ?").append(rootQuery.getVariable()).append(") \n}\n");
      query = rootQuery;
    }
    return subSelectQl.toString();
  }

  private String getSelectSparql(Set<TTIriRef> statusFilter, boolean countOnly, boolean highestUsage) throws QueryException {
    mainEntity = query.getVariable() != null ? query.getVariable() : "entity";
    StringBuilder selectQl = new StringBuilder();
    if (countOnly) {
      mainEntity = query.getVariable();
      if (null != query.getInstanceOf() && null != query.getInstanceOf().getFirst().getVariable())
        mainEntity = query.getInstanceOf().getFirst().getVariable();
      selectQl.append("SELECT (count (distinct ?").append(mainEntity).append(") as ?count)");
    } else {
      selectQl.append("SELECT ");
      selectQl.append("distinct ");
    }
    addMatchWhereSparql(selectQl, statusFilter, true, countOnly);
    return selectQl.toString();

  }

  public String getAskSparql(Set<TTIriRef> statusFilter) throws QueryException {

    StringBuilder askQl = new StringBuilder();

    askQl.append("ASK ");
    askQl.append("GRAPH ?g {");
    addMatchWhereSparql(askQl, statusFilter, false, false);
    askQl.append("}");

    return askQl.toString();
  }

  private void addMatchWhereSparql(StringBuilder sparql, Set<TTIriRef> statusFilter, boolean includeReturns, boolean countOnly) throws QueryException {
    mainEntity = "entity";
    if (query.getVariable() != null) {
      mainEntity = query.getVariable();
    }
    StringBuilder whereQl = new StringBuilder();
    whereQl.append("WHERE {");
    boolean hasSubQuery = false;
    if (query.getSubquery() != null) {
      whereQl.append(subQuery(statusFilter, false, false));
      hasSubQuery = true;
    }
    if (query.getTypeOf() != null) {
      whereQl.append("?").append(mainEntity).append(" rdf:type ").append(iriFromString(query.getTypeOf().getIri())).append(".\n");
    }
    if (queryRequest.getAskIri() != null) {
      whereQl.append(" VALUES ").append("?").append(mainEntity).append("{").append(iriFromString(queryRequest.getAskIri())).append("}\n");
    }
    processMatch(whereQl, mainEntity, query, hasSubQuery);

    if ((!countOnly && includeReturns) && null != query.getReturn()) {
      Return aReturn = query.getReturn();
      convertReturn(sparql, whereQl, aReturn);
    }

    o++;
    String statusVar = "status" + o;

    if (null != statusFilter && !statusFilter.isEmpty()) {
      List<String> statusStrings = new ArrayList<>();
      for (TTIriRef status : statusFilter) {
        statusStrings.add("<" + status.getIri() + ">");
      }
      whereQl.append("?").append(mainEntity).append(" im:status ?").append(statusVar).append(".\n");
      whereQl.append("Filter (?").append(statusVar).append(" in (").append(String.join(",", statusStrings)).append("))\n");
    }
    if (query.isActiveOnly()) {
      whereQl.append("?").append(mainEntity).append(" im:status im:Active.\n");
    }


    sparql.append("\n");

    whereQl.append("}");

    sparql.append(whereQl).append("\n");
  }

  public String getCountSparql(Set<TTIriRef> statusFilter) throws QueryException {
    return getFullSelectSparql(statusFilter, true, false);
  }

  private String escape(String text) {
    return text.replaceAll("[(\\-){}^\"/~\\\\]", " ").replaceAll("\\s+", " ").trim();
  }


  private void processMatch(StringBuilder whereQl, String parent, Match match, boolean hasSubQuery) throws QueryException {
    String subject;
    if (match.getVariable() != null)
      subject = match.getVariable();
    else if (match.getNodeRef() != null)
      subject = match.getNodeRef();
    else if (match.getParameter() != null) {
      subject = match.getParameter();
      whereQl.append(" VALUES ").append("?").append(subject).append("{").append(getIriFromAlias(null, match.getParameter(), null, null)).append("}\n");
    } else
      subject = parent;
    String mainSubject = subject;

    if (match.getGraph() != null) {
      whereQl.append(" graph ").append(iriFromAlias(match.getGraph())).append(" {");
    }
    if (match.getEntailment() != null) {
      if (match.getEntailment() == Entail.descendantsOrSelfOf) {
        o++;
        whereQl.append("?").append(subject).append(" <").append(IM.IS_A).append("> ?").append(subject).append(o).append(".\n");
        subject = subject + o;
      } else {
        throw new QueryException("Match entailment " + match.getEntailment().toString() + " is not yet supported");
      }
    }
    String pathVariable = null;
    if (match.getPath() != null) {
      for (Path pathMatch : match.getPath()) {
        pathVariable = processPath(whereQl, subject, pathMatch);

      }
    }
    if (match.getAnd() != null) {
      for (int i = 0; i < match.getAnd().size(); i++) {
        Match subMatch = match.getAnd().get(i);
        processMatch(whereQl, subject, subMatch, false);
      }
    }
    if (match.getOr() != null) {
      for (int i = 0; i < match.getOr().size(); i++) {
        if (i == 0 && !hasSubQuery)
          whereQl.append("{ \n");
        else
          whereQl.append("UNION {\n");
        Match subMatch = match.getOr().get(i);
        processMatch(whereQl, subject, subMatch, false);
        whereQl.append("}\n");
      }
    }
    if (match.getNot() != null) {
      whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
      for (int i = 0; i < match.getNot().size(); i++) {
        if (i == 0)
          whereQl.append("{ \n");
        else
          whereQl.append("UNION {\n");
        Match subMatch = match.getNot().get(i);
        processMatch(whereQl, mainSubject, subMatch, false);
        whereQl.append("}\n");
      }
      whereQl.append("}\n");
    }

    o++;
    String object = "object" + o;
    if (match.getTypeOf() != null) {
      processTypeOf(whereQl, match.getTypeOf(), subject);
    }
    if (match.getInstanceOf() != null) {
      processMatchInstanceOf(match, whereQl, subject);
    }
    if (match.getWhere() != null) {
      processWhere(whereQl, subject, match.getWhere(), pathVariable, false);
    }
    if (match.getGraph() != null) {
      whereQl.append("}");
    }

  }

  private String processPath(StringBuilder whereQl, String subject, Path pathMatch) throws QueryException {
    String pathVariable = null;
    String inverse = pathMatch.isInverse() ? "^" : "";
    String predicate = getIriFromAlias(pathMatch.getIri(), pathMatch.getParameter(), pathMatch.getVariable(), null);
    if (inverse.equals("^") && predicate.startsWith("?"))
      throw new QueryException("Inverse processPath processMatch cannot have a variable as predicate");
    whereQl.append("?").append(subject).append(" ").append(inverse).append(predicate).append(" ?").append(pathMatch.getVariable()).append(".\n");
    if (pathMatch.getPath() != null) {
      for (Path path : pathMatch.getPath()) {
        pathVariable = processPath(whereQl, pathMatch.getVariable(), path);
      }
    } else
      pathVariable = pathMatch.getVariable();
    return pathVariable;
  }


  private void processMatchInstanceOf(Match match, StringBuilder whereQl, String subject) throws QueryException {
    if (match.getInstanceOf().size() == 1) {
      o++;
      String object = "instance" + o;
      String inList = iriFromAliases(match.getInstanceOf());
      Node instance = match.getInstanceOf().getFirst();
      if (instance.getNodeRef() != null) {
        object = instance.getNodeRef();
      }
      if (instance.isMemberOf()) {
        processMatchIsMemberOf(whereQl, subject, object, inList);
      } else if (instance.isDescendantsOrSelfOf()) {
        processMatchIsDescendantsOrSelfOf(whereQl, subject, object, inList);
      } else if (instance.isDescendantsOf()) {
        processMatchIsDescendantsOf(whereQl, subject, object, inList);
      } else if (instance.isAncestorsOf() || instance.isAncestorsOrSelfOf()) {
        processMatchIsAncestorOf(whereQl, subject, object, inList);
      } else {
        processMatchEqual(whereQl, subject, inList);

      }
    } else {
      Map<Entail, List<String>> inTypes = new HashMap<>();
      Map<Entail, List<String>> outTypes = new HashMap<>();
      sortInstances(match.getInstanceOf(), inTypes, outTypes);
      boolean first = true;
      for (Map.Entry<Entail, List<String>> entry : inTypes.entrySet()) {
        o++;
        String object = "instance" + o;
        Entail entail = entry.getKey();
        List<String> in = entry.getValue();
        String inList = String.join(" ", in);
        if (first)
          whereQl.append("{");
        else
          whereQl.append("UNION {");
        first = false;
        if (entail == Entail.descendantsOrSelfOf) {
          processMatchIsDescendantsOrSelfOf(whereQl, subject, object, inList);
        } else if (entail == Entail.descendantsOf) {
          processMatchIsDescendantsOf(whereQl, subject, object, inList);
        } else if (entail == Entail.ancestorsOf) {
          processMatchIsAncestorOf(whereQl, subject, object, inList);
        } else if (entail == Entail.memberOf) {
          processMatchIsMemberOf(whereQl, subject, object, inList);
        } else
          processMatchEqual(whereQl, subject, inList);
        whereQl.append("}\n");
      }
      if (!outTypes.isEmpty()) {
        for (Map.Entry<Entail, List<String>> entry : outTypes.entrySet()) {
          o++;
          String object = "instance" + o;
          Entail entail = entry.getKey();
          List<String> out = entry.getValue();
          String outList = String.join(" ", out);
          if (entail == Entail.descendantsOrSelfOf) {
            whereQl.append("Filter not exists {");
            processMatchIsDescendantsOrSelfOf(whereQl, subject, object, outList);
            whereQl.append("}");
          } else if (entail == Entail.descendantsOf) {
            whereQl.append("Filter not exists {");
            processMatchIsDescendantsOf(whereQl, subject, object, outList);
            whereQl.append("}");
          } else if (entail == Entail.ancestorsOf) {
            whereQl.append("Filter not exists {");
            processMatchIsAncestorOf(whereQl, subject, object, outList);
            whereQl.append("}");
          } else if (entail == Entail.memberOf) {
            whereQl.append("Filter not exists {");
            processMatchIsMemberOf(whereQl, subject, object, outList);
            whereQl.append("}");
          } else
            processMatchNotEqual(whereQl, subject, outList);
        }

      }
    }
  }

  private void sortInstances(List<Node> instanceOf, Map<Entail, List<String>> inTypes, Map<Entail, List<String>> outTypes) throws QueryException {
    for (Node instance : instanceOf) {
      Entail entail = Entail.equal;
      if (instance.isMemberOf())
        entail = Entail.memberOf;
      else if (instance.isDescendantsOrSelfOf())
        entail = Entail.descendantsOrSelfOf;
      else if (instance.isDescendantsOf())
        entail = Entail.descendantsOf;
      else if (instance.isAncestorsOf())
        entail = Entail.ancestorsOf;
      if (instance.isExclude()) {
        outTypes.computeIfAbsent(entail, m -> new ArrayList<>()).add(iriFromAlias(instance));
      } else
        inTypes.computeIfAbsent(entail, m -> new ArrayList<>()).add(iriFromAlias(instance));
    }
  }

  private void processMatchIsMemberOf(StringBuilder whereQl, String subject, String object, String inList) {
    whereQl.append("?").append(subject).append(" ^").append("<").append(IM.HAS_MEMBER).append("> ").append("?").append(object).append("\n");
    whereQl.append("Values ").append("?").append(object).append(" {").append(inList).append(" }\n");

  }

  private void processMatchIsDescendantsOrSelfOf(StringBuilder whereQl, String subject, String object, String inList) {
    whereQl.append("?").append(subject).append(" im:isA ?").append(object).append(".\n");
    whereQl.append("Values ").append("?").append(object).append(" {").append(inList).append(" }\n");
  }

  private void processMatchIsDescendantsOf(StringBuilder whereQl, String subject, String object, String inList) {
    whereQl.append("?").append(subject).append(" im:isA ?").append(object).append(".\n");
    whereQl.append("Values ").append("?").append(object).append(" {").append(inList).append(" }\n");
    whereQl.append("Filter (?").append(subject).append(" not in (").append(inList.replace(" ", ",")).append("))\n");
  }

  private void processMatchIsAncestorOf(StringBuilder whereQl, String subject, String object, String inList) {
    o++;
    whereQl.append("?").append(subject).append(" ^im:isA ?").append(object).append(".\n");
    whereQl.append("Values ").append("?").append(object).append(" {").append(inList).append(" }\n");
  }

  private void processMatchEqual(StringBuilder whereQl, String subject, String inList) {
    whereQl.append("?").append(subject).append(" rdf:type ?type.\n");
    whereQl.append("Values ?").append(subject).append(" {").append(inList).append(" }\n");
  }


  private void processMatchNotEqual(StringBuilder whereQl, String subject, String outList) {
    whereQl.append("Filter (?").append(subject);
    if (!outList.contains(" "))
      whereQl.append(" =").append(outList).append(")\n");
    else
      whereQl.append(" not in (").append(outList.replace(" ", ",")).append("))\n");
  }

  private void processTypeOf(StringBuilder whereQl, Node type, String subject) throws QueryException {
    String typeIris = iriFromAlias(type);
    if (type.isDescendantsOrSelfOf()) {
      o++;
      String supertype = "supertype" + o;
      whereQl.append("?").append(subject).append(" im:isA ?").append(supertype).append(".\n");
      whereQl.append(" VALUES ?").append(supertype).append(" {").append(typeIris).append(" }\n");
    } else if (type.isDescendantsOf()) {
      o++;
      String supertype = "supertype" + o;
      whereQl.append("?").append(subject).append(" im:isA ?").append(supertype).append(".\n");
      whereQl.append(" VALUES ?").append(supertype).append(" {").append(typeIris).append(" }\n");
      whereQl.append("Filter (?").append(subject).append("!= ").append(typeIris).append(")\n");
    } else {
      if (typeIris.contains(" ")) {
        o++;
        String types = "types" + o;
        whereQl.append("?").append(subject).append(" rdf:type ").append(types).append(".\n");
        whereQl.append(" VALUES ?").append(types).append(" {").append(typeIris).append(" }\n");
      } else
        whereQl.append("?").append(subject).append(" rdf:type ").append(typeIris).append(".\n");
    }
  }

  /**
   * Proecesses the processWhere processWhere clause, the remaining processMatch clause including subqueries
   *
   * @param whereQl the string builder sparql
   * @param subject the parent subject passed to this processWhere clause
   * @param where   the processWhere clause
   */
  private void processWhere(StringBuilder whereQl, String subject, Where where, String pathVariable, boolean isInRoleGroup) throws QueryException {
    if (pathVariable != null) {
      subject = pathVariable;
    }
    String propertyVariable = null;
    if (!where.isInverse()) {
      if (where.getVariable() != null) propertyVariable = where.getVariable();
    }
    if (where.getNodeRef() != null) {
      subject = where.getNodeRef();
    }
    if (where.isAnyRoleGroup() && !where.isInverse() && !isInRoleGroup) {
      whereQl.append("?").append(subject).append(" im:roleGroup ").append("?roleGroup").append(o).append(".\n");
      subject = "roleGroup" + o;
      o++;
    }
    if (where.isAnyRoleGroup() && where.isInverse() && !isInRoleGroup) {
      whereQl.append("?").append(subject).append(" ^im:roleGroup ").append("?roleGroup").append(o).append(".\n");
      subject = "roleGroup" + o;
      o++;
    }
    if (where.isRoleGroup() && !isInRoleGroup) {
      whereQl.append("?").append(subject).append(" im:roleGroup ").append("?roleGroup").append(".\n");
      subject = "roleGroup";
      processNestedWheres(where, whereQl, subject, pathVariable, true);
      return;
    }
    if (where.getIri() != null || where.getParameter() != null) {
      if (where.getIsNull()) {
        whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
      }
      processWhereProperty(whereQl, subject, where);
      if (where.getIs() != null) {
        processWhereIs(whereQl, where, false);
      } else if (where.getNotIs() != null) {
        processWhereIs(whereQl, where, true);
      } else if (where.getValue() != null) {
        whereValue(whereQl, where);
      } else if (where.getValueVariable() != null) {
        whereQl.append(" ?").append(where.getValueVariable()).append(".\n");
      }
      if (where.getIsNull()) {
        whereQl.append("}\n");
      }
    }
    processNestedWheres(where, whereQl, subject, pathVariable, isInRoleGroup);
  }

  private void processWhereProperty(StringBuilder whereQl, String subject, Where where) throws QueryException {
    if (where.getIri() != null && where.getParameter() != null) {
      throw new QueryException("Cannot have both iri and parameter in a where clause");
    }
    String propertyIris = iriFromAlias(where).replace(",", " ");
    String inverse = where.isInverse() ? "^" : "";
    o++;
    String propertyVariable = where.getVariable() != null ? where.getVariable() : "property" + o;
    if (where.isInverse() && (where.getParameter() != null || where.getVariable() != null || where.isDescendantsOrSelfOf() || where.isAncestorsOrSelfOf())) {
      throw new QueryException("Inverse propertyPath with parameters or variables or entailments are not supported\"");
    }
    if (where.isDescendantsOrSelfOf()) {
      String superProperty = "superProperty" + o;
      whereQl.append("VALUES ?").append(superProperty).append(" {").append(propertyIris).append("}").append("\n.");
      whereQl.append("?").append(propertyVariable).append(" im:isA ?").append(superProperty).append(".\n");
      whereQl.append("?").append(subject).append(" ?").append(propertyVariable);
    } else if (where.isAncestorsOrSelfOf()) {
      String subProperty = "subProperty" + o;
      whereQl.append("VALUES ?").append(subProperty).append(" {").append(propertyIris).append("}").append("\n.");
      whereQl.append("?").append(subProperty).append(" im:isA ?").append(propertyVariable);
      whereQl.append("?").append(subject).append(" ?").append(propertyVariable);
    } else {
      if (propertyIris.contains(" ")) {
        whereQl.append("VALUES ?").append(propertyVariable).append(" {").append(propertyIris).append("}").append("\n");
        whereQl.append("?").append(subject).append(" ").append(propertyVariable);
      } else {
        whereQl.append("?").append(subject).append(" ").append(inverse).append(propertyIris);
      }
    }

  }

  private void processNestedWheres(Where where, StringBuilder whereQl, String subject, String pathVariable, boolean isInRoleGroup) throws QueryException {
    if (where.getAnd() != null) {
      for (Where and : where.getAnd()) {
        processWhere(whereQl, subject, and, pathVariable, isInRoleGroup);
      }
    }
    if (where.getOr() != null) {
      whereQl.append("{\n");
      int i = -1;
      for (Where or : where.getOr()) {
        i++;
        if (i == 0) whereQl.append("{\n");
        else whereQl.append(" UNION {\n");
        processWhere(whereQl, subject, or, pathVariable, isInRoleGroup);
        whereQl.append("}\n");
      }
      whereQl.append("}\n");
    }
    if (where.getNot() != null) {
      whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
      whereQl.append("{\n");
      int i = -1;
      for (Where not : where.getNot()) {
        i++;
        if (i == 0) whereQl.append("{\n");
        else whereQl.append(" UNION {\n");
        processWhere(whereQl, subject, not, pathVariable, isInRoleGroup);
        whereQl.append("}\n");
      }
      whereQl.append("}}\n");
    }
  }

  private void processWhereIs(StringBuilder whereQl, Where where, boolean isNot) throws QueryException {
    String not = isNot ? " not " : "";
    List<Node> in = isNot ? where.getNotIs() : where.getIs();
    boolean subTypes = false;
    boolean superTypes = false;
    for (Element item : in) {
      if (item.isDescendantsOrSelfOf()) {
        subTypes = true;
        break;
      }
    }
    for (Element item : in) {
      if (item.isAncestorsOf() || item.isAncestorsOrSelfOf()) {
        superTypes = true;
        break;
      }
    }
    String nodeRef = in.getFirst().getNodeRef() != null ? in.getFirst().getNodeRef() : null;
    String parameter = in.getFirst().getParameter() != null ? in.getFirst().getParameter() : null;
    List<String> inList = new ArrayList<>();
    for (Element iri : in) {
      inList.add(iriFromAlias(iri));
    }
    String inString = String.join(" ", inList);
    o++;
    String object = nodeRef != null ? nodeRef : parameter != null ? parameter : "object" + o;
    if (subTypes) {
      String superObject = "super" + o;
      whereQl.append(" ?").append(object).append(".\n");
      whereQl.append(" ?").append(object).append(" im:isA ?").append(superObject).append(".\n");
      whereQl.append("VALUES ?").append(superObject).append(" { ").append(inString).append("}").append("\n");
    } else if (superTypes) {
      String subObject = "sub" + o;
      whereQl.append(" ?").append(object).append(".\n");
      whereQl.append(" ?").append(subObject).append(" im:isA ?").append(object).append(".\n");
      whereQl.append("VALUES ?").append(subObject).append(" { ").append(inString).append("}").append("\n");
    } else {
      if (inString.contains(" ")) {
        whereQl.append(" ?").append(object).append(".\n");
        whereQl.append("VALUES ?").append(object).append(" { ").append(inString).append("}").append("\n");
      } else
        whereQl.append(inString).append(".\n");
    }
  }

  private void whereValue(StringBuilder whereQl, Where where) {
    String comp = where.getOperator() != null ? where.getOperator().getValue() : Operator.eq.getValue();
    o++;
    String object = "value" + o;
    whereQl.append(" ?").append(object);
    whereQl.append("Filter (?").append(object).append(comp).append(" ");
    whereQl.append(convertValue(where));
    whereQl.append(")\n");
  }

  private String convertValue(Where value) {
    return "\"" + value.getValue() + "\"";
  }

  private void convertReturn(StringBuilder selectQl, StringBuilder whereQl, Return aReturn) throws QueryException {
    if (aReturn.getNodeRef() != null)
      selectQl.append(" ?").append(aReturn.getNodeRef());
    else if (aReturn.getAs() != null) {
      selectQl.append(" ?").append(aReturn.getAs());
    } else if (aReturn.getPropertyRef() != null) {
      selectQl.append(" ?").append(aReturn.getPropertyRef());
    }
    if (aReturn.getProperty() != null) {
      for (ReturnProperty path : aReturn.getProperty()) {
        convertReturnProperty(selectQl, whereQl, aReturn, path);
      }
    }
  }

  private void convertReturnProperty(StringBuilder selectQl, StringBuilder whereQl, Return aReturn,
                                     ReturnProperty property) throws QueryException {
    String inverse = property.isInverse() ? "^" : "";
    if (property.getIri() == null) {
      if (property.getNodeRef() != null) {
        selectQl.append(" ").append(inverse).append(property.getNodeRef());
        // labelVariable = property.getNodeRef();
      } else if (property.getPropertyRef() != null) {
        selectQl.append(" ").append(inverse).append(property.getPropertyRef());
        // labelVariable = property.getPropertyRef();
      } else if (property.getAs() != null) {
        selectQl.append(" ").append(inverse).append("?").append(property.getAs());
        // labelVariable = property.getAs();
      } else if (property.getValueRef() != null) {
        selectQl.append(" ").append("?").append(property.getValueRef());
        // labelVariable = property.getValueRef();
      }
      if (property.getReturn() != null) {
        convertReturn(selectQl, whereQl, property.getReturn());
      }
    } else {
      whereQl.append("OPTIONAL {");
      if (aReturn.getAs() != null)
        whereQl.append(" ?").append(aReturn.getAs());
      else if (aReturn.getNodeRef() != null)
        whereQl.append(" ?").append(aReturn.getNodeRef());
      else
        whereQl.append(" ?").append(mainEntity);
      if (property.getIri() != null) {
        whereQl.append(" ").append(inverse).append(iriFromString(property.getIri()));
      } else
        whereQl.append(" ").append(inverse).append("?").append(property.getPropertyRef());
      String object;
      object = getReturnObject(property);
      if (object == null) {
        o++;
        object = "o" + o;
        property.setValueRef(object);
        if (property.getReturn() != null) {
          property.getReturn().setAs(object);
        }
      }
      whereQl.append(" ?").append(object).append(".\n");
      if (!selectQl.toString().contains(object)) selectQl.append(" ?").append(object);
      if (property.getIri() != null && property.getIri().equals(RDFS.LABEL))
        // labelVariable = object;
      if (property.getReturn() != null) {
        convertReturn(selectQl, whereQl, property.getReturn());
      }
      whereQl.append("}\n");
    }
  }

  private String getReturnObject(ReturnProperty property) {
    if (property.getReturn() != null) {
      if (property.getReturn().getAs() != null)
        return property.getReturn().getAs();
      else if (property.getReturn().getNodeRef() != null)
        return property.getReturn().getNodeRef();
      else if (property.getValueRef() != null) {
        property.getReturn().setAs(property.getValueRef());
        return property.getValueRef();
      } else if (property.getAs() != null) {
        property.getReturn().setAs(property.getAs());
        return property.getAs();
      }
    } else {
      if (property.getValueRef() != null)
        return property.getValueRef();
      else if (property.getAs() != null)
        return property.getAs();
      else if (property.getNodeRef() != null)
        return property.getNodeRef();
    }
    return null;
  }


  private void orderGroupLimit(StringBuilder selectQl, Query clause, boolean countOnly, boolean highestUsage) throws QueryException {
    if (null != clause.getGroupBy()) {
      selectQl.append("Group by ");
      for (GroupBy property : clause.getGroupBy()) {
        if (property.getPropertyRef() != null) {
          selectQl.append(" ?").append(property.getPropertyRef());
        }
      }
    }
    if (null != clause.getOrderBy() && !countOnly && null != clause.getOrderBy().getProperty()) {
      generateOrderBy(selectQl, clause);
    }

    if (null != queryRequest.getPage() && queryRequest.getTextSearch() == null) {
      selectQl.append("LIMIT ").append(queryRequest.getPage().getPageSize()).append("\n");
      if (queryRequest.getPage().getPageNumber() > 0)
        selectQl.append("OFFSET ").append((queryRequest.getPage().getPageNumber() - 1) * (queryRequest.getPage().getPageSize())).append("\n");
    }
  }

  private void generateOrderBy(StringBuilder selectQl, Query clause) throws QueryException {
    selectQl.append("Order by ");
    for (OrderDirection order : clause.getOrderBy().getProperty()) {
      if (null != order.getDirection() && order.getDirection().equals(Order.descending))
        selectQl.append("DESC(");
      else
        selectQl.append("ASC(");
      if (null != order.getIri()) selectQl.append("?").append(order.getIri());
      else if (null != order.getValueVariable()) selectQl.append("?").append(order.getValueVariable());
      else throw new QueryException("Order by missing identifier: iri / valueVariable");
      selectQl.append(")");
    }
    if (null == queryRequest.getPage() && clause.getOrderBy().getLimit() > 0) {
      selectQl.append("LIMIT ").append(clause.getOrderBy().getLimit()).append("\n");
    } else {
      selectQl.append("\n");
    }
  }

  public String iriFromAlias(Element alias) throws QueryException {
    return getIriFromAlias(alias.getIri(), alias.getParameter(), alias.getVariable(), alias.getNodeRef());
  }

  public String iriFromAlias(Where alias) throws QueryException {
    return getIriFromAlias(alias.getIri(), alias.getParameter(), null, null);
  }

  private String getIriFromAlias(String id, String parameter, String variable, String valueRef) throws QueryException {
    if (null == id) {
      if (null != parameter) {
        return iriFromString(resolveReference(parameter, queryRequest));
      } else if (null != variable) {
        return "?" + variable;
      } else if (null != valueRef) {
        return "?" + valueRef;
      } else return null;
    } else
      return (iriFromString(id));
  }

  public String iriFromAlias(Node alias) throws QueryException {
    return getIriFromAlias(alias.getIri(), alias.getParameter(), alias.getVariable(), alias.getNodeRef());
  }

  public String iriFromAliases(List<Node> aliases) throws QueryException {
    List<String> iriList = new ArrayList<>();
    for (Node alias : aliases) {
      iriList.add(getIriFromAlias(alias.getIri(), alias.getParameter(), alias.getVariable(), alias.getNodeRef()));
    }
    return String.join(" ", iriList);
  }


  public String getUpdateSparql() throws QueryException {
    StringBuilder updateQl = new StringBuilder();
    StringBuilder whereQl = new StringBuilder();
    whereQl.append("WHERE { GRAPH ?g {");
    for (Match match : update.getMatch()) {
      processMatch(whereQl, "entity", match, false);
    }
    if (update.getDelete() != null) {
      updateQl.append("DELETE { ");
      for (Delete delete : update.getDelete()) {
        delete(updateQl, whereQl, delete);
      }
      updateQl.append("}");
    }
    updateQl.append("\n");

    whereQl.append("}}");

    updateQl.append(whereQl).append("\n");
    return updateQl.toString();

  }


  private void delete(StringBuilder updateQl, StringBuilder whereQl, Delete delete) throws QueryException {
    if (delete.getSubject() == null)
      updateQl.append("?").append("entity");
    if (delete.getPredicate() != null) {
      updateQl.append(" ").append(iriFromAlias(delete.getPredicate()));
      if (delete.getObject() != null) {
        updateQl.append(" ").append(iriFromAlias(delete.getObject())).append(".\n");
      } else {
        o++;
        updateQl.append(" ").append("?object").append(o).append(".\n");
        whereQl.append("?").append("entity").append(" ").append(iriFromAlias(delete.getPredicate())).append(" ").append(o).append("\n");
      }
    } else {
      o++;
      updateQl.append(" ").append("?predicate").append(o).append(" ?object").append(o).append(".\n");
      whereQl.append("?").append("entity").append(" ?predicate").append(o).append(" ?object").append(o).append(".\n");
    }
  }

}
