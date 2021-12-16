package org.endeavourhealth.imapi.logic.exporters;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.EntityRepositoryImpl2;
import org.endeavourhealth.imapi.model.CoreLegacyCode;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.TTToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.Set;
import java.util.zip.DataFormatException;

public class ExcelSetExporter {

	private ExcelSetExporter() {
		throw new IllegalStateException("Utility class");
	}

	private static final EntityRepositoryImpl2 repo = new EntityRepositoryImpl2();


	public static XSSFWorkbook getSetAsExcel(String setIri) throws DataFormatException {
		Set<String> predicates= Set.of(RDFS.LABEL.getIri(),IM.DEFINITION.getIri());
		TTEntity entity= repo.getEntity(setIri,predicates);
		String ecl= TTToECL.getExpressionConstraint(entity.get(IM.DEFINITION),true);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFFont font = workbook.createFont();
		CellStyle headerStyle = workbook.createCellStyle();
		font.setBold(true);
		headerStyle.setFont(font);
		headerStyle.setWrapText(true);
		addEclToWorkbook(ecl,workbook,headerStyle);
		addCoreExpansionToWorkBook(entity,workbook,headerStyle);
		addLegacyExpansionToWorkBook(entity,workbook,headerStyle);
		return workbook;
}

	private static void addLegacyExpansionToWorkBook(TTEntity entity, XSSFWorkbook workbook, CellStyle headerStyle) {
		Sheet sheet = workbook.createSheet("Full expansion");
		addHeaders(sheet, headerStyle, "core code","core term","extension","legacy code","Legacy term","Legacy scheme");
		sheet.setColumnWidth(0,5000);
		sheet.setColumnWidth(1,20000);
		sheet.setColumnWidth(2,2500);
		sheet.setColumnWidth(4,20000);

		Set<CoreLegacyCode> expansion= repo.getSetExpansion(entity,true);
		for (CoreLegacyCode cl:expansion){
			Row row=addRow(sheet);
			String isLondon= cl.getScheme().getIri().contains("sct#") ?"N" :"Y";
			addCells(row,cl.getCode(),cl.getTerm(),isLondon,cl.getLegacyCode(),cl.getLegacyTerm(),cl.getLegacySchemeName());
		}
		sheet.autoSizeColumn(3);

	}

	private static void addCoreExpansionToWorkBook(TTEntity entity, XSSFWorkbook workbook, CellStyle headerStyle) {
		Sheet sheet = workbook.createSheet("Core expansion");
		addHeaders(sheet, headerStyle, "code","term","extension");
		sheet.setColumnWidth(0,5000);
		sheet.setColumnWidth(1,20000);
		sheet.setColumnWidth(2,2500);

		Set<CoreLegacyCode> expansion= repo.getSetExpansion(entity,false);
		for (CoreLegacyCode cl:expansion){
			Row row=addRow(sheet);
			String isLondon= cl.getScheme().getIri().contains("sct#") ?"N" :"Y";
			addCells(row,cl.getCode(),cl.getTerm(),isLondon);
		}

	}


	private static void addEclToWorkbook(String ecl, Workbook workbook, CellStyle headerStyle) {
		Sheet sheet = workbook.createSheet("ECL set definition");
		addHeaders(sheet, headerStyle, "ECL");
		Row row = addRow(sheet);
		addCells(row, ecl);
		sheet.autoSizeColumn(0);
	}



	private static void addHeaders(Sheet sheet, CellStyle headerStyle, String... names) {
		Row header = sheet.createRow(0);

		int i = 0;
		for(String name: names) {
			sheet.setColumnWidth(i, 10000);
			Cell headerCell = header.createCell(i);
			headerCell.setCellValue(name);
			headerCell.setCellStyle(headerStyle);
			i++;
		}
	}

	private static Row addRow(Sheet sheet) {
		return sheet.createRow(sheet.getLastRowNum() + 1);
	}

	private static void addCells(Row row, String... values) {
		for (String value : values) {
			Cell iriCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum());
			if (value!=null) {

				if (value.contains("\n")) {
					iriCell.getRow()
						.setHeightInPoints(iriCell.getSheet().getDefaultRowHeightInPoints() * value.split("\n").length);
					//CellStyle cellStyle = iriCell.getSheet().getWorkbook().createCellStyle();
					//cellStyle.setWrapText(true);
					//iriCell.setCellStyle(cellStyle);
				}
				iriCell.setCellValue(value);
				CellStyle cellStyle = iriCell.getSheet().getWorkbook().createCellStyle();
				cellStyle.setWrapText(true);
				iriCell.setCellStyle(cellStyle);
			}
		}
	}
}
