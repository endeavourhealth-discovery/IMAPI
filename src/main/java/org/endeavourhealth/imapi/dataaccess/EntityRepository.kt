package org.endeavourhealth.imapi.dataaccess

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.checkerframework.checker.nullness.qual.MonotonicNonNull
import org.eclipse.rdf4j.model.BNode
import org.eclipse.rdf4j.model.Literal
import org.eclipse.rdf4j.model.Resource
import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.model.util.Values
import org.eclipse.rdf4j.query.*
import org.endeavourhealth.imapi.dataaccess.databases.IMDB
import org.endeavourhealth.imapi.dataaccess.entity.Tpl
import org.endeavourhealth.imapi.dataaccess.helpers.DALException
import org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.getString
import org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.valueList
import org.endeavourhealth.imapi.errorhandling.DataMissingException
import org.endeavourhealth.imapi.model.EntityReferenceNode
import org.endeavourhealth.imapi.model.Namespace
import org.endeavourhealth.imapi.model.Pageable
import org.endeavourhealth.imapi.model.dto.ParentDto
import org.endeavourhealth.imapi.model.iml.Entity
import org.endeavourhealth.imapi.model.search.EntityDocument
import org.endeavourhealth.imapi.model.search.SearchResultSummary
import org.endeavourhealth.imapi.model.search.SearchTermCode
import org.endeavourhealth.imapi.model.sql.SubQueryDependency
import org.endeavourhealth.imapi.model.tripletree.*
import org.endeavourhealth.imapi.transforms.TTManager
import org.endeavourhealth.imapi.vocabulary.*
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*
import java.util.function.Consumer
import kotlin.math.min

class EntityRepository {
  private var row = 0
  private val log = LoggerFactory.getLogger(javaClass)

