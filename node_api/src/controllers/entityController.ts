import EntityService from "@/services/entity.service";
import axios from "axios";
import express, { NextFunction, Request, Response } from "express";

export default class EntityController {
  public path = "/node_api/entity";
  public router = express.Router();
  private entityService;
  constructor() {
    this.entityService = new EntityService(axios);
    this.initRoutes();
  }

  private initRoutes() {
    this.router.get("/public/detailsDisplay", (req, res, next) => this.getDetailsDisplay(req, res, next));
  }

  async getDetailsDisplay(req: Request, res: Response, next: NextFunction) {
    try {
      const data = await this.entityService.getDetailsDisplay(req.query.iri as string);
      res.send(data).end();
    } catch (e) {
      next(e);
    }
  }
}
