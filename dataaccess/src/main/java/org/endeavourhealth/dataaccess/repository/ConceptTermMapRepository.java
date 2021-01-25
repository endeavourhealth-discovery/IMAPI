package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.ConceptTermMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptTermMapRepository extends JpaRepository<ConceptTermMap, String>{

}
