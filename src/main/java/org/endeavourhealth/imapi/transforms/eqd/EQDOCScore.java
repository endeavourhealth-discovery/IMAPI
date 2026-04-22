package org.endeavourhealth.imapi.transforms.eqd;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EQDOC.Score", propOrder = {
  "rangeValue"
})


public class EQDOCScore {
  protected EQDOCRangeValue rangeValue;
  public EQDOCRangeValue getRangeValue() {
    return rangeValue;
  }
  public void setRangeValue(EQDOCRangeValue rangeValue) {
    this.rangeValue = rangeValue;
  }
}

