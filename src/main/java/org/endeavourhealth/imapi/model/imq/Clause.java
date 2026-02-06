package org.endeavourhealth.imapi.model.imq;

import java.util.List;

public interface Clause<T> {
  List<T> getAnd();
  Clause<T> setAnd(List<T> and);
  List<T> getOr();
  Clause<T> setOr(List<T> or);
}
