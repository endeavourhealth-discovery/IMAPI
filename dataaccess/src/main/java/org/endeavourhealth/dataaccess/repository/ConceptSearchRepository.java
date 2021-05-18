package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.ConceptSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConceptSearchRepository extends JpaRepository<ConceptSearch, Integer> {

    @Query(value = "SELECT DISTINCT cs.* " +
            "   FROM concept_search cs " +
            "   JOIN concept c ON cs.concept_dbid = c.dbid " +
            "   JOIN concept_type ct ON cs.concept_dbid = ct.concept AND ct.type IN (:conceptType) " +
            "   WHERE MATCH(cs.term) AGAINST (:terms IN BOOLEAN MODE)" +
            "   AND c.status IN (:status) " +
            "   ORDER BY LENGTH(term), term " +
            "   LIMIT :limit", nativeQuery = true)
    List<ConceptSearch> findLegacyByTerm(@Param("terms") String terms, @Param("conceptType") List<String> conceptType,
                                        @Param("status") List<String> status, @Param("limit") Integer limit);

    @Query(value = "SELECT DISTINCT cs.* " +
            "   FROM concept_search cs " +
            "   JOIN concept c ON cs.concept_dbid = c.dbid " +
            "   JOIN concept_type ct ON cs.concept_dbid = ct.concept AND ct.type IN (:conceptType) " +
            "   WHERE (c.scheme IS NULL OR c.scheme IN (:schemes)) " +
            "   AND MATCH(cs.term) AGAINST (:terms IN BOOLEAN MODE)" +
            "   AND c.status IN (:status) " +
            "   ORDER BY LENGTH(term), term " +
            "   LIMIT :limit", nativeQuery = true)
    List<ConceptSearch> findLegacySchemesByTerm(@Param("terms") String terms, @Param("schemes") List<String> schemes,
                                               @Param("conceptType") List<String> conceptType, @Param("status") List<String> status, @Param("limit") Integer limit);


}
