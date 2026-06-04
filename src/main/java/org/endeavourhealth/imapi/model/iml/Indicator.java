package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"name", "cohort","query", "outputDefinition", "successAction","iri","query"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Indicator extends TTIriRefExtended {
  @Getter
  private List<TTIriRefExtended> isSubIndicatorOf;
  @Getter
  private TTIriRefExtended numerator;
  @Getter
  private TTIriRefExtended dataset;
  @Getter
  @Setter
  private List<TTIriRefExtended> actionIfFalse;
  @Getter
  @Setter
  private List<TTIriRefExtended> actionIfTrue;
  @Getter
  private TTIriRefExtended denominator;

  public Indicator setDataset(TTIriRefExtended dataset) {
    this.dataset = dataset;
    return this;
  }
  public Indicator setDenominator(TTIriRefExtended denominator) {
    this.denominator = denominator;
    return this;
  }

  public Indicator setnumerator(TTIriRefExtended numerator) {
    this.numerator = numerator;
    return this;
  }





  public Indicator setIsSubIndicatorOf(List<TTIriRefExtended> isSubIndicatorOf) {
    this.isSubIndicatorOf = isSubIndicatorOf;
    return this;
  }

  public Indicator addSubIndicatorOf(TTIriRefExtended indicator) {
    if (this.isSubIndicatorOf == null) {
      this.isSubIndicatorOf = new ArrayList<>();
    }
    this.isSubIndicatorOf.add(indicator);
    return this;
  }
}


