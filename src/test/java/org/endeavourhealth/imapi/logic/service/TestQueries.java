package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.interfacemanager.model.QueryRequest;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.util.Set;
import java.util.stream.Collectors;

public class TestQueries {
  public static QueryRequest pathQuery() {
    return new QueryRequest().setPathQuery(new PathQuery()
      .setName("DiabetesPath")
      .setTarget(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://snomed.info/sct#44054006"))
      .setSource(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.IM + "Patient")));
  }


  public static QueryRequest dataModelPropertyRange() {
    Query query = new Query()
      .setName("Data model property range")
      .setDescription("get node, class or datatype value (range)  of property objects for specific data model and property")
      .addIs(new Node()
        .setParameter("myDataModel"))
      .path(p -> p
        .setIri("http://www.w3.org/ns/shacl#property")
        .setNode("shaclProperty"))
      .where(w -> w
        .and(p2 -> p2
          .setNodeRef("shaclProperty")
          .setIri(ShaclVocab.PATH)
          .is(in -> in
            .setParameter("myProperty")))
        .and(p2 -> p2
          .or(p3 -> p3
            .setNodeRef("shaclProperty")
            .setIri(ShaclVocab.CLASS)
            .setNode("propType"))
          .or(p3 -> p3
            .setNodeRef("shaclProperty")
            .setIri(ShaclVocab.NODE)
            .setNode("propType"))
          .or(p3 -> p3
            .setNodeRef("shaclProperty")
            .setIri(ShaclVocab.DATATYPE)
            .setNode("propType"))))
      .return_(r -> r
        .setNodeRef("propType")
        .setIri(RdfsVocab.LABEL));
    return new QueryRequest()
      .setQuery(query)
      .argument(a -> a
        .setParameter("myDataModel")
        .setValueIri(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.IM + "Observation")))
      .argument(a -> a
        .setParameter("myProperty")
        .setValueIri(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.IM + "concept")));

  }

  public static QueryRequest rangeSuggestion() {
    return new QueryRequest()
      .setContext(TTManager.createBasicContext())
      .addArgument(new Argument()
        .setParameter("this")
        .setValueIri(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.IM + "recordOwner")))
      .query(q -> q.setIri(NamespaceVocab.IM + "Query_ObjectPropertyRangeSuggestions"));

  }

  public static QueryRequest getShaclProperty() {
    return new QueryRequest()
      .argument(a -> a.setParameter("dataModel").setValueIri(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.IM + "Patient")))
      .argument(a -> a.setParameter("property").setValueIri(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.IM + "age")))
      .query(q -> q
        .setName("Shacl property predicates for a property is a data model")
        .setDescription("Select the predicates and values and labels of the values for a given data mode and property")
        .addIs(new Node()
          .setParameter("$dataModel"))
        .path(p -> p
          .setIri(ShaclVocab.PROPERTY.toString())
          .setNode("shaclProperty"))
        .where(w -> w
          .setNodeRef("shaclProperty")
          .setIri(ShaclVocab.PATH)
          .is(in -> in.setParameter("$property")))
        .return_(s -> s
          .setNodeRef("shaclProperty")
          .setIri(ShaclVocab.CLASS)
          .return_(n -> n
            .setIri(RdfsVocab.LABEL)))
        .return_(p -> p
          .setNodeRef("shaclProperty")
          .setIri(ShaclVocab.NODE)
          .return_(n -> n
            .setIri(RdfsVocab.LABEL)))
        .return_(p -> p
          .setNodeRef("shaclProperty")
          .setIri(ShaclVocab.DATATYPE)
          .return_(n -> n
            .setIri(RdfsVocab.LABEL)))
        .return_(p -> p
          .setNodeRef("shaclProperty")
          .setIri(ShaclVocab.MAXCOUNT))
        .return_(p -> p
          .setNodeRef("shaclProperty")
          .setIri(ShaclVocab.MINCOUNT)));
  }


  public static QueryRequest getAllowableSubtypes() {
    QueryRequest qr = new QueryRequest();
    qr.setContext(TTManager.createBasicContext());
    qr.addArgument(new Argument()
      .setParameter("this")
      .setValueIri(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.IM + "Q_Queries")));
    Query query = new Query()
      .setName("Allowable child types for a folder")
      .setIri(NamespaceVocab.IM + "Query_AllowableChildTypes");
    qr.setQuery(query);
    return qr;
  }


  public static QueryRequest deleteSets() {
    return new QueryRequest()
      .addArgument(new Argument()
        .setParameter("this")
        .setValueIri(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.QR)))
      .setUpdate(new Update()
        .setIri(NamespaceVocab.IM + "DeleteSets")
        .setName("delete sets"));

  }


  public static QueryRequest subtypesParameterised() {
    return new QueryRequest()
      .addArgument(new Argument()
        .setParameter("this")
        .addToValueIriList(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://snomed.info/sct#76661004"))
        .addToValueIriList(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://snomed.info/sct#243640007")))
      .setQuery(new Query()
        .setName("Subtypes of concepts as a parameterised query")
        .return_(s -> s.setNodeRef("c").setIri(RdfsVocab.LABEL))
        .setNode("c")
        .addIs(new Node()
          .setParameter("this")
          .setDescendantsOrSelfOf(true)));
  }


  public static QueryRequest getMembers() {
    return new QueryRequest()
      .setTextSearch("FOXG1")
      .query(q -> q
        .setName("Filter concept subtypes that are members of value sets")
        .addIs(new Node().setIri(NamespaceVocab.SNOMED + "57148006")
          .setDescendantsOrSelfOf(true))
        .addIs(new Node().setIri(NamespaceVocab.SNOMED + "11164009")
          .setDescendantsOrSelfOf(true))
        .where(w -> w
          .setIri(ImVocab.HAS_MEMBER)
          .setInverse(true)
          .is(n -> n
            .setIri(NamespaceVocab.IM + "VSET_Conditions"))
          .is(n -> n
            .setIri(NamespaceVocab.IM + "VSET_ASD")))
        .return_(p -> p.setIri(RdfsVocab.LABEL))
        .return_(p -> p.setIri(ImVocab.HAS_TERM_CODE)
          .return_(p1 -> p1.setIri(RdfsVocab.LABEL))));
  }

  public static QueryRequest substanceTextSearch() {
    return new QueryRequest()
      .setTextSearch("thia")
      .addArgument(new Argument()
        .setParameter("this")
        .addToValueIriList(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://snomed.info/sct#105590001")))
      .setQuery(new Query()
        .is(i -> i
          .setParameter("this")
          .setDescendantsOrSelfOf(true))
        .return_(p -> p.setIri(RdfsVocab.LABEL))
        .return_(p -> p.setIri(ImVocab.HAS_TERM_CODE)
          .return_(p1 -> p1.setIri(RdfsVocab.LABEL)))
        .setName("substances starting with 'thia'"));
  }


  public static QueryRequest query1() {
    Query query = new Query()
      .setName("FamilyHistoryExpansionObjectFormat");
    query
      .where(w -> w
        .setIri(ImVocab.HAS_MEMBER)
        .setInverse(true)
        .addIs(new Node().setIri(NamespaceVocab.IM + "VSET_FamilyHistory")))
      .return_(p -> p
        .setIri(RdfsVocab.LABEL))
      .return_(p -> p
        .setIri(ImVocab.CODE))
      .return_(p -> p
        .setIri(ImVocab.MATCHED_TO)
        .setInverse(true)
        .return_(p1 -> p1
          .setIri(RdfsVocab.LABEL))
        .return_(p1 -> p1
          .setIri(ImVocab.CODE)));
    return new QueryRequest().setQuery(query);
  }

  public static QueryRequest shapesWithDateOFBirth() {

    Query query = new Query()
      .setName("PropertiesOfShapesUsingDateOfBirth")
      .setDescription("all of the data model properties for entities that have a property df a data of birth");
    query
      .setTypeOf(ShaclVocab.NODESHAPE.toString())
      .path(p -> p
        .setIri(ShaclVocab.PROPERTY.toString())
        .setNode("shaclProperty"))
      .where(p1 -> p1
        .setNodeRef("shaclProperty")
        .setIri(ShaclVocab.PATH)
        .addIs(NamespaceVocab.IM + "dateOfBirth"))
      .return_(p -> p
        .setIri(ShaclVocab.PROPERTY)
        .return_(s1 -> s1.setIri(ShaclVocab.PATH))
        .return_(s1 -> s1.setIri(ShaclVocab.NODE))
        .return_(s1 -> s1.setIri(ShaclVocab.MINCOUNT))
        .return_(s1 -> s1.setIri(ShaclVocab.MAXCOUNT))
        .return_(s1 -> s1.setIri(ShaclVocab.CLASS))
        .return_(s1 -> s1.setIri(ShaclVocab.DATATYPE)));
    return new QueryRequest().setQuery(query);
  }


  public static QueryRequest query4() {

    Query query = new Query()
      .setName("AsthmaSubTypesCore");
    query
      .addIs(new Node()
        .setIri(NamespaceVocab.SNOMED + "195967001").setDescendantsOrSelfOf(true))
      .return_(p -> p.setIri(RdfsVocab.LABEL))
      .return_(p -> p.setIri(ImVocab.CODE));
    return new QueryRequest().setQuery(query);
  }


  public static QueryRequest getAllowableProperties() {
    return new QueryRequest().
      addArgument(new Argument()
        .setParameter("this")
        .setValueIri(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://snomed.info/sct#763158003")))
      .setQuery(new Query()
        .setName("Allowable Properties for medications")
        .setIri(NamespaceVocab.IM + "Query_AllowableProperties")
      );
  }


  public static QueryRequest entityFilter(Set<String> entities) {
    return new QueryRequest()
      .setTextSearch("has active")
      .addArgument(new Argument()
        .setParameter("entities")
        .setValueIriList(entities.stream().map(TTIriRef::new).collect(Collectors.toSet())))
      .setQuery(new Query()
        .setName("Allowable Properties for medications")
        .setIri(QueryVocab.ENTITY_FILTER.toString())
      );
  }

  public static QueryRequest isValidProperty() {
    return new QueryRequest().
      addArgument(new Argument()
        .setParameter("property")
        .setValueIri(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://snomed.info/sct#363698007")))
      .addArgument(new Argument()
        .setParameter("concept")
        .setValueIriList((Set.of(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://snomed.info/sct#161891005")))))
      .setQuery(new Query()
        .setName("Allowable Properties for medications")
        .setIri(QueryVocab.IS_VALID_PROPERTY.toString()));
  }


  public static QueryRequest getConcepts() {
    return new QueryRequest()
      .query(q -> q
        .setActiveOnly(true)
        .setName("Search for concepts")
        .setTypeOf(ImVocab.CONCEPT.toString())
        .return_(p -> p.setIri(RdfsVocab.LABEL))
        .return_(p -> p.setIri(ImVocab.HAS_TERM_CODE)
          .return_(p1 -> p1.setIri(RdfsVocab.LABEL))))
      .setTextSearch("chest pain");
  }

  public static QueryRequest getIsas() {
    QueryRequest qr = new QueryRequest()
      .query(q -> q
        .setName("All subtypes of an entity, active only")
        .setActiveOnly(true)
        .addIs(new Node().setParameter("this").setDescendantsOrSelfOf(true))
        .return_(r -> r.setIri(RdfsVocab.LABEL)));
    qr.addArgument("this", NamespaceVocab.SNOMED + "417928002");
    return qr;
  }

  public static QueryRequest AllowablePropertiesForCovid() {
    QueryRequest qr = new QueryRequest()
      .setName("Allowable Properties for Covid")
      .setTextSearch("caus");
    qr.setQuery(new Query()
      .setIri(QueryVocab.ALLOWABLE_PROPERTIES.toString()));
    qr.argument(a -> a.setParameter("this")
      .setValueIri(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.SNOMED + "840539006")));
    return qr;
  }

  public static QueryRequest oralNsaids() {
    Query query = new Query()
      .setName("oral none steroidals")
      .return_(s -> s.setIri(RdfsVocab.LABEL))
      .addIs(new Node().setIri(NamespaceVocab.SNOMED + "763158003").setDescendantsOrSelfOf(true))
      .where(and -> and
        .and(a1 -> a1
          .setIri(NamespaceVocab.SNOMED + "127489000")
          .setDescendantsOrSelfOf(true)
          .setAnyRoleGroup(true)
          .addIs(new Node().setIri(NamespaceVocab.SNOMED + "372665008").setDescendantsOrSelfOf(true)))
        .and(a2 -> a2
          .setIri(NamespaceVocab.SNOMED + "411116001").setDescendantsOrSelfOf(true)
          .setAnyRoleGroup(true)
          .addIs(Node.iri(NamespaceVocab.SNOMED + "385268001").setDescendantsOrSelfOf(true))));

    return new QueryRequest().setQuery(query);

  }


}
