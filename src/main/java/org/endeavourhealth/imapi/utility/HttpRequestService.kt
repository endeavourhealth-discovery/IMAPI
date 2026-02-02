package org.endeavourhealth.imapi.utility

import org.apache.http.HttpException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate

class HttpRequestService {
  @Throws(HttpException::class)
  fun <T : Any> httpPost(
    path: String,
    responseType: Class<T>,
    body: MutableMap<String, Any>?,
    headers: Map<String, String>?
  ): T? {
    val reqHeaders = HttpHeaders()
    headers?.forEach { (k, v) -> reqHeaders.add(k, v) }

    val requestEntity = HttpEntity(body, reqHeaders)

    val response = RestTemplate()
      .postForEntity(path, requestEntity, responseType)
    if (response.statusCode.is2xxSuccessful) return response.body
    else throw HttpException("Post request failed to: ${path} with status code ${response.statusCode}")
  }

  fun <T : Any> httpGet(
    path: String,
    responseType: Class<T>,
    params: Map<String, String>? = HashMap(),
    headers: Map<String, String>? = null
  ): T? {
    val reqHeaders = HttpHeaders()
    headers?.forEach { (k, v) -> reqHeaders.add(k, v) }

    var url = path
    if (!params.isNullOrEmpty()) {
      url += "?" + (params.keys.map { k -> "$k={$k}" }).joinToString("&")
    }

    val response = RestTemplate()
      .exchange(url, HttpMethod.GET, HttpEntity<String>(reqHeaders), responseType, params)
    if (response.statusCode.is2xxSuccessful) return response.body
    else throw HttpException("Http get failed: " + url + " with status " + response.getStatusCode())
  }
}