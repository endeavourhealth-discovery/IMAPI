package org.endeavourhealth.imapi.queryengine;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.Namespace;

public class ClauseUtils {

  public static void assignFunction(Where where){
    String iri = where.getIri();
    TTIriRef units= where.getUnits();
    RelativeTo relativeTo = where.getRelativeTo();
    if (iri.contains("age")) {
      if (relativeTo == null) {
        relativeTo = new RelativeTo().setParameter("$searchDate");
      }
      where.setFunction(buildFunction(
        Namespace.IM + "age",
        argPath("dateOfBirth", new Path().setIri(Namespace.IM + "dateOfBirth")),
        argRelativeTo(relativeTo),
        argUnits(units)
      ));
    } else if (iri.toLowerCase().contains("date")&&relativeTo!=null&&(where.getValue()!=null||where.getRange()!=null)) {
      where.setFunction(buildFunction(
        Namespace.IM + "TimeDifference",
        argPath("firstDateTime", new Path().setIri(iri)),
        argRelativeTo(relativeTo),
        argUnits(units)
      ));
    } else if (iri.toLowerCase().contains("value")&&relativeTo!=null) {
      where.setFunction(buildFunction(
        Namespace.IM + "NumericDifference",
        argPath("firstValue", new Path().setIri(iri)),
        buildValueArgument(relativeTo)
      ));
    }
  }

  private static FunctionClause buildFunction(String iri, Argument... args) {
    FunctionClause clause = new FunctionClause();
    clause.setIri(iri);
    for (Argument a : args) {
      if (a!=null) {
        clause.addArgument(a);
      }
    }
    return clause;
  }

  private static Argument argRelativeTo(RelativeTo relativeTo) {
    Argument argument = new Argument().setParameter("relativeTo");
    if (relativeTo.getNodeRef() != null)
      argument.setValueNodeRef(relativeTo.getNodeRef());
    if (relativeTo.getIri() != null)
      argument.setValuePath(new Path().setIri(relativeTo.getIri()));

    else if (relativeTo.getParameter() != null)
      argument.setParameter("relativeTo").setValueParameter(relativeTo.getParameter());
    return argument;
  }

  private static Argument argPath(String param, Path path) {
    return new Argument().setParameter(param).setValuePath(path);
  }

  private static Argument argUnits(TTIriRef units) {
    if (units != null) {
      return new Argument().setParameter("units").setValueIri(units);
    }
    return null;
  }

  private static Argument buildValueArgument(RelativeTo relativeTo) {
    Argument arg = new Argument().setParameter("secondValue");
    if (relativeTo.getNodeRef() != null) {
      arg.setValueParameter(relativeTo.getNodeRef());
    } else if (relativeTo.getParameter() != null) {
      arg.setValueParameter(relativeTo.getParameter());
    } else if (relativeTo.getIri() != null) {
      Path path = new Path().setIri(relativeTo.getIri());
      if (relativeTo.getNodeRef() != null) {
        path.setNodeRef(relativeTo.getNodeRef());
      }
      arg.setValuePath(path);
    }
    return arg;
  }



}
