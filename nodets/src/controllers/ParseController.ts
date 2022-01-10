import { Request, Response, Application } from "express";
import { IController } from "../models";
import { getJsonFromFile } from "../services/ParsService";
import { validateTransformInputUpload } from "../validators/ParseValidator";

export default class ParseController implements IController {
  constructor(app: Application) {
    this.configureRoutes(app);
  }

  configureRoutes(app: Application) {
    app.route("/api/transform/transformInputUpload").post(validateTransformInputUpload, this.getJsonFromFile);
  }

  getJsonFromFile(req: Request, res: Response) {
    res.status(200).json(getJsonFromFile(req.body.fileString));
  }
}
