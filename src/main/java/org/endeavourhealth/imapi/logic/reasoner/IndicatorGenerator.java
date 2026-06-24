package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.endeavourhealth.imapi.utility.EnumUtils;

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
      .addType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.INDICATOR));
    if (denominator != null)
      indicator.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DENOMINATOR).toString(), TTIriRefExtensionsKt.iri(new TTIriRef(), denominator));
    if (numerator != null)
      indicator.set(ImVocab.NUMERATOR, TTIriRefExtensionsKt.iri(new TTIriRef(), numerator));
    if (dataset != null)
      indicator.set(ImVocab.HAS_DATASET, dataset.get(ImVocab.DEFINITION).asLiteral());
    return indicator;

  }
}
