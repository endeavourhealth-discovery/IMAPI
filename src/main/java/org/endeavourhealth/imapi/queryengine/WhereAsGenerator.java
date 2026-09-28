package org.endeavourhealth.imapi.queryengine;


import org.endeavourhealth.imapi.model.imq.*;

import java.util.ArrayList;
import java.util.List;

public class WhereAsGenerator {

  public static String getWhereAs(Where where) {
   List<String> parts = buildWhereSentence(where);
   return String.join(" ", parts);
  }

  public static List<String> buildWhereSentence(Where where) {
    List<String> parts = new ArrayList<>();
    if (where.getShortLabel() != null) {
      parts.add(where.getShortLabel());
      return parts;
    }
    if (where.getIs() != null) {
      parts.add(where.getIs().getFirst().getName()+ (where.getIs().size()>1? " more":""));
      return parts;
    }

    if (where.getRange() != null) {
      buildRangeSentence(where, parts);
    } else {
      buildNonRangeSentence(where, parts);
    }

    if (where.getCompare() != null) {
      addReference(parts, where.getCompare().getRight());
    }

    return parts;
  }

  private static void buildNonRangeSentence(Where where, List<String> parts) {
    if (where.getIsNull()) {
      parts.add(("is absent"));
      return;
    }

    if (where.isNotNull()) {
      parts.add(("is present"));
      return;
    }

    String units = where.getUnits() != null
      ? where.getUnits().getName()
      : "";

    String value = null;

    if (where.getValue() != null
      && (!where.getValue().equals("0") || where.getOperator() != null)) {
      value = where.getValue();
    }

    if (where.getOperator() != null) {
      parts.add(
        where.getOperator().getValue());
    }

    if (value != null && !value.isEmpty()) {
      parts.add(value + " " + units + " ");
    }
  }

  private static void buildRangeSentence(
    Where where,
    List<String> parts
  ) {
    Range range = where.getRange();

    Value from = range.getFrom();
    Value to = range.getTo();

    String fromVal = from.getValue() != null
      && !from.getValue().equals("0")
      ? from.getValue()
      : null;

    String toVal = to.getValue();

    String fromUnits = from.getUnits() != null
      ? from.getUnits().getName()
      : "";

    String toUnits = to.getUnits() != null
      ? to.getUnits().getName()
      : "";

    parts.add((" is between "));

    boolean inclusive = false;

    if (from.getOperator() != null) {
      if (from.getOperator() == Operator.gte
        || from.getOperator() == Operator.lte) {
        inclusive = true;
      }
    }

    parts.add(
      String.valueOf(fromVal) + " " + fromUnits + " "
    );

    if (inclusive) {
      parts.add(("(inc.) "));
    }

    parts.add((" and "));

    inclusive = false;

    if (to.getOperator() != null) {
      if (to.getOperator() == Operator.gte
        || to.getOperator() == Operator.lte) {
        inclusive = true;
      }
    }

    if (!inclusive && to.getOperator() != null) {
      parts.add(

        to.getOperator().getValue()
      );
    }

    parts.add(

      toVal + " " + toUnits + " "
    );

    if (inclusive) {
      parts.add(("(inc.) "));
    }
  }

  private static void addReference(
    List<String> parts,
    ValueSource source
  ) {

    if (source.getParameter() != null) {
      return;
    }
    if (source.getNodeRef() != null)
        parts.add(" of "+
          source.getName()
        );
  }



}
