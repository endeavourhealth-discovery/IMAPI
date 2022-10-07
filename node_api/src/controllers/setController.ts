import { buildSetDefinition } from "@/logic/setDefinitionBuilder";
import express, { NextFunction, Request, Response } from "express";

import TTEntity from "../model/tripletree/TTEntity";

export default class QueryController {
  public path = "/";
  public router = express.Router();

  constructor() {
    this.initRoutes();
  }

  private initRoutes() {
    this.router.get("/node_api/set/public/definition", (req, res, next) => this.getSetDefinition(req, res, next));
  }

  async getSetDefinition(req: Request, res: Response, next: NextFunction) {
    const data = await buildSetDefinition(req.query.iri as string);
    res.send(data).end();
  }
}
