package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IndicatorAction{
  private TTIriRef action;
  private List<TTIriRef> target;

  public TTIriRef getAction() {
    return action;
  }

  public IndicatorAction setAction(TTIriRef action) {
    this.action = action;
    return this;
  }

  public List<TTIriRef> getTarget() {
    return target;
  }

  public IndicatorAction setTarget(List<TTIriRef> target) {
    this.target = target;
    return this;
  }
  public IndicatorAction addTarget (TTIriRef target){
      if (this.target == null) {
        this.target = new ArrayList<>();
      }
      this.target.add(target);
      return this;
    }
    public IndicatorAction target (Consumer < TTIriRef > builder) {
      TTIriRef target = new TTIriRef();
      addTarget(target);
      builder.accept(target);
      return this;
    }


}
