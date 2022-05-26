import { Match } from './../model/sets/Match';
import { DataSet } from '../model/sets/DataSet';
import TTEntity from '../model/tripletree/TTEntity';
import { GraphdbService, iri } from '../services/graphdb.service';

import jp from 'jsonpath';
import { TextGenerator } from "../model/text";
import { OntologyUtils, ManipulationUtils, SparqlSnippets } from '../helpers/query'
const { onlyUnique, excludedPaths, entitiesFromPredicates, isTTIriRef } = ManipulationUtils;
import { IM, RDF, RDFS } from "../vocabulary"


import _ from "lodash";


export default class QueryWorkflow {

  private showConsole = false;

  private graph: GraphdbService;
  constructor() {
    this.graph = new GraphdbService();
  }


  public async getEntity(entityIri: string): Promise<DataSet> {
    this.showConsole && console.log("Loading iri: " + entityIri);
    const rs = await this.graph.execute(
      "SELECT * WHERE { ?s ?p ?def } LIMIT 1",
      {
        s: iri(entityIri),
        p: iri("http://endhealth.info/im#definition")
      });

    if (rs.length != 1)
      return {} as DataSet;

    return JSON.parse(rs[0]);
  }


  public async getDefinition(queryIri: string): Promise<any | DataSet> {
    this.showConsole && console.log("Loading iri: " + queryIri);

    let s = iri(queryIri),
      p = iri("http://endhealth.info/im#definition"),
      stmt = `SELECT * WHERE { ${s} ${p} ?def. } LIMIT 1`;

    const rs = await this.graph.execute(stmt);
    if (rs.length != 1) return {} as any | DataSet;

    const definition = ManipulationUtils.escapeCharacters(rs[0].def);
    return JSON.parse(definition);

  }


  public async allEntities(iris: string[]) {

    if (Array.isArray(iris) && iris.length > 0) {

      iris = iris
        .filter(onlyUnique)
        .filter(iri => !iri.includes('^')); //e.g. http://snomed.info/sct#735259005^ESCT1198974 breaks the query
      ;

      // const query = SparqlSnippets.allEntities(iris as string[]);

      let i = iris.map(iri => `<${iri}>`).join(','),
        stmt = `
        SELECT ?iri ?predicate ?predicateLabel ?object ?objectLabel
        WHERE   { 	
                ?iri ?predicate ?object.
            optional{?predicate rdfs:label ?predicateLabel.}
            optional{?object rdfs:label ?objectLabel.}
            FILTER (?iri IN (${i}))
        }`;
      this.showConsole && console.log(`stmt`, stmt);

      const queryResult = await this.graph.execute(stmt);

      const rs = entitiesFromPredicates(queryResult);

      return rs;

    } else {
      this.showConsole && console.log("No iri query parameter provided in GET requests");
      return {};
    }
  }


  public async getMeta(iris: string[]) {

    if (Array.isArray(iris) && iris.length > 0) {

      iris = iris
        .filter(onlyUnique)
        .filter(iri => !iri.includes('^')); //e.g. http://snomed.info/sct#735259005^ESCT1198974 breaks the query

      let i = iris.map(iri => `<${iri}>`).join(','),
        stmt = `
        SELECT *
        WHERE {
          ?id rdfs:label ?name.
          FILTER (?id IN (${i}))
        }`;

      const queryResult = await this.graph.execute(stmt);

      return queryResult;

    } else {
      this.showConsole && console.log("No iri query parameter provided in GET requests");
      return {};
    }
  }

  //populates .name if missing in TTIriRef
  public async getRichDefinition(queryIri: string): Promise<any> {


    let definition: any = await this.getDefinition(queryIri);
    definition = await this.populateDefinition(definition);
    return definition;

  }


  private async populateDefinition(definition: any): Promise<any> {
    // find all TTIriRefs in definition
    const jsonQuery = `$..[?(@.@id)]`;
    let IriRefs = jp.nodes(definition?.select?.filter, jsonQuery);
    if (IriRefs.length == 0) return {};

    IriRefs = IriRefs.filter(ref => isTTIriRef(ref.value)); // excludes objects which match the jsonQuery but  are operators/clauses instead of IriRefs 

    // get all entities from database for TTIriRefs
    const iris = IriRefs.map(item => item.value["@id"]);
    const meta = await this.getMeta(iris) as any[];
    // console.log("meta", meta)

    // populate TTIriRefs with name where missing.
    IriRefs.forEach((item: any) => {
      if (!item?.value?.name) {
        const path = jp.stringify(item.path).substring(2) + ".name";
        const entity = meta.find(entity => entity?.id?.id == item.value["@id"])
        entity ? _.set(definition?.select?.filter, path, entity?.name) : null;
      }
    })
    return definition;
  }

  public async summariseQuery(type: string = "get", payload: any): Promise<any> {

    // const definition: DataSet =
    const definition: any =
      (type == "get")
        ? await this.getRichDefinition(payload)
        : payload;

    this.showConsole && console.log(`definition`, definition);

    //matchClause = any object with a "property" key
    const jsonQuery = `$..[?(@.property)]`;    // const jsonQuery = `$..[? (@.@id)]`
    let matchClauses = jp.nodes(definition?.select?.filter, jsonQuery);
    matchClauses = matchClauses.filter(excludedPaths);

    // add summary to match clauses
    const addSummary = (clause: any) => {
      const summary = TextGenerator.summarise(clause.value) || "";
      const path = jp.stringify(clause.path).substring(2) + "name";
      summary ? _.set(definition?.select?.filter, path, summary) : null;
      this.showConsole && console.log(`### summary of clause(${path}): `, summary);
    };
    matchClauses.forEach(addSummary)
    return definition;

  }


  public async summariseClause(type: string = "get", payload: any): Promise<any> {
    const definition: any =
      (type == "post")
        ? payload //this.populateDefinition(payload) 
        : payload;
    this.showConsole && console.log(`definition`, definition);

    const summary = TextGenerator.summarise(definition) || "";
    // summary ? definition['name'] = summary : null;
    this.showConsole && console.log(`### summary of clause `, summary);
    return summary;
  }



}
