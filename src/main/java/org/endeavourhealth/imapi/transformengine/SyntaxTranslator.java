package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.map.MapProperty;

public interface SyntaxTranslator {
  Object convertToTarget(Object from);

  Object convertFromSource(Object from);

  void setPropertyValue(MapProperty rule, Object targetEntity, String path, Object targetValue);

  Object createEntity(String type);

  Object getPropertyValue(Object source, String property) throws JsonProcessingException;


  boolean isCollection(Object source);


}



