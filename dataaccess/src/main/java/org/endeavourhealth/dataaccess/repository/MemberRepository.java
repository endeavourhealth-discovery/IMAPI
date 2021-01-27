package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.Concept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Concept, String> {
    @Query(value = "SELECT m.*\n" +
        "FROM concept c\n" +
        "JOIN axiom a ON a.concept = c.dbid AND a.type = 22\n" +
        "JOIN expression e ON e.axiom = a.dbid\n" +
        "JOIN concept m ON m.dbid = e.target_concept\n" +
        "WHERE c.iri = :iri", nativeQuery = true)
    List<Concept> getCoreMappedFromLegacy(@Param("iri") String legacyIri);

    @Query(value = "SELECT m.*\n" +
        "FROM concept c\n" +
        "JOIN expression e ON e.target_concept = c.dbid\n" +
        "JOIN axiom a ON a.dbid = e.axiom AND a.type = 22\n" +
        "JOIN concept m ON m.dbid = a.concept\n" +
        "WHERE c.iri = :iri", nativeQuery = true)
    List<Concept> getLegacyMappedToCore(@Param("iri") String coreIri);

}
