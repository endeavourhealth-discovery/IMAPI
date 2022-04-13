import express, {Request, Response} from 'express';
import AuthMiddleware from '../middlewares/auth.middleware';
import {GraphdbService} from '../services/graphdb.service';

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
    this.router.get('/private', /*this.auth.secure('IM1_PUBLISH'),*/ (req, res) => this.graphDBTest(req, res));
  }

  private async graphDBTest(req: Request, res: Response) {
    const rs = await this.service.execute("SELECT * WHERE { <http://endhealth.info/im#Concept> ?p ?o } LIMIT 5");

    res.send(rs);
  }
}

