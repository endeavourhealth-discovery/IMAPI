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

    @Query(value = "SELECT * " +
        "FROM (" +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   LEFT JOIN concept s ON s.dbid = c.scheme " +
        "   WHERE (c.scheme IS NULL OR s.iri IN (':891101000252101', ':891071000252105')) " +
        "   AND c.code = :full " +
        "   AND c.status IN (:status) " +
        "   UNION " +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   LEFT JOIN concept s ON s.dbid = c.scheme " +
        "   WHERE (c.scheme IS NULL OR s.iri IN (':891101000252101', ':891071000252105')) " +
        "   AND c.iri = :full " +
        "   AND c.status IN (:status) " +
        "   UNION " +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   LEFT JOIN concept s ON s.dbid = c.scheme " +
        "   WHERE (c.scheme IS NULL OR s.iri IN (':891101000252101', ':891071000252105')) " +
        "   AND MATCH(c.name) AGAINST (:terms IN BOOLEAN MODE) " +
        "   AND c.status IN (:status) " +
        ") x " +
        "ORDER BY LENGTH(x.name) " +
        "LIMIT :limit", nativeQuery = true)
    List<Concept> search(@Param("terms") String terms, @Param("full") String full, @Param("status") List<Byte> status, @Param("limit") Integer limit);

    @Query(value = "SELECT * " +
        "FROM (" +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   WHERE c.code = :full " +
        "   AND c.status IN (:status) " +
        "   UNION " +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   WHERE c.iri = :full " +
        "   AND c.status IN (:status) " +
        "   UNION " +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   WHERE MATCH(c.name) AGAINST (:terms IN BOOLEAN MODE) " +
        "   AND c.status IN (:status) " +
        ") x " +
        "ORDER BY LENGTH(x.name) " +
        "LIMIT :limit", nativeQuery = true)
    List<Concept> searchLegacy(@Param("terms") String terms, @Param("full") String full, @Param("status") List<Byte> status, @Param("limit") Integer limit);

    @Query(value = "SELECT * " +
        "FROM (" +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   LEFT JOIN concept s ON s.dbid = c.scheme " +
        "   WHERE (c.scheme IS NULL OR s.iri IN (:schemes)) " +
        "   AND c.code = :full " +
        "   AND c.status IN (:status) " +
        "   UNION " +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   LEFT JOIN concept s ON s.dbid = c.scheme " +
        "   WHERE (c.scheme IS NULL OR s.iri IN (:schemes)) " +
        "   AND c.iri = :full " +
        "   AND c.status IN (:status) " +
        "   UNION " +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   LEFT JOIN concept s ON s.dbid = c.scheme " +
        "   WHERE (c.scheme IS NULL OR s.iri IN (:schemes)) " +
        "   AND MATCH(c.name) AGAINST (:terms IN BOOLEAN MODE) " +
        "   AND c.status IN (:status) " +
        ") x " +
        "ORDER BY LENGTH(x.name) " +
        "LIMIT :limit", nativeQuery = true)
    List<Concept> searchLegacySchemes(@Param("terms") String terms, @Param("full") String full, @Param("schemes") List<String> schemes, @Param("status") List<Byte> status, @Param("limit") Integer limit);

    @Query(value = "SELECT * " +
        "FROM (" +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 "+
        "   JOIN concept t ON t.dbid = tct.target AND t.iri = :root "+
        "   LEFT JOIN concept s ON s.dbid = c.scheme " +
        "   WHERE (c.scheme IS NULL OR s.iri IN (':891101000252101', ':891071000252105')) " +
        "   AND c.code = :full " +
        "   AND c.status IN (:status) " +
        "   UNION " +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 "+
        "   JOIN concept t ON t.dbid = tct.target AND t.iri = :root "+
        "   LEFT JOIN concept s ON s.dbid = c.scheme " +
        "   WHERE (c.scheme IS NULL OR s.iri IN (':891101000252101', ':891071000252105')) " +
        "   AND c.iri = :full " +
        "   AND c.status IN (:status) " +
        "   UNION " +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 "+
        "   JOIN concept t ON t.dbid = tct.target AND t.iri = :root "+
        "   LEFT JOIN concept s ON s.dbid = c.scheme " +
        "   WHERE (c.scheme IS NULL OR s.iri IN (':891101000252101', ':891071000252105')) " +
        "   AND MATCH(c.name) AGAINST (:terms IN BOOLEAN MODE) " +
        "   AND c.status IN (:status) " +
        ") x " +
        "ORDER BY LENGTH(x.name) " +
        "LIMIT :limit", nativeQuery = true)
	List<Concept> searchType(@Param("term") String terms, @Param("full") String full, @Param("root") String root, @Param("status") List<Byte> status, @Param("limit") Integer limit);

    @Query(value = "SELECT * " +
        "FROM (" +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 "+
        "   JOIN concept t ON t.dbid = tct.target AND t.iri = :root "+
        "   WHERE c.code = :full " +
        "   AND c.status IN (:status) " +
        "   UNION " +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 "+
        "   JOIN concept t ON t.dbid = tct.target AND t.iri = :root "+
        "   WHERE c.iri = :full " +
        "   AND c.status IN (:status) " +
        "   UNION " +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 "+
        "   JOIN concept t ON t.dbid = tct.target AND t.iri = :root "+
        "   WHERE MATCH(c.name) AGAINST (:terms IN BOOLEAN MODE) " +
        "   AND c.status IN (:status) " +
        ") x " +
        "ORDER BY LENGTH(x.name) " +
        "LIMIT :limit", nativeQuery = true)
    List<Concept> searchLegacyType(@Param("term") String terms, @Param("full") String full, @Param("root") String root, @Param("status") List<Byte> status, @Param("limit") Integer limit);


    @Query(value = "SELECT * " +
        "FROM (" +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 "+
        "   JOIN concept t ON t.dbid = tct.target AND t.iri = :root "+
        "   LEFT JOIN concept s ON s.dbid = c.scheme " +
        "   WHERE (c.scheme IS NULL OR s.iri IN (:schemes)) " +
        "   AND c.code = :full " +
        "   AND c.status IN (:status) " +
        "   UNION " +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 "+
        "   JOIN concept t ON t.dbid = tct.target AND t.iri = :root "+
        "   LEFT JOIN concept s ON s.dbid = c.scheme " +
        "   WHERE (c.scheme IS NULL OR s.iri IN (:schemes)) " +
        "   AND c.iri = :full " +
        "   AND c.status IN (:status) " +
        "   UNION " +
        "   SELECT c.* " +
        "   FROM concept c " +
        "   JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 "+
        "   JOIN concept t ON t.dbid = tct.target AND t.iri = :root "+
        "   LEFT JOIN concept s ON s.dbid = c.scheme " +
        "   WHERE (c.scheme IS NULL OR s.iri IN (:schemes)) " +
        "   AND MATCH(c.name) AGAINST (:terms IN BOOLEAN MODE) " +
        "   AND c.status IN (:status) " +
        ") x " +
        "ORDER BY LENGTH(x.name) " +
        "LIMIT :limit", nativeQuery = true)
    List<Concept> searchLegacyTypeSchemes(@Param("term") String terms, @Param("full") String full, @Param("root") String root, @Param("schemes") List<String> schemes, @Param("status") List<Byte> status, @Param("limit") Integer limit);
}
