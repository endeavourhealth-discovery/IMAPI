import express, { Request, Response, NextFunction, response } from "express";
import cors from "cors";
import { join } from "./controllers/JoinController";
import { JoinData, JpathData, TransformInputUpload } from "./models";
import { getInputFromJpath, getJsonPathOptions } from "./controllers/JsonPathController";
import { getJsonFromFile } from "./controllers/ParseController";

const app = express();
const port = 3000;

app.use(cors());
app.use(express.json({ limit: "50mb" }));

app.listen(port, () => {
  console.log(`Application is running on port ${port}.`);
});

app.post("/api/transform/join", (request: Request, response: Response, next: NextFunction) => {
  const body: JoinData = request.body;
  response.status(200).json(join(body));
});

app.post("/api/transform/transformInputUpload", async (request: Request, response: Response, next: NextFunction) => {
  response.status(200).json(getJsonFromFile(request.body.fileString));
});

app.post("/api/transform/jpathsFromInput", (request: Request, response: Response, next: NextFunction) => {
  const body: TransformInputUpload = request.body;
  response.status(200).json(getJsonPathOptions(body));
});

app.post("/api/transform/inputFromJpath", (request: Request, response: Response, next: NextFunction) => {
  const body: JpathData = request.body;
  response.status(200).json(getInputFromJpath(body.input, body.jpath));
});
