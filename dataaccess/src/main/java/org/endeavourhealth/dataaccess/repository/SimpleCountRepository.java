package org.endeavourhealth.dataaccess.repository;


import org.endeavourhealth.dataaccess.entity.SimpleCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimpleCountRepository extends JpaRepository<SimpleCount, String> {

    @Query(value = "SELECT ct.dbid, ct.type as iri, t.name as label, count(1) as `count` " +
        " FROM concept c " +
        "JOIN concept_type ct ON ct.concept = c.dbid " +
        "LEFT JOIN concept t on t.iri = ct.type "+
        "GROUP BY ct.type " +
        "ORDER BY `count` DESC ", nativeQuery = true)
    List<SimpleCount> getConceptTypeReport();

    @Query(value = "SELECT s.dbid, c.scheme as iri, s.name as label, count(1) as `count` " +
        "FROM concept c " +
        "LEFT JOIN concept s on s.iri = c.scheme " +
        "WHERE c.scheme IS NOT NULL "+
        "GROUP BY c.scheme " +
        "ORDER BY `count` DESC ", nativeQuery = true)
    List<SimpleCount> getConceptSchemeReport();

    @Query(value = "SELECT c.dbid, c.status AS iri, s.name as label, count(1) as `count` " +
        "FROM concept c " +
        "LEFT JOIN concept s on s.iri = c.status " +
        "GROUP BY c.status " +
        "ORDER BY `count` DESC ", nativeQuery = true)
    List<SimpleCount> getConceptStatusReport();

}
