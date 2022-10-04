import { buildQueryDisplayFromQuery } from "@/logic/queryBuilder/displayBuilder";
import { buildQueryObjectFromQuery } from "@/logic/queryBuilder/objectBuilder";
import express, { NextFunction, Request, Response } from "express";
import QueryRunner from "../logic/queryRunner";
import QueryWorkflow from "../logic/queryWorkflow";

import TTEntity from "../model/tripletree/TTEntity";

export default class QueryController {
  public path = "/";
  public router = express.Router();
  private runner: QueryRunner;
  private workflow: QueryWorkflow;

  constructor() {
    this.initRoutes();
    this.runner = new QueryRunner();
    this.workflow = new QueryWorkflow();
  }

  private initRoutes() {
    this.router.get("/node_api/query/public/populateOpensearch", (req, res, next) => this.populateOpensearch(req, res, next));
    this.router.get("/node_api/query/public/run", (req, res, next) => this.runQuery(req, res, next));
    this.router.get("/node_api/query/public/definition", (req, res, next) => this.definition(req, res, next));
    this.router.get("/node_api/query/public/richDefinition", (req, res, next) => this.richDefinition(req, res, next));
    this.router.get("/node_api/query/public/querySummary", (req, res, next) => this.getQuerySummary(req, res, next));
    this.router.post("/node_api/query/public/querySummary", (req, res, next) => this.postQuerySummary(req, res, next));
    this.router.get("/node_api/query/public/queryClauses", (req, res, next) => this.getQueryClauses(req, res, next));
    this.router.post("/node_api/query/public/queryClauses", (req, res, next) => this.postQueryClauses(req, res, next));
    // this.router.post('/node_api/query/public/querySummary', (req, res, next) => this.postQuerySummary(req, res, next))
    // this.router.post('/node_api/query/public/clauseSummary', (req, res, next) => this.postClauseSummary(req, res, next))
    this.router.get("/node_api/query/public/getSQL", (req, res, next) => this.getSQL(req, res, next));
    this.router.post("/node_api/query/public/quickQuery", (req, res, next) => this.quickQuery(req, res, next));
    this.router.post("/node_api/query/public/queryDisplay", (req, res, next) => this.getQueryDisplay(req, res, next));
    this.router.post("/node_api/query/public/queryObject", (req, res, next) => this.getQueryObject(req, res, next));
  }

  async getSQL(req: Request, res: Response, next: NextFunction) {
    try {
      const sql = await this.runner.generateSQL(req.query.iri as string);
      res.send(sql).end();
    } catch (e) {
      next(e);
    }
  }

  async runQuery(req: Request, res: Response, next: NextFunction) {
    try {
      const data = await this.runner.runQuery(req.query.iri as string);
      res.send(data);
      res.end();
    } catch (e) {
      next(e);
    }
  }

  async populateOpensearch(req: Request, res: Response, next: NextFunction) {
    try {
      const data = await this.workflow.populateOpensearch(req.query.index as string);
      res.send(data).end();
    } catch (e) {
      next(e);
    }
  }
  async definition(req: Request, res: Response, next: NextFunction) {
    try {
      const data = await this.workflow.getDefinition(req.query.iri as string);
      res.send(data).end();
    } catch (e) {
      next(e);
    }
  }

  async richDefinition(req: Request, res: Response, next: NextFunction) {
    try {
      const data = await this.workflow.getRichDefinition(req.query.iri as string);
      res.send(data).end();
    } catch (e) {
      next(e);
    }
  }

  async getQuerySummary(req: Request, res: Response, next: NextFunction) {
    try {
      const data = await this.workflow.summariseQuery("get", req.query.iri as string);
      res.send(data).end();
    } catch (e) {
      next(e);
    }
  }
  async postQuerySummary(req: Request, res: Response, next: NextFunction) {
    try {
      const data = await this.workflow.summariseQuery("post", req.body);
      res.send(data).end();
    } catch (e) {
      next(e);
    }
  }

  async getQueryClauses(req: Request, res: Response, next: NextFunction) {
    try {
      const data = await this.workflow.extractClauses("get", req.query.iri as string);
      res.send(data).end();
    } catch (e) {
      next(e);
    }
  }
  async postQueryClauses(req: Request, res: Response, next: NextFunction) {
    try {
      const data = await this.workflow.extractClauses("post", req.body);
      res.send(data).end();
    } catch (e) {
      next(e);
    }
  }

  // async postClauseSummary(req: Request, res: Response) {
  //   console.log(req.body)
  //   const data = await this.workflow.summariseClause("post", req.body)
  //   res.send(data).end();
  // }

  // async postClauseSummary(req: Request, res: Response, next: NextFunction) {
  //   try {
  //     console.log(req.body)
  //     const data = await this.workflow.summariseClause("post", req.body)
  //     res.send(data).end();
  //   } catch (e) {
  //     next(e);
  //   }
  // }

  async quickQuery(req: Request, res: Response, next: NextFunction) {
    try {
      const data = await this.runner.quickQuery(req.body);
      res.send(data).end();
    } catch (e) {
      next(e);
    }
  }

  async getQueryDisplay(req: Request, res: Response, next: NextFunction) {
    try {
      const data = await buildQueryDisplayFromQuery(req.body);
      res.send(data).end();
    } catch (e) {
      next(e);
    }
  }

  async getQueryObject(req: Request, res: Response, next: NextFunction) {
    try {
      const data = buildQueryObjectFromQuery(req.body);
      res.send(data).end();
    } catch (e) {
      next(e);
    }
  }
}
