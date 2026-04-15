package org.endeavourhealth.imapi.controllers

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.endeavourhealth.imapi.logic.service.SecurityService
import org.endeavourhealth.imapi.model.uprn.Activity
import org.endeavourhealth.imapi.model.uprn.UploadStatus
import org.endeavourhealth.imapi.model.uprn.UprnException
import org.endeavourhealth.imapi.model.uprn.UprnSearchResponse
import org.endeavourhealth.imapi.utility.MetricsHelper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.annotation.RequestScope
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.net.URLEncoder.encode
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.io.encoding.Base64


@RestController
@RequestScope
@RequestMapping(value = ["api/uprn/private"])
@Tag(
  name = "UPRNController",
  description = "Controller for accessing the UPRN/Assign product."
)
@CrossOrigin(origins = ["*"])
open class UPRNController(
  private val securityService: SecurityService
) {
  private val log = LoggerFactory.getLogger(UPRNController::class.java)
  private val mapper: ObjectMapper = ObjectMapper()
    .registerKotlinModule()
    .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
  private val uprnUrl  = System.getenv("UPRN_API")
  private val uprnUsername = System.getenv("UPRN_USERNAME")
  private val uprnPassword = System.getenv("UPRN_PASSWORD")
  private val headers = hashMapOf(
    "Authorization" to "Basic " + Base64.encode("${uprnUsername}:${uprnPassword}".encodeToByteArray()),
  )

  @Operation(summary = "UPRN search", description = "Perform UPRN search based on an address and property type.")
  @GetMapping(value = ["/getinfo"])
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(UprnException::class)
  open fun getInfo(
    request: HttpServletRequest,
    response: HttpServletResponse,
    @RequestParam(name = "adrec", required = true) adrec: String,
    @RequestParam(name = "commercial", required = true) commercial: String,
  ): UprnSearchResponse? {
    MetricsHelper.recordTime("API.UPRN.getinfo.GET").use {
      log.debug("getinfo")
      // securityService.requiresPermission(Permission(Resource.UPRN, listOf(UserRole.UPRN), listOf()), request);

      val uprnReq = HttpRequest.newBuilder()
        .uri(URI("$uprnUrl/api2/getinfo?adrec=${encode(adrec, Charsets.UTF_8)}&commercial=${encode(commercial, Charsets.UTF_8)}"))
        .GET()

      val response = callUPRNAndParse(uprnReq, securityService.getUser(request).id, UprnSearchResponse::class.java)

      return response
    }
  }

  @Operation(summary = "User UPRN activity", description = "Get the UPRN activity info for the given user")
  @GetMapping(value = ["/activity"])
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(UprnException::class)
  open fun activity(
    request: HttpServletRequest,
    response: HttpServletResponse,
    @RequestParam(name = "u", required = true) user: String,
  ): List<Activity>? {
    MetricsHelper.recordTime("API.UPRN.activity.GET").use {
      log.debug("activity")
      // securityService.requiresPermission(Permission(Resource.UPRN, listOf(UserRole.UPRN), listOf()), request);

      val uprnReq = HttpRequest.newBuilder()
        // .uri(URI.create("$uprnUrl/api2/activity?u=${user}"))
        .uri(URI.create("$uprnUrl/api2/activity?u=${uprnUsername}"))
        .GET()

      return callUPRNAndParse(uprnReq, securityService.getUser(request).id, object: TypeReference<List<Activity>>() {})
    }
  }

  @Operation(summary = "Download batch UPRN results", description = "Download batch UPRN results")
  @GetMapping(value = ["/download3"], produces = [MediaType.TEXT_PLAIN_VALUE])
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(UprnException::class)
  open fun download(
    request: HttpServletRequest,
    response: HttpServletResponse,
    @RequestParam(name = "file", required = true) file: String,
  ): String? {
    MetricsHelper.recordTime("API.UPRN.download.GET").use {
      log.debug("download")
      // securityService.requiresPermission(Permission(Resource.UPRN, listOf(UserRole.UPRN), listOf()), request);

      val uprnReq = HttpRequest.newBuilder()
        .uri(URI.create("$uprnUrl/api2/download3?file=${file}"))
        .GET()

      return callUPRN(uprnReq, securityService.getUser(request).id)
    }
  }

  @Operation(summary = "Upload batch addresses", description = "Upload a CSV file for batch UPRN lookup")
  @PostMapping(value = ["/fileupload2"], consumes = ["multipart/form-data"])
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(UprnException::class)
  open fun upload(
    request: HttpServletRequest,
    response: HttpServletResponse,
    @RequestPart("file") file: MultipartFile
  ): UploadStatus? {
    MetricsHelper.recordTime("API.UPRN.upload.POST").use {
      log.debug("upload")
      // securityService.requiresPermission(Permission(Resource.UPRN, listOf(UserRole.UPRN), listOf()), request);

      val fileContent = String(file.bytes)

      val byteArrays = listOf<ByteArray>(
        "--boundary\r\n".encodeToByteArray(),
        "Content-Disposition: form-data; name=\"file\"; filename=\"".encodeToByteArray(),
        file.name.encodeToByteArray(),
        "\"\r\nContent-Type: text/plain\r\n\r\n".encodeToByteArray(),
        fileContent.encodeToByteArray(),
        "\r\n--boundary--\r\n".encodeToByteArray());

      val uprnReq = HttpRequest.newBuilder()
        .uri(URI.create("$uprnUrl/api2/fileupload2"))
        .POST(HttpRequest.BodyPublishers.ofByteArrays(byteArrays.asIterable()))

      val uploadStatus = callUPRNAndParse(uprnReq, securityService.getUser(request).id, UploadStatus::class.java)
      return uploadStatus
    }
  }

  fun <T> callUPRNAndParse(request: HttpRequest.Builder, userId: String, valueType: Class<T>): T? {
    val response: String = callUPRN(request, userId, MediaType.APPLICATION_JSON)
    return mapper.readValue(response, valueType)
  }

  private fun <T> callUPRNAndParse(
    request: HttpRequest.Builder,
    userId: String,
    valueType: TypeReference<T>
  ): T? {
    val response: String = callUPRN(request, userId, MediaType.APPLICATION_JSON)
    return mapper.readValue(response, valueType)
  }

  private fun callUPRN(request: HttpRequest.Builder, userId: String, mediaType: MediaType = MediaType.TEXT_PLAIN): String {
    val client: HttpClient = HttpClient.newBuilder().build()
    request.setHeader("Accept", mediaType.toString())
    request.setHeader("Authorization", "Basic " + Base64.encode("${uprnUsername}:${uprnPassword}".encodeToByteArray()))
    request.setHeader("User-Id", userId)
    return client.send(request.build(), HttpResponse.BodyHandlers.ofString()).body()
  }

}