package org.endeavourhealth.imapi.model.tripletree;

import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class TTInstance extends TTNode{
  private String iri;
   private TTContext context = new TTContext();

   public String getIri() {
      return iri;
   }

   public TTInstance setIri(String iri) {
      this.iri = iri;
      return this;
   }

   public TTIriRef getTypeOf() {
      return getAsIriRef(RDF.TYPE);
   }

   public TTInstance setTypeOf(TTIriRef type) {
      set(RDF.TYPE, type.setIriType(OWL.CLASS));
      return this;
   }

   public TTContext getContext() {
      return context;
   }

   public TTInstance setContext(TTContext context) {
      this.context = context;
      return this;
   }
   // Utility methods for common predicates
   public TTInstance setName (String name) {
      set(RDFS.LABEL, literal(name));
      return this;
   }

   public String getName() {
      TTLiteral literal = getAsLiteral(RDFS.LABEL);
      return (literal == null) ? null : literal.getValue();
   }
}
