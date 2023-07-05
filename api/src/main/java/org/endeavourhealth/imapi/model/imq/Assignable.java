package org.endeavourhealth.imapi.model.imq;

public interface Assignable {
	public Operator getOperator();

	public Assignable setOperator(Operator operator);

	public PropertyRef getRelativeTo();

	public Assignable setRelativeTo(PropertyRef relativeTo);


	public String getValue();

	public Assignable setValue(String value);

	public String getUnit();

	public Assignable setUnit(String unit);
}
