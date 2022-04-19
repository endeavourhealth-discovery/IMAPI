import express, {Request, Response} from 'express';
import QueryRunner from '../logic/queryRunner';

export default class QueryController {
  public path = '/'
  public router = express.Router();
  private runner: QueryRunner;

  constructor() {
    this.initRoutes();
    this.runner = new QueryRunner();
  }

  private initRoutes() {
    this.router.get('/query', (req, res) => this.runQuery(req, res));
  }

  async runQuery(req: Request, res: Response) {
    const data = await this.runner.runQuery(req.query.iri as string);
    res.send(data);
    res.end();
  }
}

