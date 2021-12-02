import jp from "jsonpath";
import { TransformInputUpload } from "../models";
import { generateDataModel } from "./DataModelController";

export function getInputFromJpath(input: TransformInputUpload, jsonPath: string) {
  const inputAfter = input;
  try {
    inputAfter.inputJson = jp.query(input.inputJson, jsonPath);
    inputAfter.inputDisplayJson = jp.query(input.inputDisplayJson, jsonPath);
    inputAfter.dataModel = generateDataModel(inputAfter.inputJson);
  } catch (error) {
    console.log(error);
    input.inputDisplayJson = [];
    input.inputJson = [];
    return input;
  }

  return inputAfter;
}

export function getJsonPathOptions(input: TransformInputUpload) {
  const paths = jp.paths(input, "$..*");
  return paths;
}
