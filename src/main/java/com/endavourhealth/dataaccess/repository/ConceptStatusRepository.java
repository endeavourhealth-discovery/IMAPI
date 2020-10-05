package com.endavourhealth.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.ConceptStatus;

@Repository
public interface ConceptStatusRepository extends JpaRepository<ConceptStatus, String>{

}
