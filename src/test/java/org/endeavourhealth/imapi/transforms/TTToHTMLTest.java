package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.interfacemanager.model.NAMESPACE;
import org.endeavourhealth.interfacemanager.model.OWL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TTToHTMLTest {

  @org.junit.jupiter.api.Test
  void getExpressionText() {
    TTNode exp = new TTNode();
    TTArray inters = new TTArray();
    exp.set(new TTIriRef(OWL.INTERSECTION_OF), inters);
    TTIriRef product = new TTIriRef()
      .iri(NAMESPACE.SNOMED + "763158003")
      .name("Medicinal product");
    inters.add(product);
    TTNode roleGroup = new TTNode();
    roleGroup.set(new TTIriRef(NAMESPACE.SNOMED + "127489000").name("Has active ingredient (attribute)"),
      new TTIriRef(NAMESPACE.SNOMED + "372665008").name("Non-steroidal anti-inflammatory agent (substance)"));
    roleGroup.set(new TTIriRef(NAMESPACE.SNOMED + "411116001").name("Has manufactured dose form (attribute)"),
      new TTIriRef(NAMESPACE.SNOMED + "385268001").name("Oral dose form (dose form)"));
    inters.add(roleGroup);
    String html = TTToHTML.getExpressionText(exp);
    System.out.println(html);
    assertNotNull(html);
  }
}
