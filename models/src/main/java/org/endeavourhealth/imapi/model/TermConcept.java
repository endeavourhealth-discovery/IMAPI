package org.endeavourhealth.imapi.model;

public class TermConcept {
   private String iri;
   private String term;
   private String code;

   public TermConcept(String iri, String term, String code) {
      this.iri = iri;
      this.term = term;
      this.code = code;
   }

   public String getIri() {
      return iri;
   }

   public TermConcept setIri(String iri) {
      this.iri = iri;
      return this;
   }

   public String getTerm() {
      return term;
   }

   public TermConcept setTerm(String term) {
      this.term = term;
      return this;
   }

   public String getCode() {
      return code;
   }

   public TermConcept setCode(String code) {
      this.code = code;
      return this;
   }
}
