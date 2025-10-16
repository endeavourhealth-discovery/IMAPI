package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"name", "cohort","query", "outputDefinition", "successAction","iri","query"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Indicator extends TTIriRef {
  @Getter
  private List<TTIriRef> isSubIndicatorOf;
  @Getter
  private TTIriRef enumerator;
  @Getter
  private Query dataset;
  @Getter
  @Setter
  private List<TTIriRef> actionIfFalse;
  @Getter
  @Setter
  private List<TTIriRef> actionIfTrue;
  @Getter
  private TTIriRef denominator;

  public Indicator setDataset(Query dataset) {
    this.dataset = dataset;
    return this;
  }
  public Indicator setDenominator(TTIriRef denominator) {
    this.denominator = denominator;
    return this;
  }

  public Indicator setEnumerator(TTIriRef enumerator) {
    this.enumerator = enumerator;
    return this;
  }





  public Indicator setIsSubIndicatorOf(List<TTIriRef> isSubIndicatorOf) {
    this.isSubIndicatorOf = isSubIndicatorOf;
    return this;
  }

  public Indicator addSubIndicatorOf(TTIriRef indicator) {
    if (this.isSubIndicatorOf == null) {
      this.isSubIndicatorOf = new ArrayList<>();
    }
    this.isSubIndicatorOf.add(indicator);
    return this;
  }
}


