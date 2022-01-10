import { Request, Response, Application } from "express";
import { IController } from "../models";
import { join } from "../services/JoinService";

export default class JoinController implements IController {
  constructor(app: Application) {
    this.configureRoutes(app);
  }

  configureRoutes(app: Application) {
    app.route("/api/transform/join").post(this.getJoined);
  }

  getJoined(req: Request, res: Response) {
    const { inputs, instructions } = req.body;
    res.status(200).json(join({ inputs, instructions }));
  }
}
