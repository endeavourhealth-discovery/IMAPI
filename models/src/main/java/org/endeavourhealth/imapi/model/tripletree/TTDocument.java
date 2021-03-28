package org.endeavourhealth.imapi.model.tripletree;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonSerialize(using = TTDocumentSerializer.class)
@JsonDeserialize(using = TTDocumentDeserializer.class)
public class TTDocument extends TTNode{
   private TTIriRef graph;
   private List<TTPrefix> prefixes = new ArrayList<>();
   private List<TTConcept> concepts;
   private List<TTConcept> individuals;
   private Map<Class,List<String>> predicateTemplate;

   public TTIriRef getGraph() {
      return graph;
   }

   public TTDocument(TTIriRef defaultGraph){
      this.graph= defaultGraph;
   }
   public TTDocument(){
   }
   public TTDocument(Map<Class,List<String>> predicateTemplate){
      this.predicateTemplate= predicateTemplate;
   }

   public TTDocument setGraph(TTIriRef graph) {
      this.graph = graph;
      return this;
   }

   public List<TTPrefix> getPrefixes() {
      return prefixes;
   }

   public TTDocument setPrefixes(List<TTPrefix> prefixes) {
      this.prefixes = prefixes;
      return this;
   }
   public TTDocument addPrefix(TTPrefix prefix) {
      if (prefix != null) {
         if (prefixes == null) {
            prefixes = new ArrayList<>();
         }
         prefixes.add(prefix);
      }
      return this;
   }
   public TTDocument addPrefix(String iri, String prefix) {
      return addPrefix(new TTPrefix(iri, prefix));
   }

   public Map<Class, List<String>> getPredicateTemplate() {
      return predicateTemplate;
   }

   public TTDocument setPredicateTemplate(Map<Class, List<String>> predicateTemplate) {
      this.predicateTemplate = predicateTemplate;
      return this;
   }

   @Override
   public TTDocument set(TTIriRef predicate, TTValue value) {
      super.set(predicate, value);
      return this;
   }

   public List<TTConcept> getConcepts() {
      return concepts;
   }

   public TTDocument setConcepts(List<TTConcept> concepts) {
      this.concepts = concepts;
      return this;
   }
   public TTDocument addConcept(TTConcept concept){
      if (this.concepts==null)
         this.concepts= new ArrayList<>();
      this.concepts.add(concept);
      return this;
   }

   public List<TTConcept> getIndividuals() {
      return individuals;
   }

   public TTDocument setIndividuals(List<TTConcept> individuals) {
      this.individuals = individuals;
      return this;
   }
   public TTDocument addIndividual(TTConcept concept){
      if (this.individuals==null)
         this.individuals= new ArrayList<>();
      this.individuals.add(concept);
      return this;
   }
}
