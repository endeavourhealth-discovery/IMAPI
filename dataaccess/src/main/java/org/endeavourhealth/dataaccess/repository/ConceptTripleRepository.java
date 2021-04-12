package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.Tpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface ConceptTripleRepository extends JpaRepository<Tpl, String> {
    Set<Tpl> findByObject_Iri(String iri);
    Set<Tpl> findBySubject_Iri_AndPredicate_Iri(String iri, String predicate);
    Set<Tpl> findByObject_Iri_AndPredicate_Iri(String iri, String predicate);
    
    @Query(value = "SELECT o.iri, o.name, o.status " +
            "FROM concept c " +
    		"JOIN tpl t ON t.subject = c.dbid " +
            "JOIN concept p ON p.dbid = t.predicate AND p.iri IN " +
    		"('http://endhealth.info/im#isA', 'http://endhealth.info/im#isContainedIn') " +
            "JOIN concept o ON o.dbid = t.object " +
    		"WHERE c.iri = :iri " +
            "LIMIT :size OFFSET :row ", nativeQuery = true)
    List<String> findImmediateParentsByIri(String iri, int size, int row);

    @Query(value = "SELECT o.iri, o.name, o.status " +
            "FROM concept c " +
    		"JOIN tpl t ON t.object = c.dbid " +
            "JOIN concept p ON p.dbid = t.predicate AND p.iri IN " +
    		"('http://endhealth.info/im#isA', 'http://endhealth.info/im#isContainedIn') " +
            "JOIN concept o ON o.dbid = t.subject " +
    		"WHERE c.iri = :iri " +
            "LIMIT :size OFFSET :row ", nativeQuery = true)
    List<String> findImmediateChildrenByIri(String iri, int size, int row);

    @Modifying
    @Query(value = "INSERT INTO tpl (subject, graph, group_number, predicate, object) " +
        "VALUES (:subject, :graph, :group_number, :predicate, :object)", nativeQuery = true)
    Tpl insertTriple(@Param("subject") int subject, @Param("graph") int graph, @Param("group_number") int group_number, @Param("predicate") int predicate,
                     @Param("object") int object);
}
