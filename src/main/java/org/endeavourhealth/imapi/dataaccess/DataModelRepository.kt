package org.endeavourhealth.imapi.dataaccess

import org.eclipse.rdf4j.model.util.Values
import org.eclipse.rdf4j.query.BindingSet
import org.endeavourhealth.imapi.dataaccess.databases.IMDB
import org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper
import org.endeavourhealth.imapi.model.iml.*
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.RDFS
import org.endeavourhealth.imapi.vocabulary.XSD

class DataModelRepository {
  fun getProperties(): MutableList<TTIriRef> {
    val result: MutableList<TTIriRef> = mutableListOf()

    val spql = """
      SELECT ?s ?name
      WHERE {
        ?s rdf:type rdf:PropertyRef ;
        rdfs:label ?name .
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(spql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          result.add(TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()))
        }
      }
    }
    return result
  }

  fun findDataModelsFromProperty(propIri: String): MutableList<TTIriRef> {
    val dmList: MutableList<TTIriRef> = mutableListOf()
    IMDB.getConnection().use { conn ->
      val sparql = """
        SELECT ?dm ?dmName
        WHERE {
          ?dm sh:property ?prop .
          ?dm rdfs:label ?dmName .
          ?prop sh:path ?propIri .
        }
        
        """.trimIndent()
      val qry = conn.prepareTupleSparql(sparql)
      qry.setBinding("propIri", Values.iri(propIri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          dmList.add(TTIriRef(bs.getValue("dm").stringValue(), bs.getValue("dmName").stringValue()))
        }
      }
    }
    return dmList
  }

  fun checkPropertyType(propIri: String): String? {
    IMDB.getConnection().use { conn ->
      val query = """
        SELECT ?objectProperty ?dataProperty
        WHERE {
          bind(exists{?propIr i ?isA ?objProp} as ?objectProperty)
          bind(exists{?propIri ?isA ?dataProp} as ?dataProperty)
        }
        
        """.trimIndent()
      val qry = conn.prepareTupleSparql(query)
      qry.setBinding("propIri", Values.iri(propIri))
      qry.setBinding("isA", IM.IS_A.asDbIri())
      qry.setBinding("objProp", IM.DATAMODEL_OBJECTPROPERTY.asDbIri())
      qry.setBinding("dataProp", IM.DATAMODEL_DATAPROPERTY.asDbIri())
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          if (bs.hasBinding("objectProperty")) return IM.DATAMODEL_OBJECTPROPERTY.toString()
          else if (bs.hasBinding("dataProperty")) return IM.DATAMODEL_DATAPROPERTY.toString()
        }
      }
    }
    return null
  }

  fun addDataModelSubtypes(dataModel: NodeShape) {
    IMDB.getConnection().use { conn ->
      val sql = this.subtypeSql
      val qry = conn.prepareTupleSparql(sql)
      qry.setBinding("entity", Values.iri(dataModel.iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          if (bs.getValue("subdatamodel") != null) {
            dataModel.addSubType(
              TTIriRef.iri(bs.getValue("subdatamodel").stringValue())
                .setName(bs.getValue("subdatamodelname").stringValue())
            )
          }
        }
      }
    }
  }


  fun getRelatedTypes(iri: String): NodeShape {
    val result = getDataModelDisplayProperties(iri, false)
    val sql: String = """
      Select distinct ?folderOrder ?folder  ?folderName ?type ?typeName   
      where {
          Values ?c {<${iri}>}
          ?c rdfs:label ?cName.
          ?type sh:property ?property.
          ?property sh:node ?c.
          filter not exists{
              ?c sh:property ?property1.
              ?property1 sh:path ?path.
              filter not exists {?path rdf:type sh:Function}
              ?property1 sh:node ?type.
          }
          filter not exists {?type im:abstract true}
          ?type im:isContainedIn ?folder.
          optional {?folder sh:order ?folderOrder}
          ?folder rdfs:label ?folderName.
          ?type rdfs:label ?typeName.
      }
      order by ?folderOrder ?folder ?typeName
      
      """.trimIndent()
    val folderMap: MutableMap<String, NodeShape> = mutableMapOf()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val folderIri = bs.getValue("folder").stringValue()
          val folderName = bs.getValue("folderName").stringValue()
          val typeName = bs.getValue("typeName").stringValue()
          val typeIri = bs.getValue("type").stringValue()
          var folder = folderMap[folderIri]
          if (folder == null) {
            folder = NodeShape()
            folder.setIri(folderIri)
            folder.setName(folderName)
            folderMap[folderIri] = folder
            result.addFolder(folder)
          }
          val type = NodeShape()
          type.setIri(typeIri)
          type.setName(typeName)
          folder.addType(type)
        }
      }
    }
    return result
  }


  fun getDataModelDisplayProperties(iri: String, pathsOnly: Boolean): NodeShape {
    val nodeShape = NodeShape()
    nodeShape.setIri(iri)
    addDataModelSubtypes(nodeShape)
    IMDB.getConnection().use { conn ->
      val sql = if (pathsOnly) getPathSql(iri) else getPropertySql(iri)
      val qry = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          nodeShape.setName(bs.getValue("entityName").stringValue())
          var group: PropertyShape? = null
          if (bs.getValue("path") != null) {
            if (bs.getValue("group") != null) {
              group = getGroupFromNode(bs, nodeShape)
            }
            addProperty(nodeShape, group, bs)
          }
        }
      }
    }
    return nodeShape
  }

  private fun getPropertyFromNode(node: NodeShape, iri: String): PropertyShape {
    if (node.property != null) {
      val found = node.property.firstOrNull() { it.path.iri == iri }
      if (found != null) return found
    }
    val group = PropertyShape()
    node.addProperty(group)
    group.setPath(TTIriRef.iri(iri))
    return group
  }

  private fun getGroupFromNode(bs: BindingSet, nodeShape: NodeShape): PropertyShape {
    val groupIri = bs.getValue("group").stringValue()
    val group = getPropertyFromNode(nodeShape, groupIri)
    group.setGroup(TTIriRef.iri(groupIri).setName(bs.getValue("groupName").stringValue()))
    group.setOrder(bs.getValue("groupOrder").stringValue().toInt())
    if (bs.getValue("highCardinality") != null) {
      if (group.highCardinality != null) {
        group.highCardinality = true
      }
    } else if (group.highCardinality == null) {
      group.highCardinality = false
    }
    return group
  }

  private fun getPropertyFromGroup(group: PropertyShape, iri: String): PropertyShape {
    if (group.property != null) {
      val found = group.property.firstOrNull { it.path.iri == iri }
      if (found != null) return found
    }
    val property = PropertyShape()
    property.setPath(TTIriRef.iri(iri))
    group.addProperty(property)
    return property
  }

  private fun addProperty(node: NodeShape, group: PropertyShape?, bs: BindingSet) {
    val propertyIri = bs.getValue("path").stringValue()

    val property = if (group != null) getPropertyFromGroup(group, propertyIri)
    else getPropertyFromNode(node, propertyIri)

    property.path.setName(bs.getValue("pathName").stringValue())

    if (bs.getValue("class") != null) {
      property.setClazz(
        PropertyRange().setIri(bs.getValue("class").stringValue()).setName(bs.getValue("className").stringValue())
          .setType(
            TTIriRef.iri(bs.getValue("classType").stringValue()).setName(bs.getValue("classTypeName").stringValue())
          )
      )
    }

    if (bs.getValue("datatype") != null) {
      getPropertyDataType(bs, property)
    } else if (bs.getValue("node") != null) {
      property.setNode(
        PropertyRange().setIri(bs.getValue("node").stringValue()).setName(bs.getValue("nodeName").stringValue())
          .setType(
            TTIriRef.iri(bs.getValue("nodeType").stringValue()).setName(bs.getValue("nodeTypeName").stringValue())
          )
      )
    }

    if (bs.getValue("order") != null) {
      property.setOrder(bs.getValue("order").stringValue().toInt())
    }
    if (bs.getValue("minCount") != null) {
      property.setMinCount(bs.getValue("minCount").stringValue().toInt())
    }
    if (bs.getValue("minCount") != null) {
      property.setMaxCount(bs.getValue("minCount").stringValue().toInt())
    }
    if (bs.getValue("definingProperty") != null) {
      property.isDefiningProperty = true
      node.setDefiningProperty(TTIriRef.iri(propertyIri))
    }
    if (bs.getValue("orderable") != null) {
      getPropertyOrderable(bs, property)
    }
    if (bs.getValue("comment") != null) {
      property.setComment(bs.getValue("comment").stringValue())
    }

    if (bs.getValue("hasValue") != null) {
      getPropertyValue(bs, property)
    }
    if (bs.getValue("parameter") != null) {
      addParameter(property, bs)
    }
    if (bs.getValue("propertyDefinition") != null) {
      property.setDefinition(bs.getValue("propertyDefinition").stringValue())
    }
    if (bs.getValue("highCardinality") != null) {
      property.highCardinality = true
    }
    if (bs.getValue("inversePath") != null) {
      property.setInversePath(
        TTIriRef.iri(bs.getValue("inversePath").stringValue())
          .setName(bs.getValue("inversePathName").stringValue())
      )
    }
    if (bs.getValue("association") != null) {
      if (bs.getValue("association").stringValue() == "true") {
        property.setAssociation(true)
      }
    }
  }

  private fun getPropertyDataType(bs: BindingSet, property: PropertyShape) {
    var datatype = property.datatype
    if (datatype == null) {
      datatype = PropertyRange()
      datatype.setIri(bs.getValue("datatype").stringValue()).setName(bs.getValue("datatypeName").stringValue()).setType(
        TTIriRef.iri(bs.getValue("datatypeType").stringValue()).setName(bs.getValue("datatypeTypeName").stringValue())
      )
      property.setDatatype(datatype)
      if (bs.getValue("pattern") != null) {
        datatype.setPattern(bs.getValue("pattern").stringValue())
      }
      if (bs.getValue("isRelativeValue") != null) {
        datatype.setRelativeValue("true".equals(bs.getValue("isRelativeValue").stringValue(), ignoreCase = true))
      }
      if (bs.getValue("units") != null) {
        datatype.setUnits(
          TTIriRef.iri(bs.getValue("units").stringValue()).setName(bs.getValue("unitsName").stringValue())
        )
      }
      if (bs.getValue("operator") != null) {
        datatype.setOperator(
          TTIriRef.iri(bs.getValue("operator").stringValue()).setName(bs.getValue("operatorName").stringValue())
        )
      }
    }
    if (bs.getValue("datatypeQualifier") != null) {
      addDataTypeQualifier(datatype, bs)
    }
  }

  private fun addParameter(property: PropertyShape, bs: BindingSet) {
    val parameter = getParameterFromProperty(property, bs.getValue("parameterName").stringValue())
    parameter.setLabel(bs.getValue("parameterName").stringValue())
    parameter.setType(
      TTIriRef.iri(bs.getValue("parameterType").stringValue()).setName(bs.getValue("parameterTypeName").stringValue())
    )
    if (bs.getValue("parameterSubtype") != null) {
      if (parameter.parameterSubType == null) {
        parameter.addParameterSubType(
          TTIriRef.iri(bs.getValue("parameterSubtype").stringValue())
            .setName(bs.getValue("parameterSubtypeName").stringValue())
        )
      } else if (!parameter.parameterSubType
          .contains(TTIriRef.iri(bs.getValue("parameterSubtype").stringValue()))
      ) {
        parameter.addParameterSubType(
          TTIriRef.iri(bs.getValue("parameterSubtype").stringValue())
            .setName(bs.getValue("parameterSubtypeName").stringValue())
        )
      }
    }
  }

  private fun addDataTypeQualifier(datatype: PropertyRange, bs: BindingSet) {
    val qualifierIri = bs.getValue("datatypeQualifier").stringValue()
    val qualifier = getQualifierFromDataType(datatype, qualifierIri)
    qualifier.setIri(bs.getValue("datatypeQualifier").stringValue()).setName(bs.getValue("qualifierName").stringValue())
    if (bs.getValue("qualifierPattern") != null) {
      qualifier.setPattern(bs.getValue("qualifierPattern").stringValue())
    }
    if (bs.getValue("qualifierIntervalUnit") != null) {
      qualifier.setIntervalUnit(TTIriRef.iri(bs.getValue("qualifierIntervalUnit").stringValue()))
    }
  }

  private fun getQualifierFromDataType(datatype: PropertyRange, iri: String): PropertyRange {
    if (datatype.qualifier != null) {
      val found = datatype.qualifier.firstOrNull { it.iri == iri }
      if (found != null) return found
    }
    val qualifier = PropertyRange()
    qualifier.setIri(iri)
    datatype.addQualifier(qualifier)
    return qualifier
  }


  private fun getParameterFromProperty(property: PropertyShape, parameterName: String): ParameterShape {
    if (property.parameter != null) {
      for (param in property.parameter) {
        if (param.label == parameterName) {
          return param
        }
      }
    }
    val param = ParameterShape()
    property.addParameter(param)
    return param
  }


  private val subtypeSql: String
    get() = """
      Select ?subdatamodel ?subdatamodelname
      WHERE {
        optional  {
          ?subdatamodel rdfs:subClassOf ?entity.
          ?subdatamodel rdfs:label ?subdatamodelname
        }
      }
      
      """.trimIndent()


  private fun getPropertySql(iri: String): String {
    return """
      Select ?entityName ?property ?groupOrder ?group ?groupName ?order ?path ?pathName ?pathType
      ?inversePath ?inversePathName
      ?highCardinality
      ?class ?className ?classType ?classTypeName
      ?datatype ?datatypeName ?datatypeType ?datatypeTypeName
      ?pattern ?intervalUnit ?intervalUnitName
      ?datatypeQualifier ?qualifierOrder ?qualifierName ?qualifierPattern ?qualifierIntervalUnit ?qualifierIntervalUnitName
      ?node ?nodeName ?nodeType ?nodeTypeName
      ?rangeType ?rangeTypeName ?hasValue ?hasValueName
      ?minCount ?maxCount
      ?parameter ?parameterName ?parameterType ?parameterTypeName ?parameterSubtype ?parameterSubtypeName
      ?comment ?propertyDefinition ?units ?unitsName ?operator ?operatorName ?isRelativeValue
      ?orderable ?ascending ?descending ?definingProperty ?association
      WHERE {
         Values ?entity { <${iri}> }
        ?entity sh:property ?property.
        ?entity rdfs:label ?entityName.
        optional {
          ?property sh:group ?group.
          ?group rdfs:label ?groupName.
          optional {?group sh:order ?groupOrder}
        }
        optional {
          ?property im:highCardinality ?highCardinality.
        }
         optional {
          ?property sh:inversePath ?inversePath.
          ?inversePath rdfs:label ?inversePathName.
        }
        optional {?property sh:order ?order.}
        optional {
          ?property im:orderable ?orderable.
          ?orderable im:ascending ?ascending.
          ?orderable im:descending ?descending.
        }
        optional {
          ?property sh:path ?path.
          ?path rdf:type ?pathType.
          ?path rdfs:label ?pathName.
          BIND(EXISTS { ?path im:isA im:association} AS ?association)
          optional {?path im:definingProperty ?definingProperty.}
          optional {?path im:definition ?propertyDefinition}
          optional {
            ?path sh:parameter ?parameter.
            ?parameter rdfs:label ?parameterName.
            optional {
              ?parameter sh:class ?parameterType.
              ?parameterType rdfs:label ?parameterTypeName.
               optional { ?parameterSubtype im:isA ?parameterType.
                  ?parameterSubtype rdfs:label ?parameterSubtypeName
                  }
               }
            optional {?parameter sh:datatype ?parameterType.
              ?parameterType rdfs:label ?parameterTypeName.
              optional {
               ?parameterSubtype im:isA ?parameterType.
                ?parameterSubtype rdfs:label ?parameterSubtypeName
                }
              }
            optional {?parameter sh:node ?parameterType.
            ?parameterType rdfs:label ?parameterTypeName.
            optional {
            ?parameterSubtype im:isA ?parameterType.
              ?parameterSubtype rdfs:label ?parameterSubtypeName
              }
            }
          }
        }
        optional {
          ?property sh:minCount ?minCount.
        }
        optional {
          ?property rdfs:comment ?comment.
        }
        optional {
          ?property sh:maxCount ?maxCount.
        }
        optional {
          ?property sh:class ?class.
          ?class rdfs:label ?className.
          ?class rdf:type ?classType.
          ?classType rdfs:label ?classTypeName.
        }
        optional {
          ?property sh:datatype ?datatype.
          ?datatype rdfs:label ?datatypeName.
          ?datatype rdf:type ?datatypeType.
          ?datatypeType rdfs:label ?datatypeTypeName.
          optional {
            ?datatype im:intervalUnit ?intervalUnit.
            ?intervalUnit rdfs:label ?intervalUnitName
          }
          optional { ?datatype sh:pattern ?pattern}
          optional {
            ?datatype im:units ?units.
            ?units rdfs:label ?unitsName
          }
          optional {?datatype im:isRelativeValue ?isRelativeValue}
          optional {
            ?datatype im:operator ?operator.
            ?operator rdfs:label ?operatorName
          }
          optional {
            ?datatype im:datatypeQualifier ?datatypeQualifier.
            ?datatypeQualifier rdfs:label ?qualifierName.
            optional {?datatypeQualifier sh:order ?qualifierOrder}
            optional { ?datatypeQualifier sh:pattern ?qualifierPattern}
            optional {?datatypeQualifier im:isRelativeValue ?isRelativeValue}
            optional {
              ?datatypeQualifier im:units ?qualifierIntervalUnit.
              ?qualifierIntervalUnit rdfs:label ?qualifierIntervalUnitName
            }
          }
        }
        optional {
          ?property sh:hasValue ?hasValue.
          optional {?hasValue rdfs:label ?hasValueName}
        }
        optional {
          ?property sh:group ?group.
          ?group rdfs:label ?groupName.
          ?group sh:order ?groupOrder.
        }
        optional {
          ?property sh:node ?node.
          ?node rdfs:label ?nodeName.
          ?node rdf:type ?nodeType.
          ?nodeType rdfs:label ?nodeTypeName.
        }
      }
      order by ?groupOrder ?order ?qualifierOrder
      
      """.trimIndent()
  }


  private fun getPathSql(iri: String): String {
    return """
      Select ?entityName ?property ?order ?path ?pathName ?pathType
      ?node ?nodeName ?nodeType ?nodeTypeName
      WHERE {
      Values ?entity { <${iri}> }
        ?entity sh:property ?property.
        ?entity rdfs:label ?entityName.
        ?property sh:node ?node.
        ?node rdfs:label ?nodeName.
        ?node rdf:type ?nodeType.
        ?nodeType rdfs:label ?nodeTypeName.
        ?property sh:path ?path.
        ?path rdf:type ?pathType.
        ?path rdfs:label ?pathName.
        OPTIONAL {?property sh:order ?order.}
      }
      order by ?order
      
      """.trimIndent()
  }

  fun findUIPropertyForQB(dmIri: String, propIri: String): UIProperty {
    val uiProp = UIProperty()
    uiProp.setIri(propIri)

    var spql = """
      SELECT ?property ?name
        (IF(EXISTS {
          ?property sh:node ?valueA
        }, "node",
        IF(EXISTS {
          ?property sh:datatype ?valueB
        }, "datatype",
        IF(EXISTS {
          ?property sh:class ?valueC
        }, "class", "None"))) AS ?propertyType)
        ?valueType ?valueTypeName ?intervalUnitIri ?unitsIri ?operatorIri ?qualifierIri ?qualifierName
        WHERE {
          ?dmIri sh:property ?property .
          ?property sh:path ?propIri .
          ?propIri rdfs:label ?name .
          ?property (sh:class | sh:node | sh:datatype) ?valueType .
          ?valueType rdfs:label ?valueTypeName.
          OPTIONAL {
             ?valueType im:datatypeQualifier ?qualifierIri .
             ?qualifierIri rdfs:label ?qualifierName .
          }
          OPTIONAL{
             ?valueType im:intervalUnit ?intervalUnitIri .
          }
          OPTIONAL{
             ?valueType im:units ?unitsIri .
          }
          OPTIONAL {
             ?valueType im:operator ?operatorIri .
          }
        }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(spql)
      qry.setBinding("dmIri", Values.iri(dmIri))
      qry.setBinding("propIri", Values.iri(propIri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          uiProp.setName(bs.getValue("name").stringValue())
          uiProp.setPropertyType(bs.getValue("propertyType").stringValue())
          if (bs.getValue("valueType") != null) {
            uiProp.setValueType(bs.getValue("valueType").stringValue())
            uiProp.setValueTypeName(bs.getValue("valueTypeName").stringValue())
          }
          if (bs.getValue("intervalUnitIri") != null) uiProp.setIntervalUnitIri(
            bs.getValue("intervalUnitIri").stringValue()
          )
          if (bs.getValue("unitsIri") != null) uiProp.setUnitIri(bs.getValue("unitsIri").stringValue())
          if (bs.getValue("operatorIri") != null) uiProp.setOperatorIri(bs.getValue("operatorIri").stringValue())
          if (bs.getValue("qualifierIri") != null && bs.getValue("qualifierName") != null) uiProp.addQualifierOption(
            bs.getValue(
              "qualifierIri"
            ).stringValue(), bs.getValue("qualifierName").stringValue()
          )
        }
      }
    }
    if (uiProp.propertyType == "class") {
      spql = """
        Select (count(?member) as ?setMemberCount)
        where {
         ${SparqlHelper.valueList("dmIri", mutableListOf(dmIri))}
         ${SparqlHelper.valueList("propIri", mutableListOf(propIri))}
          ?dmIri sh:property ?property.
          ?property sh:path ?propIri.
          ?property sh:class ?valueSet.
          ?valueSet im:hasMember ?member.
        }
        
        """.trimIndent()
      IMDB.getConnection().use { conn ->
        val qry = conn.prepareTupleSparql(spql)
        qry.evaluate().use { rs ->
          while (rs.hasNext()) {
            val bs = rs.next()
            if (bs.getValue("setMemberCount") != null) uiProp.setSetMemberCount(
              bs.getValue("setMemberCount").stringValue().toInt()
            )
          }
        }
      }
    }
    return uiProp
  }

  fun getPathDatatype(iri: String): TTIriRef? {
    val sql = """
        select distinct ?dataType where {
          ?property sh:path ?path.
          ?property sh:datatype ?dataType.
        } limit 1
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.setBinding("path", Values.iri(iri))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          if (bs.getValue("dataType") != null) {
            return TTIriRef(bs.getValue("dataType").stringValue())
          }
        }
      }
    }
    return null
  }

  fun getDataModelPropertiesWithValueType(iris: MutableSet<String>, valueType: String): MutableList<NodeShape> {
    val results: MutableList<NodeShape> = mutableListOf()
    val iriMap: MutableMap<String, NodeShape> = mutableMapOf()
    val sql: String = """
      select ?nodeShape ?path ?pathLabel
      where {
      ${SparqlHelper.valueList("nodeShape", iris)}
      ${SparqlHelper.valueList("datatype", mutableListOf(valueType))}
       ?nodeShape sh:property ?property.
       ?property sh:path ?path.
       ?property sh:datatype ?datatype.
       ?path rdfs:label ?pathLabel.
       }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val nodeShapeIri = bs.getValue("nodeShape").stringValue()
          if (!iriMap.containsKey(nodeShapeIri)) {
            val shape = NodeShape()
            shape.setIri(nodeShapeIri)
            shape.setProperty(ArrayList<PropertyShape>())
            iriMap[nodeShapeIri] = shape
            results.add(shape)
          }
          val shape: NodeShape = iriMap[nodeShapeIri]!!
          val property = PropertyShape()
          shape.addProperty(property)
          property.setPath(TTIriRef.iri(bs.getValue("path").stringValue()))
          property.path.setName(bs.getValue("pathLabel").stringValue())
        }
      }
    }
    return results
  }

  fun getInversePath(source: String, target: String): TTIriRef? {
    val sql: String = """
      select ?inversePath ?pathLabel
      where {
       values ?source { <${source}> }
       values ?target { <${target}> }
       ?source sh:property ?property.
       ?property sh:inversePath ?inversePath.
       ?inversePath rdfs:label ?pathLabel.
       ?property sh:node ?target.
       }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          return TTIriRef.iri(bs.getValue("inversePath").stringValue()).setName(bs.getValue("pathLabel").stringValue())
        }
      }
    }
    return null
  }

  fun getPropertyValueSet(nodeShape: String, properties: MutableSet<String>): TTIriRef? {
    val sql: String = """
      select ?propertyValueSet ?propertyValueSetName
      where {
       values ?nodeShape { <${nodeShape}> }
       ${SparqlHelper.valueList("path", properties)}
       ?nodeShape sh:property ?property.
       ?property sh:path ?path.
       ?property sh:class ?propertyValueSet.
       ?propertyValueSet rdfs:label ?propertyValueSetName.
       }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          return TTIriRef.iri(bs.getValue("propertyValueSet").stringValue())
            .setName(bs.getValue("propertyValueSetName").stringValue())
        }
      }
    }
    return null
  }

  fun getSemanticMap(iri: String): SemanticMap {
    val sql: String = """
      select ?iri ?name ?entry ?entryName
      ?sourceEntity ?sourceEntityLabel
      ?sourceType ?sourceTypeLabel
      ?sourceEntityProperty ?sourceEntityPropertyLabel
      ?sourceValueProperty ?sourceValuePropertyLabel
      ?targetText ?targetValue ?rangeFrom ?rangeTo ?function ?functionName
      ?defaultText ?defaultValue
      where {
       values ?iri { <${iri}> }
       ?iri rdfs:label ?name.
       optional {?iri im:defaultText ?defaultText}
       optional {?iri im:defaultValue ?defaultValue}
         optional { ?iri im:sourceType ?sourceType.
          ?sourceType rdfs:label ?sourceTypeLabel. }
       optional { ?iri im:sourceEntityProperty ?sourceEntityProperty.
         ?sourceEntityProperty rdfs:label ?sourceEntityPropertyLabel.}
         optional { ?iri im:sourceValueProperty ?sourceValueProperty.
          ?sourceValueProperty rdfs:label ?sourceValuePropertyLabel.}
         optional { ?iri im:function ?function.
            ?function rdfs:label ?functionName.}
       optional {?iri im:hasEntry ?entry.
         ?entry rdfs:label ?entryName.
         ?entry im:sourceEntity ?sourceEntity.
         ?sourceEntity rdfs:label ?sourceEntityLabel.
         optional { ?entry im:rangeFrom ?rangeFrom. }
         optional { ?entry im:rangeTo ?rangeTo. }
         optional {?entry sh:order ?order.}
         optional { ?entry im:targetText ?targetText. }
         optional { ?entry im:targetValue ?targetValue. }
       }
      }
      
      """.trimIndent()
    val map = SemanticMap()
    val entryMap: MutableMap<String, SemanticMapEntry> = mutableMapOf()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          map.setIri(bs.getValue("iri").stringValue())
          map.setName(bs.getValue("name").stringValue())
          if (bs.getValue("entry") != null) {
            var entry = entryMap[bs.getValue("entry").stringValue()]
            if (entry == null) {
              entry = SemanticMapEntry()
              entry.setIri(bs.getValue("entry").stringValue())
              entry.setName(bs.getValue("entryName").stringValue())
              entryMap[bs.getValue("entry").stringValue()] = entry
              map.addEntry(entry)
            }
            if (bs.getValue("sourceEntity") != null) {
              entry.setSourceEntity(
                TTIriRef.iri(bs.getValue("sourceEntity").stringValue())
                  .setName(bs.getValue("sourceEntityLabel").stringValue())
              )
            }
            if (bs.getValue("sourceEntityProperty") != null) {
              map.setSourceEntityProperty(
                TTIriRef.iri(bs.getValue("sourceEntityProperty").stringValue())
                  .setName(bs.getValue("sourceEntityPropertyLabel").stringValue())
              )
            }
            if (bs.getValue("sourceValueProperty") != null) {
              map.setSourceValueProperty(
                TTIriRef.iri(bs.getValue("sourceValueProperty").stringValue())
                  .setName(bs.getValue("sourceValuePropertyLabel").stringValue())
              )
            }
            if (bs.getValue("targetText") != null) {
              entry.setTargetText(bs.getValue("targetText").stringValue())
            }
            if (bs.getValue("targetValue") != null) {
              entry.setTargetValue(bs.getValue("targetValue").stringValue().toDouble())
            }
            if (bs.getValue("rangeFrom") != null) {
              entry.setRangeFrom(bs.getValue("rangeFrom").stringValue().toDouble())
            }
            if (bs.getValue("rangeTo") != null) {
              entry.setRangeTo(bs.getValue("rangeTo").stringValue().toDouble())
            }
            if (bs.getValue("order") != null) {
              entry.setOrder(bs.getValue("order").stringValue().toInt())
            }
            if (bs.getValue("function") != null) {
              map.setFunction(
                TTIriRef.iri(bs.getValue("function").stringValue()).setName(bs.getValue("functionName").stringValue())
              )
            }
          }
          if (bs.getValue("defaultText") != null) {
            map.setDefaultText(bs.getValue("defaultText").stringValue())
          }
          if (bs.getValue("defaultValue") != null) {
            map.setDefaultValue(bs.getValue("defaultValue").stringValue().toDouble())
          }

          if (bs.getValue("sourceType") != null) {
            map.setSourceType(
              TTIriRef.iri(bs.getValue("sourceType").stringValue())
                .setName(bs.getValue("sourceTypeLabel").stringValue())
            )
          }
        }
      }
    }
    return map
  }

  companion object {
    private fun getPropertyOrderable(bs: BindingSet, property: PropertyShape) {
      property.setOrderable(true)
      property.setAscending(bs.getValue("ascending").stringValue())
      property.setDescending(bs.getValue("descending").stringValue())
    }

    private fun getPropertyValue(bs: BindingSet, property: PropertyShape) {
      val hasValue = bs.getValue("hasValue")
      if (hasValue.isIRI) {
        property.setHasValue(TTIriRef.iri(hasValue.stringValue()).setName(bs.getValue("hasValueName").stringValue()))
        property.setHasValueType(TTIriRef.iri(RDFS.RESOURCE))
      } else {
        property.setHasValue(hasValue.stringValue())
        property.setHasValueType(TTIriRef.iri(XSD.STRING))
      }
    }
  }
}
