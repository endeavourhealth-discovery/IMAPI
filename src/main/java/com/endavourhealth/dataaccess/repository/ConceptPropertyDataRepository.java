package com.endavourhealth.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.ConceptPropertyData;

@Repository
public interface ConceptPropertyDataRepository extends JpaRepository<ConceptPropertyData, String> {
	
	List<ConceptPropertyData> findByConcept(String concept);

}
