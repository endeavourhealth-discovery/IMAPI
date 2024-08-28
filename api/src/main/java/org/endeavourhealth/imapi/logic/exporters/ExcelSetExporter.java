package org.endeavourhealth.imapi.logic.exporters;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.model.exporters.SetExporterOptions;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.logic.exporters.helpers.ExporterHelpers.*;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * Manages the Exports concept set data in excel workboook format
 */
@Component
public class ExcelSetExporter {

  public static final String EXTENSION = "extension";
  public static final String IM_1_ID = "im1Id";
  public static final String SUBSET_VERSION = "subsetVersion";
  public static final String SUBSET_IRI = "subsetIri";
  public static final String SUBSET = "subset";
  public static final String SET = "set";
  public static final String USAGE = "usage";
  public static final String SCHEME = "scheme";
  public static final String STATUS = "status";
  public static final String TERM = "term";
  public static final String CODE = "code";
  public static final String LEGACY_CODE = "legacy code";
  public static final String LEGACY_TERM = "Legacy term";
  public static final String LEGACY_SCHEME = "Legacy scheme";
  public static final String LEGACY_USAGE = "Legacy Usage";
  public static final String CODE_ID = "code id";
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

  private static void addCellValue(Row row, Object value) {
    if (value instanceof String valueAsString) {
      Cell stringCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum(), CellType.STRING);
      if (valueAsString.contains("\n")) {
        stringCell.getRow()
          .setHeightInPoints(stringCell.getSheet().getDefaultRowHeightInPoints() * ((String) value).split("\n").length);
      }
      stringCell.setCellValue(valueAsString);
    } else if (value instanceof Integer valueAsInteger) {
      Cell intCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum(), CellType.NUMERIC);
      intCell.setCellValue(valueAsInteger);
    } else {
      Cell iriCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum(), CellType.STRING);
      iriCell.setCellValue("UNHANDLED TYPE");
    }
  }

  private static void setColumnWidthCoreWithSubset(Sheet sheet) {
    sheet.setColumnWidth(0, 5000);
    sheet.setColumnWidth(1, 20000);
    sheet.setColumnWidth(2, 10000);
    sheet.setColumnWidth(3, 2500);
    sheet.setColumnWidth(4, 15000);
    sheet.setColumnWidth(5, 15000);
    sheet.setColumnWidth(6, 15000);
    sheet.setColumnWidth(7, 2500);

  }

  private static void setColumnWidthCore(Sheet sheet) {
    sheet.setColumnWidth(0, 5000);
    sheet.setColumnWidth(1, 20000);
    sheet.setColumnWidth(2, 10000);
    sheet.setColumnWidth(3, 2500);
    sheet.setColumnWidth(4, 15000);
    sheet.setColumnWidth(5, 2500);

  }

  private static void setColumnWidthLegacy(Sheet sheet, int index) {
    sheet.setColumnWidth(index, 5000);
    sheet.setColumnWidth(index + 1, 20000);
    sheet.setColumnWidth(index + 2, 10000);
    sheet.setColumnWidth(index + 3, 3000);
    sheet.setColumnWidth(index + 4, 5000);

  }

  /**
   * Exports  3 excel sheets from the data store
   * <p>Sheet 1 = the definition in ECL</p>
   * <p>Sheet 2 = the expanded core concept set</p>
   * <p>Sheet 3 = the expanded core and legacy code set</p>
   *
   * @param options of the set
   * @return work book
   */
  public XSSFWorkbook getSetAsExcel(SetExporterOptions options) throws JsonProcessingException, QueryException {
    TTEntity entity = entityTripleRepository.getEntityPredicates(options.getSetIri(), Set.of(IM.DEFINITION)).getEntity();
    if (entity.getIri() == null || entity.getIri().isEmpty())
      return workbook;

    String setName = entityRepository2.getBundle(options.getSetIri(), Set.of(RDFS.LABEL)).getEntity().getName();

    String ecl = getEcl(entity);
    if (ecl != null && options.includeDefinition()) {
      addDefinitionToWorkbook(ecl);
    }

    if (options.includeCore() || options.includeLegacy()) {
      Set<Concept> members = setExporter.getExpandedSetMembers(options.getSetIri(), options.includeCore(), options.includeLegacy(), options.includeSubsets(), options.getSchemes()).stream()
        .sorted(Comparator.comparing(Concept::getName)).collect(Collectors.toCollection(LinkedHashSet::new));

      if (options.includeSubsets()) {
        members = members.stream().sorted(Comparator.comparing(this::byMemberContainedInName))
          .collect(Collectors.toCollection(LinkedHashSet::new));
      }

      if (options.includeCore()) {
        addCoreExpansionToWorkBook(setName, members, options.isIm1id(), options.includeSubsets());
      }

      if (options.includeLegacy()) {
        addLegacyExpansionToWorkBook(setName, members, options.isIm1id(), options.isOwnRow(), options.includeSubsets());
      }
    }

    return workbook;
  }

  private String byMemberContainedInName(Concept member) {
    return (null == member.getIsContainedIn() || member.getIsContainedIn().isEmpty()) ? "" : member.getIsContainedIn().iterator().next().getName();
  }

  private String getEcl(TTEntity entity) throws QueryException, JsonProcessingException {
    if (entity.get(iri(IM.DEFINITION)) == null)
      return null;
    return new IMQToECL().getECLFromQuery(entity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class), true);
  }

  private void addCoreExpansionToWorkBook(String setName, Set<Concept> members, boolean im1id, boolean includeSubsets) {

    Sheet sheet = workbook.getSheet("Core expansion");
    if (null == sheet) sheet = workbook.createSheet("Core expansion");
    if (includeSubsets) {
      if (im1id) {
        addHeaders(sheet, headerStyle, CODE, TERM, STATUS, SCHEME, USAGE, SET, SUBSET, SUBSET_IRI, SUBSET_VERSION, EXTENSION, IM_1_ID);
        setColumnWidthCoreWithSubset(sheet);
        sheet.setColumnWidth(8, 2500);
      } else {
        addHeaders(sheet, headerStyle, CODE, TERM, STATUS, SCHEME, USAGE, SET, SUBSET, SUBSET_IRI, SUBSET_VERSION, EXTENSION);
        setColumnWidthCoreWithSubset(sheet);
      }

    } else {
      if (im1id) {
        addHeaders(sheet, headerStyle, CODE, TERM, STATUS, SCHEME, USAGE, SET, EXTENSION, IM_1_ID);
        setColumnWidthCore(sheet);
        sheet.setColumnWidth(6, 2500);
      } else {
        addHeaders(sheet, headerStyle, CODE, TERM, STATUS, SCHEME, USAGE, SET, EXTENSION);
        setColumnWidthCore(sheet);
      }

    }
    Set<String> addedCoreIris = new HashSet<>();
    for (Concept cl : members) {
      if (!addedCoreIris.contains(cl.getIri())) {
        addCoreExpansionConceptToWorkBook(sheet, addedCoreIris, cl, im1id, setName, includeSubsets);
      }
    }
  }

  private void addCoreExpansionConceptToWorkBook(Sheet sheet, Set<String> addedCoreIris, Concept concept, boolean im1id, String setName, boolean includeSubsets) {
    String usage = getUsage(concept);
    String scheme = concept.getScheme().getName();
    String isExtension = getIsExtension(concept);
    String subSet = getSubSet(concept);
    String subsetIri = getSubsetIri(concept);
    String subsetVersion = setSubsetVersion(concept);
    String status = concept.getStatus().getName();

    String code = concept.getCode();
    if (concept.getAlternativeCode() != null)
      code = concept.getAlternativeCode();
    if (concept.getIm1Id() != null && im1id) {
      for (String im1 : concept.getIm1Id()) {
        Row row = addRow(sheet);
        if (includeSubsets) {
          addCells(row, code, concept.getName(), status, scheme, usage, setName, subSet, subsetIri, subsetVersion, isExtension, im1);
        } else {
          addCells(row, code, concept.getName(), status, scheme, usage, setName, isExtension, im1);
        }
      }
    } else {
      Row row = addRow(sheet);
      if (includeSubsets) {
        addCells(row, code, concept.getName(), status, scheme, usage, setName, subSet, subsetIri, subsetVersion, isExtension);
      } else {
        addCells(row, code, concept.getName(), status, scheme, usage, setName, isExtension);
      }
    }
    addedCoreIris.add(concept.getIri());
  }

  private void addLegacyExpansionToWorkBook(String setName, Set<Concept> members, boolean im1id, boolean ownRow, boolean includeSubsets) {

    Sheet sheet = workbook.getSheet("Full expansion");
    if (null == sheet) sheet = workbook.createSheet("Full expansion");
    if (includeSubsets) {
      if (ownRow) {
        addHeaders(sheet, headerStyle, CODE, TERM, SCHEME, USAGE, SET, SUBSET, SUBSET_IRI, SUBSET_VERSION, EXTENSION);
        setColumnWidthCoreWithSubset(sheet);
      } else {
        if (im1id) {
          addHeaders(sheet, headerStyle, CODE, TERM, SCHEME, USAGE, SET, SUBSET, SUBSET_IRI, SUBSET_VERSION, EXTENSION,
            LEGACY_CODE, LEGACY_TERM, LEGACY_SCHEME, LEGACY_USAGE, CODE_ID, IM_1_ID);
          setColumnWidthCoreWithSubset(sheet);
          setColumnWidthLegacy(sheet, 8);
          sheet.setColumnWidth(12, 7500);
        } else {
          addHeaders(sheet, headerStyle, CODE, TERM, SCHEME, USAGE, SET, SUBSET, SUBSET_IRI, SUBSET_VERSION, EXTENSION,
            LEGACY_CODE, LEGACY_TERM, LEGACY_SCHEME, LEGACY_USAGE, CODE_ID);
          setColumnWidthCoreWithSubset(sheet);
          setColumnWidthLegacy(sheet, 8);
        }
      }
    } else {
      if (ownRow) {
        addHeaders(sheet, headerStyle, CODE, TERM, SCHEME, USAGE, SET, EXTENSION);
        setColumnWidthCore(sheet);
      } else {
        if (im1id) {
          addHeaders(sheet, headerStyle, CODE, TERM, SCHEME, USAGE, SET, EXTENSION,
            LEGACY_CODE, LEGACY_TERM, LEGACY_SCHEME, LEGACY_USAGE, CODE_ID, IM_1_ID);
          setColumnWidthCore(sheet);
          setColumnWidthLegacy(sheet, 6);
          sheet.setColumnWidth(10, 7500);
        } else {
          addHeaders(sheet, headerStyle, CODE, TERM, SCHEME, USAGE, SET, EXTENSION,
            LEGACY_CODE, LEGACY_TERM, LEGACY_SCHEME, LEGACY_USAGE, CODE_ID);
          setColumnWidthCore(sheet);
          setColumnWidthLegacy(sheet, 6);
        }

      }
    }

    for (Concept concept : members) {
      String scheme = concept.getScheme().getName();
      String subset = getSubSet(concept);
      String subsetIri = getSubsetIri(concept);
      String subsetVersion = setSubsetVersion(concept);
      String isExtension = getIsExtension(concept);
      String usage = getUsage(concept);
      String code = concept.getCode();
      if (concept.getAlternativeCode() != null)
        code = concept.getAlternativeCode();
      if (concept.getMatchedFrom() == null) {
        Row row = addRow(sheet);
        if (includeSubsets) {
          addCells(row, code, concept.getName(), scheme, usage, setName, subset, subsetIri, subsetVersion, isExtension);
        } else {
          addCells(row, code, concept.getName(), scheme, usage, setName, isExtension);
        }
      } else {
        List<Concept> sortedLegacy = concept.getMatchedFrom()
          .stream()
          .sorted(Comparator
            .comparing(Concept::getIri)
          )
          .toList();
        addLegacyExpansionConceptsToWorkbook(sheet, concept, isExtension, sortedLegacy, im1id, ownRow, setName, includeSubsets);
      }
      if (ownRow) {
        Row emptyRow = addRow(sheet);
        addCells(emptyRow, "");
      }
    }
    sheet.autoSizeColumn(3);
  }

  private void addLegacyExpansionConceptsToWorkbook(Sheet sheet, Concept concept, String isExtension, List<Concept> sortedLegacy,
                                                    boolean im1id, boolean ownRow, String setName, boolean includeSubsets) {
    String scheme = concept.getScheme().getName();
    String subset = getSubSet(concept);
    String subsetIri = getSubsetIri(concept);
    String subsetVersion = setSubsetVersion(concept);
    String usage = getUsage(concept);
    String code = concept.getCode();
    if (concept.getAlternativeCode() != null)
      code = concept.getAlternativeCode();
    if (ownRow) {
      Row row = addRow(sheet);
      if (includeSubsets) {
        addCells(row, code, concept.getName(), scheme, usage, setName, subset, subsetIri, subsetVersion, isExtension);
      } else {
        addCells(row, code, concept.getName(), scheme, usage, setName, isExtension);
      }
    }
    for (Concept legacy : sortedLegacy) {
      String legacyCode = legacy.getCode();
      String legacyScheme = legacy.getScheme().getIri();
      String legacyTerm = legacy.getName();
      String legacyUsage = getUsage(legacy);
      String legacyCodeId = legacy.getCodeId();
      if (legacy.getIm1Id() == null || !im1id) {
        Row row = addRow(sheet);
        if (!ownRow) {
          if (includeSubsets) {
            addCells(row, code, concept.getName(), scheme, usage, setName, subset, subsetIri, subsetVersion, isExtension,
              legacyCode, legacyTerm, legacyScheme, legacyUsage, legacyCodeId);
          } else {
            addCells(row, concept.getCode(), concept.getName(), scheme, usage, setName, isExtension,
              legacyCode, legacyTerm, legacyScheme, legacyUsage, legacyCodeId);
          }
        } else {
          addCells(row, legacyCode, legacyTerm, legacyScheme, legacyUsage, legacyCodeId);
        }
      } else {
        for (String im1Id : legacy.getIm1Id()) {
          Row row = addRow(sheet);
          if (!ownRow) {
            if (includeSubsets) {
              addCells(row, code, concept.getName(), scheme, usage, setName, subset, subsetIri, subsetVersion, isExtension,
                legacyCode, legacyTerm, legacyScheme, legacyUsage, legacyCodeId, im1Id);
            } else {
              addCells(row, code, concept.getName(), scheme, usage, setName, isExtension,
                legacyCode, legacyTerm, legacyScheme, legacyUsage, legacyCodeId, im1Id);
            }
          } else {
            addCells(row, legacyCode, legacyTerm, legacyScheme, legacyUsage, legacyCodeId, im1Id);
          }
        }
      }
    }
  }

  private void addDefinitionToWorkbook(String ecl) {
    Sheet sheet = workbook.getSheet("Definition");
    if (null == sheet) sheet = workbook.createSheet("Definition");
    addHeaders(sheet, headerStyle, "ECL");

    String[] eclLines = ecl.split("\n");
    for (String eclLine : eclLines) {
      Row row = addRow(sheet);
      addCells(row, eclLine);
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
}
