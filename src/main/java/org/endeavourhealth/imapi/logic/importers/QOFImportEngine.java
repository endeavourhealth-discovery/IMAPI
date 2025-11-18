package org.endeavourhealth.imapi.logic.importers;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.endeavourhealth.imapi.model.qof.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Slf4j
public class QOFImportEngine {

  public static QOFDocument processFile(File file) throws IOException {
    log.info("Processing QOF file: {}", file.getName());
    String fileName = file.getName().replace(".docx", "");
    XWPFDocument document = new XWPFDocument(Files.newInputStream(file.toPath()));

    QOFDocument qofDoc = new QOFDocument()
      .setName(fileName);

    List<IBodyElement> bodyElements = document.getBodyElements();

    String category = document.getProperties().getCoreProperties().getCategory();
    String h1 = "";
    String h2 = "";
    String h3 = "";
    String h4 = "";
    XWPFTable prevTable = null;
    Indicator currIndicator = null;

    log.info("Processing document elements");

    for (IBodyElement bodyElement : bodyElements) {
      if (bodyElement instanceof XWPFParagraph && !((XWPFParagraph) bodyElement).getText().trim().isEmpty()) {
        XWPFParagraph p = (XWPFParagraph) bodyElement;
        if (p.getStyleID() != null) {
          switch (p.getStyleID()) {
            case "Heading1" -> {
              h1 = p.getText();
              h2 = "";
              h3 = "";
              h4 = "";
            }
            case "Heading2" -> {
              h2 = p.getText();
              h3 = "";
              h4 = "";
            }
            case "Heading3" -> {
              h3 = p.getText();
              h4 = "";
            }
            case "Heading4" -> h4 = p.getText();
          }
        }
      } else if (bodyElement instanceof XWPFTable table) {
        String tableTitle = table.getRow(0).getCell(0).getText().trim();
        switch (tableTitle) {
          case "Term":
            processDatasetTable(qofDoc, table);
            break;
          case "Qualifying criteria":
            processSelectionTable(qofDoc, h3, table);
            break;
          case "Rule number":
            processPopulationsTable(qofDoc, prevTable, table);
            break;
          case "Cluster name":
            processCodeClusterTable(qofDoc, table);
            break;
          case "Field number":
            processExtractionTable(qofDoc, table);
            break;
          case "Indicator ID":
            currIndicator = processIndicator(qofDoc, category, table);
            break;
          case "Denominator":
            processDenominatorsTable(currIndicator, table);
            break;
          case "Numerator":
            processNumeratorsTable(currIndicator, table);
            break;
        }
        prevTable = table;
      }
    }

    document.close();
    log.info("QOF file processed");

    return qofDoc;
  }

  private static void processDatasetTable(QOFDocument qofDoc, XWPFTable datasetTable) {
    for (int i = 1; i < datasetTable.getRows().size() - 1; i++) {
      List<String> cells = getCellText(datasetTable.getRow(i).getTableICells());
      if (cells.isEmpty() || "Term".equals(cells.get(0)))
        continue;

      qofDoc.addTerm(cells.get(0), cells.get(1));
    }
  }

  private static void processSelectionTable(QOFDocument qofDoc, String name, XWPFTable ruleTable) {
    Selection selection = new Selection()
      .setName(name);
    qofDoc.getSelections().add(selection);

    for (int i = 1; i < ruleTable.getRows().size() - 1; i++) {
      List<String> cells = getCellText(ruleTable.getRow(i).getTableICells());
      if (cells.isEmpty() || "Qualifying criteria".equals(cells.get(0)))
        continue;

      selection.addRule(new Rule()
        .setLogicText(cells.get(0))
        .setIfTrue(cells.get(1).toUpperCase())
        .setIfFalse(cells.get(2).toUpperCase())
        .setDescription(cells.get(3))
      );
    }
  }

