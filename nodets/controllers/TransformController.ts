import { isArrayHasLength, isObjectHasKeys } from "../helpers/DataTypeCheckers";
import { TransformInstruction, TransfromType } from "../models";
import { FunctionWrapper } from "../transformFunctions/FunctionWrapper";
import { queryWithJpath, setValueWithJpath } from "./JsonPathController";

export function getTransformTypes() {
  return Object.keys(TransfromType).map(functionName => functionName.toLowerCase());
}

export function getFunctions() {
  return Object.getOwnPropertyNames(FunctionWrapper.prototype)
    .filter(propertyName => typeof new FunctionWrapper()[propertyName] === "function")
    .filter(functionName => functionName !== "constructor");
}

export function transformByInstruction(instruction: TransformInstruction, instances: any[], input: any) {
  const newValue = setValueWithJpath(instances, instruction.destinationPath, getValue(input, instances, instruction));
  instruction.exampleTransformed = newValue;
  return { instruction, instances };
}

function getValue(input: any[], instance: any, instruction: TransformInstruction): string {
  switch (instruction.transformType) {
    case "function":
      return getValueFromFunctions(instance, input, instruction);
    case "template":
      return getValueFromTemplate(input, instruction);
    case "reference":
      return getValueFromReference(input, instruction.transformValue);
    case "constant":
      return instruction.transformValue as string;
    default:
      return instruction.transformValue as string;
  }
}

function getValueFromReference(input: any[], transformValue: string) {
  const referenceValues = queryWithJpath(input[0], transformValue);
  if (!isArrayHasLength(referenceValues)) {
    return undefined;
  }
  return referenceValues[0];
}

function getValueFromFunctions(instance: any, input: any[], instruction: TransformInstruction): string {
  const value = getValueFromReference(input, instruction.transformValue);
  const transformations: string[] = [];
  transformations.push(value);
  let index = 0;
  instruction.transformFunctions.forEach(transformFunction => {
    transformations.push(getValueFromFunction(instance, transformations[index], transformFunction));
    index++;
  });
  return transformations[transformations.length - 1];
}

function getValueFromFunction(dataModel: any, value: string, transformValue: string): string {
  if (functionExists(transformValue)) {
    const functionWrapper = new FunctionWrapper();
    return functionWrapper[transformValue](value);
  } else {
    return "";
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
