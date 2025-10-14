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
  private List<Indicator> and;
  @Getter
  private List<Indicator> or;
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




  public Indicator setOr(List<Indicator> alternative) {
    this.or = alternative;
    return this;
  }
  public Indicator addOr (Indicator indicator){
      if (this.or == null) {
        this.or = new ArrayList<>();
      }
      this.or.add(indicator);
      return this;
  }
  public Indicator or (Consumer< Indicator > builder) {
      Indicator indicator = new Indicator();
      addOr(indicator);
      builder.accept(indicator);
      return this;
  }



  public Indicator setQuery(TTIriRef query) {
    this.query = query;
    return this;
  }

  public Indicator setAnd(List<Indicator> and) {
    this.and = and;
    return this;
  }
  public Indicator addAnd (Indicator indicator){
      if (this.and == null) {
        this.and = new ArrayList<>();
      }
      this.and.add(indicator);
      return this;
  }
  public Indicator and (Consumer< Indicator > builder) {
      Indicator indicator = new Indicator();
      addAnd(indicator);
      builder.accept(indicator);
      return this;
    }





}