  private static void processPopulationsTable(QOFDocument qofDoc, XWPFTable regTable, XWPFTable ruleTable) {
    List<String> cells = getCellText(regTable.getRow(1).getTableICells());

    Register register = new Register()
      .setName(cells.get(0))
      .setDescription(cells.get(1))
      .setBase(cells.get(2));
    qofDoc.getRegisters().add(register);

    for (int i = 1; i < ruleTable.getRows().size() - 1; i++) {
      cells = getCellText(regTable.getRow(i).getTableICells());
      if (cells.isEmpty() || "Rule number".equals(cells.get(0)))
        continue;

      register.addRule(new Rule()
        .setOrder(i)
        .setLogicText(cells.get(1))
        .setIfTrue(cells.get(2).toUpperCase())
        .setIfFalse(cells.get(3).toUpperCase())
        .setDescription(cells.get(4))
      );

    }
  }

  private static void processCodeClusterTable(QOFDocument qofDoc, XWPFTable codeClusterTable) {
    for (int i = 1; i < codeClusterTable.getRows().size() - 1; i++) {
      List<String> cells = getCellText(codeClusterTable.getRow(i).getTableICells());
      if (cells.isEmpty() || "Cluster name".equals(cells.get(0)))
        continue;

      CodeCluster codeCluster = new CodeCluster()
        .setCode(cells.get(0))
        .setDescription(cells.get(1))
        .setSNOMEDCT(cells.get(2));

      qofDoc.addCodeCluster(codeCluster);
    }
  }


  private static void processExtractionTable(QOFDocument qofDoc, XWPFTable fieldTable) {
    for (int i = 1; i < fieldTable.getRows().size() - 1; i++) {
      List<String> cells = getCellText(fieldTable.getRow(i).getTableICells());
      if (cells.isEmpty() || "Field number".equals(cells.get(0)))
        continue;

      qofDoc.getExtractionFields().add(new ExtractionField()
        .setField(i)
        .setName(cells.get(1))
        .setCluster(cells.get(2))
        .setLogicText(cells.get(3))
        .setDescription(cells.get(4))
      );
    }
  }

  private static Indicator processIndicator(QOFDocument qofDoc, String category, XWPFTable indTable) {
    List<String> cells = getCellText(indTable.getRow(1).getTableICells());

    Indicator indicator = new Indicator()
      .setName(cells.get(0))
      .setDescription(cells.get(1))
      .setBase(category + cells.get(2));
    qofDoc.getIndicators().add(indicator);

    return indicator;
  }

  private static void processDenominatorsTable(Indicator indicator, XWPFTable ruleTable) {
    for (int i = 2; i < ruleTable.getRows().size() - 1; i++) {
      List<String> cells = getCellText(ruleTable.getRow(i).getTableICells());
      if (cells.isEmpty() || "Rule number".equals(cells.get(0)))
        continue;

      indicator.addDenominator(new Rule()
        .setOrder(i-1)
        .setLogicText(cells.get(1))
        .setIfTrue(cells.get(2).toUpperCase())
        .setIfFalse(cells.get(3).toUpperCase())
        .setDescription(cells.get(4))
      );
    }
  }

  private static void processNumeratorsTable(Indicator indicator, XWPFTable ruleTable) {

    for (int i = 2; i < ruleTable.getRows().size() - 1; i++) {
      List<String> cells = getCellText(ruleTable.getRow(i).getTableICells());
      if (cells.isEmpty() || "Rule number".equals(cells.get(0)))
        continue;

      indicator.addNumerator(new Rule()
        .setOrder(i-1)
        .setLogicText(cells.get(1))
        .setIfTrue(cells.get(2).toUpperCase())
        .setIfFalse(cells.get(3).toUpperCase())
        .setDescription(cells.get(4))
      );
    }
  }

  private static List<String> getCellText(List<ICell> cells) {
    List<String> result = cells.stream()
      .map(QOFImportEngine::getICellText)
      .toList();

    if (result.stream().allMatch(String::isEmpty))
      return List.of();

    return result;
  }

  private static String getICellText(ICell cell) {
    String text = "";
    if (cell instanceof XWPFTableCell tabCell) {
      text = tabCell.getTextRecursively().replace("\t", " ").replace("\u00A0", " ");
    } else if (cell instanceof XWPFSDTCell sdtCell) {
      text = sdtCell.getContent().getText();
    }

    return text.trim();
  }
}