package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.DataModelRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.iml.NodeShape;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class DataModelService {
  private EntityRepository entityRepository = new EntityRepository();
  private DataModelRepository dataModelRepository = new DataModelRepository();
  private EntityService entityService = new EntityService();


  public List<TTIriRef> getDataModelsFromProperty(String propIri) {
    return dataModelRepository.findDataModelsFromProperty(propIri);
  }

  public String checkPropertyType(String iri) {
    return dataModelRepository.checkPropertyType(iri);
  }

  public List<TTIriRef> getProperties() {
    return dataModelRepository.getProperties();
  }


  public NodeShape getDataModelDisplayProperties(String iri) {
    return dataModelRepository.getDataModelDisplayProperties(iri);
  }


  public List<DataModelProperty> getDataModelProperties(String iri) {
    TTEntity entity = entityRepository.getBundle(iri, Set.of(SHACL.PROPERTY, RDFS.LABEL)).getEntity();
    return getDataModelProperties(entity);
  }

  public List<DataModelProperty> getDataModelProperties(TTEntity entity) {
    List<DataModelProperty> properties = new ArrayList<>();
    if (entity == null)
      return Collections.emptyList();
    if (entity.has(iri(SHACL.PROPERTY))) {
      getDataModelPropertyGroups(entity, properties);
    }
    return properties.stream().sorted(Comparator.comparing(DataModelProperty::getOrder)).toList();
  }

  private void getDataModelPropertyGroups(TTEntity entity, List<DataModelProperty> properties) {
    for (TTValue propertyGroup : entity.get(iri(SHACL.PROPERTY)).iterator()) {
      if (propertyGroup.isNode()) {
        TTIriRef inheritedFrom = propertyGroup.asNode().has(iri(IM.INHERITED_FROM))
          ? propertyGroup.asNode().get(iri(IM.INHERITED_FROM)).asIriRef()
          : null;
        if (propertyGroup.asNode().has(iri(SHACL.PATH))) {
          getDataModelShaclProperties(properties, propertyGroup, inheritedFrom);
        }
      }
    }
  }

  private void getDataModelShaclProperties(List<DataModelProperty> properties, TTValue propertyGroup, TTIriRef inheritedFrom) {
    TTIriRef propertyPath = propertyGroup.asNode().get(iri(SHACL.PATH)).asIriRef();
    if (properties.stream()
      .noneMatch(o -> o.getProperty().getIri().equals(propertyPath.getIri()))) {
      properties.add(getPropertyValue(inheritedFrom, propertyGroup, propertyPath));
    }
  }

  private DataModelProperty getPropertyValue(TTIriRef inheritedFrom, TTValue property, TTIriRef propertyPath) {
    DataModelProperty pv = new DataModelProperty().setInheritedFrom(inheritedFrom).setProperty(propertyPath);

    if (property.asNode().has(iri(SHACL.CLASS)))
      pv.setType(property.asNode().get(iri(SHACL.CLASS)).asIriRef());
    if (property.asNode().has(iri(SHACL.NODE)))
      pv.setType(property.asNode().get(iri(SHACL.NODE)).asIriRef());
    if (property.asNode().has(iri(OWL.CLASS)))
      pv.setType(property.asNode().get(iri(OWL.CLASS)).asIriRef());
    if (property.asNode().has(iri(SHACL.DATATYPE)))
      pv.setType(property.asNode().get(iri(SHACL.DATATYPE)).asIriRef());
    if (property.asNode().has(iri(SHACL.FUNCTION)))
      pv.setType(property.asNode().get(iri(SHACL.FUNCTION)).asIriRef());
    if (property.asNode().has(iri(SHACL.MAXCOUNT)))
      pv.setMaxExclusive(property.asNode().get(iri(SHACL.MAXCOUNT)).asLiteral().getValue());
    if (property.asNode().has(iri(SHACL.MINCOUNT)))
      pv.setMinExclusive(property.asNode().get(iri(SHACL.MINCOUNT)).asLiteral().getValue());
    pv.setOrder(property.asNode().has(iri(SHACL.ORDER)) ? property.asNode().get(iri(SHACL.ORDER)).asLiteral().intValue() : 0);

    return pv;
  }
}
