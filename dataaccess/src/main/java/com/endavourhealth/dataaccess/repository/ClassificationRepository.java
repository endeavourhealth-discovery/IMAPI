package com.endavourhealth.dataaccess.repository;

import com.endavourhealth.dataaccess.entity.Classification;
import com.endavourhealth.dataaccess.entity.ConceptAxiom;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Integer>{
    Set<Classification> findByParent_Iri(String parentIri);
    Set<Classification> findByChild_Iri(String childIri);
}
