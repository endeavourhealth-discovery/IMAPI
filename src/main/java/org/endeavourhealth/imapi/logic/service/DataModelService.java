package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.DataModelRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.PropertyDisplay;
import org.endeavourhealth.imapi.model.dto.UIProperty;
import org.endeavourhealth.imapi.model.iml.NodeShape;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

@Component
public class DataModelService {
  private final EntityRepository entityRepository;
  private final DataModelRepository dataModelRepository;
  private final EntityService entityService;

  public DataModelService() {
    entityRepository = new EntityRepository();
    dataModelRepository = new DataModelRepository();
    entityService = new EntityService(entityRepository);
  }

  public DataModelService(DataModelRepository dataModelRepository, EntityRepository entityRepository) {
    this.dataModelRepository = dataModelRepository;
    this.entityRepository = entityRepository;
    entityService = new EntityService(entityRepository);
  }

  public List<TTIriRef> getDataModelsFromProperty(String propIri) {
    return dataModelRepository.findDataModelsFromProperty(propIri);
  }

  public String checkPropertyType(String iri) {
    return dataModelRepository.checkPropertyType(iri);
  }

  public List<TTIriRef> getProperties() {
    return dataModelRepository.getProperties();
  }


  public NodeShape getDataModelDisplayProperties(String iri, boolean pathsOnly) {
    return dataModelRepository.getDataModelDisplayProperties(iri, pathsOnly);
  }


  public List<DataModelProperty> getDataModelProperties(String iri) {
    return getDataModelProperties(iri, true);
  }


  public List<DataModelProperty> getDataModelProperties(String iri, Boolean includeComplexTypes) {
    TTEntity entity = entityRepository.getBundle(iri, asHashSet(SHACL.PROPERTY, RDFS.LABEL)).getEntity();
    return getDataModelProperties(entity, includeComplexTypes);
  }

  public List<DataModelProperty> getDataModelProperties(TTEntity entity) {
    return getDataModelProperties(entity, true);
  }

  public List<DataModelProperty> getDataModelProperties(TTEntity entity, Boolean includeComplexTypes) {
    List<DataModelProperty> properties = new ArrayList<>();
    if (entity == null)
      return Collections.emptyList();
    if (entity.has(iri(SHACL.PROPERTY))) {
      getDataModelPropertyGroups(entity, properties, includeComplexTypes);
    }
    return properties.stream().sorted(Comparator.comparing(DataModelProperty::getOrder)).toList();
  }

