package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class IndicatorGenerator {

  public TTEntity createIndicator(String iri, String name, String description,
                                  Namespace namespace,
                                  String denominator,
                                  String numerator,
                                  TTEntity dataset){
    TTEntity indicator= new TTEntity()
      .setIri(iri)
      .setName(name)
      .setDescription(description)
      .addType(iri(IM.INDICATOR));
    if (denominator!=null)
      indicator.set(iri(IM.DENOMINATOR).toString(),iri(denominator));
    if (numerator!=null)
      indicator.set(IM.NUMERATOR,iri(numerator));
    if (dataset!=null)
      indicator.set(IM.HAS_DATASET,dataset.get(IM.DEFINITION).asLiteral());
    return indicator;

  }
}
