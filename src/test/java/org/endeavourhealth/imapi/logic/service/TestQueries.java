package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TestQueries {
  public static QueryRequest pathQuery() {
    return new QueryRequest().setPathQuery(new PathQuery()
      .setName("DiabetesPath")
      .setTarget(iri("http://snomed.info/sct#44054006"))
      .setSource(iri(NAMESPACE.IM + "Patient")));
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
          .setIri(SHACL.PATH)
          .is(in -> in
            .setParameter("myProperty")))
        .and(p2 -> p2
          .or(p3 -> p3
            .setNodeRef("shaclProperty")
            .setIri(SHACL.CLASS)
            .setNode("propType"))
          .or(p3 -> p3
            .setNodeRef("shaclProperty")
            .setIri(SHACL.NODE)
            .setNode("propType"))
          .or(p3 -> p3
            .setNodeRef("shaclProperty")
            .setIri(SHACL.DATATYPE)
            .setNode("propType"))))
      .return_(r -> r
          .setNodeRef("propType")
          .setIri(RDFS.LABEL));
    return new QueryRequest()
      .setQuery(query)
      .argument(a -> a
        .setParameter("myDataModel")
        .setValueIri(TTIriRef.iri(NAMESPACE.IM + "Observation")))
      .argument(a -> a
        .setParameter("myProperty")
        .setValueIri(TTIriRef.iri(NAMESPACE.IM + "concept")));

  }

  public static QueryRequest rangeSuggestion() {
    return new QueryRequest()
      .setContext(TTManager.createBasicContext())
      .addArgument(new Argument()
        .setParameter("this")
        .setValueIri(TTIriRef.iri(NAMESPACE.IM + "recordOwner")))
      .query(q -> q.setIri(NAMESPACE.IM + "Query_ObjectPropertyRangeSuggestions"));

  }

  public static QueryRequest getShaclProperty() {
    return new QueryRequest()
      .argument(a -> a.setParameter("dataModel").setValueIri(TTIriRef.iri(NAMESPACE.IM + "Patient")))
      .argument(a -> a.setParameter("property").setValueIri(TTIriRef.iri(NAMESPACE.IM + "age")))
      .query(q -> q
        .setName("Shacl property predicates for a property is a data model")
        .setDescription("Select the predicates and values and labels of the values for a given data mode and property")
        .addIs(new Node()
          .setParameter("$dataModel"))
        .path(p -> p
          .setIri(SHACL.PROPERTY.toString())
          .setNode("shaclProperty"))
        .where(w -> w
          .setNodeRef("shaclProperty")
          .setIri(SHACL.PATH)
          .is(in -> in.setParameter("$property")))
        .return_(s -> s
          .setNodeRef("shaclProperty")
            .setIri(SHACL.CLASS)
            .return_(n -> n
                .setIri(RDFS.LABEL)))
        .return_(p -> p
          .setNodeRef("shaclProperty")
            .setIri(SHACL.NODE)
            .return_(n -> n
                .setIri(RDFS.LABEL)))
          .return_(p -> p
            .setNodeRef("shaclProperty")
            .setIri(SHACL.DATATYPE)
            .return_(n -> n
              .setIri(RDFS.LABEL)))
          .return_(p -> p
            .setNodeRef("shaclProperty")
            .setIri(SHACL.MAXCOUNT))
          .return_(p -> p
            .setNodeRef("shaclProperty")
            .setIri(SHACL.MINCOUNT)));
  }


  public static QueryRequest getAllowableSubtypes() {
    QueryRequest qr = new QueryRequest();
    qr.setContext(TTManager.createBasicContext());
    qr.addArgument(new Argument()
      .setParameter("this")
      .setValueIri(TTIriRef.iri(NAMESPACE.IM + "Q_Queries")));
    Query query = new Query()
      .setName("Allowable child types for a folder")
      .setIri(NAMESPACE.IM + "Query_AllowableChildTypes");
    qr.setQuery(query);
    return qr;
  }


  public static QueryRequest deleteSets() {
    return new QueryRequest()
      .addArgument(new Argument()
        .setParameter("this")
        .setValueIri(iri(NAMESPACE.QR)))
      .setUpdate(new Update()
        .setIri(NAMESPACE.IM + "DeleteSets")
        .setName("delete sets"));

  }


  public static QueryRequest subtypesParameterised() {
    return new QueryRequest()
      .addArgument(new Argument()
        .setParameter("this")
        .addToValueIriList(TTIriRef.iri("http://snomed.info/sct#76661004"))
        .addToValueIriList(TTIriRef.iri("http://snomed.info/sct#243640007")))
      .setQuery(new Query()
        .setName("Subtypes of concepts as a parameterised query")
        .return_(s -> s.setNodeRef("c").setIri(RDFS.LABEL))
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
        .addIs(new Node().setIri(NAMESPACE.SNOMED + "57148006")
          .setDescendantsOrSelfOf(true))
        .addIs(new Node().setIri(NAMESPACE.SNOMED + "11164009")
          .setDescendantsOrSelfOf(true))
        .where(w -> w
          .setIri(IM.HAS_MEMBER)
          .setInverse(true)
          .is(n -> n
            .setIri(NAMESPACE.IM + "VSET_Conditions"))
          .is(n -> n
            .setIri(NAMESPACE.IM + "VSET_ASD")))
          .return_(p -> p.setIri(RDFS.LABEL))
          .return_(p -> p.setIri(IM.HAS_TERM_CODE)
            .return_(p1 -> p1.setIri(RDFS.LABEL))));
  }

  public static QueryRequest substanceTextSearch() {
    return new QueryRequest()
      .setTextSearch("thia")
      .addArgument(new Argument()
        .setParameter("this")
        .addToValueIriList(TTIriRef.iri("http://snomed.info/sct#105590001")))
      .setQuery(new Query()
        .is(i -> i
          .setParameter("this")
          .setDescendantsOrSelfOf(true))
          .return_(p -> p.setIri(RDFS.LABEL))
          .return_(p -> p.setIri(IM.HAS_TERM_CODE)
            .return_(p1 -> p1.setIri(RDFS.LABEL)))
        .setName("substances starting with 'thia'"));
  }


  public static QueryRequest query1() {
    Query query = new Query()
      .setName("FamilyHistoryExpansionObjectFormat");
    query
      .where(w -> w
        .setIri(IM.HAS_MEMBER)
        .setInverse(true)
        .addIs(new Node().setIri(NAMESPACE.IM + "VSET_FamilyHistory")))
        .return_(p -> p
          .setIri(RDFS.LABEL))
        .return_(p -> p
          .setIri(IM.CODE))
        .return_(p -> p
          .setIri(IM.MATCHED_TO)
          .setInverse(true)
            .return_(p1 -> p1
              .setIri(RDFS.LABEL))
            .return_(p1 -> p1
              .setIri(IM.CODE)));
    return new QueryRequest().setQuery(query);
  }

  public static QueryRequest shapesWithDateOFBirth() {

    Query query = new Query()
      .setName("PropertiesOfShapesUsingDateOfBirth")
      .setDescription("all of the data model properties for entities that have a property df a data of birth");
    query
      .setTypeOf(SHACL.NODESHAPE.toString())
      .path(p -> p
        .setIri(SHACL.PROPERTY.toString())
        .setNode("shaclProperty"))
      .where(p1 -> p1
        .setNodeRef("shaclProperty")
        .setIri(SHACL.PATH)
        .addIs(NAMESPACE.IM + "dateOfBirth"))
        .return_(p -> p
          .setIri(SHACL.PROPERTY)
            .return_(s1 -> s1.setIri(SHACL.PATH))
            .return_(s1 -> s1.setIri(SHACL.NODE))
            .return_(s1 -> s1.setIri(SHACL.MINCOUNT))
            .return_(s1 -> s1.setIri(SHACL.MAXCOUNT))
            .return_(s1 -> s1.setIri(SHACL.CLASS))
            .return_(s1 -> s1.setIri(SHACL.DATATYPE)));
    return new QueryRequest().setQuery(query);
  }


  public static QueryRequest query4() {

    Query query = new Query()
      .setName("AsthmaSubTypesCore");
    query
      .addIs(new Node()
        .setIri(NAMESPACE.SNOMED + "195967001").setDescendantsOrSelfOf(true))
        .return_(p -> p.setIri(RDFS.LABEL))
        .return_(p -> p.setIri(IM.CODE));
    return new QueryRequest().setQuery(query);
  }


  public static QueryRequest getAllowableProperties() {
    return new QueryRequest().
      addArgument(new Argument()
        .setParameter("this")
        .setValueIri(TTIriRef.iri("http://snomed.info/sct#763158003")))
      .setQuery(new Query()
        .setName("Allowable Properties for medications")
        .setIri(NAMESPACE.IM + "Query_AllowableProperties")
      );
  }


  public static QueryRequest entityFilter(Set<String> entities) {
    return new QueryRequest()
      .setTextSearch("has active")
    .addArgument(new Argument()
        .setParameter("entities")
        .setValueIriList(entities.stream().map(TTIriRef::iri).collect(Collectors.toSet())))
      .setQuery(new Query()
        .setName("Allowable Properties for medications")
        .setIri(QUERY.ENTITY_FILTER.toString())
      );
  }

  public static QueryRequest isValidProperty() {
    return new QueryRequest().
      addArgument(new Argument()
        .setParameter("property")
        .setValueIri(TTIriRef.iri("http://snomed.info/sct#363698007")))
      .addArgument(new Argument()
        .setParameter("concept")
        .setValueIriList((Set.of(iri("http://snomed.info/sct#161891005")))))
      .setQuery(new Query()
        .setName("Allowable Properties for medications")
        .setIri(QUERY.IS_VALID_PROPERTY.toString()));
  }




  public static QueryRequest getConcepts() {
    return new QueryRequest()
      .query(q -> q
        .setActiveOnly(true)
        .setName("Search for concepts")
        .setTypeOf(IM.CONCEPT.toString())
          .return_(p -> p.setIri(RDFS.LABEL))
          .return_(p -> p.setIri(IM.HAS_TERM_CODE)
            .return_(p1 -> p1.setIri(RDFS.LABEL))))
      .setTextSearch("chest pain");
  }

  public static QueryRequest getIsas() {
    QueryRequest qr = new QueryRequest()
      .query(q -> q
        .setName("All subtypes of an entity, active only")
        .setActiveOnly(true)
        .addIs(new Node().setParameter("this").setDescendantsOrSelfOf(true))
        .return_(r -> r.setIri(RDFS.LABEL)));
    qr.addArgument("this", NAMESPACE.SNOMED + "417928002");
    return qr;
  }

  public static QueryRequest AllowablePropertiesForCovid() {
    QueryRequest qr = new QueryRequest()
      .setName("Allowable Properties for Covid")
      .setTextSearch("caus");
    qr.setQuery(new Query()
      .setIri(QUERY.ALLOWABLE_PROPERTIES.toString()));
    qr.argument(a -> a.setParameter("this")
      .setValueIri(iri(NAMESPACE.SNOMED + "840539006")));
    return qr;
  }

  public static QueryRequest oralNsaids() {
    Query query = new Query()
      .setName("oral none steroidals")
        .return_(s -> s.setIri(RDFS.LABEL))
      .addIs(new Node().setIri(NAMESPACE.SNOMED + "763158003").setDescendantsOrSelfOf(true))
      .where(and -> and
        .and(a1 -> a1
          .setIri(NAMESPACE.SNOMED + "127489000")
          .setDescendantsOrSelfOf(true)
          .setAnyRoleGroup(true)
          .addIs(new Node().setIri(NAMESPACE.SNOMED + "372665008").setDescendantsOrSelfOf(true)))
        .and(a2 -> a2
          .setIri(NAMESPACE.SNOMED + "411116001").setDescendantsOrSelfOf(true)
          .setAnyRoleGroup(true)
          .addIs(Node.iri(NAMESPACE.SNOMED + "385268001").setDescendantsOrSelfOf(true))));

    return new QueryRequest().setQuery(query);

  }


}
