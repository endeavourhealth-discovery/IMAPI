package org.endeavourhealth.imapi.model

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

// Standard (RFC 7807) REST API (error) response structure
// https://www.rfc-editor.org/rfc/rfc7807
class ProblemDetailResponse {
  var status: Int? = null
    private set
  var title: String? = null
    private set
  var detail: String? = null
    private set

  fun setStatus(status: Int?): ProblemDetailResponse {
    this.status = status
    return this
  }

  fun setTitle(title: String?): ProblemDetailResponse {
    this.title = title
    return this
  }

  fun setDetail(detail: String?): ProblemDetailResponse {
    this.detail = detail
    return this
  }

  companion object {
    val JSON_MEDIA_TYPE: MediaType = MediaType.valueOf("application/problem+json")

    fun create(status: HttpStatus): ResponseEntity<ProblemDetailResponse> {
      val pdr = ProblemDetailResponse().setStatus(status.value())
      return ResponseEntity<ProblemDetailResponse>(pdr, status)
    }

    fun create(status: HttpStatus, title: String): ResponseEntity<ProblemDetailResponse> {
      val pdr = ProblemDetailResponse()
        .setStatus(status.value())
        .setTitle(title)
      return ResponseEntity<ProblemDetailResponse>(pdr, status)
    }

    @JvmStatic
    fun create(status: HttpStatus, title: String, detail: String): ResponseEntity<ProblemDetailResponse> {
      val pdr = ProblemDetailResponse()
        .setStatus(status.value())
        .setTitle(title)
        .setDetail(detail)
      return ResponseEntity<ProblemDetailResponse>(pdr, status)
    }
  }
}
