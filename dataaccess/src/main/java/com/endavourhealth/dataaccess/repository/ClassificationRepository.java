package com.endavourhealth.dataaccess.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.Classification;


@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Integer>{

	List<Classification> findByParent_Iri(String parentIri);
	Set<Classification> findByChild_Iri(String childIri);
}
