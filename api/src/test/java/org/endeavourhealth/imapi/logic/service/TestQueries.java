package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TestQueries {
  public static String ex = "http://example.org/qry#";

  public static QueryRequest pathQuery(){
    return new QueryRequest().setPathQuery(new PathQuery()
        .setName("DiabetesPath")
      .setTarget(iri("http://snomed.info/sct#44054006"))
      .setSource(iri(IM.NAMESPACE+"Patient")));
  }


  public static QueryRequest dataModelPropertyRange() {
    Query query = new Query()
      .setName("Data model property range")
      .setDescription("get node, class or datatype value (range)  of property objects for specific data model and property")
      .match(m -> m
        .addInstanceOf(new Node()
          .setParameter("myDataModel"))
        .where(p -> p
          .setIri("http://www.w3.org/ns/shacl#property")
          .match(m1 -> m1
            .setVariable("shaclProperty")
            .setBoolWhere(Bool.and)
            .where(p2 -> p2
              .setIri(SHACL.PATH)
              .is(in -> in
                .setParameter("myProperty")))
            .where(p2 -> p2
              .setBoolWhere(Bool.or)
              .where(p3 -> p3
                .setIri(SHACL.CLASS)
                .match(m3 -> m3
                  .setVariable("propType")))
              .where(p3 -> p3
                .setIri(SHACL.NODE)
                .match(m3 -> m3
                  .setVariable("propType")))
              .where(p3 -> p3
                .setIri(SHACL.DATATYPE)
                .match(m3 -> m3
                  .setVariable("propType")))))))
      .return_(r -> r
        .setNodeRef("propType")
        .property(p -> p
          .setIri(RDFS.LABEL)));
    return new QueryRequest()
      .setQuery(query)
      .argument(a -> a
        .setParameter("myDataModel")
        .setValueIri(TTIriRef.iri(IM.NAMESPACE + "Observation")))
      .argument(a -> a
        .setParameter("myProperty")
        .setValueIri(TTIriRef.iri(IM.NAMESPACE + "concept")));

  }

  public static QueryRequest rangeSuggestion() {
    return new QueryRequest()
      .setContext(TTManager.createBasicContext())
      .addArgument(new Argument()
        .setParameter("this")
        .setValueIri(TTIriRef.iri(IM.NAMESPACE + "recordOwner")))
      .query(q -> q
        .setName("Suggested range for a property")
        .setDescription("get node, class or datatype values (ranges)  of property objects that have 4this as their path")
        .match(m -> m
          .where(w -> w
            .setIri(SHACL.PATH)
            .addIs(new Node().setParameter("this")))
          .where(w -> w
            .setBoolWhere(Bool.or)
            .where(p -> p
              .setIri(SHACL.NODE)
              .match(n -> n
                .setVariable("range")))
            .where(p -> p
              .setIri(SHACL.CLASS)
              .match(n -> n
                .setVariable("range")))
            .where(p -> p
              .setIri(SHACL.DATATYPE)
              .match(n -> n
                .setVariable("range")))))
        .return_(s -> s.setNodeRef("range")
          .property(p -> p.setIri(RDFS.LABEL))));
  }

  public static QueryRequest getShaclProperty() {
    return new QueryRequest()
      .argument(a -> a.setParameter("dataModel").setValueIri(TTIriRef.iri(IM.NAMESPACE + "Patient")))
      .argument(a -> a.setParameter("property").setValueIri(TTIriRef.iri(IM.NAMESPACE + "age")))
      .query(q -> q
        .setName("Query - shacl property predicates for a property is a data model")
        .setDescription("Select the predicates and values and labels of the values for a given data mode and property")
        .match(m -> m
          .addInstanceOf(new Node()
            .setParameter("$dataModel"))
          .where(p -> p
            .setIri(SHACL.PROPERTY)
            .match(n -> n
              .setVariable("shaclProperty")
              .where(w -> w
                .setIri(SHACL.PATH)
                .is(in -> in.setParameter("$property"))))))
        .return_(s -> s
          .setNodeRef("shaclProperty")
          .property(p -> p
            .setIri(SHACL.CLASS)
            .return_(n -> n
              .property(p1 -> p1
                .setIri(RDFS.LABEL))))
          .property(p -> p
            .setIri(SHACL.NODE)
            .return_(n -> n
              .property(p1 -> p1
                .setIri(RDFS.LABEL))))
          .property(p -> p
            .setIri(SHACL.DATATYPE)
            .return_(n -> n
              .property(p1 -> p1.setIri(RDFS.LABEL))))
          .property(p -> p
            .setIri(SHACL.MAXCOUNT))
          .property(p -> p
            .setIri(SHACL.MINCOUNT))));
  }



  public static QueryRequest getAllowableSubtypes() {
    QueryRequest qr = new QueryRequest();
    qr.setContext(TTManager.createBasicContext());
    qr.addArgument(new Argument()
      .setParameter("this")
      .setValueIri(TTIriRef.iri(IM.NAMESPACE + "Concept")));
    Query query = new Query()
      .setName("Allowable child types for a folder")
      .setIri(IM.NAMESPACE + "Query_AllowableChildTypes");
    qr.setQuery(query);
    return qr;
  }


  public static QueryRequest deleteSets() {
    return new QueryRequest()
      .addArgument(new Argument()
        .setParameter("this")
        .setValueIri(iri(QR.NAMESPACE)))
      .setUpdate(new Update()
        .setIri(IM.NAMESPACE + "DeleteSets")
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
        .return_(s -> s.setNodeRef("c").property(p -> p.setIri(RDFS.LABEL)))
        .match(f -> f
          .setVariable("c")
          .addInstanceOf(new Node()
            .setParameter("this")
            .setDescendantsOrSelfOf(true))));
  }


  public static QueryRequest getMembers() {
    return new QueryRequest()
      .setTextSearch("FOXG1")
      .query(q -> q
        .setName("Filter concept subtypes that are members of value sets")
        .match(m -> m
              .addInstanceOf(new Node().setIri(SNOMED.NAMESPACE + "57148006")
                .setDescendantsOrSelfOf(true))
              .addInstanceOf(new Node().setIri(SNOMED.NAMESPACE + "11164009")
                .setDescendantsOrSelfOf(true))
          .where(w -> w
            .setIri(IM.HAS_MEMBER)
            .setInverse(true)
            .is(n -> n
              .setIri(IM.NAMESPACE + "VSET_Conditions"))
            .is(n -> n
              .setIri(IM.NAMESPACE + "VSET_ASD")))));
  }


  public static QueryRequest rangeTextSearch() {
    return new QueryRequest()
      .setTextSearch("Hyper")
      .addArgument(new Argument()
        .setParameter("this")
        .addToValueIriList(TTIriRef.iri("http://snomed.info/sct#404684003"))
        .addToValueIriList(TTIriRef.iri("http://snomed.info/sct#71388002")))
      .setQuery(new Query()
        .setName("Get allowable property values with text filter")
        .return_(s -> s.property(p -> p.setIri(RDFS.LABEL)))
        .match(f -> f
          .addInstanceOf(new Node()
            .setParameter("this")
            .setDescendantsOrSelfOf(true))));
  }

  public static QueryRequest substanceTextSearch() {
    return new QueryRequest()
      .setTextSearch("thia")
      .addArgument(new Argument()
        .setParameter("this")
        .addToValueIriList(TTIriRef.iri("http://snomed.info/sct#105590001")))
      .setQuery(new Query()
        .match(m -> m
          .instanceOf(i -> i
            .setParameter("this")
            .setDescendantsOrSelfOf(true)))
        .setName("substances starting with 'thia'"));
  }


  public static QueryRequest query1() {
    Query query = new Query()
      .setName("FamilyHistoryExpansionObjectFormat");
    query
      .match(f -> f
        .where(w -> w
          .setIri(IM.HAS_MEMBER)
          .setInverse(true)
          .addIs(new Node().setIri(IM.NAMESPACE + "VSET_FamilyHistory"))))
      .return_(s -> s
        .property(p -> p
          .setIri(RDFS.LABEL))
        .property(p -> p
          .setIri(IM.CODE))
        .property(p -> p
          .setIri(IM.MATCHED_TO)
          .setInverse(true)
          .return_(n -> n
            .property(p1 -> p1
              .setIri(RDFS.LABEL))
            .property(p1 -> p1
              .setIri(IM.CODE)))));
    return new QueryRequest().setQuery(query);
  }

  public static QueryRequest query2() {

    Query query = new Query()
      .setName("PropertiesOfShapesUsingDateOfBirth")
      .setDescription("all of the data model properties for entities that have a property df a data of birth");
    query
      .match(f -> f
        .setTypeOf(SHACL.NODESHAPE)
        .where(p -> p
          .setIri(SHACL.PROPERTY)
          .match(w1 -> w1
            .where(p1 -> p1
              .setIri(SHACL.PATH)
              .addIs(IM.NAMESPACE + "dateOfBirth")))))
      .match(m -> m
        .where(p -> p
          .setIri(SHACL.PROPERTY)))
      .return_(s -> s
        .property(p -> p
          .setIri(SHACL.PROPERTY)
          .return_(n -> n
            .property(s1 -> s1.setIri(SHACL.PATH))
            .property(s1 -> s1.setIri(SHACL.NODE))
            .property(s1 -> s1.setIri(SHACL.MINCOUNT))
            .property(s1 -> s1.setIri(SHACL.MAXCOUNT))
            .property(s1 -> s1.setIri(SHACL.CLASS))
            .property(s1 -> s1.setIri(SHACL.DATATYPE)))));
    return new QueryRequest().setQuery(query);
  }


  public static QueryRequest query4() {

    Query query = new Query()
      .setName("AsthmaSubTypesCore");
    query
      .match(f -> f
        .addInstanceOf(new Node()
          .setIri(SNOMED.NAMESPACE + "195967001").setDescendantsOrSelfOf(true)))
      .return_(s -> s
        .property(p -> p.setIri(RDFS.LABEL))
        .property(p -> p.setIri(IM.CODE)));
    return new QueryRequest().setQuery(query);
  }

  public static QueryRequest getAllowableRanges() {
    QueryRequest qr = new QueryRequest().setQuery(new Query().setIri(IM.NAMESPACE + "Query_AllowableRanges")
      .setName("Allowable ranges"));
    qr.addArgument(new Argument().setParameter("this")
      .setValueIri(TTIriRef.iri("http://snomed.info/sct#127489000")));
    return qr;
  }

  public static QueryRequest getAllowableProperties() {
    return new QueryRequest().
      addArgument(new Argument()
        .setParameter("this")
        .setValueIri(TTIriRef.iri("http://snomed.info/sct#763158003")))
      .setQuery(new Query()
        .setName("Allowable Properties for medications")
        .setIri(IM.NAMESPACE + "Query_AllowableProperties")
      );
  }

  public static QueryRequest getConcepts() {
    return new QueryRequest()
      .query(q -> q
        .setActiveOnly(true)
        .setName("Search for concepts")
        .match(f -> f
          .setTypeOf(IM.CONCEPT))
        .return_(s -> s
          .property(p -> p
            .setIri(RDFS.LABEL))))
      .setTextSearch("chest pain");
  }

  public static QueryRequest getIsas() {
    QueryRequest qr = new QueryRequest()
      .query(q -> q
        .setName("All subtypes of an entity, active only")
        .setActiveOnly(true)
        .match(f -> f.addInstanceOf(new Node().setParameter("this").setDescendantsOrSelfOf(true)))
        .return_(r -> r.property(s -> s.setIri(RDFS.LABEL))));
    qr.addArgument("this", SNOMED.NAMESPACE + "417928002");
    return qr;
  }

  public static QueryRequest AllowablePropertiesForCovid() {
    QueryRequest qr = new QueryRequest()
      .setTextSearch("causative");
    Query query = new Query()
      .setName("AllowablePropertiesForCovidStarting with causative")
      .setDescription("'using property domains get the allowable properties match the supertypes of this concept")
      .setActiveOnly(true);
    query
      .match(f -> f
        .setTypeOf(new Node().setIri(IM.CONCEPT)
          .setDescendantsOrSelfOf(true))
        .where(w1 -> w1
          .setIri(RDFS.DOMAIN)
          .addIs(new Node().setIri(SNOMED.NAMESPACE + "674814021000119106").setAncestorsOf(true))
        ))
      .return_(r -> r
        .property(s -> s.setIri(IM.CODE))
        .property(s -> s.setIri(RDFS.LABEL)));
    qr.setQuery(query);
    return qr;
  }

  public static QueryRequest query6() {
    Query query = new Query()
      .setName("Some Barts cerner codes with context including a regex");
    query
      .return_(r -> r
        .property(s -> s.setIri(RDFS.LABEL))
        .property(s -> s.setIri(IM.CODE))
        .property(s -> s
          .setIri(IM.CONCEPT)
          .setInverse(true)
          .return_(n -> n
            .property(s1 -> s1.setIri(IM.SOURCE_VALUE))
            .property(s1 -> s1.setIri(IM.SOURCE_REGEX))
            .property(s1 -> s1.setIri(IM.SOURCE_SYSTEM))
            .property(s1 -> s1.setIri(IM.SOURCE_TABLE))
            .property(s1 -> s1.setIri(IM.SOURCE_FIELD)))))
      .match(f -> f
        .where(p -> p
          .setIri(IM.HAS_SCHEME)
          .is(i -> i
            .setIri(GRAPH.BARTS_CERNER)))
        .where(p -> p
          .setIri(IM.CONCEPT)
          .setInverse(true)
          .match(m1 -> m1
            .where(w1 -> w1
              .setIri(IM.SOURCE_REGEX)))));
    return new QueryRequest().setQuery(query);
  }

  public static QueryRequest oralNsaids() {
    Query query = new Query()
      .setName("oral none steroidals")
      .return_(r -> r
        .property(s -> s.setIri(RDFS.LABEL)))
      .match(rf -> rf
        .setBoolMatch(Bool.and)
        .match(f -> f
          .addInstanceOf(new Node().setIri(SNOMED.NAMESPACE + "763158003").setDescendantsOrSelfOf(true))
          .where(a1 -> a1
            .setIri(SNOMED.NAMESPACE + "127489000")
            .setDescendantsOrSelfOf(true)
            .setAnyRoleGroup(true)
            .addIs(new Node().setIri(SNOMED.NAMESPACE + "372665008").setDescendantsOrSelfOf(true)))
          .where(a2 -> a2
            .setIri(SNOMED.NAMESPACE + "411116001").setDescendantsOrSelfOf(true)
            .setAnyRoleGroup(true)
            .addIs(Node.iri(SNOMED.NAMESPACE + "385268001").setDescendantsOrSelfOf(true)))));

    return new QueryRequest().setQuery(query);

  }


}
