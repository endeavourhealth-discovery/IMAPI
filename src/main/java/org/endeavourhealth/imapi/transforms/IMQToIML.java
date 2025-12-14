package org.endeavourhealth.imapi.transforms;

import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.logic.service.QueryDescriptor;
import org.endeavourhealth.imapi.model.iml.IMLLanguage;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;


@Slf4j
public class IMQToIML extends QueryDescriptor{
  private Map<String,Set<String>> iriVariables= new HashMap<>();
  private final Map<String,String> prefixes= new HashMap<>();
  private final IMLLanguage language= new IMLLanguage();
  private final List<String> definitions= new ArrayList<>();
  private final Map<String,String> variables= new HashMap<>();
  private String entityType;



  public IMLLanguage getIML(String entityIri,boolean detailsOnly) throws QueryException {

    try {
      TTEntity entity = getRepo().getEntityPredicates(entityIri, Set.of(IM.ALTERNATIVE_CODE.toString(),
        IM.DEFINITION.toString(),
        IM.DENOMINATOR.toString(),
        IM.NUMERATOR.toString(),
        RDFS.LABEL.toString())).getEntity();
      StringBuilder dsl = new StringBuilder();

      Query query = entity.get(IM.DEFINITION).asLiteral().objectValue(Query.class);
      describeQuery(query, DisplayMode.LOGICAL);
      LogicOptimizer.optimizeQuery(query);
      dsl.append("Define ");
      String prefix= getAndAddPrefixes(entityIri);
      dsl.append(prefix).append(":").append(entityIri.substring(entityIri.lastIndexOf("#")+1)).append(" as Query{\n");
      if (!detailsOnly) {
        dsl.append("name: \"").append(entity.getName()).append("\"\n");
        if (entity.getDescription() != null)
          dsl.append("description: \"").append(entity.getDescription()).append("\"\n");
      }
      dsl.append(convertQuery(entityIri,query));
      dsl.append("}\n");
      dsl.append(addDefinitions());
      dsl.append("\n");
      dsl.append(addPrefixes());
      language.setText(dsl.toString());
      return language;
    } catch (Exception ex) {
      throw new QueryException(ex.getMessage(),ex);
    }
  }



  private String addPrefixes() {
    StringBuilder prefixDsl= new StringBuilder();
    if (!prefixes.isEmpty()) {
      for (Map.Entry<String, String> entry : prefixes.entrySet()) {
        prefixDsl.append("Prefix ").append(entry.getKey()).append(": ").append("<")
          .append(entry.getValue()).append(">").append("\n");
        language.getPrefixes().put(entry.getKey(), entry.getValue());
      }
    }
    return prefixDsl.toString();
  }



  private String addDefinitions() {
    StringBuilder definitionDsl= new StringBuilder();
    definitionDsl.append("\n");
    if (!definitions.isEmpty()) {
      for (String definition : definitions) {
        definitionDsl.append(definition).append("\n");
      }
    }
    for (Map.Entry<String,Set<String>> entry : iriVariables.entrySet()) {
      language.setIriVariables(iriVariables);
      definitionDsl.append("Define ").append(entry.getKey()).append("= ");
      Set<String> values = new HashSet<>();
      for (String value : entry.getValue()) {
        String prefix= getAndAddPrefixes(value);
        if (prefix!=null) {
          values.add(prefix+ ":"+ value.substring(value.lastIndexOf("#") + 1));
        }
        else values.add("<"+value+">");
      }
      definitionDsl.append(String.join(",",values));;
      definitionDsl.append("\n");
    }
    return definitionDsl.toString();
  }

  private String getAndAddPrefixes(String value) {
    if (value.contains("#")&&value.split("#")[0].contains("/")) {
      String prefix = value.substring(value.lastIndexOf("/") + 1).split("#")[0];
      prefixes.put(prefix, value.substring(0, value.lastIndexOf("#") + 1));
      return prefix;
    }
    return null;
  }


