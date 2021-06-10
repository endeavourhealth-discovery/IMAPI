package org.endeavourhealth.imapi.model.tripletree.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.io.IOException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * JSON LD- Serializer for TTDocument (triple tree node with collection of concepts)
 * <p>Uses @context for prefixes and common annotation elements</p>
 */
public class TTDocumentSerializer extends StdSerializer<TTDocument> {
   private List<TTIriRef> predicateTemplate;
   private TTNodeSerializer helper;

   public TTDocumentSerializer() {
      this(null);
   }

   public TTDocumentSerializer(Class<TTDocument> t) {
      super(t);
   }


   @Override
   public void serialize(TTDocument document, JsonGenerator gen, SerializerProvider prov) throws IOException {
        Boolean usePrefixes = (Boolean) prov.getAttribute(TTContext.OUTPUT_CONTEXT);
        usePrefixes = (usePrefixes != null && usePrefixes);

      setPredicateOrder();
      helper= new TTNodeSerializer(document.getContext(), predicateTemplate, usePrefixes);
      gen.writeStartObject();
      helper.serializeContexts(document.getPrefixes(), gen);
      if (document.getGraph()!=null) {
         gen.writeFieldName("@graph");
         gen.writeStartObject();
         gen.writeStringField("@id", helper.prefix(document.getGraph().getIri()));
         gen.writeEndObject();
      }
      if (document.getCrudOperation()!=null){
         gen.writeFieldName("crudOperation");
         TTIriRef ref = document.getCrudOperation().asIriRef();
         gen.writeStartObject();
         gen.writeStringField("@id", helper.prefix(ref.getIri()));
         if (ref.getName() != null && !ref.getName().isEmpty())
            gen.writeStringField("name", ref.getName());
         gen.writeEndObject();
      }
      if (document.getConcepts()!=null&&!document.getConcepts().isEmpty()) {
         gen.writeArrayFieldStart("concepts");
         for (TTConcept concept: document.getConcepts()){
            gen.writeStartObject();
            gen.writeStringField("@id",helper.prefix(concept.getIri()));
            helper.serializeNode(concept, gen);
            gen.writeEndObject();
         }
         gen.writeEndArray();
      }
      if (document.getIndividuals()!=null&&!document.getIndividuals().isEmpty()) {
         gen.writeArrayFieldStart("individuals");
         for (TTInstance instance: document.getIndividuals()){
            gen.writeStartObject();
            if (instance.getIri()!=null)
               gen.writeStringField("@id",helper.prefix(instance.getIri()));
            helper.serializeNode(instance, gen);
            gen.writeEndObject();
         }
         gen.writeEndArray();
      }
      if (document.getTransactions()!=null&&!document.getTransactions().isEmpty()) {
         gen.writeArrayFieldStart("transactions");
         for (TTTransaction instance: document.getTransactions()){
            gen.writeStartObject();
            if (instance.getCrud()!=null)
               gen.writeStringField("crud",helper.prefix(instance.getCrud().getIri()));
            if (instance.getIri()!=null)
               gen.writeStringField("@id",helper.prefix(instance.getIri()));
            helper.serializeNode(instance, gen);
            gen.writeEndObject();
         }
         gen.writeEndArray();
      }

      gen.writeEndObject();
   }

   private void setPredicateOrder() {
      predicateTemplate = new ArrayList<>();
      predicateTemplate.add(RDF.TYPE);
      predicateTemplate.add(RDFS.LABEL);
      predicateTemplate.add(RDFS.COMMENT);
      predicateTemplate.add(IM.CODE);
      predicateTemplate.add(IM.HAS_SCHEME);
      predicateTemplate.add(IM.STATUS);
      predicateTemplate.add(IM.IS_A);
   }

}

