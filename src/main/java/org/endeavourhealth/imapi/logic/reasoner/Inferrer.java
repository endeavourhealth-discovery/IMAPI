package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTEntityMap;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.interfacemanager.model.RdfsVocab;

public class Inferrer {

  private void inheritDomains(TTEntity property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(new TTIriRefExtended(RdfsVocab.SUBCLASS_OF)).getElements()) {
      TTIriRefExtended superIri = superProp.asIriRef();
      TTEntity superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(new TTIriRefExtended(RdfsVocab.DOMAIN)) != null) {
        superEntity.get(new TTIriRefExtended(RdfsVocab.DOMAIN)).
          getElements().forEach(dom -> property.addObject(new TTIriRefExtended(RdfsVocab.DOMAIN), dom));
      }
    }
  }

  private void inheritRanges(TTEntity property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(new TTIriRefExtended(RdfsVocab.SUBCLASS_OF)).getElements()) {
      TTIriRefExtended superIri = superProp.asIriRef();
      TTEntity superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(new TTIriRefExtended(RdfsVocab.RANGE)) != null) {
        superEntity.get(new TTIriRefExtended(RdfsVocab.RANGE)).
          getElements().forEach(dom -> property.addObject(new TTIriRefExtended(RdfsVocab.RANGE), dom));
      }
    }
  }


  public void inheritDomRans(TTEntity property, TTEntityMap propertyMap) {
    inheritDomains(property, propertyMap);
    inheritRanges(property, propertyMap);

  }
}
