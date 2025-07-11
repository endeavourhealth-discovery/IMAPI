package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * Converts Discovery JSON Triple tree syntax document to OWL EL functional syntax using an OWL factory.
 * Note that this is a limited transform for the purposes of EL based inferrencing using a reasoner. DL axioms are ignored or converted
 * to EL similar structures
 * For example property ranges domains are ignored if present. Cardinality restrictions are converted to existential quantification.
 * Data type restrictions are ignored.
 * <p>Thus a transform back from the OWL EL version will not match the source unless the source is EL only.
 */
public class TTToOWLEL {
  private DefaultPrefixManager prefixManager;
  private OWLDataFactory dataFactory;
  private OWLOntologyManager manager;
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

  public OWLOntologyManager transform(TTDocument document, TTManager dmanager, Graph graph) throws OWLOntologyCreationException {

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
      Map<TTIriRef, TTArray> predicates = entity.getPredicateMap();
      processEntityPredicates(entity, predicates, iri);
    }
  }

  private void processEntityPredicates(TTEntity entity, Map<TTIriRef, TTArray> predicates, IRI iri) {
    for (Map.Entry<TTIriRef, TTArray> entry : predicates.entrySet()) {
      if (entry.getKey().equals(iri(RDFS.SUBCLASS_OF))) {
        if (!entity.isType(iri(RDF.PROPERTY)))
          addSubClassOf(iri, entry.getValue());
        else
          addSubPropertyOf(iri, iri(OWL.OBJECT_PROPERTY), entry.getValue());
      } else if (entry.getKey().equals(iri(OWL.EQUIVALENT_CLASS))) {
        addEquivalentClasses(iri, entry.getValue());
      } else if (entry.getKey().equals(iri(RDFS.SUB_PROPERTY_OF))) {
        addSubPropertyOf(iri, iri(OWL.OBJECT_PROPERTY), entry.getValue());
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
      if (exp.isIriRef() || exp.asNode().get(iri(OWL.WITH_RESTRICTIONS)) == null) {
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
      if (propertyType.equals(iri(OWL.OBJECT_PROPERTY))) {
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
    if (exp.get(iri(OWL.ON_PROPERTY)) != null) {
      IRI prop = getIri(exp.get(iri(OWL.ON_PROPERTY)).asIriRef());
      owlOpe = dataFactory.getOWLObjectProperty(prop);
    } else {
      IRI prop = getIri(exp.get(iri(OWL.INVERSE_OF)).asIriRef());
      owlOpe = dataFactory
        .getOWLObjectInverseOf(
          dataFactory.getOWLObjectProperty(prop));
    }
    if (exp.get(iri(OWL.ALL_VALUES_FROM)) != null) {
      return dataFactory.getOWLObjectAllValuesFrom(
        owlOpe,
        getOWLClassExpression(exp.get(iri(OWL.ALL_VALUES_FROM)).asValue())
      );
    } else if (exp.get(iri(OWL.SOME_VALUES_FROM)) != null) {
      return dataFactory.getOWLObjectAllValuesFrom(
        owlOpe,
        getOWLClassExpression(exp.get(iri(OWL.SOME_VALUES_FROM)).asValue())
      );
    } else if (exp.get(iri(OWL.MIN_CARDINALITY)) != null) {
      return dataFactory.getOWLObjectSomeValuesFrom(
        owlOpe,
        getOWLClassExpression(exp.get(iri(OWL.ON_CLASS)).asValue())
      );
    } else if (exp.get(iri(OWL.MAX_CARDINALITY)) != null) {
      return dataFactory.getOWLObjectSomeValuesFrom(
        owlOpe,
        getOWLClassExpression(exp.get(iri(OWL.ON_CLASS)).asValue())
      );
    } else if (exp.get(iri(OWL.ON_CLASS)) != null) {
      return dataFactory.getOWLObjectSomeValuesFrom(
        owlOpe,
        getOWLClassExpression(exp.get(iri(OWL.ON_CLASS)).asValue())
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
      if (cex.asNode().get(iri(OWL.INTERSECTION_OF)) != null) {
        return dataFactory.getOWLObjectIntersectionOf(
          cex.asNode().get(iri(OWL.INTERSECTION_OF))
            .stream()
            .map(this::getOWLClassExpression)
            .collect(Collectors.toSet()));
        //
      } else if (cex.asNode().get(iri(OWL.UNION_OF)) != null) {
        return dataFactory.getOWLObjectUnionOf(
          cex.asNode().get(iri(OWL.UNION_OF))
            .stream()
            .map(this::getOWLClassExpression)
            .collect(Collectors.toSet()));
      } else if (cex.asNode().get(iri(OWL.ON_PROPERTY)) != null) {
        return getOPERestrictionAsOWlClassExpression(cex);
      } else if (cex.asNode().get(iri(OWL.ONE_OF)) != null) {
        return getOneOfAsOWLClassExpression(cex.asNode().get(iri(OWL.ONE_OF)));
      } else if (cex.asNode().get(iri(OWL.COMPLEMENT_OF)) != null) {
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
      if (cex.asNode().get(iri(OWL.INTERSECTION_OF)) != null) {
        return dataFactory.getOWLObjectIntersectionOf(
          cex.asNode().get(iri(OWL.INTERSECTION_OF))
            .stream()
            .map(this::getOWLEquivalentClassExpression)
            .collect(Collectors.toSet()));
        //
      } else if (cex.asNode().get(iri(OWL.UNION_OF)) != null) {
        return dataFactory.getOWLObjectUnionOf(
          cex.asNode().get(iri(OWL.UNION_OF))
            .stream()
            .map(this::getOWLClassExpression)
            .collect(Collectors.toSet()));
      } else if (cex.asNode().get(iri(OWL.ON_PROPERTY)) != null) {
        return getOPERestrictionAsOWlClassExpression(cex);
      } else if (cex.asNode().get(iri(OWL.ONE_OF)) != null) {
        return getOneOfAsOWLClassExpression(cex.asNode().get(iri(OWL.ONE_OF)));
      } else if (cex.asNode().get(iri(OWL.COMPLEMENT_OF)) != null) {
        return (getComplementOfAsAOWLClassExpression(cex));
      }
    }
    return dataFactory.getOWLClass("not sure of type of expression", prefixManager);

  }


  private OWLClassExpression getComplementOfAsAOWLClassExpression(TTValue cex) {
    return dataFactory
      .getOWLObjectComplementOf(
        getOWLClassExpression(
          cex.asNode().get(iri(OWL.COMPLEMENT_OF)).asValue()));
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
    if (ttEntity.isType(iri(OWL.CLASS)))
      entity = dataFactory.getOWLEntity(EntityType.CLASS, iri);
    else if (ttEntity.isType(iri(IM.CONCEPT)))
      entity = dataFactory.getOWLEntity(EntityType.CLASS, iri);
    else if (ttEntity.isType(iri(OWL.OBJECT_PROPERTY)))
      entity = dataFactory.getOWLEntity(EntityType.OBJECT_PROPERTY, iri);
    else if (ttEntity.isType(iri(OWL.DATATYPE_PROPERTY)))
      entity = dataFactory.getOWLEntity(EntityType.OBJECT_PROPERTY, iri);
    else if (ttEntity.isType(iri(RDF.PROPERTY)))
      entity = dataFactory.getOWLEntity(EntityType.OBJECT_PROPERTY, iri);
    else if (ttEntity.isType(iri(OWL.ANNOTATION_PROPERTY)))
      entity = dataFactory.getOWLEntity(EntityType.ANNOTATION_PROPERTY, iri);
    else if (ttEntity.isType(iri(OWL.NAMED_INDIVIDUAL)))
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

