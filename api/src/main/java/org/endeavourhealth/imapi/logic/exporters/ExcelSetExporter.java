package org.endeavourhealth.imapi.logic.exporters;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.model.CoreLegacyCode;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.transforms.TTToECL;
import org.endeavourhealth.imapi.transforms.TTToTurtle;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SHACL;
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

    private EntityRepository2 repo = new EntityRepository2();
    private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();

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
    public XSSFWorkbook getSetAsExcel(String setIri,boolean legacy) throws DataFormatException {
        Set<String> predicates = Set.of(RDFS.LABEL.getIri(), IM.DEFINITION.getIri(),IM.HAS_MEMBER.getIri());
        TTEntity entity = entityTripleRepository.getEntityPredicates(setIri, predicates).getEntity();

        if (entity.getIri() == null || entity.getIri().isEmpty())
            return workbook;

        String ecl = getEcl(entity);
        String ttl = getTtl(entity);
        addDefinitionToWorkbook(ecl, ttl);

        Set<String> subsets = repo.getSubsets(entity.getIri());

        if (!subsets.isEmpty()) {
            Set<String> codesAddedToWorkbook = new HashSet<>();
            Set<String> legacyCodesAddedToWorkbook = new HashSet<>();
            Set<String> expandedSets = new HashSet<>();
            Set<String> legacyExpandedSets = new HashSet<>();
            for (String memberIri : subsets) {
                TTEntity member = entityTripleRepository.getEntityPredicates(memberIri, predicates).getEntity();
                addCoreExpansionToWorkBook(expandedSets, codesAddedToWorkbook, member);
                if (legacy)
                    addLegacyExpansionToWorkBook(legacyExpandedSets, legacyCodesAddedToWorkbook, member);
            }
        } else {
            addCoreExpansionToWorkBook(new HashSet<>(), new HashSet<>(), entity);
            if (legacy)
                addLegacyExpansionToWorkBook(new HashSet<>(), new HashSet<>(), entity);
        }

        return workbook;
    }

    private String getEcl(TTEntity entity) throws DataFormatException {
        if (entity.get(IM.DEFINITION) == null && entity.get(IM.HAS_MEMBER) == null)
            return null;

        String ecl;
        if (entity.get(IM.HAS_MEMBER) != null) {
            ecl = "";
            TTNode orNode = new TTNode();
            entity.addObject(IM.DEFINITION, orNode);
            for (TTValue value : entity.get(IM.HAS_MEMBER).getElements()) {
                orNode.addObject(SHACL.OR, value);
            }
        } else {
            ecl = TTToECL.getExpressionConstraint(entity.get(IM.DEFINITION), true);
        }

        return ecl;
    }

    private String getTtl(TTEntity entity) {
        TTToTurtle turtleConverter = new TTToTurtle();
        List<Namespace> namespaces = entityTripleRepository.findNamespaces();
        TTContext context = new TTContext();
        for(Namespace namespace : namespaces){
            context.add(namespace.getIri(), namespace.getPrefix(), namespace.getName());
        }
        return turtleConverter.transformEntity(entity, context);
    }

    private void addLegacyExpansionToWorkBook(Set<String> expandedSets, Set<String> legacyIrisAddedToWorkbook, TTEntity entity) {
        Sheet sheet = workbook.getSheet("Full expansion");
        if (null == sheet) sheet = workbook.createSheet("Full expansion");
        addHeaders(sheet, headerStyle, "core code", "core term", "extension", "legacy code", "Legacy term", "Legacy scheme");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 25000);
        sheet.setColumnWidth(2, 2500);
        sheet.setColumnWidth(4, 20000);

        if (!expandedSets.contains(entity.getIri())) {
            List<CoreLegacyCode> expansion = repo.getSetExpansion(entity.get(IM.DEFINITION), true);
            for (CoreLegacyCode cl : expansion) {
                Row row = addRow(sheet);
                String isExtension = cl.getScheme().getIri().contains("sct#") ? "N" : "Y";
                String legacyScheme = cl.getLegacyScheme() == null ? "" : cl.getLegacyScheme().getIri();
                addCells(row, cl.getCode(), cl.getTerm(), isExtension, cl.getLegacyCode(), cl.getLegacyTerm(), legacyScheme);
                legacyIrisAddedToWorkbook.add(cl.getLegacyIri());
            }
            sheet.autoSizeColumn(3);
            expandedSets.add(entity.getIri());
        }

    }

    private void addCoreExpansionToWorkBook(Set<String> expandedSets, Set<String> codesAddedToWorkbook, TTEntity entity) {
        Sheet sheet = workbook.getSheet("Core expansion");
        if (null == sheet) sheet = workbook.createSheet("Core expansion");
        addHeaders(sheet, headerStyle, "code", "term", "extension");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 20000);
        sheet.setColumnWidth(2, 2500);

        if (!expandedSets.contains(entity.getIri())) {
            List<CoreLegacyCode> expansion = repo.getSetExpansion(entity.get(IM.DEFINITION), false);
            for (CoreLegacyCode cl : expansion) {
                if (!codesAddedToWorkbook.contains(cl.getIri())) {
                    Row row = addRow(sheet);
                    String isExtension = cl.getScheme().getIri().contains("sct#") ? "N" : "Y";
                    addCells(row, cl.getCode(), cl.getTerm(), isExtension);
                    codesAddedToWorkbook.add(cl.getIri());
                }
            }
            expandedSets.add(entity.getIri());
        }

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
