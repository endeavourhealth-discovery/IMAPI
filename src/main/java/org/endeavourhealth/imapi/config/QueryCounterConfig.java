package org.endeavourhealth.imapi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.utility.QueryCounter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Logs requests that make many database queries. Off by default; set {@code imapi.querycounter.threshold}
 * (env {@code IMAPI_QUERYCOUNTER_THRESHOLD}) to the number of queries at which a request should be reported.
 */
@Slf4j
@Configuration
public class QueryCounterConfig implements WebMvcConfigurer {
  private static final int TOP_QUERIES = 3;

  public QueryCounterConfig(@Value("${imapi.querycounter.threshold:0}") int threshold) {
    QueryCounter.configure(threshold);
    if (threshold > 0) log.info("Query counter enabled: reporting requests with {} or more queries", threshold);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new HandlerInterceptor() {
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(StartTime.ATTRIBUTE, System.nanoTime());
        QueryCounter.start();
        return true;
      }

      @Override
      public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        QueryCounter.Stats stats = QueryCounter.finish();
        if (stats != null && stats.getQueries() >= QueryCounter.getThreshold()) {
          Object start = request.getAttribute(StartTime.ATTRIBUTE);
          long millis = start instanceof Long s ? (System.nanoTime() - s) / 1_000_000 : -1;
          log.warn("{} {} made {} queries on {} connections in {} ms. Most repeated: {}",
            request.getMethod(), request.getRequestURI(), stats.getQueries(), stats.getConnections(), millis,
            stats.topQueries(TOP_QUERIES));
        }
      }
    }).addPathPatterns("/api/**");
  }

  private static final class StartTime {
    static final String ATTRIBUTE = QueryCounterConfig.class.getName() + ".start";

    private StartTime() {
    }
  }
}
