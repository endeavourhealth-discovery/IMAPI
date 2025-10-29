# Extending the QOF → IMQ Transformer

This guide describes how to add support for new predicate operators or field types without changing the core mapper.

## Strategy Interface
A lightweight SPI is provided to plug in custom predicate mappings.

- Package: `org.endeavourhealth.imapi.transform.qofimq.spi`
- Interface: `PredicateMappingStrategy`
- Purpose: given a parsed `PredicateNode`, produce an IMQ `Match` if the strategy supports the node.

Registration options:
- Spring: declare strategy beans (`@Component`) and inject them into a custom mapper that delegates before falling back to the default mapping.
- Manual: instantiate and invoke from application code where needed.

## Implementation Steps
1. Create a class implementing `PredicateMappingStrategy` and annotate with `@Component` if using Spring.
2. Implement `supports(node)` to return `true` for your custom operator/shape.
3. Implement `map(node, iriResolver)` to build IMQ `Match`/`Where`/`Range` accordingly.
4. Wire the strategy list into a delegating mapper (see example below) or add a small pre-processing hook before calling the default `QofAstToImqMapper`.

## Example: CONTAINS operator
Suppose inputs include `{ "field": "QOF:name", "op": "CONTAINS", "value": "abc" }`.

```java
@Component
public class ContainsOperatorStrategy implements PredicateMappingStrategy {
  @Override
  public boolean supports(PredicateNode node) {
    return node.getOp().name().equals("CONTAINS");
  }

  @Override
  public Match map(PredicateNode node, IriResolver iriResolver) {
    String fieldIri = iriResolver.resolveField(node.getField()).getIri();
    Where w = new Where().setIri(fieldIri).setOperator(Operator.contains).setValue(String.valueOf(node.getValue()));
    Match m = new Match().setWhere(w);
    if (node.isNegated()) {
      return new Match().addNot(m);
    }
    return m;
  }
}
```

Then create a delegating mapper (optional):

```java
@Component
public class DelegatingQofMapper {
  private final List<PredicateMappingStrategy> strategies;
  private final QofAstToImqMapper defaultMapper;

  public DelegatingQofMapper(List<PredicateMappingStrategy> strategies, QofAstToImqMapper defaultMapper) {
    this.strategies = strategies;
    this.defaultMapper = defaultMapper;
  }

  public Query toImq(AstNode node) {
    if (node instanceof PredicateNode p) {
      for (var s : strategies) {
        if (s.supports(p)) return new Query().setWhere(s.map(p, defaultMapper.getIriResolver()).getWhere());
      }
    }
    return defaultMapper.toImq(node);
  }
}
```

Note: The core mapper currently covers `EQUALS`, `NOT_EQUALS`, `IN`, `NOT_IN`, `RANGE`, `EXISTS`. New operators (e.g., `startsWith`, `contains`) can be layered via strategies.

## Versioning and Backwards Compatibility
- Additive strategies should not change existing behavior.
- Ensure tests cover both positive and negative cases for new operators.
