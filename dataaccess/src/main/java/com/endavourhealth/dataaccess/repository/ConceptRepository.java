package com.endavourhealth.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.Concept;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, String> {

	Concept findByIri(String concept);

	Concept findByDbid(int concept);

    @Query(value = "SELECT c.* " +
        "FROM concept c " +
        "LEFT JOIN concept s ON s.dbid = c.scheme " +
        "WHERE (c.scheme IS NULL OR s.iri = ':891101000252101') " +
        "AND MATCH(c.name) AGAINST (:term IN BOOLEAN MODE) " +
//        "ORDER BY LENGTH(c.name) " +
        "LIMIT 10", nativeQuery = true)
    List<Concept> search(@Param("term") String term);

    @Query(value = "SELECT c.* " +
        "FROM concept c " +
        "WHERE MATCH(c.name) AGAINST (:term IN BOOLEAN MODE) " +
//        "ORDER BY LENGTH(c.name) " +
        "LIMIT 10", nativeQuery = true)
    List<Concept> searchLegacy(@Param("term") String term);

    @Query(value = "SELECT c.* " +
        "FROM concept c " +
        "JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 " +
        "JOIN concept t ON t.dbid = tct.target " +
        "WHERE MATCH(c.name) AGAINST (:term IN BOOLEAN MODE) " +
        "AND t.iri = :root " +
//        "ORDER BY LENGTH(c.name) " +
        "LIMIT 10", nativeQuery = true)
    List<Concept> search(@Param("term") String term, @Param("root") String root);

    @Query(value = "SELECT c.* " +
        "FROM concept c " +
        "JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 " +
        "JOIN concept t ON t.dbid = tct.target " +
        "LEFT JOIN concept s ON s.dbid = c.scheme " +
        "WHERE (c.scheme IS NULL OR s.iri = ':891101000252101') " +
        "AND MATCH(c.name) AGAINST (:term IN BOOLEAN MODE) " +
        "AND t.iri = :root " +
//        "ORDER BY LENGTH(c.name) " +
        "LIMIT 10", nativeQuery = true)
	List<Concept> searchLegacy(@Param("term") String term, @Param("root") String root);
}
