package org.endeavourhealth.imapi.logic.exporters;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.model.CoreLegacyCode;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.TTToECL;
import org.endeavourhealth.imapi.transforms.TTToTurtle;
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
    private XSSFFont font;
    private CellStyle headerStyle;

    public ExcelSetExporter() {
        workbook = new XSSFWorkbook();
        font = workbook.createFont();
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
     * @param setIri
     * @return
     * @throws DataFormatException
     */
    public XSSFWorkbook getSetAsExcel(String setIri, boolean core, boolean legacy, boolean flat) throws DataFormatException {
        TTEntity entity = entityTripleRepository.getEntityPredicates(setIri, Set.of(IM.DEFINITION.getIri())).getEntity();

        if (entity.getIri() == null || entity.getIri().isEmpty())
            return workbook;

        if (entity.get(IM.DEFINITION)==null && entity.get(IM.HAS_MEMBER)==null)
            throw new DataFormatException(setIri+" has neither a definition nor members");

        if (!entity.has(IM.DEFINITION))
            entity = entityTripleRepository.getEntityPredicates(setIri, Set.of(IM.HAS_MEMBER.getIri())).getEntity();

        String ecl = getEcl(entity);
        String ttl = getTtl(entity);
        addDefinitionToWorkbook(ecl, ttl);

        if (core || legacy) {
            Set<CoreLegacyCode> members = setExporter.getExpandedSetMembers(setIri, legacy);

            if (core) {
                addCoreExpansionToWorkBook(members);
            }

            if (legacy) {
                if (flat)
                    addLegacyExpansionToWorkBookFlat(members);
                else
                    addLegacyExpansionToWorkBook(members);
            }
        }

        return workbook;
    }

    private String getEcl(TTEntity entity) throws DataFormatException {
        if (entity.get(IM.DEFINITION) == null && entity.get(IM.HAS_MEMBER) == null)
            return null;

        String ecl;
        if (entity.get(IM.HAS_MEMBER) != null) {
            ecl = TTToECL.getExpressionConstraint(entity.get(IM.HAS_MEMBER), true);
        } else {
            ecl = TTToECL.getExpressionConstraint(entity.get(IM.DEFINITION), true);
        }

        return ecl;
    }

    private String getTtl(TTEntity entity) {
        TTToTurtle turtleConverter = new TTToTurtle();
        List<Namespace> namespaces = entityTripleRepository.findNamespaces();
        TTContext context = new TTContext();
        for (Namespace namespace : namespaces) {
            context.add(namespace.getIri(), namespace.getPrefix(), namespace.getName());
        }
        return turtleConverter.transformEntity(entity, context);
    }


    private void addCoreExpansionToWorkBook(Set<CoreLegacyCode> members) {
        List<CoreLegacyCode> sortedMembers = members
            .stream()
            .sorted(Comparator.comparing(CoreLegacyCode::getCode))
            .collect(Collectors.toList());

        Sheet sheet = workbook.getSheet("Core expansion");
        if (null == sheet) sheet = workbook.createSheet("Core expansion");
        addHeaders(sheet, headerStyle, "code", "term", "extension");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 20000);
        sheet.setColumnWidth(2, 2500);

        Set<String> addedCoreIris = new HashSet<>();
        for (CoreLegacyCode cl : sortedMembers) {
            if (!addedCoreIris.contains(cl.getIri())) {
                Row row = addRow(sheet);
                String isExtension = cl.getScheme().getIri().contains("sct#") ? "N" : "Y";
                addCells(row, cl.getCode(), cl.getTerm(), isExtension);
                addedCoreIris.add(cl.getIri());
            }
        }
    }

    private void addLegacyExpansionToWorkBook(Set<CoreLegacyCode> members) {
        List<CoreLegacyCode> sortedMembers = members
            .stream()
            .sorted(Comparator
                .comparing(CoreLegacyCode::getCode)
                .thenComparing(CoreLegacyCode::getLegacyCode)
                .thenComparing(CoreLegacyCode::getLegacyIri)
            )
            .collect(Collectors.toList());

        Sheet sheet = workbook.getSheet("Full expansion");
        if (null == sheet) sheet = workbook.createSheet("Full expansion");
        addHeaders(sheet, headerStyle, "core code", "core term", "extension", "legacy code", "Legacy term", "Legacy scheme");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 25000);
        sheet.setColumnWidth(2, 2500);
        sheet.setColumnWidth(4, 20000);

        Set<String> addedLegacyIris = new HashSet<>();
        for (CoreLegacyCode cl : sortedMembers) {
            Row row = addRow(sheet);
            String isExtension = cl.getScheme().getIri().contains("sct#") ? "N" : "Y";
            String legacyScheme = cl.getLegacyScheme() == null ? "" : cl.getLegacyScheme().getIri();
            if (cl.getLegacyIri() != null && !addedLegacyIris.contains(cl.getLegacyIri())) {
                addCells(row, cl.getCode(), cl.getTerm(), isExtension, cl.getLegacyCode(), cl.getLegacyTerm(), legacyScheme);
                addedLegacyIris.add(cl.getLegacyIri());
            } else {
                addCells(row, cl.getCode(), cl.getTerm(), isExtension);
            }
        }
        sheet.autoSizeColumn(3);
    }

    private void addLegacyExpansionToWorkBookFlat(Set<CoreLegacyCode> members) {
        List<CoreLegacyCode> sortedMembers = members
            .stream()
            .sorted(Comparator
                .comparing(CoreLegacyCode::getCode)
                .thenComparing(CoreLegacyCode::getLegacyCode)
                .thenComparing(CoreLegacyCode::getLegacyIri)
            )
            .collect(Collectors.toList());

        Sheet sheet = workbook.getSheet("Full expansion");
        if (null == sheet) sheet = workbook.createSheet("Full expansion");
        addHeaders(sheet, headerStyle, "code", "term", "scheme", "IMv1 ID");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 25000);
        sheet.setColumnWidth(2, 2500);
        sheet.setColumnWidth(3, 5000);

        Set<String> addedIris = new HashSet<>();
        for (CoreLegacyCode cl : sortedMembers) {
            if (cl.getIri() != null && !addedIris.contains(cl.getIri())) {
                Row row = addRow(sheet);
                String scheme = cl.getScheme() == null ? "" : cl.getScheme().getIri();
                addCells(row, cl.getCode(), cl.getTerm(), scheme, cl.getIm1Id());
                addedIris.add(cl.getIri());
            }
        }
        for (CoreLegacyCode cl : sortedMembers) {
            if (cl.getLegacyIri() != null && !addedIris.contains(cl.getLegacyIri())) {
                Row row = addRow(sheet);
                String legacyScheme = cl.getLegacyScheme() == null ? "" : cl.getLegacyScheme().getIri();
                addCells(row, cl.getLegacyCode(), cl.getLegacyTerm(), legacyScheme, cl.getLegacyIm1Id());
                addedIris.add(cl.getLegacyIri());
            }
        }
        sheet.autoSizeColumn(3);
    }

    private void addDefinitionToWorkbook(String ecl, String ttl) {
        Sheet sheet = workbook.getSheet("Definition");
        if (null == sheet) sheet = workbook.createSheet("Definition");
        addHeaders(sheet, headerStyle, "ECL", "Turtle");

        String[] eclLines = ecl.split("\n");
        String[] ttlLines = ttl.split("\n");

        for (int i = 0; i < Math.max(eclLines.length, ttlLines.length); i++) {
            String e = (i < eclLines.length) ? eclLines[i] : "";
            String t = (i < ttlLines.length) ? ttlLines[i] : "";

            Row row = addRow(sheet);
            addCells(row, e, t);
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
