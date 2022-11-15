package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.*;

public class TripleTreeFiler implements ObjectFiler {

	public Object createEntity(String type){
		TTEntity target= new TTEntity();
		target.addType(TTIriRef.iri(type));

		return target;
	}


	@Override
	public void setPropertyValue(Object targetEntity, String property, Object targetValue) throws RuntimeException{
				if (property.equals("@id")|property.equals("id")|property.equals("iri"))
					((TTNode) targetEntity).setIri(String.valueOf(targetValue));
				else {
					try {
						if (targetValue instanceof String|(targetValue instanceof Number))
							((TTNode) targetEntity).set(TTIriRef.iri(property),TTLiteral.literal(targetValue));
						else {
							TTNode node= new TTNode();
							((TTNode) targetEntity).set(TTIriRef.iri(property),node);
						}
					} catch (JsonProcessingException e) {
						throw new RuntimeException("Unable to transform source to target literal");
					}
				}
	}
}
