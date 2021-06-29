package org.endeavourhealth.dataaccess.helpers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.dto.SemanticProperty;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;

import java.util.List;

public class XlsHelper {

	private final Workbook workbook;
	private final CellStyle headerStyle;

	public XlsHelper() {
		this.workbook = new XSSFWorkbook();
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		this.headerStyle = workbook.createCellStyle();
		font.setBold(true);
		headerStyle.setFont(font);
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public CellStyle getHeaderStyle() {
		return headerStyle;
	}

	public void addChildren(List<EntityReferenceNode> childrenList) {
		Sheet sheet = workbook.createSheet("Children");
		sheet.setColumnWidth(0, 20000);
		sheet.setColumnWidth(1, 20000);

		Row header = sheet.createRow(0);

		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("Name");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(1);
		headerCell.setCellValue("Iri");
		headerCell.setCellStyle(headerStyle);

		for (EntityReferenceNode child : childrenList) {
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(child.getName());
			cell = row.createCell(1);
			cell.setCellValue(child.getIri());
		}

	}

	public void addParents(List<EntityReferenceNode> parentList) {
		Sheet sheet = workbook.createSheet("Parents");
		sheet.setColumnWidth(0, 20000);
		sheet.setColumnWidth(1, 20000);

		Row header = sheet.createRow(0);

		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("Name");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(1);
		headerCell.setCellValue("Iri");
		headerCell.setCellStyle(headerStyle);

		for (TTValue parent : parentList) {
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(parent.asIriRef().getName());
			cell = row.createCell(1);
			cell.setCellValue(parent.asIriRef().getIri());
		}
	}

	public void addSemanticProperties(List<SemanticProperty> propertyList) {
		Sheet sheet = workbook.createSheet("Semantic properties");
		sheet.setColumnWidth(0, 10000);
		sheet.setColumnWidth(1, 10000);
		sheet.setColumnWidth(2, 10000);
		sheet.setColumnWidth(3, 10000);
		sheet.setColumnWidth(4, 10000);
		sheet.setColumnWidth(5, 10000);

		Row header = sheet.createRow(0);
		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("Name");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(1);
		headerCell.setCellValue("Iri");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(2);
		headerCell.setCellValue("Type Name");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(3);
		headerCell.setCellValue("Type Iri");
		headerCell.setCellStyle(headerStyle);


		for (SemanticProperty property : propertyList) {
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(property.getProperty().getName());
			cell = row.createCell(1);
			cell.setCellValue(property.getProperty().getIri());
			cell = row.createCell(2);
			cell.setCellValue(property.getType().getName());
			cell = row.createCell(3);
			cell.setCellValue(property.getType().getIri());
		}
	}

	public void addMembers(ExportValueSet exportValueSet) {
		Sheet sheet = workbook.createSheet("Members");

		sheet.setColumnWidth(0, 1000);
		sheet.setColumnWidth(1, 10000);
		sheet.setColumnWidth(2, 10000);
		sheet.setColumnWidth(3, 10000);
		sheet.setColumnWidth(4, 10000);
		sheet.setColumnWidth(5, 10000);
		sheet.setColumnWidth(6, 10000);

		Row header = sheet.createRow(0);
		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("Included");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(1);
		headerCell.setCellValue("Member Name");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(2);
		headerCell.setCellValue("Member Iri");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(3);
		headerCell.setCellValue("Member Code");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(4);
		headerCell.setCellValue("MemberScheme Name");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(5);
		headerCell.setCellValue("MemberScheme Iri");
		headerCell.setCellStyle(headerStyle);

		for (ValueSetMember c : exportValueSet.getIncluded()) {
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue("Yes");
			cell = row.createCell(1);
			cell.setCellValue(c.getEntity().getName());
			cell = row.createCell(2);
			cell.setCellValue(c.getEntity().getIri());
			cell = row.createCell(3);
			cell.setCellValue(c.getCode());

			if (c.getScheme() != null) {
				cell = row.createCell(4);
				cell.setCellValue(c.getScheme().getIri());
				cell = row.createCell(5);
				cell.setCellValue(c.getScheme().getName());
			}
		}

		for (ValueSetMember c : exportValueSet.getExcluded()) {
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue("No");
			cell = row.createCell(1);
			cell.setCellValue(c.getEntity().getName());
			cell = row.createCell(2);
			cell.setCellValue(c.getEntity().getIri());
			cell = row.createCell(3);
			cell.setCellValue(c.getCode());

			if (c.getScheme() != null) {
				cell = row.createCell(4);
				cell.setCellValue(c.getScheme().getIri());
				cell = row.createCell(5);
				cell.setCellValue(c.getScheme().getName());
			}
		}

	}

	public void addDataModelProperties(List<DataModelProperty> properties) {
		Sheet sheet = workbook.createSheet("Data model properties");
		sheet.setColumnWidth(0, 10000);
		sheet.setColumnWidth(1, 10000);
		sheet.setColumnWidth(2, 10000);
		sheet.setColumnWidth(3, 10000);
		sheet.setColumnWidth(4, 10000);
		sheet.setColumnWidth(5, 10000);

		Row header = sheet.createRow(0);
		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("Name");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(1);
		headerCell.setCellValue("Iri");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(2);
		headerCell.setCellValue("Type Name");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(3);
		headerCell.setCellValue("Type Iri");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(4);
		headerCell.setCellValue("InheritedFrom Name");
		headerCell.setCellStyle(headerStyle);
		headerCell = header.createCell(5);
		headerCell.setCellValue("InheritedFrom Iri");
		headerCell.setCellStyle(headerStyle);

		for (DataModelProperty property : properties) {
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(property.getProperty().getName());
			cell = row.createCell(1);
			cell.setCellValue(property.getProperty().getIri());
			if(property.getType()!=null){
				cell = row.createCell(2);
				cell.setCellValue(property.getType().getName());
				cell = row.createCell(3);
				cell.setCellValue(property.getType().getIri());
			}

			if (null != property.getInheritedFrom()) {
				cell = row.createCell(4);
				cell.setCellValue(property.getInheritedFrom().getName());
				cell = row.createCell(5);
				cell.setCellValue(property.getInheritedFrom().getIri());
			}
		}
	}
}
