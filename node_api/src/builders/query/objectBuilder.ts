import { QueryObject } from "im-library/dist/types/interfaces/Interfaces";
import { Enums, Vocabulary, Helpers } from "im-library/dist/api";
import axios from "axios";
import EntityService from "@/services/entity.service";

const { DataTypeCheckers } = Helpers;
const { isArrayHasLength, isObjectHasKeys } = DataTypeCheckers;
const { RDFS, IM } = Vocabulary;
const { QueryDisplayType } = Enums;
const entityService = new EntityService(axios);

export async function getQueryObjectByIri(iri: string) {
  const entity = (await entityService.getPartialEntity(iri, [IM.DEFINITION])).data;
  if (!entity[IM.DEFINITION]) return {} as QueryObject;
  return buildQueryObjectFromQuery(JSON.parse(entity[IM.DEFINITION]));
}

export function buildQueryObjectFromQuery(queryAPI: any) {
  const queryUI = {} as QueryObject;
  queryUI.children = [] as QueryObject[];
  buildRecursively(queryAPI, queryUI);
  return queryUI;
}

export function buildQueryObject(label: string, queryAPIObject: any) {
  const queryObject = {
    key: Math.floor(Math.random() * 9999999999999999),
    label: label,
    // type: { firstType: "org.endeavourhealth.imapi.model.tripletree.TTIriRef" },
    // value: { "@id": selected.value["@id"], name: selected.value.name },
    children: [] as QueryObject[],
    selectable: true
  } as QueryObject;
  return queryObject;
}

function buildRecursively(queryAPI: any, queryUI: QueryObject) {
  if (queryAPI != null) {
    Object.keys(queryAPI).forEach(key => {
      if (isPrimitiveType(queryAPI[key])) {
        const queryObject = buildQueryObject(key, queryAPI[key]);
        queryObject.value = queryAPI[key];
        queryUI.children?.push(queryObject);
      } else if (isArray(queryAPI[key])) {
        queryAPI[key].forEach((element: any) => {
          const queryObject = buildQueryObject(key, element);
          queryUI.children?.push(queryObject);
          buildRecursively(element, queryObject);
        });
      } else if (isObject(queryAPI[key])) {
        const queryObject = buildQueryObject(key, queryAPI[key]);
        queryUI.children?.push(queryObject);
        buildRecursively(queryAPI[key], queryObject);
      }
    });
  }
}

function isPrimitiveType(object: any) {
  const primitiveTypes = ["string", "boolean"];
  return primitiveTypes.includes(typeof object);
}

function isObject(object: any) {
  return typeof object === "object";
}

function isArray(object: any) {
  return Array.isArray(object);
}
