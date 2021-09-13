package org.endeavourhealth.imapi.dataaccess.helpers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.dto.SemanticProperty;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.MemberType;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

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

	public void addSummary(TTEntity summary) {
		if (summary.getIri().isEmpty()) {
			return;
		}
		Set<TTIriRef> predicates = summary.getPredicateMap().keySet();
		List<String> predicateNames = new ArrayList<>();
		predicateNames.add("Iri");
		predicateNames.addAll(predicates.stream().map(prefix -> prefix.getName()).collect(Collectors.toList()));
		Sheet sheet = workbook.createSheet("Concept summary");
		addHeaders(sheet, 10000, predicateNames);
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
//		iri
		Cell iriCell = row.createCell(row.getLastCellNum() + 1);
		iriCell.setCellValue(summary.getIri());

		for (TTIriRef predicate : predicates) {
			Cell cell = row.createCell(row.getLastCellNum());
			TTValue value = summary.get(iri(predicate.getIri(), predicate.getName()));
			if (value.isIriRef()) {
				cell.setCellValue(value.asIriRef().getName());
			} else if (value.isLiteral()) {
				cell.setCellValue(value.asLiteral().getValue());
			} else if (value.isList()) {
				String result;
				List<String> names = value.asArray().getElements().stream().map(item -> item.asIriRef().getName()).collect(Collectors.toList());
				result = String.join(",", names);
				cell.setCellValue(result);
			} else {
				System.out.println("XLS helper encountered unexpected concept summary type while adding summaries to spreadsheet");
			}
		}
	}

	public void addHasSubTypes(List<EntityReferenceNode> childrenList) {
		Sheet sheet = workbook.createSheet("Has sub types");
		List<String> headers = Arrays.asList(new String[]{"Name", "Iri"});
		addHeaders(sheet, 20000, headers);

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
		List<String> headers = Arrays.asList(new String[]{"Name", "Iri"});
		addHeaders(sheet, 20000, headers);

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
		List<String> headers = Arrays.asList(new String[]{"Name", "Iri", "Type Name", "Type Iri"});
		addHeaders(sheet, 10000, headers);

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
		List<String> headers = Arrays.asList(new String[]{"Member type", "Member name", "Member iri", "Member code", "Scheme name", "Scheme iri", "Subset name", "Subset iri"});
		addHeaders(sheet, 10000, headers);

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
		List<String> headers = Arrays.asList(new String[]{"Included", "Member Name", "Member Iri", "Member Code", "Inherited Name", "Inherited Iri"});
        addHeaders(sheet, 10000, headers);

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

	public void addTerms(List<TermCode> terms) {
		Sheet sheet = workbook.createSheet("Terms");
		List<String> headers = Arrays.asList(new String[]{"Name", "Code", "Scheme"});
		addHeaders(sheet, 10000, headers);

		for (TermCode term : terms) {
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(term.getName());
			cell = row.createCell(1);
			cell.setCellValue(term.getCode());
			cell = row.createCell(2);
			cell.setCellValue(term.getScheme());
		}
	}

	private void addHeaders(Sheet sheet, int size, List<String> names) {
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
