package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.queryengine.QueryValidator;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.*;
import java.util.stream.Collectors;

public class SparqlConverter {
  private final QueryRequest queryRequest;
  private final String tabs = "";
  int o = 0;
  String mainEntity;
  private Query query;
  private Update update;
  private String labelVariable;


  public SparqlConverter(QueryRequest queryRequest) throws QueryException {
    this.queryRequest = queryRequest;
    if (queryRequest.getQuery() != null) {
      this.query = queryRequest.getQuery();
      new QueryValidator().validateQuery(this.query);
    } else
      this.update = queryRequest.getUpdate();

  }

  public static String getDefaultPrefixes() {
    TTContext prefixes = new TTContext();
    StringBuilder sparql = new StringBuilder();
    prefixes.add(RDFS.NAMESPACE, "rdfs");
    sparql.append("PREFIX rdfs: <" + RDFS.NAMESPACE + ">\n");
    prefixes.add(RDF.NAMESPACE, "rdf");
    sparql.append("PREFIX rdf: <" + RDF.NAMESPACE + ">\n");
    prefixes.add(IM.NAMESPACE, "im");
    sparql.append("PREFIX im: <" + IM.NAMESPACE + ">\n");
    prefixes.add(XSD.NAMESPACE, "xsd");
    sparql.append("PREFIX xsd: <" + XSD.NAMESPACE + ">\n");
    prefixes.add(SNOMED.NAMESPACE, "sn");
    sparql.append("PREFIX sn: <" + SNOMED.NAMESPACE + ">\n");
    prefixes.add(SHACL.NAMESPACE, "sh");
    sparql.append("PREFIX sh: <").append(SHACL.NAMESPACE).append(">\n\n");
    return sparql.toString();
  }

  /**
   * Resolves a query $ vaiable value using the query request argument map
   *
   * @param value        the $alias is the query definition
   * @param queryRequest the Query request object submitted via the API
   * @return the value of the variable as a String
   * @throws QueryException if the variable is unresolvable
   */
  public static String resolveReference(String value, QueryRequest queryRequest) throws QueryException {
    if (value.equalsIgnoreCase("$referenceDate") && null != queryRequest.getReferenceDate())
      return queryRequest.getReferenceDate();

    value = value.replace("$", "");
    if (null != queryRequest.getArgument()) {
      for (Argument argument : queryRequest.getArgument()) {
        String res = processArgument(argument, value);
        if (res != null) return res;
      }
    }
    return value;
  }