  fun getEntityReferenceByIri(iri: String): TTIriRef {
    val result = TTIriRef()
    val sql = """
      SELECT ?sname
      WHERE {
        ?s rdfs:label ?sname .
        optional { ?s rdfs:comment ?sdescription }
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("s", Values.iri(iri))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          result.setIri(iri).setName(bs.getValue("sname").stringValue())
          if (bs.getValue("sdescription") != null) result.setDescription(bs.getValue("sdescription").stringValue())
        }
      }
    }
    return result
  }

  fun getEntitiesByType(type: String, offset: Int, limit: Int, vararg predicates: String): MutableList<TTEntity> {
    val entities: MutableMap<String, TTEntity> = hashMapOf()
    val predicateList = predicates.toMutableList().joinToString(" ") { p -> "<$p>" }
    val sql: String = """
      select ?entity ?type ?predicate ?object ?predicate2 ?object2
      
      where {
      values ?predicate {$predicateList}
      ?entity rdf:type ?type.
      ?entity ?predicate ?object.
      optional {
        ?object ?predicate2 ?object2.
        filter (isBlank(?object))
        filter (!isBlank(?object2))
        }
      }
      order by ?object
      offset $offset limit $limit
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("type", Values.iri(type))
      val blankNodes: MutableMap<String, TTNode> = hashMapOf()
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val iri = bs.getValue("entity").stringValue()
          val entity = TTEntity().setIri(iri)
          entities.putIfAbsent(iri, entity)
          entity.addObject(
            Values.iri(RDF.TYPE.toString()) as TTIriRef,
            TTIriRef.iri(bs.getValue("type").stringValue())
          )
          val `object` = bs.getValue("object")
          if (`object`.isIRI) {
            entity.addObject(TTIriRef.iri(bs.getValue("predicate").stringValue()), TTIriRef.iri(`object`.stringValue()))
          } else if (`object`.isBNode) {
            var node = blankNodes[`object`.stringValue()]
            if (node == null) {
              node = TTNode()
              blankNodes[`object`.stringValue()] = TTNode()
              entity.addObject(TTIriRef.iri(bs.getValue("predicate").stringValue()), node)
            }
            val object2 = bs.getValue("object2")
            if (object2.isIRI) {
              node.set(TTIriRef.iri(bs.getValue("predicate2").stringValue()), TTIriRef.iri(object2.stringValue()))
            } else node.set(
              TTIriRef.iri(bs.getValue("predicate2").stringValue()),
              TTLiteral.literal(object2.stringValue())
            )
          } else {
            entity.addObject(
              TTIriRef.iri(bs.getValue("predicate").stringValue()),
              TTLiteral.literal(`object`.stringValue())
            )
          }
        }
      }
    }
    return entities.values.toMutableList()
  }

  fun getTypesByIris(stringIris: MutableSet<String>): Map<String, MutableSet<TTIriRef>> {
    val iriToTypesMap: MutableMap<String, MutableSet<TTIriRef>> = hashMapOf()

    val sql: String = """
      SELECT ?s ?o ?oname
      WHERE {
        ?s rdf:type ?o .
        ?o rdfs:label ?oname .
        VALUES ?s { ${getIriLine(stringIris)} }
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val iri = bs.getValue("s").stringValue()
          iriToTypesMap.getOrPut(iri) { hashSetOf() }
            .add(TTIriRef(bs.getValue("o").stringValue(), bs.getValue("oname").stringValue()))
        }
      }
    }
    return iriToTypesMap.toMap()
  }

  @Throws(DataMissingException::class)
  fun getEntitySummariesByIris(stringIris: MutableSet<String>): MutableList<SearchResultSummary> {
    val summaries: MutableList<SearchResultSummary> = mutableListOf()

    val sql: String = """
      SELECT ?s ?sname ?scode ?sstatus ?sstatusname ?sdescription ?sscheme ?sschemename
      WHERE {
        ?s rdfs:label ?sname .
        OPTIONAL { ?s im:code ?scode . }
        OPTIONAL {
          ?s im:status ?sstatus .
          ?sstatus rdfs:label ?sstatusname .
        }
        OPTIONAL {
          ?s im:scheme ?sscheme .
          ?sscheme rdfs:label ?sschemename
        }
        OPTIONAL { ?s rdfs:comment ?sdescription .}
        VALUES ?s { ${getIriLine(stringIris)} }
      }
      
      """.trimIndent()

    val iriToTypesMap = getTypesByIris(stringIris)

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val iri = bs.getValue("s").stringValue()
          if (null == bs.getValue("sscheme")) {
            throw DataMissingException("Entity $iri is missing required data im:scheme")
          }
          if (null == bs.getValue("sstatus")) {
            throw DataMissingException("Entity $iri is missing required data im:status")
          }
          val types: MutableSet<TTIriRef>? = iriToTypesMap[iri]
          if (types.isNullOrEmpty()) {
            throw DataMissingException("Entity $iri is missing required data rdf:type")
          }
          val summary = SearchResultSummary()
          summary
            .setIri(iri)
            .setName(bs.getValue("sname").stringValue())
            .setCode(bs.getValue("scode")?.stringValue() ?: "")
            .setScheme(
              TTIriRef(
                bs.getValue("sscheme").stringValue(),
                (bs.getValue("sschemename")?.stringValue() ?: "")
              )
            )
            .setType(types)
            .setStatus(
              TTIriRef(
                bs.getValue("sstatus").stringValue(),
                bs.getValue("sstatusname")?.stringValue() ?: ""
              )
            )
            .setDescription(bs.getValue("sdescription")?.stringValue() ?: "")
          summaries.add(summary)
        }
      }
    }
    if (summaries.size != stringIris.size) {
      val missing = stringIris
        .filter { iri: String? -> summaries.none { summ: SearchResultSummary -> summ.iri == iri } }

      throw DataMissingException("One of more iris do not exist. Missing iris: $missing")
    }
    return summaries
  }

  @Throws(DataMissingException::class)
  fun getEntitySummaryByIri(iri: String): SearchResultSummary {
    val sql = """
      SELECT ?s ?sname ?type ?typeName ?scode ?sstatus ?sstatusname ?sdescription ?sscheme ?sschemename ?intervalUnit ?intervalUnitName ?qualifier ?qualifierName
      WHERE {
        ?s rdfs:label ?sname .
        OPTIONAL { ?s im:code ?scode . }
        OPTIONAL {
          ?s im:status ?sstatus .
          ?sstatus rdfs:label ?sstatusname .
        }
        OPTIONAL {
          ?s im:scheme ?sscheme .
          ?sscheme rdfs:label ?sschemename .
        }
        OPTIONAL {
          ?s rdf:type ?type .
          ?type rdfs:label ?typeName .
        }
        OPTIONAL { ?s rdfs:comment ?sdescription . }
        OPTIONAL {
          ?s im:intervalUnit ?intervals .
          ?intervalUnit rdfs:subClassOf ?intervals .
          ?intervalUnit rdfs:label ?intervalUnitName .
          filter (?intervalUnit!= ?intervals)
        }
        OPTIONAL {
          ?s im:datatypeQualifier ?qualifier .
          ?qualifier rdfs:label ?qualifierName .
        }
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("s", Values.iri(iri))
      try {
        qry.evaluate().use { rs ->
          val result = SearchResultSummary()
          while (rs.hasNext()) {
            val bs = rs.next()
            result.setIri(bs.getValue("s").stringValue())
            if (null != bs.getValue("sname")) {
              result.setName(bs.getValue("sname").stringValue())
            } else throw DataMissingException("Entity $iri is missing required data rdfs:label")
            if (null != bs.getValue("scode")) {
              result.setCode(bs.getValue("scode").stringValue())
            }
            if (null != bs.getValue("sstatus")) {
              result.setStatus(
                TTIriRef(
                  bs.getValue("sstatus").stringValue(),
                  bs.getValue("sstatusname").stringValue() ?: ""
                )
              )
            } else throw DataMissingException("Entity $iri is missing required data im:status")
            if (bs.hasBinding("sdescription")) {
              result.setDescription(
                bs.getValue("sdescription").stringValue() ?: ""
              )
            }
            if (bs.hasBinding("type")) {
              result.addType(
                TTIriRef.iri(bs.getValue("type").stringValue())
                  .setName(bs.getValue("typeName").stringValue())
              )
            } else throw DataMissingException("Entity $iri is missing required data rdf:type")

            if (bs.hasBinding("sscheme")) {
              result.setScheme(
                TTIriRef(
                  bs.getValue("sscheme").stringValue(),
                  bs.getValue("sschemename").stringValue() ?: ""
                )
              )
            } else throw DataMissingException("Entity $iri is missing required data im:scheme")
            if (bs.hasBinding("intervalUnit")) {
              result.addIntervalUnit(
                TTIriRef.iri(bs.getValue("intervalUnit").stringValue())
                  .setName(bs.getValue("intervalUnitName").stringValue())
              )
            }
            if (bs.hasBinding("qualifier")) {
              val qualifier = TTIriRef.iri(bs.getValue("qualifier").stringValue())
                .setName(bs.getValue("qualifierName").stringValue())
              if (result.qualifier?.contains(qualifier) != true) {
                result.addQualifier(qualifier)
              }
            }
          }
          return result
        }
      } catch (e: QueryEvaluationException) {
        throw DataMissingException("Entity with iri: $iri does not exist")
      }
    }
  }

  fun findParentHierarchies(iri: String): MutableList<ParentDto?> {
    val result: MutableList<ParentDto?> = ArrayList<ParentDto?>()

    val spql: String = """
      SELECT *
      WHERE {
        ?s ($PARENT_PREDICATES) ?o .
        ?o rdfs:label ?name .
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spql)
      qry.setBinding("s", Values.iri(iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          result.add(ParentDto(bs.getValue("o").stringValue(), bs.getValue("name").stringValue(), null))
        }
      }
    }
    return result
  }

  fun iriExists(iri: String): Boolean {
    var result = false

    IMDB.getConnection().use { conn ->
      val sql = """
        SELECT *
        WHERE {
          ?s ?p ?o.
        }
        limit 1
        
        """.trimIndent()
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("s", Values.iri(iri))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          result = true
        }
      }
    }
    return result
  }

  val iM1SchemeOptions: MutableList<String>
    get() {
      val results: MutableList<String> = mutableListOf()

      val sql = """
      SELECT DISTINCT ?o
      WHERE {
        ?s ?im1Scheme ?o .
      }
      
      """.trimIndent()

      IMDB.getConnection().use { conn ->
        val qry: TupleQuery = conn.prepareTupleSparql(sql)
        qry.setBinding("im1Scheme", NAMESPACE.IM1.asDbIri())
        qry.evaluate().use { rs ->
          while (rs.hasNext()) {
            val bs = rs.next()
            results.add(bs.getValue("o").stringValue())
          }
        }
      }
      return results
    }

  fun predicatePathExists(subject: String, predicate: TTIriRef, `object`: String): Boolean {
    IMDB.getConnection().use { conn ->
      val sprql = """
          ASK {
            ?s (?p)* ?o.
          }
        
        """.trimIndent()
      val qry: BooleanQuery = conn.prepareBooleanSparql(sprql)
      qry.setBinding("p", Values.iri(predicate.iri))
      qry.setBinding("s", Values.iri(subject))
      qry.setBinding("o", Values.iri(`object`))
      return qry.evaluate()
    }
  }

  fun getOSDocument(iri: String): EntityDocument {
    val result = EntityDocument().setIri(iri)
    hydrateCoreProperties(result)
    hydrateTerms(result)
    hydrateIsAs(result)
    hydrateSubsumptionCount(result)
    return result
  }

  private fun hydrateCoreProperties(entityDocument: EntityDocument) {
    val spql = """
      SELECT ?iri ?name ?preferredName ?status ?statusName ?code ?scheme ?schemeName ?type ?typeName ?usageTotal ?extraType ?extraTypeName
      WHERE {
        ?iri rdf:type ?type .
        Optional { ?iri rdfs:label ?name.}
        Optional { ?iri im:preferredName ?preferredName.}
        Optional {
          ?iri im:isA ?extraType.
          ?extraType rdfs:label ?extraTypeName.
          filter (?extraType in (im:dataModelProperty, im:DataModelEntity))
        }
        Optional {?type rdfs:label ?typeName}
        Optional {
          ?iri im:status ?status.
          Optional {?status rdfs:label ?statusName}
        }
        Optional {
          ?iri im:scheme ?scheme
          Optional {?scheme rdfs:label ?schemeName .}
        }
        Optional {?iri im:code ?code.}
        Optional {?iri im:usageTotal ?usageTotal.}
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val tupleQuery: TupleQuery = conn.prepareTupleSparql(spql)
      tupleQuery.setBinding("iri", Values.iri(entityDocument.iri))
      tupleQuery.evaluate().use { qr ->
        if (qr.hasNext()) {
          hydrateCorePropertiesSetEntityDocumentProperties(entityDocument, qr)
        }
      }
    }
  }

  private fun hydrateTerms(entityDocument: EntityDocument) {
    val sparql = """
      SELECT ?termCode ?synonym ?termCodeStatus
      WHERE {
      ?iri im:hasTermCode ?tc.
        Optional {?tc im:code ?termCode}
        Optional  {?tc rdfs:label ?synonym}
        Optional  {?tc im:status ?termCodeStatus}
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val tupleQuery: TupleQuery = conn.prepareTupleSparql(sparql)
      tupleQuery.setBinding("iri", Values.literal(entityDocument.iri))
      tupleQuery.evaluate().use { qr ->
        while (qr.hasNext()) {
          val rs = qr.next()
          var termCode: String? = null
          var synonym: String? = null
          var status: TTIriRef? = null
          if (rs.hasBinding("synonym")) synonym = rs.getValue("synonym").stringValue().lowercase(Locale.getDefault())
          if (rs.hasBinding("termCode")) termCode = rs.getValue("termCode").stringValue()
          if (rs.hasBinding("termCodeStatus")) status = TTIriRef.iri(rs.getValue("termCodeStatus").stringValue())

          if (synonym != null) {
            val tc = getTermCode(entityDocument, synonym)
            if (tc == null) {
              entityDocument.addTermCode(synonym, termCode, status, null)
            } else if (termCode != null) {
              tc.setCode(termCode)
            }
          } else if (termCode != null) {
            val tc = getTermCodeFromCode(entityDocument, termCode)
            if (tc == null) entityDocument.addTermCode(null, termCode, status, null)
          }
        }
      }
    }
  }

  private fun hydrateSubsumptionCount(entityDocument: EntityDocument) {
    val sparql = """
      SELECT (count(?subType) as ?subsumptions)
      WHERE {
        ?iri ^im:isA ?subType.
        ?subType im:status im:Active.
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val tupleQuery: TupleQuery = conn.prepareTupleSparql(sparql)
      tupleQuery.setBinding("iri", Values.literal(entityDocument.iri))
      tupleQuery.evaluate().use { qr ->
        while (qr.hasNext()) {
          val rs = qr.next()
          entityDocument.setSubsumptionCount(rs.getValue("subsumptions").stringValue().toInt())
        }
      }
    }
  }

  private fun getTermCode(blob: EntityDocument, term: String): SearchTermCode? {
    for (tc in blob.termCode) {
      if (tc.term != null && tc.term == term) return tc
    }
    return null
  }

  private fun getTermCodeFromCode(blob: EntityDocument, code: String): SearchTermCode? {
    for (tc in blob.termCode) {
      if (tc.code == code) return tc
    }
    return null
  }

  private fun hydrateIsAs(entityDocument: EntityDocument) {
    val spql = """
      SELECT ?superType
      WHERE {
        ?iri im:isA ?superType.
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val tupleQuery: TupleQuery = conn.prepareTupleSparql(spql)
      tupleQuery.setBinding("iri", Values.literal(entityDocument.iri))
      tupleQuery.evaluate().use { qr ->
        while (qr.hasNext()) {
          val rs = qr.next()
          entityDocument.isA.add(TTIriRef.iri(rs.getValue("superType").stringValue()))
        }
      }
    }
  }

  fun getPredicates(iri: String): MutableSet<String> {
    val predicates: MutableSet<String> = hashSetOf()
    IMDB.getConnection().use { conn ->
      val sparql = """
        SELECT DISTINCT ?p
        WHERE {
          ?s ?p ?o .
        }
        
        """.trimIndent()
      val qry: TupleQuery = conn.prepareTupleSparql(sparql)
      qry.setBinding("s", Values.iri(iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          predicates.add(bs.getValue("p").stringValue())
        }
      }
    }
    return predicates
  }

  fun findAncestorsByType(
    childIri: String,
    relationshipIri: VocabEnum,
    candidateAncestorIris: MutableList<String>
  ): MutableList<TTIriRef> {
    val result: MutableList<TTIriRef> = mutableListOf()
    val sql = """
      SELECT ?aname
      WHERE {
        ?c ?r ?a .
        ?a rdfs:label ?aname .
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("c", Values.iri(childIri))
      qry.setBinding("r", relationshipIri.asDbIri())
      for (candidate in candidateAncestorIris) {
        qry.setBinding("a", Values.iri(candidate))
        qry.evaluate().use { rs ->
          if (rs.hasNext()) {
            val bs = rs.next()
            result.add(TTIriRef().setIri(candidate).setName(bs.getValue("aname").stringValue()))
          }
        }
      }
    }
    return result
  }

  /**
   * An alternative method of getting an entity definition assuming all predicates inclided
   *
   * @param iri of the entity
   * @return a bundle including the entity and the predicate names
   */
  fun getBundle(iri: String): TTBundle {
    return getBundle(iri, null, false)
  }

  /**
   * An alternative method of getting an entity definition
   *
   * @param iri        of the entity
   * @param predicates List of predicates to `include`
   * @return bundle with entity and map of predicate names
   */
  fun getBundle(iri: String, predicates: MutableSet<String>?): TTBundle {
    return getBundle(iri, predicates, false)
  }

  /**
   * An alternative method of getting an entity definition
   *
   * @param iri               of the entity
   * @param predicates        List of predicates
   * @param excludePredicates Flag denoting if predicate list is inclusion or exclusion
   * @return Bundle
   */
  fun getBundle(iri: String, predicates: MutableSet<String>?, excludePredicates: Boolean): TTBundle {
    return getBundle(iri, predicates, excludePredicates, 5)
  }

  fun getBundle(iri: String, predicates: MutableSet<String>?, excludePredicates: Boolean, depth: Int): TTBundle {
    var predicates = predicates
    val bundle = TTBundle().setEntity(TTEntity().setIri(iri)).setPredicates(HashMap<String?, String?>())
    if (null != predicates && predicates.contains(RDFS.LABEL.toString()) && !predicates.contains(RDFS.COMMENT.toString())) {
      val predicatesPlus: MutableSet<String> = predicates.toMutableSet()
      predicatesPlus.add(RDFS.COMMENT.toString())
      predicates = predicatesPlus
    }

    val sql = getBundleSparql(predicates, excludePredicates, depth)

    IMDB.getConnection().use { conn ->
      val qry: GraphQuery = conn.prepareGraphSparql(sql.toString())
      qry.setBinding("entity", Values.iri(iri))
      qry.evaluate().use { gs ->
        val valueMap: MutableMap<String, TTValue> = mutableMapOf()
        for (st in gs) {
          processStatement(bundle, valueMap, iri, st)
        }
        val iris = TTManager.getIrisFromNode(bundle.entity)
        getIriNames(conn, iris)
        val iriToName = hashMapOf<String, String>()
        for (ttIriRef in iris) {
          iriToName[ttIriRef.iri] = ttIriRef.name
        }
        setNames(bundle.entity, iriToName)
        iris.forEach(Consumer { predicate: TTIriRef -> bundle.addPredicate(predicate) })
      }
      return bundle
    }
  }

  private fun setNames(subject: TTValue, iriToName: MutableMap<String, String>) {
    if (subject.isIriRef && subject.asIriRef().name.isNullOrEmpty()) subject.asIriRef()
      .setName(iriToName[subject.asIriRef().iri])
    else if (subject.isNode && subject.asNode().predicateMap != null) {
      for ((_, value) in subject.asNode().predicateMap) {
        for (value in value.elements) {
          if (value.isIriRef && value.asIriRef().name.isNullOrEmpty()) value.asIriRef()
            .setName(iriToName[value.asIriRef().iri])
          else if (value.isNode) setNames(value, iriToName)
        }
      }
    }
  }

  fun getNames(iris: MutableSet<TTIriRef>) {
    IMDB.getConnection().use { conn ->
      getIriNames(conn, iris)
    }
  }

  /**
   * creates ranges for properties without ranges where the super properties have them
   */
  fun inheritRanges(graph: GRAPH) {
    IMDB.getConnection().use { conn ->
      inheritRanges(conn, graph)
    }
  }

  fun inheritRanges(conn: IMDB, graph: GRAPH) {
    val sql = """
      INSERT { ?property rdfs:range ?range }
      WHERE {
        ?property rdf:type rdf:Property.
        {
          #filter (?property= <http://snomed.info/sct#10362801000001104> )
          ?property (rdfs:subClassOf)* ?superclass.
          filter not exists {
            ?property (rdfs:subClassOf) [
              (rdfs:subClassOf)+ ?superclass
            ]
          }
          filter not exists {?property rdfs:range ?anyrange}
          ?superclass rdfs:range ?range.
          ?range rdfs:label ?rlabel.
        }
      }
      
      """.trimIndent()
    val upd = conn.prepareInsertSparql(sql, graph)
    upd.execute()
  }

  /**
   * Returns an entity iri and name from a code or a term code
   *
   * @param code the code or description id or term code
   * @return iri and name of entity
   */
  fun getCoreFromCode(code: String?, namespaces: List<NAMESPACE>): MutableSet<Entity>? {
    val schemes = namespaces.map { obj -> obj.toString() }.toMutableList()

    val sql: String = """
      SELECT ?concept ?label ?legacyLabel ?type
      WHERE {
        values ?code {"$code"}
        values ?codeProperty {im:code im:codeId im:alternativeCode}
        ${valueList("scheme", schemes)}
        {
          ?concept ?codeProperty ?code.
          filter (isIri(?concept))
          ?concept rdfs:label ?label.
          ?concept im:scheme ?scheme.
          filter (?scheme in(sn:,im:))
        }
        UNION {
          ?concept im:hasTermCode ?node.
          ?node im:code ?code.
          ?concept rdfs:label ?label.
           ?concept im:scheme ?scheme.
          filter (?scheme in(sn:,im:))
        }
        UNION {
          ?legacy im:hasTermCode ?node.
          ?node im:code ?code.
          ?legacy im:matchedTo ?concept.
          ?concept rdfs:label ?label.
          ?concept im:scheme ?schemes.
        }
        UNION {
          ?legacy ?codeProperty ?code.
          ?legacy im:hasTermCode ?tc.
          ?tc rdfs:label ?legacyLabel.
          ?legacy im:matchedTo ?concept.
          ?concept rdfs:label ?label.
          ?concept im:scheme ?schemes .
        }
        ?concept rdf:type ?type.
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      return getConceptRefsFromResult(qry)
    }
  }


  /**
   * Returns a core entity iri and name from a legacy term
   *
   * @param term      the code or description id or term code
   * @param namespace the legacy scheme of the term
   * @return iri and name of entity
   */
  fun getCoreFromLegacyTerm(term: String, namespace: NAMESPACE): MutableSet<Entity>? {
    val sql = """
      SELECT ?concept ?label ?type
      WHERE {
          ?legacy rdfs:label ?term.
          ?legacy im:matchedTo ?concept.
          ?legacy im:hasScheme ?scheme.
          ?concept rdfs:label ?label.
          ?concept rdf:type ?type.
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("term", Values.literal(term))
      qry.setBinding("scheme", namespace.asDbIri())
      return getConceptRefsFromResult(qry)
    }
  }

  /**
   * Returns an entity iri and name from a term code
   *
   * @param code      the code that is a term code
   * @param namespace the scheme of the term
   * @return set of iris and name of entity
   */
  fun getReferenceFromTermCode(code: String, namespace: NAMESPACE): MutableSet<Entity>? {
    val sql = """
      SELECT ?concept ?label ?type
      WHERE {
          ?concept im:scheme ?scheme .
          ?concept im:hasTermCode ?tc.
          ?tc im:code ?code.
          ?concept rdfs:label ?label.
          ?concept rdf:type ?type.
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("code", Values.literal(code))
      qry.setBinding("scheme", namespace.asDbIri())
      return getConceptRefsFromResult(qry)
    }
  }

  fun getCodesToIri(namespace: NAMESPACE): MutableMap<String, String> {
    val sql: String = """
      SELECT ?code ?scheme ?iri ?altCode
      WHERE {
        Values ?scheme {<$namespace>}
        ?iri im:code ?code.
        OPTIONAL {?iri im:alternativeCode ?altCode}
        ?iri im:scheme ?scheme
      }
      
      """.trimIndent()
    return getCodes(sql)
  }

  val codeToIri: MutableMap<String, String>
    get() {
      val sql = """
      SELECT ?code ?scheme ?iri ?altCode
      WHERE {
        ?iri im:code ?code.
        OPTIONAL {?iri im:alternativeCode ?altCode}
        ?iri im:scheme ?scheme
      }
      
      """.trimIndent()
      return getCodes(sql)
    }

  private fun getCodes(sql: String): MutableMap<String, String> {
    val codeToIri: MutableMap<String, String> = hashMapOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.evaluate().use { gs ->
        while (gs.hasNext()) {
          val bs = gs.next()
          val code = bs.getValue("code").stringValue()
          val scheme = bs.getValue("scheme").stringValue()
          val iri = bs.getValue("iri").stringValue()
          codeToIri[scheme + code] = iri
          if (bs.getValue("altCode") != null) {
            codeToIri[scheme + bs.getValue("altCode").stringValue()] = iri
          }
        }
      }
    }
    return codeToIri
  }

  /**
   * Returns A core entity iri and name from a core term
   *
   * @param term the code or description id or term code
   * @return iri and name of entity
   */
  fun getReferenceFromCoreTerm(term: String): TTIriRef? {
    val schemes = VocabUtils.asArrayList(NAMESPACE.IM, NAMESPACE.SNOMED)
    val sql: String = """
      select ?concept ?label
      where {
        ${valueList("scheme", schemes)}
        {
          ?concept rdfs:label ?label;
                   im:scheme ?scheme.
          filter(isIri(?concept))
        }
        union {
          ?tc rdfs:label ?label.
          ?concept im:hasTermCode ?tc;
                   im:scheme ?scheme.
        }
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("label", Values.literal(term))
      return getConceptRefFromResult(qry)
    }
  }

  val allMatchedLegacy: MutableMap<String, MutableSet<String>>
    get() {
      val sql = """
      SELECT ?legacy ?concept
      WHERE {
        ?legacy im:matchedTo ?concept.
      }
      
      """.trimIndent()
      val maps: MutableMap<String, MutableSet<String>> = hashMapOf()
      IMDB.getConnection().use { conn ->
        val qry: TupleQuery = conn.prepareTupleSparql(sql)
        val concept = TTIriRef()
        qry.evaluate().use { gs ->
          while (gs.hasNext()) {
            val bs = gs.next()
            val legacy = bs.getValue("legacy").stringValue()
            maps.getOrPut(legacy) { hashSetOf() }.add(bs.getValue("concept").toString())
            if (bs.getValue("label") != null) concept.setName(bs.getValue("label").stringValue())
          }
        }
      }
      return maps
    }

  private fun getConceptRefFromResult(qry: TupleQuery): TTIriRef? {
    var concept: TTIriRef? = null
    qry.evaluate().use { gs ->
      while (gs.hasNext()) {
        val bs = gs.next()
        concept = TTIriRef.iri(bs.getValue("concept").stringValue())
        if (bs.getValue("label") != null) concept.setName(bs.getValue("label").stringValue())
      }
    }
    return concept
  }

  private fun getConceptRefsFromResult(qry: TupleQuery): MutableSet<Entity>? {
    var results: MutableSet<Entity>? = null
    val iris: MutableSet<String?> = hashSetOf()
    qry.evaluate().use { gs ->
      while (gs.hasNext()) {
        val bs = gs.next()
        if (results == null) results = hashSetOf()
        val iri = bs.getValue("concept").stringValue()
        if (!iris.contains(iri)) {
          iris.add(iri)
          val concept = Entity().setIri(iri)
          concept.addType(TTIriRef.iri(bs.getValue("type").stringValue()))
          if (bs.getValue("label") != null) concept.setName(bs.getValue("label").stringValue())
          if (bs.getValue("legacyLabel") != null) concept.setName(bs.getValue("legacyLabel").stringValue())
          results.add(concept)
        }
      }
    }
    return results
  }

  private fun getBundleSparql(predicates: Set<String>?, excludePredicates: Boolean, depth: Int): StringJoiner {
    val sql = StringJoiner(System.lineSeparator())
    sql.add("CONSTRUCT {").add("  ?entity ?1predicate ?1Level.").add("  ?1Level rdfs:label ?1Name.")
    for (i in 1..<depth) {
      sql.add("  ?" + i + "Level ?" + (i + 1) + "predicate ?" + (i + 1) + "Level.")
        .add("  ?" + (i + 1) + "predicate rdfs:label ?" + (i + 1) + "pName.")
        .add("  ?" + (i + 1) + "Level rdfs:label ?" + (i + 1) + "Name.")
    }
    sql.add("} WHERE { {")

    sql.add("  ?entity ?1predicate ?1Level.").add("  ?1predicate rdfs:label ?1pName.")
    if (!predicates.isNullOrEmpty()) {
      val inPredicates = StringBuilder()
      for ((i, pred) in predicates.withIndex()) {
        inPredicates.append(if (i > 0) "," else "").append("<").append(pred).append("> ")
      }
      if (excludePredicates) sql.add("   FILTER (?1predicate NOT IN ($inPredicates))")
      else sql.add("   FILTER (?1predicate IN ($inPredicates))")
      sql.add("}")
      if (!excludePredicates) {
        sql.add(
          """
          UNION {
          ?1predicate owl:inverseOf ?1revPredicate .
          ?1Level ?1revPredicate ?entity
          FILTER (?1predicate IN ($inPredicates))
          }
          
          """.trimIndent()
        )
      }
    } else sql.add("}")

    sql.add("  OPTIONAL {?1Level rdfs:label ?1Name.").add("    FILTER (!isBlank(?1Level))}")
    for (i in 1..<depth) {
      sql.add("  OPTIONAL {?${i}Level ?${i + 1}predicate ?${i + 1}Level.")
        .add("    FILTER (isBlank(?${i}Level))")
        .add("  OPTIONAL {?${i + 1}predicate rdfs:label ?${i + 1}pName}")
        .add("  OPTIONAL {?${i + 1}Level rdfs:label ?${i + 1}Name")
        .add("    FILTER (!isBlank(?${i + 1}Level))}")
    }
    sql.add(java.lang.String.join("", Collections.nCopies(depth, "}")))
    return sql
  }

  private fun processStatement(
    bundle: TTBundle,
    tripleMap: MutableMap<String, TTValue>,
    entityIri: String?,
    st: Statement
  ) {
    val entity = bundle.entity
    val s = st.subject
    val p = st.predicate
    val o = st.getObject()
    val subject = s.stringValue()
    val predicate = p.stringValue()
    val value = o.stringValue()
    val predNames = bundle.predicates
    val iriRef = tripleMap.getOrPut(predicate) { TTIriRef.iri(predicate) }.asIriRef()
    predNames[predicate] = iriRef.name ?: predicate
    val node: TTNode
    if (predicate == RDFS.LABEL.toString()) {
      if (s.isIRI) {
        if (subject == entityIri) {
          entity.setName(value)
        } else {
          tripleMap.getOrPut(subject) { TTIriRef.iri(subject) }.asIriRef().setName(value)
          predNames.computeIfPresent(subject) { _: String, _: String -> value }
        }
      } else {
        tripleMap.getOrPut(subject) { TTNode() }
          .asNode()
          .set(RDFS.LABEL.asIri(), TTLiteral.literal(value, (o as Literal).datatype.stringValue()))
      }
    } else {
      if (s.isIRI) {
        node = entity
      } else {
        node = tripleMap.getOrPut(subject) { TTNode() }.asNode()
      }
      if (o.isBNode) {
        tripleMap.putIfAbsent(value, TTNode())
        node.addObject(iriRef, tripleMap[value])
      } else if (o.isIRI) {
        tripleMap.putIfAbsent(value, TTIriRef.iri(value))
        node.addObject(iriRef, tripleMap[value])
      } else {
        node.set(
          iriRef,
          tripleMap.getOrPut(value) { TTLiteral.literal(value, (o as Literal).datatype.stringValue()) }.asLiteral()
        )
      }
    }
  }

  /**
   * Types (with names) of many entities in as few queries as possible. Entities with no type are absent from the map.
   * Replaces calling [.getEntityTypes] or a single-predicate bundle once per entity.
   *
   * @param iris the entities to look up
   * @return map of entity iri to its types
   */
  fun getTypesForEntities(iris: MutableCollection<String>?): MutableMap<String, TTArray> {
    val result: MutableMap<String, TTArray> = hashMapOf()
    if (iris.isNullOrEmpty()) return result

    val distinct = iris.stream().distinct().toList()
    var from = 0
    while (from < distinct.size) {
      val batch: MutableList<String> = distinct.subList(from, min(from + TYPE_LOOKUP_BATCH_SIZE, distinct.size))
      val sql: String = """
        SELECT ?s ?o ?oname
        WHERE {
          ${valueList("s", batch)}
          ?s rdf:type ?o .
          OPTIONAL { ?o rdfs:label ?oname }
        }
        
        """.trimIndent()

      IMDB.getConnection().use { conn ->
        val qry: TupleQuery = conn.prepareTupleSparql(sql)
        qry.evaluate().use { rs ->
          while (rs.hasNext()) {
            val bs = rs.next()
            val type = TTIriRef.iri(bs.getValue("o").stringValue())
            if (bs.hasBinding("oname")) type.setName(bs.getValue("oname").stringValue())
            val types = result.computeIfAbsent(bs.getValue("s").stringValue()) { TTArray() }
            if (!types.contains(type)) types.add(type)
          }
        }
      }
      from += TYPE_LOOKUP_BATCH_SIZE
    }
    return result
  }

  fun getEntityTypes(iri: String): TTArray {
    val result = TTArray()

    val sql = """
      SELECT ?o ?oname
      WHERE {
        ?s rdf:type ?o .
        ?o rdfs:label ?oname .
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("s", Values.iri(iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()

          result.add(
            TTIriRef().setIri(bs.getValue("o").stringValue()).setName(bs.getValue("oname").stringValue())

          )
        }
      }
    }
    return result
  }

  fun getEntityPredicates(iri: String, predicates: MutableSet<String>?): TTBundle {
    return getBundle(iri, predicates, false)
  }

  fun getConceptUsages(objectIri: String, rowNumber: Int?, pageSize: Int?): MutableList<TTIriRef> {
    val result: MutableList<TTIriRef> = mutableListOf()

    val sql = StringJoiner(System.lineSeparator()).add(
      """
      SELECT DISTINCT ?s ?name
      WHERE {
        { ?s ?p ?o . }
        UNION {
          ?s sh:property ?prop .
          ?prop sh:path ?propIri .
          FILTER(?propIri = ?o)
        }
        UNION {
        ?s im:uses ?o.
        }
        ?s rdfs:label ?name .
        ?s im:status ?status .
        FILTER (?p != rdfs:subclassOf && ?status != im:Inactive)
      }
      
      """.trimIndent()
    )

    if (rowNumber != null && pageSize != null) {
      sql.add("LIMIT $pageSize OFFSET $rowNumber")
    }

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql.toString())
      qry.setBinding("o", Values.iri(objectIri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          result.add(TTIriRef().setIri(bs.getValue("s").stringValue()).setName(bs.getValue("name").stringValue()))
        }
      }
    }
    return result
  }

  fun getConceptUsagesCount(objectIri: String): Int {
    val sql = """
      SELECT (COUNT(DISTINCT ?s) AS ?cnt)
      WHERE {
        { ?s ?p ?o . }
        UNION {
          ?s sh:property ?prop .
          ?prop sh:path ?propIri .
          FILTER(?propIri = ?o)
        }
        ?s im:status ?status .
        FILTER (?p != rdfs:subclassOf && ?status != im:Inactive && ?s != ?o)
      }
      
      """.trimIndent()


    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("o", Values.iri(objectIri))
      return qry.evaluate().use { rs ->
        rs.takeIf { it.hasNext() }?.next()?.getValue("cnt")?.let { (it as Literal).intValue() } ?: 0
      }
    }
  }

  fun getEntityReferenceNodes(
    stringIris: MutableSet<String>,
    schemeIris: MutableList<String>?,
    inactive: Boolean,
    parentContext: String?
  ): MutableList<EntityReferenceNode> {
    for (stringIri in stringIris) {
      Values.iri(stringIri)
    }
    val sql = StringJoiner(System.lineSeparator()).add(
      """
      SELECT ?s ?name ?typeIri ?typeName ?order ?contextOrder ?hasChildren ?hasGrandchildren  ?description ?status ?statusname ?scheme ?schemename
      WHERE {
        ?s rdfs:label ?name.
        ?s rdf:type ?typeIri.
        ?typeIri rdfs:label ?typeName.
        ${valueList("s", stringIris)}
        OPTIONAL { ?s rdfs:comment ?description . }
        OPTIONAL { ?s sh:order ?order . }
        OPTIONAL {
          ?s im:scheme ?scheme .
          ?scheme rdfs:label ?schemename
        }
        BIND(EXISTS{?child ($PARENT_PREDICATES) ?s} AS ?hasChildren)
        BIND(EXISTS{?grandChild ($PARENT_PREDICATES) ?child. ?child ($PARENT_PREDICATES) ?s} AS ?hasGrandchildren)
      
      """.trimIndent()
    )

    if (!inactive) {
      sql.add(
        """
        OPTIONAL { ?s im:status ?status .
        ?status rdfs:label ?statusname .
        FILTER (?status != im:Inactive) }
        
        """.trimIndent()
      )
    }
    if (parentContext != null) {
      sql.add(
        """
        OPTIONAL {?s im:contextOrder ?co .
        ?co im:context <$parentContext>.
        ?co sh:order ?contextOrder.}
        
        """.trimIndent()
      )
    }
    sql.add("}")


    val iriMap: MutableMap<String, EntityReferenceNode> = hashMapOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql.toString())
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val iri = bs.getValue("s").stringValue()
          val refNode = iriMap.getOrPut(iri) { EntityReferenceNode(iri).setType(TTArray()) }
          val types = refNode.type as TTArray
          types.add(
            TTIriRef.iri(bs.getValue("typeIri").stringValue())
              .setName(bs.getValue("typeName").stringValue())
          )
          if (bs.hasBinding("contextOrder")) refNode.setOrderNumber(((bs.getValue("contextOrder") as Literal).intValue()))
          else if (bs.hasBinding("order")) refNode.setOrderNumber((bs.getValue("order") as Literal).intValue())
          else refNode.setOrderNumber(Int.MAX_VALUE)
          refNode.setHasChildren((bs.getValue("hasChildren") as Literal).booleanValue())
            .setHasGrandChildren((bs.getValue("hasGrandchildren") as Literal).booleanValue())
            .setName(bs.getValue("name").stringValue())
          if (bs.hasBinding("description")) refNode.setDescription(bs.getValue("description").stringValue())
          if (bs.hasBinding("status")) {
            refNode.setStatus(
              TTIriRef(
                if (bs.getValue("status") == null) "" else bs.getValue("status").stringValue(),
                if (bs.getValue("statusname") == null) "" else bs.getValue("statusname").stringValue()
              )
            )
          }
          if (bs.hasBinding("schemename")) {
            refNode.setScheme(
              TTIriRef(
                if (bs.getValue("scheme") == null) "" else bs.getValue("scheme").stringValue(),
                if (bs.getValue("schemename") == null) "" else bs.getValue("schemename").stringValue()
              )
            )
          }
        }
      }
    }
    return iriMap.values.toMutableList()
  }

  fun getAsEntityReferenceNodes(iris: MutableList<String>): MutableList<EntityReferenceNode> {
    val result: MutableList<EntityReferenceNode> = ArrayList<EntityReferenceNode>()

    val orderMap: MutableMap<String, Int> = hashMapOf()
    for (i in iris.indices) {
      orderMap[iris[i]] = i
    }
    val sql = StringJoiner(System.lineSeparator()).add(
      """
      SELECT distinct ?entity ?name ?typeIri ?typeName ?hasChildren ?hasGrandchildren
      WHERE {
        ${valueList("entity", iris)}
        ?entity rdfs:label ?name
        OPTIONAL { ?entity rdf:type ?typeIri .
          OPTIONAL { ?typeIri rdfs:label ?typeName . }
        }
        BIND(EXISTS{?child ($PARENT_PREDICATES) ?entity} AS ?hasChildren)
        BIND(EXISTS{?grandChild ($PARENT_PREDICATES) ?child. ?child ($PARENT_PREDICATES) ?entity} AS ?hasGrandchildren)
      }
      
      """.trimIndent()
    )
    val entityMap: MutableMap<String, EntityReferenceNode> = hashMapOf()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql.toString())
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val iri = requireNotNull(bs.getValue("entity").stringValue())
          val refNode = entityMap.getOrPut(iri) {
            EntityReferenceNode(bs.getValue("entity").stringValue())
              .also {
                result.add(it)
                entityMap[iri] = it
                it.setOrderNumber(orderMap[iri])
              }
          }
          refNode.setName(bs.getValue("name").stringValue())
          if (bs.getValue("hasChildren") != null) {
            refNode.setHasChildren((bs.getValue("hasChildren") as Literal).booleanValue())
          }
          if (bs.getValue("hasGrandchildren") != null) {
            refNode.setHasGrandChildren((bs.getValue("hasGrandchildren") as Literal).booleanValue())
          }
          if (bs.getValue("typeIri") != null && bs.getValue("typeName") != null) refNode.addType(
            TTIriRef(
              bs.getValue("typeIri").stringValue(), bs.getValue("typeName").stringValue()
            )
          )
        }
      }
    }
    //Sorted by initial list order?
    //result = result.stream()
    // .sorted(Comparator.comparing(EntityReferenceNode::isHasChildren).reversed())
    // .collect(Collectors.toList());
    return result
  }


  fun getEntityReferenceNode(iri: String, schemeIris: MutableList<String>?, inactive: Boolean): EntityReferenceNode {
    val types = TTArray()
    val result = EntityReferenceNode(iri).setType(types)

    val sql = StringJoiner(System.lineSeparator()).add(
      """
      SELECT ?name ?typeIri ?typeName ?order ?hasChildren ?hasGrandchildren ?description ?status ?statusname ?scheme ?schemename
      WHERE {
        ${valueList("scheme", schemeIris)}
        Values ?s {<$iri>}
        ?s im:scheme ?scheme ;
           rdfs:label ?name .
        OPTIONAL { ?s rdfs:comment ?description . }
        OPTIONAL { ?s sh:order ?order . }
        OPTIONAL { ?s rdf:type ?typeIri .
          OPTIONAL { ?typeIri rdfs:label ?typeName . }
        }
        OPTIONAL {
          ?s im:scheme ?scheme .
          ?scheme rdfs:label ?schemename
        }
        BIND(EXISTS{?child ($PARENT_PREDICATES) ?s} AS ?hasChildren)
        BIND(EXISTS{?grandChild ($PARENT_PREDICATES) ?child. ?child ($PARENT_PREDICATES) ?s} AS ?hasGrandchildren)
      
      """.trimIndent()
    )

    if (!inactive) {
      sql.add(
        """
        OPTIONAL { ?s im:status ?status .
        ?status rdfs:label ?statusname .
        FILTER (?status != im:Inactive) }
        
        """.trimIndent()
      )
    }

    sql.add("}")

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql.toString())
      qry.setBinding("s", Values.iri(iri))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          var bs = rs.next()

          if (bs.hasBinding("order")) result.setOrderNumber((bs.getValue("order") as Literal).intValue())
          else result.setOrderNumber(Int.MAX_VALUE)

          result.setHasChildren((bs.getValue("hasChildren") as Literal).booleanValue())
            .setHasGrandChildren((bs.getValue("hasGrandchildren") as Literal).booleanValue())
            .setName(bs.getValue("name").stringValue())

          if (bs.getValue("typeIri") != null && bs.getValue("typeName") != null) types.add(
            TTIriRef(
              bs.getValue("typeIri").stringValue(), bs.getValue("typeName").stringValue()
            )
          )

          if (bs.getValue("description") != null) result.setDescription(bs.getValue("description").stringValue())

          if (bs.hasBinding("status")) {
            result.setStatus(
              TTIriRef(
                bs.getValue("status").stringValue() ?: "",
                bs.getValue("statusname").stringValue() ?: ""
              )
            )
          }

          if (bs.hasBinding("scheme") && bs.hasBinding("schemename")) {
            result.setScheme(
              TTIriRef(
                bs.getValue("scheme").stringValue() ?: "",
                bs.getValue("schemename").stringValue() ?: ""
              )
            )
          }

          while (rs.hasNext()) {
            bs = rs.next()
            types.add(TTIriRef(bs.getValue("typeIri").stringValue(), bs.getValue("typeName").stringValue()))
          }
        } else throw IllegalArgumentException("Iri does not exist: $iri")
      }
    }
    return result
  }


  fun findImmediateChildrenPagedByIriWithTotalCount(
    parentIri: String,
    schemeIris: MutableList<String>?,
    rowNumber: Int?,
    pageSize: Int?,
    inactive: Boolean,
    entityTypes: MutableList<String>?
  ): Pageable<TTIriRef> {
    val children: MutableList<TTIriRef> = mutableListOf()
    val result = Pageable<TTIriRef>()

    val sqlCount = StringJoiner(System.lineSeparator()).add("SELECT (COUNT(?c) as ?count)").add("WHERE {")
      .add("  ?c ($PARENT_PREDICATES) ?p .")
    if (!inactive) {
      sqlCount.add("  OPTIONAL { ?c im:status ?s}").add("  FILTER (?s != im:Inactive) .")
    }
    sqlCount.add("}")

    val sql = StringJoiner(System.lineSeparator()).add("SELECT ?c ?cname ").add("WHERE {")
      .add("  ?c ( $PARENT_PREDICATES) ?p .")
    if (!schemeIris.isNullOrEmpty()) {
      sql.add("?c im:scheme ?scheme . ?c rdfs:label ?cname .").add(valueList("scheme", schemeIris))
    } else {
      sql.add("?c rdfs:label ?cname .")
    }
    if (!inactive) {
      sql.add("  OPTIONAL { ?c im:status ?s}").add("  FILTER (?s != im:Inactive) .")
    }
    if (!entityTypes.isNullOrEmpty()) {
      entityTypes.add(IM.FOLDER.toString())
      sql.add("?c rdf:type ?typeIri .").add(valueList("typeIri", entityTypes))
    }
    sql.add("}")
    sql.add("ORDER BY ?cname")
    if (rowNumber != null && pageSize != null) {
      sql.add("LIMIT $pageSize").add("OFFSET $rowNumber")
    }

    IMDB.getConnection().use { conn ->
      val qryCount: TupleQuery = conn.prepareTupleSparql(sqlCount.toString())
      qryCount.setBinding("p", Values.iri(parentIri))
      qryCount.evaluate().use { rsCount ->
        val bsCount = rsCount.next()
        result.setTotalCount((bsCount.getValue("count") as Literal).intValue())
      }
      val qry: TupleQuery = conn.prepareTupleSparql(sql.toString())
      qry.setBinding("p", Values.iri(parentIri))
      qry.evaluate().use { rs ->
        result.setPageSize(pageSize)
        while (rs.hasNext()) {
          val bs = rs.next()
          children.add(TTIriRef(bs.getValue("c").stringValue(), bs.getValue("cname").stringValue()))
        }
        result.setResult(children)
      }
    }
    return result
  }


  fun findPartialWithTotalCount(
    parentIri: String,
    predicateIri: String,
    schemeIris: MutableList<String>?,
    rowNumber: Int?,
    pageSize: Int?,
    inactive: Boolean
  ): Pageable<TTIriRef> {
    val children: MutableList<TTIriRef> = mutableListOf()
    val result = Pageable<TTIriRef>()

    val sqlCount =
      StringJoiner(System.lineSeparator()).add("SELECT (COUNT(?p) as ?count)").add("WHERE {").add("  ?c ?pr ?p .")
    if (!inactive) {
      sqlCount.add("  OPTIONAL { ?p im:status ?s}").add("  FILTER (?s != im:Inactive) .")
    }
    sqlCount.add("}")

    val sql = StringJoiner(System.lineSeparator())
      .add("SELECT ?p ?pname ")
      .add("WHERE {").add("  ?c ?pr ?p .")
      .add("?p rdfs:label ?pname .")
    if (!schemeIris.isNullOrEmpty()) {
      sql.add(valueList("scheme", schemeIris)).add("  OPTIONAL { ?p im:scheme ?scheme . }")
    }
    if (!inactive) {
      sql.add("  OPTIONAL { ?p im:status ?s}").add("  FILTER (?s != im:Inactive) .")
    }
    sql.add("}")
    sql.add("ORDER BY ?pname")
    if (rowNumber != null && pageSize != null) {
      sql.add("LIMIT $pageSize").add("OFFSET $rowNumber")
    }

    IMDB.getConnection().use { conn ->
      val qryCount: TupleQuery = conn.prepareTupleSparql(sqlCount.toString())
      qryCount.setBinding("c", Values.iri(parentIri))
      qryCount.setBinding("pr", Values.iri(predicateIri))
      qryCount.evaluate().use { rsCount ->
        val bsCount = rsCount.next()
        result.setTotalCount((bsCount.getValue("count") as Literal).intValue())
      }
      val qry: TupleQuery = conn.prepareTupleSparql(sql.toString())
      qry.setBinding("c", Values.iri(parentIri))
      qry.setBinding("pr", Values.iri(predicateIri))
      qry.evaluate().use { rs ->
        result.setPageSize(pageSize)
        while (rs.hasNext()) {
          val bs = rs.next()
          children.add(TTIriRef(bs.getValue("p").stringValue(), bs.getValue("pname").stringValue()))
        }
        result.setResult(children)
      }
    }
    return result
  }

  fun findImmediateChildrenByIri(
    parentIri: String,
    schemeIris: MutableList<String>?,
    rowNumber: Int?,
    pageSize: Int?,
    inactive: Boolean
  ): MutableList<TTIriRef> {
    val result: MutableList<TTIriRef> = mutableListOf()

    val sql = StringJoiner(System.lineSeparator())
      .add("SELECT DISTINCT ?c ?cname")
      .add("WHERE { ?c ($PARENT_PREDICATES) ?p .")
      .add(valueList("schemes", schemeIris))
      .add("?c im:scheme ?schemes .")
      .add("?c rdfs:label ?cname .")

    if (!inactive) {
      sql.add("  OPTIONAL { ?c im:status ?s}").add("  FILTER (?s != im:Inactive) .")
    }

    sql.add("}")
    sql.add("ORDER BY ?cname")

    if (rowNumber != null && pageSize != null) {
      sql.add("LIMIT $pageSize").add("OFFSET $rowNumber")
    }

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql.toString())
      qry.setBinding("p", Values.iri(parentIri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          result.add(TTIriRef(bs.getValue("c").stringValue(), bs.getValue("cname").stringValue()))
        }
      }
    }
    return result
  }

  fun findImmediateParentsByIri(
    childIri: String,
    schemeIris: MutableList<String>?,
    rowNumber: Int?,
    pageSize: Int?,
    inactive: Boolean
  ): MutableList<TTIriRef> {
    val result: MutableList<TTIriRef> = mutableListOf()

    val sql = StringJoiner(System.lineSeparator())
      .add("SELECT DISTINCT ?p ?pname")
      .add("WHERE {")
      .add("  ?c ($PARENT_PREDICATES) ?p .")
      .add(valueList("schemes", schemeIris))
      .add("?c im:scheme ?schemes .")
      .add("?p rdfs:label ?pname .")

    if (!inactive) {
      sql.add("  OPTIONAL { ?p im:status  ?s}").add("  FILTER (?s != im:Inactive) .")
    }

    sql.add("}")

    if (rowNumber != null && pageSize != null) {
      sql.add("LIMIT $pageSize").add("OFFSET $rowNumber")
    }

    sql.add("ORDER BY ?pname")

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql.toString())
      qry.setBinding("c", Values.iri(childIri))

      log.debug("Executing 'findImmediateParentsByIri'...")
      qry.evaluate().use { rs ->
        log.debug("Retrieving 'findImmediateParentsByIri'...")
        while (rs.hasNext()) {
          val bs = rs.next()
          result.add(TTIriRef(bs.getValue("p").stringValue(), bs.getValue("pname").stringValue()))
        }
        log.debug("Finished ({} rows)", result.size)
      }
    }
    return result
  }

  private fun addTriples(triples: MutableList<Tpl>, subject: Resource?, parent: Int?) {
    val sql = StringJoiner(System.lineSeparator()).add(
      """
      SELECT ?sname ?p ?pname ?o ?oname
      WHERE {
        ?s ?p ?o ;
        OPTIONAL { ?s rdfs:label ?sname }
        OPTIONAL { ?p rdfs:label ?pname }
        OPTIONAL { ?o rdfs:label ?oname }
      
      """.trimIndent()
    )

    sql.add("}")
    setAndEvaluate(triples, subject, parent, sql)
  }

  private fun setAndEvaluate(triples: MutableList<Tpl>, subject: Resource?, parent: Int?, sql: StringJoiner) {
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql.toString())
      qry.setBinding("s", subject)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val tpl = Tpl().setDbid(row++).setParent(parent)
            .setPredicate(TTIriRef.iri(getString(bs, "p"), getString(bs, "pname")))

          triples.add(tpl)
          val `object` = bs.getValue("o")
          when {
            `object`.isIRI -> tpl.setObject(TTIriRef.iri(`object`.stringValue(), getString(bs, "oname")))
            `object`.isLiteral -> tpl.setLiteral(`object`.stringValue())
              .setObject(TTIriRef.iri((`object` as Literal).datatype.stringValue()))

            `object`.isBNode -> addTriples(triples, `object` as BNode, row - 1)
            else -> throw DALException("ARRAY!")
          }
        }
      }
    }
  }

  fun findParentFolderRef(iri: String): TTIriRef? {
    val sql: String = """
      SELECT ?p ?pname
      WHERE {
        ?c ($PARENT_PREDICATES) ?p .
        ?p rdfs:label ?pname .
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("c", Values.iri(iri))

      log.debug("Executing 'findParentFolderRef'...")
      qry.evaluate().use { rs ->
        log.debug("Retrieving 'findParentFolderRef'...")
        if (rs.hasNext()) {
          val bs = rs.next()
          return TTIriRef(bs.getValue("p").stringValue(), bs.getValue("pname").stringValue())
        }
      }
    }
    return null
  }

  fun hasPredicates(subjectIri: String, predicateIris: MutableSet<String>): Boolean {
    IMDB.getConnection().use { conn ->
      val sql: String = """
        ASK {
          ${valueList("predicates", predicateIris)}
          ?subject ?predicates ?value.
        }
        
        """.trimIndent()
      val sparql: BooleanQuery = conn.prepareBooleanSparql(sql)
      sparql.setBinding("subject", Values.iri(subjectIri))
      return sparql.evaluate()
    }
  }

  fun getEntities(iris: MutableSet<String>, predicates: MutableSet<String>?): MutableSet<TTEntity> {
    val result: MutableSet<TTEntity> = hashSetOf()
    val entityMap: MutableMap<String, TTEntity> = hashMapOf()
    if (iris.isEmpty()) return result
    val sql: String = """
      select ?entity ?entityLabel ?predicate ?object ?objectLabel ?subPredicate ?subObject ?subObjectLabel
      where {
        ${valueList("entity", iris)}
        ?entity rdfs:label ?entityLabel.
        optional {
          ${valueList("predicate", predicates)}
          ?entity ?predicate ?object.
          optional {
            ?object rdfs:label ?objectLabel.
          }
          optional {
          filter (isBlank(?object))
            ?object ?subPredicate ?subObject.
            optional {
              ?subObject rdfs:label ?subObjectLabel.
            }
          }
        }
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      val bnodeMap: MutableMap<String, TTNode> = hashMapOf()
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val iri = bs.getValue("entity").stringValue()
          var entity = entityMap[iri]
          if (entity == null) {
            entity = TTEntity()
              .setIri(iri)
              .setName(bs.getValue("entityLabel").stringValue())
            entityMap[iri] = entity
            result.add(entity)
          }
          if (bs.getValue("predicate") != null) {
            val predicate = bs.getValue("predicate").stringValue()
            val `object` = bs.getValue("object")
            if (`object`.isIRI) {
              val ttIriRef = TTIriRef.iri(`object`.stringValue())
              if (null != bs.getValue("objectLabel")) ttIriRef.setName(bs.getValue("objectLabel").stringValue())
              entity.addObject(TTIriRef.iri(predicate), ttIriRef)
            } else if (`object`.isBNode) {
              bnodeMap.putIfAbsent(`object`.stringValue(), TTNode())
              val blank: TTNode = bnodeMap.getOrPut(`object`.stringValue()) { TTNode() }
              if (bs.getValue("subPredicate") != null) {
                val subPredicate = bs.getValue("subPredicate").stringValue()
                val subObject = bs.getValue("subObject")
                if (subObject.isIRI) {
                  blank.addObject(
                    TTIriRef.iri(subPredicate),
                    TTIriRef.iri(subObject.stringValue()).setName(bs.getValue("subObjectLabel").stringValue())
                  )
                } else blank.addObject(TTIriRef.iri(subPredicate), TTLiteral.literal(subObject.stringValue()))
              }
            } else entity.addObject(TTIriRef.iri(predicate), TTLiteral.literal(`object`.stringValue()))
          }
        }
      }
    }
    return result
  }

  fun getEntitiesWithPredicates(
    iris: MutableSet<String>,
    predicates: MutableSet<String>?
  ): MutableMap<String, TTEntity> {
    val result: MutableMap<String, TTEntity> = hashMapOf()
    if (iris.isEmpty()) return result

    val sql: String = """
      select ?entity ?entityLabel ?predicate ?predicateLabel ?object ?objectLabel ?subPredicate ?subPredicateLabel ?subObject ?subObjectLabel
      where {
        ${valueList("entity", iris)}
        ?entity rdfs:label ?entityLabel.
        optional {
          ${valueList("predicate", predicates)}
          ?entity ?predicate ?object.
          ?predicate rdfs:label ?predicateLabel.
          optional {
            ?object rdfs:label ?objectLabel.
            ?object ?subPredicate ?subObject.
            ?subPredicate rdfs:label ?subPredicateLabel.
            optional {
              ?subObject rdfs:label ?subObjectLabel.
            }
          }
        }
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        val bnodeMap: MutableMap<String?, TTNode> = HashMap<String?, TTNode>()
        while (rs.hasNext()) {
          val bs = rs.next()
          val iri = bs.getValue("entity").stringValue()
          val entity = result.getOrPut(iri) { TTEntity() }.setIri(iri)
          entity.setName(bs.getValue("entityLabel").stringValue())
          if (bs.getValue("predicate") != null) {
            val predicate = bs.getValue("predicate").stringValue()
            result.getOrPut(predicate) { TTEntity() }.setName(bs.getValue("predicateLabel").stringValue())
            val `object` = bs.getValue("object")
            if (`object`.isIRI) {
              val ttIriRef = TTIriRef.iri(`object`.stringValue())
              if (null != bs.getValue("objectLabel")) ttIriRef.setName(bs.getValue("objectLabel").stringValue())
              entity.addObject(TTIriRef.iri(predicate), ttIriRef)
            } else if (`object`.isBNode) {
              val blank: TTNode = bnodeMap.getOrPut(`object`.stringValue()) { TTNode() }
              if (bs.getValue("subPredicate") != null) {
                val subPredicate = bs.getValue("subPredicate").stringValue()
                result.getOrPut(subPredicate) { TTEntity() }.setName(bs.getValue("subPredicateLabel").stringValue())
                val subObject = bs.getValue("subObject")
                if (subObject.isIRI) {
                  blank.addObject(
                    TTIriRef.iri(subPredicate),
                    TTIriRef.iri(subObject.stringValue()).setName(bs.getValue("subObjectLabel").stringValue())
                  )
                } else blank.addObject(TTIriRef.iri(subPredicate), TTLiteral.literal(subObject.stringValue()))
              }
            } else entity.addObject(TTIriRef.iri(predicate), TTLiteral.literal(`object`.stringValue()))
          }
        }
      }
    }
    return result
  }


  fun findEntitiesByType(typeIri: EntityType): MutableList<TTIriRef> {
    val sparqlString =
      """
        select *
          where {
              ?s rdf:type ?c .
              ?s rdfs:label ?name .
          }
        
        """.trimIndent()
    val iriRefs = mutableListOf<TTIriRef>()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sparqlString)
      qry.setBinding("c", typeIri.asDbIri())
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          iriRefs.add(TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()))
        }
      }
    }
    return iriRefs
  }

  fun findNamespaces(): MutableList<Namespace> {
    val result: MutableList<Namespace> = mutableListOf()

    val sql = """
       select *
       where {
         ?s im:isA ?namespace .
         ?s rdfs:label ?name .
       }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("namespace", IM.ROOT_NAMESPACE.asDbIri())
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val iri = bs.getValue("s").stringValue()
          val namespace = Namespace()
            .setIri(iri)
            .setName(bs.getValue("name").stringValue())
            .setPrefix(getPrefixFromIri(iri))
          result.add(namespace)
        }
      }
    }
    return result
  }

  private fun getPrefixFromIri(iri: String): String {
    val lastSlashIndex = iri.lastIndexOf('/')
    val hashIndex = iri.lastIndexOf('#')
    if (lastSlashIndex != -1 && hashIndex != -1 && hashIndex > lastSlashIndex) {
      return iri.substring(lastSlashIndex + 1, hashIndex)
    } else if (iri == NAMESPACE.FHIR.toString()) {
      return FHIR.PREFIX.toString()
    }
    return iri
  }

  fun getByNamespace(namespace: NAMESPACE): MutableSet<String> {
    val results: MutableSet<String> = hashSetOf()
    val sparql = """
      SELECT DISTINCT ?s
      WHERE {
        ?s im:scheme ?scheme
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sparql)
      qry.setBinding("scheme", namespace.asDbIri())
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          results.add(bs.getValue("s").stringValue())
        }
      }
    }
    return results
  }

  fun findInvertedIsas(iri: String): MutableList<TTIriRef> {
    val sparqlString =
      """
          select *
          where {
            ?s im:isA ?iri .
            ?s rdfs:label ?name .
          }
        
        """.trimIndent()
    val iriRefs = mutableListOf<TTIriRef>()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sparqlString)
      qry.setBinding("iri", Values.iri(iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          iriRefs.add(TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()))
        }
      }
    }
    return iriRefs
  }

  fun findOperatorOptions(iri: String): MutableList<String> {
    var options: MutableList<String> = mutableListOf()

    val sparqlString =
      """
          select *
          where {
            ?s sh:in ?o .
          }
        
        """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sparqlString)
      qry.setBinding("s", Values.iri(iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          if (null != bs.getValue("o")) {
            options = bs.getValue("o").stringValue().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toMutableList()
          }
        }
      }
    }
    return options
  }

  fun getFolderChildren(iri: String, vararg predicates: String?): MutableList<TTEntity> {
    val entities: MutableMap<String?, TTEntity> = HashMap<String?, TTEntity>()
    val predicateList =
      predicates.joinToString(" ") { p -> "<$p>" }
    val sql: String = """
      select ?entity ?predicate ?order ?object
      where {
        values ?predicate {$predicateList}
        ?entity im:isContainedIn ?containedIn.
        ?entity ?predicate ?object.
        optional {?entity sh:order ?order}
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("containedIn", Values.iri(iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val entity: TTEntity = entities.getOrPut(bs.getValue("entity").stringValue()) { TTEntity() }
          entity.setIri(bs.getValue("entity").stringValue())
          val `object` = bs.getValue("object")
          val ttValue =
            if (`object`.isIRI) TTIriRef.iri(`object`.stringValue()) else TTLiteral.literal(`object`.stringValue())
          entity.addObject(TTIriRef.iri(bs.getValue("predicate").stringValue()), ttValue)
        }
      }
    }
    return entities.values.sortedBy { entity -> entity.get(TTIriRef.iri(SHACL.ORDER)).asLiteral().intValue() }
      .toMutableList()
  }

  fun getAllowableChildTypes(iri: String?): MutableList<TTEntity> {
    val spq: String = """
      SELECT distinct  ?entity ?label ?path ?pathLabel
      WHERE {
           VALUES ?parent {<$iri>}
          {
           ?parent rdf:type ?entity.
           ?entity rdfs:label ?label.
           optional {
          ?entity sh:property ?property.
          ?property sh:path ?path.
           ?path rdfs:label ?pathLabel.
          ?property sh:node ?entity.}
      
          }
          union {
           ?parent im:contentType ?entity.
           ?entity rdfs:label ?label.
          }
          union {
          ?parent rdf:type im:Folder.
           filter not exists {?parent im:contentType ?anything}
            ?entity im:isContainedIn im:EntityTypes.
            ?entity rdfs:label ?label.
          }
      }
      
      """.trimIndent()
    val result: MutableList<TTEntity> = mutableListOf()
    val iriMap: MutableMap<String, TTEntity> = hashMapOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          var child = iriMap[bs.getValue("entity").stringValue()]
          if (child == null) {
            child = TTEntity()
              .setIri(bs.getValue("entity").stringValue())
              .setName(bs.getValue("label").stringValue())
            iriMap[bs.getValue("entity").stringValue()] = child
            result.add(child)
          }
          if (bs.getValue("path") != null) {
            child.set(
              TTIriRef.iri(SHACL.PATH), TTIriRef
                .iri(bs.getValue("path").stringValue())
                .setName(bs.getValue("pathLabel").stringValue())
            )
          } else {
            child.set(TTIriRef.iri(SHACL.PATH), TTIriRef.iri(IM.IS_CONTAINED_IN).setName("is contained in"))
          }
        }
      }
    }
    return result
  }

  fun getChildIris(iri: String): MutableList<String> {
    val spq: String = """
      select ?child ?name ?contextOrder ?order
      where {
       values ?iri {<$iri>}
        ?child im:isContainedIn|rdfs:subClassOf ?iri.
        ?child rdfs:label ?name.
        optional {?child sh:order ?order}
        optional {?child im:contextOrder ?co.
                  ?co im:context ?iri.
                  ?co sh:order ?contextOrder}
      
      }
      order by ?contextOrder ?order
      
      
      
      """.trimIndent()
    val result: MutableList<String> = mutableListOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          result.add(bs.getValue("child").stringValue())
        }
      }
    }
    return result
  }

  fun getIM1Ids(iris: MutableList<String>): MutableList<String> {
    val result: MutableList<String> = mutableListOf()
    val spq: String = """
      SELECT ?s ?im1id WHERE {
        VALUES ?s {${getIriLine(iris)}}
        ?s im:im1Id ?im1id .
      }
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          result.add(bs.getValue("im1id").stringValue())
        }
      }
    }
    return result
  }

  fun getDescendantIM1Ids(iris: List<String>, includeSelf: Boolean): MutableList<String> {
    val spq: String = """
      SELECT ?isA ?im1id WHERE {
        VALUES ?s {${getIriLine(iris)}}
        ?isA im:isA ?s .
        ?isA im:im1Id ?im1id .
      }
      """.trimIndent()
    val iriToIM1IdsMap: MutableMap<String, String> = hashMapOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          iriToIM1IdsMap[bs.getValue("isA").stringValue()] = bs.getValue("im1id").stringValue()
        }
      }
    }
    if (!includeSelf) {
      for (iri in iris) {
        iriToIM1IdsMap.remove(iri)
      }
    }
    return iriToIM1IdsMap.values.toMutableList()
  }


  fun getAncestorIM1Ids(iris: List<String>): MutableList<String> {
    val spq: String = """
      SELECT ?isA ?im1id WHERE {
        VALUES ?s {${getIriLine(iris)}}
        ?s im:isA ?isA .
        ?isA im:im1Id ?im1id .
      }
      """.trimIndent()
    val results: MutableList<String> = mutableListOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          results.add(bs.getValue("im1id").stringValue())
        }
      }
    }
    return results
  }

  fun getIsAs(iri: String): MutableSet<String> {
    val result: MutableSet<String> = mutableSetOf()
    val sql: String = """
      select ?entity
      where {
       ?entity im:isA <$iri> .
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          result.add(bs.getValue("entity").stringValue())
        }
      }
    }
    return result
  }

  fun getMemberOfIM1Ids(iris: List<String>): MutableList<String> {
    val spq: String = """
      SELECT ?im1id WHERE {
        VALUES ?s {${getIriLine(iris)}}
        ?s im:hasMember ?memberOf .
        ?memberOf im:im1Id ?im1id .
      }
      """.trimIndent()
    val results: MutableList<String> = mutableListOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          results.add(bs.getValue("im1id").stringValue())
        }
      }
    }
    return results
  }

  private fun getIriLine(stringIris: Collection<String>): String {
    return stringIris.joinToString(" ") { "<$it>" }
  }

  fun getQueryStringDefinition(iri: String): String? {
    val sql = """
      SELECT ?definition
      WHERE {
        ?s im:definition ?definition .
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("s", Values.iri(iri))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          return bs.getValue("definition").stringValue()
        }
      }
    }
    return null
  }

  fun getIriFromLegacy(scheme: String, legacyCode: String): String? {
    val sql: String = """
      select ?iri
      where {
      Values ?legacyCode {"$legacyCode"}
      Values ?scheme {<$scheme>}
        ?iri im:alternativeCode ?legacyCode.
        ?iri im:scheme ?scheme .
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          return bs.getValue("iri").stringValue()
        }
      }
    }
    return null
  }

  fun getEntityFromTerm(term: String, schemes: MutableSet<String>): MutableList<TTBundle> {
    val sql: String = """
      select ?concept ?label
      where {
        Values ?term {"$term"}
        ${valueList("scheme", schemes)}
        {
          ?concept rdfs:label ?term;
                   im:scheme ?scheme.
          filter(isIri(?concept))
        }
        union {
          ?tc rdfs:label ?term.
          ?concept im:hasTermCode ?tc;
                   im:scheme ?scheme.
        }
      }
      
      """.trimIndent()
    val result: MutableList<TTBundle> = mutableListOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          val bundle = TTBundle()
          result.add(bundle)
          val entity = TTEntity()
          bundle.setEntity(entity)
          entity.setName(term)
          entity.setIri(bs.getValue("concept").stringValue())
        }
      }
    }
    return result
  }

  fun getIndicatorSubqueries(iri: String): MutableList<SubQueryDependency> {
    val results: MutableMap<String, SubQueryDependency> = hashMapOf()
    val sql = """
          SELECT DISTINCT ?o ?label ?depth
          WHERE {
             ?s (im:denominator|im:numerator|im:dataset) ?o .
             ?o rdfs:label ?label .
             BIND(1 AS ?depth) .
          }
          ORDER BY DESC(?depth) ?label
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("s", Values.iri(iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val subqIri = bs.getValue("o").stringValue()
          if (bs.getValue("label") == null) throw RuntimeException("No label for subquery: $subqIri")
          val label = bs.getValue("label").stringValue()
          val depth = bs.getValue("depth").stringValue()
          if (!results.containsKey(subqIri)) results[subqIri] = SubQueryDependency(subqIri, label, depth.toInt())
        }
      }
    }
    val valueList: MutableList<SubQueryDependency> = results.values.toMutableList()
    valueList.sortWith(Comparator { a: SubQueryDependency, b: SubQueryDependency ->
      b.depth.compareTo(a.depth)
    })
    return valueList
  }


  fun getOrderedSubqueries(iri: String): MutableList<SubQueryDependency> {
    val results: MutableMap<String, SubQueryDependency> = hashMapOf()
    val sql = """
          SELECT DISTINCT ?o ?label ?depth
          WHERE {
             {
                 ?s im:dependentOn ?o .
                 BIND(1 AS ?depth)
             }
             UNION
             {
                 ?s im:dependentOn/im:dependentOn ?o .
                 BIND(2 AS ?depth)
             }
             UNION
             {
                 ?s im:dependentOn/im:dependentOn/im:dependentOn ?o .
                 BIND(3 AS ?depth)
             }
             UNION
             {
                 ?s im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn ?o .
                 BIND(4 AS ?depth)
             }
             UNION
             {
                 ?s im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn ?o .
                 BIND(5 AS ?depth)
             }
             UNION
             {
                 ?s im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn ?o .
                 BIND(6 AS ?depth)
             }
             UNION
             {
                 ?s im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn ?o .
                 BIND(7 AS ?depth)
             }
             UNION
             {
                 ?s im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn ?o .
                 BIND(8 AS ?depth)
             }
             UNION
             {
                 ?s im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn ?o .
                 BIND(9 AS ?depth)
             }
             UNION
             {
                 ?s im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn/im:dependentOn ?o .
                 BIND(10 AS ?depth)
             }
             OPTIONAL {
                 ?o rdfs:label ?label
             }
          }
          ORDER BY DESC(?depth) ?label
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.setBinding("s", Values.iri(iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val subqIri = bs.getValue("o").stringValue()
          if (bs.getValue("label") == null) throw RuntimeException("No label for subquery: $subqIri")
          val label = bs.getValue("label").stringValue()
          val depth = bs.getValue("depth").stringValue()
          if (!results.containsKey(subqIri)) results[subqIri] = SubQueryDependency(subqIri, label, depth.toInt())
        }
      }
    }
    val valueList: MutableList<SubQueryDependency> = results.values.toMutableList()
    valueList.sortByDescending { it.depth }
    return valueList
  }

  fun getIriDetails(iris: Set<String>): MutableMap<String, Entity> {
    val spq: String = """
      SELECT ?s ?name ?type WHERE {
        VALUES ?s {${getIriLine(iris)}}
        ?s rdf:type ?type .
        ?s rdfs:label ?name .
      }
      """.trimIndent()
    val results: MutableMap<String, Entity> = hashMapOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val iri = bs.getValue("s").stringValue()
          val name = bs.getValue("name").stringValue()
          val type = bs.getValue("type").stringValue()
          val typeRef = TTIriRef.iri(type)
          results.getOrPut(iri) { Entity().setIri(iri).setName(name).setName(name) }.type.add(typeRef)
        }
      }
    }
    return results
  }

  fun getTypeSchemeDefaults(): MutableMap<String, MutableList<TTIriRef>> {
    val spq = """
      select ?type ?scheme ?schemeLabel
      where {
      ?type im:defaultScheme ?scheme.
      ?scheme rdfs:label ?schemeLabel.}
      
      """.trimIndent()
    val results: MutableMap<String, MutableList<TTIriRef>> = hashMapOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val type = bs.getValue("type").stringValue()
          results.computeIfAbsent(type) { mutableListOf() }
            .add(
              TTIriRef.iri(bs.getValue("scheme").stringValue())
                .setName(bs.getValue("schemeLabel").stringValue())
            )
        }
      }
    }
    return results
  }

  fun getQueryEntitiesWithDefinition(): MutableList<TTEntity> {
    val spq = """
      PREFIX smh: <http://smartlifehealth.info/smh#>
      select ?s ?label ?definition
      where {
        ?s rdf:type im:Query .
        ?s rdfs:label ?label .
        ?s im:definition ?definition .
        ?s im:scheme smh: .
      }
      
      """.trimIndent()
    val results: MutableList<TTEntity> = mutableListOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val entity = TTEntity()
          entity.setIri(bs.getValue("s").stringValue())
          entity.setName(bs.getValue("label").stringValue())
          entity.set(IM.DEFINITION, TTLiteral.literal(bs.getValue("definition").stringValue()))
          results.add(entity)
        }
      }
    }
    return results
  }

  fun getRegisterQueryEntities(): MutableList<TTIriRef> {
    val spq = """
      PREFIX qof: <http://endhealth.info/qof#>
      SELECT ?s ?l
      WHERE {
          ?s rdf:type im:Query .
          ?s im:scheme qof: .
          ?s rdfs:label ?l .
          FILTER (CONTAINS(STR(?l), "_REG"))
      }
      
      """.trimIndent()
    val results: MutableList<TTIriRef> = mutableListOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          results.add(TTIriRef.iri(bs.getValue("s").stringValue()).setName(bs.getValue("l").stringValue()))
        }
      }
    }
    return results
  }

  fun getQOFQueryEntities(): MutableList<TTIriRef> {
    val spq = """
      select ?iri ?label where {
          ?iri im:isContainedIn+ <http://endhealth.info/qof#Q_QOFQueries> .
          ?iri rdf:type im:Query .
          ?iri rdfs:label ?label .
      }
      
      """.trimIndent()
    val results: MutableList<TTIriRef> = mutableListOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          results.add(TTIriRef.iri(bs.getValue("iri").stringValue()).setName(bs.getValue("label").stringValue()))
        }
      }
    }
    return results
  }

  fun getSMHQueryEntities(): MutableList<TTIriRef> {
    val spq = """
      select ?iri ?label where {
          ?iri im:isContainedIn+ <http://smartlifehealth.info/smh#Q_SmartLifeQueries> .
          ?iri rdf:type im:Query .
          ?iri rdfs:label ?label .
      }
      
      """.trimIndent()
    val results: MutableList<TTIriRef> = mutableListOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          results.add(TTIriRef.iri(bs.getValue("iri").stringValue()).setName(bs.getValue("label").stringValue()))
        }
      }
    }
    return results
  }

  fun getMatchedTo(iri: String, namespace: @MonotonicNonNull NAMESPACE): MutableSet<String> {
    val sql: String = """
      Select ?matched
      where {
      <$iri> im:matchedTo ?matched .
      ?matched im:scheme ?scheme.
      }
      
      """.trimIndent()
    val matches: MutableSet<String> = hashSetOf()
    IMDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          matches.add(bs.getValue("matched").stringValue())
        }
      }
    }
    return matches
  }

  private fun hydrateCorePropertiesSetEntityDocumentProperties(entityDocument: EntityDocument, qr: TupleQueryResult) {
    val rs = qr.next()
    entityDocument.setName(rs.getValue("name").stringValue())
    entityDocument.addTermCode(entityDocument.name, null, null, null)

    if (rs.hasBinding("preferredName")) entityDocument.setPreferredName(rs.getValue("preferredName").stringValue())

    if (rs.hasBinding("code")) entityDocument.setCode(rs.getValue("code").stringValue())

    if (rs.hasBinding("scheme")) entityDocument.setScheme(TTIriRef(rs.getValue("scheme").stringValue()))

    if (rs.hasBinding("status")) {
      val status = TTIriRef.iri(rs.getValue("status").stringValue())
      if (rs.hasBinding("statusName")) status.setName(rs.getValue("statusName").stringValue())
      entityDocument.setStatus(status)
    }

    val type = TTIriRef.iri(rs.getValue("type").stringValue())
    if (rs.hasBinding("typeName")) type.setName(rs.getValue("typeName").stringValue())
    entityDocument.addType(type)

    if (rs.hasBinding("extraType")) {
      val extraType = TTIriRef.iri(rs.getValue("extraType").stringValue(), rs.getValue("extraTypeName").stringValue())
      entityDocument.addType(extraType)
      if (extraType == TTIriRef.iri(NAMESPACE.IM.toString() + "DataModelEntity")) {
        val usageTotal = 2000000
        entityDocument.setUsageTotal(usageTotal)
      }
    }
    if (rs.hasBinding("usageTotal")) {
      entityDocument.setUsageTotal((rs.getValue("usageTotal") as Literal).intValue())
    }
  }

  fun getIriNames(conn: IMDB, iris: MutableSet<TTIriRef>) {
    if (iris.isEmpty()) return

    val toFetch: MutableSet<String> = hashSetOf()
    // iri -> the reference to fill in, so each result row is matched with a lookup instead of a scan of all iris
    val pending: MutableMap<String, TTIriRef> = hashMapOf()

    iris.forEach(Consumer { i: TTIriRef ->
      val name: String? = iriNameCache.getIfPresent(i.getIri())
      if (name != null) i.setName(name)
      else {
        toFetch.add("<${i.iri}>")
        pending.putIfAbsent(i.iri, i)
      }
    })

    if (toFetch.isEmpty()) {
      return
    }

    val sql: String = """
      SELECT ?iri ?label ?description
      WHERE {
        ?iri rdfs:label ?label.
        Optional { ?iri rdfs:comment ?description }
        filter (?iri in (${toFetch.joinToString(",")}))
      }
      
      """.trimIndent()

    val qry = conn.prepareTupleSparql(sql)
    try {
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val i = pending[bs.getValue("iri").stringValue()]
          if (i != null) {
            i.setName(bs.getValue("label").stringValue())
            iriNameCache.put(i.iri, i.name)
            if (bs.getValue("description") != null) i.setDescription(bs.getValue("description").stringValue())
          }
        }
      }
    } catch (e: Exception) {
      log.error(e.message, e)
    }
  }

  companion object {
    const val PARENT_PREDICATES: String =
      "rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf|im:isSubsetOf| im:isSubIndicatorOf"
    private const val TYPE_LOOKUP_BATCH_SIZE = 200
    private val iriNameCache: Cache<String, String> = Caffeine.newBuilder()
      .expireAfterAccess(Duration.ofSeconds(30))
      .maximumSize(100)
      .build()


  }
}
