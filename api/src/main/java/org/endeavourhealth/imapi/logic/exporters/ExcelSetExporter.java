package org.endeavourhealth.imapi.logic.exporters;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.IMLToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Manages the Exports concept set data in excel workboook format
 */
@Component
public class ExcelSetExporter {

    private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();

    private SetExporter setExporter = new SetExporter();

    private XSSFWorkbook workbook;
    private CellStyle headerStyle;
    private EntityRepository2 entityRepository2 = new EntityRepository2();

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
    public XSSFWorkbook getSetAsExcel(String setIri, boolean definition, boolean core, boolean legacy,boolean includeSubsets, boolean ownRow,
                                      boolean im1id, List<String> schemes) throws JsonProcessingException, QueryException {
        TTEntity entity = entityTripleRepository.getEntityPredicates(setIri, Set.of(IM.DEFINITION.getIri())).getEntity();
        if (entity.getIri() == null || entity.getIri().isEmpty())
            return workbook;

        String setName = entityRepository2.getBundle(setIri,Set.of(RDFS.LABEL.getIri())).getEntity().getName();

        String ecl = getEcl(entity);
        if(ecl != null && definition) {
            addDefinitionToWorkbook(ecl);
        }

        if (core || legacy) {
            Set<Concept> members = setExporter.getExpandedSetMembers(setIri, legacy, includeSubsets, schemes).stream()
                    .sorted(Comparator.comparing(Concept::getName)).collect(Collectors.toCollection(LinkedHashSet::new));

            if(includeSubsets) {
                members = members.stream().sorted(Comparator.comparing(m -> m.getIsContainedIn()
                        .iterator().next().getName())).collect(Collectors.toCollection(LinkedHashSet::new));
            }

            if (core) {
                addCoreExpansionToWorkBook(setName, members, im1id, includeSubsets);
            }

            if (legacy) {
                addLegacyExpansionToWorkBook(setName, members, im1id, ownRow, includeSubsets);
            }
        }

        return workbook;
    }

    private String getEcl(TTEntity entity) throws QueryException, JsonProcessingException {
        if (entity.get(IM.DEFINITION) == null)
            return null;
        return IMLToECL.getECLFromQuery(entity.get(IM.DEFINITION).asLiteral().objectValue(Query.class), true);
    }

    private void addCoreExpansionToWorkBook(String setName, Set<Concept> members, boolean im1id, boolean includeSubsets) {

        Sheet sheet = workbook.getSheet("Core expansion");
        if (null == sheet) sheet = workbook.createSheet("Core expansion");
        if(includeSubsets) {
            if(im1id) {
                addHeaders(sheet, headerStyle, "code", "term", "set", "subset", "subsetIri", "extension","usage","im1Id");
                sheet.setColumnWidth(0, 5000);
                sheet.setColumnWidth(1, 20000);
                sheet.setColumnWidth(2, 15000);
                sheet.setColumnWidth(3, 15000);
                sheet.setColumnWidth(4, 15000);
                sheet.setColumnWidth(5, 2500);
                sheet.setColumnWidth(6, 2500);
                sheet.setColumnWidth(7, 2500);
            } else {
                addHeaders(sheet, headerStyle, "code", "term", "set", "subset", "subsetIri", "extension");
                sheet.setColumnWidth(0, 5000);
                sheet.setColumnWidth(1, 20000);
                sheet.setColumnWidth(2, 15000);
                sheet.setColumnWidth(3, 15000);
                sheet.setColumnWidth(4, 15000);
                sheet.setColumnWidth(5, 2500);
            }

        } else {
            if(im1id) {
                addHeaders(sheet, headerStyle, "code", "term", "set", "extension","usage","im1Id");
                sheet.setColumnWidth(0, 5000);
                sheet.setColumnWidth(1, 20000);
                sheet.setColumnWidth(2, 15000);
                sheet.setColumnWidth(3, 2500);
                sheet.setColumnWidth(4, 2500);
                sheet.setColumnWidth(5, 2500);
            } else {
                addHeaders(sheet, headerStyle, "code", "term", "set", "extension");
                sheet.setColumnWidth(0, 5000);
                sheet.setColumnWidth(1, 20000);
                sheet.setColumnWidth(2, 15000);
                sheet.setColumnWidth(3, 2500);
            }

        }
        Set<String> addedCoreIris = new HashSet<>();
        for (Concept cl : members) {
            if (!addedCoreIris.contains(cl.getIri())) {
                addCoreExpansionConceptToWorkBook(sheet, addedCoreIris, cl, im1id, setName, includeSubsets);
            }
        }
    }

