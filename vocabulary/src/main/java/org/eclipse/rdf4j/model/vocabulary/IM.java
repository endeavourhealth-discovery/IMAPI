package org.eclipse.rdf4j.model.vocabulary;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;

public class IM {
    public static final String NAMESPACE = "http://www.EndeavourHealth.org/InformationModel/Ontology#";
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
    public static final IRI IS_CONTAINED_IN = Vocabularies.createIRI(NAMESPACE,"isContainedIn");
    public static final IRI LEGACY = Vocabularies.createIRI(NAMESPACE,"Legacy");
    public static final String XSD_NAMESPACE= "http://www.w3.org/2001/XMLSchema#";
    public static final IRI PATTERN= Vocabularies.createIRI(XSD_NAMESPACE,"pattern");
    public static final IRI HAS_MEMBER_EXCLUSION = Vocabularies.createIRI(XSD_NAMESPACE,"hasMemberExclusion");
    public static final IRI HAS_MEMBER_INSTANCE = Vocabularies.createIRI(XSD_NAMESPACE,"hasMemberInstance");
    public static final IRI HAS_MEMBER_EXC_INSTANCE = Vocabularies.createIRI(XSD_NAMESPACE,"hasMemberExcInstance");
    public static final IRI IS_A= Vocabularies.createIRI(NAMESPACE,"isA");
    public static final IRI ROLE_GROUP= Vocabularies.createIRI(NAMESPACE,"roleGroup");
    public static final IRI IN_ROLE_GROUP_OF= Vocabularies.createIRI(NAMESPACE,"inrRoleGroupOf");
    public static final IRI IS_INSTANCE_OF = Vocabularies.createIRI(NAMESPACE,"isInstanceOf");


}
