package org.endeavourhealth.dataaccess.repository;

import java.util.List;
import java.util.Set;

import org.endeavourhealth.dataaccess.entity.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Integer>{

	List<Classification> findByParent_Iri(String parentIri);
	Set<Classification> findByChild_Iri(String childIri);
}
