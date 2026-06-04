package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.imapi.model.tripletree.TTNode;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TTToHTMLTest {

  @org.junit.jupiter.api.Test
  void getExpressionText() {
    TTNode exp = new TTNode();
    TTArray inters = new TTArray();
    exp.set(new TTIriRefExtended(OwlVocab.INTERSECTION_OF), inters);
    TTIriRefExtended product = new TTIriRefExtended()
      .iri(NamespaceVocab. SNOMED + "763158003")
      .name("Medicinal product");
    inters.add(product);
    TTNode roleGroup = new TTNode();
    roleGroup.set(new TTIriRefExtended(NamespaceVocab. SNOMED + "127489000").name("Has active ingredient (attribute)"),
      new TTIriRefExtended(NamespaceVocab. SNOMED + "372665008").name("Non-steroidal anti-inflammatory agent (substance)"));
    roleGroup.set(new TTIriRefExtended(NamespaceVocab. SNOMED + "411116001").name("Has manufactured dose form (attribute)"),
      new TTIriRefExtended(NamespaceVocab. SNOMED + "385268001").name("Oral dose form (dose form)"));
    inters.add(roleGroup);
    String html = TTToHTML.getExpressionText(exp);
    System.out.println(html);
    assertNotNull(html);
  }
}
