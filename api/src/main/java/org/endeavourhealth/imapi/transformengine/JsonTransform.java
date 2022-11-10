package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.model.tripletree.*;

public class JsonTransform {
	public static JsonNode getValuefromPath(Object source, String path){
		String[] paths= path.split(" ");
		for (int i=0; i<paths.length; i++){
			String property= paths[i];
			if (((JsonNode) source).has(property)) {
				JsonNode value= ((JsonNode) source).get(property);
				if (i==paths.length-1){
						return value;
				}
				else
					source= value;
			}

		}
		return null;

	}



}
