package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public TTEntity getDataModelProperties(String iri){
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sql = """
        PREFIX im: <http://endhealth.info/im#>
        PREFIX sh: <http://www.w3.org/ns/shacl#>
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
        Select ?subdatamodel ?subdatamodelname ?property ?or ?order ?path ?pathname ?class ?classname ?datatype ?datatypename ?node ?nodename ?subtype ?suborder ?subtypename
        {
        optional  { 
            ?entity im:isAbstract true.
             ?subdatamodel rdfs:subClassOf ?entity.
             ?subdatamodel rdfs:label ?subdatamodelname
                    }
        optional {          
           ?entity sh:property ?property.
           filter not exists {?entity im:isAbstract true}
            optional {?property sh:order ?order.}
            optional {
                ?property sh:path ?path.
                ?path rdfs:label ?pathname.
                optional {
                   ?property sh:class ?class.
                    ?class rdfs:label ?classname.
                        }
                optional {
                    ?property sh:datatype ?datatype.
                    ?datatype rdfs:label ?datatypename
                        }
                optional {
                   ?property sh:node ?node.
                   ?node rdfs:label ?nodename.
                    optional {
                           ?node im:isAbstract true.
                              ?subtype rdfs:subClassOf ?node.
                              ?subtype rdfs:label ?subtypename.
                              optional {
                                 ?subtype sh:order ?suborder
                                       }
                              }
                          }
                      }
            optional {
                ?property sh:or ?or.
                ?or sh:path ?path.
                ?path rdfs:label ?pathname.
                optional {
                   ?or sh:class ?class.
                   ?class rdfs:label ?classname.
                         }
                optional {
                       ?or sh:datatype ?datatype.
                        ?datatype rdfs:label ?datatypename.
                        }
                optional {
                    ?or sh:node ?node.
                    ?node rdfs:label ?nodename.
                    optional {
                           ?node im:isAbstract true.
                            ?subtype rdfs:subClassOf ?node.
                            ?subtype rdfs:label ?subtypename.
                            optional {
                                 ?subtype sh:order ?suborder}
                                     }
                              }
                        }
                }
        }
        order by ?order ?property ?or ?suborder
        """;
      TupleQuery qry = conn.prepareTupleQuery(sql);
      qry.setBinding("entity",iri(iri));
      TTEntity entity= new TTEntity();
      Map<String, TTNode> ors= new HashMap<>();
      Map<String,TTNode> properties= new HashMap<>();
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          if (bs.getValue("subdatamodel") != null) {
            entity.addObject(TTIriRef.iri(IM.HAS_SUBSET),TTIriRef.iri(bs.getValue("subdatamodel").stringValue())
              .setName(bs.getValue("subdatamodelname").stringValue()));

          }
          else if (bs.getValue("property")!=null){
            if (properties.get(bs.getValue("property").stringValue()) == null) {
              properties.put(bs.getValue("property").stringValue(), new TTNode());
              entity.addObject(TTIriRef.iri(SHACL.PROPERTY), properties.get(bs.getValue("property").stringValue()));
            }
            TTNode property = properties.get(bs.getValue("property").stringValue());
            if (bs.getValue("order") != null) {
              property.set(TTIriRef.iri(SHACL.ORDER), TTLiteral.literal(Integer.parseInt(bs.getValue("order").stringValue())));
            }
            if (bs.getValue("or") != null) {
              if (ors.get(bs.getValue("or").stringValue()) == null) {
                ors.put(bs.getValue("or").stringValue(), new TTNode());
                property.addObject(TTIriRef.iri(SHACL.OR), ors.get(bs.getValue("or").stringValue()));
              }
              addPropertyPredicates(bs, ors.get(bs.getValue("or").stringValue()));
            } else addPropertyPredicates(bs, property);
          }
        }
      }
      return entity;
    }
  }

  private void addPropertyPredicates(BindingSet bs, TTNode property) {
    if (bs.getValue("path")!=null) {
      property.set(TTIriRef.iri(SHACL.PATH), TTIriRef.iri(bs.getValue("path").stringValue())
        .setName(bs.getValue("pathname").stringValue()));
    }
    if (bs.getValue("class")!=null){
      property.set(TTIriRef.iri(SHACL.CLASS),TTIriRef.iri(bs.getValue("class").stringValue())
        .setName(bs.getValue("classname").stringValue()));
    }
    else if (bs.getValue("datatype")!=null){
      property.set(TTIriRef.iri(SHACL.DATATYPE),TTIriRef.iri(bs.getValue("datatype").stringValue())
        .setName(bs.getValue("datatypename").stringValue()));
    }
    else if (bs.getValue("subtype")!=null){
      property.addObject(TTIriRef.iri(SHACL.NODE),TTIriRef.iri(bs.getValue("subtype").stringValue())
        .setName(bs.getValue("subtypename").stringValue()));
    }
    else if (bs.getValue("node")!=null){{
      property.addObject(TTIriRef.iri(SHACL.NODE),TTIriRef.iri(bs.getValue("node").stringValue())
        .setName(bs.getValue("nodename").stringValue()));
    }
    }

  }



}
