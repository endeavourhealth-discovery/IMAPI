package org.endeavourhealth.imapi.transform.qofimq;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transform.qofimq.ast.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QofAstToImqMapper {
  private final IriResolver iriResolver;

  public QofAstToImqMapper(IriResolver iriResolver) {
    this.iriResolver = iriResolver;
  }

  public Query toImq(AstNode root) {
    Match m = mapNode(root);
    // Ensure root is a Query
    Query q = new Query();
    // copy Match composition into Query
    if (m.getWhere() != null) q.setWhere(m.getWhere());
    if (m.getAnd() != null) q.setAnd(m.getAnd());
    if (m.getOr() != null) q.setOr(m.getOr());
    if (m.getNot() != null) q.setNot(m.getNot());
    return q;
  }

  private Match mapNode(AstNode node) {
    if (node instanceof BoolNode) {
      return mapBool((BoolNode) node);
    } else if (node instanceof PredicateNode) {
      return mapPredicate((PredicateNode) node);
    } else {
      throw new QofMappingException("Unknown AST node type: " + node.getClass().getSimpleName());
    }
  }

  private Match mapBool(BoolNode b) {
    Match m = new Match();
    List<AstNode> ops = b.getOperands();
    if (b.getOp() == BoolOp.AND) {
      for (AstNode a : ops) m.addAnd(mapNode(a));
      return m;
    } else if (b.getOp() == BoolOp.OR) {
      for (AstNode a : ops) m.addOr(mapNode(a));
      return m;
    } else if (b.getOp() == BoolOp.NOT) {
      // NOT expects exactly one operand (validated earlier)
      for (AstNode a : ops) m.addNot(mapNode(a));
      return m;
    } else {
      throw new QofMappingException("Unsupported boolean operator: " + b.getOp());
    }
  }

  private Match mapPredicate(PredicateNode p) {
    String fieldIri = iriResolver.resolveField(p.getField()).getIri();
    Where w = new Where().setIri(fieldIri);

    switch (p.getOp()) {
      case EQUALS:
        w.setOperator(Operator.eq);
        w.setValue(asString(p.getValue()));
        break;
      case NOT_EQUALS: {
        Match inner = new Match();
        Where eq = new Where().setIri(fieldIri).setOperator(Operator.eq).setValue(asString(p.getValue()));
        inner.setWhere(eq);
        Match m = new Match();
        m.addNot(inner);
        return wrapIfNegated(m, p.isNegated());
      }
      case IN: {
        List<Object> vals = p.getValues();
        if (vals == null || vals.isEmpty()) throw new QofMappingException("IN requires non-empty values: " + p.getField());
        Match or = new Match();
        for (Object v : vals) {
          Match item = new Match();
          item.setWhere(new Where().setIri(fieldIri).setOperator(Operator.eq).setValue(asString(v)));
          or.addOr(item);
        }
        return wrapIfNegated(or, p.isNegated());
      }
      case NOT_IN: {
        List<Object> vals = p.getValues();
        if (vals == null || vals.isEmpty()) throw new QofMappingException("NOT_IN requires non-empty values: " + p.getField());
        Match or = new Match();
        for (Object v : vals) {
          Match item = new Match();
          item.setWhere(new Where().setIri(fieldIri).setOperator(Operator.eq).setValue(asString(v)));
          or.addOr(item);
        }
        Match m = new Match();
        m.addNot(or);
        return wrapIfNegated(m, p.isNegated());
      }
      case RANGE: {
        // Expect value to be a map with optional min/max; map to Range with from/to using gt/gte and lt/lte heuristics.
        if (!(p.getValue() instanceof java.util.Map)) {
          throw new QofMappingException("RANGE value must be an object with min/max for field: " + p.getField());
        }
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> range = (java.util.Map<String, Object>) p.getValue();
        Range r = new Range();
        if (range.containsKey("min")) {
          Object min = range.get("min");
          r.setFrom(new Value().setOperator(Operator.gte).setValue(asString(min)));
        }
        if (range.containsKey("max")) {
          Object max = range.get("max");
          r.setTo(new Value().setOperator(Operator.lte).setValue(asString(max)));
        }
        w.setRange(r);
        break;
      }
      case EXISTS: {
        Object v = p.getValue();
        boolean exists = v == null || Boolean.TRUE.equals(v) || (v instanceof String && Boolean.parseBoolean((String) v));
        if (exists) {
          w.setIsNotNull(true);
        } else {
          w.setIsNull(true);
        }
        break;
      }
      default:
        throw new QofMappingException("Unsupported predicate op: " + p.getOp());
    }

    Match m = new Match();
    m.setWhere(w);
    return wrapIfNegated(m, p.isNegated());
  }

  private Match wrapIfNegated(Match m, boolean negated) {
    if (!negated) return m;
    Match out = new Match();
    out.addNot(m);
    return out;
  }

  private String asString(Object v) {
    return v == null ? null : String.valueOf(v);
  }
}
