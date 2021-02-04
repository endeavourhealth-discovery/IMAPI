package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a synonym or a key to a concept, may or may not have a term code.
 */
public class TermCode {
   private String term;
   private String code;
   private List<String> key;


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

   @JsonProperty("Key")
   public List<String> getKey() {
      return key;
   }

   public TermCode setKey(List<String> key) {
      this.key = key;
      return this;
   }
   public TermCode addKey(String key){
      if (this.key==null)
         this.key = new ArrayList<>();
      this.key.add(key);
      return this;
   }
}
