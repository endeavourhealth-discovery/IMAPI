package org.endeavourhealth.imapi.model.imq;

public interface Sortable {
	String getLatest() ;

	Sortable setLatest(String latest);

	String getEarliest();

	Sortable setEarliest(String earliest);

	String getMaximum();

	Sortable setMaximum( String maximum);

	String getMinimum();

	Sortable setMinimum(String minimum);

	Integer getCount();
}