    private void addCoreExpansionConceptToWorkBook(Sheet sheet, Set<String> addedCoreIris, Concept cl, boolean im1id, String setName, boolean includeSubsets) {
        Integer usage= cl.getUsage();
        String isExtension = cl.getScheme().getIri().contains("sct#") ? "N" : "Y";
        String subSet= cl.getIsContainedIn() != null ? cl.getIsContainedIn().iterator().next().getName() : "";
        String subsetIri = cl.getIsContainedIn() != null ? cl.getIsContainedIn().iterator().next().getIri() : "";
        if (cl.getIm1Id()!=null && im1id) {
            for (String im1 : cl.getIm1Id()) {
                Row row = addRow(sheet);
                if(includeSubsets) {
                    addCells(row, cl.getCode(), cl.getName(), setName , subSet, subsetIri, isExtension, usage == null ? "" : usage, im1);
                } else {
                    addCells(row, cl.getCode(), cl.getName(), setName , isExtension, usage == null ? "" : usage, im1);
                }
            }
        } else {
            Row row = addRow(sheet);
            if (includeSubsets) {
                addCells(row, cl.getCode(),cl.getName(), setName , subSet, subsetIri, isExtension);
            } else {
                addCells(row, cl.getCode(),cl.getName(), setName , isExtension);
            }
        }
        addedCoreIris.add(cl.getIri());
    }

    private void addLegacyExpansionToWorkBook(String setName, Set<Concept> members, boolean im1id, boolean ownRow, boolean includeSubsets) {

        Sheet sheet = workbook.getSheet("Full expansion");
        if (null == sheet) sheet = workbook.createSheet("Full expansion");
        if (includeSubsets) {
            if(ownRow) {
                addHeaders(sheet, headerStyle, "core code", "core term", "set", "subset", "subsetIri", "extension");
                sheet.setColumnWidth(0, 5000);
                sheet.setColumnWidth(1, 25000);
                sheet.setColumnWidth(2, 15000);
                sheet.setColumnWidth(3, 15000);
                sheet.setColumnWidth(4, 15000);
                sheet.setColumnWidth(5, 2500);
            } else {
                if (im1id) {
                    addHeaders(sheet, headerStyle, "core code", "core term", "set", "subset", "subsetIri", "extension",
                            "legacy code", "Legacy term", "Legacy scheme", "usage", "im1Id");
                    sheet.setColumnWidth(0, 5000);
                    sheet.setColumnWidth(1, 25000);
                    sheet.setColumnWidth(2, 15000);
                    sheet.setColumnWidth(3, 15000);
                    sheet.setColumnWidth(4, 15000);
                    sheet.setColumnWidth(5, 2500);
                    sheet.setColumnWidth(6, 20000);
                    sheet.setColumnWidth(7, 20000);
                    sheet.setColumnWidth(8, 5000);
                    sheet.setColumnWidth(9, 2500);
                    sheet.setColumnWidth(10, 2500);
                } else {
                    addHeaders(sheet, headerStyle, "core code", "core term", "set", "subset", "subsetIri", "extension",
                            "legacy code", "Legacy term", "Legacy scheme");
                    sheet.setColumnWidth(0, 5000);
                    sheet.setColumnWidth(1, 25000);
                    sheet.setColumnWidth(2, 15000);
                    sheet.setColumnWidth(3, 15000);
                    sheet.setColumnWidth(4, 15000);
                    sheet.setColumnWidth(5, 2500);
                    sheet.setColumnWidth(6, 20000);
                    sheet.setColumnWidth(7, 20000);
                    sheet.setColumnWidth(8, 5000);
                }

            }
        } else {
            if (ownRow) {
                addHeaders(sheet, headerStyle, "core code", "core term", "set", "extension");
                sheet.setColumnWidth(0, 5000);
                sheet.setColumnWidth(1, 25000);
                sheet.setColumnWidth(2, 15000);
                sheet.setColumnWidth(3, 2500);
            } else {
                if (im1id) {
                    addHeaders(sheet, headerStyle, "core code", "core term", "set", "extension",
                            "legacy code", "Legacy term", "Legacy scheme", "usage", "im1Id");
                    sheet.setColumnWidth(0, 5000);
                    sheet.setColumnWidth(1, 25000);
                    sheet.setColumnWidth(2, 15000);
                    sheet.setColumnWidth(3, 2500);
                    sheet.setColumnWidth(4, 20000);
                    sheet.setColumnWidth(5, 20000);
                    sheet.setColumnWidth(6, 2500);
                    sheet.setColumnWidth(7, 2500);
                    sheet.setColumnWidth(9, 2500);
                } else {
                    addHeaders(sheet, headerStyle, "core code", "core term", "set", "extension", "legacy code", "Legacy term", "Legacy scheme");
                    sheet.setColumnWidth(0, 5000);
                    sheet.setColumnWidth(1, 25000);
                    sheet.setColumnWidth(2, 15000);
                    sheet.setColumnWidth(3, 2500);
                    sheet.setColumnWidth(4, 20000);
                    sheet.setColumnWidth(5, 20000);
                    sheet.setColumnWidth(7, 2500);
                }

            }
        }

        for (Concept cl : members) {
            String subset = cl.getIsContainedIn() != null ? cl.getIsContainedIn().iterator().next().getName() : "";
            String subsetIri = cl.getIsContainedIn() != null ? cl.getIsContainedIn().iterator().next().getIri() : "";
            String isExtension = cl.getScheme().getIri().contains("sct#") ? "N" : "Y";
            if (cl.getMatchedFrom() == null) {
                Row row = addRow(sheet);
                if (includeSubsets) {
                    addCells(row, cl.getCode(), cl.getName(), setName, subset, subsetIri, isExtension, "");
                } else {
                    addCells(row, cl.getCode(), cl.getName(), setName, isExtension, "");
                }
            } else {
                List<Concept> sortedLegacy = cl.getMatchedFrom()
                    .stream()
                    .sorted(Comparator
                        .comparing(Concept::getIri)
                    )
                    .collect(Collectors.toList());
                addLegacyExpansionConceptsToWorkbook(sheet, cl, isExtension, sortedLegacy, im1id, ownRow, setName, includeSubsets);
            }
        }
        sheet.autoSizeColumn(3);
    }

