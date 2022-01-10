import { Request, Response, Application } from "express";
import { IController } from "../models";
import { generateDataModel, getDataModelInstanceDisplay } from "../services/DataModelService";

export default class DataModelController implements IController {
  constructor(app: Application) {
    this.configureRoutes(app);
  }

  configureRoutes(app: Application) {
    app.route("/api/transform/datamodel").post(this.getDataModel);
    app.route("/api/transform/datamodel/instance").post(this.getDataModelInstanceDisplay);
  }

  getDataModel(req: Request, res: Response) {
    res.status(200).json(generateDataModel(req.body));
  }

  getDataModelInstanceDisplay(req: Request, res: Response) {
    res.status(200).json(getDataModelInstanceDisplay(req.body));
  }
}
