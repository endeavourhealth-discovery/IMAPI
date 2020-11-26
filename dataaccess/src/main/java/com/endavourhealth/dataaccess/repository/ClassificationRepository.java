package com.endavourhealth.dataaccess.repository;

import com.endavourhealth.dataaccess.entity.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Integer>{
    List<Classification> findByParent_Iri(String parentIri, Pageable pageable);
    List<Classification> findByParent_Iri(String parentIri);

    List<Classification> findByParent_Iri_AndChild_Namespace_PrefixIn(String parentIri, List<String> namespacePrefixes);
    List<Classification> findByParent_Iri_AndChild_Namespace_PrefixIn(String parentIri, List<String> namespacePrefixes, Pageable pageable);

    Set<Classification> findByChild_Iri(String childIri);
}
