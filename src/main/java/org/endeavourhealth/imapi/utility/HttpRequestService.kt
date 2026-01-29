package org.endeavourhealth.imapi.utility

import org.apache.http.HttpException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

class HttpRequestService {
  companion object {
    @JvmStatic
    fun <T, K> Map<T, K>.toMultiValueMap(): MultiValueMap<T, K> {
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
    body: MutableMap<String, Any>?,
    headers: Map<String, String>?
  ): T {
    var reqHeaders = HttpHeaders()
    if (!headers.isNullOrEmpty())
      reqHeaders.addAll(headers.toMultiValueMap())

    val requestEntity = HttpEntity(body, reqHeaders)

    val response = RestTemplate()
      .postForEntity(path, requestEntity, responseType)
    if (response.statusCode != HttpStatus.OK) {
      throw HttpException("Post request failed to: ${path} with status code ${response.statusCode}")
    }
    return response.body ?: throw HttpException("Http post failed to: $path with empty response body ")
  }

  fun <T> httpGet(
    path: String,
    responseType: Class<T>,
    params: Map<String, String>? = HashMap(),
    headers: Map<String, String>? = null
  ): T {
    var reqHeaders = HttpHeaders()
    if (!headers.isNullOrEmpty())
      reqHeaders.addAll(headers.toMultiValueMap())

    var url = path;
    if (!params.isNullOrEmpty()) {
      url += "?" + (params.keys.map { k -> "$k={$k}" }).joinToString("&")
    }

    val response = RestTemplate()
      .exchange(url, HttpMethod.GET, HttpEntity<String>(reqHeaders), responseType, params)
    if (response.hasBody()) return response.body
    else throw HttpException("Http get failed: " + url + " with status " + response.getStatusCode())
  }
}