  private String convertQuery(String entityIri,Query query) throws QueryException {
    variables.clear();
    StringBuilder clause= new StringBuilder();
    if (query.getReturn()!=null) {
      clause.append("Get ");
      clause.append(convertReturn(query.getReturn()));
      clause.append("\n");
    }
    if (query.getIs()!=null){
      String cohort="";
      for (Node node:query.getIs()) {
        cohort = cohort + (!cohort.equals("") ? "," : "") + getVariableFromIri(node.getIri(), Context.SINGLE);
      }
      clause.append("from ");
      clause.append(cohort).append("\n");
    }
    clause.append(convertBooleans(query));
    if (query.getColumnGroup()!=null){
      clause.append("\n");
      for (Match columnGroup:query.getColumnGroup()) {
        clause.append(convertColumnGroup(columnGroup));
        clause.append("\n");
      }
    }
    clause.append("\n");
    return clause.toString();
  }

  private String convertColumnGroup(Match group) throws QueryException {
    StringBuilder clause= new StringBuilder();
    if (group.getReturn()!=null) {
      clause.append("columns ");
      clause.append(convertReturn(group.getReturn()));
    }
    if (group.getOrderBy()!=null||group.getAnd()!=null||group.getOr()!=null||group.getNot()!=null||group.getWhere()!=null) {
      clause.append("\n").append("filter ");
      clause.append(convertMatchContent(group, null));
    }
    return clause.toString();
  }

  private String convertReturn(Return aReturn) throws QueryException {
    StringBuilder clause= new StringBuilder();
    boolean first = true;
    for (ReturnProperty property : aReturn.getProperty()) {
      if (!first) clause.append(", ");
      first= false;
      clause.append(convertReturnProperty(property));
    }
    return clause.toString();
  }

  private String convertReturnProperty(ReturnProperty property) throws QueryException {
    StringBuilder clause= new StringBuilder();
    if (property.getIri()!=null) {
      clause.append(getVariableFromIri(property.getIri(),Context.PROPERTY));
      if (property.getFunction()!=null) {
        clause.append(getVariableFromIri(property.getFunction().getIri(),Context.PROPERTY));
        clause.append("(");
        for (Argument arg : property.getFunction().getArgument()) {
          convertArguments(clause, arg);
        }
        clause.append(")");
      }
    }
    if (property.getReturn()!=null) {
      clause.append("->");
      clause.append(convertReturn(property.getReturn()));
    }
    return clause.toString();
  }

  private void convertArguments(StringBuilder clause, Argument arg) throws QueryException {
    if (arg.getValueData() != null) {
      clause.append(arg.getValueData());
    }
    if (arg.getValueParameter() != null) {
      clause.append(arg.getValueParameter());
    }
    if (arg.getValuePath() != null) {
      clause.append(convertValuePath(arg.getValuePath()));
    }
  }

  private String convertBooleans(Match match) throws QueryException {
    StringBuilder clause= new StringBuilder();
    int boolIndex=0;
    for (List<Match> matches : Arrays.asList(match.getAnd(), match.getOr(), match.getNot())) {
      boolIndex++;
      if (matches != null) {
        clause.append(convertMatches(matches, boolIndex == 1 ? Bool.and : boolIndex == 2 ? Bool.or : Bool.not));
      }
    }
    return clause.toString();
  }


  private String convertMatches(List<Match> matches, Bool operator) throws QueryException {
    StringBuilder clause= new StringBuilder();
    for (int index = 0; index < matches.size(); index++) {
      Match match = matches.get(index);
      boolean hasNested= false;
      if (match.getAnd()!=null||match.getOr()!=null)
        hasNested= true;
      if (index==0){
        clause.append(operator==Bool.or ? "Either ": operator==Bool.not ? "Exclude if ":hasNested ?"and if ": "");
      }
      else {
        clause.append(operator== Bool.not ?"Exclude if ":operator==Bool.and ?" and if ":"or ").append(" ");
      }

      if (hasNested)
        clause.append("(\n");
      clause.append(convertMatch(matches.get(index),null));
      if (hasNested)
        clause.append(")\n");
    }
    return clause.toString();
  }

