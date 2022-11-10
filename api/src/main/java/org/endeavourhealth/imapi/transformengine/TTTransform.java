package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.*;

public class TTTransform {

	public static Object createEntity(String type){
		TTEntity target= new TTEntity();
		target.addType(TTIriRef.iri(type));

		return target;
	}
	public static void setValueWithPath(Object targetEntity, String path, Object targetValue) throws JsonProcessingException {
		String[] paths = path.split(" ");
		for (int i = 0; i < paths.length; i++) {
			String predicate = paths[i];
			if (i == paths.length - 1) {
				if (path.equals("@id"))
					((TTNode) targetEntity).setIri(String.valueOf(targetValue));
				else {
					try {
						TTValue targetTTValue;
						if (targetValue instanceof String)
							targetTTValue = TTLiteral.literal(targetValue);
						else if (targetValue instanceof Number) {
							((TTNode) targetEntity).set(TTIriRef.iri(predicate), TTLiteral.literal(targetValue));
						}
					} catch (JsonProcessingException e) {
						throw new RuntimeException("Unable to transform source to target literal");
					}
				}
			} else {
				TTNode targetNode = new TTNode();
				((TTNode) targetEntity).addObject(TTIriRef.iri(predicate), targetNode);
				targetEntity = targetNode;
			}
		}
	}


}
