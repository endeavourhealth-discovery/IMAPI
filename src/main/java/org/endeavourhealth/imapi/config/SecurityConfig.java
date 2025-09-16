package org.endeavourhealth.imapi.config;

import org.endeavourhealth.imapi.errorhandling.RestAccessDeniedHandler;
import org.endeavourhealth.imapi.errorhandling.RestAuthenticationEntryPoint;
import org.endeavourhealth.imapi.utility.EnvHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {


  @Bean
  protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(this::setRequestPermissions)
      .exceptionHandling(ex -> ex
        .accessDeniedHandler(accessDeniedHandler())
        .authenticationEntryPoint(authenticationEntryPoint())
      )
      .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .oauth2ResourceServer(oa2 -> oa2.jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
  }

  protected void setRequestPermissions(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry req) {
    if (EnvHelper.isPublicMode()) {
      req.requestMatchers(HttpMethod.GET, "/api/**/public/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/**/public/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/fhir/r4/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/webjars/**").permitAll();
    }

    req.requestMatchers(HttpMethod.GET, "/").permitAll()
      .requestMatchers(HttpMethod.GET, "/index.html").permitAll()
      .requestMatchers(HttpMethod.GET, "/api/status/public/**").permitAll()
      .requestMatchers(HttpMethod.POST, "/oauth/**").permitAll()
      .requestMatchers(HttpMethod.GET, "/api/cognito/public/config").permitAll()
      // Temporary for testing Smartlife API
      .requestMatchers(HttpMethod.GET, "/api/entity/public/partial").permitAll()
      .requestMatchers(HttpMethod.POST, "/api/query/public/sql").permitAll()
      // -----------------------------------
      .anyRequest().authenticated();
  }

  private HttpFirewall allowUrlEncodedSlashHttpFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowUrlEncodedSlash(true);
    firewall.setAllowUrlEncodedDoubleSlash(true);
    firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
    return firewall;
  }

  private JwtAuthenticationConverter grantedAuthoritiesExtractor() {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
        List<String> list = jwt.getClaimAsStringList("cognito:groups");
        if (list != null && !list.isEmpty())
          return list.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        else
          return null;
      }
    );
    return jwtAuthenticationConverter;
  }

  RestAccessDeniedHandler accessDeniedHandler() {
    return new RestAccessDeniedHandler();
  }

  RestAuthenticationEntryPoint authenticationEntryPoint() {
    return new RestAuthenticationEntryPoint();
  }
}