  private static String processArgument(Argument argument, String value) throws QueryException {
    if (argument.getParameter().equals(value)) {
      if (null != argument.getValueData())
        return argument.getValueData();
      else if (null != argument.getValueIri())
        return argument.getValueIri().getIri();
      else if (null != argument.getValueIriList()) {
        if (argument.getValueIriList().isEmpty())
          throw new QueryException("Argument parameter " + value + " valueIriList cannot be empty");
        return argument.getValueIriList().stream().map(TTIriRef::getIri).collect(Collectors.joining(","));
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
   * Takes an IMQ select query model and converts to SPARQL
   *
   * @return String of SPARQL
   **/
  public String getSelectSparql(Set<TTIriRef> statusFilter) throws QueryException {
    return getSelectSparql(statusFilter, false, false);
  }

  public String getSelectSparql(Query query, Set<TTIriRef> statusFilter, boolean countOnly, boolean highestUsage) throws QueryException {
    this.query = query;
    return getSelectSparql(statusFilter, countOnly, highestUsage);
  }

  public String getSelectSparql(Set<TTIriRef> statusFilter, boolean countOnly, boolean highestUsage) throws QueryException {
    mainEntity = query.getVariable() != null ? query.getVariable() : "entity";
    StringBuilder selectQl = new StringBuilder();
    addPrefixes(selectQl);
    if (countOnly) {
      if (null != query.getMatch() && null != query.getMatch().get(0).getVariable())
        mainEntity = query.getMatch().get(0).getVariable();
      if (null != query.getInstanceOf() && null != query.getInstanceOf().get(0).getVariable())
        mainEntity = query.getInstanceOf().get(0).getVariable();
      selectQl.append("SELECT (count (distinct ?").append(mainEntity).append(") as ?count)");
    } else {
      selectQl.append("SELECT ");
      selectQl.append("distinct ");
    }
    addWhereSparql(selectQl, statusFilter, true, countOnly);

    orderGroupLimit(selectQl, query, countOnly, highestUsage);
    return selectQl.toString();

  }

  public String getAskSparql(Set<TTIriRef> statusFilter) throws QueryException {

    StringBuilder askQl = new StringBuilder();
    addPrefixes(askQl);

    askQl.append("ASK ");
    addWhereSparql(askQl, statusFilter, false, false);

    return askQl.toString();

  }

  private void addPrefixes(StringBuilder sparql) {
    sparql.append(getDefaultPrefixes());
  }

  private void addWhereSparql(StringBuilder sparql, Set<TTIriRef> statusFilter, Boolean includeReturns, Boolean countOnly) throws QueryException {
    mainEntity = "entity";
    if (query.getVariable() != null) {
      mainEntity = query.getVariable();
    } else {
      if (null != query.getMatch() && null != query.getMatch().get(0).getVariable())
        mainEntity = query.getMatch().get(0).getVariable();
      if (null != query.getInstanceOf() && null != query.getInstanceOf().get(0).getVariable())
        mainEntity = query.getInstanceOf().get(0).getVariable();
    }
    StringBuilder whereQl = new StringBuilder();
    whereQl.append("WHERE {");
    if (query.getTypeOf() != null) {
      whereQl.append("?").append(mainEntity).append(" rdf:type ").append(iriFromString(query.getTypeOf().getIri())).append(".\n");
    }
    match(whereQl, mainEntity, query);

    if (Boolean.TRUE.equals(!countOnly && includeReturns) && null != query.getReturn()) {
      Return aReturn =query.getReturn();
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

    StringBuilder selectQl = new StringBuilder();
    selectQl.append(getDefaultPrefixes());
    if (null != queryRequest.getTextSearch()) {

      selectQl.append("PREFIX con-inst: <http://www.ontotext.com/connectors/lucene/instance#>\n")
        .append("PREFIX con: <http://www.ontotext.com/connectors/lucene#>\n");
    }

    selectQl.append("SELECT ");
    selectQl.append("(COUNT(distinct ?entity");
    mainEntity = "entity";
    if (null != query.getMatch() && query.getMatch().get(0).getVariable() != null)
      mainEntity = query.getMatch().get(0).getVariable();
    selectQl.append(") as ?cnt) ");
    StringBuilder whereQl = new StringBuilder();
    whereQl.append("WHERE {");
    if (query.getTypeOf() != null) {
      whereQl.append("?").append(mainEntity).append(" rdf:type ").append(iriFromString(query.getTypeOf().getIri())).append(".\n");
    }

    if (null != queryRequest.getTextSearch()) {
      textSearch(whereQl);
    }

    if (null != query.getMatch()) {
      for (Match match : query.getMatch()) {
        match(whereQl, mainEntity, match);
      }
    }

    if (null != query.getInstanceOf()) {
      match(whereQl, mainEntity, query);
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


    selectQl.append("\n");

    whereQl.append("}");

    selectQl.append(whereQl).append("\n");
    return selectQl.toString();

  }

  private String escape(String text) {
    return text.replaceAll("[(\\-){}^\"\\/~\\\\]", " ").replaceAll("\\s+", " ").trim();
  }

  private void textSearch(StringBuilder whereQl) {
    String text = queryRequest.getTextSearch();
    if (query.getReturn() == null) {
      query.return_(s -> s.property(p -> p.setIri(RDFS.LABEL)));
    }
    if (text.split(" ").length > 3) {
      whereQl.append("?").append(mainEntity).append(" rdfs:label ?labelText.\n");
      whereQl.append("filter (strstarts(?labelText,\"").append(text.replace("\"", " ")).append("\"))\n");
    } else {
      text = escape(text);
      whereQl.append("[] a con-inst:im_fts;\n")
        .append("       con:query ");
      String[] words = text.split(" ");
      for (int i = 0; i < words.length; i++) {
        words[i] = "(label:" + words[i] + "*" + ")";
      }
      String searchText = String.join(" && ", words);
      whereQl.append("\"").append(searchText).append("\" ;\n");
      whereQl.append("       con:entities ?").append(mainEntity).append(".\n");
    }
  }

  private void match(StringBuilder whereQl, String parent, Match match) throws QueryException {
    if (match.isExclude()) {
      whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
    }
    String subject;
    if (match.getVariable() != null)
      subject = match.getVariable();
    else if (match.getNodeRef() != null)
      subject = match.getNodeRef();
    else
      subject = parent;

    if (match.getGraph() != null) {
      whereQl.append(" graph ").append(iriFromAlias(match.getGraph())).append(" {");
    }
    if (match.getEntailment()!=null){
      switch (match.getEntailment()){
        case descendantsOrSelfOf :
          o++;
          whereQl.append("?").append(subject).append(" <").append(IM.IS_A).append("> ?").append(subject+o).append(".\n");
          subject= subject+o;
          break;
        default :
          throw new QueryException("Match entailment "+ match.getEntailment().toString()+" is not yet supported");
      }
    }
    if (match.getMatch() != null) {
      if ((match.getBoolMatch() != null && match.getBoolMatch() == Bool.or)) {
        for (int i = 0; i < match.getMatch().size(); i++) {
          if (i == 0)
            whereQl.append("{ \n");
          else
            whereQl.append("UNION {\n");
          Match subMatch = match.getMatch().get(i);
          match(whereQl, subject, subMatch);
          whereQl.append("}\n");
        }
      } else {
        for (Match subMatch : match.getMatch()) {
          match(whereQl, subject, subMatch);
        }
      }
    }

    o++;
    String object = "object" + o;
    if (match.getTypeOf() != null) {
      type(whereQl, match.getTypeOf(), subject);
    } else {
      if (match.getInstanceOf() != null) {
        processMatchInstanceOf(match, whereQl, subject);
      }
    }

    processMatchWhere(whereQl, subject, match.getWhere(), match.getBoolWhere());
    if (match.getGraph() != null) {
      whereQl.append("}");
    }
    if (match.isExclude())
      whereQl.append("}\n");

  }

  private void processMatchWhere(StringBuilder whereQl, String subject, List<Where> where2, Bool boolWhere) throws QueryException {
    if (where2 != null) {
      if (boolWhere == Bool.or) {
        for (int i = 0; i < where2.size(); i++) {
          if (i == 0)
            whereQl.append("{ \n");
          else
            whereQl.append("UNION {\n");
          where(whereQl, subject, where2.get(i), null);
          whereQl.append("}\n");
        }
      } else {
        for (Where where : where2) {
          where(whereQl, subject, where, null);
        }
      }
    }
  }

  private void processMatchInstanceOf(Match match, StringBuilder whereQl, String subject) throws QueryException {
    if (match.getInstanceOf().size()==1) {
      o++;
      String object="instance"+o;
      Node instance= match.getInstanceOf().get(0);
      String inList = iriFromAlias(instance).replace(","," ");
      if (instance.getNodeRef() != null) {
        object = instance.getNodeRef();
      }
      if (instance.isMemberOf()) {
        processMatchIsMemberOf(whereQl, subject,object,inList);
      } else if (instance.isDescendantsOrSelfOf()) {
         processMatchIsDescendantsOrSelfOf(whereQl, subject, object, inList);
      } else if (instance.isDescendantsOf()) {
          processMatchIsDescendantsOf(whereQl, subject, object, inList);
      } else if (instance.isAncestorsOf()) {
        processMatchIsAncestorOf(whereQl, subject, object, inList);
      } else {
        processMatchEqual(whereQl, subject, inList);

      }
    }
    else {
      Map<Entail, List<String>> inTypes = new HashMap<>();
      Map<Entail, List<String>> outTypes = new HashMap<>();
      sortInstances(match.getInstanceOf(), inTypes, outTypes);
      boolean first = true;
      for (Map.Entry<Entail,List<String>> entry:inTypes.entrySet()){
        o++;
        String object="instance"+o;
        Entail entail= entry.getKey();
        List<String> in= entry.getValue();
        String inList= String.join(" ", in);
        if (first)
          whereQl.append("{");
        else
          whereQl.append("UNION {");
        first=false;
        if (entail==Entail.descendantsOrSelfOf){
          processMatchIsDescendantsOrSelfOf(whereQl,subject,object,inList);
        }
        else if (entail==Entail.descendantsOf){
          processMatchIsDescendantsOf(whereQl,subject,object,inList);
        }
        else if (entail==Entail.ancestorsOf){
          processMatchIsAncestorOf(whereQl,subject,object,inList);
        }
        else if (entail==Entail.memberOf){
          processMatchIsMemberOf(whereQl,subject,object,inList);
        }
        else
          processMatchEqual(whereQl,subject,inList);
        whereQl.append("}\n");
      }
      if (!outTypes.isEmpty()){
        for (Map.Entry<Entail,List<String>> entry :outTypes.entrySet()){
          o++;
          String object="instance"+o;
          Entail entail= entry.getKey();
          List<String> out= entry.getValue();
          String outList= String.join(" ", out);
          if (entail==Entail.descendantsOrSelfOf){
            whereQl.append("Filter not exists {");
            processMatchIsDescendantsOrSelfOf(whereQl,subject,object,outList);
            whereQl.append("}");
          }
          else if (entail==Entail.descendantsOf){
            whereQl.append("Filter not exists {");
            processMatchIsDescendantsOf(whereQl,subject,object,outList);
            whereQl.append("}");
          }
          else if (entail==Entail.ancestorsOf){
            whereQl.append("Filter not exists {");
            processMatchIsAncestorOf(whereQl,subject,object,outList);
            whereQl.append("}");
          }
          else if (entail==Entail.memberOf){
            whereQl.append("Filter not exists {");
            processMatchIsMemberOf(whereQl,subject,object,outList);
            whereQl.append("}");
          }
          else
            processMatchNotEqual(whereQl,subject,outList);
        }

      }
    }
  }

  private void sortInstances(List<Node> instanceOf,Map<Entail,List<String>> inTypes,Map<Entail,List<String>> outTypes) throws QueryException {
    for (Node instance:instanceOf){
      Entail entail= Entail.equal;
      if (instance.isMemberOf())
        entail= Entail.memberOf;
      else if (instance.isDescendantsOrSelfOf())
        entail= Entail.descendantsOrSelfOf;
      else if (instance.isDescendantsOf())
        entail= Entail.descendantsOf;
      else if (instance.isAncestorsOf())
        entail= Entail.ancestorsOf;
      if (instance.isExclude()){
        outTypes.computeIfAbsent(entail, m -> new ArrayList<>()).add(iriFromAlias(instance));
      }
      else
        inTypes.computeIfAbsent(entail, m -> new ArrayList<>()).add(iriFromAlias(instance));
      }
  }

  private void processMatchIsMemberOf(StringBuilder whereQl, String subject,String object,String inList){
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
    whereQl.append("Filter (?").append(subject).append("not in (").append(inList.replace(" ",",")).append("))\n");
  }

  private void processMatchIsAncestorOf(StringBuilder whereQl, String subject, String object, String inList) {
    o++;
    whereQl.append("?").append(subject).append(" ^im:isA ?").append("?").append(object).append(".\n");
    whereQl.append("Values ").append("?").append(object).append(" {").append(inList).append(" }\n");
    whereQl.append("Filter (?").append(object);
  }

  private void processMatchEqual(StringBuilder whereQl, String subject, String inList) {
    whereQl.append("?").append(subject).append(" rdf:type ?type.\n");
    whereQl.append("Filter (?").append(subject);
    if (!inList.contains(" "))
      whereQl.append(" =").append(inList).append(")\n");
    else
      whereQl.append(" in (").append(inList.replace(" ",",")).append("))\n");
  }


  private void processMatchNotEqual(StringBuilder whereQl, String subject, String outList) {
   whereQl.append("Filter (?").append(subject);
    if (!outList.contains(" "))
      whereQl.append(" =").append(outList).append(")\n");
    else
      whereQl.append(" not in (").append(outList.replace(" ",",")).append("))\n");
  }

  private void type(StringBuilder whereQl, Node type, String subject) throws QueryException {
    if (type.isDescendantsOrSelfOf()) {
      o++;
      whereQl.append("?").append(subject).append(" im:isA ?").append("supertype").append(o).append(".\n");
      whereQl.append("?supertype").append(o).append(" rdf:type").append(String.join(",", iriFromAlias(type))).append(".\n");
    } else if (type.isDescendantsOf()) {
      o++;
      whereQl.append("?").append(subject).append(" im:isA ?").append("supertype").append(o).append(".\n");
      whereQl.append("?supertype").append(o).append(" rdf:type").append(String.join(",", iriFromAlias(type))).append(".\n");
      whereQl.append("Filter (?").append(subject).append("!= ").append(iriFromAlias(type)).append(")\n");
    } else {
      whereQl.append("?").append(subject).append(" rdf:type ").append(iriFromAlias(type)).append(".\n");
    }
  }

  /**
   * Proecesses the where where clause, the remaining match clause including subqueries
   *
   * @param whereQl the string builder sparql
   * @param subject the parent subject passed to this where clause
   * @param where   the where clause
   */
  private void where(StringBuilder whereQl, String subject, Where where, String parentVariable) throws QueryException {
    String propertyVariable = where.getVariable() != null ? where.getVariable() : parentVariable;
    if (where.isAnyRoleGroup() && !where.isInverse()) {
      whereQl.append("?").append(subject).append(" im:roleGroup ").append("?roleGroup").append(o).append(".\n");
      subject = "roleGroup" + o;
      o++;
    }
    if (where.getIri() != null || where.getParameter() != null) {
      o++;
      String inverse = where.isInverse() ? "^" : "";
      String property;
      property = Objects.requireNonNullElseGet(propertyVariable, () -> "p" + o);
      if (where.getParameter() != null) {
        property = iriFromAlias(where);
      }
      where.setVariable(property);
      String object;
      if (where.getMatch() != null)
        object = where.getMatch().getVariable();
      else if (where.getValueVariable() != null)
        object = where.getValueVariable();
      else {
        o++;
        object = "o" + o;
      }
      if (where.getIs() != null && where.getIs().get(0).getVariable() != null) {
        object = where.getIs().get(0).getVariable();
      }

      if (where.getIsNull()) {
        whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
      }
      if (where.isDescendantsOrSelfOf()) {
        processWhereIsDescendantsOrSelfOf(whereQl, inverse, subject, object, property, where);
      } else {
        processWhereOther(propertyVariable, whereQl, subject, object, property, where, inverse);
      }
      if (where.isAnyRoleGroup() && where.isInverse()) {
        o++;
        whereQl.append("?").append(object).append(" ^im:roleGroup ").append("?subject").append(o).append(".\n");
        object = "subject" + o;
      }

      if (where.getIsNull()) {
        whereQl.append(tabs).append(" }");
      }
      if (where.getIs() != null) {
        in(whereQl, object, where.getIs(), false);
      } else if (where.getValue() != null) {
        whereValue(whereQl, object, where);
      } else if (where.getMatch() != null) {
        match(whereQl, object, where.getMatch());
      }
    }
    processMatchWhere(whereQl, subject, where.getWhere(), where.getBoolWhere());
  }

  private void processWhereIsDescendantsOrSelfOf(StringBuilder whereQl, String inverse, String subject, String object, String property, Where where) {
    if (inverse.equals("^")) {
      whereQl.append("?").append(property).append(" im:isA ").append(iriFromString(where.getIri())).append(".\n");
      whereQl.append("?").append(object).append(" ").append("?").append(property).append(" ?").append(subject).append(".\n");
    } else {
      whereQl.append("?").append(property).append(" im:isA ").append(iriFromString(where.getIri())).append(".\n");
      whereQl.append("?").append(subject).append(" ").append("?").append(property).append(" ?").append(object).append(".\n");
    }
  }

  private void processWhereOther(String propertyVariable, StringBuilder whereQl, String subject, String object, String property, Where where, String inverse) {
    if (propertyVariable != null) {
      if (inverse.equals("^")) {
        whereQl.append("?").append(object).append(" ").append("?").append(property).append(" ?").append(subject).append(".\n");
        whereQl.append("filter (?").append(property).append(" = ").append(iriFromString(where.getIri())).append(")\n");
      } else {
        whereQl.append("?").append(subject).append(" ").append("?").append(property).append(" ?").append(object).append(".\n");
        whereQl.append("filter (?").append(property).append(" = ").append(iriFromString(where.getIri())).append(")\n");
      }
    } else
      whereQl.append("?").append(subject).append(" ").append(inverse).append(iriFromString(where.getIri())).append(" ?").append(object).append(".\n");
  }

  private void in(StringBuilder whereQl, String object, List<Node> in, boolean isNot) throws QueryException {
    String not = isNot ? " not " : "";
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
    if (subTypes) {
      String superObject = "super" + object;
      whereQl.append("?").append(object).append(" im:isA ?").append(superObject).append(".\n");
      whereQl.append("Filter (?").append(superObject).append(not).append(" in (");
    } else if (superTypes) {
      String subObject = "sub" + object;
      whereQl.append("?").append(subObject).append(" im:isA ?").append(object).append(".\n");
      whereQl.append("Filter (?").append(subObject).append(not).append(" in (");
    } else {
      whereQl.append("Filter (?").append(object).append(not).append(" in (");
    }
    List<String> inList = new ArrayList<>();
    for (Element iri : in) {
      if (iri.getNodeRef() != null)
        inList.add("?" + iri.getNodeRef());
      else
        inList.add(iriFromAlias(iri));
    }
    String inString = String.join(",", inList);
    whereQl.append(inString);
    whereQl.append("))\n");

  }

  private void whereValue(StringBuilder whereQl, String object, Where where) {
    String comp = where.getOperator() != null ? where.getOperator().getValue() : Operator.eq.getValue();
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
    if (property.getPropertyRef() != null) {
      selectQl.append(" ").append(inverse).append(property.getPropertyRef());
    } else if (property.getAs() != null) {
      selectQl.append(" ").append(inverse).append("?").append(property.getAs());
    }
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
    String object = null;

    object = setReturnObject(property);
    if (object == null) {
      o++;
      object = "o" + o;
      property.setValueRef(object);
      if (property.getReturn() != null) {
        property.getReturn().setAs(object);
      }
    }
    whereQl.append(" ?").append(object).append(".\n");
    if (property.getMatch() != null) {
      processReturnMatch(property, whereQl, object);
    }
    if (property.getReturn() != null) {
      convertReturn(selectQl, whereQl, property.getReturn());
    } else {
      if (!selectQl.toString().contains(object)) selectQl.append(" ?").append(object);
      if (labelVariable == null && property.getIri().equals(RDFS.LABEL))
        labelVariable = object;
    }

    whereQl.append("}\n");
  }

  private String setReturnObject(ReturnProperty property) {
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

  private void processReturnMatch(ReturnProperty property, StringBuilder whereQl, String object) throws QueryException {
    boolean first = true;
    for (Match match : property.getMatch()) {
      if (property.getBoolMatch() == Bool.or) {
        whereQl.append("\n{");
      }
      if (property.getBoolMatch() == Bool.or && !first) {
        whereQl.append("UNION ");
      }
      first = false;
      match(whereQl, object, match);
      if (property.getBoolMatch() == Bool.or)
        whereQl.append("}\n");
    }
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
    if (null != queryRequest.getTextSearch() && !(countOnly || highestUsage)) {
      selectQl.append("ORDER BY DESC(").append("strstarts(lcase(?").append(labelVariable)
        .append("),\"").append(escape(queryRequest.getTextSearch()).split(" ")[0])
        .append("\")) ASC(strlen(?").append(labelVariable).append("))\n");
    } else if (null != clause.getOrderBy() && !countOnly && null != clause.getOrderBy().getProperty()) {
      generateOrderBy(selectQl, clause);
    }

    if (null != queryRequest.getPage()) {
      selectQl.append("LIMIT ").append(queryRequest.getPage().getPageSize()).append("\n");
      if (queryRequest.getPage().getPageNumber() > 0)
        selectQl.append("OFFSET ").append((queryRequest.getPage().getPageNumber() - 1) * (queryRequest.getPage().getPageSize())).append("\n");
    }
  }

  private void generateOrderBy(StringBuilder selectQl, Query clause) throws QueryException {
    selectQl.append("Order by ");
    OrderDirection order = clause.getOrderBy().getProperty();
    if (null != order.getDirection() && order.getDirection().equals(Order.descending))
      selectQl.append("DESC(");
    else
      selectQl.append("ASC(");
    if (null != order.getIri()) selectQl.append("?").append(order.getIri());
    else if (null != order.getValueVariable()) selectQl.append("?").append(order.getValueVariable());
    else throw new QueryException("Order by missing identifier: iri / valueVariable");
    selectQl.append(")");
    if (null == queryRequest.getPage() && clause.getOrderBy().getLimit() > 0) {
      selectQl.append("LIMIT ").append(clause.getOrderBy().getLimit()).append("\n");
    } else {
      selectQl.append("\n");
    }
  }

  public String iriFromAlias(Element alias) throws QueryException {
    return getIriFromAlias(alias.getIri(), alias.getParameter(), alias.getVariable(), alias.getNodeRef());
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

  public String getUpdateSparql() throws QueryException {


    StringBuilder updateQl = new StringBuilder();
    updateQl.append(getDefaultPrefixes());
    StringBuilder whereQl = new StringBuilder();
    whereQl.append("WHERE {");
    for (Match match : update.getMatch()) {
      match(whereQl, "entity", match);
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
