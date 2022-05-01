import express, { Request, Response } from 'express';
import AuthMiddleware from '../middlewares/auth.middleware';
import { GraphdbService } from '../services/graphdb.service';

import { SparqlSnippets } from '../helpers/'
import VocabularyUtils from '../helpers/OntologyUtils'
import TTEntity from '../model/tripletree/TTEntity'
import { ConfigurationServicePlaceholders } from 'aws-sdk/lib/config_service_placeholders';

export default class EntityController {
  public path = '/'
  public router = express.Router();
  private auth;
  private service;

  constructor() {
    this.auth = new AuthMiddleware();
    this.initRoutes();
    this.service = new GraphdbService();
  }

  private initRoutes() {
    this.router.get('/private', /*this.auth.secure('IM1_PUBLISH'),*/(req, res) => this.graphDBTest(req, res));
    this.router.get('/api/entity/public/inferredBundle', (req, res) => this.graphDBinferredBundle(req, res));
    this.router.post('/api/entity/public/bulkUpdatePredicates', (req, res) => this.graphDBbulkUpdatePredicates(req, res));
    // /api/entity/public/fullEntity/:iri
  }

  private async graphDBTest(req: Request, res: Response) {
    const rs = await this.service.execute("SELECT * WHERE { <http://endhealth.info/im#Concept> ?p ?o } LIMIT 5");

    res.send(rs);
  }

  private async graphDBcreateEntity(req: Request, res: Response) {



  }


  //POST Request with key in body named "predicates" containing an array of predicates to be updated
  private async graphDBbulkUpdatePredicates(req: Request, res: Response) {

    const updates = req.body.predicates;

    // console.log("updates: ", updates);

    const updateDB = (update) => {

      try {

        const { action, ...quad } = update;

        console.log("update", update);

        const isObjectKeysEmptyOrNull = Object.keys(update).some(key => {
          return update[key] == null || update[key] == "";
        });

        // console.log("isObjectKeysEmptyOrNull", isObjectKeysEmptyOrNull)

        if (isObjectKeysEmptyOrNull) throw "One or more properties are empty or null";


        const query = SparqlSnippets[action](quad);

        const isQuerySuccess = new Promise((resolve, reject) => {
          if (this.service.update(query)) {
            resolve(true);
          } else {
            reject(false)
          }
        });

        return isQuerySuccess;

      } catch (err) {
        console.log("err", err)
        return false;
      }
    };

    const actions = updates.map(updateDB);

    const queryResults = Promise.all(actions);

    const rs = {
      success: true,
      message: "All updates were accepted",
    }

    queryResults.then(results => {

      const isAllUpdatesSuccessful = results.every(result => result);

      if (isAllUpdatesSuccessful) {
        return res.status(200).send(rs);
      } else {
        rs.success = false;
        rs.message = "One or more upates was rejected";
        rs["data"] = results.map((result, index) => {
          return {
            action: updates[index].action,
            index: index,
            success: result
          }
        })
        return res.status(500).send(rs);
      }
    });
  };


  //GET Request with query parameter "iri"
  private async graphDBinferredBundle(req: Request, res: Response) {

    const iri = req.query.iri;

    if (iri && iri != "") {
      const query = SparqlSnippets.inferredBundle(iri as string);

      const queryResult = await this.service.execute(query);

      let rs = {
        // entity: { "@id": iri, "rdf:type": [], "rdfs:label": null, "rdfs:comment": null, "im:isContainedIn": [], "im:definition": null },
        entity: new TTEntity(iri as string),
        predicates: {}
      };

      console.log("rs", rs);

      // populates response with queryResults
      queryResult.forEach(item => {
        const predicate = item.predicate.value || item?.predicate;
        const object = item?.object?.value || item?.object;
        const predicateLabel = item?.predicateLabel;
        const objectLabel = item?.objectLabel;

        const prefixedPredicate = VocabularyUtils.toPrefix(predicate)


        if (Array.isArray(rs.entity[prefixedPredicate])) {
          rs.entity[prefixedPredicate].push(
            {
              "@id": object,
              "name": objectLabel
            }
          );
        } else {
          rs.entity[prefixedPredicate] = object;
        }

        rs.predicates[predicate] = predicateLabel;

      })


      res.status(200).send(rs);
    } else {
      console.log("No iri query paramater provided in GET requests");
      res.status(500).send("No iri query paramater provided in GET requests");
    }
  }


}

