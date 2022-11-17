package org.endeavourhealth.imapi.transformengine;

import org.endeavourhealth.imapi.model.iml.MapRule;
import org.endeavourhealth.imapi.model.iml.Where;

import java.util.List;
import java.util.zip.DataFormatException;

public interface ObjectFiler {



	void setPropertyValue(MapRule rule, Object targetEntity, String path, Object targetValue) throws DataFormatException;

	Object createEntity(String type);

}
