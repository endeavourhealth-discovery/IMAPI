import { TransformInstruction } from "../models";

export class FunctionWrapper {
  toUpperCamelCase(value: string): string {
    const lowerCamelCase = this.toLowerCamelCase(value);
    return lowerCamelCase.charAt(0).toUpperCase() + lowerCamelCase.slice(1);
  }

  toLowerCamelCase(value: string): string {
    return value.toLowerCase().replace(/(?:^\w|[A-Z]|\b\w|\s+)/g, (match, index) => {
      if (+match === 0) return ""; // or if (/\s+/.test(match)) for white spaces
      return index === 0 ? match.toLowerCase() : match.toUpperCase();
    });
  }

  generateIri(dataModel: any, value: string): string {
    const shortcut = dataModel["@graph"]["@id"].split(":");
    const firstPart = dataModel["@context"][shortcut[0]];
    return firstPart + value;
  }

  getValueFromTemplate(input: any, instruction: TransformInstruction): string {
    const openingIndex = instruction.transformValue.indexOf("{");
    const closingIndex = instruction.transformValue.indexOf("}");
    const enclosedString = (instruction.transformValue as string).substring(openingIndex, closingIndex + 1);
    const value = input[0][enclosedString.substring(1, enclosedString.length - 1)];
    return (instruction.transformValue as string).replace(new RegExp(enclosedString), value);
  }
}
