package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityMap;
import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValueJava;
import org.endeavourhealth.interfacemanager.model.RdfsVocab;

public class Inferrer {

  private void inheritDomains(TTEntityJava property, TTEntityMap propertyMap) {
    for (TTValueJava superProp : property.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntityJava superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN)) != null) {
        superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN)).
          getElements().forEach(dom -> property.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN), dom));
      }
    }
  }

  private void inheritRanges(TTEntityJava property, TTEntityMap propertyMap) {
    for (TTValueJava superProp : property.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntityJava superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.RANGE)) != null) {
        superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.RANGE)).
          getElements().forEach(dom -> property.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.RANGE), dom));
      }
    }
  }


  public void inheritDomRans(TTEntityJava property, TTEntityMap propertyMap) {
    inheritDomains(property, propertyMap);
    inheritRanges(property, propertyMap);

  }
}
