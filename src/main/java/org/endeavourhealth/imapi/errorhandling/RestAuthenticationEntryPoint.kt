package org.endeavourhealth.imapi.errorhandling

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class)
    override fun commence(httpServletRequest: HttpServletRequest?, httpServletResponse: HttpServletResponse, ex: AuthenticationException?) {
        if (Optional.ofNullable<String?>(System.getenv("HOSTING_MODE")).orElse("") == "private") {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN)
            httpServletResponse.getOutputStream().use { out ->
                out.write("Authentication failed".toByteArray(StandardCharsets.UTF_8))
            }
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN)
            httpServletResponse.getOutputStream().use { out ->
                out.write("Access forbidden".toByteArray(StandardCharsets.UTF_8))
            }
        }
    }
}
