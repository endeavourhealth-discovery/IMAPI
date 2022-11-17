package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.iml.MapRule;
import org.endeavourhealth.imapi.model.iml.TargetUpdateMode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.zip.DataFormatException;

public class TripleTreeFiler implements ObjectFiler {

	public Object createEntity(String type){
		TTEntity target= new TTEntity();
		target.addType(TTIriRef.iri(type));

		return target;
	}


	@Override
	public void setPropertyValue(MapRule rule, Object targetEntity, String property, Object targetValue) throws DataFormatException {
		    if (property.equals("@id")|property.equals("id")|property.equals("iri"))
					((TTNode) targetEntity).setIri(((TTLiteral) targetValue).getValue());
				else {
					String predicate= property;
					if (!property.contains(":"))
						predicate= IM.NAMESPACE+property;
					if (targetValue instanceof TTArray)
						((TTNode) targetEntity).set(TTIriRef.iri(predicate),(TTArray) targetValue);
					else if (targetValue instanceof TTValue) {
						if (rule.getTargetUpdateMode()== TargetUpdateMode.ADDTOLIST){
							((TTNode) targetEntity).addObject(TTIriRef.iri(predicate), (TTValue) targetValue);
						}
						else
							((TTNode) targetEntity).set(TTIriRef.iri(predicate), (TTValue) targetValue);
					}
					else
						throw new DataFormatException("Value of property : "+property+" cannot be set as its class is invalid ("+ targetValue.getClass().getSimpleName()+")");
				}
	}
}
