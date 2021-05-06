package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.TermCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermCodeRepository extends JpaRepository<TermCode, Integer> {

    @Query(value = "SELECT t.term " +
        "FROM concept c " +
        "JOIN term_code t ON t.concept = c.dbid " +
        "WHERE c.iri = :iri  " +
        "ORDER BY t.term ", nativeQuery = true)
    public List<String> getSynonyms(@Param("iri") String iri );
}
