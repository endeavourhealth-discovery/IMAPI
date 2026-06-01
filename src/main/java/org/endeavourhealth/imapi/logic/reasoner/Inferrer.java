package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTEntityMap;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.interfacemanager.model.RDFS;

public class Inferrer {

  private void inheritDomains(TTEntity property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(new TTIriRef(RDFS.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntity superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(new TTIriRef(RDFS.DOMAIN)) != null)
        superEntity.get(new TTIriRef(RDFS.DOMAIN)).getElements().forEach(dom -> property.addObject(new TTIriRef(RDFS.DOMAIN), dom));
    }
  }

  private void inheritRanges(TTEntity property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(new TTIriRef(RDFS.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntity superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(new TTIriRef(RDFS.RANGE)) != null)
        superEntity.get(new TTIriRef(RDFS.RANGE)).getElements().forEach(dom -> property.addObject(new TTIriRef(RDFS.RANGE), dom));
    }
  }


  public void inheritDomRans(TTEntity property, TTEntityMap propertyMap) {
    inheritDomains(property, propertyMap);
    inheritRanges(property, propertyMap);

  }
}
