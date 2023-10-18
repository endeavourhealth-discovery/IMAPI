package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class USER {
    public static final String DOMAIN = "http://endhealth.info/";
    public static final String NAMESPACE = DOMAIN + "user#";
    public static final String PREFIX = "usr";
    // USER entries
    public static final TTIriRef USER_THEME = iri(USER.NAMESPACE + "UserTheme");
    public static final TTIriRef USER_MRU = iri(USER.NAMESPACE + "UserMRU");
    public static final TTIriRef USER_FAVOURITES = iri(USER.NAMESPACE + "UserFavourites");
    public static final TTIriRef ORGANISATIONS = iri(USER.NAMESPACE + "Organisations");
}