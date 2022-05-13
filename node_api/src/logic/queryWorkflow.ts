import { DataSet } from '../model/sets/DataSet';
import { GraphdbService, iri } from '../services/graphdb.service';

import jp from 'jsonpath';
import { TextGenerator } from "../model/text";
import { ManipulationUtils, SparqlSnippets } from '../helpers/query'
const { onlyUnique, excludedPaths, entitiesFromPredicates, isTTIriRef } = ManipulationUtils;


import _ from "lodash";


export default class QueryWorkflow {
  private graph: GraphdbService;
  constructor() {
    this.graph = new GraphdbService();
  }


  public async getDefinition(queryIri: string): Promise<DataSet> {
    console.log("Loading " + queryIri);
    const rs = await this.graph.execute(
      "SELECT * WHERE { ?s ?p ?def } LIMIT 1",
      {
        s: iri(queryIri),
        p: iri("http://endhealth.info/im#definition")
      });

    if (rs.length != 1)
      return {} as DataSet;

    return JSON.parse(rs[0].def);
  }


  //richDefinition //pre populated with rdf:type, range, 
  // get all TTIriRefs from definition


  /*  #swagger.parameters['obj'] = {
                in: 'body',
                description: 'Accepts an array of IRIs"',
                schema: {
                    iri: '[uuid1, iri2,...]',
                }
        } */
  public async allEntities(iris: string[]) {

    if (Array.isArray(iris) && iris.length > 0) {

      iris.filter(onlyUnique);

      const query = SparqlSnippets.allEntities(iris as string[]);

      const queryResult = await this.graph.execute(query);

      const rs = entitiesFromPredicates(queryResult);

      return rs;

    } else {
      console.log("No iri query paramater provided in GET requests");
      return {};
    }
  }




  public async getRichDefinition(queryIri: string): Promise<any> {

    const definition: DataSet = await this.getDefinition(queryIri);

    // find all TTIriRefs in definition
    const jsonQuery = `$..[?(@.@id)]`;
    let IriRefs = jp.nodes(definition.select, jsonQuery);
    if (IriRefs.length == 0) return {};

    IriRefs = IriRefs.filter(ref => isTTIriRef(ref.value)); // excludes objects which match the jsonQuery but  are operators/clauses instead of IriRefs 


    // get all entities from database for TTIriRefs
    const iris = IriRefs.map(item => item.value["@id"]);
    const entities = await this.allEntities(iris);

    // populate definition with entities
    IriRefs.forEach((item: any) => {
      const path = jp.stringify(item.path).substring(2);
      const entity = entities.find(entity => entity["@id"] == item.value["@id"])
      entity ? _.set(definition.select, path, entity) : console.log("No DB entity found for IriRef at path: " + path);
    })

    return definition;
  }

  // public async getTextSummary(propertyOrClause: any): Promise<string> {


  // }
  //populate definition
  public async summariseQuery(type: string = "get", payload: any): Promise<any> {

    const definition: DataSet =
      (type == "get")
        ? await this.getDefinition(payload)
        : payload;

    //matchClause = an object with "property" key
    const jsonQuery = `$..[?(@.property)]`;    // const jsonQuery = `$..[?(@.@id)]`
    let matchClauses = jp.nodes(definition.select, jsonQuery);
    matchClauses = matchClauses.filter(excludedPaths);


    // add summary to match clauses
    matchClauses.forEach(item => {
      const summary = TextGenerator.summarise(item.value) || "";
      const key = "name";
      const path = jp.stringify(item.path).substring(2) + key;
      summary ? _.set(definition.select, path, summary) : null;
      // console.log(`summary of clause (${path}):`, summary);
    })

    return definition;

  }
  // const summary = await this.getTextSummary(matchClauses);

  // populate

  // save query back to db + generate provenance agent + activity

  // save clause  to db as im:matchClause + update OpenSearch

  // return response with populated query definition






  // public async saveDefinition(queryIri: string, definition): Promise<DataSet>





}
