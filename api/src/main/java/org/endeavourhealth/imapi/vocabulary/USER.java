package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum USER implements Vocabulary {
    DOMAIN("http://endhealth.info/"),
    NAMESPACE(DOMAIN.iri + "user#"),
    PREFIX("usr"),
    // USER entries
    USER_THEME(NAMESPACE.iri + "UserTheme"),
    USER_MRU(NAMESPACE.iri + "UserMRU"),
    USER_FAVOURITES(NAMESPACE.iri + "UserFavourites"),
    ORGANISATIONS(NAMESPACE.iri + "Organisations");

    public final String iri;
    USER(String iri) {
        this.iri = iri;
    }

    @Override
    public TTIriRef asTTIriRef() {
        return iri(this.iri);
    }

    @Override
    public String getIri() {
        return iri;
    }

    public static boolean contains(String iri) {
        try {
            USER.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}