package org.endeavourhealth.imapi.dataaccess

import org.eclipse.rdf4j.model.util.Values
import org.endeavourhealth.imapi.dataaccess.databases.IMDB
import org.endeavourhealth.imapi.dataaccess.databases.IMDB.Companion.getConnection
import org.endeavourhealth.imapi.model.imq.*

class PathRepository {
  private val document = PathDocument()

  fun pathQuery(pathQuery: PathQuery): PathDocument {
    getConnection().use { conn ->
      val targetIri = pathQuery.target.iri
      val source = pathQuery.source.iri
      val paths = getPaths(conn, source, targetIri)
      document.setMatch(paths)
    }
    return document
  }


  private fun getPaths(conn: IMDB, source: String, target: String): MutableList<Query> {
    val result: MutableList<Query> = mutableListOf()
    val sql = """
      select ?path ?pathLabel ?path2 ?path2Label ?where ?whereLabel ?target ?targetName
      where {
        ?target rdfs:label ?targetName.
        {
          ?target ^sh:path ?property.
          ?path2 ^sh:path ?property.
          ?path2 rdfs:label ?path2Label.
          ?property ^sh:property ?source.
        }
        union {
          ?target ^sh:path ?property.
          ?path2 ^sh:path ?property.
          ?path2 rdfs:label ?path2Label.
          ?property ^sh:property ?recordType.
          ?recordType ^sh:node ?recordProperty.
          ?recordProperty sh:path ?path.
          ?path rdfs:label ?pathLabel.
          ?recordProperty ^sh:property ?source.
        }
        union {
          ?target ^im:hasMember ?valueSet.
          ?valueSet ^sh:class ?property.
          ?property sh:path ?where.
          ?where rdfs:label ?whereLabel.
          ?property ^sh:property ?source.
        }
        union {
          ?target ^im:hasMember ?valueSet.
          ?valueSet ^sh:class ?property.
          ?property sh:path ?where.
          ?where rdfs:label ?whereLabel.
          ?property ^sh:property ?recordType.
          ?recordType rdfs:label ?recordTypeLabel.
          ?recordType ^sh:node ?recordProperty.
          ?recordProperty sh:path ?path.
          ?path rdfs:label ?pathLabel.
          ?recordProperty ^sh:property ?source.
        }
        union {
          ?target im:isA ?class.
          ?class ^sh:class ?property.
          ?property sh:path ?path2.
          ?path2 rdfs:label ?path2Label.
          ?property ^sh:property ?source.
        }
        union {
          ?target im:isA ?class.
          ?class ^sh:class ?property.
          ?property sh:path ?where.
          ?where rdfs:label ?whereLabel.
          ?property ^sh:property ?recordType.
          ?recordType rdfs:label ?recordTypeLabel.
          ?recordType ^sh:node ?recordProperty.
          ?recordProperty sh:path ?path.
          ?path rdfs:label ?pathLabel.
          ?recordProperty ^sh:property ?source.
        }
      }
      group by ?path ?pathLabel ?path2 ?path2Label ?where ?whereLabel ?target ?targetName
      
      """.trimIndent()
    //The logic is to look for a target as a record types, properties, value sets or concepts linked to the source.
    val qry = conn.prepareTupleSparql(sql)
    qry.setBinding("target", Values.iri(target))
    qry.setBinding("source", Values.iri(source))
    qry.evaluate().use { rs ->
      while (rs.hasNext()) {
        val bs = rs.next()
        val query = Query()
        result.add(query)
        var pathVariable: String? = null
        bs.getValue("path")?.let { path ->
          val pathIri = path.stringValue()
          pathVariable = pathIri.substring(pathIri.lastIndexOf("#") + 1)
          val pathMatch = Path().setIri(pathIri).setName(bs.getValue("pathLabel").stringValue())
          query.addPath(pathMatch)
        }
        bs.getValue("path2")?.let { path2 ->
          val pathIri = path2.stringValue()
          query.path.first().addPath(Path().setIri(pathIri).setName(bs.getValue("path2Label").stringValue()))
          pathVariable = pathIri.substring(pathIri.lastIndexOf("#") + 1)
        }
        bs.getValue("where")?.let { where ->
          query.setWhere(
            Where()
              .setIri(where.stringValue())
              .setName(bs.getValue("whereLabel").stringValue())
              .addIs(
                Node().setIri(target).setName(bs.getValue("targetName").stringValue())
                  .setDescendantsOrSelfOf(true)
              )
          )
          if (pathVariable != null && query.where != null) query.where.setNodeRef(pathVariable)
        }
      }
    }
    return result
  }
}

