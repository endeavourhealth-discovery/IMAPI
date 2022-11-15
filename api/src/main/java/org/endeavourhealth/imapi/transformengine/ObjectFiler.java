package org.endeavourhealth.imapi.transformengine;

import org.endeavourhealth.imapi.model.iml.Where;

import java.util.List;

public interface ObjectFiler {



	void setPropertyValue(Object targetEntity, String path, Object targetValue);

	Object createEntity(String type);

}
