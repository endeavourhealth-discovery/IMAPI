package org.endeavourhealth.imapi.model;

/**
 * Holds a synonym or a key to a concept, may or may not have a term code.
 */
public class Synonym {
   private String term;
   private String termCode;


   public Synonym(){}
   public Synonym(String term,String code){
      this.term=term;
      this.termCode=code;
   }

   public String getTerm() {
      return term;
   }

   public Synonym setTerm(String term) {
      this.term = term;
      return this;
   }

   public String getTermCode() {
      return termCode;
   }

   public Synonym setTermCode(String termCode) {
      this.termCode = termCode;
      return this;
   }
}
