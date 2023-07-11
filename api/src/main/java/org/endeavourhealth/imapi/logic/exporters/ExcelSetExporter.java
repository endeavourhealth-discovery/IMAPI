package org.endeavourhealth.imapi.logic.exporters;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.IMLToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;


/**
 * Manages the Exports concept set data in excel workboook format
 */
@Component
public class ExcelSetExporter {

    private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();

    private SetExporter setExporter = new SetExporter();

    private XSSFWorkbook workbook;
    private CellStyle headerStyle;

    public ExcelSetExporter() {
        workbook = new XSSFWorkbook();
        XSSFFont font = workbook.createFont();
        headerStyle = workbook.createCellStyle();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setWrapText(true);
    }

    /**
     * Exports  3 excel sheets from the data store
     * <p>Sheet 1 = the definition in ECL</p>
     * <p>Sheet 2 = the expanded core concept set</p>
     * <p>Sheet 3 = the expanded core and legacy code set</p>
     *
     * @param setIri iri of the set
     * @return work book
     */
    public XSSFWorkbook getSetAsExcel(String setIri, boolean core, boolean legacy,boolean flat) throws DataFormatException, JsonProcessingException, QueryException {
        TTEntity entity = entityTripleRepository.getEntityPredicates(setIri, Set.of(IM.DEFINITION.getIri())).getEntity();
        if (entity.getIri() == null || entity.getIri().isEmpty())
            return workbook;

        String ecl = getEcl(entity);
        if(ecl != null) {
            addDefinitionToWorkbook(ecl);
        }

        if (core || legacy) {
            Set<Concept> members = setExporter.getExpandedSetMembers(setIri, legacy);

            if (core) {
                addCoreExpansionToWorkBook(members,flat);
            }

            if (legacy) {
                addLegacyExpansionToWorkBook(members,flat);
            }
        }

        return workbook;
    }

    private String getEcl(TTEntity entity) throws DataFormatException, JsonProcessingException {
        if (entity.get(IM.DEFINITION) == null)
            return null;
        String ecl = IMLToECL.getECLFromQuery(entity.get(IM.DEFINITION).asLiteral().objectValue(Query.class), true);
        return ecl;
    }




    private void addCoreExpansionToWorkBook(Set<Concept> members,boolean flat) {

        Sheet sheet = workbook.getSheet("Core expansion");
        if (null == sheet) sheet = workbook.createSheet("Core expansion");
        addHeaders(sheet, headerStyle, "code", "term", "extension","usage","im1Id");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 20000);
        sheet.setColumnWidth(2, 2500);
        sheet.setColumnWidth(3, 2500);
        sheet.setColumnWidth(4, 2500);

        Set<String> addedCoreIris = new HashSet<>();
        for (Concept cl : members) {
            if (!addedCoreIris.contains(cl.getIri())) {
                addCoreExpansionConceptToWorkBook(flat, sheet, addedCoreIris, cl);
            }
        }
    }

    private void addCoreExpansionConceptToWorkBook(boolean flat, Sheet sheet, Set<String> addedCoreIris, Concept cl) {
        Integer usage= cl.getUsage();
        String isExtension = cl.getScheme().getIri().contains("sct#") ? "N" : "Y";
        if (cl.getIm1Id()!=null&& flat) {
            for (String im1 : cl.getIm1Id()) {
                Row row = addRow(sheet);
                addCells(row, cl.getCode(), cl.getName(), isExtension, usage == null ? "" : usage, im1);
            }
        }
        else {
            Row row = addRow(sheet);
            addCells(row, cl.getCode(), cl.getName(), isExtension, usage == null ? "" : usage, "");
        }
        addedCoreIris.add(cl.getIri());
    }

    private void addLegacyExpansionToWorkBook(Set<Concept> members,boolean flat) {

        Sheet sheet = workbook.getSheet("Full expansion");
        if (null == sheet) sheet = workbook.createSheet("Full expansion");
        if (flat) {
            addHeaders(sheet, headerStyle, "core code", "core term", "extension", "legacy code", "Legacy term", "Legacy scheme","codeId",
                "usage", "im1Id");
            sheet.setColumnWidth(0, 5000);
            sheet.setColumnWidth(1, 25000);
            sheet.setColumnWidth(2, 2500);
            sheet.setColumnWidth(3, 20000);
            sheet.setColumnWidth(4, 20000);
            sheet.setColumnWidth(5, 2500);
            sheet.setColumnWidth(6, 2500);
            sheet.setColumnWidth(7, 2500);
            sheet.setColumnWidth(8, 2500);
        } else {
            addHeaders(sheet, headerStyle, "core code", "core term", "extension", "legacy code", "Legacy term", "Legacy scheme","codeId"
            );
            sheet.setColumnWidth(0, 5000);
            sheet.setColumnWidth(1, 25000);
            sheet.setColumnWidth(2, 2500);
            sheet.setColumnWidth(3, 20000);
            sheet.setColumnWidth(4, 20000);
            sheet.setColumnWidth(5, 2500);
            sheet.setColumnWidth(6, 2500);
        }

        for (Concept cl : members) {

            String isExtension = cl.getScheme().getIri().contains("sct#") ? "N" : "Y";
            if (cl.getMatchedFrom() == null) {
                Row row = addRow(sheet);
                addCells(row, cl.getCode(), cl.getName(), isExtension, "");

            } else {
                List<Concept> sortedLegacy = cl.getMatchedFrom()
                    .stream()
                    .sorted(Comparator
                        .comparing(Concept::getIri)
                    )
                    .collect(Collectors.toList());
                addLegacyExpansionConceptsToWorkbook(flat, sheet, cl, isExtension, sortedLegacy);
            }
        }
        sheet.autoSizeColumn(3);
    }

    private void addLegacyExpansionConceptsToWorkbook(boolean flat, Sheet sheet, Concept cl, String isExtension, List<Concept> sortedLegacy) {
        for (Concept legacy : sortedLegacy) {
            String legacyCode = legacy.getCode();
            String legacyScheme = legacy.getScheme().getIri();
            String legacyTerm = legacy.getName();
            Integer legacyUsage = legacy.getUsage();
            String codeId=legacy.getCodeId();
            if (codeId==null)
              codeId="";
            if (legacy.getIm1Id() == null || !flat) {
                Row row = addRow(sheet);
                addCells(row, cl.getCode(), cl.getName(), isExtension, legacyCode, legacyTerm, legacyScheme,codeId);
            } else {
                for (String im1Id : legacy.getIm1Id()) {
                    Row row = addRow(sheet);
                    addCells(row, cl.getCode(), cl.getName(), isExtension, legacyCode, legacyTerm, legacyScheme,codeId,
                        legacyUsage == null ? "" : legacyUsage, im1Id);
                }
            }
        }
    }

    private void addDefinitionToWorkbook(String ecl) {
        Sheet sheet = workbook.getSheet("Definition");
        if (null == sheet) sheet = workbook.createSheet("Definition");
        addHeaders(sheet, headerStyle, "ECL");

        String[] eclLines = ecl.split("\n");
        for (String eclLine:eclLines){
            Row row= addRow(sheet);
            addCells(row,eclLine);
        }
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

    private void addCells(Row row, Object... values) {
        for (Object value : values) {
            if (value != null) {
                addCellValue(row, value);
            }
        }
    }

    private static void addCellValue(Row row, Object value) {
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
        } else {
            Cell iriCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum(), CellType.STRING);
            iriCell.setCellValue("UNHANDLED TYPE");
        }
    }
}
