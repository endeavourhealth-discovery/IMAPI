package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TTToHTMLTest {

  @org.junit.jupiter.api.Test
  void getExpressionText() {
    TTNodeJava exp = new TTNodeJava();
    TTArrayJava inters = new TTArrayJava();
    exp.set(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF), inters);
    TTIriRef product = TTIriRefExtensionsKt.iri(new TTIriRef(), )
      .iri(NamespaceVocab.SNOMED + "763158003")
      .name("Medicinal product");
    inters.add(product);
    TTNodeJava roleGroup = new TTNodeJava();
    roleGroup.set(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.SNOMED + "127489000").name("Has active ingredient (attribute)"),
      TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.SNOMED + "372665008").name("Non-steroidal anti-inflammatory agent (substance)"));
    roleGroup.set(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.SNOMED + "411116001").name("Has manufactured dose form (attribute)"),
      TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.SNOMED + "385268001").name("Oral dose form (dose form)"));
    inters.add(roleGroup);
    String html = TTToHTML.getExpressionText(exp);
    System.out.println(html);
    assertNotNull(html);
  }
}
