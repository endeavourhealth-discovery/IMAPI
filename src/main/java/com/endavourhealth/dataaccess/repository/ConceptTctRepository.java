package com.endavourhealth.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.ConceptTct;

@Repository
public interface ConceptTctRepository extends JpaRepository<ConceptTct, String>{
	
	public static final int DIRECT_RELATION_LEVEL = 0;
	
	List<ConceptTct> findByTargetDbidAndLevel(Integer targetDbId, int level);

	List<ConceptTct> findBySourceDbid(int source);
	
	Long countByTargetDbidAndLevel(Integer targetDbId, int level);
}
