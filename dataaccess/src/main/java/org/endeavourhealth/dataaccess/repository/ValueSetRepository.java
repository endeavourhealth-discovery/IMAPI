package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.ValueSetMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValueSetRepository extends JpaRepository<ValueSetMember, String> {
    @Query(value = "SELECT m.iri AS concept_iri, m.name AS concept_name, m.code, m.scheme AS scheme_iri, s.name AS scheme_name\n" +
        "FROM concept c\n" +
        "JOIN tct tct ON tct.ancestor = c.dbid\n" +
        "JOIN concept t ON t.dbid = tct.type AND t.iri = 'http://endhealth.info/im#isA'\n" +
        "JOIN concept m ON m.dbid = tct.descendant\n" +
        "LEFT JOIN concept s ON s.iri = m.scheme\n" +
        "WHERE c.iri = :iri\n" +
        "UNION\n" +
        "SELECT m.iri AS concept_iri, m.name AS concept_name, m.code, m.scheme AS scheme_iri, s.name AS scheme_name\n" +
        "FROM concept c\n" +
        "JOIN tct tct ON tct.ancestor = c.dbid\n" +
        "JOIN concept t ON t.dbid = tct.type AND t.iri = 'http://endhealth.info/im#isA'\n" +
        "JOIN tpl ON tpl.object = tct.descendant\n" +
        "JOIN concept p ON p.dbid = tpl.predicate AND p.iri = 'http://endhealth.info/im#mappedFrom'\n" +
        "JOIN concept m ON m.dbid = tpl.subject\n" +
        "LEFT JOIN concept s ON s.iri = m.scheme\n" +
        "WHERE c.iri = :iri", nativeQuery = true)
    List<ValueSetMember> expandMember(@Param("iri") String iri);
}
