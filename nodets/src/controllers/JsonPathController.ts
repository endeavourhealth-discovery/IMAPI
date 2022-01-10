import { Request, Response, Application } from "express";
import { IController } from "../models";
import { getInputFromJpath, getJpathTreeOptions, getJsonPathOptions } from "../services/JsonPathService";

export default class JsonPathController implements IController {
  constructor(app: Application) {
    this.configureRoutes(app);
  }

  configureRoutes(app: Application) {
    app.route("/api/transform/inputFromJpath").post(this.getInputFromJpath);
    app.route("/api/transform/jpathsFromInput").post(this.getJsonPathOptions);
    app.route("/api/transform/jpathTreeOptions").post(this.getJpathTreeOptions);
  }

  getJpathTreeOptions(req: Request, res: Response) {
    res.status(200).json(getJpathTreeOptions(req.body));
  }

  getJsonPathOptions(req: Request, res: Response) {
    res.status(200).json(getJsonPathOptions(req.body));
  }

  getInputFromJpath(req: Request, res: Response) {
    const { input, jpath } = req.body;
    res.status(200).json(getInputFromJpath(input, jpath));
  }
}
