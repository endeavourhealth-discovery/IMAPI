import express from "express";
import cors from "cors";
import TransformController from "./controllers/TransformController";
import DataModelController from "./controllers/DataModelController";
import ParseController from "./controllers/ParseController";
import JoinController from "./controllers/JoinController";
import JsonPathController from "./controllers/JsonPathController";

const app = express();
const port = 3000;

app.use(cors());
app.use(express.json({ limit: "50mb" }));

app.listen(port, () => {
  console.log(`Application is running on port ${port}.`);
});

new TransformController(app);
new DataModelController(app);
new ParseController(app);
new JoinController(app);
new JsonPathController(app);
