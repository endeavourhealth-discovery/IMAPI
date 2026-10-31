package org.endeavourhealth.imapi.logic.service

import org.endeavourhealth.imapi.dataaccess.DataModelRepository
import org.endeavourhealth.imapi.dataaccess.EntityRepository
import org.endeavourhealth.imapi.model.DataModelProperty
import org.endeavourhealth.imapi.model.PropertyDisplay
import org.endeavourhealth.imapi.model.iml.NodeShape
import org.endeavourhealth.imapi.model.iml.SemanticMap
import org.endeavourhealth.imapi.model.iml.UIProperty
import org.endeavourhealth.imapi.model.tripletree.TTArray
import org.endeavourhealth.imapi.model.tripletree.TTEntity
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri
import org.endeavourhealth.imapi.model.tripletree.TTValue
import org.endeavourhealth.imapi.utility.Pluraliser
import org.endeavourhealth.imapi.vocabulary.*
import org.springframework.stereotype.Component

@Component
class DataModelService {
  private val entityRepository: EntityRepository
  private val dataModelRepository: DataModelRepository
  private val entityService: EntityService

  constructor() {
    entityRepository = EntityRepository()
    dataModelRepository = DataModelRepository()
    entityService = EntityService(entityRepository)
  }

  constructor(dataModelRepository: DataModelRepository, entityRepository: EntityRepository) {
    this.dataModelRepository = dataModelRepository
    this.entityRepository = entityRepository
    entityService = EntityService(entityRepository)
  }

  fun getDataModelsFromProperty(propIri: String): MutableList<TTIriRef> {
    return dataModelRepository.findDataModelsFromProperty(propIri)
  }

  fun checkPropertyType(iri: String): String? {
    return dataModelRepository.checkPropertyType(iri)
  }

  fun getProperties(): MutableList<TTIriRef> {
    return dataModelRepository.getProperties()
  }

  fun getDataModelDisplayProperties(iri: String, pathsOnly: Boolean): NodeShape {
    return dataModelRepository.getDataModelDisplayProperties(iri, pathsOnly)
  }

  fun getRelatedTypes(iri: String): NodeShape {
    return dataModelRepository.getRelatedTypes(iri)
  }

  fun getDataModelProperties(iri: String): MutableList<DataModelProperty> {
    return getDataModelProperties(iri, true)
  }

  fun getDataModelProperties(iri: String, includeComplexTypes: Boolean): MutableList<DataModelProperty> {
    val entity = entityRepository.getBundle(iri, VocabUtils.asHashSet(SHACL.PROPERTY, RDFS.LABEL)).entity
    return getDataModelProperties(entity, includeComplexTypes)
  }

  fun getDataModelProperties(entity: TTEntity?): MutableList<DataModelProperty> {
    return getDataModelProperties(entity, true)
  }

  fun getDataModelProperties(entity: TTEntity?, includeComplexTypes: Boolean): MutableList<DataModelProperty> {
    val properties: MutableList<DataModelProperty> = mutableListOf()
    if (entity == null) return mutableListOf()
    if (entity.has(iri(SHACL.PROPERTY))) {
      getDataModelPropertyGroups(entity, properties, includeComplexTypes)
    }
    return properties.sortedBy { it.order }.toMutableList()
  }

  private fun getDataModelPropertyGroups(
    entity: TTEntity,
    properties: MutableList<DataModelProperty>,
    includeComplexTypes: Boolean
  ) {
    for (propertyGroup in entity.get(TTIriRef.iri(SHACL.PROPERTY)).iterator()) {
      if (!propertyGroup.isNode) continue
      val node = propertyGroup.asNode()
      val inheritedFrom = node.takeIf { it.has(iri(IM.INHERITED_FROM)) }?.get(iri(IM.INHERITED_FROM))?.asIriRef()

      val hasPath = node.has(iri(SHACL.PATH))
      val hasType = node.has(iri(SHACL.DATATYPE)) || node.has(iri(SHACL.CLASS))
      if (hasPath && (hasType || includeComplexTypes)) {
        getDataModelShaclProperties(properties, propertyGroup, inheritedFrom)
      }
    }
  }

  private fun getDataModelShaclProperties(
    properties: MutableList<DataModelProperty>,
    propertyGroup: TTValue,
    inheritedFrom: TTIriRef?
  ) {
    val propertyPath = propertyGroup.asNode().get(iri(SHACL.PATH)).asIriRef()
    if (properties
        .none { o: DataModelProperty -> o.property?.iri == propertyPath.iri }
    ) {
      properties.add(getPropertyValue(inheritedFrom, propertyGroup, propertyPath))
    }
  }

