package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.DataModelRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.PropertyDisplay;
import org.endeavourhealth.imapi.model.iml.NodeShape;
import org.endeavourhealth.imapi.model.iml.UIProperty;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.imapi.utility.Pluraliser;
import org.springframework.stereotype.Component;

import java.util.*;

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

  private static String getCardinality(TTValue ttProperty) {
    int minCount = 0;
    if (ttProperty.asNode().has(new TTIriRefExtended(ShaclVocab.MINCOUNT))) {
      minCount = ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.MINCOUNT)).asLiteral().intValue();
    }
    int maxCount = 0;
    if (ttProperty.asNode().has(new TTIriRefExtended(ShaclVocab.MAXCOUNT))) {
      maxCount = ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.MAXCOUNT)).asLiteral().intValue();
    }
    return minCount + " : " + (maxCount == 0 ? "*" : maxCount);
  }

  public List<TTIriRefExtended> getDataModelsFromProperty(String propIri) {
    return dataModelRepository.findDataModelsFromProperty(propIri);
  }

  public String checkPropertyType(String iri) {
    return dataModelRepository.checkPropertyType(iri);
  }

  public List<TTIriRefExtended> getProperties() {
    return dataModelRepository.getProperties();
  }

  public NodeShape getDataModelDisplayProperties(String iri, boolean pathsOnly, boolean excludeGeneric) {
    return dataModelRepository.getDataModelDisplayProperties(iri, pathsOnly, excludeGeneric);
  }

  public List<DataModelProperty> getDataModelProperties(String iri) {
    return getDataModelProperties(iri, true);
  }

  public List<DataModelProperty> getDataModelProperties(String iri, Boolean includeComplexTypes) {
    TTEntity entity = entityRepository.getBundle(iri, EnumUtils.asHashSet(ShaclVocab.PROPERTY, RdfsVocab.LABEL)).getEntity();
    return getDataModelProperties(entity, includeComplexTypes);
  }

  public List<DataModelProperty> getDataModelProperties(TTEntity entity) {
    return getDataModelProperties(entity, true);
  }

  public List<DataModelProperty> getDataModelProperties(TTEntity entity, Boolean includeComplexTypes) {
    List<DataModelProperty> properties = new ArrayList<>();
    if (entity == null)
      return Collections.emptyList();
    if (entity.has(new TTIriRefExtended(ShaclVocab.PROPERTY))) {
      getDataModelPropertyGroups(entity, properties, includeComplexTypes);
    }
    return properties.stream().sorted(Comparator.comparing(DataModelProperty::getOrder)).toList();
  }

  private void getDataModelPropertyGroups(TTEntity entity, List<DataModelProperty> properties, Boolean includeComplexTypes) {
    for (TTValue propertyGroup : entity.get(new TTIriRefExtended(ShaclVocab.PROPERTY)).iterator()) {
      if (propertyGroup.isNode()) {
        TTIriRefExtended inheritedFrom = propertyGroup.asNode().has(new TTIriRefExtended(ImVocab. INHERITED_FROM))
          ?propertyGroup.asNode().get(new TTIriRefExtended(ImVocab. INHERITED_FROM)).asIriRef()
          :null;
        if (propertyGroup.asNode().has(new TTIriRefExtended(ShaclVocab.PATH)) && (propertyGroup.asNode().has(new TTIriRefExtended(ShaclVocab.DATATYPE)) || propertyGroup.asNode().has(new TTIriRefExtended(ShaclVocab.CLASS)) || includeComplexTypes)) {
          getDataModelShaclProperties(properties, propertyGroup, inheritedFrom);
        }
      }
    }
  }

  private void getDataModelShaclProperties(List<DataModelProperty> properties, TTValue propertyGroup, TTIriRefExtended inheritedFrom) {
    TTIriRefExtended propertyPath = propertyGroup.asNode().get(new TTIriRefExtended(ShaclVocab.PATH)).asIriRef();
    if (properties.stream()
      .noneMatch(o -> o.getProperty().getIri().equals(propertyPath.getIri()))) {
      properties.add(getPropertyValue(inheritedFrom, propertyGroup, propertyPath));
    }
  }

  private DataModelProperty getPropertyValue(TTIriRefExtended inheritedFrom, TTValue property, TTIriRefExtended propertyPath) {
    DataModelProperty pv = new DataModelProperty().setInheritedFrom(inheritedFrom).setProperty(propertyPath);

    if (property.asNode().has(new TTIriRefExtended(ShaclVocab.CLASS)))
      pv.setType(property.asNode().get(new TTIriRefExtended(ShaclVocab.CLASS)).asIriRef());
    if (property.asNode().has(new TTIriRefExtended(ShaclVocab.NODE)))
      pv.setType(property.asNode().get(new TTIriRefExtended(ShaclVocab.NODE)).asIriRef());
    if (property.asNode().has(new TTIriRefExtended(OwlVocab.CLASS)))
      pv.setType(property.asNode().get(new TTIriRefExtended(OwlVocab.CLASS)).asIriRef());
    if (property.asNode().has(new TTIriRefExtended(ShaclVocab.DATATYPE)))
      pv.setType(property.asNode().get(new TTIriRefExtended(ShaclVocab.DATATYPE)).asIriRef());
    if (property.asNode().has(new TTIriRefExtended(ShaclVocab.FUNCTION)))
      pv.setType(property.asNode().get(new TTIriRefExtended(ShaclVocab.FUNCTION)).asIriRef());
    if (property.asNode().has(new TTIriRefExtended(ShaclVocab.MAXCOUNT)))
      pv.setMaxExclusive(property.asNode().get(new TTIriRefExtended(ShaclVocab.MAXCOUNT)).asLiteral().getValue());
    if (property.asNode().has(new TTIriRefExtended(ShaclVocab.MINCOUNT)))
      pv.setMinExclusive(property.asNode().get(new TTIriRefExtended(ShaclVocab.MINCOUNT)).asLiteral().getValue());
    pv.setOrder(property.asNode().has(new TTIriRefExtended(ShaclVocab.ORDER)) ? property.asNode().get(new TTIriRefExtended(ShaclVocab.ORDER)).asLiteral().intValue() : 0);

    return pv;
  }

  public UIProperty getUIPropertyForQB(String dmIri, String propIri) {
    UIProperty uiProp = dataModelRepository.findUIPropertyForQB(dmIri, propIri);
    if (null != uiProp.getIntervalUnitIri()) {
      List<TTIriRefExtended> isas = entityService.getIsas(uiProp.getIntervalUnitIri());
      List<TTIriRefExtended> intervalUnitOptions = isas.stream().filter(unit -> !unit.getIri().equals(uiProp.getIntervalUnitIri())).toList();
      uiProp.setIntervalUnitOptions(intervalUnitOptions);
    }
    if (null != uiProp.getUnitIri()) {
      List<TTIriRefExtended> isas = entityService.getIsas(uiProp.getUnitIri());
      List<TTIriRefExtended> unitOptions = isas.stream().filter(unit -> !unit.getIri().equals(uiProp.getUnitIri())).toList();
      unitOptions.forEach(unit -> unit.setName(Pluraliser.pluralise(unit.getName())));
      uiProp.setUnitOptions(unitOptions);
    }
    if (null != uiProp.getOperatorIri())
      uiProp.setOperatorOptions(entityService.getOperatorOptions(uiProp.getOperatorIri()));
    return uiProp;
  }

  public List<PropertyDisplay> getPropertiesDisplay(String iri) {
    Set<String> predicates = new HashSet<>();
    predicates.add(ShaclVocab.PROPERTY.toString());
    TTEntity entity = entityRepository.getBundle(iri, predicates).getEntity();
    List<PropertyDisplay> propertyList = new ArrayList<>();
    String entityIri = entity.getIri();
    TTArray ttProperties = entity.get(new TTIriRefExtended(ShaclVocab.PROPERTY));
    if (null == ttProperties) return propertyList;

    for (TTValue ttProperty : ttProperties.getElements()) {
      String cardinality = getCardinality(ttProperty);
      String reverseCardinality = "0 : * ";
      if (ttProperty.asNode().has(new TTIriRefExtended(ShaclVocab.NODE))) {
        reverseCardinality = getReverseCardinality(ttProperty, predicates, reverseCardinality, entityIri);
      }
      if (ttProperty.asNode().has(new TTIriRefExtended(ShaclVocab.OR))) {
        handleOr(ttProperty, cardinality, reverseCardinality, propertyList);
      } else {
        handleNotOr(ttProperty, cardinality, reverseCardinality, propertyList);
      }
    }
    return propertyList;
  }

  private String getReverseCardinality(TTValue ttProperty, Set<String> predicates, String newCardinality, String entityIri) {
    TTEntity newEntity = entityRepository.getBundle(ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.NODE)).asIriRef().getIri(), predicates).getEntity();
    if (newEntity.get(new TTIriRefExtended(ShaclVocab.PROPERTY)) != null) {
      TTArray newProps = newEntity.get(new TTIriRefExtended(ShaclVocab.PROPERTY));
      for (TTValue newttProperty : newProps.getElements()) {
        if (newttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.NODE)) != null && Objects.equals(newttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.NODE)).get(0).asIriRef().getIri(), entityIri))
          newCardinality = getCardinality(newttProperty);

      }
    }
    return newCardinality;
  }

  private void handleOr(TTValue ttProperty, String cardinality, String reverseCardinality, List<PropertyDisplay> propertyList) {
    PropertyDisplay propertyDisplay = new PropertyDisplay();
    propertyDisplay.setOrder(ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.ORDER)).asLiteral().intValue());
    propertyDisplay.setCardinality(cardinality);
    propertyDisplay.setReverseCardinality(reverseCardinality);
    propertyDisplay.setOr(true);
    for (TTValue orProperty : ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.OR)).getElements()) {
      TTArray type;
      if (orProperty.asNode().has(new TTIriRefExtended(ShaclVocab.CLASS))) type = orProperty.asNode().get(new TTIriRefExtended(ShaclVocab.CLASS));
      else if (orProperty.asNode().has(new TTIriRefExtended(ShaclVocab.NODE)))
        type = orProperty.asNode().get(new TTIriRefExtended(ShaclVocab.NODE));
      else if (orProperty.asNode().has(new TTIriRefExtended(ShaclVocab.DATATYPE)))
        type = orProperty.asNode().get(new TTIriRefExtended(ShaclVocab.DATATYPE));
      else type = new TTArray();
      String name = "";
      if (orProperty.asNode().has(new TTIriRefExtended(ShaclVocab.PATH))) {
        name += orProperty.asNode().get(new TTIriRefExtended(ShaclVocab.PATH)).get(0).asIriRef().getIri() + " (";
        if (!type.isEmpty() && !type.get(0).asIriRef().getName().isEmpty()) name += type.get(0).asIriRef().getName();
        else if (!type.isEmpty() && !type.get(0).asIriRef().getIri().isEmpty())
          name += " (" + type.get(0).asIriRef().getIri().split("#")[1];
        name += ")";
        propertyDisplay.addProperty(new TTIriRefExtended(orProperty.asNode().get(new TTIriRefExtended(ShaclVocab.PATH)).get(0).asIriRef().getIri(), name));
        propertyDisplay.addType(type.get(0).asIriRef());
      }
      propertyList.add(propertyDisplay);
    }
  }

  private void handleNotOr(TTValue ttProperty, String cardinality, String reverseCardinality, List<PropertyDisplay> propertyList) {
    TTArray type;
    if (ttProperty.asNode().has(new TTIriRefExtended(ShaclVocab.CLASS))) type = ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.CLASS));
    else if (ttProperty.asNode().has(new TTIriRefExtended(ShaclVocab.NODE)))
      type = ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.NODE));
    else if (ttProperty.asNode().has(new TTIriRefExtended(ShaclVocab.DATATYPE)))
      type = ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.DATATYPE));
    else type = new TTArray();
    TTValue group = null;
    if (ttProperty.asNode().has(new TTIriRefExtended(ShaclVocab.GROUP))) {
      group = ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.GROUP)).get(0);
    }
    String name = "";
    if (ttProperty.asNode().has(new TTIriRefExtended(ShaclVocab.PATH))) {
      name += ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.PATH)).get(0).asIriRef().getName() + " (";
      if (!type.isEmpty() && type.get(0).asIriRef().getName() != null && !type.get(0).asIriRef().getName().isEmpty())
        name += type.get(0).asIriRef().getName();
      else if (!type.isEmpty() && !type.get(0).asIriRef().getIri().isEmpty()) name += type.get(0).asIriRef().getIri();
      name += ")";
    }
    PropertyDisplay propertyDisplay = new PropertyDisplay();
    if (ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.ORDER)) != null)
      propertyDisplay.setOrder(ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.ORDER)).asLiteral().intValue());
    propertyDisplay.addProperty(new TTIriRefExtended(ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.PATH)).get(0).asIriRef().getIri(), name));
    propertyDisplay.addType(type.get(0).asIriRef());
    propertyDisplay.setCardinality(cardinality);
    propertyDisplay.setReverseCardinality(reverseCardinality);
    propertyDisplay.setOr(false);
    propertyDisplay.setNode(ttProperty.asNode().get(new TTIriRefExtended(ShaclVocab.NODE)) != null);
    if (null != group) propertyDisplay.setGroup(group.asIriRef());
    propertyList.add(propertyDisplay);
  }

  public List<NodeShape> getDataModelPropertiesWithValueType(Set<String> iris, String valueType) {
    return dataModelRepository.getDataModelPropertiesWithValueType(iris, valueType);
  }

  public TTIriRefExtended getInversePath(String source, String target) {
    return dataModelRepository.getInversePath(source, target);
  }
}
