package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.ValueSetMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValueSetRepository extends JpaRepository<ValueSetMember, String> {
    @Query(value = "SELECT c.iri AS concept_iri, c.name AS concept_name, c.code, s.iri AS scheme_iri, s.name AS scheme_name\n" +
        "FROM concept c\n" +
        "LEFT JOIN concept s ON s.dbid = c.scheme\n" +
        "WHERE c.iri = :iri\n" +
        "UNION\n" +
        "SELECT t.iri AS concept_iri, t.name AS concept_name, t.code, s.iri AS scheme_iri, s.name AS scheme_name\n" +
        "FROM concept c\n" +
        "JOIN concept_tct tct ON tct.target = c.dbid\n" +
        "JOIN concept t ON t.dbid = tct.source\n" +
        "LEFT JOIN concept s ON s.dbid = t.scheme\n" +
        "WHERE c.iri = :iri", nativeQuery = true)
    List<ValueSetMember> expandMember(@Param("iri") String iri);
}