  private fun getPropertyValue(
    inheritedFrom: TTIriRef?,
    property: TTValue,
    propertyPath: TTIriRef
  ): DataModelProperty {
    val pv = DataModelProperty().setInheritedFrom(inheritedFrom).setProperty(propertyPath)
    val node = property.asNode()
    if (node.has(iri(SHACL.CLASS))) pv.setType(
      node.get(iri(SHACL.CLASS)).asIriRef()
    )
    if (node.has(iri(SHACL.NODE))) pv.setType(
      node.get(iri(SHACL.NODE)).asIriRef()
    )
    if (node.has(iri(OWL.CLASS))) pv.setType(
      node.get(iri(OWL.CLASS)).asIriRef()
    )
    if (node.has(iri(SHACL.DATATYPE))) pv.setType(
      node.get(iri(SHACL.DATATYPE)).asIriRef()
    )
    if (node.has(iri(SHACL.FUNCTION))) pv.setType(
      node.get(iri(SHACL.FUNCTION)).asIriRef()
    )
    if (node.has(iri(SHACL.MAXCOUNT))) pv.setMaxExclusive(
      node.get(iri(SHACL.MAXCOUNT)).asLiteral().value
    )
    if (node.has(iri(SHACL.MINCOUNT))) pv.setMinExclusive(
      node.get(iri(SHACL.MINCOUNT)).asLiteral().value
    )
    pv.setOrder(
      if (node.has(iri(SHACL.ORDER))) node.get(iri(SHACL.ORDER)).asLiteral()
        .intValue() else 0
    )

    return pv
  }

  fun getUIPropertyForQB(dmIri: String, propIri: String): UIProperty {
    val uiProp = dataModelRepository.findUIPropertyForQB(dmIri, propIri)
    if (null != uiProp.intervalUnitIri) {
      val isas = entityService.getIsas(uiProp.intervalUnitIri)
      val intervalUnitOptions =
        isas.filter { unit -> unit.iri != uiProp.intervalUnitIri }
      uiProp.setIntervalUnitOptions(intervalUnitOptions)
    }
    if (null != uiProp.unitIri) {
      val isas = entityService.getIsas(uiProp.unitIri)
      val unitOptions = isas.filter { unit -> unit.iri != uiProp.unitIri }
      unitOptions.forEach { unit -> unit.name = Pluraliser.pluralise(unit.name) }
      uiProp.setUnitOptions(unitOptions)
    }
    if (null != uiProp.operatorIri) uiProp.operatorOptions = entityService.getOperatorOptions(uiProp.operatorIri)
    return uiProp
  }

  fun getPropertiesDisplay(iri: String): MutableList<PropertyDisplay> {
    val predicates: MutableSet<String> = hashSetOf()
    predicates.add(SHACL.PROPERTY.toString())
    val entity = entityRepository.getBundle(iri, predicates).entity
    val propertyList: MutableList<PropertyDisplay> = mutableListOf()
    val entityIri = entity.iri
    val ttProperties = entity.get(iri(SHACL.PROPERTY)) ?: return propertyList

    for (ttProperty in ttProperties.elements) {
      val cardinality: String = getCardinality(ttProperty)
      var reverseCardinality: String = "0 : * "
      if (ttProperty.asNode().has(iri(SHACL.NODE))) {
        reverseCardinality = getReverseCardinality(ttProperty, predicates, reverseCardinality, entityIri)
      }
      if (ttProperty.asNode().has(iri(SHACL.OR))) {
        handleOr(ttProperty, cardinality, reverseCardinality, propertyList)
      } else {
        handleNotOr(ttProperty, cardinality, reverseCardinality, propertyList)
      }
    }
    return propertyList
  }

  private fun getReverseCardinality(
    ttProperty: TTValue,
    predicates: MutableSet<String>,
    newCardinality: String,
    entityIri: String
  ): String {
    var newCardinality = newCardinality
    val newEntity = entityRepository
      .getBundle(ttProperty.asNode().get(iri(SHACL.NODE)).asIriRef().iri, predicates)
      .entity
    newEntity.get(iri(SHACL.PROPERTY))?.elements?.forEach { newttProperty ->
      val node = newttProperty.asNode().get(iri(SHACL.NODE))
      if (node != null && node.get(0).asIriRef().iri == entityIri) newCardinality = getCardinality(newttProperty)
    }
    return newCardinality
  }

