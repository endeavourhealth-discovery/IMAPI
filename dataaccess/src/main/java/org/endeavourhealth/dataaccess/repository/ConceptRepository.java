package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.Concept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, Integer> {

	Concept findByIri(String concept);
    List<Concept> findAllByIriIn(Set<String> iris);
    Concept findByCodeAndScheme(String code, String scheme);
    Concept findByDbid(Integer dbid);


    @Modifying
    @Query(value = "INSERT INTO concept (iri,`name`, description, code, scheme, status,definition) " +
        "VALUES (:iri, :name, :description, :code, :scheme, :status, :definition)", nativeQuery = true)
    Concept insertConcept(@Param("iri") String iri,@Param("name") String name,@Param("description") String description,@Param("code") String code,
                       @Param("scheme") String scheme,@Param("status") String  status,@Param("definition") String definition);

    @Modifying
    @Query(value = "UPDATE concept " +
        "SET iri = :iri , name = :name, description = :description, code = :code, scheme = :scheme, status = :status,definition = :definition ", nativeQuery = true)
    Concept updateConcept(@Param("iri") String iri,@Param("name") String name,@Param("description") String description,@Param("code") String code,
                       @Param("scheme") String scheme,@Param("status") String  status,@Param("definition") String definition);


    @Query(value = "select s.* " +
        "from concept c " +
        "join tct t on t.descendant = c.dbid " +
        "join concept a on a.dbid = t.ancestor " +
        "join concept tt on tt.dbid = t.type and tt.iri = 'http://endhealth.info/im#isA' " +
        "join tpl l on l.object = a.dbid " +
        "join concept p on p.dbid = l.predicate and p.iri = 'http://endhealth.info/im#hasMembers' " +
        "join concept s on s.dbid = l.subject and s.iri = :vSet " +
        "where c.code = :code and c.scheme = :scheme " , nativeQuery = true)
    Concept isCoreCodeSchemeIncludedInVSet(@Param("code") String code, @Param("scheme") String scheme, @Param("vSet") String vSet);

    @Query(value = "select s.* " +
        "from concept c " +
        "join tct t on t.descendant = c.dbid " +
        "join concept a on a.dbid = t.ancestor " +
        "join concept tt on tt.dbid = t.type and tt.iri = 'http://endhealth.info/im#isA' " +
        "join tpl l on l.object = a.dbid " +
        "join concept p on p.dbid = l.predicate and p.iri = 'http://endhealth.info/im#notMembers' " +
        "join concept s on s.dbid = l.subject and s.iri = :vSet " +
        "where c.code = :code and c.scheme = :scheme " , nativeQuery = true)
    Concept isCoreCodeSchemeExcludedInVSet(@Param("code") String code, @Param("scheme") String scheme, @Param("vSet") String vSet);

    @Query(value = "select s.*" +
            "from concept c " +
            "join tpl tl on tl.subject = c.dbid " +
            "join concept cl on cl.dbid = tl.object " +
            "join concept clp on clp.dbid = tl.predicate and clp.iri = 'http://endhealth.info/im#matchedTo' " +
            "join tct t on t.descendant = cl.dbid " +
            "join concept a on a.dbid = t.ancestor " +
            "join concept tt on tt.dbid = t.type and tt.iri = 'http://endhealth.info/im#isA' " +
            "join tpl l on l.object = a.dbid " +
            "join concept p on p.dbid = l.predicate and p.iri = 'http://endhealth.info/im#hasMembers' " +
            "join concept s on s.dbid = l.subject and s.iri = :vSet " +
            "where c.code = :code and c.scheme = :scheme" , nativeQuery = true)
    Concept isLegacyCodeSchemeIncludedInVSet(@Param("code") String code, @Param("scheme") String scheme, @Param("vSet") String vSet);

    @Query(value = "select s.* " +
            "from concept c " +
            "join tpl tl on tl.subject = c.dbid " +
            "join concept cl on cl.dbid = tl.object " +
            "join concept clp on clp.dbid = tl.predicate and clp.iri = 'http://endhealth.info/im#matchedTo' " +
            "join tct t on t.descendant = cl.dbid " +
            "join concept a on a.dbid = t.ancestor " +
            "join concept tt on tt.dbid = t.type and tt.iri = 'http://endhealth.info/im#isA' " +
            "join tpl l on l.object = a.dbid " +
            "join concept p on p.dbid = l.predicate and p.iri = 'http://endhealth.info/im#notMembers' " +
            "join concept s on s.dbid = l.subject and s.iri = :vSet " +
            "where c.code = :code and c.scheme = :scheme" , nativeQuery = true)
    Concept isLegacyCodeSchemeExcludedInVSet(@Param("code") String code, @Param("scheme") String scheme, @Param("vSet") String vSet);

}
