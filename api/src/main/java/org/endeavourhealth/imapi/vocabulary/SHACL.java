package org.endeavourhealth.imapi.vocabulary;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum SHACL implements Vocabulary {
   NAMESPACE("http://www.w3.org/ns/shacl#"),
   PREFIX("sh"),
   PATH(NAMESPACE.iri +"path"),
   MININCLUSIVE(NAMESPACE.iri +"minInclusive"),
   MINEXCLUSIVE(NAMESPACE.iri +"minExclusive"),
   MAXINCLUSIVE(NAMESPACE.iri +"maxInclusive"),
   MAXEXCLUSIVE(NAMESPACE.iri +"maxExclusive"),
   PROPERTY(NAMESPACE.iri +"property"),
   PROPERTY_GROUP(NAMESPACE.iri +"PropertyGroup"),
   MINCOUNT(NAMESPACE.iri +"minCount"),
   MAXCOUNT(NAMESPACE.iri +"maxCount"),
   VALUE(NAMESPACE.iri +"value"),
   PATTERN(NAMESPACE.iri +"pattern"),
   INVERSEPATH(NAMESPACE.iri +"inversePath"),
   CLASS(NAMESPACE.iri +"class"),
   DATATYPE(NAMESPACE.iri +"datatype"),
   SPARQL(NAMESPACE.iri +"sparql"),
   SELECT(NAMESPACE.iri +"select"),
   PARAMETER(NAMESPACE.iri +"parameter"),
   IRI(NAMESPACE.iri +"IRI"),
   OPTIONAL(NAMESPACE.iri +"optional"),
   NODESHAPE(NAMESPACE.iri +"NodeShape"),
   TARGETCLASS(NAMESPACE.iri +"targetClass"),
   NODE(NAMESPACE.iri +"node"),
   ORDER(NAMESPACE.iri +"order"),
   OR(NAMESPACE.iri +"or"),
   NOT(NAMESPACE.iri +"not"),
   NODE_KIND(NAMESPACE.iri +"nodeKind"),
   PROPERTYSHAPE(NAMESPACE.iri +"PropertyShape"),
   AND(NAMESPACE.iri +"and"),
   NODES(NAMESPACE.iri +"nodes"),
   TARGET_TYPE(NAMESPACE.iri +"targetType"),
   TARGET(NAMESPACE.iri +"target"),
   SPARQL_TARGET(NAMESPACE.iri +"SPARQLTarget"),
   FUNCTION(NAMESPACE.iri +"FunctionClause"),
   RETURN_TYPE(NAMESPACE.iri +"returnType"),
   GROUP(NAMESPACE.iri +"group"),
   NAME(NAMESPACE.iri +"name"),
   EXPRESSION(NAMESPACE.iri +"expression"),
   HAS_VALUE(NAMESPACE.iri +"hasValue");

   public final String iri;
   SHACL(String iri) {
      this.iri = iri;
   }

   @Override
   public TTIriRef asTTIriRef() {
      return iri(this.iri);
   }

   @Override
   @JsonValue
   public String getIri() {
      return iri;
   }

   public static boolean contains(String iri) {
      try {
         SHACL.valueOf(iri);
         return true;
      } catch (IllegalArgumentException e) {
         return false;
      }
   }

    @Override
    public String toString() {
        return iri;
    }
}

