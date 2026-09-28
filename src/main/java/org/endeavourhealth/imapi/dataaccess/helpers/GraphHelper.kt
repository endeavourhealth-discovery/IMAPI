package org.endeavourhealth.imapi.dataaccess.helpers

import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.query.GraphQuery
import org.endeavourhealth.imapi.model.tripletree.*
import org.endeavourhealth.imapi.vocabulary.IM

/**
 * Query of static methods fpr handling generic sparql query processes
 */
object GraphHelper {
  /**
   * Runs a sparql construct query and processes the results into a TTEntity map with an iri TTEntity map
   *
   * @param qry Fully formed construct query with bound variables
   * @return a map of one or many iris to TTEntity being determined by the query definition
   */
  @JvmStatic
  fun getEntityMap(qry: GraphQuery): TTEntityMap {
    val entityMap = TTEntityMap()
    qry.evaluate().use { gs ->
      val valueMap: MutableMap<String, TTValue> = mutableMapOf()
      for (st in gs) {
        processStatement(entityMap, valueMap, st)
      }
      return entityMap
    }
  }

  /**
   * Generic TTNode/ TTEntity population from a set of triple statements.
   *
   * To use this, call sparql construct query that returns a set of entities making sure it includes blank nodes
   * and then for each statement pass the statement in
   *
   * @param entityMap and iri to TTEntity map for all the entities in the result set
   * @param tripleMap a map between a value and a TT object from the statement
   * @param st        The rdf4j triple statement
   */
  fun processStatement(
    entityMap: TTEntityMap,
    tripleMap: MutableMap<String, TTValue>, st: Statement
  ) {
    val s = st.subject
    val p = st.predicate
    val o = st.getObject()
    val subject = s.stringValue()
    val predicate = p.stringValue()
    val value = o.stringValue()
    if (predicate == IM.PLABEL.toString()) {
      entityMap.addPredicate(subject, value)
    } else if (predicate == IM.OLABEL.toString()) {
      tripleMap.putIfAbsent(subject, TTIriRef.iri(subject))
      tripleMap[subject]!!.asIriRef().setName(value)
    } else {
      val node: TTNode
      tripleMap.putIfAbsent(predicate, TTIriRef.iri(predicate))
      if (s.isIRI) {
        entityMap.entities.putIfAbsent(subject, TTEntity().setIri(subject))
        node = entityMap.entities[subject]!!
      } else {
        tripleMap.putIfAbsent(subject, TTNode())
        node = tripleMap[subject]!!.asNode()
      }
      if (o.isBNode) {
        tripleMap.putIfAbsent(value, TTNode())
        node.addObject(tripleMap[predicate]!!.asIriRef(), tripleMap[value])
      } else if (o.isIRI) {
        tripleMap.putIfAbsent(value, TTIriRef.iri(value))
        node.addObject(tripleMap[predicate]!!.asIriRef(), tripleMap[value])
      } else {
        tripleMap.putIfAbsent(value, TTLiteral.literal(value))
        node.set(tripleMap[predicate]!!.asIriRef(), tripleMap[value]!!.asLiteral())
      }
    }
  }
}
