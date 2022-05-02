import { Match } from './../model/sets/Match';
import { DataSet } from '../model/sets/DataSet';
import TTEntity from '../model/tripletree/TTEntity';
import { GraphdbService, iri } from '../services/graphdb.service';

import jp from 'jsonpath';
import { SparqlSnippets } from '../helpers/'
import { OntologyUtils, ManipulationUtils, QueryUtils } from '../helpers'
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

  // use entityService to get all metadat for TTIriRefs

  // POST Reqwuest with body containing array named "iris"
  private async getAllEntities(iris: string[]) {

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
    let IriRefs = jp.nodes(definition.match, jsonQuery);
    if (IriRefs.length == 0) return {};

    IriRefs = IriRefs.filter(ref => isTTIriRef(ref.value)); // excludes objects which match the jsonQuery but  are operators/clauses instead of IriRefs 


    // get all entities from database for TTIriRefs
    const iris = IriRefs.map(item => item.value["@id"]);
    const entities = await this.getAllEntities(iris);

    // populate definition with entities
    IriRefs.forEach((item: any) => {
      const path = jp.stringify(item.path).substring(2);
      const entity = entities.find(entity => entity["@id"] == item.value["@id"])
      entity ? _.set(definition.match, path, entity) : console.log("No DB entity found IriRef at path: " + path);
    })

    return definition;
  }

  // public async getTextSummary(propertyOrClause: any): Promise<string> {


  // }
  //populate definition
  public async populateQuery(queryIri: string): Promise<any> {

    // split into individual match-clauses

    const definition: DataSet = await this.getRichDefinition(queryIri);

    // const jsonQuery = `$..[?(@.@id)]`
    //matches an object with an "id" and "property" key
    const jsonQuery = `$..[?(@.property)]`;
    let matchClauses = jp.nodes(definition.match, jsonQuery);
    matchClauses = matchClauses.filter(excludedPaths);


    // add summary to match clauses
    matchClauses.forEach(item => {
      const summary  = QueryUtils.summariseClause(item.value);
      const path = jp.stringify(item.path).substring(2) + "_summary";
      summary ? _.set(definition.match, path, summary) : console.log("No DB entity found IriRef at path: " + path);
      console.log("summary: ", summary);
    

    })
    // const summary = await this.getTextSummary(matchClauses);

    // populate

    // save query back to db + generate provenance agent + activity

    // save clause  to db as im:matchClause + update OpenSearch

    // return response with populated query definition
    return definition;

  }







  // public async saveDefinition(queryIri: string, definition): Promise<DataSet>





}
