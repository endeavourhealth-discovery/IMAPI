package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.DataModelRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.PropertyDisplay;
import org.endeavourhealth.imapi.model.extensions.TTIriRefExtensionsKt;
import org.endeavourhealth.imapi.model.iml.UIProperty;
import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTValueJava;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.imapi.utility.Pluraliser;
import org.endeavourhealth.interfacemanager.model.*;
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

  private static String getCardinality(TTValueJava ttProperty) {
    int minCount = 0;
    if (ttProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.MINCOUNT))) {
      minCount = ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.MINCOUNT)).asLiteral().intValue();
    }
    int maxCount = 0;
    if (ttProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.MAXCOUNT))) {
      maxCount = ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.MAXCOUNT)).asLiteral().intValue();
    }
    return minCount + " : " + (maxCount == 0 ? "*" : maxCount);
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

  public NodeShape getDataModelDisplayProperties(String iri, boolean pathsOnly, boolean excludeGeneric) {
    return dataModelRepository.getDataModelDisplayProperties(iri, pathsOnly, excludeGeneric);
  }

  public List<DataModelProperty> getDataModelProperties(String iri) {
    return getDataModelProperties(iri, true);
  }

  public List<DataModelProperty> getDataModelProperties(String iri, Boolean includeComplexTypes) {
    TTEntityJava entity = entityRepository.getBundle(iri, EnumUtils.asHashSet(ShaclVocab.PROPERTY, RdfsVocab.LABEL)).getEntity();
    return getDataModelProperties(entity, includeComplexTypes);
  }

  public List<DataModelProperty> getDataModelProperties(TTEntityJava entity) {
    return getDataModelProperties(entity, true);
  }

  public List<DataModelProperty> getDataModelProperties(TTEntityJava entity, Boolean includeComplexTypes) {
    List<DataModelProperty> properties = new ArrayList<>();
    if (entity == null)
      return Collections.emptyList();
    if (entity.has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY))) {
      getDataModelPropertyGroups(entity, properties, includeComplexTypes);
    }
    return properties.stream().sorted(Comparator.comparing(DataModelProperty::getOrder)).toList();
  }

  private void getDataModelPropertyGroups(TTEntityJava entity, List<DataModelProperty> properties, Boolean includeComplexTypes) {
    for (TTValueJava propertyGroup : entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY)).iterator()) {
      if (propertyGroup.isNode()) {
        TTIriRef inheritedFrom = propertyGroup.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.INHERITED_FROM))
          ? propertyGroup.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.INHERITED_FROM)).asIriRef()
          : null;
        if (propertyGroup.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)) && (propertyGroup.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.DATATYPE)) || propertyGroup.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.CLASS)) || includeComplexTypes)) {
          getDataModelShaclProperties(properties, propertyGroup, inheritedFrom);
        }
      }
    }
  }

  private void getDataModelShaclProperties(List<DataModelProperty> properties, TTValueJava propertyGroup, TTIriRef inheritedFrom) {
    TTIriRef propertyPath = propertyGroup.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)).asIriRef();
    if (properties.stream()
      .noneMatch(o -> o.getProperty().getIri().equals(propertyPath.getIri()))) {
      properties.add(getPropertyValue(inheritedFrom, propertyGroup, propertyPath));
    }
  }

  private DataModelProperty getPropertyValue(TTIriRef inheritedFrom, TTValueJava property, TTIriRef propertyPath) {
    DataModelProperty pv = new DataModelProperty().inheritedFrom(inheritedFrom).property(propertyPath);

    if (property.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.CLASS)))
      pv.setType(property.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.CLASS)).asIriRef());
    if (property.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE)))
      pv.setType(property.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE)).asIriRef());
    if (property.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.CLASS)))
      pv.setType(property.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.CLASS)).asIriRef());
    if (property.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.DATATYPE)))
      pv.setType(property.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.DATATYPE)).asIriRef());
    if (property.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.FUNCTION)))
      pv.setType(property.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.FUNCTION)).asIriRef());
    if (property.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.MAXCOUNT)))
      pv.setMaxExclusive(property.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.MAXCOUNT)).asLiteral().getValue());
    if (property.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.MINCOUNT)))
      pv.setMinExclusive(property.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.MINCOUNT)).asLiteral().getValue());
    pv.setOrder(property.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.ORDER)) ? property.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.ORDER)).asLiteral().intValue() : 0);

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
    TTEntityJava entity = entityRepository.getBundle(iri, predicates).getEntity();
    List<PropertyDisplay> propertyList = new ArrayList<>();
    String entityIri = entity.getIri();
    TTArrayJava ttProperties = entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY));
    if (null == ttProperties) return propertyList;

    for (TTValueJava ttProperty : ttProperties.getElements()) {
      String cardinality = getCardinality(ttProperty);
      String reverseCardinality = "0 : * ";
      if (ttProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE))) {
        reverseCardinality = getReverseCardinality(ttProperty, predicates, reverseCardinality, entityIri);
      }
      if (ttProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.OR))) {
        handleOr(ttProperty, cardinality, reverseCardinality, propertyList);
      } else {
        handleNotOr(ttProperty, cardinality, reverseCardinality, propertyList);
      }
    }
    return propertyList;
  }

  private String getReverseCardinality(TTValueJava ttProperty, Set<String> predicates, String newCardinality, String entityIri) {
    TTEntityJava newEntity = entityRepository.getBundle(ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE)).asIriRef().getIri(), predicates).getEntity();
    if (newEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY)) != null) {
      TTArrayJava newProps = newEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY));
      for (TTValueJava newttProperty : newProps.getElements()) {
        if (newttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE)) != null && Objects.equals(newttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE)).get(0).asIriRef().getIri(), entityIri))
          newCardinality = getCardinality(newttProperty);

      }
    }
    return newCardinality;
  }

  private void handleOr(TTValueJava ttProperty, String cardinality, String reverseCardinality, List<PropertyDisplay> propertyList) {
    PropertyDisplay propertyDisplay = new PropertyDisplay();
    propertyDisplay.setOrder(ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.ORDER)).asLiteral().intValue());
    propertyDisplay.setCardinality(cardinality);
    propertyDisplay.setReverseCardinality(reverseCardinality);
    propertyDisplay.setOr(true);
    for (TTValueJava orProperty : ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.OR)).getElements()) {
      TTArrayJava type;
      if (orProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.CLASS)))
        type = orProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.CLASS));
      else if (orProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE)))
        type = orProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE));
      else if (orProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.DATATYPE)))
        type = orProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.DATATYPE));
      else type = new TTArrayJava();
      String name = "";
      if (orProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH))) {
        name += orProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)).get(0).asIriRef().getIri() + " (";
        if (!type.isEmpty() && !type.get(0).asIriRef().getName().isEmpty()) name += type.get(0).asIriRef().getName();
        else if (!type.isEmpty() && !type.get(0).asIriRef().getIri().isEmpty())
          name += " (" + type.get(0).asIriRef().getIri().split("#")[1];
        name += ")";
        propertyDisplay.addProperty(TTIriRefExtensionsKt.iri(new TTIriRef(), orProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)).get(0).asIriRef().getIri(), name));
        propertyDisplay.addType(type.get(0).asIriRef());
      }
      propertyList.add(propertyDisplay);
    }
  }

  private void handleNotOr(TTValueJava ttProperty, String cardinality, String reverseCardinality, List<PropertyDisplay> propertyList) {
    TTArrayJava type;
    if (ttProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.CLASS)))
      type = ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.CLASS));
    else if (ttProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE)))
      type = ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE));
    else if (ttProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.DATATYPE)))
      type = ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.DATATYPE));
    else type = new TTArrayJava();
    TTValueJava group = null;
    if (ttProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.GROUP))) {
      group = ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.GROUP)).get(0);
    }
    String name = "";
    if (ttProperty.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH))) {
      name += ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)).get(0).asIriRef().getName() + " (";
      if (!type.isEmpty() && type.get(0).asIriRef().getName() != null && !type.get(0).asIriRef().getName().isEmpty())
        name += type.get(0).asIriRef().getName();
      else if (!type.isEmpty() && !type.get(0).asIriRef().getIri().isEmpty()) name += type.get(0).asIriRef().getIri();
      name += ")";
    }
    PropertyDisplay propertyDisplay = new PropertyDisplay();
    if (ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.ORDER)) != null)
      propertyDisplay.setOrder(ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.ORDER)).asLiteral().intValue());
    propertyDisplay.addProperty(TTIriRefExtensionsKt.iri(new TTIriRef(), ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)).get(0).asIriRef().getIri(), name));
    propertyDisplay.addType(type.get(0).asIriRef());
    propertyDisplay.setCardinality(cardinality);
    propertyDisplay.setReverseCardinality(reverseCardinality);
    propertyDisplay.setOr(false);
    propertyDisplay.setNode(ttProperty.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE)) != null);
    if (null != group) propertyDisplay.setGroup(group.asIriRef());
    propertyList.add(propertyDisplay);
  }

  public List<NodeShape> getDataModelPropertiesWithValueType(Set<String> iris, String valueType) {
    return dataModelRepository.getDataModelPropertiesWithValueType(iris, valueType);
  }

  public TTIriRef getInversePath(String source, String target) {
    return dataModelRepository.getInversePath(source, target);
  }
}
