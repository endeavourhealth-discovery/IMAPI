package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTEntityMap;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.RDFS;

public class Inferrer {

	private void inheritDomains(TTEntity property, TTEntityMap propertyMap) {
		for (TTValue superProp:property.get(RDFS.SUBCLASSOF.asTTIriRef()).getElements()){
			TTIriRef superIri= superProp.asIriRef();
			TTEntity superEntity= propertyMap.getEntity(superIri.getIri());
			inheritDomains(superEntity, propertyMap);
			if (superEntity.get(RDFS.DOMAIN.asTTIriRef())!=null)
				superEntity.get(RDFS.DOMAIN.asTTIriRef()).getElements().forEach(dom-> property.addObject(RDFS.DOMAIN.asTTIriRef(),dom));
		}
	}

	private void inheritRanges(TTEntity property, TTEntityMap propertyMap) {
		for (TTValue superProp:property.get(RDFS.SUBCLASSOF.asTTIriRef()).getElements()){
			TTIriRef superIri= superProp.asIriRef();
			TTEntity superEntity= propertyMap.getEntity(superIri.getIri());
			inheritDomains(superEntity, propertyMap);
			if (superEntity.get(RDFS.RANGE.asTTIriRef())!=null)
				superEntity.get(RDFS.RANGE.asTTIriRef()).getElements().forEach(dom-> property.addObject(RDFS.RANGE.asTTIriRef(),dom));
		}
	}


	public void inheritDomRans(TTEntity property,TTEntityMap propertyMap){
		inheritDomains(property,propertyMap);
		inheritRanges(property,propertyMap);

	}
}
