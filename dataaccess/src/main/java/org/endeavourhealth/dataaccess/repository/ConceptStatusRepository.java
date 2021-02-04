package org.endeavourhealth.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.endeavourhealth.dataaccess.entity.ConceptStatus;

@Repository
public interface ConceptStatusRepository extends JpaRepository<ConceptStatus, String>{

}
