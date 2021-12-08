import { isArrayHasLength, isObjectHasKeys } from "../helpers/DataTypeCheckers";
import { TransformInstruction, TransfromType } from "../models";
import { FunctionWrapper } from "../transformFunctions/FunctionWrapper";
import { getPathFromPathArray } from "./JsonPathController";

export function getTransformTypes() {
  return Object.keys(TransfromType);
}

export function getFunctions() {
  return Object.getOwnPropertyNames(FunctionWrapper.prototype)
    .filter(item => typeof new FunctionWrapper()[item] === "function")
    .filter(functionName => functionName !== "constructor");
}

export function transformRow(input: any[], instance: any, instruction: TransformInstruction) {
  const destinationPath = getPathFromPathArray(input[0], instruction.destinationPath);
  instance[destinationPath] = getValue(input, instance, instruction);
}

export function getValue(input: any[], instance: any, instruction: TransformInstruction): string {
  switch (instruction.transformType) {
    case "function":
      return getValueFromFunctions(instance, input, instruction);
    case "template":
      return getValueFromTemplate(input, instruction);
    case "reference":
      return input[0][instruction.transformValue as string];
    case "constant":
      return instruction.transformValue as string;
    default:
      return instruction.transformValue as string;
  }
}

function getValueFromFunctions(instance: any, input: any[], instruction: TransformInstruction): string {
  const value = input[0][instruction.property as string];
  const transformations: string[] = [];
  transformations.push(value);
  let index = 0;
  if (isArrayHasLength(instruction.transformValue)) {
    (instruction.transformValue as []).forEach(transformValue => {
      transformations.push(getValueFromFunction(instance, transformations[index], transformValue));
      index++;
    });
    return transformations[transformations.length - 1];
  }

  return getValueFromFunction(instance, value, instruction.transformValue as string);
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
  const openingIndex = instruction.transformValue.indexOf("{");
  const closingIndex = instruction.transformValue.indexOf("}");
  const enclosedString = (instruction.transformValue as string).substring(openingIndex, closingIndex + 1);
  const value = input[0][enclosedString.substring(1, enclosedString.length - 1)];
  return (instruction.transformValue as string).replace(new RegExp(enclosedString), value);
}
