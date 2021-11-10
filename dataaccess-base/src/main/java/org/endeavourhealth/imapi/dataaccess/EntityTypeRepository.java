package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.tripletree.TTArray;

public interface EntityTypeRepository {
    TTArray getEntityTypes(String iri);
}
