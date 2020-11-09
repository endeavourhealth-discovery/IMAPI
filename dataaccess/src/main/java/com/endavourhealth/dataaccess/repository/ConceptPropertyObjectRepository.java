package com.endavourhealth.dataaccess.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;

@Repository
public interface ConceptPropertyObjectRepository extends JpaRepository<ConceptPropertyObject, String>{
	
	List<ConceptPropertyObject> findByConceptDbid(int conceptDbid);
	List<ConceptPropertyObject> findByPropertyDbid(int propertyDbid);
	List<ConceptPropertyObject> findByObjectDbid(int objectDbid);

    Set<ConceptPropertyObject> findByConcept_IriAndProperty_Iri(String conceptIri, String propertyIri);
    Set<ConceptPropertyObject> findByProperty_IriAndObject_Iri(String propertyIri, String objectIri);
}
