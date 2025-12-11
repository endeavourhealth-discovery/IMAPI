package org.endeavourhealth.imapi.logic.service

import org.apache.commons.text.WordUtils
import org.endeavourhealth.imapi.dataaccess.CodeGenRepository
import org.endeavourhealth.imapi.model.DataModelProperty
import org.endeavourhealth.imapi.model.codegen.CodeGenTemplate
import org.endeavourhealth.imapi.model.dto.CodeGenDto
import org.endeavourhealth.imapi.model.search.SearchResultSummary
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.vocabulary.EntityType
import org.endeavourhealth.imapi.vocabulary.Namespace
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDate
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class CodeGenService {
  private val entityService: EntityService = EntityService()
  private val dataModelService: DataModelService = DataModelService()
  private val codeGenRepository: CodeGenRepository = CodeGenRepository()

  fun getCodeTemplateList(): List<String> = codeGenRepository.codeTemplateList

  fun getCodeTemplate(name: String): CodeGenDto = codeGenRepository.getCodeTemplate(name)

  fun updateCodeTemplate(
    name: String,
    extension: String,
    wrapper: String?,
    dataTypeMap: Map<String, String>?,
    template: String,
    complexTypes: Boolean?
  ) {
    codeGenRepository.updateCodeTemplate(name, extension, wrapper, dataTypeMap, template, complexTypes)
  }

  fun generateCodeForModel(modelIri: String, template: CodeGenDto, namespace: String): String {
    val model = getModelSummary(modelIri)
    return generateCodeForModel(template, model, namespace)
  }

  fun generateCode(iri: String?, templateName: String, namespace: String): HttpEntity<Any> {
    val models: List<TTIriRef> = if (iri.isNullOrEmpty()) getIMModels() else listOf(getModelSummary(iri))
    val template = codeGenRepository.getCodeTemplate(templateName)
    return createModelCodeZip(namespace, models, template)
  }

  private fun getIMModels(): List<TTIriRef> {
    val models = entityService.getEntitiesByType(EntityType.NODESHAPE)
    return models.filter { it.iri.startsWith(Namespace.IM.toString()) }
  }

  private fun getModelSummary(iri: String): TTIriRef {
    val summary: SearchResultSummary = entityService.getSummary(iri)
    return TTIriRef(summary.iri, summary.name).setDescription(summary.description)
  }

  private fun createModelCodeZip(namespace: String, models: List<TTIriRef>, template: CodeGenDto): HttpEntity<Any> {
    val baos = ByteArrayOutputStream()
    try {
      ZipOutputStream(baos).use { zos ->
        addModelsToZip(namespace, models, template, zos)
      }
    } catch (e: IOException) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to create zip:" + e.message)
    }
    val filename = models.first().name + " " + LocalDate.now()
    val headers = HttpHeaders()
    headers.contentType = MediaType("application", "force-download")
    headers[HttpHeaders.CONTENT_DISPOSITION] = "attachment;filename=\"$filename.txt\""
    return HttpEntity(baos.toByteArray(), headers)
  }

  @Throws(IOException::class)
  private fun addModelsToZip(namespace: String, models: List<TTIriRef>, template: CodeGenDto, zos: ZipOutputStream) {
    for (model in models) {
      val code = generateCodeForModel(template, model, namespace)
      val entry = ZipEntry(clean(toTitleCase(codify(model.name))) + template.extension)
      zos.putNextEntry(entry)
      zos.write(code.toByteArray())
      zos.closeEntry()
    }
  }

  protected fun generateCodeForModel(template: CodeGenDto, model: TTIriRef, namespace: String): String {
    val properties = dataModelService.getDataModelProperties(model.iri, template.complexTypes)
    return generateCodeForModel(template, model, properties, namespace)
  }

  protected fun generateCodeForModel(
      template: CodeGenDto,
      model: TTIriRef,
      properties: List<DataModelProperty>?,
      namespace: String
  ): String {
    val code = StringBuilder()
    val t: CodeGenTemplate = splitTemplate(template.template)
    code.append(replaceTokens(template, t.header, namespace, model, null))
    if (!properties.isNullOrEmpty()) {
      for (prop in properties) {
        code.append(replacePropertyTokens(template, t.property, t.collectionProperty, namespace, model, prop))
      }
    }
    code.append(t.footer)
    return code.toString()
  }

  private fun splitTemplate(template: String): CodeGenTemplate {
    val propertyTemp = "<template #property>"
    val propertyTempEnd = "</template #property>"
    val arrayTemp = "<template #array>"
    val arrayTempEnd = "</template #array>"

    val ps_s = template.indexOf(propertyTemp)
    if (ps_s < 0) return CodeGenTemplate()

    var ps_l = propertyTemp.length
    if ("\n" == template.substring(ps_s + ps_l, ps_s + ps_l + 1)) ps_l++

    val pe_s = template.indexOf(propertyTempEnd, ps_s)
    var pe_l = propertyTempEnd.length
    if (template.length > pe_s + pe_l && "\n" == template.substring(pe_s + pe_l, pe_s + pe_l + 1)) pe_l++

    val as_s = template.indexOf(arrayTemp)
    var as_l = arrayTemp.length
    if (as_s > 0 && "\n" == template.substring(as_s + as_l, as_s + as_l + 1)) as_l++

    val ae_s = template.indexOf(arrayTempEnd, as_s)

    val header = template.substring(0, ps_s)
    val footer = template.substring(pe_s + pe_l)
    val property = template.substring(ps_s + ps_l, if (as_s > 0) as_s else pe_s)
    val array = if (as_s > 0) template.substring(as_s + as_l, ae_s) else ""

    return CodeGenTemplate()
      .setHeader(header)
      .setFooter(footer)
      .setProperty(property)
      .setCollectionProperty(array)
  }

  private fun replacePropertyTokens(
      template: CodeGenDto,
      property: String,
      collectionProperty: String,
      namespace: String,
      model: TTIriRef,
      prop: DataModelProperty
  ): String {
    var propertyLocal = property
    if (prop.isArray) propertyLocal += collectionProperty
    return replaceTokens(template, propertyLocal, namespace, model, prop)
  }

  private fun replaceTokens(
      template: CodeGenDto,
      subTemplate: String,
      namespace: String?,
      model: TTIriRef,
      prop: DataModelProperty?
  ): String {
    var result = subTemplate

    if (namespace != null) result = replaceVariants(result, "NAME SPACE", namespace)
    if (model.hasName()) result = replaceVariants(result, "MODEL NAME", model.name)
    if (model.hasDescription()) result = replaceVariants(result, "MODEL COMMENT", model.description)

    if (prop != null && prop.hasProperty() && prop.property.hasName() && prop.hasType()) {
      result = if (prop.hasType() && template.getDataType(prop.type.iri) != null) {
        replaceTypedPropertyTokens(template, prop, result)
      } else if (prop.isArray && template.hasCollectionWrapper()) {
        replaceArrayPropertyTokens(template, prop, result)
      } else {
        replaceRemainingPropertyTokens(prop, result)
      }

      result = replaceVariants(result, "PROPERTY NAME", prop.property.name)
    }

    return result
  }

  private fun replaceRemainingPropertyTokens(prop: DataModelProperty, result: String): String {
    val basePropertyType = if (prop.type.hasName()) prop.type.name else "!!UNKNOWN!!"
    return replaceVariants(result, "DATA TYPE", basePropertyType)
  }

  private fun replaceArrayPropertyTokens(template: CodeGenDto, prop: DataModelProperty, result: String): String {
    if (!template.hasCollectionWrapper()) return result

    var res = result
    if (!template.collectionWrapper.contains("${'$'}{")) {
      res = replaceVariants(res, "DATA TYPE", template.collectionWrapper)
    } else {
      val basePropertyType = if (prop.type.hasName()) prop.type.name else "!!UNKNOWN!!"
      val cw = template.collectionWrapper
      val cwType = cw.substring(cw.indexOf("${'$'}{"), cw.indexOf("}") + 1)
      val placeholder = replaceVariants(template.collectionWrapper, "BASE DATA TYPE", "${'$'}{basedatatypeplaceholder}")
      res = replaceVariants(res, "DATA TYPE", placeholder)
      res = res.replace("${'$'}{basedatatypeplaceholder}", cwType)
      res = replaceVariants(res, "BASE DATA TYPE", basePropertyType)
    }
    return res
  }

  private fun replaceTypedPropertyTokens(template: CodeGenDto, prop: DataModelProperty, result: String): String {
    val basePropertyType = template.getDataType(prop.type.iri)
    val propertyType = if (prop.isArray && template.hasCollectionWrapper())
      replace(template.collectionWrapper, "BASE DATA TYPE", basePropertyType)
    else basePropertyType

    var res = replace(result, "BASE DATA TYPE", basePropertyType)
    res = replace(res, "DATA TYPE", propertyType)
    return res
  }

  private fun replaceVariants(template: String, name: String, value: String): String {
    return template
      .replace("${'$'}{" + name + "}", value)                                                // Unaltered
      .replace("${'$'}{" + toTitleCase(name) + "}", toTitleCase(value))                      // Title Case
      .replace("${'$'}{" + toCamelCase(name) + "}", toCamelCase(value))                      // camel Case
      .replace("${'$'}{" + name.lowercase() + "}", value.lowercase())                    // lower case
      .replace("${'$'}{" + clean(codify(name)) + "}", clean(codify(value)))                                // Codified
      .replace("${'$'}{" + clean(toTitleCase(codify(name))) + "}", clean(toTitleCase(codify(value))))      // TitleCase (codified)
      .replace("${'$'}{" + clean(toCamelCase(codify(name))) + "}", clean(toCamelCase(codify(value))))      // camelCase (codified)
      .replace("${'$'}{" + clean(codify(name).lowercase()) + "}", clean(codify(value).lowercase()))   // lowercase (codified)
  }

  private fun replace(template: String, name: String, value: String): String {
    return template
      .replace("${'$'}{" + name + "}", value)
      .replace("${'$'}{" + clean(name) + "}", value)
      .replace("${'$'}{" + name.lowercase() + "}", value)
      .replace("${'$'}{" + clean(name.lowercase()) + "}", value)
      .replace("${'$'}{" + toCamelCase(name) + "}", value)
      .replace("${'$'}{" + clean(toCamelCase(name)) + "}", value)
      .replace("${'$'}{" + toTitleCase(name) + "}", value)
      .replace("${'$'}{" + clean(toTitleCase(name)) + "}", value)
  }

  private fun clean(name: String): String = name.replace(" ", "")

  private fun codify(name: String): String {
    return name
      .replace("-", " ")
      .replace(".", " ")
      .replace(",", " ")
      .replace("#", " ")
      .replace("'", " ")
      .replace("(", " ")
      .replace(")", " ")
      .replace("\"", " ")
      .replace("/", " ")
  }

  private fun toCamelCase(strIn: String): String {
    var str = toTitleCase(strIn)
    str = str.substring(0, 1).lowercase() + str.substring(1)
    return str
  }

  private fun toTitleCase(str: String): String = WordUtils.capitalizeFully(str)
}