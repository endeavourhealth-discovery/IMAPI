import { isArrayHasLength, isObjectHasKeys } from "../helpers/DataTypeCheckers";

export function getDataModelInstanceDisplay(dataModels: any[]) {
  const instances = [] as any[];
  dataModels.forEach((entity: any) => {
    const instance = {};
    if (isObjectHasKeys(entity)) {
      Object.keys(entity).forEach(key => {
        (instance as any)[key] = "";
      });
    }
    instances.push(instance);
  });
  return instances;
}

export function generateDataModel(inputJson: any[]) {
  const dataModels = [];

  if (isArrayHasLength(inputJson)) {
    const exampleRow = inputJson[0];
    addEntitiesRecursively(dataModels, exampleRow, undefined);
  }

  return dataModels;
}

function addEntitiesRecursively(dataModel: any, exampleRow: any, propertyName: any) {
  const entity = {
    "@id": "string",
    type: "string",
    label: "string"
  };
  if (isObjectHasKeys(exampleRow)) {
    Object.keys(exampleRow).forEach(key => {
      if (isObjectHasKeys(exampleRow[key])) {
        addEntitiesRecursively(dataModel, exampleRow[key], key);
        entity[key] = { "@id": "string" };
      } else {
        entity[key] = typeof exampleRow[key];
      }
    });
  }
  dataModel.push(entity);
}
