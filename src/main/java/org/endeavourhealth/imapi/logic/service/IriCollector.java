package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IriCollector {

  public static Set<String> collectIris(Query query) {
    Set<String> iris = new HashSet<>();
    collectQueryIris(query, iris);
    return iris;
  }


  private static void collectQueryIris(Query query, Set<String> iris) {
    if (query.getColumnGroup() != null) {
      for (Query subQuery : query.getColumnGroup()) {
        collectMatchIris(subQuery, iris);
      }
    }
    collectMatchIris(query, iris);
  }


  private static void collectReturnIris(Return prop, Set<String> iriSet) {
    if (prop.getIri() != null) iriSet.add(prop.getIri());
    if (prop.getUnits() != null)
      iriSet.add(prop.getUnits().getIri());
    if (prop.getCase() != null) {
      if (prop.getCase().getWhen() != null)
        for (When when : prop.getCase().getWhen()) {
          collectWhereIris(when, iriSet);
          if (when.getThen() != null)
            collectExpressionIris(when.getThen(), iriSet);
        }
    }
    if (prop.getFunction() != null) {
      collectFunctionIris(prop.getFunction(), iriSet);
    }
    if (prop.getSemanticMap() != null) {
      iriSet.add(prop.getSemanticMap().getIri());
    }
  }

  private static void collectExpressionIris(Expression then, Set<String> iriSet) {
    if (then.getIri() != null)
      iriSet.add(then.getIri());
  }

  private static void collectPathIris(Path path, Set<String> iriSet) {
    if (path.getIri() != null)
      iriSet.add(path.getIri());
    if (path.getTypeOf() != null) {
      iriSet.add(path.getTypeOf().getIri());
    }
    if (path.getPath() != null) {
      for (Path subPath : path.getPath()) {
        collectPathIris(subPath, iriSet);
      }
    }
  }


  private static void collectMatchIris(Query query, Set<String> iriSet) {

    if (query.getIri() != null) {
      iriSet.add(query.getIri());
    }
    if (query.getTypeOf() != null) {
      iriSet.add(query.getTypeOf().getIri());
    }
    if (query.getPath() != null) {
      for (Path path : query.getPath()) {
        collectPathIris(path, iriSet);
      }
    }
    if (query.getIs() != null) {
      Node node = query.getIs();
      if (node.getMatch() != null)
        collectMatchIris(node.getMatch(), iriSet);
      else if (node.getIri() != null)
        iriSet.add(node.getIri());
    }
    for (List<Query> queries : Arrays.asList(query.getOr(), query.getAnd(), query.getRule())) {
      if (queries != null) {
        for (Query subQuery : queries) {
          collectMatchIris(subQuery, iriSet);
        }
      }
    }
    if (query.getThen()!=null){
      collectMatchIris(query.getThen(), iriSet);
    }




    if (query.getWhere() != null) {
      collectWhereIris(query.getWhere(), iriSet);
    }

    if (query.getThen() != null) {
      collectMatchIris(query.getThen(), iriSet);
    }
    if (query.getReturn() != null) {
      for (Return prop : query.getReturn()) {
        collectReturnIris(prop, iriSet);
      }
    }
    if (query.getOrderBy() != null) {
      collectOrderByIris(query.getOrderBy(), iriSet);
    }
  }

  private static void collectOrderByIris(OrderLimit orderBy, Set<String> iriSet) {
    if (orderBy.getProperty() != null) {
      for (OrderDirection property : orderBy.getProperty()) {
        iriSet.add(property.getIri());
      }
    }
  }


  private static void collectWhereIris(Where where, Set<String> iriSet) {
    if (where.getIri() != null) {
      iriSet.add(where.getIri());
    }
    if (where.getQualifier() != null) {
      iriSet.add(where.getQualifier().getIri());
    }

    if (where.getAnd() != null) {
      for (Where subWhere : where.getAnd()) {
        collectWhereIris(subWhere, iriSet);
      }
    }
    if (where.getOr() != null) {
      for (Where subWhere : where.getOr()) {
        collectWhereIris(subWhere, iriSet);
      }
    }
    if (where.getIs() != null) {
      for (Node node : where.getIs())
        if (node.getIri() != null)
          iriSet.add(node.getIri());
    }
    collectAssignableIris(where, iriSet);

    if (where.getRange() != null) {
      if (where.getRange().getFrom() != null) {
        collectAssignableIris(where.getRange().getFrom(), iriSet);
      }
      if (where.getRange().getTo() != null) {
        collectAssignableIris(where.getRange().getTo(), iriSet);
      }
    }
    if (where.getQualifier() != null) {
      iriSet.add(where.getQualifier().getIri());
    }
  }

  private static void collectFunctionIris(FunctionClause function, Set<String> iriSet) {
    if (function.getIri() != null) {
      iriSet.add(function.getIri());
    }
    if (function.getArgument() != null) {
      for (Argument argument : function.getArgument()) {
        if (argument.getValuePath() != null) {
          collectPathIris(argument.getValuePath(), iriSet);
        }
        if (argument.getValueIri() != null) iriSet.add(argument.getValueIri().getIri());
        if (argument.getValueIriList() != null) {
          for (TTIriRef valueIri : argument.getValueIriList()) iriSet.add(valueIri.getIri());
        }
      }
    }
  }

  private static void collectAssignableIris(Assignable assignable, Set<String> iriSet) {

    if (assignable.getCompare() != null) {
      collectCompareIris(assignable.getCompare(), iriSet);
    }

  }

  private static void collectCompareIris(Compare compare, Set<String> iriSet) {
    collectValueSourceIris(compare.getLeft(), iriSet);
    collectValueSourceIris(compare.getRight(), iriSet);
    if (compare.getUnits() != null)
      iriSet.add(compare.getUnits().getIri());
  }

  private static void collectValueSourceIris(ValueSource source, Set<String> iriSet) {
    if (source.getIri() != null) {
      iriSet.add(source.getIri());
    }

  }

}
