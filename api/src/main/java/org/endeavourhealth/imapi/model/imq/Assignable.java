package org.endeavourhealth.imapi.model.imq;

public interface Assignable {
	public Operator getOperator();

	public Assignable setOperator(Operator operator);

	public Property getRelativeTo();

	public Assignable setRelativeTo(Property relativeTo);


	public String getValue();

	public Assignable setValue(String value);

	public String getUnit();

	public Assignable setUnit(String unit);
}
