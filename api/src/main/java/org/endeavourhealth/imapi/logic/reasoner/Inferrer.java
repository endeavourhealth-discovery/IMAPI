package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTEntityMap;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.RDFS;

public class Inferrer {

	private void inheritDomains(TTEntity property, TTEntityMap propertyMap) {
		for (TTValue superProp:property.get(RDFS.SUBCLASSOF).getElements()){
			TTIriRef superIri= superProp.asIriRef();
			TTEntity superEntity= propertyMap.getEntity(superIri.getIri());
			inheritDomains(superEntity, propertyMap);
			if (superEntity.get(RDFS.DOMAIN)!=null)
				superEntity.get(RDFS.DOMAIN).getElements().forEach(dom-> property.addObject(RDFS.DOMAIN,dom));
		}
	}

	private void inheritRanges(TTEntity property, TTEntityMap propertyMap) {
		for (TTValue superProp:property.get(RDFS.SUBCLASSOF).getElements()){
			TTIriRef superIri= superProp.asIriRef();
			TTEntity superEntity= propertyMap.getEntity(superIri.getIri());
			inheritDomains(superEntity, propertyMap);
			if (superEntity.get(RDFS.RANGE)!=null)
				superEntity.get(RDFS.RANGE).getElements().forEach(dom-> property.addObject(RDFS.RANGE,dom));
		}
	}


	public void inheritDomRans(TTEntity property,TTEntityMap propertyMap){
		inheritDomains(property,propertyMap);
		inheritRanges(property,propertyMap);

	}
}
