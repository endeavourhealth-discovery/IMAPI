package org.endeavourhealth.imapi.transforms;


import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class IMQTermGenerator {


  public static String generateMeaningfulTerm(Match match) {
    StringBuilder term = new StringBuilder();
    if (match.getOrderBy()!=null){
      term.append(getOrderBy(match.getOrderBy()));
    }
    //if (match.getPath()!=null){
     // term.append(getPath(match.getPath().getFirst()));
   // }
    String concept = getConcept(match);
    if (!concept.isEmpty()) {
      term.append(concept);
      String timeWindow = getTimeWindow(match);
      if (!timeWindow.isEmpty()) {
        term.append("_").append(timeWindow);
      }
      return term.toString();
    }


    return term.toString();
  }

  public static String getOrderBy(OrderLimit orderBy) {
    String orderDisplay = "";
    for (OrderDirection property : orderBy.getProperty()) {
      String field = property.getIri();
      if (field.toLowerCase().contains("date")) {
        if (property.getDirection() == Order.descending) orderDisplay = "latest";
        else orderDisplay = "earliest";
      } else {
        if (property.getDirection() == Order.descending) orderDisplay = "maximum";
        else orderDisplay = "minimum";
      }
    }
    if (orderBy.getLimit() > 1)
      orderDisplay = orderDisplay + "_" + orderBy.getLimit()+"_";
    orderBy.setDescription(orderDisplay);

    return orderDisplay;
  }

  private static String getPath(Path path){
    StringBuilder pathToken= new StringBuilder();
    pathToken.append(getShort(path));
    if (path.getPath()!=null){
      pathToken.append(path.getPath().getFirst());
    }
    return pathToken.toString();
  }

  private static String getShort(IriLD node){
    if (node.getName()!=null){
      return getToken(node.getName().replace(" ",""));
    }
    else {
      String iri= node.getIri();
      iri= iri.substring(iri.lastIndexOf('#')+1);
      return getToken(iri);
    }
  }

  private static String getToken(String name) {
    StringBuilder token = new StringBuilder();
    int pos=1;
    token.append(name.charAt(0));
    for (char c : name.toCharArray()) {
      pos++;
      if (Character.isUpperCase(c)) {
        token.append("_").append(c);
        pos = 1;
      }
      if (pos<4)
        token.append(c);
    }
    return token.toString();
  }

  private static String getConcept(Match match) {
    if (match.getWhere() == null) return "";
    StringBuilder whereTerm = new StringBuilder();
    Where where = match.getWhere();
    if (where.getAnd() != null) {
      for (Where and : where.getAnd()) {
        whereTerm.append(getWhereConcept(and));
      }
    }
    if (where.getOr() != null) {
      for (Where or : where.getOr()) {
        whereTerm.append(getWhereConcept(or));
      }
    }
    if (where.getIri() != null) {
      whereTerm.append(getWhereConcept(where));
    }
    return whereTerm.toString();
  }

  private static String getWhereConcept(Where where) {
    StringBuilder whereTerm = new StringBuilder();
    if (where.getAnd() != null) {
      for (Where and : where.getAnd()) {
        whereTerm.append(getWhereConcept(and));
      }
    }
    if (where.getOr() != null) {
      for (Where or : where.getOr()) {
        whereTerm.append(getWhereConcept(or));
      }
    }
    if (where.getIri() == null) {
      return "";
    }
    if (where.getIs() != null) {
      if (where.getValueLabel() != null) {
        whereTerm.append(getToken(where.getValueLabel()));
      } else whereTerm.append(getToken(where.getIs().getFirst().getName()));
    }
    return whereTerm.toString();
  }


  private static String getTimeWindow(Match match) {
    StringBuilder timeTerm = new StringBuilder();
    if (match.getWhere() == null) return "";
    if (match.getWhere().getAnd() != null) {
      for (Where and : match.getWhere().getAnd()) {
        timeTerm.append(getWhereTimeWindow(and));
      }
    }
    if (match.getWhere().getOr() != null) {
      for (Where or : match.getWhere().getOr()) {
        timeTerm.append(getWhereTimeWindow(or));
      }
    }
    return timeTerm.toString();
  }

  private static String getWhereTimeWindow(Where where){
    StringBuilder timeTerm = new StringBuilder();
    if (where.getAnd() != null) {
      for (Where and : where.getAnd()) {
        timeTerm.append(getWhereTimeWindow(and));
      }
    }
    if (where.getOr() != null) {
      for (Where or : where.getOr()) {
        timeTerm.append(getWhereTimeWindow(or));
      }
    }
    if (where.getIri() != null) {
      if (where.getIri().toLowerCase().contains("date")){
        if (where.getValue()!=null &&where.getUnits()!=null) {
          String val = where.getValue().replace(".", "_").replace("-","");
          String unit = where.getUnits().getName();
          String achieve="";
          if (where.getRelativeTo()!=null) {
            if (where.getRelativeTo().getParameter() != null) {
              if (where.getRelativeTo().getParameter().equals("$searchDate")) {
                if (where.getValue().startsWith("-")) {
                  timeTerm.append("L").append(val);
                }
              } else if (where.getRelativeTo().getParameter().equals("achievementDate")) {
                timeTerm.append(val);
                achieve = "Ach_dat";
              }
            }
            if (unit.toLowerCase().startsWith("month")) {
              timeTerm.append("M");
            } else if (unit.toLowerCase().startsWith("day")) {
              timeTerm.append("D");
            } else if (unit.toLowerCase().startsWith("year")) {
              timeTerm.append("Y");
            }
            if (!where.getValue().startsWith("-")){
              timeTerm.append("After");
            }
            if (!achieve.isEmpty()) {
              timeTerm.append("_").append(achieve);
            }
          }
        }
      }
    }
    return timeTerm.toString();
  }

}
