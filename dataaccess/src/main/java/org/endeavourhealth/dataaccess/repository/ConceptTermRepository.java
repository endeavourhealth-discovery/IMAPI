package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.ConceptTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConceptTermRepository extends JpaRepository<ConceptTerm, Integer> {

    @Query(value = "SELECT t.term " +
        "FROM concept c " +
        "JOIN concept_term t ON t.concept = c.dbid " +
        "WHERE c.iri = :iri  " +
        "ORDER BY t.term ", nativeQuery = true)
    public List<String> getSynonyms(@Param("iri") String iri );
}
