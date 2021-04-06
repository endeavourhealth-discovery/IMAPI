package org.endeavourhealth.dataaccess.repository;


import org.endeavourhealth.dataaccess.entity.SimpleCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimpleCountRepository extends JpaRepository<SimpleCount, String> {

    @Query(value = "SELECT t.dbid, t.iri as iri, t.name as label, count(1) as `count`\n" +
        "FROM concept c \n" +
        "JOIN concept_type ct ON ct.concept = c.dbid \n" +
        "LEFT JOIN concept t on t.iri = ct.type \n" +
        "GROUP BY t.dbid , t.iri , t.name \n" +
        "ORDER BY `count` DESC  ", nativeQuery = true)
    List<SimpleCount> getConceptTypeReport();

    @Query(value = "SELECT s.dbid, c.scheme as iri, s.name as label, count(1) as `count` " +
        "FROM concept c " +
        "LEFT JOIN concept s on s.iri = c.scheme " +
        "WHERE c.scheme IS NOT NULL "+
        "GROUP BY s.dbid,c.scheme, s.name " +
        "ORDER BY `count` DESC ", nativeQuery = true)
    List<SimpleCount> getConceptSchemeReport();

    @Query(value = "SELECT s.dbid, s.iri, s.name as label, count(1) as `count` " +
        "FROM concept c " +
        "LEFT JOIN concept s on s.iri = c.status " +
        "GROUP BY s.dbid, s.iri, s.name " +
        "ORDER BY `count` DESC ", nativeQuery = true)
    List<SimpleCount> getConceptStatusReport();

}
