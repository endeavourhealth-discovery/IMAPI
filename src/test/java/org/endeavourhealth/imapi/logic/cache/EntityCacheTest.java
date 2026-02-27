package org.endeavourhealth.imapi.logic.cache;

import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.PropertyRepository;
import org.endeavourhealth.imapi.dataaccess.ShapeRepository;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EntityCacheTest {

    @BeforeEach
    void setUp() {
        EntityCache.shapes.clear();
        EntityCache.properties.clear();
        EntityCache.entities.clear();
        EntityCache.predicateOrder.clear();
        EntityCache.predicateNames.clear();
    }

    @Test
    void refreshShapes() {
        try (MockedStatic<ShapeRepository> mockedShapeRepository = mockStatic(ShapeRepository.class)) {
            TTEntityMap shapeMap = new TTEntityMap();
            TTEntity shape = new TTEntity("http://example.org/shape1");
            shapeMap.getEntities().put(shape.getIri(), shape);
            mockedShapeRepository.when(ShapeRepository::getShapes).thenReturn(shapeMap);

            EntityCache.refreshShapes();

            assertThat(EntityCache.getShapes()).containsKey("http://example.org/shape1");
        }
    }

    @Test
    void getProperty_CacheHit() {
        TTEntity property = new TTEntity("http://example.org/prop1");
        EntityCache.addProperty(property);

        TTBundle bundle = EntityCache.getProperty("http://example.org/prop1");

        assertThat(bundle).isNotNull();
        assertThat(bundle.getEntity().getIri()).isEqualTo("http://example.org/prop1");
    }

    @Test
    void getProperty_CacheMiss() {
        try (MockedStatic<PropertyRepository> mockedPropertyRepository = mockStatic(PropertyRepository.class)) {
            TTEntityMap propertyMap = new TTEntityMap();
            TTEntity property = new TTEntity("http://example.org/prop1");
            property.set(iri("http://www.w3.org/2000/01/rdf-schema#subClassOf"), new TTArray());
            propertyMap.getEntities().put(property.getIri(), property);
            mockedPropertyRepository.when(() -> PropertyRepository.getProperty(anyString())).thenReturn(propertyMap);

            TTBundle bundle = EntityCache.getProperty("http://example.org/prop1");

            assertThat(bundle).isNotNull();
            assertThat(bundle.getEntity().getIri()).isEqualTo("http://example.org/prop1");
            assertThat(EntityCache.getProperties()).containsKey("http://example.org/prop1");
        }
    }

    @Test
    void getEntity_CacheHit() {
        TTEntity entity = new TTEntity("http://example.org/entity1");
        EntityCache.addEntity(entity);

        TTBundle bundle = EntityCache.getEntity("http://example.org/entity1");

        assertThat(bundle).isNotNull();
        assertThat(bundle.getEntity().getIri()).isEqualTo("http://example.org/entity1");
    }

    @Test
    void getEntity_CacheMiss() {
        try (MockedConstruction<EntityRepository> mockedEntityRepository = mockConstruction(EntityRepository.class, (mock, context) -> {
            TTBundle bundle = new TTBundle().setEntity(new TTEntity("http://example.org/entity1"));
            when(mock.getBundle(anyString())).thenReturn(bundle);
        })) {
            TTBundle bundle = EntityCache.getEntity("http://example.org/entity1");

            assertThat(bundle).isNotNull();
            assertThat(bundle.getEntity().getIri()).isEqualTo("http://example.org/entity1");
            assertThat(EntityCache.entities).containsKey("http://example.org/entity1");
        }
    }

    @Test
    void getShape_CacheHit() {
        TTEntity shape = new TTEntity("http://example.org/shape1");
        EntityCache.addShape(shape);

        TTBundle bundle = EntityCache.getShape("http://example.org/shape1");

        assertThat(bundle).isNotNull();
        assertThat(bundle.getEntity().getIri()).isEqualTo("http://example.org/shape1");
    }

    @Test
    void getShape_CacheMiss() {
        try (MockedStatic<ShapeRepository> mockedShapeRepository = mockStatic(ShapeRepository.class)) {
            TTEntityMap shapeMap = new TTEntityMap();
            TTEntity shape = new TTEntity("http://example.org/shape1");
            shapeMap.getEntities().put(shape.getIri(), shape);
            mockedShapeRepository.when(() -> ShapeRepository.getShapeAndAncestors(anyString())).thenReturn(shapeMap);

            TTBundle bundle = EntityCache.getShape("http://example.org/shape1");

            assertThat(bundle).isNotNull();
            assertThat(bundle.getEntity().getIri()).isEqualTo("http://example.org/shape1");
            assertThat(EntityCache.getShapes()).containsKey("http://example.org/shape1");
        }
    }

    @Test
    void cacheShapes_WithPredicateOrder() {
        TTEntityMap shapeMap = new TTEntityMap();
        TTEntity shape = new TTEntity("http://example.org/shape1");
        shape.set(iri(SHACL.PROPERTY), new TTArray().add(new TTNode().set(iri(SHACL.PATH), iri("http://example.org/path1", "Path 1"))));
        shapeMap.getEntities().put(shape.getIri(), shape);

        EntityCache.cacheShapes(shapeMap);

        assertThat(EntityCache.getShapes()).containsKey("http://example.org/shape1");
        assertThat(EntityCache.getPredicateOrder("http://example.org/shape1")).hasSize(1);
        assertThat(EntityCache.getPredicateName("http://example.org/path1")).isEqualTo("Path 1");
    }

    @Test
    void getPredicatesFromNode() {
        TTNode node = new TTNode();
        node.set(iri("http://example.org/p1"), iri("http://example.org/o1"));
        TTNode nestedNode = new TTNode();
        nestedNode.set(iri("http://example.org/p2"), iri("http://example.org/o2"));
        node.addObject(iri("http://example.org/p3"), nestedNode);

        Set<TTIriRef> predicates = EntityCache.getPredicatesFromNode(node);

        assertThat(predicates).hasSize(3);
        assertThat(predicates).extracting(TTIriRef::getIri)
                .containsExactlyInAnyOrder("http://example.org/p1", "http://example.org/p2", "http://example.org/p3");
    }

    @Test
    void run_CallsRefreshShapes() {
        try (MockedStatic<ShapeRepository> mockedShapeRepository = mockStatic(ShapeRepository.class)) {
            TTEntityMap shapeMap = new TTEntityMap();
            mockedShapeRepository.when(ShapeRepository::getShapes).thenReturn(shapeMap);

            EntityCache cache = new EntityCache();
            cache.run();

            mockedShapeRepository.verify(ShapeRepository::getShapes, times(1));
        }
    }

    @Test
    void predicateMethods() {
        EntityCache.addPredicateName("http://example.org/p1", "Name 1");
        assertThat(EntityCache.getPredicateName("http://example.org/p1")).isEqualTo("Name 1");

        List<TTIriRef> order = List.of(iri("http://example.org/p1"), iri("http://example.org/p2"));
        EntityCache.setPredicateOrder("http://example.org/e1", order);
        assertThat(EntityCache.getPredicateOrder("http://example.org/e1")).isEqualTo(order);
    }
}
