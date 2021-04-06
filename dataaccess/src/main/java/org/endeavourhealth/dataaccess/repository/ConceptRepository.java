package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.Concept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, Integer> {

	Concept findByIri(String concept);
    List<Concept> findAllByIriIn(Set<String> iris);

    @Query(value = "SELECT * " +
        "FROM (" +
        "   (SELECT c.*, ct.type " +
        "   FROM concept c " +
        "   JOIN concept_type ct ON c.dbid = ct.dbid AND ct.type IN (:conceptType) " +
        "   WHERE c.code = :full " +
        "   AND c.status IN (:status) " +
        "   LIMIT :limit) " +
        "   UNION " +
        "   (SELECT c.*, ct.type " +
        "   FROM concept c " +
        "   JOIN concept_type ct ON c.dbid = ct.dbid AND ct.type IN (:conceptType) " +
        "   WHERE c.iri = :full " +
        "   AND c.status IN (:status) " +
        "   LIMIT :limit) " +
        "   UNION " +
        "   (SELECT c.*, ct.type " +
        "   FROM concept c " +
        "   JOIN concept_type ct ON c.dbid = ct.dbid AND ct.type IN (:conceptType) " +
        "   WHERE MATCH(c.name) AGAINST (:terms IN BOOLEAN MODE) " +
        "   AND c.status IN (:status) " +
        "   LIMIT :limit) " +
        ") x " +
        "ORDER BY x.type DESC, LENGTH(x.name) " +
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

    @Query(value = "SELECT c.iri " +
        "FROM concept c " +
        "WHERE c.scheme = :scheme AND c.code = :code  ", nativeQuery = true)
    String getConceptIdForSchemeCode(@Param("scheme") String scheme,@Param("code") String code);

    @Query(value = "SELECT c.code " +
        "FROM concept c " +
        "WHERE c.dbid = :dbid ", nativeQuery = true)
    String getCodeForConceptDbid(@Param(("dbid")) Integer dbid);

    @Query(value = "SELECT c.dbid " +
        "FROM concept c " +
        "WHERE c.scheme = :scheme AND c.code = :code  ", nativeQuery = true)
    Integer getConceptdbidForSchemeCode(@Param("scheme") String scheme,@Param("code") String code);

}
