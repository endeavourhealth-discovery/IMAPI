package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTEntityMap;
import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.interfacemanager.model.RdfsVocab;

public class Inferrer {

  private void inheritDomains(TTEntity property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntity superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN)) != null) {
        superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN)).
          getElements().forEach(dom -> property.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.DOMAIN), dom));
      }
    }
  }

  private void inheritRanges(TTEntity property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntity superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.RANGE)) != null) {
        superEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.RANGE)).
          getElements().forEach(dom -> property.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.RANGE), dom));
      }
    }
  }


  public void inheritDomRans(TTEntity property, TTEntityMap propertyMap) {
    inheritDomains(property, propertyMap);
    inheritRanges(property, propertyMap);

  }
}
