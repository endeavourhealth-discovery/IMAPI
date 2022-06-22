import { Query } from 'im-library/dist/types/models/modules/AutoGen';
import { GraphdbService, iri } from '../services/graphdb.service';

import jp from 'jsonpath';
import { TextGenerator } from "../helpers/text";
import { ManipulationUtils, SparqlSnippets } from '../helpers/query'
const { onlyUnique, excludedPaths, entitiesFromPredicates, isTTIriRef } = ManipulationUtils;


import _ from "lodash";


export default class QueryWorkflow {

  private showConsole = false;

  private graph: GraphdbService;
  constructor() {
    this.graph = new GraphdbService();
  }

  public async getDefinition(entityIri: string): Promise<Query> {
    this.showConsole && console.log("Loading iri: " + entityIri);
    const rs = await this.graph.execute(
      "SELECT * WHERE { ?s ?p ?def } LIMIT 1",
      {
        s: iri(entityIri),
        p: iri("http://endhealth.info/im#query")
      });

    if (rs.length != 1)
      return {} as Query;

    return JSON.parse(rs[0].def.value);
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



  //populates .name if missing in TTIriRef
  public async getRichDefinition(queryIri: string): Promise<any> {

    let definition: any = await this.getDefinition(queryIri);
    definition = await this.populateDefinition(definition);
    return definition;

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


  //populates all TTIriRefs with names 
  private async populateDefinition(definition: any): Promise<any> {
    // console.log("definition", definition);

    // find all TTIriRefs in definition
    const jsonQuery = `$..[?(@.@id)]`;
    let IriRefs = jp.nodes(definition?.select?.match, jsonQuery);
    if (IriRefs.length == 0) return {};

    IriRefs = IriRefs.filter(ref => isTTIriRef(ref.value)); // excludes objects which match the jsonQuery but  are operators/clauses instead of IriRefs 

    // get all entities from database for TTIriRefs
    const iris = IriRefs.map(item => item.value["@id"]);
    const meta = await this.getMeta(iris) as any[];

    // populate TTIriRefs with name where missing.
    IriRefs.forEach((item: any) => {
      if (!item?.value?.name) {
        const path = jp.stringify(item.path).substring(2) + ".name";
        const entity = meta.find(entity => entity?.id?.id == item.value["@id"])
        const name = entity?.name?.value || entity?.name;
        entity ? _.set(definition?.select?.match, path, name) : null;
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
    let matchClauses = jp.nodes(definition?.select?.match, jsonQuery);
    matchClauses = matchClauses.filter(excludedPaths);

    // add summary to match clauses
    const addSummary = (clause: any) => {
      const summary = TextGenerator.summarise(clause.value) || "";
      const path = jp.stringify(clause.path).substring(2) + "name";
      summary ? _.set(definition?.select?.match, path, summary) : null;
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
    this.showConsole && console.log(`### summary of clause `, summary);
    return summary;
  }



}
