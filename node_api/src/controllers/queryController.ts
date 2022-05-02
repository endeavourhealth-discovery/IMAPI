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
    this.router.get('/api/query/public/run', (req, res) => this.runQuery(req, res));
    this.router.get('/api/query/public/definition', (req, res) => this.definition(req, res));
    this.router.get('/api/query/public/richDefinition', (req, res) => this.richDefinition(req, res));
    this.router.get('/api/query/public/populate', (req, res) => this.populate(req, res))

  }

  async runQuery(req: Request, res: Response) {
    const data = await this.runner.runQuery(req.query.iri as string);
    res.send(data);
    res.end();
  }

  async definition (req: Request, res: Response) {
    const data = await this.workflow.getDefinition(req.query.iri as string);
    res.send(data).end();
  }

  async richDefinition (req: Request, res: Response) {
    const data = await this.workflow.getRichDefinition(req.query.iri as string);
    res.send(data).end();
  }


  
 
  protected async populate(req: Request, res: Response) {


    const data =await this.workflow.populateQuery(req.query.iri as string)

    // console.log("query", data)
    res.send(data).end();

  }

}

