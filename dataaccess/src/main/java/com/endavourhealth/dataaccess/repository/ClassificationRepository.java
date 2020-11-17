package com.endavourhealth.dataaccess.repository;

import com.endavourhealth.dataaccess.entity.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Integer>{
    Set<Classification> findByParent_Iri(String parentIri);
    Set<Classification> findByChild_Iri(String childIri);
}
