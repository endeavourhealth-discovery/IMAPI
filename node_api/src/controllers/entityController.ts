import express, { Request, Response } from 'express';
import AuthMiddleware from '../middlewares/auth.middleware';
import { GraphdbService } from '../services/graphdb.service';

import VocabularyUtils from '../helpers/manipulation/OntologyUtils'

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
    this.router.post('/api/entity/public/updatePredicates', (req, res) => this.graphDBupdatePredicates(req, res));
    // /api/entity/public/fullEntity/:iri
  }

  private async graphDBTest(req: Request, res: Response) {
    const rs = await this.service.execute("SELECT * WHERE { <http://endhealth.info/im#Concept> ?p ?o } LIMIT 5");

    res.send(rs);
  }


  private async graphDBupdatePredicates(req: Request, res: Response) {

    const updates = req.body.predicates;

    // console.log("updates: ", updates);

    const updateDB = (update) => {

      try {

        const { iri, property, value } = update;

        const isObjectKeysEmptyOrNull = Object.keys(update).some(key => {
          return update[key] == null || update[key] == "";
        });

        // console.log("isObjectKeysEmptyOrNull", isObjectKeysEmptyOrNull)

        if (isObjectKeysEmptyOrNull) throw "One or more properties are empty or null";

        const query = `
      DELETE {
        <${iri}> ${property} ?oldValue
      }
      INSERT {
        <${iri}> ${property}  "${value}"
      }
      WHERE {
        <${iri}> ${property} ?oldValue
      }`;


        const isQuerySuccess = new Promise((resolve, reject) => {
          if (this.service.update(query)) {
            resolve(true);
          } else {
            reject(false)
          }
        });

        //this.service.update(query).then(resolve).catch(reject);

        // console.log("isQuerySuccess", isQuerySuccess)
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
      message: "All updates accepted",
    }

    queryResults.then(results => {

      if (results.every(result => result)) {
        return res.status(200).send(rs);
      } else {
        rs.success = false;
        rs.message = "One or more upates failed";
        rs["data"] = results.map((result, index) => {
          return {
            action: "update",
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
      const query = `
      SELECT 
        ?predicate 
        ?predicateLabel 
        ?object 
        ?objectLabel
      WHERE {
                  <${iri}> ?predicate ?object.
                  ?predicate rdfs:label ?predicateLabel.
        optional  {?object rdfs:label ?objectLabel.}
        }`;


      const queryResult = await this.service.execute(query);

      //response
      let rs = {
        entity: { "@id": iri, "rdf:type": [], "rdfs:label": null, "rdfs:comment": null, "im:isContainedIn": [], "im:definition": null },
        predicates: {}
      };

      // populates response with queryResults
      queryResult.forEach(item => {
        const predicate = item.predicate.value || item?.predicate;
        const object = item?.object?.value || item?.object;
        const predicateLabel = item?.predicateLabel;
        const objectLabel = item?.objectLabel;

        const prefixedPredicate = VocabularyUtils.toPrefixedPredicate(predicate)

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

