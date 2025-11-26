package org.endeavourhealth.imapi.logic.importers

import org.apache.poi.xwpf.usermodel.*
import org.endeavourhealth.imapi.model.qof.*
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files

object QOFImportEngine {
    private val log = LoggerFactory.getLogger(QOFImportEngine::class.java)

    fun processFile(file: File): QOFDocument {
        log.info("Processing QOF file: {}", file.name)
        val fileName = file.name.replace(".docx", "")
        val document = XWPFDocument(Files.newInputStream(file.toPath()))

        val qofDoc = QOFDocument().setName(fileName)

        val bodyElements = document.bodyElements

        val category = document.properties.coreProperties.category
        var h3 = ""
        var prevTable: XWPFTable? = null
        var currIndicator: Indicator? = null

        log.info("Processing document elements")

        for (bodyElement in bodyElements) {
          if (bodyElement is XWPFParagraph && bodyElement.text.trim().isNotEmpty()) {
            if (bodyElement.styleID != null) {
              h3 = when (bodyElement.styleID) {
                "Heading1" -> ""
                "Heading2" -> ""
                "Heading3" -> bodyElement.text
                else -> h3
              }
            }
          } else if (bodyElement is XWPFTable) {
            val tableTitle = bodyElement.getRow(0).getCell(0).text.trim()
            when (tableTitle) {
              "Term" -> processDatasetTable(qofDoc, bodyElement)
              "Qualifying criteria" -> processSelectionTable(qofDoc, h3, bodyElement)
              "Rule number" -> processPopulationsTable(qofDoc, prevTable!!, bodyElement)
              "Cluster name" -> processCodeClusterTable(qofDoc, bodyElement)
              "Field number" -> processExtractionTable(qofDoc, bodyElement)
              "Indicator ID" -> currIndicator = processIndicator(qofDoc, category, bodyElement)
              "Denominator" -> processDenominatorsTable(currIndicator!!, bodyElement)
              "Numerator" -> processNumeratorsTable(currIndicator!!, bodyElement)
            }
            prevTable = bodyElement
          }
        }

        document.close()
        log.info("QOF file processed")

        return qofDoc
    }

    private fun processDatasetTable(qofDoc: QOFDocument, datasetTable: XWPFTable) {
        for (i in 1 until datasetTable.rows.size - 1) {
            val cells = getCellText(datasetTable.getRow(i).tableICells)
            if (cells.isEmpty() || "Term" == cells[0]) continue

            qofDoc.addTerm(cells[0], cells[1])
        }
    }

    private fun processSelectionTable(qofDoc: QOFDocument, name: String, ruleTable: XWPFTable) {
        val selection = Selection().setName(name)
        qofDoc.selections.add(selection)

        for (i in 1 until ruleTable.rows.size - 1) {
            val cells = getCellText(ruleTable.getRow(i).tableICells)
            if (cells.isEmpty() || "Qualifying criteria" == cells[0]) continue

            selection.addRule(Rule()
                .setLogicText(cells[0])
                .setIfTrue(cells[1].uppercase())
                .setIfFalse(cells[2].uppercase())
                .setDescription(cells[3])
            )
        }
    }

    private fun processPopulationsTable(qofDoc: QOFDocument, regTable: XWPFTable, ruleTable: XWPFTable) {
        val cells = getCellText(regTable.getRow(1).tableICells)

        val register = Register()
            .setName(cells[0])
            .setDescription(cells[1])
            .setBase(cells[2])
        qofDoc.registers.add(register)

        for (i in 1 until ruleTable.rows.size - 1) {
            val cells2 = getCellText(ruleTable.getRow(i).tableICells)
            if (cells2.isEmpty() || "Rule number" == cells2[0]) continue

            register.addRule(Rule()
                .setOrder(i)
                .setLogicText(cells2[1])
                .setIfTrue(cells2[2].uppercase())
                .setIfFalse(cells2[3].uppercase())
                .setDescription(cells2[4])
            )
        }
    }

    private fun processCodeClusterTable(qofDoc: QOFDocument, codeClusterTable: XWPFTable) {
        for (i in 1 until codeClusterTable.rows.size - 1) {
            val cells = getCellText(codeClusterTable.getRow(i).tableICells)
            if (cells.isEmpty() || "Cluster name" == cells[0]) continue

            val codeCluster = CodeCluster()
                .setCode(cells[0])
                .setDescription(cells[1])
                .setSNOMEDCT(cells[2])

            qofDoc.addCodeCluster(codeCluster)
        }
    }

    private fun processExtractionTable(qofDoc: QOFDocument, fieldTable: XWPFTable) {
        for (i in 1 until fieldTable.rows.size - 1) {
            val cells = getCellText(fieldTable.getRow(i).tableICells)
            if (cells.isEmpty() || "Field number" == cells[0]) continue

            qofDoc.extractionFields.add(ExtractionField()
                .setField(i)
                .setName(cells[1])
                .setCluster(cells[2])
                .setLogicText(cells[3])
                .setDescription(cells[4])
            )
        }
    }

    private fun processIndicator(qofDoc: QOFDocument, category: String, indTable: XWPFTable): Indicator {
        val cells = getCellText(indTable.getRow(1).tableICells)

        val indicator = Indicator()
            .setName(cells[0])
            .setDescription(cells[1])
            .setBase(category + cells[2])
        qofDoc.indicators.add(indicator)

        return indicator
    }

    private fun processDenominatorsTable(indicator: Indicator, ruleTable: XWPFTable) {
        for (i in 2 until ruleTable.rows.size - 1) {
            val cells = getCellText(ruleTable.getRow(i).tableICells)
            if (cells.isEmpty() || "Rule number" == cells[0]) continue

            indicator.addDenominator(Rule()
                .setOrder(i - 1)
                .setLogicText(cells[1])
                .setIfTrue(cells[2].uppercase())
                .setIfFalse(cells[3].uppercase())
                .setDescription(cells[4])
            )
        }
    }

    private fun processNumeratorsTable(indicator: Indicator, ruleTable: XWPFTable) {
        for (i in 2 until ruleTable.rows.size - 1) {
            val cells = getCellText(ruleTable.getRow(i).tableICells)
            if (cells.isEmpty() || "Rule number" == cells[0]) continue

            indicator.addNumerator(Rule()
                .setOrder(i - 1)
                .setLogicText(cells[1])
                .setIfTrue(cells[2].uppercase())
                .setIfFalse(cells[3].uppercase())
                .setDescription(cells[4])
            )
        }
    }

    private fun getCellText(cells: List<ICell>): List<String> {
        val result = cells.map { getICellText(it) }

        return if (result.all { it.trim().isEmpty() }) emptyList() else result
    }

    private fun getICellText(cell: ICell): String {
        val text = when (cell) {
            is XWPFTableCell -> cell.textRecursively.replace("\t", " ").replace("\u00A0", " ")
            is XWPFSDTCell -> cell.content.text
            else -> ""
        }

        return text
    }
}