package org.endeavourhealth.imapi.model.imq;

import java.util.ArrayList;
import java.util.Optional;

public class Prefixes extends ArrayList<Prefix> {

	public String getNamespace(String prefix){
		Optional<Prefix> result = this.stream()
			.filter(obj -> obj.getPrefix().equals(prefix))
			.findFirst();
		return result.map(Prefix::getNamespace).orElse(null);
	}

	public String getPrefix(String namespace){
		Optional<Prefix> result = this.stream()
			.filter(obj -> obj.getNamespace().equals(namespace))
			.findFirst();
		return result.map(Prefix::getPrefix).orElse(null);
	}
}
