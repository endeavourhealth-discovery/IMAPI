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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

  @Bean
  protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http
      .cors(cors -> cors.configurationSource(corsFilter()))
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(this::setRequestPermissions)
      .exceptionHandling(ex -> ex
        .accessDeniedHandler(accessDeniedHandler())
        .authenticationEntryPoint(authenticationEntryPoint())
      )
      .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .addFilterBefore(new JwtCookieAuthFilter(), BasicAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public UrlBasedCorsConfigurationSource corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOrigins(List.of("http://localhost:8082"));
    config.setAllowedMethods(Arrays.asList("POST", "GET", "DELETE", "PUT", "OPTIONS"));
    config.setAllowedHeaders(Arrays.asList("X-Requested-From", "Origin", "Content-Type", "Accept", "Authorization"));
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
  }

  protected void setRequestPermissions(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry req) {
    req.requestMatchers(HttpMethod.OPTIONS).permitAll();
    if (EnvHelper.isPublicMode()) {
      req.requestMatchers(HttpMethod.GET, "/api/*/public/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/*/public/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/*/private/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/*/private/**").permitAll()
//        .requestMatchers(HttpMethod.GET, "/api/fhir/r4/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/webjars/**").permitAll();
    }

    req.requestMatchers(HttpMethod.GET, "/").permitAll()
      .requestMatchers(HttpMethod.GET, "/index.html").permitAll()
      .requestMatchers(HttpMethod.POST, "/oauth/**").permitAll()
      .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
      .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
      .requestMatchers(HttpMethod.GET, "/api/*/public/**").permitAll()
      .requestMatchers(HttpMethod.POST, "/api/*/public/**").permitAll()
      // Temporary for testing Smartlife API
      .requestMatchers(HttpMethod.GET, "/api/fhir/r4/**").permitAll()
      // -----------------------------------
      .anyRequest().authenticated();
  }

  private HttpFirewall allowUrlEncodedSlashHttpFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowUrlEncodedSlash(true);
    firewall.setAllowUrlEncodedDoubleSlash(true);
    firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
    return firewall;
  }

  RestAccessDeniedHandler accessDeniedHandler() {
    return new RestAccessDeniedHandler();
  }

  RestAuthenticationEntryPoint authenticationEntryPoint() {
    return new RestAuthenticationEntryPoint();
  }
}
