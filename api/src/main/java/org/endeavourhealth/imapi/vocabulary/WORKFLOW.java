package org.endeavourhealth.imapi.vocabulary;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.im.GRAPH;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum WORKFLOW implements Vocabulary {
    DOMAIN("http://endhealth.info/"),
    NAMESPACE(DOMAIN.iri + "workflow#"),

    BUG_REPORT(NAMESPACE.iri + "bugReport"),
    DATE_CREATED(NAMESPACE.iri + "dateCreated"),
    CREATED_BY(NAMESPACE.iri + "createdBy"),
    ASSIGNED_TO(NAMESPACE.iri + "assignedTo"),
    STATE(NAMESPACE.iri + "state"),

    // bug report
    RELATED_PRODUCT(NAMESPACE.iri + "relatedProduct"),
    RELATED_MODULE(NAMESPACE.iri + "relatedModule"),
    OPERATING_SYSTEM(NAMESPACE.iri + "operatingSystem"),
    BROWSER(NAMESPACE.iri + "browser"),
    SEVERITY(NAMESPACE.iri + "severity"),
    ERROR(NAMESPACE.iri + "errorDetails"),
    REPRODUCE_STEPS(NAMESPACE.iri + "reproduceSteps"),
    EXPECTED_RESULT(NAMESPACE.iri + "expectedResult"),
    ACTUAL_RESULT(NAMESPACE.iri + "actualResult"),
    RELATED_VERSION(NAMESPACE.iri + "relatedVersion");

    public final String iri;

    WORKFLOW(String url) {
        this.iri = url;
    }

    @Override
    public TTIriRef asTTIriRef() {
        return iri(this.iri);
    }

    @Override
    @JsonValue
    public String getIri() {
        return iri;
    }

    public static boolean contains(String iri) {
        try {
            WORKFLOW.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return iri;
    }
}
