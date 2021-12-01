export interface JoinData {
  inputs: TransformInputUpload[];
  instructions: JoinInstruction[];
}

export interface TransformInputUpload {
  id: string;
  inputFile: File;
  inputJson: any[];
  inputDisplayJson: any[];
}

export interface JoinInstruction {
  dataA: string;
  dataB: string;
  propertyA: string;
  propertyB: string;
  joinType: string;
  nestedPropertyName: string;
}

export interface JpathData {
  input: TransformInputUpload;
  jpath: string;
}