  private String convertMatch(Match match,String from) throws QueryException {
    StringBuilder clause = new StringBuilder();
    clause.append(convertMatchContent(match, from));
    return clause.toString();
  }
  private String convertMatchContent(Match match,String from) throws QueryException {
    StringBuilder clause= new StringBuilder();
    if (match.getAnd()!=null||match.getOr()!=null||match.getNot()!=null) {
      clause.append(convertBooleans(match));
      return clause.toString();
    }
    if (match.getOrderBy() != null) {
      clause.append(describeOrderBy(match.getOrderBy())).append(" ");
    }
    if (match.getIs() != null) {
      clause.append("is ");
      StringBuilder cohort= new StringBuilder();
      for (Node node:match.getIs()) {
        cohort.append(!cohort.toString().isEmpty() ? "," : "").append(getVariableFromIri(node.getIri(), Context.SINGLE));
      }
      clause.append(cohort).append("\n");
    }
    if (match.getTypeOf()!=null) {
      clause.append(getVariableFromIri(match.getTypeOf().getIri(), Context.SINGLE)).append(" ");
    }

    if (match.getWhere() != null) {
      clause.append(convertWhere(match.getWhere(),0,match.getVariable(),from)).append(" ");
    }
    if (match.getPath()!=null){
      clause.append(convertPath(match,match.getPath(),from));
      return clause.toString();
    }

    clause.append("\n");
    return clause.toString();

  }

  private String convertPath(Match match,List<Path> paths,String from) throws QueryException {
    StringBuilder clause= new StringBuilder();
    for (Path path : paths) {
      clause.append("Link ");
      if (path.getIri() != null) {
        clause.append(getVariableFromIri(path.getIri(), Context.PROPERTY)).append(" ").append(path.getVariable()).append(" ");
        clause.append(getMatchWheres(match, path.getVariable(), from));
        if (path.getPath() != null) {
          clause.append(convertPath(match, path.getPath(), from));
        }
      }
    }
    return clause.toString();
  }

  private String getMatchWheres(Match match, String nodeRef,String from) throws QueryException {
    StringBuilder clause= new StringBuilder();
    if (match.getWhere()!=null) {
      clause.append(convertWhere(match.getWhere(), 0,nodeRef,from));
    }
    else
      clause.append(convertBooleans(match));
    return clause.toString();
  }


  private String convertValuePath(Path path) throws QueryException {
    StringBuilder clause= new StringBuilder();
    clause.append(getVariableFromIri(path.getIri(), Context.SINGLE));
    if (path.getPath()!=null){
      clause.append(convertValuePath(path.getPath().getFirst()));
    }
    return clause.toString();

  }



  private String convertWhere(Where where,int level,String nodeRef,String from) throws QueryException {
    StringBuilder clause= new StringBuilder();
    if (where.getAnd() != null) {
      clause.append(convertWheres(where.getAnd(), Bool.and, level + 1,nodeRef,from));
      return clause.toString();
    } else if (where.getOr() != null) {
      clause.append(convertWheres(where.getOr(), Bool.or, level + 1,nodeRef,from));
      return clause.toString();
    }
    if (nodeRef!=null){
      if (where.getNodeRef()==null||(!where.getNodeRef().equals(nodeRef))) {
        return "";
      }
    }
    if (where.getIri() != null) {
      clause.append(getVariableFromIri(where.getIri(), Context.SINGLE)).append(" ");
      if (from!=null){
        clause.append("of ").append(from).append(" ");
      }
      if (where.getIs()!=null &&where.getValueLabel()!=null){
        clause.append("is ").append(where.getValueLabel()).append(" ");
        return clause.toString();
      }
      else if (where.getOperator() != null) {
        if (where.isNot())
          clause.append("not ");
        clause.append(convertValueWhere(where));
      } else if (where.getIs() != null) {
        clause.append("of ").append(getVariableFromIri(where.getIs(), Context.SINGLE));
      } else if (where.getRange() != null) {
        clause.append(convertRangeWhere(where));
      }
      if (where.getRelativeTo() != null) {
        clause.append(convertRelativeWhere(where));
      }
      clause.append(" ");
      return clause.toString();
    }
    return clause.toString();
  }

  private String convertFunctionWhere(Where where) throws QueryException {
    StringBuilder clause = new StringBuilder();
      boolean first = true;
      clause.append(getVariableFromIri(where.getFunction().getIri(), Context.PROPERTY)).append("(");
      for (Argument arg : where.getFunction().getArgument()) {
        if (!first) clause.append(", ");
        first= false;
        convertArguments(clause, arg);
        if (arg.getValueIri() != null) {
          clause.append(getVariableFromIri(arg.getValueIri().getIri(), Context.PROPERTY));
        }
      }
      clause.append(") ");
      return clause.toString();
    }
  private String convertRelativeWhere(Where where) throws QueryException {
    StringBuilder clause = new StringBuilder();
    if (where.getValue()!=null){
      clause.append("relative to ");
    }
    RelativeTo relativeTo= where.getRelativeTo();
    if (relativeTo.getIri()!=null){
      clause.append(getVariableFromIri(relativeTo.getIri(), Context.SINGLE)).append(" of  ");
    }
    if (relativeTo.getParameter()!=null){
      clause.append(relativeTo.getParameter()).append(" ");
    }
    if (relativeTo.getNodeRef()!=null){
      clause.append(relativeTo.getNodeRef()).append(" ");
    }
    return clause.toString();
  }

