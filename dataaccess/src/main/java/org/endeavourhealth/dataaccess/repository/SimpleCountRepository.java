package org.endeavourhealth.dataaccess.repository;


import org.endeavourhealth.dataaccess.entity.SimpleCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimpleCountRepository extends JpaRepository<SimpleCount, String> {

    @Query(value = "SELECT t.dbid, t.iri as label, count(1) as `count`" +
        " FROM concept c " +
        "join concept_type t on t.dbid = c.type "+
        "GROUP BY c.type " +
        "ORDER BY `count` DESC ", nativeQuery = true)
    List<SimpleCount> getConceptTypeReport();

    @Query(value = "SELECT s.dbid, s.name as label, count(1) as `count`" +
        " FROM concept c " +
        "join concept s on s.dbid = c.scheme "+
        "GROUP BY c.scheme " +
        "ORDER BY `count` DESC ", nativeQuery = true)
    List<SimpleCount> getConceptSchemeReport();

    @Query(value = "SELECT s.dbid, s.name as label, count(1) as `count`" +
        " FROM concept c " +
        "join  concept_status s on s.dbid = c.status " +
        "GROUP BY c.status " +
        "ORDER BY `count` DESC ", nativeQuery = true)
    List<SimpleCount> getConceptStatusReport();

}