  private fun handleOr(
    ttProperty: TTValue,
    cardinality: String,
    reverseCardinality: String,
    propertyList: MutableList<PropertyDisplay>
  ) {
    val propertyDisplay = PropertyDisplay()
    val node = ttProperty.asNode()
    node.get(iri(SHACL.ORDER))?.asLiteral()?.intValue()?.let(propertyDisplay::setOrder)
    propertyDisplay.setCardinality(cardinality)
    propertyDisplay.setReverseCardinality(reverseCardinality)
    propertyDisplay.setIsOr(true)
    for (orProperty in node.get(iri(SHACL.OR)).elements) {
      val orNode = orProperty.asNode()
      val type = listOf(SHACL.CLASS, SHACL.NODE, SHACL.DATATYPE)
        .firstOrNull { orNode.has(iri(it)) }
        ?.let { orNode.get(iri(it)) }
        ?: TTArray()
      if (orNode.has(iri(SHACL.PATH))) {
        val orPath = orNode.get(iri(SHACL.PATH)).get(0).asIriRef()
        val typeRef = type.get(0)?.asIriRef()
        val typeName = when {
          typeRef == null -> ""
          typeRef.name.isNotEmpty() -> typeRef.name
          typeRef.iri.isNotEmpty() -> typeRef.iri.substringAfter("#", "")
          else -> ""
        }
        val name = "${orPath.iri} (${typeName.ifEmpty { "" }})"
        propertyDisplay.addProperty(iri(orPath.iri, name))
        typeRef?.let(propertyDisplay::addType)
      }
      propertyList.add(propertyDisplay)
    }
  }

  private fun handleNotOr(
    ttProperty: TTValue,
    cardinality: String,
    reverseCardinality: String,
    propertyList: MutableList<PropertyDisplay>
  ) {
    val node = ttProperty.asNode()
    val type =
      listOf(SHACL.CLASS, SHACL.NODE, SHACL.DATATYPE).firstOrNull { node.has(iri(it)) }?.let { node.get(iri(it)) }
        ?: TTArray()

    val group = node.get(iri(SHACL.GROUP))?.get(0)?.asIriRef()
    val path = node.get(iri(SHACL.PATH))?.get(0)?.asIriRef() ?: return
    val typeRef = type.get(0)?.asIriRef()
    val typeName = when {
      !typeRef?.name.isNullOrEmpty() -> typeRef.name
      !typeRef?.iri.isNullOrEmpty() -> typeRef.iri
      else -> ""
    }
    val name = "${path.name} ($typeName)"

    val propertyDisplay = PropertyDisplay().apply {
      node.get(iri(SHACL.ORDER))?.asLiteral()?.intValue()?.let { setOrder(it) }
      addProperty(iri(path.iri, name))
      typeRef?.let { addType(it) }
      setCardinality(cardinality)
      setReverseCardinality(reverseCardinality)
      setIsOr(false)
      setIsNode(node.get(iri(SHACL.NODE)) != null)
      group?.let(::setGroup)
    }

    propertyList.add(propertyDisplay)
  }

  fun getDataModelPropertiesWithValueType(iris: MutableSet<String>, valueType: String): MutableList<NodeShape> {
    return dataModelRepository.getDataModelPropertiesWithValueType(iris, valueType)
  }

  fun getInversePath(source: String, target: String): TTIriRef? {
    return dataModelRepository.getInversePath(source, target)
  }

  fun getSemanticMap(iri: String): SemanticMap {
    return dataModelRepository.getSemanticMap(iri)
  }

  fun getFlatDataModelDisplayProperties(iri: String): NodeShape {
    return dataModelRepository.getFlatDataModelDisplayProperties(iri)
  }

  companion object {
    private fun getCardinality(ttProperty: TTValue): String {
      val minCount = if (ttProperty.asNode().has(iri(SHACL.MINCOUNT))) {
        ttProperty.asNode().get(iri(SHACL.MINCOUNT)).asLiteral().intValue()
      } else 0
      val maxCount = if (ttProperty.asNode().has(iri(SHACL.MAXCOUNT))) {
        ttProperty.asNode().get(iri(SHACL.MAXCOUNT)).asLiteral().intValue()
      } else 0
      return "$minCount : ${(if (maxCount == 0) "*" else maxCount)}"
    }
  }
}
