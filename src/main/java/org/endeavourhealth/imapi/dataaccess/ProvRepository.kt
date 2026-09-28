package org.endeavourhealth.imapi.dataaccess

import org.eclipse.rdf4j.model.util.Values
import org.endeavourhealth.imapi.dataaccess.databases.ProvDB
import org.endeavourhealth.imapi.model.tripletree.TTEntity
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.vocabulary.IM

class ProvRepository {
  fun getProvHistory(iri: String): MutableList<TTEntity> {
    val results: MutableList<TTEntity> = mutableListOf()

    val sql = """
      SELECT *
      WHERE {
        ?prov im:provenanceTarget ?entity ;
        im:effectiveDate ?effectiveDate ;
        im:provenanceActivityType ?activityType .
        Optional {
          ?prov im:provenanceAgent ?agent .
          Optional {?agent rdfs:label ?agentName .}
        }
        Optional {
          ?prov im:usedEntity ?usedEntity .
          Optional {?usedEntity rdfs:label ?usedEntityName .}
        }
        Optional {?activityType rdfs:label ?activityTypeName .}
      } order by desc(?effectiveDate)
      
      """.trimIndent()

    ProvDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.setBinding("entity", Values.iri(iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val entity = TTEntity(bs.getValue("prov").stringValue())
          entity.set(TTIriRef.iri(IM.PROVENANCE_TARGET), iri)
          entity.set(TTIriRef.iri(IM.EFFECTIVE_DATE), bs.getValue("effectiveDate").stringValue())
          entity.set(
            TTIriRef.iri(IM.PROVENANCE_ACTIVITY_TYPE),
            TTIriRef(bs.getValue("activityType").stringValue(), bs.getValue("activityTypeName").toString())
          )
          if (bs.getValue("agent") != null) {
            entity.set(
              TTIriRef.iri(IM.PROVENANCE_AGENT),
              TTIriRef(bs.getValue("agent").stringValue(), bs.getValue("agentName").stringValue())
            )
          }
          if (bs.getValue("usedEntity") != null) {
            entity.set(
              TTIriRef.iri(IM.PROVENANCE_USED), TTIriRef(
                bs.getValue("usedEntity").stringValue(),
                if (bs.getValue("usedEntityName") != null) bs.getValue("usedEntityName").stringValue() else null
              )
            )
          }
          results.add(entity)
        }
      }
    }
    return results
  }
}
