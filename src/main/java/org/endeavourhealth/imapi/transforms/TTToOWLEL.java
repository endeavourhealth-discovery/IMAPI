package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;
import org.endeavourhealth.interfacemanager.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Converts Discovery JSON Triple tree syntax document to OWL EL functional syntax using an OWL factory.
 * Note that this is a limited transform for the purposes of EL based inferrencing using a reasoner. DL axioms are ignored or converted
 * to EL similar structures
 * For example property ranges domains are ignored if present. Cardinality restrictions are converted to existential quantification.
 * Data type restrictions are ignored.
 * <p>Thus a transform back from the OWL EL version will not match the source unless the source is EL only.
 */
public class TTToOWLEL {
  private final DefaultPrefixManager prefixManager;
  private final OWLDataFactory dataFactory;
  private final OWLOntologyManager manager;
  private OWLOntology ontology;
  private TTManager ttManager;


  public TTToOWLEL() {
    manager = OWLManager.createOWLOntologyManager();
    dataFactory = manager.getOWLDataFactory();
    prefixManager = new DefaultPrefixManager();
  }


  /**
   * Transforms an information model JSON-LD RDF ontology to an OWL ontology
   *
   * @param document TTDocyment - the document to transform
   * @param dmanager TTManager - The Discovery ontology manager
   * @return OWLOntology manager together with one ontology (optional) and a set of prefixes
   * @throws OWLOntologyCreationException if the owl ontology cannot be created
   */

  public OWLOntologyManager transform(TTDocument document, TTManager dmanager, GraphVocab graph) throws OWLOntologyCreationException {

    ttManager = dmanager;
    //if the dmanager is null create it
    if (dmanager == null) {
      ttManager = new TTManager();
      ttManager.setDocument(document);
    }

    //Create ontology
    ontology = manager.createOntology(IRI.create(graph.toString()));

    processPrefixes(document.getPrefixes());
    processEntities(document.getEntities());
    return manager;
  }

  private void processPrefixes(List<TTPrefix> prefixes) {
    for (TTPrefix prefix : prefixes) {
      prefixManager.setPrefix(prefix.getPrefix() + ":", prefix.getIri());
    }
    PrefixDocumentFormat ontologyFormat = new FunctionalSyntaxDocumentFormat();
    ontologyFormat.copyPrefixesFrom(prefixManager);
    manager.setOntologyFormat(ontology, ontologyFormat);
  }

  private void processEntities(List<TTEntity> entities) {
    if (entities == null || entities.isEmpty())
      return;
    int classno = 0;

    for (TTEntity entity : entities) {
      classno = classno + 1;
      IRI iri = getIri(entity.getIri());
      addDeclaration(entity);
      Map<TTIriRefExtended, TTArray> predicates = entity.getPredicateMap();
      processEntityPredicates(entity, predicates, iri);
    }
  }

  private void processEntityPredicates(TTEntity entity, Map<TTIriRefExtended, TTArray> predicates, IRI iri) {
    for (Map.Entry<TTIriRefExtended, TTArray> entry : predicates.entrySet()) {
      if (entry.getKey().equals(new TTIriRefExtended(RdfsVocab.SUBCLASS_OF))) {
        if (!entity.isType(new TTIriRefExtended(RdfVocab.PROPERTY)))
          addSubClassOf(iri, entry.getValue());
        else
          addSubPropertyOf(iri, new TTIriRefExtended(OwlVocab.OBJECT_PROPERTY), entry.getValue());
      } else if (entry.getKey().equals(new TTIriRefExtended(OwlVocab.EQUIVALENT_CLASS))) {
        addEquivalentClasses(iri, entry.getValue());
      } else if (entry.getKey().equals(new TTIriRefExtended(RdfsVocab.SUB_PROPERTY_OF))) {
        addSubPropertyOf(iri, new TTIriRefExtended(OwlVocab.OBJECT_PROPERTY), entry.getValue());
      } else if (entry.getValue().isLiteral())
        addAnnotation(iri, entry.getKey(), entry.getValue().asLiteral());
    }
  }

