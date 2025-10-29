package org.endeavourhealth.imapi.transform.qofimq;

import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Operator;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.Where;
import org.endeavourhealth.imapi.transform.qofimq.ast.AstNode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QofAstToImqMapperTest {

  private QofImqProperties propsWithQofNs() {
    QofImqProperties p = new QofImqProperties();
    // Built-in Namespace.QOF exists; also test custom mapping works
    p.getNamespaceMappings().put("QOF", "http://endhealth.info/qof#");
    return p;
  }

  private Query transform(String json) {
    QofAstParser parser = new QofAstParser();
    AstNode root = parser.parse(json);
    IriResolver iri = new IriResolver(propsWithQofNs());
    QofAstToImqMapper mapper = new QofAstToImqMapper(iri);
    return mapper.toImq(root);
  }

  @Test
  void mapsEqualsPredicate() {
    String json = "{ \"field\": \"QOF:age\", \"op\": \"EQUALS\", \"value\": 30 }";
    Query q = transform(json);
    Where w = q.getWhere();
    assertNotNull(w);
    assertEquals(Operator.eq, w.getOperator());
    assertEquals("http://endhealth.info/qof#age", w.getIri());
    assertEquals("30", w.getValue());
  }

  @Test
  void mapsRangePredicateToRangeFromTo() {
    String json = "{ \"field\": \"QOF:bmi\", \"op\": \"RANGE\", \"min\": 18.5, \"max\": 25 }";
    Query q = transform(json);
    Where w = q.getWhere();
    assertNotNull(w);
    assertNotNull(w.getRange());
    assertNotNull(w.getRange().getFrom());
    assertNotNull(w.getRange().getTo());
    assertEquals("18.5", w.getRange().getFrom().getValue());
    assertEquals(Operator.gte, w.getRange().getFrom().getOperator());
    assertEquals("25", w.getRange().getTo().getValue());
    assertEquals(Operator.lte, w.getRange().getTo().getOperator());
  }

  @Test
  void mapsInAndNotIn() {
    String inJson = "{ \"field\": \"QOF:code\", \"op\": \"IN\", \"values\": [\"A\", \"B\"] }";
    Query qIn = transform(inJson);
    List<Match> ors = qIn.getOr();
    assertNotNull(ors);
    assertEquals(2, ors.size());
    assertEquals("A", ors.get(0).getWhere().getValue());

    String notInJson = "{ \"field\": \"QOF:code\", \"op\": \"NOT_IN\", \"values\": [\"A\", \"B\"] }";
    Query qNotIn = transform(notInJson);
    assertNotNull(qNotIn.getNot());
    assertFalse(qNotIn.getNot().isEmpty());
  }

  @Test
  void mapsExistsAndNegations() {
    String existsJson = "{ \"field\": \"QOF:smoker\", \"op\": \"EXISTS\" }";
    Where w1 = transform(existsJson).getWhere();
    assertTrue(w1.getIsNotNull());

    String notExistsJson = "{ \"field\": \"QOF:smoker\", \"op\": \"EXISTS\", \"value\": false }";
    Where w2 = transform(notExistsJson).getWhere();
    assertTrue(w2.getIsNull());

    String negatedEq = "{ \"field\": \"QOF:age\", \"op\": \"EQUALS\", \"value\": 21, \"negated\": true }";
    Query qNeg = transform(negatedEq);
    assertNotNull(qNeg.getNot());
    assertEquals(1, qNeg.getNot().size());
  }

  @Test
  void mapsNotEqualsWrapsNot() {
    String json = "{ \"field\": \"QOF:age\", \"op\": \"NOT_EQUALS\", \"value\": 21 }";
    Query q = transform(json);
    assertNotNull(q.getNot());
    assertEquals(1, q.getNot().size());
  }
}
