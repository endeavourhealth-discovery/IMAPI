package org.endeavourhealth.imapi.logic.importers;

import org.endeavourhealth.imapi.model.qof.QOFExpressionNode;

import java.util.*;

public class QOFExpressionParser {


  public static QOFExpressionNode parseExpression(String expression) {
    if (expression.contains("WHERE"))
      expression = expression.substring(expression.indexOf("WHERE ") + 6).trim();

    expression = expression
      .replace("If ", "")
      .replace("RETURN ", "")
      .replace("  "," ");

    // Remove extra whitespace and normalize
    String normalized = expression.trim();

    // Simple approach: look for AND/OR operators with proper grouping
    QOFExpressionNode root = parseRecursive(normalized);

    if (expression.contains("ELSE ")) {
      root.setFailResult(expression.substring(expression.indexOf("ELSE ")+5));
      expression = expression.substring(0, expression.indexOf("ELSE "));
    }

    if (expression.contains("THEN ")) {
      root.setPassResult(expression.substring(expression.indexOf("THEN ")+5));
      expression = expression.substring(0, expression.indexOf("THEN "));
    }
    return root;
  }

  private static QOFExpressionNode parseRecursive(String expression) {
    // Look for top-level AND/OR operations
    int topLevelAnd = findTopLevelOperator(expression, "AND");
    int topLevelOr = findTopLevelOperator(expression, "OR");

    // If we have both AND and OR, use the one that comes first
    String operator = null;
    if (topLevelAnd != -1 && topLevelOr != -1) {
      if (topLevelAnd < topLevelOr) {
        operator = "AND";
      } else {
        operator = "OR";
      }
    } else if (topLevelAnd != -1) {
      operator = "AND";
    } else if (topLevelOr != -1) {
      operator = "OR";
    }

    // If we found a top-level operator, split and recurse
    if (operator != null && expression.contains(operator)) {
      List<String> parts = splitAtOperator(expression, operator);
      QOFExpressionNode node = QOFExpressionNode.createOperatorNode(operator);

      for (String part : parts) {
        // Remove outer parentheses if present
        String trimmedPart = part.trim();
        String cleanPart = trimmedPart;

        if (trimmedPart.startsWith("(") && trimmedPart.endsWith(")")) {
          cleanPart = trimmedPart.substring(1, trimmedPart.length() - 1).trim();
        }

        node.addChild(parseRecursive(cleanPart));
      }

      return node;
    }

    // If no top-level operator found, check if it's a simple condition or has nested parentheses
    String cleanedExpression = expression.trim();
    if (cleanedExpression.startsWith("(") && cleanedExpression.endsWith(")")) {
      return parseRecursive(cleanedExpression.substring(1, cleanedExpression.length() - 1));
    }

    // Return as a simple condition
    return QOFExpressionNode.createConditionNode(expression);
  }

  private static int findTopLevelOperator(String expression, String operator) {
    int parenCount = 0;
    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);
      switch (c) {
        case '(':
          parenCount++;
          break;
        case ')':
          parenCount--;
          break;
        default:
          if (parenCount == 0 && i + operator.length() <= expression.length()) {
            String substr = expression.substring(i, i + operator.length());
            if (substr.equals(operator)) {
              // Check if it's a complete word (surrounded by spaces or start/end)
              char before = (i > 0) ? expression.charAt(i - 1) : ' ';
              char after = (i + operator.length() < expression.length()) ?
                expression.charAt(i + operator.length()) : ' ';

              if (Character.isWhitespace(before) && Character.isWhitespace(after)) {
                return i;
              }
            }
          }
          break;
      }
    }
    return -1;
  }

  private static List<String> splitAtOperator(String expression, String operator) {
    List<String> parts = new ArrayList<>();
    int parenCount = 0;
    int start = 0;

    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);
      switch (c) {
        case '(':
          parenCount++;
          break;
        case ')':
          parenCount--;
          break;
        default:
          if (parenCount == 0 && i + operator.length() <= expression.length()) {
            String substr = expression.substring(i, i + operator.length());
            if (substr.equals(operator)) {
              // Check if it's a complete word
              char before = (i > 0) ? expression.charAt(i - 1) : ' ';
              char after = (i + operator.length() < expression.length()) ?
                expression.charAt(i + operator.length()) : ' ';

              if (Character.isWhitespace(before) && Character.isWhitespace(after)) {
                parts.add(expression.substring(start, i).trim());
                start = i + operator.length();
              }
            }
          }
          break;
      }
    }

    // Add the last part
    if (start < expression.length()) {
      parts.add(expression.substring(start).trim());
    }

    return parts;
  }

  public static void main(String[] args) {
    String input = "Find ANY [REV_DAT] WHERE ({REV_DAT} = {WRITPASTP_DAT}  AND  {ASTCONTASS_DAT} > ({REV_DAT} – 1 month)  AND {ASTCONTASS_DAT} <= {REV_DAT} AND {ASTEXACB_DAT} > ({REV_DAT} – 1 month)  AND {ASTEXACB_DAT} <= {REV_DAT}  AND  (REV_DAT > (PPED – 12 months))";

    System.out.println("Input expression:");
    System.out.println(input);
    System.out.println("\nParsed hierarchy:");

    QOFExpressionNode rootNode = parseExpression(input);
    System.out.println(rootNode.toFormattedString());
  }
}
