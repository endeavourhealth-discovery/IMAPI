package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.Tpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ConceptTripleRepository extends JpaRepository<Tpl, String> {
    Set<Tpl> findByObject_Iri(String iri);
    Set<Tpl> findBySubject_Iri_AndPredicate_Iri(String iri, String predicate);
    Set<Tpl> findByObject_Iri_AndPredicate_Iri(String iri, String predicate);
}
