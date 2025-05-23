package org.endeavourhealth.imapi.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UIProperty {
  private String iri;
  private String name;
  private String propertyType;
  private String valueType;
  private int maxCount;
  private int number;
  private String valueLabel;

  private String intervalUnitIri;
  private List<TTIriRef> intervalUnitOptions;

  private String unitIri;
  private List<TTIriRef> unitOptions;

  private String operatorIri;
  private List<String> operatorOptions;

  private List<TTIriRef> qualifierOptions;

  public UIProperty() {
  }

  public UIProperty addQualifierOption(String iri, String name) {
    if (this.qualifierOptions == null) {
      this.qualifierOptions = new ArrayList<>();
    }
    this.qualifierOptions.add(new TTIriRef(iri, name));
    return this;
  }
}
