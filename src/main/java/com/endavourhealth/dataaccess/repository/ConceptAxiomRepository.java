package com.endavourhealth.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.ConceptAxiom;

@Repository
public interface ConceptAxiomRepository extends JpaRepository<ConceptAxiom, Integer>{
	
	List<ConceptAxiom> findByConcept(int concept);

}
