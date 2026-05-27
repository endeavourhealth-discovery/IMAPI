package org.endeavourhealth.imapi.springEnumMapping;

import org.endeavourhealth.interfacemanager.model.REPO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class REPOConverter implements Converter<String, REPO> {
  @Override
  public REPO convert(String source) {
    return REPO.Companion.decode(source);
  }
}
