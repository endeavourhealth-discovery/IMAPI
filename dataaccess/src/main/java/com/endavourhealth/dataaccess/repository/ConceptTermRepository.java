package com.endavourhealth.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.ConceptTerm;

@Repository
public interface ConceptTermRepository extends JpaRepository<ConceptTerm, String>{

}
