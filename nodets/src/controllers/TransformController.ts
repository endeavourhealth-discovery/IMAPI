import { Request, Response, Application } from "express";
import { IController } from "../models";
import { getFunctions, getTransformed, getTransformTypes, transformByInstruction } from "../services/TransformService";

export default class TransformController implements IController {
  constructor(app: Application) {
    this.configureRoutes(app);
  }

  configureRoutes(app: Application) {
    app.route("/api/transform/functions").get(this.getFunctions);
    app.route("/api/transform/transformed").post(this.getTransformed);
    app.route("/api/transform/types").get(this.getTransformTypes);
    app.route("/api/transform/rowTransformation").post(this.getTransformedByInstruction);
  }

  getTransformed(req: Request, res: Response) {
    const { inputJson, dataModelJson, instructions } = req.body;
    res.status(200).send(getTransformed(inputJson, dataModelJson, instructions));
  }

  getFunctions(req: Request, res: Response) {
    res.status(200).send(getFunctions());
  }

  getTransformTypes(req: Request, res: Response) {
    res.status(200).send(getTransformTypes());
  }

  getTransformedByInstruction(req: Request, res: Response) {
    const { instruction, instances, input } = req.body;
    res.status(200).send(transformByInstruction(instruction, instances, input));
  }
}
