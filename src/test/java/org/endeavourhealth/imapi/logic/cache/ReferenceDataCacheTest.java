package org.endeavourhealth.imapi.logic.cache;

import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.dto.FilterOptionsDto;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class ReferenceDataCacheTest {

  @BeforeEach
  @AfterEach
  void clear() {
    ReferenceDataCache.invalidateAll();
  }

  private static List<Namespace> namespaces() {
    return new ArrayList<>(List.of(new Namespace("http://endhealth.info/im#", "im", "Information model")));
  }

  private static FilterOptionsDto options() {
    FilterOptionsDto dto = new FilterOptionsDto();
    dto.setStatus(new ArrayList<>(List.of(new TTIriRef("http://endhealth.info/im#Active", "Active"))));
    dto.setTypeSchemes(Map.of("http://endhealth.info/im#Concept", new ArrayList<>(List.of(new TTIriRef("http://endhealth.info/im#", "IM")))));
    return dto;
  }

  @Test
  void namespacesAreLoadedOnce() {
    AtomicInteger loads = new AtomicInteger();
    for (int i = 0; i < 3; i++) {
      assertThat(ReferenceDataCache.getNamespaces(() -> {
        loads.incrementAndGet();
        return namespaces();
      })).hasSize(1);
    }
    assertThat(loads).hasValue(1);
  }

  @Test
  void invalidateForcesReload() {
    AtomicInteger loads = new AtomicInteger();
    ReferenceDataCache.getNamespaces(() -> {
      loads.incrementAndGet();
      return namespaces();
    });
    ReferenceDataCache.invalidateAll();
    ReferenceDataCache.getNamespaces(() -> {
      loads.incrementAndGet();
      return namespaces();
    });
    assertThat(loads).hasValue(2);
  }

  @Test
  void callerMutationDoesNotAffectCache() {
    List<Namespace> first = ReferenceDataCache.getNamespaces(ReferenceDataCacheTest::namespaces);
    first.get(0).setName("changed");

    List<Namespace> second = ReferenceDataCache.getNamespaces(ReferenceDataCacheTest::namespaces);
    assertThat(second.get(0).getName()).isEqualTo("Information model");
  }

  @Test
  void filterOptionsAreCopiedAndLoadedOnce() {
    AtomicInteger loads = new AtomicInteger();
    FilterOptionsDto first = ReferenceDataCache.getFilterOptions(() -> {
      loads.incrementAndGet();
      return options();
    });
    first.getStatus().get(0).setName("changed");
    first.getStatus().clear();
    first.getTypeSchemes().values().forEach(List::clear);

    FilterOptionsDto second = ReferenceDataCache.getFilterOptions(() -> {
      loads.incrementAndGet();
      return options();
    });
    assertThat(loads).hasValue(1);
    assertThat(second.getStatus()).hasSize(1);
    assertThat(second.getStatus().get(0).getName()).isEqualTo("Active");
    assertThat(second.getTypeSchemes().get("http://endhealth.info/im#Concept")).hasSize(1);
  }

  @Test
  void optionsAndDefaultsAreCachedSeparately() {
    FilterOptionsDto opts = ReferenceDataCache.getFilterOptions(ReferenceDataCacheTest::options);
    FilterOptionsDto defaults = ReferenceDataCache.getFilterDefaults(FilterOptionsDto::new);

    assertThat(opts.getStatus()).hasSize(1);
    assertThat(defaults.getStatus()).isNull();
  }

  @Test
  void nullResultIsNotCached() {
    AtomicInteger loads = new AtomicInteger();
    assertThat(ReferenceDataCache.getFilterOptions(() -> {
      loads.incrementAndGet();
      return null;
    })).isNull();
    ReferenceDataCache.getFilterOptions(() -> {
      loads.incrementAndGet();
      return options();
    });
    assertThat(loads).hasValue(2);
  }
}
