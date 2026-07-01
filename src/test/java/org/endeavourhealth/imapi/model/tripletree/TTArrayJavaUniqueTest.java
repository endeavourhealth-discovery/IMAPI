package org.endeavourhealth.imapi.model.tripletree;

import org.junit.jupiter.api.Test;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteralJava.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TTArrayJavaUniqueTest {
  @Test
  void differentObjectDifferentValue_Iri() {
    TTArrayJava actual = new TTArrayJava();
    actual.add(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://example.org#SAME"));
    actual.add(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://example.org#DIFFERENT"));

    assertEquals(2, actual.size());
  }

  @Test
  void sameObjectSameValue_Iri() {
    TTIriRef testIri = TTIriRefExtensionsKt.iri(new TTIriRef(), "http://example.org#SAME");

    TTArrayJava actual = new TTArrayJava();

    actual.add(testIri);
    assertEquals(1, actual.size());
    actual.add(testIri);
    assertEquals(1, actual.size());
  }

  @Test
  void differentObjectSameValue_Iri() {
    TTArrayJava actual = new TTArrayJava();
    actual.add(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://example.org#SAME"));
    actual.add(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://example.org#SAME"));

    assertEquals(1, actual.size());
  }

  @Test
  void differentObjectDifferentValue_Literal() {
    TTArrayJava actual = new TTArrayJava();
    actual.add(literal("SAME"));
    actual.add(literal("DIFFERENT"));

    assertEquals(2, actual.size());
  }

  @Test
  void differentObjectSameValue_Literal() {
    TTArrayJava actual = new TTArrayJava();
    actual.add(literal("SAME"));
    actual.add(literal("SAME"));

    assertEquals(1, actual.size());
  }


  @Test
  void sameObjectSameValue_Literal() {
    TTLiteralJava lit = literal("SAME");

    TTArrayJava actual = new TTArrayJava();
    actual.add(lit);
    actual.add(lit);

    assertEquals(1, actual.size());
  }
}
