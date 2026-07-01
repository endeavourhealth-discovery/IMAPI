package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.interfacemanager.model.Return;

import java.util.List;

public interface Returnable {
  List<Return> getReturn();

  Returnable setReturn(List<Return> returns);
}
