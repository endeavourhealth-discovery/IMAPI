package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.iml.ListMode;
import org.endeavourhealth.imapi.model.iml.MapRule;

import java.util.zip.DataFormatException;

public interface SyntaxTranslator {
	Object convertToTarget(Object from) throws DataFormatException;

	Object convertFromSource(Object from) throws DataFormatException;

	void setPropertyValue(MapRule rule, Object targetEntity, String path, Object targetValue) throws DataFormatException;

	Object createEntity(String type);

	Object getPropertyValue(Object source, String property) throws DataFormatException, JsonProcessingException;

	Object getListItems(Object source, ListMode listMode) throws DataFormatException;

	boolean isCollection(Object source);


}



