package org.endeavourhealth.imapi.logic.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.logic.service.EclService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EclSearchRequest;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.DownloadByQueryOptions;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExcelSearchExporterTest {

    @Mock
    private SearchService searchService;

    @Mock
    private EclService eclService;

    private ExcelSearchExporter excelSearchExporter;

    @BeforeEach
    void setUp() {
        excelSearchExporter = new ExcelSearchExporter(searchService, eclService);
    }

    @Test
    void getSearchAsExcel_QueryRequest_ReturnsWorkbook() throws Exception {
        // Arrange
        DownloadByQueryOptions options = new DownloadByQueryOptions();
        options.setQueryRequest(new QueryRequest());
        options.setTotalCount(10);

        SearchResponse response = new SearchResponse();
        List<SearchResultSummary> entities = new ArrayList<>();
        entities.add(createSummary("iri1", "name1", "code1"));
        response.setEntities(entities);

        when(searchService.queryIMSearch(any(QueryRequest.class))).thenReturn(response);

        // Act
        XSSFWorkbook workbook = excelSearchExporter.getSearchAsExcel(options);

        // Assert
        assertNotNull(workbook);
        Sheet sheet = workbook.getSheet("Search results");
        assertNotNull(sheet);
        assertEquals(1, sheet.getLastRowNum()); // Header + 1 row
    }

    @Test
    void getSearchAsExcel_EclRequest_ReturnsWorkbook() throws Exception {
        // Arrange
        DownloadByQueryOptions options = new DownloadByQueryOptions();
        options.setEclSearchRequest(new EclSearchRequest());
        options.setTotalCount(10);

        SearchResponse response = new SearchResponse();
        List<SearchResultSummary> entities = new ArrayList<>();
        entities.add(createSummary("iri1", "name1", "code1"));
        response.setEntities(entities);

        when(eclService.eclSearch(any(EclSearchRequest.class))).thenReturn(response);

        // Act
        XSSFWorkbook workbook = excelSearchExporter.getSearchAsExcel(options);

        // Assert
        assertNotNull(workbook);
        Sheet sheet = workbook.getSheet("Search results");
        assertNotNull(sheet);
        assertEquals(1, sheet.getLastRowNum());
    }

    @Test
    void getSearchAsExcel_NoRequest_ThrowsException() {
        // Arrange
        DownloadByQueryOptions options = new DownloadByQueryOptions();

        // Act & Assert
        assertThrows(QueryException.class, () -> excelSearchExporter.getSearchAsExcel(options));
    }

    @Test
    void getSearchAsExcel_EmptyEntities_ReturnsEmptyWorkbook() throws Exception {
        // Arrange
        DownloadByQueryOptions options = new DownloadByQueryOptions();
        options.setQueryRequest(new QueryRequest());

        SearchResponse response = new SearchResponse();
        response.setEntities(Collections.emptyList());

        when(searchService.queryIMSearch(any(QueryRequest.class))).thenReturn(response);

        // Act
        XSSFWorkbook workbook = excelSearchExporter.getSearchAsExcel(options);

        // Assert
        assertNotNull(workbook);
        assertNull(workbook.getSheet("Search results"));
    }

    @Test
    void getSearchAsExcel_VariousDataTypes_ReturnsWorkbook() throws Exception {
        // Arrange
        DownloadByQueryOptions options = new DownloadByQueryOptions();
        options.setQueryRequest(new QueryRequest());

        SearchResponse response = new SearchResponse();
        List<SearchResultSummary> entities = new ArrayList<>();
        
        SearchResultSummary summary = createSummary("iri1", "name1", "code1");
        summary.setDescription("desc1\nwith\nnewline");
        summary.setStatus(TTIriRef.iri("statusIri", "Active"));
        summary.setScheme(TTIriRef.iri("schemeIri", "SchemeName"));
        summary.setType(new HashSet<>(Arrays.asList(
            TTIriRef.iri("type1", "Type 1"),
            TTIriRef.iri("type2", "Type 2")
        )));
        
        entities.add(summary);
        response.setEntities(entities);

        when(searchService.queryIMSearch(any(QueryRequest.class))).thenReturn(response);

        // Act
        XSSFWorkbook workbook = excelSearchExporter.getSearchAsExcel(options);

        // Assert
        assertNotNull(workbook);
        Sheet sheet = workbook.getSheet("Search results");
        assertNotNull(sheet);
        assertEquals(1, sheet.getLastRowNum());
        assertEquals("iri1", sheet.getRow(1).getCell(0).getStringCellValue());
        assertEquals("name1", sheet.getRow(1).getCell(1).getStringCellValue());
        assertEquals("code1", sheet.getRow(1).getCell(2).getStringCellValue());
        assertEquals("desc1\nwith\nnewline", sheet.getRow(1).getCell(3).getStringCellValue());
        assertEquals("Active", sheet.getRow(1).getCell(4).getStringCellValue());
        assertEquals("SchemeName", sheet.getRow(1).getCell(5).getStringCellValue());
        
        String typeCell = sheet.getRow(1).getCell(6).getStringCellValue();
        assertTrue(typeCell.contains("Type 1"));
        assertTrue(typeCell.contains("Type 2"));
        assertTrue(typeCell.startsWith("[") && typeCell.endsWith("]"));
    }

    @Test
    void getSearchAsExcel_NullValues_ReturnsWorkbook() throws Exception {
        // Arrange
        DownloadByQueryOptions options = new DownloadByQueryOptions();
        options.setQueryRequest(new QueryRequest());

        SearchResponse response = new SearchResponse();
        List<SearchResultSummary> entities = new ArrayList<>();
        
        SearchResultSummary summary = new SearchResultSummary();
        summary.setIri("iri1");
        // Other fields are null
        
        entities.add(summary);
        response.setEntities(entities);

        when(searchService.queryIMSearch(any(QueryRequest.class))).thenReturn(response);

        // Act
        XSSFWorkbook workbook = excelSearchExporter.getSearchAsExcel(options);

        // Assert
        assertNotNull(workbook);
        Sheet sheet = workbook.getSheet("Search results");
        assertNotNull(sheet);
        assertEquals(1, sheet.getLastRowNum());
        assertEquals("iri1", sheet.getRow(1).getCell(0).getStringCellValue());
        assertEquals("", sheet.getRow(1).getCell(1).getStringCellValue()); // name is null
        assertEquals("", sheet.getRow(1).getCell(2).getStringCellValue()); // code is null
    }

    private SearchResultSummary createSummary(String iri, String name, String code) {
        SearchResultSummary summary = new SearchResultSummary();
        summary.setIri(iri);
        summary.setName(name);
        summary.setCode(code);
        return summary;
    }
}
