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

  private var _operator: String? = null
  private var _condition: String? = null
  private var _passResult: String? = null
  private var _failResult: String? = null
  private val _children: MutableList<QOFExpressionNode> = mutableListOf()

  companion object {
    @JvmStatic
    fun createOperatorNode(operator: String): QOFExpressionNode {
      val node = QOFExpressionNode()
      node.operator = operator
      return node
    }

    @JvmStatic
    fun createConditionNode(condition: String): QOFExpressionNode {
      val node = QOFExpressionNode()
      node.condition = condition
      return node
    }
  }

  var operator: String?
    get() = _operator
    set(value) {
      _operator = value
    }

  var condition: String?
    get() = _condition
    set(value) {
      _condition = value
    }

  var children: MutableList<QOFExpressionNode>
    get() = _children
    set(value) {
      _children.clear()
      _children.addAll(value)
    }

  var passResult: String?
    get() = _passResult
    set(value) {
      _passResult = value
    }

  var failResult: String?
    get() = _failResult
    set(value) {
      _failResult = value
    }

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
    } else if (!condition.isNullOrEmpty()) {
      sb.append("$indentStr$ANSI_BLUE$condition$ANSI_RESET\n")
    }

    val result = sb.toString()

    return if (result.replace("\n", "").isBlank()) "==EMPTY==" else result
  }
}
