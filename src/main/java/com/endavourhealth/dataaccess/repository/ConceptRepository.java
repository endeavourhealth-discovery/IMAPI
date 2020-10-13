package com.endavourhealth.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.Concept;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, String> {

	@Query(value = "SELECT c.* FROM concept c JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0 JOIN concept p ON p.dbid = tct.property JOIN concept t ON t.dbid = tct.target WHERE c.name LIKE %:term% AND t.iri = :root LIMIT 10", nativeQuery = true)
	List<Concept> search(@Param("term") String term, @Param("root") String root);

	Concept findByIri(String concept);

	Concept findByDbid(int concept);
	
	
	@Query(value = "SELECT m.* FROM concept v JOIN concept p ON p.iri = :member JOIN concept_property_object cpo ON cpo.concept = v.dbid AND cpo.property = p.dbid JOIN concept m ON m.dbid = cpo.object WHERE v.iri = :iri", nativeQuery = true)
	List<Concept> getMembers(@Param("member") String member, @Param("iri") String iri);
	
	// Legacy Endpoints
//	@Query(value="SELECT a.definition FROM concept c JOIN concept_axiom a ON a.concept = c.dbid WHERE c.iri = :iri", nativeQuery=true)
//	List<JsonNode> getAxioms(@Param("iri") String iri);
//	
//	@Query(value="SELECT SQL_CALC_FOUND_ROWS DISTINCT o.minCardinality, o.maxCardinality, p.iri AS r_iri, p.name AS r_name, p.description AS r_description, c.iri AS c_iri, c.name AS c_name, c.description AS c_description FROM concept_property_object o JOIN concept v ON v.dbid = o.object AND v.iri = :iri JOIN concept p ON p.dbid = o.property JOIN concept c ON c.dbid = o.concept", nativeQuery=true)
//	PagedResultSet<RelatedConcept> getSources(@Param("iri") String iri);
//	
//	@Query(value="SELECT SQL_CALC_FOUND_ROWS DISTINCT o.minCardinality, o.maxCardinality, p.iri AS r_iri, p.name AS r_name, p.description AS r_description, v.iri AS c_iri, v.name AS c_name, c.description AS c_description FROM concept_property_object o JOIN concept c ON c.dbid = o.concept AND c.iri = :iri JOIN concept p ON p.dbid = o.property JOIN concept v ON v.dbid = o.object", nativeQuery=true)
//	PagedResultSet<RelatedConcept> getTargets(@Param("iri") String iri);
//	
//	@Query(value="SELECT * FROM concept WHERE iri = :iri", nativeQuery=true)
//	Concept getConcept(@Param("iri") String iri);
//	
//	@Query(value="SELECT null AS minCardinality, null AS maxCardinality, p.iri AS r_iri, p.name AS r_name, p.description AS r_description, t.iri AS c_iri, t.name AS c_name, c.description AS c_description FROM concept c JOIN concept_tct tct ON tct.source = c.dbid AND tct.level >= 0 JOIN concept p ON p.dbid = tct.property JOIN concept t ON t.dbid = tct.target WHERE c.iri = :iri AND p.iri IN (:relationships) GROUP BY tct.level ORDER BY tct.level", nativeQuery=true)
//	List<RelatedConcept> getTree(@Param("iri") String iri, @Param("relationships") List<String> relationships);
//	
//	@Query(value="SELECT scm.iri, scm.name AS scheme, COUNT(DISTINCT(s.dbid)) AS cnt FROM concept c JOIN concept p ON p.iri = \"sn:116680003\" JOIN concept_tct t ON t.target = c.dbid AND t.property = p.dbid AND t.level > 0 JOIN concept s ON s.dbid = t.source LEFT JOIN concept scm on scm.dbid = s.scheme WHERE c.iri = :iri GROUP BY scm.dbid", nativeQuery=true)
//	List<SchemeCount> getChildCountByScheme(@Param("iri") String iri);
//	
//	@Query(value="SELECT DISTINCT scm.iri, scm.name AS scheme, s.iri AS childIri, s.name AS childName, s.code AS childCode FROM concept c JOIN concept p ON p.iri = \"sn:116680003\" JOIN concept_tct t ON t.target = c.dbid AND t.property = p.dbid AND t.level > 0 JOIN concept s ON s.dbid = t.source LEFT JOIN concept scm on scm.dbid = s.scheme WHERE c.iri = :iri AND scm.iri = :scheme ORDER BY scm.name, s.name", nativeQuery=true)
//	List<SchemeChildren> getChildren(@Param("iri") String iri, @Param("iri") String scheme);
//	
//	@Query(value="SELECT p.iri AS p_iri, p.name AS p_name, cpo.minCardinality AS min_cardinality, cpo.maxCardinality AS max_cardinality, v.iri as v_iri, v.name AS v_name, -1 as level, c.iri as o_iri, c.name AS o_name FROM concept c JOIN concept_property_object cpo ON cpo.concept = c.dbid JOIN concept p ON p.dbid = cpo.property JOIN concept v ON v.dbid = cpo.object JOIN concept_tct tct ON tct.source = cpo.property JOIN concept t ON t.dbid = tct.target WHERE c.iri = :iri AND t.iri IN ('owl:topObjectProperty', 'owl:topDataProperty')", nativeQuery=true)
//	List<Property> getProperties(@Param("iri") String iri);
//	
//	@Query(value="SELECT p.iri AS p_iri, p.name AS p_name, cpo.minCardinality AS min_cardinality, cpo.maxCardinality AS max_cardinality, v.iri as v_iri, v.name AS v_name, i.level as level, h.iri as o_iri, h.name AS o_name FROM concept c JOIN concept_tct i ON i.source = c.dbid JOIN concept_property_object cpo ON cpo.concept = i.target JOIN concept h ON h.dbid = cpo.concept JOIN concept p ON p.dbid = cpo.property JOIN concept v ON v.dbid = cpo.object JOIN concept_tct tct ON tct.source = cpo.property JOIN concept t ON t.dbid = tct.target WHERE c.iri = :iri AND t.iri IN ('owl:topObjectProperty', 'owl:topDataProperty')", nativeQuery=true)
//	List<Property> getInheritedProperties(@Param("iri") String iri);
	
	
	
	
	

}