  private String getVariableFromRelativeTo(RelativeTo relativeTo) throws QueryException {
    if (relativeTo.getParameter()!=null){
      return relativeTo.getParameter();
    }
    StringBuilder clause= new StringBuilder();
    if (relativeTo.getNodeRef()!=null){
      clause.append(relativeTo.getNodeRef()).append("->");
    }
    if (relativeTo.getIri()!=null){
      clause.append(getVariableFromIri(relativeTo.getIri(),Context.PROPERTY));
    }
    return clause.toString();
  }

  private String convertWheres(List<Where> wheres, Bool operator,int level,String nodeRef,String from) throws QueryException {
    StringBuilder clause= new StringBuilder();
    for (int index = 0; index < wheres.size(); index++) {
      Where where = wheres.get(index);
      if (index==0)
        clause.append(operator==Bool.or ? "either " : "");
      else
        clause.append(operator==Bool.or ? "or " : "and ");
      if (where.getAnd()!=null||where.getOr()!=null){
        clause.append("(\n");
      }
      clause.append(convertWhere(where,level,nodeRef,from));
      if (where.getAnd()!=null||where.getOr()!=null){
        clause.append(")\n");
      }
    }
    return clause.toString();
  }

  private String convertRangeWhere(Where where) throws QueryException {
    StringBuilder clause= new StringBuilder();
    if (where.isNot())
      clause.append("not ");
    if (where.getRange().getFrom()!=null) {
      clause.append("between ");
      clause.append(convertValueWhere(where.getRange().getFrom()));
    }
    if (where.getRange().getTo()!=null) {
      clause.append("and ");
      clause.append(convertValueWhere(where.getRange().getTo()));
    }
    if (where.getUnits()!=null)
      clause.append(getVariableFromIri(where.getUnits().getIri(),Context.PLURAL)).append(" ");
    return clause.toString();
  }

  private String convertValueWhere(Assignable where) throws QueryException {
    StringBuilder clause= new StringBuilder();

    if (where.getOperator()!=null){
      clause.append(where.getOperator().getValue()).append(" ");
    }
    if (where.getValue()!=null){
      clause.append(where.getValue()).append(" ");
    }
    if (where.getUnits()!=null&&where.getFunction()==null){
      clause.append(getVariableFromIri(where.getUnits().getIri(),Context.PLURAL)).append(" ");
    }
    return clause.toString();

  }



  private String getVariableFromIri(List<Node> nodes,Context context){
    Set<String> variableNames= new HashSet<>();
    for (Node node : nodes) {
      String variableName = getTermInContext(node, context);
      String shortName = getIdentifier(variableName);
      if (node.getIri() != null) {
        TTEntity entity= getIriContext().get(node.getIri());
        iriVariables.computeIfAbsent(shortName, c -> new HashSet<>()).add(node.getIri());
        variableNames.add(shortName);
        language.getInfo().put(shortName,entity.getDescription()!=null ?entity.getDescription(): entity.getName());
      }
    }
    return String.join(",",variableNames);
  }

  private String getIdentifier(String text){
    return text
      .replaceAll("[^A-Za-z0-9_]", "_")
      .replaceAll("_+", "_");
  }


  private String getVariableFromIri(String iri,Context context) throws QueryException {
    TTEntity entity= getIriContext().get(iri);
    if (entity==null)
      throw new QueryException("Entity for iri: "+ iri+" does not exist");
    String variable;
    if (entity.get(IM.ALTERNATIVE_CODE)!=null) {
      variable = entity.get(IM.ALTERNATIVE_CODE).asLiteral().getValue();
    }
    else variable = getIdentifier(entity.getName());
    iriVariables.computeIfAbsent(variable,v-> new HashSet<>()).add(iri);
    return variable;
  }





}
