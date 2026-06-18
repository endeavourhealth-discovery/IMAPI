package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.library.vocabulary.NAMESPACE;
import org.endeavourhealth.library.vocabulary.OWL;
import org.endeavourhealth.library.model.tripletree.TTArray;
import org.endeavourhealth.library.model.tripletree.TTIriRef;
import org.endeavourhealth.library.model.tripletree.TTNode;

import static org.endeavourhealth.library.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TTToHTMLTest {

  @org.junit.jupiter.api.Test
  void getExpressionText() {
    TTNode exp = new TTNode();
    TTArray inters = new TTArray();
    exp.set(iri(OWL.INTERSECTION_OF), inters);
    TTIriRef product = new TTIriRef()
      .setIri(NAMESPACE.SNOMED + "763158003")
      .setName("Medicinal product");
    inters.add(product);
    TTNode roleGroup = new TTNode();
    roleGroup.set(iri(NAMESPACE.SNOMED + "127489000").setName("Has active ingredient (attribute)"),
      iri(NAMESPACE.SNOMED + "372665008").setName("Non-steroidal anti-inflammatory agent (substance)"));
    roleGroup.set(iri(NAMESPACE.SNOMED + "411116001").setName("Has manufactured dose form (attribute)"),
      iri(NAMESPACE.SNOMED + "385268001").setName("Oral dose form (dose form)"));
    inters.add(roleGroup);
    String html = TTToHTML.getExpressionText(exp);
    System.out.println(html);
    assertNotNull(html);
  }
}
