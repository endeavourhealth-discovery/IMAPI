package org.endeavourhealth.imapi.logic.importers;

import org.apache.poi.xwpf.usermodel.*;
import org.endeavourhealth.imapi.model.qof.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class QOFImportEngine {

  public static QOFDocument processFile(File file) throws IOException {
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
      } else if (bodyElement instanceof XWPFTable) {
        XWPFTable table = (XWPFTable) bodyElement;
        switch (table.getRow(0).getCell(0).getText()) {
          case "Qualifying criteria":
            processSelectionTable(qofDoc, h3, table);
            break;
          case "Rule number":
            processPopulationsTable(qofDoc, prevTable, table);
            break;
          case "Field number":
            processExtractionTable(qofDoc, table);
            break;
          case "Denominator":
            processDenominatorsTable(qofDoc, category, prevTable, table);
            break;
        }
        prevTable = table;
      }
    }

    document.close();

    return qofDoc;
  }

  private static void processSelectionTable(QOFDocument qofDoc, String name, XWPFTable ruleTable) {
    Selection selection = new Selection()
      .setName(name);
    qofDoc.getSelections().add(selection);

    for (int i = 1; i < ruleTable.getRows().size() - 1; i++) {
      List<ICell> cells = ruleTable.getRow(i).getTableICells();

      if ("Qualifying criteria".equals(getICellText(cells.get(0))))
        continue;

      selection.addRule(new SelectionRule()
        .setLogic(getICellText(cells.get(0)))
        .setIfTrue(getICellText(cells.get(1)).toUpperCase())
        .setIfFalse(getICellText(cells.get(2)).toUpperCase())
        .setDescription(getICellText(cells.get(3)))
      );
    }
  }

  private static void processPopulationsTable(QOFDocument qofDoc, XWPFTable regTable, XWPFTable ruleTable) {
    List<ICell> cells = regTable.getRow(1).getTableICells();

    Register register = new Register()
      .setName(getICellText(cells.get(0)))
      .setDescription(getICellText(cells.get(1)))
      .setBase(getICellText(cells.get(2)));
    qofDoc.getRegisters().add(register);

    for (int i = 1; i < ruleTable.getRows().size() - 1; i++) {
      cells = ruleTable.getRow(i).getTableICells();

      if ("Rule number".equals(getICellText(cells.get(0))))
        continue;

      register.addRule(new Rule()
        .setRule(i)
        .setLogic(getICellText(cells.get(1)))
        .setIfTrue(getICellText(cells.get(2)).toUpperCase())
        .setIfFalse(getICellText(cells.get(3)).toUpperCase())
        .setDescription(getICellText(cells.get(4)))
      );
    }
  }

  private static void processExtractionTable(QOFDocument qofDoc, XWPFTable fieldTable) {
    for (int i = 1; i < fieldTable.getRows().size() - 1; i++) {
      List<ICell> cells = fieldTable.getRow(i).getTableICells();

      if ("Field number".equals(getICellText(cells.get(0))))
        continue;

      qofDoc.getExtractionFields().add(new ExtractionField()
        .setField(i)
        .setName(getICellText(cells.get(1)))
        .setCluster(getICellText(cells.get(2)))
        .setLogic(getICellText(cells.get(3)))
        .setDescription(getICellText(cells.get(4)))
      );
    }
  }

  private static void processDenominatorsTable(QOFDocument qofDoc, String category, XWPFTable regTable, XWPFTable ruleTable) {
    List<ICell> cells = regTable.getRow(1).getTableICells();

    Indicator indicator = new Indicator()
      .setName(getICellText(cells.get(0)))
      .setDescription(getICellText(cells.get(1)))
      .setBase(category + getICellText(cells.get(2)));
    qofDoc.getIndicators().add(indicator);

    for (int i = 2; i < ruleTable.getRows().size() - 1; i++) {
      cells = ruleTable.getRow(i).getTableICells();

      if ("Rule number".equals(getICellText(cells.get(0))))
        continue;

      indicator.addRule(new Rule()
        .setRule(i-1)
        .setLogic(getICellText(cells.get(1)))
        .setIfTrue(getICellText(cells.get(2)).toUpperCase())
        .setIfFalse(getICellText(cells.get(3)).toUpperCase())
        .setDescription(getICellText(cells.get(4)))
      );
    }
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