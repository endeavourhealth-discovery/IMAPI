package org.endeavourhealth.imapi.model.qof

class QOFExpressionNode {
  val ANSI_RESET: String = "\u001B[0m"
  val ANSI_BLACK: String = "\u001B[30m"
  val ANSI_RED: String = "\u001B[31m"
  val ANSI_GREEN: String = "\u001B[32m"
  val ANSI_YELLOW: String = "\u001B[33m"
  val ANSI_BLUE: String = "\u001B[34m"
  val ANSI_PURPLE: String = "\u001B[35m"
  val ANSI_CYAN: String = "\u001B[36m"
  val ANSI_WHITE: String = "\u001B[37m"

  val condition: QOFCondition = QOFCondition();
  var operator: String? = null
  var passResult: String? = null
  var failResult: String? = null
  val children: MutableList<QOFExpressionNode> = mutableListOf()

  fun toFormattedString(): String {
    val result = toRecursiveFormattedString(0).trim()

    val finalResult = StringBuilder(result)
    if (passResult != null) {
      finalResult.append("$ANSI_GREEN\nOn Pass: $passResult $ANSI_RESET")
    }
    if (failResult != null) {
      finalResult.append("$ANSI_RED\nOn Fail: $failResult $ANSI_RESET")
    }

    return finalResult.toString()
  }

  private fun toRecursiveFormattedString(indent: Int): String {
    val sb = StringBuilder()
    val indentStr = "    ".repeat(indent)

    if (!operator.isNullOrEmpty()) {
      sb.append("$indentStr$ANSI_YELLOW$operator$ANSI_RESET\n")
      for (child in children) {
        val childResult = child.toRecursiveFormattedString(indent + 1)
        sb.append(childResult)
      }
    } else if (!condition.leftOperand.isNullOrEmpty()) {
      sb.append("$indentStr$ANSI_BLUE${condition.leftOperand}$ANSI_RESET $ANSI_YELLOW${condition.comparator}$ANSI_RESET $ANSI_CYAN${condition.rightOperand}\n")
    }

    val result = sb.toString()

    return if (result.replace("\n", "").isBlank()) "==EMPTY==" else result
  }
}
