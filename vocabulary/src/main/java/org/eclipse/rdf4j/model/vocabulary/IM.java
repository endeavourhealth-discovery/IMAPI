package org.eclipse.rdf4j.model.vocabulary;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;

public class IM {
    public static final String NAMESPACE = "http://www.DiscoveryDataService.org/InformationModel/Ontology#";
    public static final String PREFIX = "im";
    public static final Namespace NS = Vocabularies.createNamespace(PREFIX, NAMESPACE);
    public static final IRI MODULE = Vocabularies.createIRI(NAMESPACE, "module");
    public static final IRI ONTOLOGY = Vocabularies.createIRI(NAMESPACE, "ontology");
    public static final IRI CODE = Vocabularies.createIRI(NAMESPACE, "code");
    public static final IRI HAS_SCHEME = Vocabularies.createIRI(NAMESPACE, "scheme");
    public static final IRI STATUS = Vocabularies.createIRI(NAMESPACE, "status");
    public static final IRI SYNONYM = Vocabularies.createIRI(NAMESPACE, "synonym");
    public static final IRI RECORD = Vocabularies.createIRI(NAMESPACE, "Record");
    public static final IRI VALUESET = Vocabularies.createIRI(NAMESPACE, "ValueSet");
    public static final IRI HAS_MEMBERS = Vocabularies.createIRI(NAMESPACE, "hasMembers");
    public static final IRI HAS_EXPANSION = Vocabularies.createIRI(NAMESPACE, "hasExpansion");
    public static final IRI EXCLUDE = Vocabularies.createIRI(NAMESPACE, "exclude");
}
