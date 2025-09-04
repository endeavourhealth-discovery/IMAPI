package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"name", "operator","indicator", "failAction", "successAction","iri","query"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Indicator extends TTIriRef {
  @Getter
  private List<Indicator> must;
  @Getter
  private List<Indicator> alternative;
  @Getter
  private TTIriRef query;
  @Getter
  @Setter
  private Query definition;
  @Getter
  @Setter
  private List<TTIriRef> actionIfFalse;
  @Getter
  @Setter
  private List<TTIriRef> actionIfTrue;




  public Indicator setAlternative(List<Indicator> alternative) {
    this.alternative = alternative;
    return this;
  }
  public Indicator addAlternative (Indicator indicator){
      if (this.alternative == null) {
        this.alternative = new ArrayList<>();
      }
      this.alternative.add(indicator);
      return this;
  }
  public Indicator alternative (Consumer< Indicator > builder) {
      Indicator indicator = new Indicator();
      addAlternative(indicator);
      builder.accept(indicator);
      return this;
  }



  public Indicator setQuery(TTIriRef query) {
    this.query = query;
    return this;
  }

  public Indicator setMust(List<Indicator> must) {
    this.must = must;
    return this;
  }
  public Indicator addMust (Indicator indicator){
      if (this.must == null) {
        this.must = new ArrayList<>();
      }
      this.must.add(indicator);
      return this;
  }
  public Indicator must (Consumer< Indicator > builder) {
      Indicator indicator = new Indicator();
      addMust(indicator);
      builder.accept(indicator);
      return this;
    }





}
