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
  private Bool operator;
  @Getter
  private TTIriRef query;
  @Getter
  private List<IndicatorAction> failAction;
  private List<IndicatorAction> successAction;
  private List<Indicator> stateIfTrue;
  private List<Indicator> stateIfFalse;
  private IndicatorType indicatorType;

  public IndicatorType getIndicatorType() {
    return indicatorType;
  }
  public Indicator setIndicatorType(IndicatorType indicatorType) {
    this.indicatorType = indicatorType;
    return this;
  }

  public List<Indicator> getStateIfTrue() {
    return stateIfTrue;
  }
  public Indicator setStateIfTrue(List<Indicator> stateIfTrue) {
    this.stateIfTrue = stateIfTrue;
    return this;
  }
  public Indicator addStateIfTrue (Indicator stateIfTrue){
      if (this.stateIfTrue == null) {
        this.stateIfTrue = new ArrayList<>();
      }
      this.stateIfTrue.add(stateIfTrue);
      return this;
  }

  public List<Indicator> getStateIfFalse() {
    return stateIfFalse;
  }
  public Indicator setStateIfFalse(List<Indicator> stateIfFalse) {
    this.stateIfFalse = stateIfFalse;
    return this;
  }
  public Indicator addStateIfFalse (Indicator stateIfFalse){
      if (this.stateIfFalse == null) {
        this.stateIfFalse = new ArrayList<>();
      }
      this.stateIfFalse.add(stateIfFalse);
      return this;
  }



  public Indicator setOperator(Bool operator) {
    this.operator = operator;
    return this;
  }

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


  public Indicator setFailAction(List<IndicatorAction> failAction) {
    this.failAction = failAction;
    return this;
  }
  public Indicator addFailAction (IndicatorAction failAction){
      if (this.failAction == null) {
        this.failAction = new ArrayList<>();
      }
      this.failAction.add(failAction);
      return this;
  }
  public Indicator failAction (Consumer < IndicatorAction > builder) {
      IndicatorAction failAction = new IndicatorAction();
      addFailAction(failAction);
      builder.accept(failAction);
      return this;
  }


  public List<IndicatorAction> getSuccessAction() {
    return successAction;
  }

  public Indicator setSuccessAction(List<IndicatorAction> successAction) {
    this.successAction = successAction;
    return this;
  }
  public Indicator addSuccessAction (IndicatorAction successAction){
      if (this.successAction == null) {
        this.successAction = new ArrayList<>();
      }
      this.successAction.add(successAction);
      return this;
  }
  public Indicator successAction (Consumer < IndicatorAction > builder) {
      IndicatorAction successAction = new IndicatorAction();
      addSuccessAction(successAction);
      builder.accept(successAction);
      return this;
  }


}
