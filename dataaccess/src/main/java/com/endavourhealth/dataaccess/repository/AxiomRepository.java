package com.endavourhealth.dataaccess.repository;

import com.endavourhealth.dataaccess.entity.Axiom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AxiomRepository extends JpaRepository<Axiom, String> {
    @Query(value = "SELECT a.*\n" +
        "FROM concept c\n" +
        "JOIN axiom a ON a.concept = c.dbid\n" +
        "WHERE c.iri = :iri\n", nativeQuery = true)
	List<Axiom> findByIri(@Param("iri") String iri);
}
