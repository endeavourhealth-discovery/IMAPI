package org.endeavourhealth.dataaccess.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.endeavourhealth.dataaccess.entity.Classification;

@Service
public class ClassificationNativeQueries {
	
	@Autowired
	EntityManager entityManager;

	public List<Object[]> findClassificationByParentIri(String parentIri) {
    	Query query = entityManager.createNamedQuery(Classification.FIND_CLASSIFICATION_BY_PARENT_IRI);
    	setParentIriParameter(query, parentIri);
		
    	@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
    			
		return results;
	}
	
	public List<Object[]> findClassificationByParentIriAndChildNamespace(String parentIri, List<String> childNamespaces) {
    	Query query = entityManager.createNamedQuery(Classification.FIND_CLASSIFICATION_BY_PARENT_IRI_AND_CHILD_NAMESPACES);
    	setParentIriParameter(query, parentIri);
    	setNamespacePrefixesParameter(query, childNamespaces);
		
    	@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
    			
		return results;
	}
	
	public Classification getClassificaton(Object[] row) {
		return (Classification) row[0];
	}
	
	public boolean getChildHasChildren(Object[] row) {
		return (boolean) row[1];
	}
	
	private void setParentIriParameter(Query query, String parentIri) {
		query.setParameter("parentIri", parentIri);
	}

	private void setNamespacePrefixesParameter(Query query, List<String> childNamespaces) {
		query.setParameter("namespacePrefixes", childNamespaces);
	}
	
}
