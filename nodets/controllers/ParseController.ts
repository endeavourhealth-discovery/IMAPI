export function getJsonFromFile(fileString: string): any[] {
  const lines = fileString.split("\n");
  const jsonArray = [] as any[];
  const headers = lines[0].split(",").map(header => header.replace(/"+/g, "").trim());

  for (var i = 1; i < lines.length; i++) {
    const obj = {} as any;
    const currentline = lines[i].split(",");

    for (var j = 0; j < headers.length; j++) {
      obj[headers[j]] = currentline[j] ? currentline[j].replace(/"+/g, "").trim() : "";
    }

    jsonArray.push(obj);
  }
  return jsonArray;
}
