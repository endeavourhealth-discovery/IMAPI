package org.endeavourhealth.imapi.model.qof

const val ANSI_RESET: String = "\u001B[0m"
const val ANSI_RED: String = "\u001B[31m"
const val ANSI_GREEN: String = "\u001B[32m"
const val ANSI_YELLOW: String = "\u001B[33m"
const val ANSI_BLUE: String = "\u001B[34m"
const val ANSI_PURPLE: String = "\u001B[35m"
const val ANSI_CYAN: String = "\u001B[36m"

class QOFExpressionNode {
  val condition: QOFCondition = QOFCondition()
  var operator: String? = null
  var passResult: String? = null
  var failResult: String? = null
  val children: MutableList<QOFExpressionNode> = mutableListOf()

  fun toFormattedString(): String {
    val result = toRecursiveFormattedString(0).trim()

    val finalResult = StringBuilder(result)
    if (passResult != null) {
      finalResult.append("\n${ANSI_GREEN}Pass: $passResult $ANSI_RESET")
    }
    if (failResult != null) {
      finalResult.append("\n${ANSI_RED}Fail: $failResult $ANSI_RESET")
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
      sb.append("$indentStr$ANSI_BLUE${condition.leftOperand}$ANSI_RESET $ANSI_PURPLE${condition.comparator}$ANSI_RESET $ANSI_CYAN${condition.rightOperand}\n")
    }

    val result = sb.toString()

    return if (result.replace("\n", "").isBlank()) "==EMPTY==" else result
  }
}
