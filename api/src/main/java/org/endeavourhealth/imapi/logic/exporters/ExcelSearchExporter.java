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
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.search.DownloadOptions;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

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

    public XSSFWorkbook getSearchAsExcel(DownloadOptions downloadOptions) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException, DataFormatException {
        SearchResponse searchResponse = null;
        if (null != downloadOptions.getQueryRequest()) {
            QueryRequest queryRequest = downloadOptions.getQueryRequest();
            queryRequest.setPage(new Page().setPageNumber(1).setPageSize(downloadOptions.getTotalCount()));
            searchResponse = searchService.queryIMSearch(downloadOptions.getQueryRequest());
        } else if (null != downloadOptions.getEclSearchRequest()) {
            EclSearchRequest eclSearchRequest = downloadOptions.getEclSearchRequest();
            eclSearchRequest.setPage(1);
            eclSearchRequest.setSize(downloadOptions.getTotalCount());
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
        addHeaders(sheet, headerStyle, "Iri" ,"Name", "Code", "Description", "Status", "Scheme", "Type");
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
        addCellValue(row, entity.getEntityType());
    }

    private void addCellValue(Row row, Object value) {
        if (value instanceof String) {
            Cell stringCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum(), CellType.STRING);
            if (((String) value).contains("\n")) {
                stringCell.getRow()
                    .setHeightInPoints(stringCell.getSheet().getDefaultRowHeightInPoints() * ((String) value).split("\n").length);
            }
            stringCell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            Cell intCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum(), CellType.NUMERIC);
            intCell.setCellValue((Integer) value);
        } else if (value instanceof TTIriRef) {
            Cell ttIriRefCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum(), CellType.STRING);
            ttIriRefCell.setCellValue(iriToString((TTIriRef) value,false));
        } else if (value instanceof ArrayList<?>) {
            Cell arrayCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum(), CellType.STRING);
            arrayCell.setCellValue(listToString((List<?>) value));
        } else if (value instanceof Set<?>) {
            Cell arrayCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum(), CellType.STRING);
            arrayCell.setCellValue(setToString((Set<?>) value));
        } else if (null == value) {
            Cell intCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum(), CellType.STRING);
            intCell.setCellValue("");
        } else {
            Cell iriCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum(), CellType.STRING);
            iriCell.setCellValue("UNHANDLED TYPE");
        }
    }

    private String listToString(List<?> list) {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (Object item : list) {
            addItemToJoiner(item, stringJoiner, list.size() > 1);
        }
        if (list.size() > 1) return "[" + stringJoiner.toString() + "]";
        return stringJoiner.toString();
    }

    private String setToString(Set<?> set) {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (Object item : set) {
            addItemToJoiner(item, stringJoiner, set.size() > 1);
        }
        if (set.size() > 1) return "[" + stringJoiner.toString() + "]";
        return stringJoiner.toString();
    }

    private void addItemToJoiner(Object item, StringJoiner stringJoiner, boolean inArray) {
        if (item instanceof String) stringJoiner.add((String) item);
        else if (item instanceof Integer) {
            Integer itemAsInteger = (Integer) item;
            stringJoiner.add(itemAsInteger.toString());
        } else if (item instanceof TTIriRef) {
            stringJoiner.add(iriToString((TTIriRef) item, inArray));
        } else if (item instanceof ArrayList<?>) {
            stringJoiner.add(listToString((List<?>) item));
        } else if (item instanceof Set<?>) {
            stringJoiner.add(setToString((Set<?>) item));
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
