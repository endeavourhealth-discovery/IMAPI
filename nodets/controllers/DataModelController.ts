import { isArrayHasLength, isObjectHasKeys } from "../helpers/DataTypeCheckers";

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
    const entity = {
      "@id": "http://graph#MainEntity",
      "rdf:type": [
        {
          "@id": "sh:NodeShape"
        }
      ],
      "rdfs:label": "MainEntity"
    };
    if (isObjectHasKeys(exampleRow)) {
      Object.keys(exampleRow).forEach(key => {
        entity["im:" + key] = typeof exampleRow[key];
      });
    }
    dataModel.entities.push(entity);
  }

  return dataModel;
}
