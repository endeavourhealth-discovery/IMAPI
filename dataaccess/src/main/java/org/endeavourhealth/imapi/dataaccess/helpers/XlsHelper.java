package org.endeavourhealth.imapi.dataaccess.helpers;

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
		addHeaders(sheet, 20000, "Name", "Iri");

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
		addHeaders(sheet, 20000, "Name", "Iri");

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
		addHeaders(sheet, 10000, "Name", "Iri", "Type Name", "Type Iri");

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

	public void addMembersSheet(ExportValueSet exportValueSet) {
		Sheet sheet = workbook.createSheet("Members");
		addHeaders(sheet, 10000, "Included", "Member Name", "Member Iri", "Member Code", "Scheme Name", "Scheme Iri");

        addMembers(sheet, exportValueSet.getMembers(), "Yes");

//        addMembers(sheet, exportValueSet.getMembers(), "No");

    }

    private void addMembers(Sheet sheet, List<ValueSetMember> included, String yes) {
        for (ValueSetMember c : included) {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            Cell cell = row.createCell(0);
            cell.setCellValue(yes);
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
        addHeaders(sheet, 10000, "Included", "Member Name", "Member Iri", "Member Code", "Inherited Name", "Inherited Iri");

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

	private void addHeaders(Sheet sheet, int size, String...names) {
        Row header = sheet.createRow(0);

        int i = 0;
        for(String name: names) {
            sheet.setColumnWidth(i, size);
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(name);
            headerCell.setCellStyle(headerStyle);
            i++;
        }
    }
}
