package org.endeavourhealth.imapi.model.requests

import org.endeavourhealth.imapi.model.iml.Page
import org.endeavourhealth.imapi.model.imq.Argument
import org.endeavourhealth.imapi.vocabulary.GRAPH

class FunctionRequest {
  var functionIri: String? = null
    private set
  var arguments: MutableList<Argument>? = arrayListOf()
    private set
  var page: Page? = null
    private set
  var graph: GRAPH? = null
    private set

  fun setFunctionIri(functionIri: String?): FunctionRequest {
    this.functionIri = functionIri
    return this
  }

  fun setArguments(arguments: MutableList<Argument>): FunctionRequest {
    this.arguments = arguments
    return this
  }

  fun addArgument(argument: Argument): FunctionRequest {
    if (null == arguments) this.arguments = arrayListOf()
    this.arguments!!.add(argument)
    return this
  }

  fun setPage(page: Page?): FunctionRequest {
    this.page = page
    return this
  }

  fun setGraph(graph: GRAPH?): FunctionRequest {
    this.graph = graph
    return this
  }
}
