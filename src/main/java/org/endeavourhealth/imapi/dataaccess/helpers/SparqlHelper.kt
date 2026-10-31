package org.endeavourhealth.imapi.dataaccess.helpers

import org.eclipse.rdf4j.model.util.Values
import org.eclipse.rdf4j.query.BindingSet
import org.endeavourhealth.imapi.vocabulary.NAMESPACE

object SparqlHelper {

  @JvmStatic
  fun getString(bs: BindingSet, s: String): String? {
    return if (bs.hasBinding(s) && bs.getValue(s) != null) bs.getValue(s).stringValue()
    else null
  }

  @JvmStatic
  fun valueList(param: String, iris: MutableCollection<String>?): String {
    if (iris.isNullOrEmpty()) return ""
    val value = "    VALUES ?${param} {${iris.joinToString(" ") { iri -> "<${Values.iri(iri).stringValue()}>" }}}"
    return value
  }

  @JvmStatic
  fun inList(param: String, size: Int): String =
    (0 until size).joinToString(prefix = "(", postfix = ")") { "?$param$it" }

  @JvmStatic
  fun addSparqlPrefixes(sparql: String): String {
    return """
      PREFIX rdfs: <${NAMESPACE.RDFS}>
      PREFIX im: <${NAMESPACE.IM}>
      PREFIX rdf: <${NAMESPACE.RDF}>
      PREFIX sn: <${NAMESPACE.SNOMED}>
      PREFIX sh: <${NAMESPACE.SHACL}>
      PREFIX xsd: <${NAMESPACE.XSD}>
      
      $sparql
      """.trimIndent()
  }
}
