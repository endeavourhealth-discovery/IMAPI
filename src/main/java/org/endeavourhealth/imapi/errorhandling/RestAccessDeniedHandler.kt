package org.endeavourhealth.imapi.errorhandling

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.charset.StandardCharsets

@Component
class RestAccessDeniedHandler : AccessDeniedHandler {
  private val log = LoggerFactory.getLogger(javaClass)

  @Throws(IOException::class)
  override fun handle(
    httpServletRequest: HttpServletRequest,
    httpServletResponse: HttpServletResponse,
    e: AccessDeniedException
  ) {
    log.debug("handle")
    httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED
    httpServletResponse.outputStream.use { out ->
      out.write("Access denied".toByteArray(StandardCharsets.UTF_8))
    }
  }
}
