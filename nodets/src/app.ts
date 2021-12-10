import express, { Request, Response, NextFunction, response } from "express";
import cors from "cors";
import { join } from "./controllers/JoinController";
import { getInputFromJpath, getJpathTreeOptions, getJsonPathOptions } from "./controllers/JsonPathController";
import { getJsonFromFile } from "./controllers/ParseController";
import { generateDataModel, getDataModelInstanceDisplay } from "./controllers/DataModelController";
import { getFunctions, getTransformTypes, transformByInstruction } from "./controllers/TransformController";

const app = express();
const port = 3000;

app.use(cors());
app.use(express.json({ limit: "50mb" }));

app.listen(port, () => {
  console.log(`Application is running on port ${port}.`);
});

app.post("/api/transform/join", (request: Request, response: Response, next: NextFunction) => {
  const { inputs, instructions } = request.body;
  response.status(200).json(join({ inputs, instructions }));
});

app.post("/api/transform/transformInputUpload", async (request: Request, response: Response, next: NextFunction) => {
  response.status(200).json(getJsonFromFile(request.body.fileString));
});

app.post("/api/transform/jpathsFromInput", (request: Request, response: Response, next: NextFunction) => {
  response.status(200).json(getJsonPathOptions(request.body));
});

app.post("/api/transform/jpathTreeOptions", (request: Request, response: Response, next: NextFunction) => {
  response.status(200).json(getJpathTreeOptions(request.body));
});

app.post("/api/transform/datamodel", (request: Request, response: Response, next: NextFunction) => {
  response.status(200).json(generateDataModel(request.body));
});

app.post("/api/transform/datamodel/instance", (request: Request, response: Response, next: NextFunction) => {
  response.status(200).json(getDataModelInstanceDisplay(request.body));
});

app.get("/api/transform/types", (request: Request, response: Response, next: NextFunction) => {
  response.status(200).json(getTransformTypes());
});

app.get("/api/transform/functions", (request: Request, response: Response, next: NextFunction) => {
  response.status(200).json(getFunctions());
});

app.post("/api/transform/rowTransformation", (request: Request, response: Response, next: NextFunction) => {
  const { instruction, instances, input } = request.body;
  response.status(200).json(transformByInstruction(instruction, instances, input));
});

app.post("/api/transform/inputFromJpath", (request: Request, response: Response, next: NextFunction) => {
  const { input, jpath } = request.body;
  response.status(200).json(getInputFromJpath(input, jpath));
});
