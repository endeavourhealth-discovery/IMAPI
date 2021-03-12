package org.endeavourhealth.imapi.model;

import org.endeavourhealth.imapi.model.tripletree.TTConcept;

import java.util.HashSet;
import java.util.Set;

public class ModelDocument {
   private String graphIri;
   private Set<Namespace> prefixes;
   private Set<TTConcept> concept;
   private Set<TTConcept> individual;
   private Set<TermCode> termCode;

   public String getGraphIri() {
      return graphIri;
   }

   public ModelDocument setGraphIri(String graphIri) {
      this.graphIri = graphIri;
      return this;
   }

   public Set<Namespace> getPrefixes() {
      return prefixes;
   }

   public ModelDocument setPrefixes(Set<Namespace> prefixes) {
      this.prefixes = prefixes;
      return this;
   }

   public ModelDocument addPrefixes(Namespace namespace){
      if (this.prefixes==null)
         this.prefixes= new HashSet<>();
      this.prefixes.add(namespace);
      return this;
   }

   public Set<TTConcept> getConcept() {
      return concept;
   }

   public ModelDocument setConcept(Set<TTConcept> concept) {
      this.concept = concept;
      return this;
   }

   public ModelDocument addConcept(TTConcept concept){
      if (this.concept==null)
         this.concept= new HashSet<>();
      this.concept.add(concept);
      return this;
   }

   public Set<TTConcept> getIndividual() {
      return individual;
   }

   public ModelDocument setIndividual(Set<TTConcept> individual) {
      this.individual = individual;
      return this;
   }

   public ModelDocument addIndividual(TTConcept individual){
      if (this.individual==null)
         this.individual= new HashSet<>();
      this.individual.add(individual);
      return this;
   }

   public Set<TermCode> getTermCode() {
      return termCode;
   }

   public ModelDocument setTermCode(Set<TermCode> termCode) {
      this.termCode = termCode;
      return this;
   }
}
