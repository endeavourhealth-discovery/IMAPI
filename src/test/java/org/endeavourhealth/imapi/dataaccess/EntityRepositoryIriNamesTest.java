package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.impl.MapBindingSet;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EntityRepositoryIriNamesTest {

  private static MapBindingSet row(String iri, String label, String description) {
    MapBindingSet bs = new MapBindingSet();
    bs.addBinding("iri", Values.iri(iri));
    bs.addBinding("label", Values.literal(label));
    if (description != null) bs.addBinding("description", Values.literal(description));
    return bs;
  }

  @Test
  void namesAndDescriptionsAreSetOnTheMatchingReferences() {
    // distinct iris from other tests: the name cache is static
    TTIriRef a = new TTIriRef("http://example.org/iriNamesTest#a");
    TTIriRef b = new TTIriRef("http://example.org/iriNamesTest#b");
    TTIriRef c = new TTIriRef("http://example.org/iriNamesTest#c");
    Set<TTIriRef> iris = new LinkedHashSet<>(Set.of(a, b, c));

    IMDB conn = mock(IMDB.class);
    TupleQuery query = mock(TupleQuery.class);
    TupleQueryResult result = mock(TupleQueryResult.class);
    when(conn.prepareTupleSparql(anyString())).thenReturn(query);
    when(query.evaluate()).thenReturn(result);
    when(result.hasNext()).thenReturn(true, true, true, false);
    when(result.next()).thenReturn(
      row("http://example.org/iriNamesTest#b", "B label", "B description"),
      row("http://example.org/iriNamesTest#a", "A label", null),
      row("http://example.org/iriNamesTest#unrequested", "ignored", null));

    EntityRepository.getIriNames(conn, iris);

    assertThat(a.getName()).isEqualTo("A label");
    assertThat(a.getDescription()).isNull();
    assertThat(b.getName()).isEqualTo("B label");
    assertThat(b.getDescription()).isEqualTo("B description");
    assertThat(c.getName()).isNull();
  }

  @Test
  void cachedNamesDoNotTriggerAnotherQuery() {
    TTIriRef first = new TTIriRef("http://example.org/iriNamesTest#cached");
    IMDB conn = mock(IMDB.class);
    TupleQuery query = mock(TupleQuery.class);
    TupleQueryResult result = mock(TupleQueryResult.class);
    when(conn.prepareTupleSparql(anyString())).thenReturn(query);
    when(query.evaluate()).thenReturn(result);
    when(result.hasNext()).thenReturn(true, false);
    when(result.next()).thenReturn(row("http://example.org/iriNamesTest#cached", "Cached label", null));
    EntityRepository.getIriNames(conn, Set.of(first));

    TTIriRef second = new TTIriRef("http://example.org/iriNamesTest#cached");
    IMDB otherConn = mock(IMDB.class);
    EntityRepository.getIriNames(otherConn, Set.of(second));

    assertThat(second.getName()).isEqualTo("Cached label");
    verifyNoInteractions(otherConn);
  }
}
