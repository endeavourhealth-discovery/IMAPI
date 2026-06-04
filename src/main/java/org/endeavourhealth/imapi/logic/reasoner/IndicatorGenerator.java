package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
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
      .addType(new TTIriRefExtended(ImVocab. INDICATOR));
    if (denominator != null)
      indicator.set(new TTIriRefExtended(ImVocab. DENOMINATOR).toString(), new TTIriRefExtended(denominator));
    if (numerator != null)
      indicator.set(ImVocab. NUMERATOR, new TTIriRefExtended(numerator));
    if (dataset != null)
      indicator.set(ImVocab. HAS_DATASET, dataset.get(ImVocab. DEFINITION).asLiteral());
    return indicator;

  }
}
