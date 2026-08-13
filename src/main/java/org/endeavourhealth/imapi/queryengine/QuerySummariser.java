package org.endeavourhealth.imapi.queryengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuerySummariser {
  private final EntityRepository repo = new EntityRepository();
  private final Map<String, String> nodeRefToLabel = new HashMap<>();
  @Getter
  private Map<String, TTEntity> iriContext;
  private StringBuilder shortDescription = new StringBuilder();
  private StringBuilder summary = new StringBuilder();
  private int level;


  public String summariseQuery(Query query) throws QueryException, JsonProcessingException {
    new QueryDescriptor().describeSingleMatch(query);
    summariseMatch(query, 0, "");
    return summary.toString();
  }


  public void summariseMatch(Query query, Integer index, String bool) {
    int subIndex = -1;
    if (query.getTypeOf() != null) {
      summary.append(query.getTypeOf().getName()).append(" and ");
      indent();
    }
    if (!bool.isEmpty() && !bool.equals("union") && !bool.equals("step") && index > 0) {
      summary.append(bool).append(" ");
    }
    if (bool.equals("or") && index == 0) {
      summary.append("either ");
    }
    if (query.getFrom() != null) {
      summary.append("From ").append(query.getFrom());
      indent();
    }

    if (query.getOrderBy() != null) {
      summary.append(query.getOrderBy().getDescription());
    }


    if (query.getIs() != null) {
      summariseIs(query.getIs());
    }

    if (query.getOr() != null) {
      level++;
      for (Query subQuery : query.getOr()) {
        subIndex++;
        indent();
        summariseMatch(subQuery, subIndex, "or");
      }
      level--;
    }
    if (query.getAnd() != null) {
      level++;
      for (Query subQuery : query.getAnd()) {
        subIndex++;
        indent();
        summariseMatch(subQuery, subIndex, "and");
      }
      level--;
    }

    if (query.getPath() != null) {
      for (Path path : query.getPath()) {
        summarisePath(path);
      }
    }

    if (query.getWhere() != null) {
      summary.append(" and ");
      summariseWhere(query.getWhere(), 0, "");
    }
    if (query.getNode() != null) {
      summary.append("as (").append(query.getNode()).append(")");
    }
  }

  private void summarisePath(Path path) {
    summary.append(path.getName());
    if (path.getPath() != null) {
      for (Path subPath : path.getPath()) {
        summary.append("->");
        summarisePath(subPath);
      }
    }
  }


  private void summariseWheres(List<Where> wheres, Integer index, String bool) {
    int subIndex = -1;
    for (Where where : wheres) {
      subIndex++;
      summariseWhere(where, subIndex, bool);
    }
  }


  private void summariseIsList(List<Node> inSets) {
    for (Node set : inSets) {
      summariseIs(set);
    }
  }

  private void summariseIs(Node set) {
    if (set.getDescription() != null) {
      summary.append(set.getDescription()).append(" ");
    } else summary.append(set.getName()).append(" ");
    if (set.getMatch() != null) {
      summariseMatch(set.getMatch(), 0, "");
    }
  }


  private void summariseWhere(Where where, Integer index, String bool) {
    if (!bool.isEmpty()) {
      if (index > 0) summary.append(bool).append(" ");
      else if (bool.equals("or")) summary.append("either ");
    }

    if (where.getQualifier() != null) {
      summary.append(where.getQualifier().getName()).append(" of ");
    }
    if (where.getName() != null) {
      summary.append(where.getName()).append(" ");
    }
    if (where.getAnd() != null) {
      summariseWheres(where.getAnd(), index, "and");
    }
    if (where.getOr() != null) {
      summariseWheres(where.getOr(), index, "or");
    }
    if (where.getDescription() != null) {
      summary.append(where.getDescription()).append(" ");
    } else if (where.getIs() != null) {
      summariseIsList(where.getIs());
    }
    if (where.getRange() != null) {
      summariseRange(where.getRange());
    }
    if (where.getCompare() != null)
      summariseCompare(where.getCompare());
  }

  private void summariseRange(Range range) {
    summary.append("between ");
    summariseAssignable(range.getFrom());
    summary.append(" and ");
    summariseAssignable(range.getTo());
  }

  private void summariseAssignable(Value assignable) {
    if (assignable.getOperator() != null)
      summary.append(assignable.getOperator().getValue()).append(" ");
    if (assignable.getValue() != null)
      summary.append(assignable.getValue()).append(" ");

    if (assignable.getCompare() != null)
      summariseCompare(assignable.getCompare());
  }


  private void summariseCompare(Compare compare) {
    summariseValueSource(compare.getLeft());
    if (compare.getUnits() != null)
      summary.append(compare.getUnits().getName()).append(" ");
    summary.append("relative to ");
    summariseValueSource(compare.getRight());
  }

  private void summariseValueSource(ValueSource source) {
    if (source.getName() != null) {
      summary.append(source.getName()).append(" ");
    }
    if (source.getParameter() != null) {
      summary.append(source.getName());
    }
  }


  private void indent() {
    summary.append("\n" + "                         ".substring(0, Math.min(level, 20)));
  }
}