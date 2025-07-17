package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.model.dto.UIProperty;
import org.endeavourhealth.imapi.model.iml.NodeShape;
import org.endeavourhealth.imapi.model.iml.ParameterShape;
import org.endeavourhealth.imapi.model.iml.PropertyRange;
import org.endeavourhealth.imapi.model.iml.PropertyShape;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.addSparqlPrefixes;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.valueList;

public class DataModelRepository {
  public List<TTIriRef> getProperties(Graph graph) {
    List<TTIriRef> result = new ArrayList<>();

    String spql = """
      SELECT ?s ?name
      WHERE {
        ?s rdf:type rdf:PropertyRef ;
        rdfs:label ?name .
      }
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(spql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()));
        }
      }
    }

    return result;
  }


  public List<TTIriRef> findDataModelsFromProperty(String propIri, Graph graph) {
    List<TTIriRef> dmList = new ArrayList<>();
    try (IMDB conn = IMDB.getConnection(graph)) {
      String sparql = """
        SELECT ?dm ?dmName
        WHERE {
          ?dm sh:property ?prop .
          ?dm rdfs:label ?dmName .
          ?prop sh:path ?propIri .
        }
        """;
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("propIri", iri(propIri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          dmList.add(new TTIriRef(bs.getValue("dm").stringValue(), bs.getValue("dmName").stringValue()));
        }
      }
    }
    return dmList;

  }

  public String checkPropertyType(String propIri, Graph graph) {
    try (IMDB conn = IMDB.getConnection(graph)) {
      String query = """
        SELECT ?objectProperty ?dataProperty
        WHERE {
          bind(exists{?propIr i ?isA ?objProp} as ?objectProperty)
          bind(exists{?propIri ?isA ?dataProp} as ?dataProperty)
        }
        """;
      TupleQuery qry = conn.prepareTupleSparql(query);
      qry.setBinding("propIri", iri(propIri));
      qry.setBinding("isA", IM.IS_A.asDbIri());
      qry.setBinding("objProp", IM.DATAMODEL_OBJECTPROPERTY.asDbIri());
      qry.setBinding("dataProp", IM.DATAMODEL_DATAPROPERTY.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          if (bs.hasBinding("objectProperty")) return IM.DATAMODEL_OBJECTPROPERTY.toString();
          else if (bs.hasBinding("dataProperty")) return IM.DATAMODEL_DATAPROPERTY.toString();
        }
      }
    }
    return null;
  }

  public void addDataModelSubtypes(NodeShape dataModel, Graph graph) {
    try (IMDB conn = IMDB.getConnection(graph)) {
      String sql = getSubtypeSql();
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("entity", iri(dataModel.getIri()));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          if (bs.getValue("subdatamodel") != null) {
            dataModel.addSubType(TTIriRef.iri(bs.getValue("subdatamodel").stringValue()).setName(bs.getValue("subdatamodelname").stringValue()));
          }
        }
      }
    }
  }


  public NodeShape getDataModelDisplayProperties(String iri, boolean pathsOnly, Graph graph) {
    NodeShape nodeShape = new NodeShape();
    nodeShape.setIri(iri);
    addDataModelSubtypes(nodeShape, graph);
    try (IMDB conn = IMDB.getConnection(graph)) {
      String sql = pathsOnly ? getPathSql() : getPropertySql();
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("entity", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          nodeShape.setName(bs.getValue("entityName").stringValue());
          PropertyShape group = null;
          if (bs.getValue("path") != null) {
            if (bs.getValue("group") != null) {
              group = getGroupFromNode(bs, nodeShape);

            }
            addProperty(nodeShape, group, bs);
          }
        }
      }
    }
    return nodeShape;
  }

  private PropertyShape getPropertyFromNode(NodeShape node, String iri) {
    if (node.getProperty() != null) {
      Optional<PropertyShape> found = node.getProperty().stream().filter(f -> f.getPath().getIri().equals(iri)).findFirst();
      if (found.isPresent()) return found.get();
    }
    PropertyShape group = new PropertyShape();
    node.addProperty(group);
    group.setPath(TTIriRef.iri(iri));
    return group;
  }

  private PropertyShape getGroupFromNode(BindingSet bs, NodeShape nodeShape) {
    String groupIri = bs.getValue("group").stringValue();
    PropertyShape group = getPropertyFromNode(nodeShape, groupIri);
    group.setGroup(TTIriRef.iri(groupIri).setName(bs.getValue("groupName").stringValue()));
    group.setOrder(Integer.parseInt(bs.getValue("groupOrder").stringValue()));
    return group;
  }

  private PropertyShape getPropertyFromGroup(PropertyShape group, String iri) {
    if (group.getProperty() != null) {
      Optional<PropertyShape> found = group.getProperty().stream().filter(f -> f.getPath().getIri().equals(iri)).findFirst();
      if (found.isPresent()) return found.get();
    }
    PropertyShape property = new PropertyShape();
    property.setPath(TTIriRef.iri(iri));
    group.addProperty(property);
    return property;

  }

  private void addProperty(NodeShape node, PropertyShape group, BindingSet bs) {
    String propertyIri = bs.getValue("path").stringValue();
    PropertyShape property;

    if (group != null) property = getPropertyFromGroup(group, propertyIri);
    else property = getPropertyFromNode(node, propertyIri);

    property.getPath().setName(bs.getValue("pathName").stringValue());

    if (bs.getValue("class") != null) {
      property.setClazz(new PropertyRange().setIri(bs.getValue("class").stringValue()).setName(bs.getValue("className").stringValue()).setType(TTIriRef.iri(bs.getValue("classType").stringValue()).setName(bs.getValue("classTypeName").stringValue())));
    }

    if (bs.getValue("datatype") != null) {
      getPropertyDataType(bs, property);
    } else if (bs.getValue("node") != null) {
      property.setNode(new PropertyRange().setIri(bs.getValue("node").stringValue()).setName(bs.getValue("nodeName").stringValue()).setType(TTIriRef.iri(bs.getValue("nodeType").stringValue()).setName(bs.getValue("nodeTypeName").stringValue())));
    }

    if (bs.getValue("order") != null) {
      property.setOrder(Integer.parseInt(bs.getValue("order").stringValue()));
    }
    if (bs.getValue("minCount") != null) {
      property.setMinCount(Integer.parseInt(bs.getValue("minCount").stringValue()));
    }
    if (bs.getValue("minCount") != null) {
      property.setMaxCount(Integer.parseInt(bs.getValue("minCount").stringValue()));
    }
    if (bs.getValue("orderable") != null) {
      getPropertyOrderable(bs, property);
    }
    if (bs.getValue("comment") != null) {
      property.setComment(bs.getValue("comment").stringValue());
    }

    if (bs.getValue("hasValue") != null) {
      getPropertyValue(bs, property);
    }
    if (bs.getValue("parameter") != null) {
      addParameter(property, bs);
    }
    if (bs.getValue("propertyDefinition") != null) {
      property.setDefinition(bs.getValue("propertyDefinition").stringValue());
    }
  }

  private void getPropertyDataType(BindingSet bs, PropertyShape property) {
    PropertyRange datatype = property.getDatatype();
    if (datatype == null) {
      datatype = new PropertyRange();
      datatype.setIri(bs.getValue("datatype").stringValue()).setName(bs.getValue("datatypeName").stringValue()).setType(TTIriRef.iri(bs.getValue("datatypeType").stringValue()).setName(bs.getValue("datatypeTypeName").stringValue()));
      property.setDatatype(datatype);
      if (bs.getValue("pattern") != null) {
        datatype.setPattern(bs.getValue("pattern").stringValue());
      }
      if (bs.getValue("isRelativeValue") != null) {
        datatype.setRelativeValue("true".equalsIgnoreCase(bs.getValue("isRelativeValue").stringValue()));
      }
      if (bs.getValue("units") != null) {
        datatype.setUnits(TTIriRef.iri(bs.getValue("units").stringValue()).setName(bs.getValue("unitsName").stringValue()));
      }
      if (bs.getValue("operator") != null) {
        datatype.setOperator(TTIriRef.iri(bs.getValue("operator").stringValue()).setName(bs.getValue("operatorName").stringValue()));
      }
    }
    if (bs.getValue("datatypeQualifier") != null) {
      addDataTypeQualifier(datatype, bs);
    }
  }

  private static void getPropertyOrderable(BindingSet bs, PropertyShape property) {
    property.setOrderable(true);
    property.setAscending(bs.getValue("ascending").stringValue());
    property.setDescending(bs.getValue("descending").stringValue());
  }

  private static void getPropertyValue(BindingSet bs, PropertyShape property) {
    Value hasValue = bs.getValue("hasValue");
    if (hasValue.isIRI()) {
      property.setHasValue(TTIriRef.iri(hasValue.stringValue()).setName(bs.getValue("hasValueName").stringValue()));
      property.setHasValueType(TTIriRef.iri(RDFS.RESOURCE));
    } else {
      property.setHasValue(hasValue.stringValue());
      property.setHasValueType(TTIriRef.iri(XSD.STRING));
    }
  }

  private void addParameter(PropertyShape property, BindingSet bs) {
    ParameterShape parameter = getParameterFromProperty(property, bs.getValue("parameterName").stringValue());
    parameter.setLabel(bs.getValue("parameterName").stringValue());
    parameter.setType(TTIriRef.iri(bs.getValue("parameterType").stringValue()).setName(bs.getValue("parameterTypeName").stringValue()));
    if (bs.getValue("parameterSubtype") != null) {
      if (parameter.getParameterSubType() == null) {
        parameter.addParameterSubType(TTIriRef.iri(bs.getValue("parameterSubtype").stringValue()).setName(bs.getValue("parameterSubtypeName").stringValue()));
      } else if (!parameter.getParameterSubType().contains(TTIriRef.iri(bs.getValue("parameterSubtype").stringValue()))) {
        parameter.addParameterSubType(TTIriRef.iri(bs.getValue("parameterSubtype").stringValue()).setName(bs.getValue("parameterSubtypeName").stringValue()));
      }
    }

  }

  private void addDataTypeQualifier(PropertyRange datatype, BindingSet bs) {
    String qualifierIri = bs.getValue("datatypeQualifier").stringValue();
    PropertyRange qualifier = getQualifierFromDataType(datatype, qualifierIri);
    qualifier.setIri(bs.getValue("datatypeQualifier").stringValue()).setName(bs.getValue("qualifierName").stringValue());
    if (bs.getValue("qualifierPattern") != null) {
      qualifier.setPattern(bs.getValue("qualifierPattern").stringValue());
    }
    if (bs.getValue("qualifierIntervalUnit") != null) {
      qualifier.setIntervalUnit(TTIriRef.iri(bs.getValue("qualifierIntervalUnit").stringValue()));
    }
  }

  private PropertyRange getQualifierFromDataType(PropertyRange datatype, String iri) {
    if (datatype.getQualifier() != null) {
      Optional<PropertyRange> found = datatype.getQualifier().stream().filter(f -> f.getIri().equals(iri)).findFirst();
      if (found.isPresent()) return found.get();
    }
    PropertyRange qualifier = new PropertyRange();
    qualifier.setIri(iri);
    datatype.addQualifier(qualifier);
    return qualifier;
  }


  private ParameterShape getParameterFromProperty(PropertyShape property, String parameterName) {
    if (property.getParameter() != null) {
      for (ParameterShape param : property.getParameter()) {
        if (param.getLabel().equals(parameterName)) {
          return param;
        }
      }
    }
    ParameterShape param = new ParameterShape();
    property.addParameter(param);
    return param;
  }


  private String getSubtypeSql() {
    return """
      Select ?subdatamodel ?subdatamodelname
      WHERE {
        optional  {
          ?subdatamodel rdfs:subClassOf ?entity.
          ?subdatamodel rdfs:label ?subdatamodelname
        }
      }
      """;
  }


  private String getPropertySql() {
    return """
      Select ?entityName ?property ?groupOrder ?group ?groupName ?order ?path ?pathName ?pathType
      ?class ?className ?classType ?classTypeName
      ?datatype ?datatypeName ?datatypeType ?datatypeTypeName
      ?pattern ?intervalUnit ?intervalUnitName
      ?datatypeQualifier ?qualifierOrder ?qualifierName ?qualifierPattern ?qualifierIntervalUnit ?qualifierIntervalUnitName
      ?node ?nodeName ?nodeType ?nodeTypeName
      ?rangeType ?rangeTypeName ?hasValue ?hasValueName
      ?minCount ?maxCount
      ?parameter ?parameterName ?parameterType ?parameterTypeName ?parameterSubtype ?parameterSubtypeName
      ?comment ?propertyDefinition ?units ?unitsName ?operator ?operatorName ?isRelativeValue
      ?orderable ?ascending ?descending
      WHERE {
        ?entity sh:property ?property.
        ?entity rdfs:label ?entityName.
        optional {
          ?property sh:group ?group.
          ?group rdfs:label ?groupName.
          optional {?group sh:order ?groupOrder}
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
          optional {?path im:definition ?propertyDefinition}
          optional {
            ?path im:parameter ?parameter.
            ?parameter rdfs:label ?parameterName.
            ?parameter sh:class ?parameterType.
            ?parameterType rdfs:label ?parameterTypeName.
            optional {
              ?parameterSubtype im:isA ?parameterType.
              ?parameterSubtype rdfs:label ?parameterSubtypeName
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
      """;
  }


  private String getPathSql() {
    return """
      Select ?entityName ?property ?order ?path ?pathName ?pathType
      ?node ?nodeName ?nodeType ?nodeTypeName
      WHERE {
        ?enti  ty sh:property ?property.
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
      """;
  }

  public UIProperty findUIPropertyForQB(String dmIri, String propIri, Graph graph) {
    UIProperty uiProp = new UIProperty();
    uiProp.setIri(propIri);

    String spql = """
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
        ?valueType ?intervalUnitIri ?unitsIri ?operatorIri ?qualifierIri ?qualifierName
        WHERE {
          ?dmIri sh:property ?property .
          ?property sh:path ?propIri .
          ?propIri rdfs:label ?name .
          ?property (sh:class | sh:node | sh:datatype) ?valueType .
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
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(spql);
      qry.setBinding("dmIri", iri(dmIri));
      qry.setBinding("propIri", iri(propIri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          uiProp.setName(bs.getValue("name").stringValue());
          uiProp.setPropertyType(bs.getValue("propertyType").stringValue());
          uiProp.setValueType(bs.getValue("valueType").stringValue());
          if (bs.getValue("intervalUnitIri") != null)
            uiProp.setIntervalUnitIri(bs.getValue("intervalUnitIri").stringValue());
          if (bs.getValue("unitsIri") != null) uiProp.setUnitIri(bs.getValue("unitsIri").stringValue());
          if (bs.getValue("operatorIri") != null) uiProp.setOperatorIri(bs.getValue("operatorIri").stringValue());
          if (bs.getValue("qualifierIri") != null && bs.getValue("qualifierName") != null)
            uiProp.addQualifierOption(bs.getValue("qualifierIri").stringValue(), bs.getValue("qualifierName").stringValue());
        }
      }
    }

    return uiProp;
  }

  public PropertyShape getDefiningProperty(String iri) {
    String sql = """
      select ?path ?valueSet
      where {
       %s
       ?iri sh:property ?property.
       ?property sh:path ?path.
       ?path im:isA im:definingProperty.
       ?property sh:class ?valueSet.
       }
      """.formatted(valueList("iri", Set.of(iri)));
    PropertyShape property = new PropertyShape();
    try (IMDB conn = IMDB.getConnection(Graph.IM)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          property.setPath(TTIriRef.iri(bs.getValue("path").stringValue()));
          property.setClazz(new PropertyRange().setIri(bs.getValue("valueSet").stringValue()));
        }
      }
      return property;
    }
  }
}
