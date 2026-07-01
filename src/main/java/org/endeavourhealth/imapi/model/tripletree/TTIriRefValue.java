package org.endeavourhealth.imapi.model.tripletree;

import org.endeavourhealth.interfacemanager.model.TTIriRef;

public class TTIriRefValue extends TTIriRef implements TTValueJava {
  @Override
  public TTIriRef asIriRef() {
    return this;
  }

  @Override
  public boolean isIriRef() {
    return true;
  }
}
