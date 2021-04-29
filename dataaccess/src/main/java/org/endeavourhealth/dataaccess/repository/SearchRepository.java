package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.entity.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<SearchResult, Integer> {

    @Query(value = "SELECT DISTINCT c.dbid, c.iri, c.name,  c.code, c.scheme, c.status,c.description, c.code AS `match`  " +
            "   FROM concept c " +
            "   JOIN concept_type ct ON c.dbid = ct.concept AND ct.type IN (:conceptType) " +
            "   WHERE c.code = :full " +
            "   AND c.status IN (:status) " +
            "   LIMIT :limit", nativeQuery = true)
    List<SearchResult> findLegacyByCode(@Param("full") String full, @Param("conceptType") List<String> conceptType,
                                   @Param("status") List<String> status, @Param("limit") Integer limit);

    @Query(value = "SELECT DISTINCT c.dbid, c.iri, c.name,  c.code, c.scheme, c.status,c.description, c.iri AS `match` " +
            "   FROM concept c " +
            "   JOIN concept_type ct ON c.dbid = ct.concept AND ct.type IN (:conceptType) " +
            "   WHERE c.iri = :full " +
            "   AND c.status IN (:status) " +
            "   LIMIT :limit ",nativeQuery = true)
    List<SearchResult> findLegacyByIri(@Param("full") String full,@Param("conceptType") List<String> conceptType,
                                  @Param("status") List<String> status, @Param("limit") Integer limit);

    @Query(value = "SELECT DISTINCT c.dbid, c.iri, c.name,  c.code, c.scheme, c.status,c.description, c.name AS `match` " +
            "   FROM concept c " +
            "   JOIN concept_type ct ON c.dbid = ct.concept AND ct.type IN (:conceptType) " +
            "   WHERE MATCH(c.name) AGAINST (:terms IN BOOLEAN MODE) " +
            "   AND c.status IN (:status) " +
            "   LIMIT :limit ",nativeQuery = true)
    List<SearchResult> findLegacyByName(@Param("terms") String terms,@Param("conceptType") List<String> conceptType,
                                   @Param("status") List<String> status, @Param("limit") Integer limit);

    @Query(value = "SELECT DISTINCT c.dbid, c.iri, c.name, c.code, c.scheme, c.status,c.description, t.term AS `match` " +
            "   FROM concept c " +
            "   JOIN concept_term t ON c.dbid = t.concept " +
            "   JOIN concept_type ct ON c.dbid = ct.concept AND ct.type IN (:conceptType) " +
            "   WHERE MATCH(t.term) AGAINST (:terms IN BOOLEAN MODE) " +
            "   AND c.status IN (:status) " +
            "   LIMIT :limit ",nativeQuery = true)
    List<SearchResult> findLegacyByTerm(@Param("terms") String terms,@Param("conceptType") List<String> conceptType,
                                   @Param("status") List<String> status, @Param("limit") Integer limit);

    @Query(value = "SELECT DISTINCT c.dbid, c.iri, c.name,   c.code, c.scheme, c.status,c.description, c.code AS `match` " +
            "   FROM concept c " +
            "   JOIN concept_type ct ON c.dbid = ct.concept AND ct.type IN (:conceptType) " +
            "   WHERE (c.scheme IS NULL OR c.scheme IN (:schemes)) " +
            "   AND c.code = :full " +
            "   AND c.status IN (:status) " +
            "   LIMIT :limit ", nativeQuery = true)
    List<SearchResult> findLegacySchemesByCode(@Param("full") String full, @Param("schemes") List<String> schemes,
                                          @Param("conceptType") List<String> conceptType, @Param("status") List<String> status, @Param("limit") Integer limit);

    @Query(value = "SELECT DISTINCT c.dbid, c.iri, c.name,  c.code, c.scheme, c.status,c.description, c.iri AS `match` " +
            "   FROM concept c " +
            "   JOIN concept_type ct ON c.dbid = ct.concept AND ct.type IN (:conceptType) " +
            "   WHERE (c.scheme IS NULL OR c.scheme IN (:schemes)) " +
            "   AND c.iri = :full " +
            "   AND c.status IN (:status) " +
            "   LIMIT :limit ", nativeQuery = true)
    List<SearchResult> findLegacySchemesByIri(@Param("full") String full, @Param("schemes") List<String> schemes,
                                         @Param("conceptType") List<String> conceptType, @Param("status") List<String> status, @Param("limit") Integer limit);

    @Query(value = "SELECT DISTINCT c.dbid, c.iri, c.name,  c.code, c.scheme, c.status,c.description, c.name AS `match` " +
            "   FROM concept c " +
            "   JOIN concept_type ct ON c.dbid = ct.concept AND ct.type IN (:conceptType) " +
            "   WHERE (c.scheme IS NULL OR c.scheme IN (:schemes)) " +
            "   AND MATCH(c.name) AGAINST (:terms IN BOOLEAN MODE) " +
            "   AND c.status IN (:status) " +
            "   LIMIT :limit ", nativeQuery = true)
    List<SearchResult> findLegacySchemesByName(@Param("terms") String terms, @Param("schemes") List<String> schemes,
                                          @Param("conceptType") List<String> conceptType, @Param("status") List<String> status, @Param("limit") Integer limit);

    @Query(value = "SELECT DISTINCT c.dbid, c.iri, c.name,  c.code, c.scheme, c.status,c.description, t.term AS `match` " +
            "   FROM concept c " +
            "   JOIN concept_term t ON c.dbid = t.concept " +
            "   JOIN concept_type ct ON c.dbid = ct.concept AND ct.type IN (:conceptType) " +
            "   WHERE (c.scheme IS NULL OR c.scheme IN (:schemes)) " +
            "   AND MATCH(t.term) AGAINST (:terms IN BOOLEAN MODE) " +
            "   AND c.status IN (:status) " +
            "   LIMIT :limit ", nativeQuery = true)
    List<SearchResult> findLegacySchemesByTerm(@Param("terms") String terms, @Param("schemes") List<String> schemes,
                                          @Param("conceptType") List<String> conceptType, @Param("status") List<String> status, @Param("limit") Integer limit);

}
