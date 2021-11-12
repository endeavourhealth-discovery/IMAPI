package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.EntitySummary;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EntityTripleRepository {
    List<Tpl> getTriplesRecursive(String iri, Set<String> predicates, int limit);

    Set<ValueSetMember> getObjectBySubjectAndPredicate(String subjectIri, String predicateIri);

    boolean hasChildren(String iri, boolean inactive) throws DALException;

    List<TTIriRef> findImmediateChildrenByIri(String iri, Integer rowNumber, Integer pageSize, boolean inactive);

    List<TTIriRef> findImmediateParentsByIri(String iri, Integer rowNumber, Integer pageSize, boolean inactive);

    List<TTIriRef> getActiveSubjectByObjectExcludeByPredicate(String objectIri, Integer rowNumber, Integer pageSize, String excludePredicateIri);

    Integer getCountOfActiveSubjectByObjectExcludeByPredicate(String objectIri, String excludePredicateIri);

    Set<ValueSetMember> getSubjectByObjectAndPredicateAsValueSetMembers(String objectIri, String predicateIri);

    Set<TTIriRef> getObjectIriRefsBySubjectAndPredicate(String subjectIri, String predicateIri);

    List<Namespace> findNamespaces();

    Collection<SimpleMap> getSubjectFromObjectPredicate(String objectIri, TTIriRef predicate);

    TTBundle getEntityPredicates(String entityIri, Set<String> predicates, int unlimited);

    Set<EntitySummary> getLegacyConceptSummaries(Set<EntitySummary> result);

    Collection<EntitySummary> getSubjectAndDescendantSummariesByPredicateObjectRelType(String predicate, String object);

    Set<EntitySummary> getSubclassesAndReplacements(String iri);
}
