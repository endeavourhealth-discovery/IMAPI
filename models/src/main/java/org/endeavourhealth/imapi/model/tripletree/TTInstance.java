package org.endeavourhealth.imapi.model.tripletree;

public class TTInstance extends TTNode{
  private String iri;
  private TTIriRef type;

   public String getIri() {
      return iri;
   }

   public TTInstance setIri(String iri) {
      this.iri = iri;
      return this;
   }

   public TTIriRef getType() {
      return type;
   }

   public TTInstance setType(TTIriRef type) {
      this.type = type;
      return this;
   }
}
