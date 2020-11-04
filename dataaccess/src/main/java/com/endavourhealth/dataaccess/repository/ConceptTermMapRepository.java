package com.endavourhealth.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.ConceptTermMap;

@Repository
public interface ConceptTermMapRepository extends JpaRepository<ConceptTermMap, String>{

}
