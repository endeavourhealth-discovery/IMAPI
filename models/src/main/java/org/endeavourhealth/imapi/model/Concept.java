package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="conceptType")
@JsonSubTypes({
    @JsonSubTypes.Type(value= Concept.class, name="Class"),
    @JsonSubTypes.Type(value= ObjectProperty.class, name="ObjectProperty"),
    @JsonSubTypes.Type(value= DataProperty.class, name="DataProperty"),
    @JsonSubTypes.Type(value= DataType.class, name="DataType"),
    @JsonSubTypes.Type(value= AnnotationProperty.class, name="Annotation")})
@JsonPropertyOrder({"conceptType","id","status","version","iri","name","description",
        "code","scheme","annotations","expression","subClassOf",",equivalentTo","DisjointWith"})
public class Concept implements IMAnnotated {
    private Integer dbid;
    private String iri;
    private String name;
    private String description;
    private String code;
    private ConceptReference scheme;
    private ConceptStatus status;
    private Integer version;
    private Set<ConceptReference> isA;
    private Set<Annotation> annotations;
    private ConceptType conceptType;
    private Set<ClassAxiom> subClassOf;
    private Set<ClassAxiom> equivalentTo;
    private ClassExpression expression;
    private Set<ConceptReference> DisjointWith;
    private List<Synonym> synonym;
    private boolean isRef;



    /**
     * Alternative constructor passing in the preferred concept type
     * @param conceptType
     */
    public Concept(ConceptType conceptType){
        this.setConceptType(conceptType);
    }

    public Concept() {
        this.setConceptType(ConceptType.CLASSONLY);
    }

    @Override
    public Integer getDbid() {
        return dbid;
    }

    @Override
    public Concept setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }


    @Override
    public ConceptStatus getStatus() {
        return status;
    }

    @Override
    public Concept setStatus(ConceptStatus status) {
        this.status=status;
        return this;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public Concept setVersion(Integer version) {
        this.version= version;
        return this;
    }



    public Concept(String iri, String name) {
        this.iri = iri;
        this.name = name;
    }

    public String getIri() {
        return iri;
    }

    public Concept setIri(String iri) {
        this.iri = iri;
        return this;
    }



    public String getName() {
        return name;
    }

    public Concept setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Concept setDescription(String description) {
        this.description = description;
        return this;
    }

    public ConceptReference getScheme() {
        return scheme;
    }

    public Concept setScheme(ConceptReference scheme) {
        this.scheme = scheme;
        return this;
    }

    @JsonIgnore
    public Concept setScheme(String scheme) {
        this.scheme = new ConceptReference(scheme);
        return this;
    }

    public String getCode() {
        return code;
    }

    public Concept setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Returns a list of IRIs which are the direct superconcepts of this concept
     * <p>This may include asserted superclasses and super properties and equivalent classes as well as those generated by a reasoner</p>
     * @return list of super concepts
     */
    @JsonProperty("IsA")
    public Set<ConceptReference> getIsA() {
        return isA;
    }

    public Concept setIsA(Set<ConceptReference> isA) {
        this.isA = isA;
        return this;
    }
    public Concept addIsa(ConceptReference isa){
        if (this.isA==null)
            isA= new HashSet<>();
        isA.add(isa);
        return this;
    }

    @JsonProperty("annotations")
    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    public Concept setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
        return this;
    }
    public Concept addAnnotation(Annotation annotation) {
        if (this.annotations==null)
            this.annotations= new HashSet<>();
        this.annotations.add(annotation);
        return this;
    }



    @JsonProperty("conceptType")
    @JsonIgnore
    public ConceptType getConceptType() {
        return conceptType;
    }

    @JsonIgnore
    public Concept setConceptType(ConceptType conceptType) {
        this.conceptType = conceptType;
        return this;
    }
    @JsonProperty("DisjointWith")
    public Set<ConceptReference> getDisjointWith() {
        return DisjointWith;
    }

    public Concept setDisjointWith(Set<ConceptReference> DisjointWith) {
        this.DisjointWith = DisjointWith;
        return this;
    }
    public Concept addDisjointWith(ConceptReference iri) {
        if (this.DisjointWith == null)
            this.DisjointWith = new HashSet<>();
        this.DisjointWith.add(iri);
        return this;
    }

    public Concept addDisjointWith(String iri) {
        if (this.DisjointWith == null)
            this.DisjointWith = new HashSet<>();
        this.DisjointWith.add(new ConceptReference(iri));
        return this;
    }

    @JsonProperty("Expression")
    public ClassExpression getExpression(){
        return expression;
    }
    public Concept setExpression(ClassExpression cex){
        this.expression = cex;
        return this;
    }

    @JsonProperty("SubClassOf")
    public Set<ClassAxiom> getSubClassOf() {
        return subClassOf;
    }

    public Concept setSubClassOf(Set<ClassAxiom> subClassOf) {
        this.subClassOf = subClassOf;
        return this;
    }

    public Concept addSubClassOf(ClassAxiom subClassOf) {
        if (this.subClassOf == null)
            this.subClassOf = new HashSet<>();

        this.subClassOf.add(subClassOf);
        return this;
    }

    @JsonProperty("EquivalentTo")
    public Set<ClassAxiom> getEquivalentTo() {
        return equivalentTo;
    }

    public Concept setEquivalentTo(Set<ClassAxiom> equivalentTo) {
        this.equivalentTo = equivalentTo;
        return this;
    }

    public Concept addEquivalentTo(ClassAxiom equivalentTo) {
        if (this.equivalentTo == null)
            this.equivalentTo = new HashSet<>();
        this.equivalentTo.add(equivalentTo);
        return this;
    }

    public List<Synonym> getSynonym() {
        return synonym;
    }

    public Concept setSynonym(List<Synonym> synonym) {
        this.synonym = synonym;
        return this;
    }
    public Concept addSynonym(Synonym synonym){
        if (this.synonym==null)
            this.synonym= new ArrayList<>();
        this.synonym.add(synonym);
        return this;
    }

    public boolean isRef() {
        return isRef;
    }

    public Concept setRef(boolean ref) {
        isRef = ref;
        return this;
    }
}
