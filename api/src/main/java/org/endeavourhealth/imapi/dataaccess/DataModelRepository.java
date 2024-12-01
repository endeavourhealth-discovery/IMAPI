package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.iml.PropertyRange;
import org.endeavourhealth.imapi.model.iml.NodeShape;
import org.endeavourhealth.imapi.model.iml.ParameterShape;
import org.endeavourhealth.imapi.model.iml.PropertyShape;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.addSparqlPrefixes;

public class DataModelRepository {
  public List<TTIriRef> getProperties() {
    List<TTIriRef> result = new ArrayList<>();

    String spql = """
      SELECT ?s ?name
      WHERE {
        ?s rdf:type rdf:PropertyRef ;
        rdfs:label ?name .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, spql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()));
        }
      }
    }

    return result;
  }


  public List<TTIriRef> findDataModelsFromProperty(String propIri) {
    List<TTIriRef> dmList = new ArrayList<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sparql = """
        SELECT ?dm ?dmName
        WHERE {
          ?dm sh:property ?prop .
          ?dm rdfs:label ?dmName .
          ?prop sh:path ?propIri .
        }
        """;
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sparql));
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

  public String checkPropertyType(String propIri) {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String query = """
        SELECT ?objectProperty ?dataProperty
        WHERE {"
          bind(exists{?propIri ?isA ?objProp} as ?objectProperty)
          bind(exists{?propIri ?isA ?dataProp} as ?dataProperty)
        }
        """;
      TupleQuery qry = conn.prepareTupleQuery(query);
      qry.setBinding("propIri", iri(propIri));
      qry.setBinding("isA", iri(IM.IS_A));
      qry.setBinding("objProp", iri(IM.DATAMODEL_OBJECTPROPERTY));
      qry.setBinding("dataProp", iri(IM.DATAMODEL_DATAPROPERTY));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          if (bs.hasBinding("objectProperty")) return IM.DATAMODEL_OBJECTPROPERTY;
          else if (bs.hasBinding("dataProperty")) return IM.DATAMODEL_DATAPROPERTY;
        }
      }
    }
    return null;
  }

  public void addDataModelSubtypes(NodeShape dataModel){
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sql = getSubtypeSql();
      TupleQuery qry = conn.prepareTupleQuery(sql);
      qry.setBinding("entity", iri(dataModel.getIri()));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          if (bs.getValue("subdatamodel") != null) {
            dataModel.addSubType(TTIriRef.iri(bs.getValue("subdatamodel").stringValue())
              .setName(bs.getValue("subdatamodelname").stringValue()));
          }
        }
      }
    }
  }

  public NodeShape getDataModelDisplayProperties(String iri) {
    NodeShape nodeShape = new NodeShape();
    nodeShape.setIri(iri);
    addDataModelSubtypes(nodeShape);
    Map<String, PropertyShape> groups= new HashMap<>();
    Map<String,PropertyShape> properties= new HashMap<>();
    Map<String, PropertyRange> dataTypes =new HashMap<>();
    Map<String, PropertyRange> qualifiers= new HashMap<>();
    List<PropertyShape> propertyList= new ArrayList<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
        String sql = getPropertysql();
        TupleQuery qry = conn.prepareTupleQuery(sql);
        qry.setBinding("entity", iri(iri));
        try (TupleQueryResult rs = qry.evaluate()) {
          while (rs.hasNext()) {
            BindingSet bs = rs.next();
            PropertyShape group = null;
            int offset = 0;
            if (bs.getValue("path") != null) {
              if (bs.getValue("group") != null) {
                String groupIri = bs.getValue("group").stringValue();
                if (groups.get(groupIri) == null) {
                  group = new PropertyShape();
                  groups.put(groupIri, group);
                  propertyList.add(group);
                  group.setGroup(TTIriRef.iri(bs.getValue("group").stringValue())
                    .setName(bs.getValue("groupName").stringValue()));
                  group.setOrder(Integer.parseInt(bs.getValue("groupOrder").stringValue()));
                }
                group = groups.get(groupIri);
                offset = group.getOrder();
              }
              String propertyIri = bs.getValue("property").stringValue();
              if (properties.get(propertyIri) == null) {
                PropertyShape property = new PropertyShape();
                properties.put(propertyIri, property);
                if (group != null) {
                  group.addProperty(property);
                } else
                  propertyList.add(property);
              }
              PropertyShape property = properties.get(propertyIri);
              property.setPath(TTIriRef.iri(bs.getValue("path").stringValue())
                .setName(bs.getValue("pathName").stringValue()));
              property.addType(TTIriRef.iri(bs.getValue("pathType").stringValue()));
              if (bs.getValue("class") != null) {
                property.setClazz(new PropertyRange().setIri(bs.getValue("class").stringValue())
                  .setName(bs.getValue("className").stringValue())
                  .setType(TTIriRef.iri(bs.getValue("classType").stringValue())
                    .setName(bs.getValue("classTypeName").stringValue())));
              } else if (bs.getValue("datatype") != null) {
                PropertyRange datatype= dataTypes.get(propertyIri);
                if (datatype==null){
                  datatype= new PropertyRange();
                  datatype.setIri(bs.getValue("datatype").stringValue())
                    .setName(bs.getValue("datatypeName").stringValue())
                    .setType(TTIriRef.iri(bs.getValue("datatypeType").stringValue())
                      .setName(bs.getValue("datatypeTypeName").stringValue()));
                  dataTypes.put(propertyIri,datatype);
                  property.setDatatype(datatype);
                  if (bs.getValue("pattern")!=null){
                    datatype.setPattern(bs.getValue("pattern").stringValue());
                  }
                  if (bs.getValue("intervalUnit")!=null){
                    datatype.setPattern(bs.getValue("intervalUnit").stringValue());
                  }
                }
                if (bs.getValue("datatypeQualifier")!=null){
                  PropertyRange qualifier= new PropertyRange();
                  datatype.addQualifier(qualifier);
                  qualifier
                    .setIri(bs.getValue("datatypeQualifier").stringValue())
                    .setName(bs.getValue("qualifierName").stringValue());
                  if (bs.getValue("qualifierPattern")!=null){
                    qualifier.setPattern(bs.getValue("qualifierPattern").stringValue());
                  }
                  if (bs.getValue("qualifierIntervalUnit")!=null){
                    qualifier.setIntervalUnit(TTIriRef.iri(bs.getValue("qualifierIntervalUnit").stringValue()));
                  }

                }
              } else if (bs.getValue("node") != null) {
                property.setNode(new PropertyRange().setIri(bs.getValue("node").stringValue())
                  .setName(bs.getValue("nodeName").stringValue())
                  .setType(TTIriRef.iri(bs.getValue("nodeType").stringValue())
                    .setName(bs.getValue("nodeTypeName").stringValue())));
              }
              if (bs.getValue("order") != null) {
                property.setOrder(offset + Integer.parseInt(bs.getValue("order").stringValue()));
              }
              if (bs.getValue("minCount") != null) {
                property.setMinCount(Integer.parseInt(bs.getValue("minCount").stringValue()));
              }
              if (bs.getValue("minCount") != null) {
                property.setMaxCount(Integer.parseInt(bs.getValue("minCount").stringValue()));
              }
              if (bs.getValue("comment") != null) {
                property.setComment(bs.getValue("comment").stringValue());
              }

              if (bs.getValue("hasValue") != null) {
                Value hasValue = bs.getValue("hasValue");
                if (hasValue.isIRI()) {
                  property.setHasValue(TTIriRef.iri(hasValue.stringValue())
                    .setName(bs.getValue("hasValueName").stringValue()));
                  property.setHasValueType(TTIriRef.iri(RDFS.RESOURCE));
                } else {
                  property.setHasValue(hasValue.stringValue());
                  property.setHasValueType(TTIriRef.iri(XSD.STRING));
                }

              }
              if (bs.getValue("parameter")!=null){
                ParameterShape parameter=getParameter(property,bs.getValue("parameterName").stringValue());
                if (parameter==null) {
                  parameter = new ParameterShape();
                  property.addParameter(parameter);
                }
                parameter.setLabel(bs.getValue("parameterName").stringValue());
                parameter.setType(TTIriRef.iri(bs.getValue("parameterType").stringValue())
                  .setName(bs.getValue("parameterTypeName").stringValue()));
                if (bs.getValue("parameterSubtype")!=null) {
                  if (parameter.getParameterSubType() == null) {
                    parameter.addParameterSubType(TTIriRef.iri(bs.getValue("parameterSubtype").stringValue())
                      .setName(bs.getValue("parameterSubtypeName").stringValue()));
                  }
                  else if (!parameter.getParameterSubType().contains(TTIriRef.iri(bs.getValue("parameterSubtype").stringValue()))) {
                    parameter.addParameterSubType(TTIriRef.iri(bs.getValue("parameterSubtype").stringValue())
                      .setName(bs.getValue("parameterSubtypeName").stringValue()));
                  }
                }
              }
              if (bs.getValue("propertyDefinition") != null) {
                property.setDefinition(bs.getValue("propertyDefinition").stringValue());
              }
            }
          }
          if (!propertyList.isEmpty()) {
            propertyList.sort(Comparator.comparingInt(PropertyShape::getOrder));
            nodeShape.setProperty(propertyList);
          }

        }
      }
    return nodeShape;
  }

  private ParameterShape getParameter(PropertyShape property, String parameterName){
    if (property.getParameter()==null)
      return null;
    for (ParameterShape param: property.getParameter()) {
      if (param.getLabel().equals(parameterName)) {
        return param;
      }
    }
      return null;
  }



  private String getSubtypeSql() {
    return """
      PREFIX im: <http://endhealth.info/im#>
      PREFIX sh: <http://www.w3.org/ns/shacl#>
      PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
      Select ?subdatamodel ?subdatamodelname
      {
      optional  {
          ?subdatamodel rdfs:subClassOf ?entity.
          ?subdatamodel rdfs:label ?subdatamodelname
         }
       }
       """;
  }



  private String getPropertysql(){
    return """
        PREFIX im: <http://endhealth.info/im#>
        PREFIX sh: <http://www.w3.org/ns/shacl#>
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
        Select ?property ?groupOrder ?group ?groupName ?order ?path ?pathName ?pathType
        ?class ?className ?classType ?classTypeName
        ?datatype ?datatypeName ?datatypeType ?datatypeTypeName
        ?pattern ?intervalUnit
        ?datatypeQualifier ?qualifierOrder ?qualifierName ?qualifierPattern ?qualifierIntervalUnit
        ?node ?nodeName ?nodeType ?nodeTypeName
        ?rangeType ?rangeTypeName ?hasValue ?hasValueName
        ?minCount ?maxCount
        ?parameter ?parameterName ?parameterType ?parameterTypeName ?parameterSubtype ?parameterSubtypeName
        ?comment ?propertyDefinition
        {
           ?entity sh:property ?property.
           optional {?property sh:group ?group.
                     ?group rdfs:label ?groupName.
                     optional {?group sh:order ?groupOrder}
                     }
            optional {?property sh:order ?order.}
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
                   optional { ?parameterSubtype im:isA ?parameterType.
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
                    optional { ?datatype im:intervalUnit ?intervalUnit.}
                    optional { ?datatype sh:pattern ?pattern}
                    optional {?datatype im:datatypeQualifier ?datatypeQualifier.
                    ?datatypeQualifier rdfs:label ?qualifierName.
                    optional {?datatypeQualifier sh:order ?qualifierOrder}
                    optional { ?datatypeQualifier sh:pattern ?qualifierPattern}
                    optional { ?datatypeQualifier im:intervalUnit ?qualifierIntervalUnit}
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




}
