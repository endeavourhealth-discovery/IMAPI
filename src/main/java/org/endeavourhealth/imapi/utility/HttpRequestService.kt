package org.endeavourhealth.imapi.utility

import org.apache.http.HttpException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

class HttpRequestService {
  private val log = LoggerFactory.getLogger(HttpRequestService::class.java)

  companion object {
    @JvmStatic
    fun <T : Any, K> Map<T, K>.toMultiValueMap(): MultiValueMap<T, K> {
      val mvm: MultiValueMap<T, K> = LinkedMultiValueMap()
      if (this.isNotEmpty()) {
        for (entry in this.entries) {
          mvm.add(entry.key, entry.value)
        }
      }
      return mvm
    }
  }

  @Throws(HttpException::class)
  fun <T> httpPost(
    path: String,
    responseType: Class<T>,
    body: MutableMap<String, Any>? = null,
    headers: Map<String, String>? = null
  ): T? {
    var reqHeaders = HttpHeaders()
    if (!headers.isNullOrEmpty())
      reqHeaders.addAll(headers.toMultiValueMap())

    val requestEntity = HttpEntity(body, reqHeaders)

    val response = RestTemplate()
      .postForEntity(path, requestEntity, responseType)
    if (response.statusCode.is2xxSuccessful) return response.body
    else throw HttpException("Post request failed to: ${path} with status code ${response.statusCode}")
  }

  fun <T> httpGet(
    path: String,
    responseType: Class<T>,
    params: Map<String, String> = HashMap(),
    headers: Map<String, String>? = null,
    accept: List<MediaType>? = null
  ): T? {
    var reqHeaders = HttpHeaders()
    if (!headers.isNullOrEmpty())
      reqHeaders.addAll(headers.toMultiValueMap())

    if (accept != null)
      reqHeaders.accept = accept

    var url = path;
    if (params.isNotEmpty()) {
      url += "?" + (params.keys.map { k -> "$k={$k}" }).joinToString("&")
    }

    val response = RestTemplate()
      .exchange(url, HttpMethod.GET, HttpEntity<String>(reqHeaders), responseType, params)
    if (response.statusCode.is2xxSuccessful) return response.body
    else throw HttpException("Http get failed: " + url + " with status " + response.statusCode)
  }
}