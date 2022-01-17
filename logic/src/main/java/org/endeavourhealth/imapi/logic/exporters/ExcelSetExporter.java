package org.endeavourhealth.imapi.logic.exporters;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.EntityRepositoryImpl2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepositoryImpl;
import org.endeavourhealth.imapi.model.CoreLegacyCode;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.TTToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;


/**
 * Manages the Exports concept set data in excel workboook format
 */
@Component
public class ExcelSetExporter {

    private EntityRepositoryImpl2 repo = new EntityRepositoryImpl2();
    private EntityTripleRepository entityTripleRepository = new EntityTripleRepositoryImpl();

    /**
     * Exports  3 excel sheets from the data store
     * <p>Sheet 1 = the definition in ECL</p>
     * <p>Sheet 2 = the expanded core concept set</p>
     * <p>Sheet 3 = the expanded core and legacy code set</p>
     *
     * @param setIri
     * @return
     * @throws DataFormatException
     */
    public XSSFWorkbook getSetAsExcel(String setIri) throws DataFormatException {
        Set<String> predicates = Set.of(RDFS.LABEL.getIri(), IM.DEFINITION.getIri());
        TTEntity entity = entityTripleRepository.getEntityPredicates(setIri, predicates, 0).getEntity();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFFont font = workbook.createFont();
        CellStyle headerStyle = workbook.createCellStyle();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setWrapText(true);
        if (entity.get(IM.DEFINITION) == null) {
            return workbook;
        }
        populateWorkbook(entity, workbook, headerStyle);

        return workbook;
    }

    private void populateWorkbook(TTEntity entity, XSSFWorkbook workbook, CellStyle headerStyle) throws DataFormatException {
        String ecl = TTToECL.getExpressionConstraint(entity.get(IM.DEFINITION), true);
        addEclToWorkbook(ecl, workbook, headerStyle);
        addCoreExpansionToWorkBook(entity, workbook, headerStyle);
        addLegacyExpansionToWorkBook(entity, workbook, headerStyle);
    }

    private void addLegacyExpansionToWorkBook(TTEntity entity, XSSFWorkbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.getSheet("Full expansion");
        if (null == sheet) sheet = workbook.createSheet("Full expansion");
        addHeaders(sheet, headerStyle, "core code", "core term", "extension", "legacy code", "Legacy term", "Legacy scheme");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 20000);
        sheet.setColumnWidth(2, 2500);
        sheet.setColumnWidth(4, 20000);

        Set<CoreLegacyCode> expansion = repo.getSetExpansion(entity, true);
        for (CoreLegacyCode cl : expansion) {
            Row row = addRow(sheet);
            String isLondon = cl.getScheme().getIri().contains("sct#") ? "N" : "Y";
            addCells(row, cl.getCode(), cl.getTerm(), isLondon, cl.getLegacyCode(), cl.getLegacyTerm(), cl.getLegacySchemeName());
        }
        sheet.autoSizeColumn(3);

    }

    private void addCoreExpansionToWorkBook(TTEntity entity, XSSFWorkbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.getSheet("Core expansion");
        if (null == sheet) sheet = workbook.createSheet("Core expansion");
        addHeaders(sheet, headerStyle, "code", "term", "extension");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 20000);
        sheet.setColumnWidth(2, 2500);

        Set<CoreLegacyCode> expansion = repo.getSetExpansion(entity, false);
        for (CoreLegacyCode cl : expansion) {
            Row row = addRow(sheet);
            String isLondon = cl.getScheme().getIri().contains("sct#") ? "N" : "Y";
            addCells(row, cl.getCode(), cl.getTerm(), isLondon);
        }

    }


    private void addEclToWorkbook(String ecl, Workbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.getSheet("ECL set definition");
        if (null == sheet) sheet = workbook.createSheet("ECL set definition");
        addHeaders(sheet, headerStyle, "ECL");
        Row row = addRow(sheet);
        addCells(row, ecl);
        sheet.autoSizeColumn(0);
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

    private Row addRow(Sheet sheet) {
        return sheet.createRow(sheet.getLastRowNum() + 1);
    }

    private void addCells(Row row, String... values) {
        for (String value : values) {
            Cell iriCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum());
            if (value != null) {
                if (value.contains("\n")) {
                    iriCell.getRow()
                            .setHeightInPoints(iriCell.getSheet().getDefaultRowHeightInPoints() * value.split("\n").length);
                }
                iriCell.setCellValue(value);
            }
        }
    }
}
