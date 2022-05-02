import { Match } from './../model/sets/Match';
import { DataSet } from '../model/sets/DataSet';
import TTEntity from '../model/tripletree/TTEntity';
import { GraphdbService, iri } from '../services/graphdb.service';

import jp from 'jsonpath';
import { SparqlSnippets } from '../helpers/'
import { OntologyUtils, ManipulationUtils } from '../helpers'



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

      iris.filter(ManipulationUtils.onlyUnique);

      const query = SparqlSnippets.allEntities(iris as string[]);

      const queryResult = await this.graph.execute(query);

      const rs = ManipulationUtils.entitiesFromPredicates(queryResult);


      return rs;

    } else {
      console.log("No iri query paramater provided in GET requests");
      return {};
    }
  }




  public async getRichDefinition(queryIri: string): Promise<any> {

    const definition: DataSet = await this.getDefinition(queryIri);

    const jsonQuery = `$..[?(@.@id)]`;
    const TTIriRefsList = jp.nodes(definition.match, jsonQuery);
    if (TTIriRefsList.length == 0) return {};


    const TTIRIRefs = TTIriRefsList.map(item => item?.value["@id"]);


    // console.log("TTIriRefs: " + TTIriRefs);

    const entities = await this.getAllEntities(TTIRIRefs);

    return entities;





  }

  //populate definition
  public async populateQuery(queryIri: string): Promise<any> {

    // split into individual match-clauses

    const definition: DataSet = await this.getRichDefinition(queryIri);

    // const jsonQuery = `$..[?(@.@id)]`
    //matches an object with an "id" and "property" key
    const jsonQuery = `$..[?(@.@id && @.property)]`;
    const matchClauses = jp.nodes(definition.match, jsonQuery);
    console.log("matchClauses: " + matchClauses);
    console.log("path: " + matchClauses);


    // generate query

    // populate

    // save query back to db + generate provenance agent + activity

    // save clause  to db as im:matchClause + update OpenSearch

    // return response with populated query definition
    return matchClauses;

  }







  // public async saveDefinition(queryIri: string, definition): Promise<DataSet>





}
