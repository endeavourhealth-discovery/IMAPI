package org.endeavourhealth.imapi.model.imq;

import java.util.List;

public interface BoolGroup<T extends BoolGroup<T>> {
  List<T> getOr();
  List<T> getAnd();
  List<T> getNot();

  T setOr(List<T> ors);
  T setAnd(List<T> ands);
  T setNot(List<T> nots);
}