  private void getDataModelPropertyGroups(TTEntity entity, List<DataModelProperty> properties, Boolean includeComplexTypes) {
    for (TTValue propertyGroup : entity.get(iri(SHACL.PROPERTY)).iterator()) {
      if (propertyGroup.isNode()) {
        TTIriRef inheritedFrom = propertyGroup.asNode().has(iri(IM.INHERITED_FROM))
          ? propertyGroup.asNode().get(iri(IM.INHERITED_FROM)).asIriRef()
          : null;
        if (propertyGroup.asNode().has(iri(SHACL.PATH)) && (propertyGroup.asNode().has(iri(SHACL.DATATYPE)) || propertyGroup.asNode().has(iri(SHACL.CLASS)) || includeComplexTypes)) {
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

  public UIProperty getUIPropertyForQB(String dmIri, String propIri) {
    UIProperty uiProp = dataModelRepository.findUIPropertyForQB(dmIri, propIri);
    if (null != uiProp.getIntervalUnitIri()) {
      List<TTIriRef> isas = entityService.getIsas(uiProp.getIntervalUnitIri());
      List<TTIriRef> intervalUnitOptions = isas.stream().filter(unit -> !unit.getIri().equals(uiProp.getIntervalUnitIri())).toList();
      uiProp.setIntervalUnitOptions(intervalUnitOptions);
    }
    if (null != uiProp.getUnitIri()) {
      List<TTIriRef> isas = entityService.getIsas(uiProp.getUnitIri());
      List<TTIriRef> unitOptions = isas.stream().filter(unit -> !unit.getIri().equals(uiProp.getUnitIri())).toList();
      uiProp.setUnitOptions(unitOptions);
    }
    if (null != uiProp.getOperatorIri())
      uiProp.setOperatorOptions(entityService.getOperatorOptions(uiProp.getOperatorIri()));
    return uiProp;
  }

  public List<PropertyDisplay> getPropertiesDisplay(String iri) {
    Set<String> predicates = new HashSet<>();
    predicates.add(SHACL.PROPERTY.toString());
    TTEntity entity = entityRepository.getBundle(iri, predicates).getEntity();
    List<PropertyDisplay> propertyList = new ArrayList<>();
    String entityIri = entity.getIri();
    TTArray ttProperties = entity.get(iri(SHACL.PROPERTY));
    if (null == ttProperties) return propertyList;

    for (TTValue ttProperty : ttProperties.getElements()) {
      String cardinality = getCardinality(ttProperty);
      String reverseCardinality = "0 : * ";
      if (ttProperty.asNode().has(iri(SHACL.NODE))) {
        reverseCardinality = getReverseCardinality(ttProperty, predicates, reverseCardinality, entityIri);
      }
      if (ttProperty.asNode().has(iri(SHACL.OR))) {
        handleOr(ttProperty, cardinality, reverseCardinality, propertyList);
      } else {
        handleNotOr(ttProperty, cardinality, reverseCardinality, propertyList);
      }
    }
    return propertyList;
  }

  private static String getCardinality(TTValue ttProperty) {
    int minCount = 0;
    if (ttProperty.asNode().has(iri(SHACL.MINCOUNT))) {
      minCount = ttProperty.asNode().get(iri(SHACL.MINCOUNT)).asLiteral().intValue();
    }
    int maxCount = 0;
    if (ttProperty.asNode().has(iri(SHACL.MAXCOUNT))) {
      maxCount = ttProperty.asNode().get(iri(SHACL.MAXCOUNT)).asLiteral().intValue();
    }
    return minCount + " : " + (maxCount == 0 ? "*" : maxCount);
  }

  private String getReverseCardinality(TTValue ttProperty, Set<String> predicates, String newCardinality, String entityIri) {
    TTEntity newEntity = entityRepository.getBundle(ttProperty.asNode().get(iri(SHACL.NODE)).asIriRef().getIri(), predicates).getEntity();
    if (newEntity.get(iri(SHACL.PROPERTY)) != null) {
      TTArray newProps = newEntity.get(iri(SHACL.PROPERTY));
      for (TTValue newttProperty : newProps.getElements()) {
        if (newttProperty.asNode().get(iri(SHACL.NODE)) != null && Objects.equals(newttProperty.asNode().get(iri(SHACL.NODE)).get(0).asIriRef().getIri(), entityIri))
          newCardinality = getCardinality(newttProperty);

      }
    }
    return newCardinality;
  }

  private void handleOr(TTValue ttProperty, String cardinality, String reverseCardinality, List<PropertyDisplay> propertyList) {
    PropertyDisplay propertyDisplay = new PropertyDisplay();
    propertyDisplay.setOrder(ttProperty.asNode().get(iri(SHACL.ORDER)).asLiteral().intValue());
    propertyDisplay.setCardinality(cardinality);
    propertyDisplay.setReverseCardinality(reverseCardinality);
    propertyDisplay.setOr(true);
    for (TTValue orProperty : ttProperty.asNode().get(iri(SHACL.OR)).getElements()) {
      TTArray type;
      if (orProperty.asNode().has(iri(SHACL.CLASS))) type = orProperty.asNode().get(iri(SHACL.CLASS));
      else if (orProperty.asNode().has(iri(SHACL.NODE))) type = orProperty.asNode().get(iri(SHACL.NODE));
      else if (orProperty.asNode().has(iri(SHACL.DATATYPE))) type = orProperty.asNode().get(iri(SHACL.DATATYPE));
      else type = new TTArray();
      String name = "";
      if (orProperty.asNode().has(iri(SHACL.PATH))) {
        name += orProperty.asNode().get(iri(SHACL.PATH)).get(0).asIriRef().getIri() + " (";
        if (!type.isEmpty() && !type.get(0).asIriRef().getName().isEmpty()) name += type.get(0).asIriRef().getName();
        else if (!type.isEmpty() && !type.get(0).asIriRef().getIri().isEmpty())
          name += " (" + type.get(0).asIriRef().getIri().split("#")[1];
        name += ")";
        propertyDisplay.addProperty(iri(orProperty.asNode().get(iri(SHACL.PATH)).get(0).asIriRef().getIri(), name));
        propertyDisplay.addType(type.get(0).asIriRef());
      }
      propertyList.add(propertyDisplay);
    }
  }

  private void handleNotOr(TTValue ttProperty, String cardinality, String reverseCardinality, List<PropertyDisplay> propertyList) {
    TTArray type;
    if (ttProperty.asNode().has(iri(SHACL.CLASS))) type = ttProperty.asNode().get(iri(SHACL.CLASS));
    else if (ttProperty.asNode().has(iri(SHACL.NODE))) type = ttProperty.asNode().get(iri(SHACL.NODE));
    else if (ttProperty.asNode().has(iri(SHACL.DATATYPE))) type = ttProperty.asNode().get(iri(SHACL.DATATYPE));
    else type = new TTArray();
    TTValue group = null;
    if (ttProperty.asNode().has(iri(SHACL.GROUP))) {
      group = ttProperty.asNode().get(iri(SHACL.GROUP)).get(0);
    }
    String name = "";
    if (ttProperty.asNode().has(iri(SHACL.PATH))) {
      name += ttProperty.asNode().get(iri(SHACL.PATH)).get(0).asIriRef().getName() + " (";
      if (!type.isEmpty() && type.get(0).asIriRef().getName() != null && !type.get(0).asIriRef().getName().isEmpty())
        name += type.get(0).asIriRef().getName();
      else if (!type.isEmpty() && !type.get(0).asIriRef().getIri().isEmpty()) name += type.get(0).asIriRef().getIri();
      name += ")";
    }
    PropertyDisplay propertyDisplay = new PropertyDisplay();
    if (ttProperty.asNode().get(iri(SHACL.ORDER)) != null)
      propertyDisplay.setOrder(ttProperty.asNode().get(iri(SHACL.ORDER)).asLiteral().intValue());
    propertyDisplay.addProperty(iri(ttProperty.asNode().get(iri(SHACL.PATH)).get(0).asIriRef().getIri(), name));
    propertyDisplay.addType(type.get(0).asIriRef());
    propertyDisplay.setCardinality(cardinality);
    propertyDisplay.setReverseCardinality(reverseCardinality);
    propertyDisplay.setOr(false);
    propertyDisplay.setNode(ttProperty.asNode().get(iri(SHACL.NODE)) != null);
    if (null != group) propertyDisplay.setGroup(group.asIriRef());
    propertyList.add(propertyDisplay);
  }

  public List<NodeShape> getDataModelPropertiesWithValueType(Set<String> iris, String valueType) {
    return dataModelRepository.getDataModelPropertiesWithValueType(iris, valueType);
  }
}
