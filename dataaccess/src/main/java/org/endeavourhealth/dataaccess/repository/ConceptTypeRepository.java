package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.ConceptType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConceptTypeRepository extends JpaRepository<ConceptType, Integer> {

    List<ConceptType> findAllByConcept_Dbid(Integer dbid);


}