  private void checkUndeclared(IRI iri, OWLEntity entity) {
    if (ttManager.getEntity(iri.toString()) == null) {
      OWLDeclarationAxiom declaration = dataFactory.getOWLDeclarationAxiom(entity);
      manager.addAxiom(ontology, declaration);
    }
  }

  private void addEquivalentClasses(IRI iri, TTArray eqClasses) {

    for (TTValue exp : eqClasses.iterator()) {
      if (exp.isIriRef() || exp.asNode().get(new TTIriRefExtended(OwlVocab.WITH_RESTRICTIONS)) == null) {
        OWLEquivalentClassesAxiom equAx;
        equAx = dataFactory.getOWLEquivalentClassesAxiom(
          dataFactory.getOWLClass(iri),
          getOWLEquivalentClassExpression(exp));
        manager.addAxiom(ontology, equAx);
      }
    }
  }

  private void addSubPropertyOf(IRI iri, TTIriRef propertyType, TTArray superClasses) {
    for (TTValue exp : superClasses.iterator()) {
      if (propertyType.equals(new TTIriRefExtended(OwlVocab.OBJECT_PROPERTY))) {
        OWLSubObjectPropertyOfAxiom subAx = dataFactory
          .getOWLSubObjectPropertyOfAxiom(
            dataFactory
              .getOWLObjectProperty(iri),
            dataFactory.getOWLObjectProperty(getIri(exp.asIriRef())));

        manager.addAxiom(ontology, subAx);
      } else {
        OWLSubDataPropertyOfAxiom subAx = dataFactory.getOWLSubDataPropertyOfAxiom(
          dataFactory.getOWLDataProperty(iri), dataFactory.getOWLDataProperty(getIri(exp.asIriRef())));
        manager.addAxiom(ontology, subAx);

      }
    }
  }

  private void addSubClassOf(IRI iri, TTArray superClasses) {
    for (TTValue exp : superClasses.iterator()) {
      OWLSubClassOfAxiom subAx;
      subAx = dataFactory.getOWLSubClassOfAxiom(
        dataFactory.getOWLClass(iri),
        getOWLClassExpression(exp));
      manager.addAxiom(ontology, subAx);
    }
  }


  private OWLClassExpression getOPERestrictionAsOWlClassExpression(TTValue cex) {
    OWLObjectPropertyExpression owlOpe;
    TTNode exp = cex.asNode();
    if (exp.get(new TTIriRefExtended(OwlVocab.ON_PROPERTY)) != null) {
      IRI prop = getIri(exp.get(new TTIriRefExtended(OwlVocab.ON_PROPERTY)).asIriRef());
      owlOpe = dataFactory.getOWLObjectProperty(prop);
    } else {
      IRI prop = getIri(exp.get(new TTIriRefExtended(OwlVocab.INVERSE_OF)).asIriRef());
      owlOpe = dataFactory
        .getOWLObjectInverseOf(
          dataFactory.getOWLObjectProperty(prop));
    }
    if (exp.get(new TTIriRefExtended(OwlVocab.ALL_VALUES_FROM)) != null) {
      return dataFactory.getOWLObjectAllValuesFrom(
        owlOpe,
        getOWLClassExpression(exp.get(new TTIriRefExtended(OwlVocab.ALL_VALUES_FROM)).asValue())
      );
    } else if (exp.get(new TTIriRefExtended(OwlVocab.SOME_VALUES_FROM)) != null) {
      return dataFactory.getOWLObjectAllValuesFrom(
        owlOpe,
        getOWLClassExpression(exp.get(new TTIriRefExtended(OwlVocab.SOME_VALUES_FROM)).asValue())
      );
    } else if (exp.get(new TTIriRefExtended(OwlVocab.MIN_CARDINALITY)) != null) {
      return dataFactory.getOWLObjectSomeValuesFrom(
        owlOpe,
        getOWLClassExpression(exp.get(new TTIriRefExtended(OwlVocab.ON_CLASS)).asValue())
      );
    } else if (exp.get(new TTIriRefExtended(OwlVocab.MAX_CARDINALITY)) != null) {
      return dataFactory.getOWLObjectSomeValuesFrom(
        owlOpe,
        getOWLClassExpression(exp.get(new TTIriRefExtended(OwlVocab.ON_CLASS)).asValue())
      );
    } else if (exp.get(new TTIriRefExtended(OwlVocab.ON_CLASS)) != null) {
      return dataFactory.getOWLObjectSomeValuesFrom(
        owlOpe,
        getOWLClassExpression(exp.get(new TTIriRefExtended(OwlVocab.ON_CLASS)).asValue())
      );
    } else {
      return dataFactory.getOWLClass("not sure", prefixManager);
    }

  }

