package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.interfacemanager.model.NAMESPACE;
import org.endeavourhealth.interfacemanager.model.OWL;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
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
    roleGroup.set(TTIriRef.iri(NAMESPACE.SNOMED + "127489000").setName("Has active ingredient (attribute)"),
      TTIriRef.iri(NAMESPACE.SNOMED + "372665008").setName("Non-steroidal anti-inflammatory agent (substance)"));
    roleGroup.set(TTIriRef.iri(NAMESPACE.SNOMED + "411116001").setName("Has manufactured dose form (attribute)"),
      TTIriRef.iri(NAMESPACE.SNOMED + "385268001").setName("Oral dose form (dose form)"));
    inters.add(roleGroup);
    String html = TTToHTML.getExpressionText(exp);
    System.out.println(html);
    assertNotNull(html);
  }
}
