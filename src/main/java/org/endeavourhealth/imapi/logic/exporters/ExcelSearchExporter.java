package org.endeavourhealth.imapi.logic.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.logic.service.EclService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EclSearchRequest;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.DownloadByQueryOptions;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

public class ExcelSearchExporter {
  private XSSFWorkbook workbook;
  private CellStyle headerStyle;
  private SearchService searchService = new SearchService();
  private EclService eclService = new EclService();

  public ExcelSearchExporter() {
    workbook = new XSSFWorkbook();
    XSSFFont font = workbook.createFont();
    headerStyle = workbook.createCellStyle();
    font.setBold(true);
    headerStyle.setFont(font);
    headerStyle.setWrapText(true);
  }

  public XSSFWorkbook getSearchAsExcel(DownloadByQueryOptions downloadByQueryOptions) throws OpenSearchException, JsonProcessingException, QueryException {
    SearchResponse searchResponse = null;
    if (null != downloadByQueryOptions.getQueryRequest()) {
      QueryRequest queryRequest = downloadByQueryOptions.getQueryRequest();
      queryRequest.setPage(new Page().setPageNumber(1).setPageSize(downloadByQueryOptions.getTotalCount()));
      searchResponse = searchService.queryIMSearch(downloadByQueryOptions.getQueryRequest());
    } else if (null != downloadByQueryOptions.getEclSearchRequest()) {
      EclSearchRequest eclSearchRequest = downloadByQueryOptions.getEclSearchRequest();
      eclSearchRequest.setPage(1);
      eclSearchRequest.setSize(downloadByQueryOptions.getTotalCount());
      searchResponse = eclService.eclSearch(eclSearchRequest);
    }
    if (null == searchResponse) throw new QueryException("Download must have either queryRequest or eclSearchRequest");
    List<SearchResultSummary> entities = searchResponse.getEntities();
    if (null == entities || entities.isEmpty()) return workbook;
    addSearchResultsToWorkbook(entities);
    return workbook;
  }

  private void addSearchResultsToWorkbook(List<SearchResultSummary> entities) {
    Sheet sheet = workbook.getSheet("Search results");
    if (null == sheet) sheet = workbook.createSheet("Search results");
    addHeaders(sheet, headerStyle, "Iri", "Name", "Code", "Description", "Status", "Scheme", "Type");
    setColumnWidths(sheet, List.of(10000, 20000, 5000, 20000, 5000, 10000, 10000));
    for (SearchResultSummary entity : entities) {
      addSearchResultToWorkbook(entity, sheet);
    }
  }

  private String iriToString(TTIriRef iri, boolean inArray) {
    if (inArray) return "\"" + iri.getName() + "\"";
    else return iri.getName();
  }

  private void addSearchResultToWorkbook(SearchResultSummary entity, Sheet sheet) {
    Row row = addRow(sheet);
    addCellValue(row, entity.getIri());
    addCellValue(row, entity.getName());
    addCellValue(row, entity.getCode());
    addCellValue(row, entity.getDescription());
    addCellValue(row, entity.getStatus());
    addCellValue(row, entity.getScheme());
    addCellValue(row, entity.getType());
  }

  private int getLastCellNum(Row row) {
    return row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();
  }

  private void addCellValue(Row row, Object value) {
    switch (value) {
      case String valueAsString -> {
        Cell stringCell = row.createCell(getLastCellNum(row), CellType.STRING);
        if (valueAsString.contains("\n")) {
          stringCell.getRow()
            .setHeightInPoints(stringCell.getSheet().getDefaultRowHeightInPoints() * valueAsString.split("\n").length);
        }
        stringCell.setCellValue(valueAsString);
      }
      case Integer valueAsInteger -> {
        Cell intCell = row.createCell(getLastCellNum(row), CellType.NUMERIC);
        intCell.setCellValue(valueAsInteger);
      }
      case TTIriRef valueAsTTIriRef -> {
        Cell ttIriRefCell = row.createCell(getLastCellNum(row), CellType.STRING);
        ttIriRefCell.setCellValue(iriToString(valueAsTTIriRef, false));
      }
      case ArrayList<?> valueAsList -> {
        Cell arrayCell = row.createCell(getLastCellNum(row), CellType.STRING);
        arrayCell.setCellValue(listToString(valueAsList));
      }
      case Set<?> valueAsSet -> {
        Cell arrayCell = row.createCell(getLastCellNum(row), CellType.STRING);
        arrayCell.setCellValue(setToString(valueAsSet));
      }
      case null -> {
        Cell intCell = row.createCell(getLastCellNum(row), CellType.STRING);
        intCell.setCellValue("");
      }
      default -> {
        Cell iriCell = row.createCell(getLastCellNum(row), CellType.STRING);
        iriCell.setCellValue("UNHANDLED TYPE");
      }
    }
  }

  private String listToString(List<?> list) {
    StringJoiner stringJoiner = new StringJoiner(",");
    for (Object item : list) {
      addItemToJoiner(item, stringJoiner, list.size() > 1);
    }
    if (list.size() > 1) return "[" + stringJoiner + "]";
    return stringJoiner.toString();
  }

  private String setToString(Set<?> set) {
    StringJoiner stringJoiner = new StringJoiner(",");
    for (Object item : set) {
      addItemToJoiner(item, stringJoiner, set.size() > 1);
    }
    if (set.size() > 1) return "[" + stringJoiner + "]";
    return stringJoiner.toString();
  }

  private void addItemToJoiner(Object item, StringJoiner stringJoiner, boolean inArray) {
    if (item instanceof String itemAsString) stringJoiner.add(itemAsString);
    else if (item instanceof Integer itemAsInteger) {
      stringJoiner.add(itemAsInteger.toString());
    } else if (item instanceof TTIriRef itemAsTTIriRef) {
      stringJoiner.add(iriToString(itemAsTTIriRef, inArray));
    } else if (item instanceof ArrayList<?> itemAsList) {
      stringJoiner.add(listToString(itemAsList));
    } else if (item instanceof Set<?> itemAsSet) {
      stringJoiner.add(setToString(itemAsSet));
    }
  }

  private Row addRow(Sheet sheet) {
    return sheet.createRow(sheet.getLastRowNum() + 1);
  }

  private void addHeaders(Sheet sheet, CellStyle headerStyle, String... names) {
    Row header = sheet.createRow(0);

    int i = 0;
    for (String name : names) {
      sheet.setColumnWidth(i, 10000);
      Cell headerCell = header.createCell(i);
      headerCell.setCellValue(name);
      headerCell.setCellStyle(headerStyle);
      i++;
    }
  }

  private void setColumnWidths(Sheet sheet, List<Integer> widths) {
    for (Integer width : widths) {
      sheet.setColumnWidth(widths.indexOf(width), width);
    }
  }
}
