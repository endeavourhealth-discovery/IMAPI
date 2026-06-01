package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.IM;
import org.endeavourhealth.interfacemanager.model.NAMESPACE;

public class IndicatorGenerator {

  public TTEntity createIndicator(String iri, String name, String description,
                                  NAMESPACE namespace,
                                  String denominator,
                                  String numerator,
                                  TTEntity dataset) {
    TTEntity indicator = new TTEntity()
      .setIri(iri)
      .setName(name)
      .setDescription(description)
      .setScheme(EnumUtils.asIri(namespace))
      .addType(new TTIriRef(IM.INDICATOR));
    if (denominator != null)
      indicator.set(new TTIriRef(IM.DENOMINATOR).toString(), new TTIriRef(denominator));
    if (numerator != null)
      indicator.set(IM.NUMERATOR, new TTIriRef(numerator));
    if (dataset != null)
      indicator.set(IM.HAS_DATASET, dataset.get(IM.DEFINITION).asLiteral());
    return indicator;

  }
}
