package com.endavourhealth.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.Concept;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, String>{
	
	@Query(value="SELECT c.* FROM concept c JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 JOIN concept p ON p.dbid = tct.property JOIN concept t ON t.dbid = tct.target WHERE c.name LIKE %:term% AND t.iri = :root", 
			nativeQuery = true)
	List<Concept> search(@Param("term") String term, @Param("root") String root);
	
	Concept findByIri(String concept);
	Concept findByDbid(int concept);

}