  public OWLClassExpression getOWLClassExpression(TTValue cex) {
    if (cex.isIriRef()) {
      IRI iri = getIri(cex.asIriRef());
      checkUndeclared(iri, dataFactory.getOWLEntity(EntityType.CLASS, iri));
      return dataFactory.getOWLClass(getIri(cex.asIriRef()));
    } else if (cex.isNode()) {
      if (cex.asNode().get(new TTIriRefExtended(OwlVocab.INTERSECTION_OF)) != null) {
        return dataFactory.getOWLObjectIntersectionOf(
          cex.asNode().get(new TTIriRefExtended(OwlVocab.INTERSECTION_OF))
            .stream()
            .map(this::getOWLClassExpression)
            .collect(Collectors.toSet()));
        //
      } else if (cex.asNode().get(new TTIriRefExtended(OwlVocab.UNION_OF)) != null) {
        return dataFactory.getOWLObjectUnionOf(
          cex.asNode().get(new TTIriRefExtended(OwlVocab.UNION_OF))
            .stream()
            .map(this::getOWLClassExpression)
            .collect(Collectors.toSet()));
      } else if (cex.asNode().get(new TTIriRefExtended(OwlVocab.ON_PROPERTY)) != null) {
        return getOPERestrictionAsOWlClassExpression(cex);
      } else if (cex.asNode().get(new TTIriRefExtended(OwlVocab.ONE_OF)) != null) {
        return getOneOfAsOWLClassExpression(cex.asNode().get(new TTIriRefExtended(OwlVocab.ONE_OF)));
      } else if (cex.asNode().get(new TTIriRefExtended(OwlVocab.COMPLEMENT_OF)) != null) {
        return (getComplementOfAsAOWLClassExpression(cex));
      }
    }
    return dataFactory.getOWLClass("not sure of type of expression", prefixManager);

  }


  public OWLClassExpression getOWLEquivalentClassExpression(TTValue cex) {
    if (cex.isIriRef()) {
      IRI iri = getIri(cex.asIriRef());
      checkUndeclared(iri, dataFactory.getOWLEntity(EntityType.CLASS, iri));
      return dataFactory.getOWLClass(getIri(cex.asIriRef()));
    } else if (cex.isNode()) {
      if (cex.asNode().get(new TTIriRefExtended(OwlVocab.INTERSECTION_OF)) != null) {
        return dataFactory.getOWLObjectIntersectionOf(
          cex.asNode().get(new TTIriRefExtended(OwlVocab.INTERSECTION_OF))
            .stream()
            .map(this::getOWLEquivalentClassExpression)
            .collect(Collectors.toSet()));
        //
      } else if (cex.asNode().get(new TTIriRefExtended(OwlVocab.UNION_OF)) != null) {
        return dataFactory.getOWLObjectUnionOf(
          cex.asNode().get(new TTIriRefExtended(OwlVocab.UNION_OF))
            .stream()
            .map(this::getOWLClassExpression)
            .collect(Collectors.toSet()));
      } else if (cex.asNode().get(new TTIriRefExtended(OwlVocab.ON_PROPERTY)) != null) {
        return getOPERestrictionAsOWlClassExpression(cex);
      } else if (cex.asNode().get(new TTIriRefExtended(OwlVocab.ONE_OF)) != null) {
        return getOneOfAsOWLClassExpression(cex.asNode().get(new TTIriRefExtended(OwlVocab.ONE_OF)));
      } else if (cex.asNode().get(new TTIriRefExtended(OwlVocab.COMPLEMENT_OF)) != null) {
        return (getComplementOfAsAOWLClassExpression(cex));
      }
    }
    return dataFactory.getOWLClass("not sure of type of expression", prefixManager);

  }


