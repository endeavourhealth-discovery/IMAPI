package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.logic.service.QueryDescriptor;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.utility.Pluraliser;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;
import java.util.stream.Collectors;

public class IMQToIML extends QueryDescriptor{
  private StringBuilder dsl;
  private Map<String,Set<String>> iriVariables= new HashMap<>();
  private String lastTypeOf;
  private Map<String,String> prefixes= new HashMap<>();

  public String getIML(String entityIri) throws QueryException {
    try {
      TTEntity entity = getRepo().getEntityPredicates(entityIri, Set.of(IM.ALTERNATIVE_CODE.toString(),
        IM.DEFINITION.toString(),
        RDFS.LABEL.toString())).getEntity();
      Query query = entity.get(IM.DEFINITION).asLiteral().objectValue(Query.class);
      describeQuery(query, DisplayMode.LOGICAL);
      LogicOptimizer.optimizeQuery(query);
      getIriContext().put(getVariableFromIri(entityIri,Context.SINGLE),entity);
      dsl = new StringBuilder();
      dsl.append("Define ");
      dsl.append(getVariableFromIri(entityIri,Context.SINGLE)).append(" as ");
      convertQuery(query);
      addDefinitions();
      addPrefixes();
      return dsl.toString();
    } catch (Exception ex) {
      throw new QueryException(ex.getMessage(),ex);
    }
  }

  private void addPrefixes() {
    if (!prefixes.isEmpty()) {
      for (Map.Entry<String, String> entry : prefixes.entrySet()) {
        dsl.append("Prefix ").append(entry.getKey()).append(": ").append("&lt;").append(entry.getValue()).append("&gt;").append("\n");
      }
    }
  }

  private void addDefinitions() {
    dsl.append("\n");
    for (Map.Entry<String,Set<String>> entry : iriVariables.entrySet()) {
      dsl.append("Define ").append(entry.getKey()).append("= ");
      Set<String> values = new HashSet<>();
      for (String value : entry.getValue()) {
        String prefix= null;
        if (value.contains("#")&&value.split("#")[0].contains("/")){
          prefix= value.substring(value.lastIndexOf("/")+1).split("#")[0];
        }
        if (prefix!=null) {
          prefixes.put(prefix, value.substring(0, value.lastIndexOf("#") + 1));
          values.add(prefix+ ":"+ value.substring(value.lastIndexOf("#") + 1));
        }
        else values.add("<"+value+">");
      }
      dsl.append(String.join(",",values));;
      dsl.append("\n");
    }
  }

  private void convertQuery(Query query) {
    dsl.append("(\n");
    if (query.getTypeOf()!=null){
      dsl.append(getVariableFromIri(query.getTypeOf().getIri(),Context.PLURAL)).append(" ");
      lastTypeOf = query.getTypeOf().getIri();
    }
    if (query.getIsCohort()!=null){
      dsl.append("From ").append(getVariableFromIri(query.getIsCohort().getIri(),Context.SINGLE)).append("\n");
    }
    if (query.getAnd()!=null) {
      convertMatches(query.getAnd(), query, Bool.and);
    }
    else if (query.getOr()!=null) {
      convertMatches(query.getOr(), query, Bool.and);
      }
    else if (query.getNot()!=null) {
      convertMatches(query.getNot(),query,Bool.not);
    }
    dsl.append(")\n");
  }

  private void convertMatches(List<Match> matches, Query parent, Bool operator) {
    if (parent.getIsCohort()!=null){
      dsl.append("and ");
    }
    for (int index = 0; index < matches.size(); index++) {
      if (index==0){
        dsl.append(operator==Bool.or ? "Either ": operator==Bool.not ? "Exclude ":"");
      }
      else {
        dsl.append(operator== Bool.not ?"Exclude": operator).append(" ");
      }
      convertMatch(matches.get(index));
    }
  }

  private void convertMatch(Match match) {
    if (match.getWhere()!=null){
      convertWhere(match.getWhere(),0);
    }
    if (match.getIsCohort()!=null){
        dsl.append("From ").append(getVariableFromIri(match.getIsCohort().getIri(),Context.SINGLE)).append("\n");
    }
  }

  private void convertWhere(Where where,int level) {
    if (level>1)
      dsl.append("(\n");
    if (where.getIri()!=null) {
      dsl.append(getVariableFromIri(where.getIri(),Context.SINGLE)).append(" ");
      if (where.getOperator() != null) {
        convertValueWhere(where);
      }
      else if (where.getIs()!=null){
        dsl.append("is ").append(getVariableFromIri(where.getIs(),Context.SINGLE));
      }
    }
    else {
      if (where.getAnd() != null) {
        convertWheres(where.getAnd(), Bool.and, level + 1);
      } else if (where.getOr() != null) {
        convertWheres(where.getOr(), Bool.or, level + 1);
      }
    }
    if (level > 1)
        dsl.append(")\n");
    else dsl.append("\n");
  }

  private void convertWheres(List<Where> wheres, Bool operator,int level) {
    for (int index = 0; index < wheres.size(); index++) {
      if (index>0)
        dsl.append(operator==Bool.or ? "Or " : "And ");
      convertWhere(wheres.get(index),level);
    }
  }

  private void convertValueWhere(Where where) {
    if (where.getOperator()!=null){
      dsl.append(where.getOperator().getValue()).append(" ");
    }
    if (where.getValue()!=null){
      dsl.append(where.getValue()).append(" ");
    }
    if (where.getUnits()!=null){
      dsl.append(getVariableFromIri(where.getUnits().getIri(),Context.PLURAL)).append(" ");
    }
    dsl.append(")\n");
  }


  private String getVariableFromIri(List<Node> nodes,Context context){
    Set<String> variableNames= new HashSet<>();
    for (Node node : nodes) {
      String variableName = getTermInContext(node, context);
      String shortName = variableName.replace(" ", "_");
      if (node.getIri() != null) {
        iriVariables.computeIfAbsent(shortName, c -> new HashSet<>()).add(node.getIri());
        variableNames.add(shortName);
      }
    }
    return String.join(",",variableNames);
  }


  private String getVariableFromIri(String iri,Context context) {
    TTEntity entity= getIriContext().get(iri);
    if (entity.get(IM.ALTERNATIVE_CODE)!=null){
      String variable= entity.get(IM.ALTERNATIVE_CODE).asLiteral().getValue();
      variable= getTermInContext(variable,context);
      iriVariables.computeIfAbsent(variable,v-> new HashSet<>()).add(iri);
      return entity.get(IM.ALTERNATIVE_CODE).asLiteral().getValue();
    }
    else return entity.getName().replace(" ","_");
  }





  private void cohortBuilder(Query query) {
    dsl.append("Defined as: \n");
    if (query.getTypeOf()!=null){
      dsl.append(query.getTypeOf().getName()).append(" ");
    }
    else dsl.append("Entity ");
    if (query.getIsCohort()!=null)
      dsl.append("From ").append(query.getIsCohort().getName()).append(" ");

  }

  private String getLocalName(String iri, boolean plural){
    String localName= iri.substring(iri.lastIndexOf('#')+1);
    if (plural)
      localName= Pluraliser.pluralise(localName);
    return localName;
  }


}
