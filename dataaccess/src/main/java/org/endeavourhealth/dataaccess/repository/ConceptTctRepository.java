package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.Tct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ConceptTctRepository extends JpaRepository<Tct, String> {

    Set<Tct> findByAncestor_Iri_AndType_Iri_AndLevel(String iri, String type, Integer level);
    Set<Tct> findByDescendant_Iri_AndType_Iri_AndLevel(String iri, String type, Integer level);
    Set<Tct> findByDescendant_Iri_AndType_Iri_AndAncestor_IriIn(String iri, String type, List<String> candidates);
    Set<Tct> findByAncestor_Iri_AndType_Iri(String iri, String type);
	Set<Tct> findByDescendant_Iri_AndAncestor_IriIn(String iri, List<String> candidates);
    Set<Tct> findByDescendant_Iri_AndType_OrderByLevel(String iri, String type);
}
