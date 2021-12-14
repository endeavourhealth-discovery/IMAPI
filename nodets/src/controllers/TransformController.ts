import { isArrayHasLength, isObjectHasKeys } from "../helpers/DataTypeCheckers";
import { TransformInstruction, TransfromType } from "../models";
import { FunctionWrapper } from "../transformFunctions/FunctionWrapper";
import { queryWithJpath, setValueWithJpath } from "./JsonPathController";

export function getTransformed(inputJson: any[], dataModelJson: any[], instructions: TransformInstruction[]) {
  const transformed = [];
  inputJson.forEach(row => {
    let instances = [];
    dataModelJson.forEach(dataModel => {
      const instance = JSON.parse(JSON.stringify(dataModel));
      instances.push(instance);
    });
    instructions.forEach(instruction => {
      instances = transformByInstruction(instruction, instances, row).instances;
    });
    transformed.push(...instances);
  });

  return transformed;
}

export function getTransformTypes() {
  return Object.keys(TransfromType).map(functionName => functionName.toLowerCase());
}

export function getFunctions() {
  return Object.getOwnPropertyNames(FunctionWrapper.prototype)
    .filter(propertyName => typeof new FunctionWrapper()[propertyName] === "function")
    .filter(functionName => functionName !== "constructor");
}

export function transformByInstruction(instruction: TransformInstruction, instances: any[], input: any) {
  const newValue = getValue(input, instances, instruction);
  instruction.destinationPaths.forEach(path => {
    setValueWithJpath(instances, path, newValue);
  });
  instruction.exampleTransformed = newValue;
  return { instruction, instances };
}

function getValue(input: any[], instance: any, instruction: TransformInstruction): string {
  let value;
  switch (instruction.transformType) {
    case "template":
      value = getValueFromTemplate(input, instruction);
      break;
    case "reference":
      value = getValueFromReference(input, instruction.transformValue);
      break;
    case "constant":
      value = instruction.transformValue as string;
      break;
    default:
      value = instruction.transformValue as string;
      break;
  }
  if (isArrayHasLength(instruction.transformFunctions)) {
    return getValueFromFunctions(value, instruction);
  }

  return value;
}

function getValueFromReference(input: any[], transformValue: string) {
  const referenceValues = queryWithJpath(input[0] ? input[0] : input, transformValue);
  if (!isArrayHasLength(referenceValues)) {
    return undefined;
  }
  return referenceValues[0];
}

function getValueFromFunctions(value: string, instruction: TransformInstruction): string {
  const transformations: string[] = [];
  transformations.push(value);
  let index = 0;
  instruction.transformFunctions.forEach(transformFunction => {
    transformations.push(getValueFromFunction(transformations[index], transformFunction));
    index++;
  });
  return transformations[transformations.length - 1];
}

function getValueFromFunction(value: string, transformValue: string): string {
  if (functionExists(transformValue)) {
    const functionWrapper = new FunctionWrapper();
    return functionWrapper[transformValue](value);
  } else {
    return value;
  }
}

function functionExists(functionName: string) {
  const functions = Object.getOwnPropertyNames(FunctionWrapper.prototype);
  return functions.includes(functionName);
}

function getValueFromTemplate(input: any, instruction: TransformInstruction): string {
  let newValue = instruction.transformValue;
  const templateValues = [];
  const referenceValues = [];
  const regex = /{([^}]+)}/g;
  let curMatch;

  while ((curMatch = regex.exec(instruction.transformValue))) {
    templateValues.push(curMatch[1]);
  }

  templateValues.forEach(value => {
    referenceValues.push(getValueFromReference(input, value));
  });

  for (var i = 0; i < templateValues.length; i++) {
    newValue = newValue.replace(new RegExp("{" + templateValues[i] + "}", "gi"), referenceValues[i]);
  }

  return newValue;
}
