package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.endeavourhealth.imapi.model.imq.Bool;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"name", "operator","indicator", "failAction", "successAction","iri","query"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Indicator extends TTIriRef {
  @Getter
  private List<Indicator> indicator;
  @Getter
  private TTIriRef query;




  public Indicator setQuery(TTIriRef query) {
    this.query = query;
    return this;
  }

  public Indicator setIndicator(List<Indicator> indicator) {
    this.indicator = indicator;
    return this;
  }
  public Indicator addIndicator (Indicator indicator){
      if (this.indicator == null) {
        this.indicator = new ArrayList<>();
      }
      this.indicator.add(indicator);
      return this;
  }
  public Indicator indicator (Consumer< Indicator > builder) {
      Indicator indicator = new Indicator();
      addIndicator(indicator);
      builder.accept(indicator);
      return this;
    }





}
