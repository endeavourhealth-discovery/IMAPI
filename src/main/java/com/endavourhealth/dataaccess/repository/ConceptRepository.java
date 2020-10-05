package com.endavourhealth.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.Concept;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, String>{

}
