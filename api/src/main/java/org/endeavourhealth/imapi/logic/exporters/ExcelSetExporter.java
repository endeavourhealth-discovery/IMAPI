package org.endeavourhealth.imapi.logic.exporters;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.discovery.SettingsBasedSeedHostsProvider;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.logic.service.SetService;
import org.endeavourhealth.imapi.model.CoreLegacyCode;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.TTToECL;
import org.endeavourhealth.imapi.transforms.TTToTurtle;
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

    private EntityRepository2 repo = new EntityRepository2();
    private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
    private SetService setService = new SetService();

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
        Set<String> predicates = Set.of(RDFS.LABEL.getIri(), IM.DEFINITION.getIri());
        TTEntity entity = entityTripleRepository.getEntityPredicates(setIri, predicates, 0).getEntity();

        if (entity.get(IM.DEFINITION) == null) {
            return workbook;
        }

        addDefinitionToWorkbook(entity);
        if (hasSubset(entity.getIri())) {
            Set<String> codesAddedToWorkbook = new HashSet<>();
            Set<String> legacyCodesAddedToWorkbook = new HashSet<>();
            List<String> memberList = new ArrayList<>();
            Set<String> expandedSets = new HashSet<>();
            Set<String> legacyExpandedSets = new HashSet<>();
            addAllMemberIris(memberList, setIri);
            memberList.remove(setIri);
            for (String memberIri : memberList) {
                TTEntity member = entityTripleRepository.getEntityPredicates(memberIri, predicates, 0).getEntity();
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


    private void addDefinitionToWorkbook(TTEntity set) {
        Sheet sheet = workbook.getSheet("Definition");
        if (null == sheet) sheet = workbook.createSheet("Definition");
        addHeaders(sheet, headerStyle, "Iri", "Name", "ECL", "Turtle");
        TTToTurtle turtleConverter = new TTToTurtle();
        List<Namespace> namespaces = entityTripleRepository.findNamespaces();
        TTContext context = new TTContext();
        for(Namespace namespace : namespaces){
            context.add(namespace.getIri(), namespace.getPrefix(), namespace.getName());
        }
        String turtle = turtleConverter.transformEntity(set, context);

        try {
            String ecl = TTToECL.getExpressionConstraint(set.get(IM.DEFINITION),true);

            String[] eclLines = ecl.split("\n");
            String[] ttlLines = turtle.split("\n");

            for (int i = 0; i < Math.max(eclLines.length, ttlLines.length); i++) {
                String iri = (i==0) ? set.getIri() : "";
                String name = (i==0) ? set.getName() : "";
                String e = (i < eclLines.length) ? eclLines[i] : "";
                String t = (i < ttlLines.length) ? ttlLines[i] : "";

                Row row = addRow(sheet);
                addCells(row, iri, name, e, t);
            }

        } catch (DataFormatException e){
            Row row = addRow(sheet);
            addCells(row, set.getIri(), set.getName(), "Error", turtle);
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
