import express, { Request, Response } from 'express';
import QueryRunner from '../logic/queryRunner';
import QueryWorkflow from '../logic/queryWorkflow';


import TTEntity from '../model/tripletree/TTEntity'


export default class QueryController {
  public path = '/'
  public router = express.Router();
  private runner: QueryRunner;
  private workflow: QueryWorkflow;

  constructor() {
    this.initRoutes();
    this.runner = new QueryRunner();
    this.workflow = new QueryWorkflow();
  }

  private initRoutes() {

    this.router.get('/node_api/query/public/run', (req, res) => this.runQuery(req, res));
    this.router.get('/node_api/query/public/definition', (req, res) => this.definition(req, res));
    this.router.get('/node_api/query/public/richDefinition', (req, res) => this.richDefinition(req, res));
    this.router.get('/node_api/query/public/querySummary', (req, res) => this.getQuerySummary(req, res))
    this.router.post('/node_api/query/public/querySummary', (req, res) => this.postQuerySummary(req, res))

  }

  /* #swagger.parameters['iri'] = {
     in: 'query',
     description: 'The Iri of the query you want to run. Example: "urn:uuid:40a4a1f1-b768-4db8-a8a6-6df744935d97',
  } 
  */
  async runQuery(req: Request, res: Response) {

    const data = await this.runner.runQuery(req.query.iri as string);
    res.send(data);
    res.end();
  }


  async definition(req: Request, res: Response) {
    const data = await this.workflow.getDefinition(req.query.iri as string);
    res.send(data).end();
  }


  async richDefinition(req: Request, res: Response) {
    const data = await this.workflow.getRichDefinition(req.query.iri as string);
    res.send(data).end();
  }

  async postQuerySummary(req: Request, res: Response) {
    res.setHeader('Content-Type', 'application/json')
    const data = await this.workflow.summariseQuery("post", req.body)
    res.send(data).end();

  }
  async getQuerySummary(req: Request, res: Response) {
    res.setHeader('Content-Type', 'application/json')
    const data = await this.workflow.summariseQuery("get", req.query.iri as string)
    res.send(data).end();

  }
}

