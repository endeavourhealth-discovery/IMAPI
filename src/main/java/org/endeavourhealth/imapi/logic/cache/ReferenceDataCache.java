package org.endeavourhealth.imapi.logic.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.dto.FilterOptionsDto;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Short-lived cache for reference data that is read often and changes rarely (namespaces and the search filter
 * options/defaults). Each lookup runs several SPARQL queries, and the UI requests them on every page load.
 * <p>
 * Entries expire after {@link #TTL} so changes made outside this process (e.g. bulk imports) are still picked up, and
 * {@link #invalidateAll()} is called after anything is filed through this API. Callers always receive copies because
 * the cached DTOs are mutable.
 */
public final class ReferenceDataCache {
  static final Duration TTL = Duration.ofMinutes(5);
  private static final String NAMESPACES = "namespaces";
  private static final String FILTER_OPTIONS = "filterOptions";
  private static final String FILTER_DEFAULTS = "filterDefaults";

  private static final Cache<String, Object> CACHE = Caffeine.newBuilder()
    .expireAfterWrite(TTL)
    .maximumSize(10)
    .build();

  private ReferenceDataCache() {
  }

  public static List<Namespace> getNamespaces(Supplier<List<Namespace>> loader) {
    List<Namespace> cached = load(NAMESPACES, loader);
    return cached == null ? null : cached.stream().map(n -> new Namespace(n.getIri(), n.getPrefix(), n.getName())).toList();
  }

  public static FilterOptionsDto getFilterOptions(Supplier<FilterOptionsDto> loader) {
    return copy(load(FILTER_OPTIONS, loader));
  }

  public static FilterOptionsDto getFilterDefaults(Supplier<FilterOptionsDto> loader) {
    return copy(load(FILTER_DEFAULTS, loader));
  }

  /**
   * Call after anything that may change the cached data has been written.
   */
  public static void invalidateAll() {
    CACHE.invalidateAll();
  }

  @SuppressWarnings("unchecked")
  private static <T> T load(String key, Supplier<T> loader) {
    // Cache.get is atomic per key, so concurrent misses run the loader once; a null result is not cached
    return (T) CACHE.get(key, k -> loader.get());
  }

  private static FilterOptionsDto copy(FilterOptionsDto source) {
    if (source == null) return null;
    FilterOptionsDto result = new FilterOptionsDto();
    result.setStatus(copy(source.getStatus()));
    result.setSchemes(copy(source.getSchemes()));
    result.setTypes(copy(source.getTypes()));
    result.setSortFields(copy(source.getSortFields()));
    result.setSortDirections(copy(source.getSortDirections()));
    if (source.getTypeSchemes() != null) {
      Map<String, List<TTIriRef>> typeSchemes = new LinkedHashMap<>();
      source.getTypeSchemes().forEach((k, v) -> typeSchemes.put(k, copy(v)));
      result.setTypeSchemes(typeSchemes);
    }
    return result;
  }

  private static List<TTIriRef> copy(List<TTIriRef> source) {
    if (source == null) return null;
    return source.stream()
      .map(r -> new TTIriRef(r.getIri(), r.getName()).setDescription(r.getDescription()))
      .collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));
  }
}
