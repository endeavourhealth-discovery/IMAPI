package org.endeavourhealth.imapi.logic.exporters;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.errorhandling.GeneralCustomException;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static org.endeavourhealth.imapi.logic.exporters.helpers.ExporterHelpers.*;

@Slf4j
public class SetTextFileExporter {
  public static final String IM_1_ID = "im1id";

  private static void addHeader(boolean includeIM1id, boolean includeSubsets, boolean includeLegacy, boolean subsumption,StringJoiner results, String del) {
    String headerLine="";
    if (subsumption) headerLine = "subsumed"+del;
    headerLine += "code" + del + "term" + del + "status" + del + "scheme" + del + "usage" + del + "extension" + del + "set";
    if (includeSubsets) headerLine += del + "subset" + del + "subsetIri" + del + "subsetVersion";
    if (includeIM1id) headerLine += del + IM_1_ID;
    if (includeLegacy) headerLine += del + "legacyCode" + del + "legacyTerm" + del + "legacyScheme" + del + "codeId"+del+"legacyAltCode";
    results.add(headerLine);
  }

  public byte[] generateFile(String fileType, Set<Concept> members, String setName, boolean includeIM1id, boolean includeSubsets, boolean includeLegacy,
  List<String> subsumption,String ecl) throws IOException, GeneralCustomException {
    if (subsumption==null) subsumption=new ArrayList<>();
    log.trace("Generating output...");
    switch (fileType) {
      case "tsv" -> {
        return getTextFile("\t", members, setName, includeIM1id, includeSubsets, includeLegacy,subsumption).getBytes();
      }
      case "csv" -> {
        return getTextFile(",", members, setName, includeIM1id, includeSubsets, includeLegacy,subsumption).getBytes();
      }
      case "xlsx" -> {
        String fileContent = getTextFile("\t", members, setName, includeIM1id, includeSubsets, includeLegacy,subsumption);
        return getExcel(fileContent,ecl);
      }
      default -> {
        return new byte[0];
      }
    }
  }

  private boolean hasSubsets(Set<Concept> members){
    for (Concept member : members) {
      if (member.getIsContainedIn()!=null)
        return true;
    }
    return false;
  }

  private String getTextFile(String del, Set<Concept> members, String setName, boolean includeIM1id, boolean includeSubsets, boolean includeLegacy,List<String> subsumption) {
    StringJoiner results = new StringJoiner(System.lineSeparator());
    if (includeSubsets)
      includeSubsets = hasSubsets(members);
    addHeader(includeIM1id, includeSubsets, includeLegacy, !subsumption.isEmpty(),results, del);
    for (Concept member : members) {
      addConcept(results, member, setName, includeIM1id, includeSubsets, !subsumption.isEmpty(), del);

    }
    return results.toString();
  }
  private CellStyle createTextStyle(Workbook workbook) {
    CellStyle textStyle = workbook.createCellStyle();
    DataFormat format = workbook.createDataFormat();
    textStyle.setDataFormat(format.getFormat("@")); // Excel "Text" type
    return textStyle;
  }

  private byte[] getExcel(String csvString,String ecl) throws IOException, GeneralCustomException {
    try (Workbook workbook = new XSSFWorkbook()) {
      if (ecl != null && !ecl.isEmpty()) {
        Sheet sheet = workbook.createSheet("ECLDefinition");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("ECL");
        row= sheet.createRow(1);
        row.createCell(0).setCellValue(ecl);
      }
      Sheet sheet = workbook.createSheet("Expansion");
      CellStyle textStyle = createTextStyle(workbook);
      String[] lines = csvString.split("\n");
      int rowNum = 0;

      for (String line : lines) {
        Row row = sheet.createRow(rowNum++);
        StringTokenizer st = new StringTokenizer(line, "\t");
        int cellNum = 0;
        while (st.hasMoreTokens()) {
          String cellValue = st.nextToken();
          Cell cell = row.createCell(cellNum++,CellType.STRING);
          cell.setCellStyle(textStyle);
          cellValue = cellValue.replaceAll("\r\n|\n|\t", "");
          cellValue = cellValue.trim();
          cell.setCellValue(cellValue);
        }
      }

      for (int colNum = 0; colNum < sheet.getRow(0).getLastCellNum(); colNum++) {
        sheet.autoSizeColumn(colNum);
      }

      try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
      } catch (IOException e) {
        throw new GeneralCustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
  }

  private void addConcept(StringJoiner results, Concept member, String setName, boolean includeIM1id, boolean includeSubsets,
                          boolean includeSubsumed, String del) {
    String scheme = member.getScheme().getName();
    String usage = getUsage(member);
    String subSet = getSubSet(member);
    String subsetIri = getSubsetIri(member);
    String subsetVersion = setSubsetVersion(member);
    String status = getStatus(member);
    String isExtension = member.getScheme().getIri().contains("sct#") ? "N" : "Y";
    List<String> valueArray;
    if (includeSubsumed)
      valueArray= List.of(member.isSubsumed() ?"Y":"N",member.getCode(), member.getName(), status, scheme, usage, isExtension, setName);
    else valueArray= List.of(member.getCode(), member.getName(), status, scheme, usage, isExtension, setName);

    ArrayList<String> values = new ArrayList<>(valueArray);
    for (int i = 0; i < values.size(); i++) {
      if (values.get(i) == null) {
        values.set(i,"");
      }
    }
    if (includeSubsets) values.addAll(List.of(subSet, subsetIri, subsetVersion));
    if (includeIM1id) values.add(member.getIm1Id());
    if (member.getMatchedFrom() == null) {
      addLineData(del, results, values.toArray(new String[0]));
      return;
    } else {
      for (Concept matchedFrom : member.getMatchedFrom()) {
        List<String> combined = new ArrayList<>(values);
        String[] legacyArray = addLegacy(matchedFrom);
        combined.addAll(Stream.of(legacyArray).toList());
        addLineData(del, results, combined.toArray(new String[0]));
      }
    }
  }

  private String[] addLegacy(Concept legacyMember) {

    String[] valueArray = {legacyMember.getCode(), legacyMember.getName(), legacyMember.getScheme().getIri(),
      legacyMember.getCodeId(),legacyMember.getAlternativeCode()};
    for (int i = 0; i < valueArray.length; i++) {
      if (valueArray[i] == null) {
        valueArray[i] = "";
      }
    }
    return valueArray;
  }

  private String[] addSubsumed(StringJoiner results, Concept subsumedMember, String del) {

    String[] valueArray = {subsumedMember.getCode(), subsumedMember.getName(),
      subsumedMember.getCodeId()};
    for (int i = 0; i < valueArray.length; i++) {
      if (valueArray[i] == null) {
        valueArray[i] = "";
      }
    }
    return valueArray;
  }

  private void addLineData(String del, StringJoiner results, String... values) {
    StringJoiner line = new StringJoiner(del);
    for (CharSequence value : values) {
      if (",".equals(del)) line.add("\"" + value + "\"");
      else line.add(value);
    }
    results.add(line.toString());
  }
}
