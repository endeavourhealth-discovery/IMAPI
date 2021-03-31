package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.Concept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, Integer> {

	Concept findByIri(String concept);

    @Query(value = "SELECT * " +
        "FROM (" +
        "   (SELECT c.* " +
        "   FROM concept c " +
        "   WHERE c.code = :full " +
        "   AND c.status IN (:status) " + "   AND c.type IN (:conceptType) " +
        "   LIMIT :limit) " +
        "   UNION " +
        "   (SELECT c.* " +
        "   FROM concept c " +
        "   WHERE c.iri = :full " +
        "   AND c.status IN (:status) " + "   AND c.type IN (:conceptType) " +
        "   LIMIT :limit) " +
        "   UNION " +
        "   (SELECT c.* " +
        "   FROM concept c " +
        "   WHERE MATCH(c.name) AGAINST (:terms IN BOOLEAN MODE) " +
        "   AND c.status IN (:status) " + "   AND c.type IN (:conceptType) " +
        "   LIMIT :limit) " +
        ") x " +
        "ORDER BY x.type DESC, x.weighting DESC, LENGTH(x.name) " +
        "LIMIT :limit", nativeQuery = true)
    List<Concept> searchLegacy(@Param("terms") String terms, @Param("full") String full,@Param("conceptType") List<String> conceptType, @Param("status") List<String> status, @Param("limit") Integer limit);

    @Query(value = "SELECT * " +
        "FROM (" +
        "   (SELECT c.*, ct.type " +
        "   FROM concept c " +
        "   JOIN concept_type ct ON c.dbid = ct.dbid AND ct.type IN (:conceptType) " +
        "   WHERE (c.scheme IS NULL OR c.scheme IN (:schemes)) " +
        "   AND c.code = :full " +
        "   AND c.status IN (:status) " +
        "   LIMIT :limit) " +
        "   UNION " +
        "   (SELECT c.*, ct.type " +
        "   FROM concept c " +
        "   JOIN concept_type ct ON c.dbid = ct.dbid AND ct.type IN (:conceptType) " +
        "   WHERE (c.scheme IS NULL OR c.scheme IN (:schemes)) " +
        "   AND c.iri = :full " +
        "   AND c.status IN (:status) " +
        "   LIMIT :limit) " +
        "   UNION " +
        "   (SELECT c.*, ct.type " +
        "   FROM concept c " +
        "   JOIN concept_type ct ON c.dbid = ct.dbid AND ct.type IN (:conceptType) " +
        "   WHERE (c.scheme IS NULL OR c.scheme IN (:schemes)) " +
        "   AND MATCH(c.name) AGAINST (:terms IN BOOLEAN MODE) " +
        "   AND c.status IN (:status) " +
        "   LIMIT :limit) " +
        ") x " +
        "ORDER BY x.type DESC, LENGTH(x.name) " +
        "LIMIT :limit", nativeQuery = true)
    List<Concept> searchLegacySchemes(@Param("terms") String terms, @Param("full") String full, @Param("schemes") List<String> schemes,@Param("conceptType") List<String> conceptType, @Param("status") List<String> status, @Param("limit") Integer limit);
}
