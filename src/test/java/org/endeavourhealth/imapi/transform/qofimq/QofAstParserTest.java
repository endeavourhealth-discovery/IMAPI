package org.endeavourhealth.imapi.transform.qofimq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.transform.qofimq.ast.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QofAstParserTest {

  @Test
  void parsesNestedBooleanTree() {
    String json = "{\n" +
      "  \"operator\": \"AND\",\n" +
      "  \"operands\": [\n" +
      "    { \"field\": \"QOF:age\", \"op\": \"RANGE\", \"min\": 18, \"max\": 65 },\n" +
      "    { \"operator\": \"OR\", \"operands\": [\n" +
      "        { \"field\": \"QOF:sex\", \"op\": \"EQUALS\", \"value\": \"F\" },\n" +
      "        { \"operator\": \"NOT\", \"operands\": [ { \"field\": \"QOF:pregnant\", \"op\": \"EXISTS\" } ] }\n" +
      "    ] }\n" +
      "  ]\n" +
      "}";

    QofAstParser parser = new QofAstParser();
    AstNode root = parser.parse(json);

    assertTrue(root instanceof BoolNode);
    BoolNode and = (BoolNode) root;
    assertEquals(BoolOp.AND, and.getOp());
    assertEquals(2, and.getOperands().size());

    assertTrue(and.getOperands().get(0) instanceof PredicateNode);
    assertTrue(and.getOperands().get(1) instanceof BoolNode);

    BoolNode or = (BoolNode) and.getOperands().get(1);
    assertEquals(BoolOp.OR, or.getOp());
    assertEquals(2, or.getOperands().size());

    assertTrue(or.getOperands().get(1) instanceof BoolNode);
    BoolNode not = (BoolNode) or.getOperands().get(1);
    assertEquals(BoolOp.NOT, not.getOp());
    assertEquals(1, not.getOperands().size());
  }
}
