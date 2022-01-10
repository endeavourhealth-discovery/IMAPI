import alasql from "alasql";
import { JoinData, JoinInstruction, TransformInputUpload } from "../models";
import { generateDataModel } from "./DataModelService";

export function join(body: JoinData) {
  body.instructions.forEach(currentInstr => {
    const joinedTransform = currentInstr.joinType === "nested" ? joinNested(currentInstr, body.inputs) : joinFlat(currentInstr, body.inputs); // join dataA with dataB
    body.inputs = body.inputs.filter(input => input.id !== currentInstr.dataA && input.id !== currentInstr.dataB); // remove dataA and dataB from inputs
    body.inputs.push(joinedTransform); // add joinedData to inputs
    body.instructions = getUpdatedInstructions(body, currentInstr, joinedTransform); // replace instructions with dataA/dataB.id with joinedData.id
  });
  return body.inputs;
}

function getJoinedTransform(instr: JoinInstruction, joinedJson: any[], joinedDisplayJson: any[]) {
  return {
    id: instr.dataA + instr.dataB + Date.now(),
    inputJson: joinedJson,
    inputDisplayJson: joinedDisplayJson,
    inputFile: { name: "Joined-" + Date.now() + ".json", lastModified: Date.now() } as File,
    dataModel: generateDataModel(joinedJson)
  } as TransformInputUpload;
}

function joinNested(instr: JoinInstruction, inputs: TransformInputUpload[]) {
  const dataA = getInputById(instr.dataA, inputs);
  const dataB = getInputById(instr.dataB, inputs);
  dataA.inputJson.forEach(inputRow => {
    inputRow[instr.nestedPropertyName] = getRowByProperty(dataB.inputJson, instr.propertyB, inputRow[instr.propertyA]);
  });

  dataA.inputDisplayJson.forEach(inputRow => {
    inputRow[instr.nestedPropertyName] = getRowByProperty(dataB.inputDisplayJson, instr.propertyB, inputRow[instr.propertyA]);
  });

  return getJoinedTransform(instr, dataA.inputJson, dataA.inputDisplayJson);
}

function getRowByProperty(rows: any[], property: string, value: string) {
  return rows.find(row => row[property] === value);
}

function joinFlat(instr: JoinInstruction, inputs: TransformInputUpload[]) {
  const dataA = getInputById(instr.dataA, inputs);
  const dataB = getInputById(instr.dataB, inputs);
  const joinedJson = alasql("SELECT * FROM ? dataA \
      LEFT JOIN ? dataB ON dataA." + instr.propertyA + "= dataB." + instr.propertyB, [
    dataA.inputJson,
    dataB.inputJson
  ]);
  const joinedDisplayJson = alasql("SELECT * FROM ? dataA \
    LEFT JOIN ? dataB ON dataA." + instr.propertyA + "= dataB." + instr.propertyB, [
    dataA.inputDisplayJson,
    dataB.inputDisplayJson
  ]);
  return getJoinedTransform(instr, joinedJson, joinedDisplayJson);
}

function getInputById(id: string, inputs: TransformInputUpload[]) {
  return inputs.find(ipnut => ipnut.id === id);
}

function getUpdatedInstructions(body: JoinData, currentInstr: JoinInstruction, joinedTransform: TransformInputUpload): JoinInstruction[] {
  const copyOfInstr = [...body.instructions];
  const currentInstrDataA = currentInstr.dataA;
  const currentInstrDataB = currentInstr.dataB;
  copyOfInstr.forEach(instruction => {
    if (instruction.dataA === currentInstrDataA) {
      instruction.dataA = joinedTransform.id;
    }
    if (instruction.dataA === currentInstrDataB) {
      instruction.dataA = joinedTransform.id;
    }
    if (instruction.dataB === currentInstrDataA) {
      instruction.dataB = joinedTransform.id;
    }
    if (instruction.dataB === currentInstrDataB) {
      instruction.dataB = joinedTransform.id;
    }
  });
  return copyOfInstr;
}
