package org.endeavourhealth.imapi.dataaccess

import org.eclipse.rdf4j.model.Literal
import org.eclipse.rdf4j.model.util.Values
import org.eclipse.rdf4j.query.BindingSet
import org.eclipse.rdf4j.query.TupleQuery
import org.endeavourhealth.imapi.dataaccess.databases.IMDB
import org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.valueList
import org.endeavourhealth.imapi.model.Pageable
import org.endeavourhealth.imapi.model.iml.Concept
import org.endeavourhealth.imapi.model.iml.Page
import org.endeavourhealth.imapi.model.imq.Node
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.imq.QueryException
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.model.tripletree.TTEntity
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.model.tripletree.TTNode
import org.endeavourhealth.imapi.vocabulary.*
import org.slf4j.LoggerFactory
import java.util.*

class SetRepository {
  private val log = LoggerFactory.getLogger(javaClass)

  @Throws(QueryException::class)
  fun getMembersFromDefinition(imQuery: Query): MutableSet<Concept> {
    val result: MutableSet<Concept> = mutableSetOf()
    val newRequest = QueryRequest().setQuery(imQuery)
    val sql = SparqlConverter(newRequest).getSelectSparql(false, false)
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          result.add(Concept().setIri(bs.getValue(ENTITY).stringValue()))
        }
      }
    }
    return result
  }

  @Throws(QueryException::class)
  fun getSetExpansionFromQuery(
    imQuery: Query, statusFilter: MutableSet<TTIriRef>, schemeFilter: MutableList<String>, page: Page?
  ): MutableSet<Concept> {
    setReturn(imQuery, false)
    val newRequest = QueryRequest().setQuery(imQuery)
    if (page?.pageNumber != null && null != page.pageSize) newRequest.setPage(page)
    val sql = SparqlConverter(newRequest).getSelectSparql(statusFilter, false, false)
    val entityVariable = imQuery.node ?: "entity"
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      return expand(qry, false, false, schemeFilter, entityVariable)
    }
  }


  private fun setReturn(imQuery: Query, includeLegacy: Boolean) {
    imQuery
      .path {
        it
          .setOptional(true)
          .setIri(IM.HAS_SCHEME.toString())
          .setTypeOf(IM.CONCEPT.toString())
          .setNode("scheme")
      }
      .path {
        it
          .setOptional(true)
          .setIri(IM.HAS_STATUS.toString())
          .setTypeOf(IM.CONCEPT.toString())
          .setNode("status")
      }
      .path {
        it
          .setOptional(true)
          .setIri(RDF.TYPE.toString())
          .setTypeOf(IM.CONCEPT.toString())
          .setNode(ENTITY_TYPE)
      }
      .return_ { it.setNodeRef("entity") }
      .return_ { it.setNodeRef(ENTITY_TYPE) }
      .return_ { it.setIri(RDFS.LABEL).`as`("term") }
      .return_ { it.setIri(IM.CODE).`as`("code") }
      .return_ { it.setNodeRef("scheme") }
      .return_ {
        it
          .setNodeRef("scheme")
          .setIri(RDFS.LABEL)
          .`as`("schemeName")
      }
      .return_ {
        it
          .setIri(IM.USAGE_TOTAL)
          .`as`("usage")
      }
      .return_ {
        it
          .setIri(IM.IM_1_ID)
          .`as`(IM_1_ID)
      }
      .return_ { it.setNodeRef("status") }
      .return_ {
        it
          .setNodeRef("status")
          .setIri(RDFS.LABEL)
          .`as`("statusName")
      }
      .return_ {
        it
          .setNodeRef(ENTITY_TYPE)
          .setIri(RDFS.LABEL)
          .`as`(TYPE_NAME)
      }
      .return_ {
        it
          .setIri(IM.CODE_ID)
          .`as`("codeId")
      }
      .return_ {
        it
          .setIri(IM.ALTERNATIVE_CODE)
          .`as`("alternativeCode")
      }

    if (includeLegacy) {
      imQuery
        .path {
          it
            .setOptional(true)
            .setIri(IM.MATCHED_TO.toString())
            .setNode("legacy")
            .setInverse(true)
            .setTypeOf(IM.CONCEPT.toString())
        }
        .path {
          it
            .setOptional(true)
            .setIri(IM.HAS_SCHEME.toString())
            .setTypeOf(IM.CONCEPT.toString())
            .setNode("legacyScheme")
        }
        .return_ { it.setNodeRef("legacy") }
        .return_ { it.setNodeRef("legacyScheme") }
        .return_ { it.setNodeRef("legacy").setIri(RDFS.LABEL).`as`("legacyTerm") }
        .return_ { it.setNodeRef("legacy").setIri(IM.CODE).`as`("legacyCode") }
        .return_ {
          it
            .setNodeRef("legacyScheme")
            .setIri(RDFS.LABEL)
            .`as`("legacySchemeName")
        }
        .return_ { it.setNodeRef("legacy").setIri(IM.USAGE_TOTAL).`as`("legacyUse") }
        .return_ { it.setNodeRef("legacy").setIri(IM.CODE_ID).`as`("legacyCodeId") }
        .return_ { it.setNodeRef("legacy").setIri(IM.IM_1_ID).`as`("legacyIm1Id") }
      imQuery
        .path {
          it
            .setOptional(true)
            .setIri(IM.LOCAL_SUBCLASS_OF.toString())
            .setNode("legacy")
            .setInverse(true)
            .setTypeOf(IM.CONCEPT.toString())
        }
        .path {
          it
            .setOptional(true)
            .setIri(IM.HAS_SCHEME.toString())
            .setTypeOf(IM.CONCEPT.toString())
            .setNode("legacyScheme")
        }
        .return_ { it.setNodeRef("legacy") }
        .return_ { it.setNodeRef("legacyScheme") }
        .return_ { it.setNodeRef("legacy").setIri(RDFS.LABEL).`as`("legacyTerm") }
        .return_ { it.setNodeRef("legacy").setIri(IM.CODE).`as`("legacyCode") }
        .return_ { it.setNodeRef("legacyScheme").setIri(RDFS.LABEL).`as`("legacySchemeName") }
        .return_ { it.setNodeRef("legacy").setIri(IM.USAGE_TOTAL).`as`("legacyUse") }
        .return_ { it.setNodeRef("legacy").setIri(IM.CODE_ID).`as`("legacyCodeId") }
        .return_ { it.setNodeRef("legacy").setIri(IM.IM_1_ID).`as`("legacyIm1Id") }
    }
  }


  @Throws(QueryException::class)
  fun getSetExpansionTotalCount(imQuery: Query, statusFilter: MutableSet<TTIriRef>): Int {
    //add scheme filter
    val newRequest = QueryRequest().setQuery(imQuery)
    val sql = SparqlConverter(newRequest).getCountSparql(statusFilter)
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      return getCountForSparql(qry)
    }
  }

  fun getSubsetIrisWithNames(iri: String): MutableSet<TTIriRef> {
    val result: MutableSet<TTIriRef> = mutableSetOf()

    val sql = """
      SELECT ?subset ?name
      WHERE {
        ?subset ?isSubset ?set .
        ?subset ?label ?name .
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.setBinding("set", Values.iri(iri))
      qry.setBinding("isSubset", IM.IS_SUBSET_OF.asDbIri())
      qry.setBinding("label", RDFS.LABEL.asDbIri())
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val subsetIri = bs.getValue("subset").stringValue()
          val subsetName = bs.getValue("name").stringValue()
          try {
            val subset = TTIriRef(subsetIri, subsetName)
            result.add(subset)
          } catch (ignored: IllegalArgumentException) {
            log.warn("Invalid subset iri [{}] for set [{}]", subsetIri, iri)
          }
        }
      }
    }
    return result
  }

  private fun expand(
    qry: TupleQuery,
    includeLegacy: Boolean,
    subsumedBy: Boolean,
    schemes: MutableList<String>,
    entityVariable: String
  ): MutableSet<Concept> {
    val result: MutableSet<Concept> = mutableSetOf()
    val coreSchemes: MutableSet<String> = VocabUtils.asHashSet(NAMESPACE.SNOMED, NAMESPACE.IM)
    val conceptMap: MutableMap<String, Concept> = mutableMapOf()
    qry.evaluate().use { rs ->
      while (rs.hasNext()) {
        val bs = rs.next()
        val concept = bs.getValue(entityVariable).stringValue()
        var cl = conceptMap[concept]
        val scheme = bs.getValue("scheme")
        if (cl == null) {
          cl = buildConcept(concept, conceptMap, result, bs)
          if (subsumedBy) cl.isSubsumed = bs.getValue("subsumed").stringValue() == "Y"
        } else {
          val type = bs.getValue(ENTITY_TYPE)
          val typeName = bs.getValue(TYPE_NAME)
          if (null != type) {
            cl.addType(TTIriRef.iri(type.stringValue(), typeName.stringValue()))
          }
        }
        val im1Id = bs.getValue(IM_1_ID)
        if (im1Id != null) cl.im1Id = im1Id.stringValue()
        if (includeLegacy) {
          val legacyScheme = if (bs.getValue(LEGACY_SCHEME) != null) bs.getValue(LEGACY_SCHEME).stringValue() else null
          if (legacyScheme == null) {
            if (scheme != null && !coreSchemes.contains(scheme.stringValue())) {
              bindLegacyFromCore(bs, cl)
            }
          } else if (schemes.isEmpty()) {
            bindResults(bs, cl)
          } else {
            if (schemes.stream().anyMatch { s: String? -> s == legacyScheme }) {
              bindResults(bs, cl)
            }
          }
        }
      }
    }
    return result.sortedBy { it.name }.toCollection(LinkedHashSet())
  }

  private fun buildConcept(
    concept: String,
    conceptMap: MutableMap<String, Concept>,
    result: MutableSet<Concept>,
    bs: BindingSet
  ): Concept {
    val cl = Concept()
    conceptMap[concept] = cl
    result.add(cl)
    val name = bs.getValue("term")
    val code = bs.getValue("code")
    val alternativeCode = bs.getValue("alternativeCode")
    val scheme = bs.getValue("scheme")
    val schemeName = bs.getValue("schemeName")
    val usage = bs.getValue("usage")
    val status = bs.getValue("status")
    val statusName = bs.getValue("statusName")
    val type = bs.getValue(ENTITY_TYPE)
    val typeName = bs.getValue(TYPE_NAME)
    val codeId = bs.getValue("codeId")
    cl.setIri(concept)
    if (name != null) cl.setName(name.stringValue())
    if (code != null) {
      cl.setCode(code.stringValue())
    }
    if (alternativeCode != null) {
      cl.setAlternativeCode(alternativeCode.stringValue())
    }
    if (null != scheme) {
      cl.setScheme(TTIriRef.iri(scheme.stringValue(), schemeName.stringValue()))
    }
    if (null != status) {
      cl.setStatus(TTIriRef.iri(status.stringValue(), statusName.stringValue()))
    }
    if (null != type) {
      cl.addType(TTIriRef.iri(type.stringValue(), typeName.stringValue()))
    }
    if (null != codeId) {
      cl.setCodeId(codeId.stringValue())
    }
    cl.setUsage(if (usage == null) 0 else (usage as Literal).intValue())
    return cl
  }

  private fun getCountForSparql(qry: TupleQuery): Int {
    qry.evaluate().use { rs ->
      if (rs.hasNext()) {
        val bs = rs.next()
        return (bs.getValue("count") as Literal).intValue()
      } else {
        return 0
      }
    }
  }


  private fun bindLegacyFromCore(bs: BindingSet, cl: Concept) {
    val legIri = cl.iri
    if (legIri != null) {
      var legacy = matchLegacy(cl, legIri)
      if (legacy == null) {
        legacy = Concept()
        cl.addMatchedFrom(legacy)
        legacy.setIri(legIri)
        legacy.code = cl.code
        legacy.name = cl.name
        legacy.scheme = cl.scheme
        legacy.codeId = cl.codeId
        legacy.alternativeCode = cl.alternativeCode
        legacy.usage = cl.usage
      }
      val lid = bs.getValue(IM_1_ID)
      if (lid != null) legacy.im1Id = lid.stringValue()
    }
  }


  fun bindConceptSetToDataModel(iri: String, dataModels: MutableSet<TTNode>) {
    val deleteBinding = """
      DELETE { ?concept im:binding ?datamodel}
      WHERE {
        ?concept im:binding ?datamodel
      }
      
      """.trimIndent()

    val newBinding = StringJoiner("\n").add("INSERT DATA {")
    var blankCount = 0
    for (dataModel in dataModels) {
      blankCount++
      val pathIri = dataModel.get(TTIriRef.iri(SHACL.PATH)).asIriRef().iri
      val nodeIri = dataModel.get(TTIriRef.iri(SHACL.NODE)).asIriRef().iri
      newBinding.add(
        """
        <${iri}> im:binding _:b${blankCount} .
        _:b${blankCount} sh:path <${pathIri}> .
        _:b${blankCount} sh:node <${nodeIri}> .
        
        """.trimIndent()
      )
    }
    newBinding.add("}")

    IMDB.getConnection().use { conn ->
      conn.begin()
      var upd = conn.prepareDeleteSparql(deleteBinding)
      upd.setBinding(CONCEPT, Values.iri(iri))
      upd.execute()
      upd = conn.prepareInsertSparql(newBinding.toString(), GRAPH.IM)
      upd.execute()
      conn.commit()
    }
  }

  fun getSets(): MutableSet<String> {
    val setIris: MutableSet<String> = mutableSetOf()
    IMDB.getConnection().use { conn ->
      val spq = """
        SELECT distinct ?iri
        WHERE {
          ?iri rdf:type ?type.
          FILTER (?type in (im:ValueSet, im:ConceptSet))
        }
        
        """.trimIndent()
      val qry = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          setIris.add(rs.next().getValue("iri").stringValue())
        }
      }
    }
    return setIris
  }

  fun updateMembers(iri: String, members: MutableSet<Concept>, graph: GRAPH) {
    IMDB.getConnection().use { conn ->
      val spq = """
        DELETE { ?concept im:hasMember ?x.}
        WHERE {
          ?concept im:hasMember ?x.
        }
        
        """.trimIndent()
      val upd = conn.prepareDeleteSparql(spq)
      upd.setBinding(CONCEPT, Values.iri(iri))
      upd.execute()
      var sj = StringJoiner("\n")
      sj.add("INSERT DATA {")
      var batch = 0
      for (member in members) {
        batch++
        if (batch == 1000) {
          sj.add("}")
          sendUp(sj, conn, graph)
          sj = StringJoiner("\n")
          sj.add("INSERT DATA {")
          batch = 0
        }
        sj.add("<" + iri + "> im:hasMember <" + member.iri + ">.")
      }
      sj.add("}")
      sendUp(sj, conn, graph)
    }
  }

  fun updateMemberCount(iri: String) {
    var count = 0
    IMDB.getConnection().use { conn ->
      var sql: String = """
        Select (count(?member) as ?count)
        Where {<${iri}> im:hasMember ?member.}
        
        """.trimIndent()
      val qry = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          count = (rs.next().getValue("count") as Literal).intValue()
        }
      }
      sql = """
        INSERT DATA {<${iri}>  im:memberCount ${count}}
        
        """.trimIndent()
      val upd = conn.prepareInsertSparql(sql, GRAPH.IM)
      conn.begin()
      upd.execute()
      conn.commit()
    }
  }


  private fun sendUp(sj: StringJoiner, conn: IMDB, graph: GRAPH) {
    val upd = conn.prepareInsertSparql(sj.toString(), GRAPH.IM)
    upd.setBinding("g", graph.asDbIri())
    conn.begin()
    upd.execute()
    conn.commit()
  }


  private fun bindResults(bs: BindingSet, cl: Concept) {
    val legIri = bs.getValue("legacy")
    if (legIri != null) {
      var legacy = matchLegacy(cl, legIri.stringValue())
      if (legacy == null) {
        legacy = Concept()
        cl.addMatchedFrom(legacy)
        legacy.setIri(legIri.stringValue())
        val lc = bs.getValue("legacyCode")
        val lt = bs.getValue("legacyTerm")
        val ls = bs.getValue(LEGACY_SCHEME)
        val lsn = bs.getValue("legacySchemeName")
        val luse = bs.getValue("legacyUse")
        val codeId = bs.getValue("legacyCodeId")
        val legacyAlternativeCode = bs.getValue("legacyAlternativeCode")
        val legacyStatus = if (bs.getValue(LEGACY_STATUS) != null) bs.getValue(LEGACY_STATUS).stringValue() else null
        val legacyStatusName =
          if (bs.getValue(LEGACY_STATUS_NAME) != null) bs.getValue(LEGACY_STATUS_NAME).stringValue() else null
        if (null != legacyStatus && null != legacyStatusName) legacy.setStatus(TTIriRef(legacyStatus, legacyStatusName))
        if (lc != null) legacy.setCode(lc.stringValue())
        if (lt != null) legacy.setName(lt.stringValue())
        if (ls != null) {
          if (lsn == null) legacy.setScheme(TTIriRef.iri(ls.stringValue()))
          else legacy.setScheme(TTIriRef.iri(ls.stringValue(), lsn.stringValue()))
        }
        if (codeId != null) {
          legacy.setCodeId(codeId.stringValue())
        }
        if (legacyAlternativeCode != null) {
          legacy.setAlternativeCode(legacyAlternativeCode.stringValue())
        }
        legacy.setUsage(if (luse == null) 0 else (luse as Literal).intValue())
      }
      val lid = bs.getValue("legacyIm1Id")
      if (lid != null) legacy.im1Id = lid.stringValue()
    }
  }


  private fun matchLegacy(cl: Concept, iri: String?): Concept? {
    if (cl.matchedFrom != null) for (legacy in cl.matchedFrom) if (legacy.iri == iri) return legacy
    return null
  }

  fun getSomeMembers(setIri: String, limit: Int): MutableSet<Concept> {
    val sparql: String = """
      SELECT *
      WHERE {
        ?setIri im:hasMember ?entity .
      }
      LIMIT $limit
      
      """.trimIndent()
    val result: MutableSet<Concept> = mutableSetOf()

    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      qry.setBinding("setIri", Values.iri(setIri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val concept = Concept()
          concept.setIri(bs.getValue(ENTITY).stringValue())
          result.add(concept)
        }
      }
    }
    return result
  }

  fun getBindingsForConcept(members: MutableSet<String>): MutableSet<TTNode> {
    val result: MutableSet<TTNode> = mutableSetOf()
    val sparqlIris = members.map { "<${it}>" }
    val iriList = sparqlIris.joinToString(",") { it }
    val spql: String = """
      SELECT distinct ?dataModel ?path
      WHERE {
        ?memberIri ^im:hasMember ?valueSet.
        filter (?memberIri in(${iriList}))
        {
          ?valueSet ^sh:class ?property.
          ?property sh:path ?path.
          ?property ^sh:property ?dataModel.
        }
        UNION {
          ?valueSet ^im:concept ?dataModel.
        }
      }
      GROUP BY ?dataModel ?path
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(spql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val dataModel = TTNode()
          dataModel.set(TTIriRef.iri(SHACL.NODE), TTIriRef.iri(bs.getValue("dataModel").stringValue()))
          if (bs.getValue("path") != null) dataModel.set(
            TTIriRef.iri(SHACL.PATH),
            TTIriRef.iri(bs.getValue("path").stringValue())
          )
          else dataModel.set(TTIriRef.iri(SHACL.PATH), TTIriRef.iri(IM.CONCEPT_PROPERTY))
          result.add(dataModel)
        }
      }
    }
    return result
  }


  fun getExpansionFromIri(
    setIri: String, includeLegacy: Boolean, schemes: MutableList<String>,
    subsumptionPredicates: MutableList<String>
  ): MutableSet<Concept> {
    val select = StringBuilder().append("Select distinct ?entity ?subsumed ")
    if (subsumptionPredicates.isNotEmpty()) {
      select.append("?subsumed ")
    }
    select.append("?term ?code ?scheme ?schemeName ?status ?statusName ?im1Id ?use ?codeId ?alternativeCode ")
    if (includeLegacy) {
      select.append("?legacy ?legacyTerm ?legacyCode ?legacyScheme ?legacySchemeName ?legacyIm1Id ?legacyUse ?legacyCodeId ?legacyAlternativeCode")
    }
    select.append("\n")
      .append(
        """
         WHERE {
          Values ?setIri{<${setIri}>}
        
        """.trimIndent()
      )
      .append("\n")
    select.append(addOr(true, includeLegacy, schemes, null))
    select.append(addOr(false, includeLegacy, schemes, subsumptionPredicates))
    select.append("}  ")
    val subsumedBy = subsumptionPredicates.isNotEmpty()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(select.toString())
      return expand(qry, includeLegacy, subsumedBy, mutableListOf(), "entity")
    }
  }

  private fun addOr(
    first: Boolean,
    includeLegacy: Boolean,
    schemes: MutableList<String>,
    subsumptionPredicates: MutableList<String>? = mutableListOf()
  ): String {
    val spql = StringJoiner("\n")
    if (first) {
      spql.add("{")
      spql.add("BIND(\"N\" AS ?subsumed)")
      spql.add(" ?setIri im:hasMember ?entity.")
    } else {
      spql.add(
        """
          UNION {
            BIND("Y" AS ?subsumed)
            ?setIri im:hasMember ?member.
            Values ?subsumedBy{${subsumptionPredicates!!.joinToString(" ") { "<$it>" }}}
           ?entity ?subsumedBy ?member.
        
        """.trimIndent()
      )
    }
    spql.add(
      """
      ?entity rdfs:label ?term;
              im:code ?code;
              im:scheme ?scheme.
      ?scheme rdfs:label ?schemeName .
      OPTIONAL { ?entity im:status ?status . ?status rdfs:label ?statusName . }
      OPTIONAL { ?entity im:im1Id ?im1Id . }
      OPTIONAL { ?entity im:usageTotal ?use . }
      OPTIONAL { ?entity im:codeId ?codeId . }
      OPTIONAL { ?entity im:alternativeCode ?alternativeCode.} 
      
      """.trimIndent()
    )
    if (includeLegacy) {
      spql.add(
        """
        OPTIONAL {
          ?legacy im:matchedTo ?entity.
          ?legacy rdfs:label ?legacyTerm.
          ?legacy im:code ?legacyCode.
          ?legacy im:scheme ?legacyScheme.
          ?legacyScheme rdfs:label ?legacySchemeName .
          OPTIONAL { ?legacy im:im1Id ?legacyIm1Id }
          OPTIONAL { ?legacy im:status ?legacyStatus . ?legacyStatus rdfs:label ?legacyStatusName . }
          OPTIONAL { ?legacy im:usageTotal ?legacyUse }
          OPTIONAL { ?legacy im:codeId ?codeId}
          OPTIONAL { ?legacy im:alternativeCode ?legacyAlternativeCode.}
          OPTIONAL { ?legacy im:codeId ?legacyCodeId }
        
        """.trimIndent()
      )
      if (schemes.isNotEmpty()) {
        val schemeIris = java.lang.String.join(",", getIris(schemes))
        spql.add(" FILTER (?legacyScheme IN (" + schemeIris + "))")
      }
      spql.add("}\n")
    }
    spql.add("}")
    return spql.toString()
  }


  private fun getIris(schemes: MutableList<String>): MutableList<String> {
    return schemes.map { "<$it>" }.toMutableList()
  }


  fun getDistillation(iris: String): MutableSet<String> {
    val isas: MutableSet<String> = mutableSetOf()

    IMDB.getConnection().use { conn ->
      val sql: String = """
        SELECT ?child
        WHERE {
        VALUES ?child { $iris }
        VALUES ?parent { $iris }
        ?child ?isA ?parent .
        FILTER (?child != ?parent)}
        
        """.trimIndent()
      val qry = conn.prepareTupleSparql(sql)
      qry.setBinding("isA", IM.IS_A.asDbIri())
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          isas.add(bs.getValue("child").stringValue())
        }
      }
      return isas
    }
  }


  fun getMembers(iri: String, entailed: Boolean, pageNumber: Int, pageSize: Int): Pageable<Node> {
    if (entailed) {
      val result = getMemberWithPredicate(iri, IM.ENTAILED_MEMBER.toString(), pageNumber, pageSize)
      if (result.totalCount!! > 0) return result
    }
    return getMemberWithPredicate(iri, IM.HAS_MEMBER.toString(), pageNumber, pageSize)
  }

  private fun getMemberWithPredicate(iri: String, predicate: String, pageNumber: Int, pageSize: Int): Pageable<Node> {
    val result = Pageable<Node>()
    result.setTotalCount(0)
    var sql = """
      Select (count(distinct ?instance) as ?count)
      where {
      ?s ?p ?instance
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.setBinding("s", Values.iri(iri))
      qry.setBinding("p", Values.iri(predicate))
      qry.evaluate().use { rsCount ->
        val bsCount = rsCount.next()
        result.setTotalCount((bsCount.getValue("count") as Literal).intValue())
      }
    }
    val offset = ((pageNumber - 1) * pageSize).toString()
    if (result.totalCount == 0) return result
    if (predicate == IM.ENTAILED_MEMBER.toString()) {
      sql = """
        Select ?member ?entailment ?name  ?exclude
        where {
        ?s im:entailedMember ?instance.
        ?instance im:is ?member.
        ?member rdfs:label ?name.
        optional {?instance im:exclude ?exclude.}
        optional {?instance im:entailment ?entailment}
        }
        order by ?exclude
        limit $pageSize
        offset $offset
        
        """.trimIndent()
    } else {
      sql = """
        Select ?member ?name
        where {
        ?s im:hasMember ?member.
        ?member rdfs:label ?name.
        }
        limit $pageSize
        offset $offset
        
        """.trimIndent()
    }
    val resultSet: MutableList<Node> = mutableListOf()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.setBinding("s", Values.iri(iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val node = Node()
          resultSet.add(node)
          node.setIri(bs.getValue("member").stringValue()).setName(bs.getValue("name").stringValue())
          if (bs.getValue("entailment") != null) {
            val entailment = bs.getValue("entailment").stringValue()
            when (IM.from(entailment)) {
              IM.DESCENDANTS_OR_SELF_OF -> node.setDescendantsOrSelfOf(true)
              IM.DESCENDANTS_OF -> node.setDescendantsOf(true)
              IM.ANCESTORS_OF -> node.setAncestorsOf(true)
              else -> {}
            }
            if (bs.getValue("exclude") != null) {
              node.setExclude(true)
            }
          }
        }
        result.setResult(resultSet)
      }
    }
    return result
  }

  fun getExpansionFromEntailedMembers(setIri: String): MutableSet<Concept> {
    val sql: String = """
      select distinct ?member
      where {
        Values ?set { <$setIri> }
        ?set im:entailedMember ?entailed.
        {
          ?entailed im:is ?member.
          filter not exists {?entailed im:entailment ?entailment}
        }
        union {
          ?entailed im:is ?parent.
          ?entailed im:entailment im:DescendantsOrSelfOf.
          ?member im:isA ?parent.
        }
        union {
          ?entailed im:is ?parent.
          ?entailed im:entailment im:DescendantsOf.
          ?member im:isA ?parent.
          filter (?member!=?parent)
        }
        union {
          ?entailed im:is ?child.
          ?entailed im:entailment im:AncestorsOf.
          ?child im:isA ?member.
        }
        filter not exists {
          ?member im:isA ?parent2.
          ?parent2 ^im:is ?entailment2.
          ?entailment2 im:entailment im:DescendantsOrSelfOf.
          ?entailment2 im:exclude true.
        }
        filter not exists {
          ?member im:isA ?parent2.
          filter (?member!=?parent2)
          ?parent2 ^im:is ?entailment2.
          ?entailment2 im:entailment im:DescendantsOf.
          ?entailment2 im:exclude true.
        }
        filter not exists {
          ?parent2 im:isA ?member.
          ?parent2 ^im:is ?entailment2.
          ?entailment2 im:entailment im:AncestorsOf.
          ?entailment2 im:exclude true.
        }
      }
      
      """.trimIndent()
    val expansion: MutableSet<Concept> = mutableSetOf()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          expansion.add(Concept().setIri(bs.getValue("member").stringValue()))
        }
      }
    }
    return expansion
  }


  fun isValidPropertyForDomains(propertyIri: String, entityIris: MutableSet<String>): Boolean {
    val spq: String = """
      ASK
      WHERE {?property im:isA ?superProperty.
      ${valueList("property", mutableListOf(propertyIri))}
      ${valueList("concept", entityIris)}
      ?superProperty rdfs:domain ?domain.
      ?concept im:isA ?domain.
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      return conn.prepareBooleanSparql(spq).evaluate()
    }
  }

  fun isValidRangeForProperty(propertyIri: String, valueIri: String): Boolean {
    val values = mutableListOf(propertyIri, valueIri)
    val spq: String = """
      ASK
      WHERE {?property im:isA ?superProperty.
      ${valueList("property", values)}
      ?superProperty rdfs:range ?range.
      ${valueList("concept", values)}
      ?concept im:isA ?range.
      }
      
      
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      return conn.prepareBooleanSparql(spq).evaluate()
    }
  }


  fun getValidConcepts(conceptIris: MutableSet<String>): MutableMap<String, Boolean> {
    val result: MutableMap<String, Boolean> = mutableMapOf()
    val spq: String = """
      SELECT ?concept ?type
      WHERE {
        ${valueList("concept", conceptIris)}
        optional {?concept rdf:type ?type.}
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val conceptIri = bs.getValue("concept").stringValue()
          result[conceptIri] = bs.getValue("type") != null
        }
      }
    }
    return result
  }

  fun getMemberCount(iri: String): Int {
    val spq: String = """
      SELECT ?memberCount
      WHERE {
        <$iri> in:memberCount ?memberCount.
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(spq)
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          return (bs.getValue("memberCount") as Literal).intValue()
        }
      }
    }
    return 0
  }

  fun deleteSemanticMaps() {
    IMDB.getConnection().use { conn ->
      val spq = """
        DELETE { ?concept im:semanticMap ?map.}
        WHERE {
          ?concept im:semanticMap ?map.
        }
        
        """.trimIndent()
      val upd = conn.prepareDeleteSparql(spq)
      upd.execute()
    }
  }

  fun updateSemanticMaps(mappedConcepts: MutableSet<TTEntity>, graph: GRAPH) {
    IMDB.getConnection().use { conn ->
      var sj = StringJoiner("\n")
      sj.add("INSERT DATA {")
      var batch = 0

      for (concept in mappedConcepts) {
        ++batch
        if (batch == 1000) {
          sj.add("}")
          this.sendUp(sj, conn, graph)
          sj = StringJoiner("\n")
          sj.add("INSERT DATA {")
          batch = 0
        }
        for (mapEntry in concept.get(IM.HAS_MAP_ENTRY).elements) {
          sj.add("<" + concept.iri + "> <" + IM.HAS_MAP_ENTRY.toString() + "> <" + mapEntry.asIriRef().iri + ">.")
        }
      }

      sj.add("}")
      this.sendUp(sj, conn, graph)
    }
  }

  companion object {
    const val EXPANDED_ENTITY: String = "expandedEntity"
    const val ENTITY: String = "entity"
    const val IM_1_ID: String = "im1Id"
    const val ENTITY_TYPE: String = "entityType"
    const val TYPE_NAME: String = "typeName"
    const val LEGACY_SCHEME: String = "legacyScheme"
    const val LEGACY_STATUS: String = "legacyStatus"
    const val LEGACY_STATUS_NAME: String = "legacyStatusName"

    const val CONCEPT: String = "concept"
  }
}

