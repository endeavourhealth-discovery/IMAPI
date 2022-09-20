import { Query } from 'im-library/dist/types/models/modules/AutoGen';
import { GraphdbService, iri } from '../services/graphdb.service';

import jp from 'jsonpath';
import { TextGenerator } from "../helpers/text";
import { ManipulationUtils, SparqlSnippets } from '../helpers/query'
const { onlyUnique, excludedPaths, entitiesFromPredicates, isTTIriRef } = ManipulationUtils;
import _ from "lodash";
import { v4 } from "uuid";
import axios from 'axios';
import * as fs from 'fs';


export default class QueryWorkflow {

  private showConsole = false;


  private graph: GraphdbService;
  constructor() {
    this.graph = new GraphdbService();
  }

  public static saveFile(file: string): void {
    fs.writeFile("output.json", file, 'utf8', function (err: any) {
      if (err) {
        console.log("An error occured while writing JSON Object to File.");
      }
      console.log("JSON file has been saved.");
    });
  }


  public static async getAllQueries(): Promise<any> {
    try {

      var data = JSON.stringify({
        "query": {
          "name": "get entities with property",
          "description": "get entities with property",
          "mainEntity": {
            "@id": "http://www.w3.org/ns/shacl#NodeShape"
          },
          "resultFormat": "OBJECT",
          "select": {
            "property": [
              {
                "@id": "http://www.w3.org/2000/01/rdf-schema#label"
              }
            ],
            "match": [
              {
                "entityType": {
                  "@id": "http://endhealth.info/im#Query"
                },
                "property": []
              }
            ]
          },
          "usePrefixes": true
        }
      });

      var config = {
        method: 'post',
        url: process.env.API + '/api/query/public/queryIM',
        headers: {
          'Content-Type': 'application/json'
        },
        data: data
      };

      const res = axios(config as any)
        .then(function (response) {
          // console.log(response.data);
          // return JSON.stringify(response.data);
          return response.data;

        })
        .catch(function (error) {
          console.log(error);
        });
      return res;

    } catch (error) {
      return {} as any;
    }
  }


  public async populateOpensearch(index: string): Promise<any> {

    const allQueryIris = await QueryWorkflow.getAllQueries();
    const iris = allQueryIris?.entities?.map((item: any) => item["@id"]);

    const getClauses = (iri: string) => { // sample async action
      return this.extractClauses("get", iri);
    };
    // map over forEach since it returns

    var actions = iris.map(getClauses); // run the function over all items

    // we now have a promises array and we want to wait for it

    let results: any = Promise.all(actions); // pass array of promises
    // console.log(iris)

    results.then((data: any[]) => {

      let clauses: any[] = [];
      data.forEach(item => {
        if (item.length > 0) {
          clauses = [...clauses, ...item];
        }
      });

      let output = "";
      clauses.forEach((clause: any, clauseIndex: number) => {
        const indexText = `{ "index" : { "_index": "${index}", "_id" : ${clauseIndex} } }`
        output += indexText + "\n";
        output += JSON.stringify(clause) + "\n";
      });
      QueryWorkflow.saveFile(output);

    });
  }



  public async getLocalDefinition(queryIri: string): Promise<any> {
    const queries = require("../examples/queries.json") || null;
    const query = queries ? queries.find((item: any) => item["@id"] == queryIri) : ""
    console.log(queryIri, query)
    return query;
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

    IriRefs = IriRefs.filter((ref: any) => isTTIriRef(ref.value)); // excludes objects which match the jsonQuery but  are operators/clauses instead of IriRefs

    // get all entities from database for TTIriRefs
    const iris = IriRefs.map((item: any) => item.value["@id"]);
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

    //matchClause = an object with "property" key
    const jsonQuery = `$..[?(@.property || @.pathTo)]`;    // const jsonQuery = `$..[? (@.@id)]`
    let clauses = QueryWorkflow.findClauses(definition?.select?.match);
    console.log("clauses", clauses)

    // add summary to match clauses
    clauses.forEach((item: any) => {
      const textPath = item.path + ".displayText";
      const htmlPath = item.path + ".displayHTML";
      const { text, html } = TextGenerator.summarise(item.value) || "";
      // const htmlSummary = TextGenerator.htmlSummary(item.value) || "";
      text ? _.set(definition?.select?.match, textPath, text) : null;
      // htmlSummary ? _.set(definition?.select?.match, htmlPath, htmlSummary) : null;
      console.log(`### summary of clause(${textPath}): `, text);
    })
    this.showConsole && console.log(`### Definition with displayText + HTML: \n`, _.cloneDeep(definition));
    return definition;

  }


