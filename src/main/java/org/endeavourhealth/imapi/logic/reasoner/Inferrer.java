package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTEntityMap;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class Inferrer {

  private void inheritDomains(TTEntity property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(iri(RDFS.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntity superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(iri(RDFS.DOMAIN)) != null)
        superEntity.get(iri(RDFS.DOMAIN)).getElements().forEach(dom -> property.addObject(iri(RDFS.DOMAIN), dom));
    }
  }

  private void inheritRanges(TTEntity property, TTEntityMap propertyMap) {
    for (TTValue superProp : property.get(iri(RDFS.SUBCLASS_OF)).getElements()) {
      TTIriRef superIri = superProp.asIriRef();
      TTEntity superEntity = propertyMap.getEntity(superIri.getIri());
      inheritDomains(superEntity, propertyMap);
      if (superEntity.get(iri(RDFS.RANGE)) != null)
        superEntity.get(iri(RDFS.RANGE)).getElements().forEach(dom -> property.addObject(iri(RDFS.RANGE), dom));
    }
  }


  public void inheritDomRans(TTEntity property, TTEntityMap propertyMap) {
    inheritDomains(property, propertyMap);
    inheritRanges(property, propertyMap);

  }
}
