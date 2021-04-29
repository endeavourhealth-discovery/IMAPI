package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TTIriRef extends TTValue {
    public static TTIriRef iri(String iri) {
        return new TTIriRef(iri);
    }

    public static TTIriRef typedIri(String iri,TTIriRef iriType){
        return new TTIriRef(iri,iriType);
    }

    public static TTIriRef iri(String iri, String name) {
        return new TTIriRef(iri, name);
    }
    public static TTIriRef typedIri(TTIriRef iri,TTIriRef iriType){
        return new TTIriRef(iri.getIri()).setIriType(iriType);
    }


    private String iri;
    private String name;
    private TTIriRef iriType;


    public TTIriRef() {
    }
    public TTIriRef(String iri) {
        this.iri = iri;
    }
    public TTIriRef(String iri, String name) {
        this.iri = iri;
        this.name = name;
    }
    public TTIriRef(String iri, String name,String iriType) {
        this.iri = iri;
        this.name = name;
        this.iriType= iri(iriType);
    }
    public TTIriRef(String iri, String name,TTIriRef iriType) {
        this.iri = iri;
        this.name = name;
        this.iriType= iriType;
    }
    public TTIriRef(String iri, TTIriRef iriType) {
        this.iri = iri;
        this.iriType= iriType;
    }


    @JsonProperty("@id")
    public String getIri() {
        return this.iri;
    }

    public TTIriRef setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public TTIriRef setName(String name) {
        this.name = name;
        return this;
    }
    public TTIriRef getIriType() {
        return iriType;
    }

    public TTIriRef setIriType(String iriType) {
        this.iriType = iri(iriType);
        return this;
    }
    public TTIriRef setIriType(TTIriRef iriType) {
        this.iriType = iriType;
        return this;
    }

    @Override
    public TTIriRef asIriRef() {
        return this;
    }

    @Override
    @JsonIgnore
    public boolean isIriRef() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TTIriRef ttIriRef = (TTIriRef) o;
        return iri.equals(ttIriRef.iri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri);
    }

    @Override
    public boolean isTypedIri(){
        if (iriType==null)
            return false;
        return true;
    }
    @Override
    public TTIriRef asTypedIri() {
        if (iriType==null)
            return null;
        else
            return this;
    }





}