  public async extractClauses(type: string = "get", payload: any): Promise<any> {

    // const definition: DataSet =
    const definition: any =
      (type == "get")
        ? await this.getDefinition(payload)
        : payload;

    this.showConsole && console.log(`definition`, definition);

    //matchClause = an object with "property" key
    const jsonQuery = `$..[?(@.property || @.pathTo)]`;    // const jsonQuery = `$..[? (@.@id)]`
    let clauses = QueryWorkflow.findClauses(definition?.select?.match);
    console.log("clauses", clauses)

    // add summary to match clauses
    clauses = clauses.map((item: any) => {
      const path = item.path;
      const { text, html } = TextGenerator.summarise(item.value) || "";
      // const htmlSummary = TextGenerator.htmlSummary(item.value) || "";
      // text ? _.set(definition?.select?.match, textPath, text) : null;
      // htmlSummary ? _.set(definition?.select?.match, htmlPath, htmlSummary) : null;
      console.log(`### summary of clause(${path}): `, text);

      return {
        "iri": `urn:uuid:${v4()}`,
        "name": text,
        "entityType": [
          {
            "@id": `http://endhealth.info/im#MatchClause`,
            "name": "Match Clause"
          },
        ],
        "matchClause": item.value
      }


    })
    this.showConsole && console.log(`### Definition with displayText + HTML: \n`, _.cloneDeep(definition));
    return clauses;

  }


  // public async summariseClause(type: string = "get", payload: any): Promise<any> {
  //   const definition: any =
  //     (type == "post")
  //       ? payload //this.populateDefinition(payload) 
  //       : payload;
  //   this.showConsole && console.log(`definition`, definition);

  //   const summary = TextGenerator.summarise(definition) || "";
  //   this.showConsole && console.log(`### summary of clause `, summary);
  //   return summary;
  // }

  //returns jsonPath for all clauses (properties or matchClauses) that need need displayText / displayHT
  public static findClauses(matchClause: any): any {
    let result: any[] = [];
    const visited = new Set();
    const queuedPaths = [""];
    const getChildren = (path: string) => {
      return path == "" ? matchClause : _.get(matchClause, path);
    }



    const clauseIsAdded = (path: string, clause: any): boolean => {
      //datamodel entities are translated at the level of the matchClause
      if (Object.keys(clause).some(key => key == "pathTo")) {
        result.push({ path: path, value: clause })
        return true;
      } else if (Object.keys(clause).some(key => key == "property")) {
        clause?.property.forEach((property: any, index: number) => {
          result.push({ path: `${path}.property[${index}]`, value: property })
        })
        return true;
      }
      return false;
    };


    while (queuedPaths.length > 0) {
      const currentPath = queuedPaths.shift();
      const children = getChildren(currentPath as string);
      const isArray = Array.isArray(children);

      if (isArray && children.length > 0) {

        children.forEach((child: any, index: number) => {
          //if a child contains a clause needing translation it is added  adds to final results.
          // otherwise it's children are added to the queue for inspection 
          if (clauseIsAdded(`${currentPath}[${index}]`, child) == false) {
            if (child?.or) queuedPaths.push(`${currentPath}[${index}].or`)
            if (child?.and) queuedPaths.push(`${currentPath}[${index}].and`)
          }
        })
      }

    }
    return result;
  }




}
