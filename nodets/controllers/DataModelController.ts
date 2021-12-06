import { isArrayHasLength, isObjectHasKeys } from "../helpers/DataTypeCheckers";

export function getDataModelInstanceDisplay(ttdocument: any) {
  const instances = [] as any[];
  ttdocument.entities.forEach((entity: any) => {
    const instance = {};
    if (isObjectHasKeys(entity)) {
      Object.keys(entity).forEach(key => {
        switch (key) {
          case "rdf:type":
            (instance as any)[key] = entity["rdfs:label"];
            break;
          case "sh:property":
            entity["sh:property"].forEach((property: any) => {
              const propertyName: string = property["sh:path"]?.name || property["sh:path"]["@id"];
              const value = "sh:node" in property ? { "@id": null } : null;
              (instance as any)[propertyName] = value;
            });
            break;

          default:
            (instance as any)[key] = "";
            break;
        }
      });
    }
    instances.push(instance);
  });
  return instances;
}

export function generateDataModel(inputJson: any[]) {
  const dataModel = {
    "@context": {
      rdf: "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
      im: "http://endhealth.info/im#",
      graph: "http://graph#",
      owl: "http://www.w3.org/2002/07/owl#",
      sh: "http://www.w3.org/ns/shacl#",
      rdfs: "http://www.w3.org/2000/01/rdf-schema#"
    },
    "@graph": "graph:graph",
    entities: []
  };

  if (isArrayHasLength(inputJson)) {
    const exampleRow = inputJson[0];
    addEntitiesRecursively(dataModel, exampleRow, undefined);
  }

  return dataModel;
}

function addEntitiesRecursively(dataModel: any, exampleRow: any, propertyName: any) {
  const entity = {
    "@id": propertyName ? "http://graph#" + propertyName : "http://graph#MainEntity",
    "rdf:type": [
      {
        "@id": "sh:NodeShape"
      }
    ],
    "rdfs:label": propertyName ? propertyName : "MainEntity"
  };
  if (isObjectHasKeys(exampleRow)) {
    Object.keys(exampleRow).forEach(key => {
      if (isObjectHasKeys(exampleRow[key])) {
        addEntitiesRecursively(dataModel, exampleRow[key], key);
        entity["im:" + key] = { "@id": "http://graph#" + key };
      } else {
        entity["im:" + key] = typeof exampleRow[key];
      }
    });
  }
  dataModel.entities.push(entity);
}
