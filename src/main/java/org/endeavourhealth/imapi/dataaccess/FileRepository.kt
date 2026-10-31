package org.endeavourhealth.imapi.dataaccess

import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.NAMESPACE
import org.endeavourhealth.imapi.vocabulary.RDFS
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class FileRepository(val dataPath: String) {
  private val log = LoggerFactory.getLogger(javaClass)
  private val codeCoreMap: MutableMap<NAMESPACE, MutableMap<String, MutableSet<String>>> =
    EnumMap(NAMESPACE::class.java)
  private val termCoreMap: MutableMap<NAMESPACE, MutableMap<String, MutableSet<String>>> =
    EnumMap(NAMESPACE::class.java)
  private val codes: MutableMap<NAMESPACE, MutableMap<String, MutableSet<String>>> =
    EnumMap(NAMESPACE::class.java)
  private val coreTerms: MutableMap<String, String> = hashMapOf()
  private val termCodes: MutableMap<NAMESPACE, MutableMap<String, String>> =
    EnumMap(NAMESPACE::class.java)
  private val codeIds: MutableMap<NAMESPACE, MutableMap<String, MutableSet<String>>> =
    EnumMap(NAMESPACE::class.java)
  private val coreIris: MutableMap<String, String> = hashMapOf()

  @Throws(IOException::class)
  fun getCoreName(iri: String): String? {
    if (coreIris.isEmpty()) {
      fetchCoreIris()
    }
    return coreIris[iri]
  }

  @Throws(IOException::class)
  private fun fetchCoreIris() {
    val fileName = getFile("CoreIris")
    readFileToStringMap(fileName, coreIris)
  }

  @Throws(IOException::class)
  fun getCodeToIri(): MutableMap<String, String> {
    val codeToIri: MutableMap<String, String> = hashMapOf()
    val fileName = getFile("CodeMap")
    readFileToStringMap(fileName, codeToIri)
    return codeToIri
  }

  @Throws(IOException::class)
  fun fetchRelationships(
    relationshipMap: MutableMap<String, MutableMap<String, MutableSet<String>>>,
    blockingIris: Set<String>
  ) {
    val fileName = getFile("SubTypes")
    val count = AtomicInteger()
    Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1).use { lines ->
      lines.forEach { line: String ->
        count.getAndIncrement()
        val fields = line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val child: String = fields[0]
        var relationship = fields[1]
        val parent: String = fields[2]
        if (relationship == RDFS.SUB_PROPERTY_OF.toString()) relationship = RDFS.SUBCLASS_OF.toString()
        if (relationship == IM.LOCAL_SUBCLASS_OF.toString()) relationship = RDFS.SUBCLASS_OF.toString()
        if (relationship == IM.PREVIOUS_ENTITY_OF.toString()) relationship = RDFS.SUBCLASS_OF.toString()
        if (!blockingIris.contains(parent)) {
          val parentMap = relationshipMap.getOrPut(relationship) { hashMapOf() }
          val parents = parentMap.getOrPut(child) { hashSetOf() }
          parents.add(parent)
        }
        if (count.get() % 1000000 == 0) log.info("{} relationships collected", count)
      }
    }
  }

  @Throws(IOException::class)
  fun getCoreFromCodeId(codeId: String, schemes: List<NAMESPACE>): MutableSet<TTIriRef> {
    for (scheme in schemes) {
      if (codeIds[scheme] == null) {
        fetchCodeIds(scheme)
      }
      codeIds[scheme]?.get(codeId)?.let { ids ->
        return ids.mapTo(hashSetOf()) { TTIriRef.iri(it) }
      }
    }
    return mutableSetOf()
  }


  /**
   * Returns A core entity iri and name from a core term
   *
   * @param term the code or description id or term code
   * @return iri and name of entity
   */
  @Throws(IOException::class)
  fun getReferenceFromCoreTerm(term: String): TTIriRef? {
    if (coreTerms.isEmpty()) {
      fetchCoreTerms()
    }
    return if (coreTerms[term] != null) TTIriRef.iri(coreTerms[term]) else null
  }


  @Throws(IOException::class)
  fun getAllMatchedLegacy(): MutableMap<String, MutableSet<String>> {
    val legacyMap = hashMapOf<String, MutableSet<String>>()
    val fileName = getFile("LegacyCore")
    BufferedReader(FileReader(fileName)).use { reader ->
      var line = reader.readLine()
      while (!line.isNullOrEmpty()) {
        var fields = line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val legacy: String = fields[0]
        if (fields.size < 2) {
          log.info("invalid line $line")
          val x = reader.readLine()
          log.info(x)
          line = java.lang.String.join("", line, x)
          fields = line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }
        val core: String = fields[1]
        legacyMap.getOrPut(legacy) { hashSetOf() }.add(core)
        line = reader.readLine()
      }
    }
    return legacyMap
  }

  @Throws(IOException::class)
  private fun fetchCoreTerms() {
    val fileName = getFile("CoreTerms")
    readFileToStringMap(fileName, coreTerms)
  }

  fun getCoreFromCode(originalCode: String, schemes: List<NAMESPACE>): MutableSet<TTIriRef> {
    try {
      for (scheme in schemes) {
        if (codeCoreMap[scheme] == null) fetchCodeCoreMap(scheme)
        codeCoreMap[scheme]?.get(originalCode)?.let {
          return it.map { iri -> TTIriRef.iri(iri) }.toMutableSet()
        }
      }
      return mutableSetOf()
    } catch (e: Exception) {
      log.error("unable to retrieve core from code : ${e.message}")
      return mutableSetOf()
    }
  }

  @Throws(IOException::class)
  fun getCoreFromLegacyTerm(originalTerm: String, scheme: NAMESPACE): MutableSet<TTIriRef> {
    if (termCoreMap[scheme] == null) fetchTermCoreMap(scheme)
    termCoreMap[scheme]?.get(originalTerm)?.let { return it.map { iri -> TTIriRef.iri(iri) }.toMutableSet() }
    return mutableSetOf()
  }

  @Throws(IOException::class)
  fun getAllEntities(): MutableSet<String> {
    val entities: MutableSet<String> = hashSetOf()
    val fileName = getFile("Entities")
    Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1).use { lines ->
      lines.forEach { e -> entities.add(e) }
    }
    return entities
  }


  @Throws(IOException::class)
  fun fetchCodeMap(scheme: NAMESPACE) {
    val codeSet = codes.getOrPut(scheme) { hashMapOf() }
    val fileName = getSchemeFile("CodeMap", scheme)
    readFileToSetMap(fileName, codeSet)
  }

  @Throws(IOException::class)
  fun fetchCodeIds(scheme: NAMESPACE) {
    val codeSet = codeIds.getOrPut(scheme) { hashMapOf() }
    val fileName = getSchemeFile("CodeIds", scheme)
    readFileToSetMap(fileName, codeSet)
  }


  @Throws(IOException::class)
  fun fetchTermCodes(scheme: NAMESPACE) {
    val iris = termCodes.getOrPut(scheme) { hashMapOf() }
    val fileName = getSchemeFile("TermCodes", scheme)
    readFileToStringMap(fileName, iris)
  }

  @Throws(IOException::class)
  fun getDescendants(concept: String): MutableMap<String, MutableSet<String>> {
    return fetchDescendants(concept)
  }

  @Throws(IOException::class)
  private fun fetchDescendants(concept: String): MutableMap<String, MutableSet<String>> {
    val fileName = getFile("Descendants")
    val children: MutableMap<String, MutableSet<String>> = hashMapOf()
    Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1).use { lines ->
      lines.forEach { line ->
        val fields = line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val parent = fields[0]
        val child = fields[1]
        val childName = fields[2]
        if (parent == concept) {
          children.getOrPut(child) { hashSetOf() }.add(childName)
        }
      }
    }
    return children
  }

  @Throws(IOException::class)
  fun getCodeCoreMap(scheme: NAMESPACE): MutableMap<String, MutableSet<String>> {
    return codeCoreMap[scheme] ?: fetchCodeCoreMap(scheme)
  }

  @Throws(IOException::class)
  fun fetchCodeCoreMap(scheme: NAMESPACE): MutableMap<String, MutableSet<String>> {
    return codeCoreMap.getOrPut(scheme) { hashMapOf() }.also {
      val fileName = getSchemeFile("CodeCoreMap", scheme)
      readFileToSetMap(fileName, it)
    }
  }

  @Throws(IOException::class)
  fun fetchTermCoreMap(scheme: NAMESPACE) {
    val coreMap = termCoreMap.getOrPut(scheme) { hashMapOf() }
    val fileName = getSchemeFile("TermCoreMap", scheme)
    readFileToSetMap(fileName, coreMap)
  }

  @Throws(IOException::class)
  private fun readFileToSetMap(fileName: String, coreMap: MutableMap<String, MutableSet<String>>) {
    Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1).use { lines ->
      lines.forEach { line ->
        val fields = line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val term = fields[0]
        val core = fields[1]
        coreMap.getOrPut(term) { hashSetOf() }.add(core)
      }
    }
  }

  @Throws(IOException::class)
  private fun readFileToStringMap(fileName: String, stringMap: MutableMap<String, String>) {
    Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1).use { lines ->
      lines.forEach { line ->
        val fields = line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val code = fields[0]
        val iri = fields[1]
        stringMap[code] = iri
      }
    }
  }


  private fun getSchemeFile(fileType: String?, namespace: NAMESPACE): String {
    return "$dataPath/$fileType-${namespace.name}.txt"
  }

  private fun getFile(fileType: String?): String {
    return "$dataPath/$fileType.txt"
  }
}