  private OWLClassExpression getComplementOfAsAOWLClassExpression(TTValue cex) {
    return dataFactory
      .getOWLObjectComplementOf(
        getOWLClassExpression(
          cex.asNode().get(new TTIriRefExtended(OwlVocab.COMPLEMENT_OF)).asValue()));
  }

  private OWLClassExpression getOneOfAsOWLClassExpression(TTArray cex) {
    Set<OWLNamedIndividual> indiList = new HashSet<>();
    for (TTValue oneOf : cex.iterator()) {
      indiList.add(dataFactory.getOWLNamedIndividual(getIri(oneOf.asIriRef())));
    }
    return dataFactory.getOWLObjectOneOf(indiList);
  }

  private void addDeclaration(TTEntity ttEntity) {

    IRI iri = getIri(ttEntity.getIri());
    OWLEntity entity;
    if (ttEntity.isType(new TTIriRefExtended(OwlVocab.CLASS)))
      entity = dataFactory.getOWLEntity(EntityType.CLASS, iri);
    else if (ttEntity.isType(new TTIriRefExtended(ImVocab.CONCEPT)))
      entity = dataFactory.getOWLEntity(EntityType.CLASS, iri);
    else if (ttEntity.isType(new TTIriRefExtended(OwlVocab.OBJECT_PROPERTY)))
      entity = dataFactory.getOWLEntity(EntityType.OBJECT_PROPERTY, iri);
    else if (ttEntity.isType(new TTIriRefExtended(OwlVocab.DATATYPE_PROPERTY)))
      entity = dataFactory.getOWLEntity(EntityType.OBJECT_PROPERTY, iri);
    else if (ttEntity.isType(new TTIriRefExtended(RdfVocab.PROPERTY)))
      entity = dataFactory.getOWLEntity(EntityType.OBJECT_PROPERTY, iri);
    else if (ttEntity.isType(new TTIriRefExtended(OwlVocab.ANNOTATION_PROPERTY)))
      entity = dataFactory.getOWLEntity(EntityType.ANNOTATION_PROPERTY, iri);
    else if (ttEntity.isType(new TTIriRefExtended(OwlVocab.NAMED_INDIVIDUAL)))
      entity = dataFactory.getOWLEntity(EntityType.NAMED_INDIVIDUAL, iri);
    else
      entity = dataFactory.getOWLEntity(EntityType.CLASS, iri);
    OWLDeclarationAxiom declaration = dataFactory.getOWLDeclarationAxiom(entity);
    manager.addAxiom(ontology, declaration);
  }


  private void addAnnotation(IRI iri, TTIriRef property, TTValue value) {
    OWLAnnotation annotation = dataFactory.getOWLAnnotation(
      dataFactory.getOWLAnnotationProperty(getIri(property.getIri())),
      dataFactory.getOWLLiteral(value.asLiteral().getValue()));
    manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(iri, annotation));
  }


  private IRI getIri(TTIriRef tiri) {
    String iri = tiri.getIri();
    return getIri(iri);
  }

  private IRI getIri(String iri) {
    if (iri.toLowerCase().startsWith("http:") || iri.toLowerCase().startsWith("https:"))
      return IRI.create(iri);
    else
      return prefixManager.getIRI(iri);
  }

}

