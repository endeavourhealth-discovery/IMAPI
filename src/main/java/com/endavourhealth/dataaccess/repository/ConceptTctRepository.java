package com.endavourhealth.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.ConceptTct;

@Repository
public interface ConceptTctRepository extends JpaRepository<ConceptTct, String>{

}
