package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.ConceptTct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ConceptTctRepository extends JpaRepository<ConceptTct, String> {

	Set<ConceptTct> findBySource_Iri_AndTarget_IriIn(String iri, List<String> candidates);

    Set<ConceptTct> findBySource_IriOrderByLevel(String iri);

    Set<ConceptTct> findByTarget_Iri(String iri);
}
