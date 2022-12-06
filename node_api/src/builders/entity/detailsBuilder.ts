import { TTBundle, TTIriRef } from "im-library/dist/types/interfaces/Interfaces";
import { Vocabulary, Helpers } from "im-library/dist/api";
const { DataTypeCheckers } = Helpers;
const { isObjectHasKeys, isArrayHasLength } = DataTypeCheckers;
const { IM, RDFS, SHACL } = Vocabulary;
import * as crypto from "crypto";

export function buildDetails(definition: TTBundle): any[] {
  const treeNode = { children: [] as any[] };
  buildTreeDataRecursively(treeNode, definition.entity, definition.predicates);
  return treeNode.children!;
}

function buildTreeDataRecursively(treeNode: any, entity: any, predicates: any) {
  if (isObjectHasKeys(entity)) {
    for (const key of Object.keys(entity)) {
      if (key === IM.ROLE_GROUP) {
        addRoleGroup(treeNode, entity, predicates, key);
      } else if (key === IM.HAS_TERM_CODE) {
        addTermCodes(treeNode, entity, predicates, key);
      } else if (key === SHACL.PROPERTY) {
        addProperty(treeNode, entity, predicates, key);
      } else if (key === IM.HAS_MAP) {
        const defaultNode = { key: key, label: predicates[key], children: [] };
        treeNode.children.push(defaultNode);
        addDefault(defaultNode, entity, predicates, key);
      } else if (key !== "@id") {
        const newTreeNode = { key: key, label: predicates[key], children: [] };
        treeNode.children?.push(newTreeNode);
        buildTreeDataRecursively(newTreeNode, entity[key], predicates);
      }
    }
  } else if (isArrayHasLength(entity)) {
    for (const item of entity) {
      addIriLink(treeNode, item);
    }
  } else {
    addValueToLabel(treeNode, ": ", entity);
  }
}

function addValueToLabel(treeNode: any, divider: string, value: any) {
  treeNode.label += divider + value;
}

function addIriLink(treeNode: any, item: TTIriRef) {
  if (item["@id"] === IM.NAMESPACE + "loadMore")
    treeNode.children?.push({ key: item["@id"], label: item.name, type: "loadMore", data: { predicate: treeNode.key, totalCount: (item as any).totalCount } });
  else treeNode.children?.push({ key: item["@id"], label: item.name, type: "link" });
}

function addDefault(treeNode: any, entity: any, predicates: any, key: string | number) {
  if (isArrayHasLength(entity[key])) {
    for (const [index, item] of [entity[key]].entries()) {
      if (isObjectHasKeys(item[index], ["@id", "name"])) {
        addIriLink(treeNode, item[index]);
      } else {
        addDefault(treeNode, item, predicates, index);
      }
    }
  } else if (isObjectHasKeys(entity[key])) {
    for (const objectKey of Object.keys(entity[key])) {
      const objectNode = {
        key: String(crypto.randomBytes(64).readBigUInt64BE()),
        label: predicates[objectKey] || objectKey,
        children: [] as any
      };

      treeNode.children.push(objectNode);
      addDefault(objectNode, entity[key], predicates, objectKey);
    }
  } else {
    addValueToLabel(treeNode, " - ", entity[key]);
  }
}

function addTermCodes(treeNode: any, entity: any, predicates: any, key: string) {
  const newTreeNode = { key: key, label: predicates[key] || entity[key]?.path?.[0]?.name || key, children: [] as any[] };
  treeNode.children?.push(newTreeNode);
  if (isArrayHasLength(entity[key])) {
    for (const termCode of entity[key]) {
      const termCodeNode = {
        key: termCode[IM.CODE],
        label: termCode[RDFS.LABEL] + " - " + termCode[IM.CODE],
        children: [] as any[]
      };
      newTreeNode.children.push(termCodeNode);
    }
  }
}

function addRoleGroup(treeNode: any, entity: any, predicates: any, key: string) {
  const newTreeNode = { key: key, label: predicates[key] || entity[key]?.path?.[0]?.name || key, children: [] as any[] };
  treeNode.children?.push(newTreeNode);
  if (isArrayHasLength(entity[key])) {
    for (const roleGroup of entity[key]) {
      const propertyNode = {
        key: IM.NAMESPACE + "groupNumber" + roleGroup[IM.NAMESPACE + "groupNumber"],
        label: "role group " + roleGroup[IM.NAMESPACE + "groupNumber"],
        children: [] as any[]
      };
      newTreeNode.children?.push(propertyNode);

      for (const roleKey of Object.keys(roleGroup)) {
        if (roleKey !== IM.NAMESPACE + "groupNumber")
          propertyNode.children?.push({
            key: key + "." + roleKey,
            label: predicates[roleKey],
            data: roleGroup[roleKey]?.[0],
            type: "property"
          });
      }
    }
  }
}

function addProperty(treeNode: any, entity: any, predicates: any, key: string) {
  const newTreeNode = { key: key, label: predicates[key] || entity[key]?.path?.[0]?.name || key, children: [] as any[] };
  treeNode.children?.push(newTreeNode);
  if (isArrayHasLength(entity[key])) {
    for (const property of entity[key]) {
      const propertyNode = {
        key: property?.[SHACL.PATH]?.[0]?.["@id"] || key,
        label: property?.[SHACL.PATH]?.[0]?.name || predicates[key] || key,
        children: [] as any[]
      };
      newTreeNode.children?.push(propertyNode);

      for (const propertyKey of Object.keys(property)) {
        if (SHACL.NAMESPACE + "order" === propertyKey) {
          propertyNode.children?.push({
            key: key + "." + propertyKey,
            label: predicates[propertyKey] + ": " + property[propertyKey]
          });
        } else {
          propertyNode.children?.push({
            key: key + "." + propertyKey,
            label: predicates[propertyKey],
            data: property[propertyKey]?.[0],
            type: "property"
          });
        }
      }
    }
  }
}
