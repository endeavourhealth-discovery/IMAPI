package org.endeavourhealth.imapi.model.imq;

import java.util.List;

public interface Returnable {
  List<Return> getReturn();

  Returnable setReturn(List<Return> returns);
}
