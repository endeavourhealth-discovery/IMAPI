package org.endeavourhealth.dataaccess.repository;

import java.util.List;

import org.endeavourhealth.dataaccess.entity.Concept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, String> {

	Concept findByIri(String concept);

	Concept findByDbid(int concept);

    @Query(value = "SELECT c.* " +
        "FROM concept c " +
        "LEFT JOIN concept s ON s.dbid = c.scheme " +
        "WHERE (c.scheme IS NULL OR s.iri IN (':891101000252101', ':891071000252105')) " +
        "AND MATCH(c.name) AGAINST (:term IN BOOLEAN MODE) " +
        "AND c.status < 2 " +
        "ORDER BY LENGTH(c.name) " +
        "LIMIT :limit", nativeQuery = true)
    List<Concept> search(@Param("term") String term, @Param("limit") Integer limit);

    @Query(value = "SELECT c.* " +
        "FROM concept c " +
        "WHERE MATCH(c.name) AGAINST (:term IN BOOLEAN MODE) " +
        "AND c.status < 2 " +
        "ORDER BY LENGTH(c.name) " +
        "LIMIT :limit", nativeQuery = true)
    List<Concept> searchLegacy(@Param("term") String term, @Param("limit") Integer limit);

    @Query(value = "SELECT c.* " +
        "FROM concept c " +
        "LEFT JOIN concept s ON s.dbid = c.scheme " +
        "WHERE (c.scheme IS NULL OR s.iri IN (:schemes)) " +
        "AND MATCH(c.name) AGAINST (:term IN BOOLEAN MODE) " +
        "AND c.status < 2 " +
        "ORDER BY LENGTH(c.name) " +
        "LIMIT :limit", nativeQuery = true)
    List<Concept> searchLegacySchemes(@Param("term") String term, @Param("schemes") List<String> schemes, @Param("limit") Integer limit);

    @Query(value = "SELECT c.* " +
        "FROM concept c " +
        "JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 " +
        "JOIN concept t ON t.dbid = tct.target " +
        "LEFT JOIN concept s ON s.dbid = c.scheme " +
        "WHERE (c.scheme IS NULL OR s.iri IN (':891101000252101', ':891071000252105') " +
        "AND MATCH(c.name) AGAINST (:term IN BOOLEAN MODE) " +
        "AND t.iri = :root " +
        "AND c.status < 2 " +
        "ORDER BY LENGTH(c.name) " +
        "LIMIT :limit", nativeQuery = true)
	List<Concept> searchType(@Param("term") String term, @Param("root") String root, @Param("limit") Integer limit);

    @Query(value = "SELECT c.* " +
        "FROM concept c " +
        "JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 " +
        "JOIN concept t ON t.dbid = tct.target " +
        "WHERE MATCH(c.name) AGAINST (:term IN BOOLEAN MODE) " +
        "AND t.iri = :root " +
        "AND c.status < 2 " +
        "ORDER BY LENGTH(c.name) " +
        "LIMIT :limit", nativeQuery = true)
    List<Concept> searchLegacyType(@Param("term") String term, @Param("root") String root, @Param("limit") Integer limit);


    @Query(value = "SELECT c.* " +
        "FROM concept c " +
        "JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 " +
        "JOIN concept t ON t.dbid = tct.target " +
        "LEFT JOIN concept s ON s.dbid = c.scheme " +
        "WHERE (c.scheme IS NULL OR s.iri IN (:schemes)) " +
        "AND MATCH(c.name) AGAINST (:term IN BOOLEAN MODE) " +
        "AND t.iri = :root " +
        "AND c.status < 2 " +
        "ORDER BY LENGTH(c.name) " +
        "LIMIT :limit", nativeQuery = true)
    List<Concept> searchLegacyTypeSchemes(@Param("term") String term, @Param("root") String root, @Param("schemes") List<String> schemes, @Param("limit") Integer limit);
}
