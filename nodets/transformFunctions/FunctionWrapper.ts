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
}
