package org.eclipse.rdf4j.model.vocabulary;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;

public class SNOMED {
    public static final String NAMESPACE = "http://snomed.info/sct#";
    public static final String PREFIX = "sn";
    public static final Namespace NS = Vocabularies.createNamespace(PREFIX, NAMESPACE);
    public static final IRI IS_A = Vocabularies.createIRI(NAMESPACE, "116680003");
}
