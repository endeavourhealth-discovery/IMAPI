package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.ValueSetMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValueSetRepository extends JpaRepository<ValueSetMember, String> {
    @Query(value = "SELECT m.iri AS concept_iri, m.name AS concept_name, m.code, s.iri AS scheme_iri, s.name AS scheme_name\n" +
        "FROM concept c\n" +
        "JOIN concept_tct tct ON tct.target = c.dbid\n" +
        "JOIN concept m ON m.dbid = tct.source\n" +
        "LEFT JOIN concept s ON s.dbid = m.scheme\n" +
        "WHERE c.iri = :iri\n" +
        "UNION\n" +
        "SELECT m.iri AS concept_iri, m.name AS concept_name, m.code, s.iri AS scheme_iri, s.name AS scheme_name\n" +
        "FROM concept c\n" +
        "JOIN concept_tct tct ON tct.target = c.dbid\n" +
        "JOIN expression le ON le.target_concept = tct.source\n" +
        "JOIN axiom la ON la.dbid = le.axiom AND la.type = 22\n" +
        "JOIN concept m ON m.dbid = la.concept\n" +
        "LEFT JOIN concept s ON s.dbid = m.scheme\n" +
        "WHERE c.iri = :iri", nativeQuery = true)
    List<ValueSetMember> expandMember(@Param("iri") String iri);
}
