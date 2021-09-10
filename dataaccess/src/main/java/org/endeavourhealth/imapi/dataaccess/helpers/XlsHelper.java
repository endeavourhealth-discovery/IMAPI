package org.endeavourhealth.imapi.dataaccess.helpers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.dto.SemanticProperty;
import org.endeavourhealth.imapi.model.search.EntitySummary;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.MemberType;
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

	public void addSummary(EntitySummary summary) {
		Sheet sheet = workbook.createSheet("Concept summary");
		addHeaders(sheet, 10000, "Name", "Iri", "Code", "Description", "Status", "Scheme", "Type", "IsDescendantOf");
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
//		name
		Cell cell1 = row.createCell(0);
		cell1.setCellValue(summary.getName());
//		iri
		Cell cell2 = row.createCell(1);
		cell2.setCellValue(summary.getIri());
//		code
		Cell cell3 = row.createCell(2);
		cell3.setCellValue(summary.getCode());
//		description
		Cell cell4 = row.createCell(3);
		cell4.setCellValue(summary.getDescription());
//		status
		Cell cell5 = row.createCell(4);
		cell5.setCellValue(summary.getStatus().getName());
//		scheme
		Cell cell6 = row.createCell(5);
		cell6.setCellValue(summary.getScheme().getName());
//		type
		Cell cell7 = row.createCell(6);
		String typeString = "";
		for (TTValue type : summary.getEntityType().getElements()) {
			if (type.isIriRef()) {
				if (typeString == "") {
					typeString = type.asIriRef().getName();
				} else {
					typeString = typeString + ", " + type.asIriRef().getName();
				}
			}
		}
		cell7.setCellValue(typeString);
//		isDescendantOf
		Cell cell8 = row.createCell(7);
		String descendantString = "";
		for (TTIriRef descendant : summary.getIsDescendentOf()) {
			if (descendantString == "") {
				descendantString = descendant.getName();
			} else {
				descendantString = descendantString + ", " + descendant.getName();
			}
		}
		cell8.setCellValue(descendantString);
	}

	public void addHasSubTypes(List<EntityReferenceNode> childrenList) {
		Sheet sheet = workbook.createSheet("Has sub types");
		addHeaders(sheet, 20000, "Name", "Iri");

		for (EntityReferenceNode child : childrenList) {
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(child.getName());
			cell = row.createCell(1);
			cell.setCellValue(child.getIri());
		}

	}

	public void addIsA(List<EntityReferenceNode> parentList) {
		Sheet sheet = workbook.createSheet("Is a");
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
		addHeaders(sheet, 10000, "Member type", "Member name", "Member iri", "Member code", "Scheme name", "Scheme iri", "Subset name", "Subset iri");

        addMembers(sheet, exportValueSet.getMembers());

    }

    private void addMembers(Sheet sheet, List<ValueSetMember> included) {
        for (ValueSetMember c : included) {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            Cell cell = row.createCell(0);
			cell.setCellValue(c.getType().name());
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
            if (c.getType() == MemberType.SUBSET) {
				cell = row.createCell(6);
				cell.setCellValue(c.getDirectParent().getName());
				cell = row.createCell(7);
				cell.setCellValue(c.getDirectParent().getIri());
			} else {
				cell = row.createCell(6);
				cell.setCellValue("N/A");
				cell = row.createCell(7);
				cell.setCellValue("N/A");
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
