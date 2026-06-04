package org.endeavourhealth.imapi.model.iml;

import lombok.Getter;
import org.endeavourhealth.interfacemanager.model.ArrayButtons;

@Getter
public class ArrayButtonsExtended extends ArrayButtons {

  @Override
  public ArrayButtonsExtended up(Boolean up) {
    this.setUp(up);
    return this;
  }

  @Override
  public ArrayButtonsExtended down(Boolean down) {
    this.setDown(down);
    return this;
  }

  @Override
  public ArrayButtonsExtended plus(Boolean plus) {
    this.setPlus(plus);
    return this;
  }

  @Override
  public ArrayButtonsExtended minus(Boolean minus) {
    this.setMinus(minus);
    return this;
  }

  @Override
  public ArrayButtonsExtended addOnlyIfLast(Boolean addOnlyIfLast) {
    this.setAddOnlyIfLast(addOnlyIfLast);
    return this;
  }

}