    private void addLegacyExpansionConceptsToWorkbook(Sheet sheet, Concept cl, String isExtension, List<Concept> sortedLegacy,
                                                      boolean im1id, boolean ownRow, String setName, boolean includeSubsets) {
        String subset = cl.getIsContainedIn() != null ? cl.getIsContainedIn().iterator().next().getName() : "";
        String subsetIri = cl.getIsContainedIn() != null ? cl.getIsContainedIn().iterator().next().getIri() : "";
        if(ownRow) {
            Row row = addRow(sheet);
            if(includeSubsets) {
                addCells(row, cl.getCode(), cl.getName(), setName, subset, subsetIri, isExtension);
            } else {
                addCells(row, cl.getCode(), cl.getName(), setName, isExtension);
            }
        }
        for (Concept legacy : sortedLegacy) {
            String legacyCode = legacy.getCode();
            String legacyScheme = legacy.getScheme().getIri();
            String legacyTerm = legacy.getName();
            Integer legacyUsage = legacy.getUsage();
            if (legacy.getIm1Id() == null || !im1id) {
                Row row = addRow(sheet);
                if(!ownRow) {
                    if(includeSubsets) {
                        addCells(row, cl.getCode(), cl.getName(), setName, subset, subsetIri, isExtension, legacyCode, legacyTerm, legacyScheme);
                    } else {
                        addCells(row, cl.getCode(), cl.getName(), setName, isExtension, legacyCode, legacyTerm, legacyScheme);
                    }
                } else {
                    addCells(row,legacyCode, legacyTerm, legacyScheme );
                }
            } else {
                for (String im1Id : legacy.getIm1Id()) {
                    Row row = addRow(sheet);
                    if(!ownRow) {
                        if(includeSubsets) {
                            addCells(row, cl.getCode(), cl.getName(), setName, subset, subsetIri, isExtension, legacyCode, legacyTerm, legacyScheme,
                                    legacyUsage == null ? "" : legacyUsage, im1Id);
                        } else {
                            addCells(row, cl.getCode(), cl.getName(), setName, isExtension, legacyCode, legacyTerm, legacyScheme,
                                    legacyUsage == null ? "" : legacyUsage, im1Id);
                        }
                    } else {
                        addCells(row,legacyCode, legacyTerm, legacyScheme,
                                legacyUsage == null ? "" : legacyUsage, im1Id );
                    }
                }
            }
        }
        if(ownRow) {
            Row emptyRow = addRow(sheet);
            addCells(emptyRow,"");
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
            addCellValue(row, Objects.requireNonNullElse(value, ""));
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
