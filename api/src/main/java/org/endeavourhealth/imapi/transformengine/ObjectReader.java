package org.endeavourhealth.imapi.transformengine;

import org.endeavourhealth.imapi.model.iml.Where;

import java.util.List;

public interface ObjectReader {

	Object getPropertyValue(Object source, String property, Where where);
}
