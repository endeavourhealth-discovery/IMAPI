package org.endeavourhealth.imapi.utility;

import ca.uhn.fhir.context.FhirContext;

/**
 * Lazily created, shared FhirContext. Creating a FhirContext is expensive; it is thread-safe and meant to be reused.
 */
public final class FhirContextHolder {
  private static final class Holder {
    private static final FhirContext R4 = FhirContext.forR4();
  }

  private FhirContextHolder() {
  }

  public static FhirContext r4() {
    return Holder.R4;
  }
}
