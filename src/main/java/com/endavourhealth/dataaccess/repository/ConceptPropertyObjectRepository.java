package com.endavourhealth.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;

@Repository
public interface ConceptPropertyObjectRepository extends JpaRepository<ConceptPropertyObject, String>{
	List<ConceptPropertyObject> findByConcept(int concept);
}
