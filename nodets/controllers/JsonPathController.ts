import jp, { PathComponent } from "jsonpath";
import { PathOption, TransformInputUpload } from "../models";
import { generateDataModel } from "./DataModelController";

export function getInputFromJpath(input: TransformInputUpload, jsonPath: string) {
  const inputAfter = input;
  try {
    inputAfter.inputJson = jp.query(input.inputJson, jsonPath);
    inputAfter.inputDisplayJson = jp.query(input.inputDisplayJson, jsonPath);
    inputAfter.dataModel = generateDataModel(inputAfter.inputJson);
  } catch (error) {
    console.log(error);
    input.inputDisplayJson = [];
    input.inputJson = [];
    return input;
  }

  return inputAfter;
}

export function getJsonPathOptions(input: any) {
  return jp.paths(input, "$..*");
}

export function getJpathTreeOptions(input: any) {
  const pathOptions = [] as PathOption[];
  const paths = getJsonPathOptions(input);

  paths.forEach(path => {
    const node = { key: "$", label: "$", data: ["$"], children: [] as PathOption[] };
    path.shift();
    addPathRecursively(pathOptions, node, path);

    const found = pathOptions.find(pathOption => pathOption.label === "$");
    if (found) {
      found.children.push(...node.children);
    } else {
      pathOptions.push(node);
    }
  });
  joinPathOptions(pathOptions);
  return pathOptions;
}

function addPathRecursively(pathOptions: PathOption[], parentNode: any, propertyList: PathComponent[]) {
  if (propertyList.length) {
    const property = propertyList[0];
    const data = [];
    data.push(...parentNode.data);
    data.push(property);
    const childNode = { key: jp.stringify(data), label: property, data: data, children: [] } as PathOption;
    propertyList.shift();
    if (propertyList.length) {
      addPathRecursively(pathOptions, childNode, propertyList);
    }
    parentNode.children.push(childNode);
  }
}

function joinPathOptions(pathOptions: PathOption[]) {
  const levelKeys = jp.query(pathOptions, "$.*.key");
  const keySet = new Set(levelKeys);

  keySet.forEach(key => {
    const optionsToJoin = pathOptions.filter(pathOption => pathOption.key === key);

    if (optionsToJoin.length) {
      const restOptions = pathOptions.filter(pathOption => pathOption.key !== key);
      pathOptions.length = 0;
      const joined = optionsToJoin.shift();
      optionsToJoin.forEach(option => {
        joined.children.push(...option.children);
      });
      pathOptions.push(...restOptions);
      pathOptions.push(joined);
    }
  });

  pathOptions.forEach(pathOption => {
    if (pathOption.children.length) {
      joinPathOptions(pathOption.children);
    }
  });
}
