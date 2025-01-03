package org.endeavourhealth.imapi.logic.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.set.SetOptions;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.logic.exporters.helpers.ExporterHelpers.*;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class SetTextFileExporter {
  public static final String IM_1_ID = "im1id";
  private static final Logger LOG = LoggerFactory.getLogger(SetTextFileExporter.class);

  private static void addHeader(boolean includeIM1id, boolean includeSubsets, StringJoiner results, String del) {
    String headerLine = "code" + del + "term" + del + "status" + del + "scheme" + del + "usage" + del + "extension" + del + "set";
    if (includeSubsets) headerLine += del + "subset" + del + "subsetIri" + del + "subsetVersion";
    if (includeIM1id) headerLine += del + IM_1_ID;
    results.add(headerLine);
  }

  public byte[] generateFile(String fileType, Set<Concept> members, String setName, boolean includeIM1id, boolean includeSubsets) throws IOException {
    LOG.trace("Generating output...");
    switch (fileType) {
      case "tsv" -> {
        return getTextFile("\t", members, setName, includeIM1id, includeSubsets).getBytes();
      }
      case "csv" -> {
        return getTextFile(",", members, setName, includeIM1id, includeSubsets).getBytes();
      }
      case "xlsx" -> {
        String fileContent = getTextFile("\t", members, setName, includeIM1id, includeSubsets);
        return getExcel(fileContent);
      }
    }
    return null;
  }

  private String getTextFile(String del, Set<Concept> members, String setName, boolean includeIM1id, boolean includeSubsets) {
    StringJoiner results = new StringJoiner(System.lineSeparator());
    addHeader(includeIM1id, includeSubsets, results, del);

    for (Concept member : members) {
      addConcept(results, member, setName, includeIM1id, includeSubsets, del);

      if (member.getMatchedFrom() != null) {
        for (Concept matchedFrom : member.getMatchedFrom())
          addConcept(results, matchedFrom, setName, includeIM1id, includeSubsets, del);
      }
    }
    return results.toString();
  }

  private byte[] getExcel(String csvString) throws IOException {
    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("Sheet1");
      String[] lines = csvString.split("\n");
      int rowNum = 0;

      for (String line : lines) {
        Row row = sheet.createRow(rowNum++);
        StringTokenizer st = new StringTokenizer(line, "\t");

        int cellNum = 0;
        while (st.hasMoreTokens()) {
          String cellValue = st.nextToken();
          Cell cell = row.createCell(cellNum++);
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
        throw new RuntimeException(e);
      }
    }
  }

  private void addConcept(StringJoiner results, Concept member, String setName, boolean includeIM1id, boolean includeSubsets, String del) {
    String scheme = member.getScheme().getName();
    String usage = getUsage(member);
    String subSet = getSubSet(member);
    String subsetIri = getSubsetIri(member);
    String subsetVersion = setSubsetVersion(member);
    String status = getStatus(member);
    String isExtension = member.getScheme().getIri().contains("sct#") ? "N" : "Y";
    String[] valueArray = {member.getCode(), member.getName(), status, scheme, usage, isExtension, setName};
    for (int i = 0; i < valueArray.length; i++) {
      if (valueArray[i] == null) {
        valueArray[i] = "";
      }
    }
    ArrayList<String> values = new ArrayList<>(List.of(valueArray));
    if (includeSubsets) values.addAll(List.of(subSet, subsetIri, subsetVersion));
    if (includeIM1id) values.add(member.getIm1Id());
    addLineData(del, results, values.toArray(new String[0]));
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
