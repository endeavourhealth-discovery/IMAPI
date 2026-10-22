package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class KeepAsLookupTest {
  private fun match(node: String, type: String) = Query().setNode(node).setTypeOf(type)

  @Test
  fun findsNodesInAndOrAndColumnGroups() {
    val query = Query()
      .setAnd(listOf(match("a", "http://x#A")))
      .setOr(listOf(match("b", "http://x#B")))
      .setColumnGroup(listOf(Query().setAnd(listOf(match("c", "http://x#C")))))
    val lookup = KeepAsLookup(QueryRequest().setQuery(query))

    assertEquals("http://x#A", lookup.getDataModelFromKeepAs("a"))
    assertEquals("http://x#B", lookup.getDataModelFromKeepAs("b"))
    assertEquals("http://x#C", lookup.getDataModelFromKeepAs("c"))
  }

  @Test
  fun unknownOrNullNodeReturnsNull() {
    val lookup = KeepAsLookup(QueryRequest().setQuery(Query().setAnd(listOf(match("a", "http://x#A")))))

    assertNull(lookup.getDataModelFromKeepAs("missing"))
    assertNull(lookup.getDataModelFromKeepAs(null))
  }

  @Test
  fun mainQueryWinsOverColumnGroupForDuplicateNode() {
    val query = Query()
      .setAnd(listOf(match("a", "http://x#Main")))
      .setColumnGroup(listOf(Query().setAnd(listOf(match("a", "http://x#Group")))))

    assertEquals("http://x#Main", KeepAsLookup(QueryRequest().setQuery(query)).getDataModelFromKeepAs("a"))
  }
}
