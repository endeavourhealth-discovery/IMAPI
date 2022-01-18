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

import java.util.ArrayList;
import java.util.HashSet;
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

        String ecl = TTToECL.getExpressionConstraint(entity.get(IM.DEFINITION), true);
        addEclToWorkbook(ecl, workbook, headerStyle);
        if (hasSubset(entity.getIri())) {
            Set<String> codesAddedToWorkbook = new HashSet<String>();
            Set<String> legacyCodesAddedToWorkbook = new HashSet<String>();
            List<String> memberList = new ArrayList<>();
            addAllMemberIris(memberList, setIri);
            memberList.remove(setIri);
            for (String memberIri : memberList) {
                TTEntity member = entityTripleRepository.getEntityPredicates(memberIri, predicates, 0).getEntity();
                addCoreExpansionToWorkBook(codesAddedToWorkbook, member, workbook, headerStyle);
                addLegacyExpansionToWorkBook(legacyCodesAddedToWorkbook, member, workbook, headerStyle);
            }
        } else {
            addCoreExpansionToWorkBook(new HashSet<String>(), entity, workbook, headerStyle);
            addLegacyExpansionToWorkBook(new HashSet<String>(), entity, workbook, headerStyle);
        }


        return workbook;
    }

    private void addLegacyExpansionToWorkBook(Set<String> legacyCodesAddedToWorkbook, TTEntity entity, XSSFWorkbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.getSheet("Full expansion");
        if (null == sheet) sheet = workbook.createSheet("Full expansion");
        addHeaders(sheet, headerStyle, "core code", "core term", "extension", "legacy code", "Legacy term", "Legacy scheme");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 20000);
        sheet.setColumnWidth(2, 2500);
        sheet.setColumnWidth(4, 20000);

        Set<CoreLegacyCode> expansion = repo.getSetExpansion(entity, true);
        for (CoreLegacyCode cl : expansion) {
            if (!legacyCodesAddedToWorkbook.contains(cl.getLegacyCode())) {
                Row row = addRow(sheet);
                String isLondon = cl.getScheme().getIri().contains("sct#") ? "N" : "Y";
                addCells(row, cl.getCode(), cl.getTerm(), isLondon, cl.getLegacyCode(), cl.getLegacyTerm(), cl.getLegacySchemeName());
                legacyCodesAddedToWorkbook.add(cl.getLegacyCode());
            }
        }
        sheet.autoSizeColumn(3);

    }

    private void addCoreExpansionToWorkBook(Set<String> codesAddedToWorkbook, TTEntity entity, XSSFWorkbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.getSheet("Core expansion");
        if (null == sheet) sheet = workbook.createSheet("Core expansion");
        addHeaders(sheet, headerStyle, "code", "term", "extension");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 20000);
        sheet.setColumnWidth(2, 2500);

        Set<CoreLegacyCode> expansion = repo.getSetExpansion(entity, false);
        for (CoreLegacyCode cl : expansion) {
            if (!codesAddedToWorkbook.contains(cl.getCode())) {
                Row row = addRow(sheet);
                String isLondon = cl.getScheme().getIri().contains("sct#") ? "N" : "Y";
                addCells(row, cl.getCode(), cl.getTerm(), isLondon);
                codesAddedToWorkbook.add(cl.getCode());
            }
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

    private void addAllMemberIris(List<String> allIris, String iri) {
        if (repo.isSet(iri) && hasSubset(iri)) {
            List<String> memberList = repo.getMemberIris(iri);
            for (String subsetIri : memberList) {
                addAllMemberIris(allIris, subsetIri);
            }
        }
        allIris.add(iri);
    }

    private boolean hasSubset(String iri) {
        List<String> memberList = repo.getMemberIris(iri);
        return memberList.stream().anyMatch(member -> repo.isSet(member));
    }

}
