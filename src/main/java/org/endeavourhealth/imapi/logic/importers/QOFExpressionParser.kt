package org.endeavourhealth.imapi.logic.importers

import org.endeavourhealth.imapi.model.imq.Operator
import org.endeavourhealth.imapi.model.qof.QOFExpressionNode
import org.endeavourhealth.imapi.utility.logger

class QOFExpressionParser(private var expression: String) {
  private val log = logger(QOFExpressionParser::class.java)
  private val comparators = setOf("=", "<", ">", "<=", ">=")
  private var lastVariable: String? = null

  fun parse(): QOFExpressionNode {
    if (expression.contains("WHERE")) {
      expression = expression.substring(expression.indexOf("WHERE ") + 6).trim()
    }

    log.info("=========================================================================================")
    log.info("Original expression: {}", expression)

    expression = expression
      .replace("≠", "!=")
      .replace("–", "-")
      .replace(Regex("(If |RETURN | {2})"), " ")

    // Remove extra whitespace and normalize
    val normalized = expression.trim()

    log.info("Normalized expression: {}", normalized)

    // Simple approach: look for AND/OR operators with proper grouping
    val root = parseRecursive(normalized)

    if (expression.contains("ELSE ")) {
      val elsePart = expression.substring(expression.indexOf("ELSE ") + 5).trim()
      if (!"NULL".equals(elsePart, ignoreCase = true)) {
        root.failResult = elsePart;
      }
      expression = expression.substring(0, expression.indexOf("ELSE "))
    }

    if (expression.contains("THEN ")) {
      val thenPart = expression.substring(expression.indexOf("THEN ") + 5)
      if (!"NULL".equals(thenPart, ignoreCase = true)) {
        root.passResult = thenPart;
      }
      expression = expression.substring(0, expression.indexOf("THEN "))
    }

    log.info("Parsed expression:\n{}", root.toFormattedString())

    return root
  }

  private fun parseRecursive(expression: String): QOFExpressionNode {
    // Look for top-level AND/OR operations
    val topLevelAnd = findTopLevelOperator(expression, "AND")
    val topLevelOr = findTopLevelOperator(expression, "OR")

    // If we have both AND and OR, use the one that comes first
    val operator: String? = when {
      topLevelAnd != -1 && topLevelOr != -1 -> if (topLevelAnd < topLevelOr) "AND" else "OR"
      topLevelAnd != -1 -> "AND"
      topLevelOr != -1 -> "OR"
      else -> null
    }

    // If we found a top-level operator, split and recurse
    if (operator != null && expression.contains(operator)) {
      return splitOperatorAndParseRecursive(expression, operator)
    }

    // If no top-level operator found, check if it's a simple condition or has nested parentheses
    val cleanedExpression = expression.trim()
    if (cleanedExpression.startsWith("(") && cleanedExpression.endsWith(")")) {
      return parseRecursive(cleanedExpression.substring(1, cleanedExpression.length - 1))
    }

    // Return as a simple condition
    return getOperationFromExpression(cleanedExpression);
  }

  private fun getOperationFromExpression(expression: String): QOFExpressionNode {
    val equalityReplacements = listOf<String?>("!=", " on ", " of ", " at ")

    val result = QOFExpressionNode()

    log.debug("Expression: {}", expression)
    val expParts: Array<String> = splitByOperator(expression)
    log.debug(
      "Parts: {}",
      expParts.contentToString()
    )

    result.condition.leftOperand = expParts[0]
    result.condition.comparator = expParts[1]
    result.condition.rightOperand = expParts[2]

    return result
  }

  private fun splitByOperator(expression: String): Array<String> {
    val operators = arrayOf<String?>(" at ", " on ", " of ", "!=", "<=", ">=", "=", "<", ">")
    for (op in operators) {
      val index = expression.indexOf(op!!)
      if (index != -1) {
        val left = expression.substring(0, index).trim { it <= ' ' }
        val right = expression.substring(index + op.length).trim { it <= ' ' }
        return arrayOf<String>(left, op, right)
      }
    }
    // If no operator found, return the expression as left, empty operator, empty right
    return arrayOf<String>(expression.trim { it <= ' ' }, "", "")
  }
  private fun splitOperatorAndParseRecursive(expression: String, operator: String): QOFExpressionNode {
    val parts = splitAtOperator(expression, operator)
    val node = QOFExpressionNode();
    node.operator = operator;

    for (part in parts) {
      // Remove outer parentheses if present
      var trimmedPart = part.trim()

      if (trimmedPart.startsWith("(") && trimmedPart.endsWith(")")) {
        trimmedPart = trimmedPart.substring(1, trimmedPart.length - 1).trim()
      }

      if (trimmedPart.contains(" ")) {
        val firstPart = trimmedPart.substringBefore(" ")
        if (comparators.contains(firstPart)) {
          trimmedPart = "$lastVariable $trimmedPart"
        } else {
          lastVariable = firstPart
        }
      }

      node.children += parseRecursive(trimmedPart)
    }

    return node
  }

  private fun findTopLevelOperator(expression: String, operator: String): Int {
    var parenCount = 0
    for (i in expression.indices) {
      val c = expression[i]
      when (c) {
        '(' -> parenCount++
        ')' -> parenCount--
        else -> {
          if (parenCount == 0 && isCompleteWord(expression, i, operator)) {
            return i
          }
        }
      }
    }
    return -1
  }

  private fun splitAtOperator(expression: String, operator: String): List<String> {
    val parts = mutableListOf<String>()
    var parenCount = 0
    var start = 0

    for (i in expression.indices) {
      val c = expression[i]
      when (c) {
        '(' -> parenCount++
        ')' -> parenCount--
        else -> {
          if (parenCount == 0 && isCompleteWord(expression, i, operator)) {
            parts.add(expression.substring(start, i).trim())
            start = i + operator.length
          }
        }
      }
    }

    // Add the last part
    if (start < expression.length) {
      parts.add(expression.substring(start).trim())
    }

    return parts
  }

  private fun isCompleteWord(expression: String, i: Int, operator: String): Boolean {
    if (i + operator.length <= expression.length) {
      val substr = expression.substring(i, i + operator.length)
      if (substr == operator) {
        // Check if it's a complete word (surrounded by spaces or start/end)
        val before = if (i > 0) expression[i - 1] else ' '
        val after = if (i + operator.length < expression.length) expression[i + operator.length] else ' '

        return before.isWhitespace() && after.isWhitespace()
      }
    }
    return false
  }
}
