package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.queryengine.QueryValidator;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.*;
import java.util.stream.Collectors;

public class SparqlConverter {
  private final QueryRequest queryRequest;
  private final String tabs = "";
  int o = 0;
  String mainEntity;
  private Match query;
  private Update update;
  private Set<TTIriRef> statusFilter;



  public SparqlConverter(QueryRequest queryRequest) throws QueryException {
    this.queryRequest = queryRequest;
    if (queryRequest.getQuery() != null) {
      this.query = queryRequest.getQuery();
      new QueryValidator().validateQuery(this.query);
    } else
      this.update = queryRequest.getUpdate();

  }

  public static String iriFromString(String iri) {
    if (iri.startsWith("http") || iri.startsWith("urn:"))
      return "<" + iri + ">";
    else return iri;
  }

  /**
   * Resolves a query $ variable value using the query request argument map
   *
   * @param value        the $alias is the query definition
   * @param queryRequest the Query request object submitted via the API
   * @return the value of the variable as a String
   * @throws QueryException if the variable is unresolvable
   */
  public String resolveReference(String value, QueryRequest queryRequest) throws QueryException {
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
      } else if (null != argument.getValueParameter()) {
        return argument.getValueParameter();
      } else if (null != argument.getValueDataList()) {
        if (argument.getValueDataList().isEmpty())
          throw new QueryException("Argument parameter " + value + " valueDataList cannot be empty");
        return String.join(",", argument.getValueDataList());
      } else if (null != argument.getValuePath()) {
        return argument.getValuePath().getIri();
      }
    }
    return null;
  }

  /**
   * Takes an IMQ select query model and converts to SPARQL
   *
   * @return String of SPARQL
   **/
  public String getSelectSparql(Set<TTIriRef> statusFilter,boolean countOnly,boolean highestUsage) throws QueryException {
    this.statusFilter = statusFilter;
    return getFullSelectSparql(this.query,countOnly,highestUsage);
  }

  public String getSelectSparql(boolean countOnly,boolean highestUsage) throws QueryException {
    return getFullSelectSparql(this.query,countOnly,highestUsage);
  }


  public String getSelectSparql(Match match, Set<TTIriRef> statusFilter, boolean countOnly, boolean highestUsage) throws QueryException {
    this.statusFilter=statusFilter;

    return getFullSelectSparql(match,countOnly,highestUsage);
  }

  private String getFullSelectSparql(Match match,boolean countOnly,boolean highestUsage) throws QueryException {
    StringBuilder fullSelectQl = new StringBuilder();
    fullSelectQl.append(getSelectSparql(match,countOnly));
    orderGroupLimit(fullSelectQl, match, countOnly, highestUsage);
    return fullSelectQl.toString();
  }



  private String getSelectSparql(Match match,boolean countOnly) throws QueryException {
    mainEntity = match.getNode() != null ? match.getNode() : "entity";
    StringBuilder selectQl = new StringBuilder();
    if (countOnly) {
      selectQl.append("SELECT");
        selectQl.append(" (count (distinct ?").append(mainEntity).append(") as ?count)");
    } else {
      selectQl.append("SELECT ");
      selectQl.append("distinct ");
        selectQl.append("?").append(mainEntity);
    }
    processQuery(match,selectQl, !countOnly);
    return selectQl.toString();

  }

  public String getAskSparql(Set<TTIriRef> statusFilter) throws QueryException {

    StringBuilder askQl = new StringBuilder();

    askQl.append("ASK ");
    processQuery(query,askQl, false);

    return askQl.toString();
  }

  private void processQuery(Match match, StringBuilder sparql, boolean includeReturns) throws QueryException {
    StringBuilder whereQl = new StringBuilder();
    mainEntity="entity";
    if (match.getNode()!=null)
      mainEntity = match.getNode();
    if (match.getNodeRef() != null)
      mainEntity = match.getNodeRef();
    if (match.getParameter() != null)
      mainEntity = match.getParameter().replace("$","");
    whereQl.append("WHERE {");
    if (match.getTypeOf() != null) {
      whereQl.append("?").append(mainEntity).append(" rdf:type ").append(iriFromString(match.getTypeOf().getIri())).append(".\n");
    }
    if (queryRequest.getAskIri() != null) {
      whereQl.append(" VALUES ").append("?").append(mainEntity).append("{").append(iriFromString(queryRequest.getAskIri())).append("}\n");
    }
    processMatch(sparql,whereQl, mainEntity, match);

    if ((includeReturns) && null != match.getReturn()) {
      for (Return returnProperty : match.getReturn()) {
        processReturn(sparql, whereQl, returnProperty,mainEntity);
      }
    }

    o++;
    String statusVar = "status" + o;

    if (null != statusFilter && !statusFilter.isEmpty()) {
      List<String> statusStrings = new ArrayList<>();
      for (TTIriRef status : statusFilter) {
        statusStrings.add("<" + status.getIri() + ">");
      }
      whereQl.append("?").append(mainEntity).append(" im:status ?").append(statusVar).append(".\n");
      whereQl.append("VALUES ?").append(statusVar).append("{").append(String.join(" ", statusStrings)).append("}\n");
    }
    if (match.isActiveOnly()) {
      whereQl.append("?").append(mainEntity).append(" im:status im:Active.\n");
    }


    sparql.append("\n");

    whereQl.append("}");

    sparql.append(whereQl).append("\n");
  }

  public String getCountSparql(Set<TTIriRef> statusFilter) throws QueryException {
    return getSelectSparql(this.query,statusFilter,true,false);
  }

  private String escape(String text) {
    return text.replaceAll("[(\\-){}^\"/~\\\\]", " ").replaceAll("\\s+", " ").trim();
  }


  private void processMatch(StringBuilder selectQl,StringBuilder whereQl, String parent, Match match) throws QueryException {
    StringBuilder subselects=new StringBuilder();
    if (match.notExists()) {
      whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
    }
    String subject;
    if (match.getNodeRef() != null)
      subject = match.getNodeRef();
    else if (match.getNode() != null)
      subject = match.getNode();
    else if (match.getParameter() != null) {
      subject = match.getParameter().replace("$","");
      whereQl.append(" VALUES ").append("?").append(subject).append("{").append(getIriFromAlias(null, match.getParameter(), null, null)).append("}\n");
    }
    else
      subject = parent;
    String mainSubject = subject;
    if (match.getEntailment() != null) {
      if (match.getEntailment() == Entail.descendantsOrSelfOf) {
        o++;
        whereQl.append("?").append(subject).append(" <").append(IM.IS_A).append("> ?").append(subject).append(o).append(".\n");
        subject = subject + o;
      } else {
        throw new QueryException("Match entailment " + match.getEntailment() + " is not yet supported");
      }
    }
    if (match.getTypeOf() != null) {
      processTypeOf(whereQl, match.getTypeOf(), subject);
    }
    if (match.getIs() != null) {
      processMatchInstanceOf(match, whereQl, subject);
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
        if (subMatch.getNode()!=null){
          subselects.append("{").append(getSelectSparql(subMatch,false)).append("}\n");
        }
        else
          processMatch(selectQl,whereQl, subject, subMatch);
      }
    }
    if (match.getOr() != null) {
      for (int i = 0; i < match.getOr().size(); i++) {
        if (i == 0)
          whereQl.append("{ \n");
        else
          whereQl.append("UNION {\n");
        Match subMatch = match.getOr().get(i);
        processMatch(selectQl,whereQl, subject, subMatch);
        whereQl.append("}\n");
      }
    }

    o++;
    String object = "object" + o;

    if (match.getWhere() != null) {
      processWhere(whereQl, subject, match.getWhere(), false);
    }
    if (match.getGraph() != null) {
      whereQl.append("}");
    }
    if (!subselects.toString().isEmpty())
      whereQl.append(subselects);
    if (match.notExists()) {
      whereQl.append("}\n");
    }
  }

  private String processPath(StringBuilder whereQl, String subject, Path pathMatch) throws QueryException {
    if (pathMatch.isOptional()){
      whereQl.append(tabs).append("OPTIONAL {\n");
    }
    String inverse = pathMatch.isInverse() ? "^" : "";
    String object= pathMatch.getNode();
    String predicate = getIriFromAlias(pathMatch.getIri(), pathMatch.getParameter(), pathMatch.getPathVariable(), null);
    if (inverse.equals("^") && predicate.startsWith("?"))
      throw new QueryException("Inverse processPath processMatch cannot have a variable as predicate");
    whereQl.append("?").append(subject).append(" ").append(inverse).append(predicate).append(" ?").append(object).append(".\n");
    if (pathMatch.getPath() != null) {
      for (Path path : pathMatch.getPath()) {
        object = processPath(whereQl, object, path);
      }
    }
    if (pathMatch.isOptional()){
      whereQl.append("}\n");
    }
    return object;
  }

  private String getInReference(String subject,List<Node> nodes,Set<String> inSet) throws QueryException {
    String ref=null;
    for (Node node : nodes) {
      if (ref!=null)
        throw new QueryException("in list cannot be longer than one if there are references");
      if (node.getMatch()!=null){
        if (node.getMatch().getNode()!=null){
          ref= node.getMatch().getNode();
        }
        else {
          o++;
          ref = "sub" + subject + o;
          node.getMatch().setNode(ref);
        }
      }
      if (node.getNodeRef()!=null)
        ref=node.getNodeRef();
      if (node.getParameter()!=null)
        inSet.add(iriFromAlias(node));
      if (node.getNode()!=null)
        ref= node.getNode();
      else if (node.getIri()!=null)
        inSet.add("<"+ node.getIri()+">");
    }
    return ref;
  }

  private void processMatchInstanceOf(Match match, StringBuilder whereQl, String subject) throws QueryException {
    Map<Entail, List<Node>> inTypes = new HashMap<>();
    Map<Entail, List<Node>> outTypes = new HashMap<>();
    sortInstances(match.getIs(), inTypes, outTypes);
    processMatchIsNodes(whereQl,subject,inTypes, false);
    if (!outTypes.isEmpty()) {
      processMatchIsNodes(whereQl, subject, outTypes, true);
    }
  }

  private void processMatchIsNodes(StringBuilder whereQl,String subject,Map<Entail, List<Node>> isTypes, boolean negation) throws QueryException {
    boolean first = true;
    if (negation) whereQl.append("Filter not exists {");
    boolean isUnion= (isTypes.size()>1);
    for (Entail entail : isTypes.keySet()) {
        o++;
        String object = "instance" + o;
        List<Node> in = isTypes.get(entail);
        Set<String> inSet=new HashSet<>();
        String ref=getInReference(subject,in,inSet);
        if (ref!=null)
          object= ref;
        else {
          o++;
          object="instance" + o;
        }
        String inList = !inSet.isEmpty() ? String.join(" ", inSet): null;
        if (first&&isUnion)
          whereQl.append("{");
        else if (!first)
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
        if (in.getFirst().getMatch()!=null){
          whereQl.append("{").append(getSelectSparql(in.getFirst().getMatch(),false)).append("}\n");
        }
        if (isUnion)
          whereQl.append("}\n");
      }
    if (negation) whereQl.append("}");
  }

  private void sortInstances(List<Node> is, Map<Entail, List<Node>> inTypes, Map<Entail, List<Node>> outTypes) throws QueryException {
    for (Node instance : is) {
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
        outTypes.computeIfAbsent(entail, m -> new ArrayList<>()).add(instance);
      } else
        inTypes.computeIfAbsent(entail, m -> new ArrayList<>()).add(instance);
    }
  }

  private void processMatchIsMemberOf(StringBuilder whereQl, String subject, String object, String inList) {
    whereQl.append("?").append(subject).append(" ^").append("<").append(IM.HAS_MEMBER).append("> ").append("?").append(object).append("\n");
    if (inList!=null)
      whereQl.append("Values ").append("?").append(object).append(" {").append(inList).append(" }\n");

  }

  private void processMatchIsDescendantsOrSelfOf(StringBuilder whereQl, String subject, String object, String inList) {
    whereQl.append("?").append(subject).append(" im:isA ?").append(object).append(".\n");
    if (inList!=null)
      whereQl.append("Values ").append("?").append(object).append(" {").append(inList).append(" }\n");
  }

  private void processMatchIsDescendantsOf(StringBuilder whereQl, String subject, String object, String inList) {
    whereQl.append("?").append(subject).append(" im:isA ?").append(object).append(".\n");
    if (inList!=null) {
      whereQl.append("Values ").append("?").append(object).append(" {").append(inList).append(" }\n");
      whereQl.append("Filter (?").append(subject).append(" not in (").append(inList.replace(" ", ",")).append("))\n");
    }
  }

  private void processMatchIsAncestorOf(StringBuilder whereQl, String subject, String object, String inList) {
    o++;
    whereQl.append("?").append(subject).append(" ^im:isA ?").append(object).append(".\n");
    if (inList!=null)
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
  private void processWhere(StringBuilder whereQl, String subject, Where where, boolean isInRoleGroup) throws QueryException {
    if (where.getIs() != null&&where.isNot()) {
      throw new QueryException("isNotIs not supported in a where clause");
    }
    if (where.isExists()){
      whereQl.append("FILTER EXISTS {\n");
    }
    if (where.getNodeRef() != null) {
      subject = where.getNodeRef();
    }
    if (where.getSubjectVariable()!=null){
      subject= where.getSubjectVariable();
    }
    if (where.getSubjectParameter()!=null){
      subject= where.getSubjectParameter();
      whereQl.append("VALUES ?").append(subject).append(" {").append(resolveReference(where.getSubjectParameter(), queryRequest)).append("}\n");
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
      processNestedWheres(where, whereQl, subject, true);
      return;
    }
    if (where.getIri() != null || where.getParameter() != null||where.getPropertyVariable()!=null) {
      if (where.getIsNull()) {
        whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
      }
      processWhereProperty(whereQl, subject, where);
      if (where.getIs() != null) {
        processWhereIs(whereQl, where);
      } else if (where.getValue() != null) {
        whereValue(whereQl, where);
      } else if (where.getNode() != null) {
        whereQl.append(" ?").append(where.getNode()).append(".\n");
      }
      else {
        o++;
        whereQl.append(" ?").append(subject).append("o").append(o);
      }
      if (where.getIsNull()) {
        whereQl.append("}\n");
      }
      if (where.getExcludeProperty()!=null) {
        for (IriLD exclude:where.getExcludeProperty()) {
          whereQl.append("FILTER (?").append(where.getPropertyVariable()).append(" !=<").append(exclude.getIri()).append(">)\n");
        }
      }
    }
    processNestedWheres(where, whereQl, subject, isInRoleGroup);
    if (where.isExists()){
      whereQl.append("}\n");
    }
  }

  private void processWhereProperty(StringBuilder whereQl, String subject, Where where) throws QueryException {
    if (where.getIri() != null && where.getParameter() != null) {
      throw new QueryException("Cannot have both iri and parameter in a where clause");
    }
    String propertyIris;
    String propertyVariable= where.getPropertyVariable();
    if (where.getPropertyList()!=null) {
      propertyIris = String.join(" ", where.getPropertyList().stream().map(p -> "<" + p.getIri() + ">").toList());
    } else  propertyIris= iriFromAlias(where);
    String inverse = where.isInverse() ? "^" : "";
    o++;
    if (where.isInverse() && (where.getParameter() != null || where.getPropertyVariable() != null || where.isDescendantsOrSelfOf() || where.isAncestorsOf())) {
      throw new QueryException("Inverse propertyPath with parameters or variables or entailments are not supported\"");
    }
    if (where.isDescendantsOrSelfOf()) {
      if (propertyIris==null)
        throw new QueryException("Descendants or self of requires a property IRI");
      String superProperty = "superProperty" + o;
      if (propertyVariable==null)
        propertyVariable = "property" + o;
      whereQl.append("VALUES ?").append(superProperty).append(" {").append(propertyIris).append("}").append("\n.");
      whereQl.append("?").append(propertyVariable).append(" im:isA ?").append(superProperty).append(".\n");
      whereQl.append("?").append(subject).append(" ?").append(propertyVariable);
    } else if (where.isAncestorsOf()) {
      if (propertyIris==null)
        throw new QueryException("Ancestors or self of requires a property IRI");
      String subProperty = "subProperty" + o;
      if (propertyVariable==null)
        propertyVariable = "property" + o;
      whereQl.append("VALUES ?").append(subProperty).append(" {").append(propertyIris).append("}").append("\n.");
      whereQl.append("?").append(subProperty).append(" im:isA ?").append(propertyVariable);
      whereQl.append("?").append(subject).append(" ?").append(propertyVariable);
    } else if (propertyIris!=null&&propertyVariable==null) {
      whereQl.append("?").append(subject).append(" ").append(inverse).append(propertyIris);
    } else if (propertyIris!=null) {
        whereQl.append("VALUES ?").append(propertyVariable).append(" {").append(propertyIris).append("}").append("\n");
        whereQl.append("?").append(subject).append(" ?").append(propertyVariable);
    } else {
      whereQl.append("?").append(subject).append(" ?").append(propertyVariable.replace(" ","|"));

    }
  }

  private void processNestedWheres(Where where, StringBuilder whereQl, String subject, boolean isInRoleGroup) throws QueryException {
    if (where.getAnd() != null) {
      for (Where and : where.getAnd()) {
        processWhere(whereQl, subject, and, isInRoleGroup);
      }
    }
    if (where.getOr() != null) {
      whereQl.append("{\n");
      int i = -1;
      for (Where or : where.getOr()) {
        i++;
        if (i == 0) whereQl.append("{\n");
        else whereQl.append(" UNION {\n");
        processWhere(whereQl, subject, or, isInRoleGroup);
        whereQl.append("}\n");
      }
      whereQl.append("}\n");
    }
  }

  private void processWhereIs(StringBuilder whereQl, Where where) throws QueryException {

    List<Node> in = where.getIs();
    boolean subTypes = false;
    boolean superTypes = false;
    boolean membersOf= false;
    for (Node item : in) {
      if (item.isDescendantsOrSelfOf()) {
        subTypes = true;
        break;
      }
    }
    for (Node item : in) {
      if (item.isMemberOf()) {
        membersOf = true;
        break;
      }
    }
    for (Node item : in) {
      if (item.isAncestorsOf()) {
        superTypes = true;
        break;
      }
    }
    String nodeRef = in.getFirst().getNodeRef() != null ? in.getFirst().getNodeRef() : null;
    String parameter = in.getFirst().getParameter() != null ? in.getFirst().getParameter() : null;
    List<String> inList = new ArrayList<>();
    for (Node iri : in) {
      inList.add(iriFromAlias(iri));
    }
    String inString = String.join(" ", inList);
    o++;
    String object = nodeRef != null ? nodeRef : parameter != null ? parameter.replace("$","") : "object" + o;
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
    } else if (membersOf) {
      String memberObject = "member" + o;
      whereQl.append(" ?").append(object).append(".\n");
      whereQl.append(" ?").append(object).append(" ^").append("<").append(IM.HAS_MEMBER).append("> ?").append(memberObject).append(".\n");
      whereQl.append("VALUES ?").append(memberObject).append(" { ").append(inString).append("}").append("\n");
    } else {
      if (inString.contains(" ")) {
        whereQl.append(" ?").append(object).append(".\n");
        whereQl.append("VALUES ?").append(object).append(" { ").append(inString).append("}").append("\n");
      } else
        whereQl.append(" ").append(inString).append(".\n");
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



  private void processReturn(StringBuilder selectQl, StringBuilder whereQl,
                             Return property, String parentObject) throws QueryException {
    if (property.getPropertyRef()!=null){
      selectQl.append(" ?").append(property.getPropertyRef());
    }
    if (property.getPathRef()!=null){
      selectQl.append(" ?").append(property.getPathRef());
    }
    if (property.getNodeRef()!=null&&property.getIri()==null){
      selectQl.append(" ?").append(property.getNodeRef());
    }
    if (property.getIri()!=null) {
      String subject = property.getNodeRef() != null ? property.getNodeRef() : parentObject;
      String as = property.getAs().replace(" ", "");
      whereQl.append("OPTIONAL {").append(" ?").append(subject);
      String inverse = property.isInverse() ? "^" : "";
      whereQl.append(" ").append(inverse).append(iriFromString(property.getIri()));
      whereQl.append(" ?").append(as).append(".\n");
      selectQl.append(" ?").append(as);
      whereQl.append("}\n");
    }
    if (property.getReturn()!=null) {
      for (Return returnProperty : property.getReturn()) {
        processReturn(selectQl, whereQl, returnProperty, parentObject);
      }
    }
  }


  private void orderGroupLimit(StringBuilder selectQl, Match clause, boolean countOnly, boolean highestUsage) throws QueryException {
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
      if (queryRequest.getPage().getPageSize()>0) {
        selectQl.append("LIMIT ").append(queryRequest.getPage().getPageSize()).append("\n");
        if (queryRequest.getPage().getPageNumber() > 0)
          selectQl.append("OFFSET ").append((queryRequest.getPage().getPageNumber() - 1) * (queryRequest.getPage().getPageSize())).append("\n");
      }
    }
  }

  private void generateOrderBy(StringBuilder selectQl, Match clause) throws QueryException {
    selectQl.append("Order by ");
    for (OrderDirection order : clause.getOrderBy().getProperty()) {
      if (null != order.getDirection() && order.getDirection().equals(Order.descending))
        selectQl.append("DESC(");
      else
        selectQl.append("ASC(");
      if (null != order.getIri()) selectQl.append("?").append(order.getIri());
      else if (null != order.getVariable()) selectQl.append("?").append(order.getVariable());
      else throw new QueryException("Order by missing identifier: iri / valueVariable");
      selectQl.append(")");
    }
    if (null == queryRequest.getPage() && clause.getOrderBy().getLimit() > 0) {
      selectQl.append("LIMIT ").append(clause.getOrderBy().getLimit()).append("\n");
    } else {
      selectQl.append("\n");
    }
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
    return getIriFromAlias(alias.getIri(), alias.getParameter(), alias.getNode(), null);
  }



  public String getUpdateSparql() throws QueryException {
    StringBuilder updateQl = new StringBuilder();
    StringBuilder whereQl = new StringBuilder();
    whereQl.append("WHERE { ");
    for (Match match : update.getMatch()) {
      processMatch(updateQl,whereQl, "entity", match);
    }
    if (update.getDelete() != null) {
      updateQl.append("DELETE { ");
      for (Delete delete : update.getDelete()) {
        delete(updateQl, whereQl, delete);
      }
      updateQl.append("}");
    }
    updateQl.append("\n");

    whereQl.append("}");

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
