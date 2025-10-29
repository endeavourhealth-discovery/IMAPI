package org.endeavourhealth.imapi.transform.qofimq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IriResolverTest {

  @Test
  void resolvesUsingCustomMapping_thenFallsBackToBuiltIn() {
    QofImqProperties p = new QofImqProperties();
    p.getNamespaceMappings().put("ABC", "http://example.com/abc#");
    IriResolver r = new IriResolver(p);

    TTIriRef iri1 = r.resolveField("ABC:test");
    assertEquals("http://example.com/abc#test", iri1.getIri());

    // Built-in QOF should work even without explicit mapping
    TTIriRef iri2 = r.resolveField("QOF:age");
    assertTrue(iri2.getIri().startsWith("http://endhealth.info/qof#"));
  }

  @Test
  void failsOnUnknownPrefixOrBadShape() {
    QofImqProperties p = new QofImqProperties();
    IriResolver r = new IriResolver(p);

    assertThrows(IriResolutionException.class, () -> r.resolveField("no_colon"));
    assertThrows(IriResolutionException.class, () -> r.resolveField("XYZ:thing"));
    assertThrows(IriResolutionException.class, () -> r.resolveField(" "));
  }
}
