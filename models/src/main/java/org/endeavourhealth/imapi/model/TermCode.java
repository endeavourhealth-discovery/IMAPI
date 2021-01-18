package org.endeavourhealth.imapi.model;

/**
 * Holds a synonym or a key to a concept, may or may not have a term code.
 */
public class TermCode {
   private String term;
   private String code;


   public TermCode(){}
   public TermCode(String term, String code){
      this.term=term;
      this.code=code;
   }

   public String getTerm() {
      return term;
   }

   public TermCode setTerm(String term) {
      this.term = term;
      return this;
   }

   public String getCode() {
      return code;
   }

   public TermCode setCode(String code) {
      this.code = code;
      return this;
   }
}
