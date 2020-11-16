package org.endeavourhealth.imapi.model;

public interface Quantification {
   QuantificationType getQuantificationType();
   Object setQuantification(QuantificationType qtype);
   Object setQuantification(QuantificationType qtype, int exact);
   Object setQuantification(QuantificationType qtype, int min, int max);
   Integer getMin();
   Object setMin(Integer min);
   Integer getMax();
   Object setMax(Integer max);

}
