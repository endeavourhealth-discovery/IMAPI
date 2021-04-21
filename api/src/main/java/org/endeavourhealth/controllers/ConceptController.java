package org.endeavourhealth.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.converters.ConceptToImLang;
import org.endeavourhealth.dataaccess.ConceptServiceV3;
import org.endeavourhealth.dto.ConceptDto;
import org.endeavourhealth.dto.GraphDto;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/concept")
@CrossOrigin(origins = "*")
public class ConceptController {

	@Autowired
	ConceptServiceV3 conceptService;

	@Autowired
	ConceptToImLang conceptToImLang;

	@PostMapping(value = "/search")
	public SearchResponse advancedSearch(@RequestBody SearchRequest request) {
		return new SearchResponse().setConcepts(conceptService.advancedSearch(request));
	}

	@GetMapping(value = "", produces = "application/json")
	public TTConcept getConcept(@RequestParam(name = "iri") String iri) {
		return conceptService.getConcept(iri);
	}

	@GetMapping(value = "", produces = "application/imlang")
	public String getConceptImLang(@RequestParam(name = "iri") String iri) {
		return conceptToImLang.translateConceptToImLang(conceptService.getConcept(iri));
	}

	@GetMapping(value = "/children")
	public List<ConceptReferenceNode> getConceptChildren(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "includeLegacy", required = false) boolean includeLegacy) {
		return conceptService.getImmediateChildren(iri, page, size, includeLegacy);
	}

	@GetMapping(value = "/children/download", produces = { "text/csv" })
	public byte[] downloadChildren(@RequestParam(name = "iri") String iri) {
		List<ConceptReferenceNode> descendants = conceptService.getDescendants(iri);
		return null;
	}

	@GetMapping(value = "/download")
	public HttpEntity download(@RequestParam String iri, @RequestParam String format, @RequestParam boolean children,
			@RequestParam boolean parents, @RequestParam boolean properties, @RequestParam boolean members,
			@RequestParam boolean roles, @RequestParam boolean inactive) {
		TTConcept concept = getConcept(iri);

		Workbook workbook = new XSSFWorkbook();
		
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		CellStyle headerStyle = workbook.createCellStyle();
		font.setBold(true);
		headerStyle.setFont(font);

		if (children) {
			List<ConceptReferenceNode> childrenList = conceptService.getImmediateChildren(iri, null, null, false);
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

			for (ConceptReferenceNode child : childrenList) {
				Row row = sheet.createRow(sheet.getLastRowNum() + 1);
				Cell cell = row.createCell(0);
				cell.setCellValue(child.getName());
				cell = row.createCell(1);
				cell.setCellValue(child.getIri());
			}
		}

		if (parents) {
			List<TTValue> parentList = new ArrayList<TTValue>();

			TTValue value = concept.get(IM.IS_A);
			if (value != null) {
				if (value.isList()) {
					parentList = concept.getAsArrayElements(IM.IS_A);
				} else {
					parentList.add(value);
				}
			}

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

		if (properties) {
			List<PropertyValue> propertyList = getAllProperties(iri);
			Sheet sheet = workbook.createSheet("Properties");
			sheet.setColumnWidth(0, 10000);
			sheet.setColumnWidth(1, 10000);
			sheet.setColumnWidth(2, 10000);
			sheet.setColumnWidth(3, 10000);
			sheet.setColumnWidth(4, 10000);
			sheet.setColumnWidth(5, 10000);

			Row header = sheet.createRow(0);
			Cell headerCell = header.createCell(0);
			headerCell.setCellValue("Property Name");
			headerCell.setCellStyle(headerStyle);
			headerCell = header.createCell(1);
			headerCell.setCellValue("Property Iri");
			headerCell.setCellStyle(headerStyle);
			headerCell = header.createCell(2);
			headerCell.setCellValue("ValueType Name");
			headerCell.setCellStyle(headerStyle);
			headerCell = header.createCell(3);
			headerCell.setCellValue("ValueType Iri");
			headerCell.setCellStyle(headerStyle);
			headerCell = header.createCell(4);
			headerCell.setCellValue("InheritedFrom Name");
			headerCell.setCellStyle(headerStyle);
			headerCell = header.createCell(5);
			headerCell.setCellValue("InheritedFrom Iri");
			headerCell.setCellStyle(headerStyle);

			for (PropertyValue property : propertyList) {
				Row row = sheet.createRow(sheet.getLastRowNum() + 1);
				Cell cell = row.createCell(0);
				cell.setCellValue(property.getProperty().getName());
				cell = row.createCell(1);
				cell.setCellValue(property.getProperty().getIri());
				cell = row.createCell(2);
				cell.setCellValue(property.getValueType().getName());
				cell = row.createCell(3);
				cell.setCellValue(property.getValueType().getIri());

				if (null != property.getInheritedFrom()) {
					cell = row.createCell(4);
					cell.setCellValue(property.getInheritedFrom().getName());
					cell = row.createCell(5);
					cell.setCellValue(property.getInheritedFrom().getIri());
				}
			}
		}

		if (members) {
			ExportValueSet exportValueSet = conceptService.getValueSetMembers(iri, false);
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
				cell.setCellValue(c.getConcept().getName());
				cell = row.createCell(2);
				cell.setCellValue(c.getConcept().getIri());
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
				cell.setCellValue(c.getConcept().getName());
				cell = row.createCell(2);
				cell.setCellValue(c.getConcept().getIri());
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

		if (roles) {
			List<PropertyValue> roleList = getRoles(iri);
			Sheet sheet = workbook.createSheet("Roles");
			sheet.setColumnWidth(0, 10000);
			sheet.setColumnWidth(1, 10000);
			sheet.setColumnWidth(2, 10000);
			sheet.setColumnWidth(3, 10000);
			sheet.setColumnWidth(4, 10000);
			sheet.setColumnWidth(5, 10000);

			Row header = sheet.createRow(0);
			Cell headerCell = header.createCell(0);
			headerCell.setCellValue("Role Name");
			headerCell.setCellStyle(headerStyle);
			headerCell = header.createCell(1);
			headerCell.setCellValue("Role Iri");
			headerCell.setCellStyle(headerStyle);
			headerCell = header.createCell(2);
			headerCell.setCellValue("ValueType Name");
			headerCell.setCellStyle(headerStyle);
			headerCell = header.createCell(3);
			headerCell.setCellValue("ValueType Iri");
			headerCell.setCellStyle(headerStyle);

			for (PropertyValue property : roleList) {
				Row row = sheet.createRow(sheet.getLastRowNum() + 1);
				Cell cell = row.createCell(0);
				cell.setCellValue(property.getProperty().getName());
				cell = row.createCell(1);
				cell.setCellValue(property.getProperty().getIri());
				cell = row.createCell(2);
				cell.setCellValue(property.getValueType().getName());
				cell = row.createCell(3);
				cell.setCellValue(property.getValueType().getIri());
			}
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "force-download"));
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + concept.getName() + ".xlsx");

		return new HttpEntity(outputStream.toByteArray(), headers);
	}

	@GetMapping(value = "/parents")
	public List<ConceptReferenceNode> getConceptParents(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "includeLegacy", required = false) boolean includeLegacy) {
		return conceptService.getImmediateParents(iri, page, size, includeLegacy);
	}

	@GetMapping(value = "/parents/definitions")
	public List<TTConcept> getConceptAncestorDefinitions(@RequestParam(name = "iri") String iri) {
		return conceptService.getAncestorDefinitions(iri);
	}

	@GetMapping(value = "/usages")
	public List<ConceptSummary> conceptUsages(@RequestParam(name = "iri") String iri) {
		return conceptService.usages(iri);
	}

	@GetMapping(value = "/mappedFrom")
	public List<TTIriRef> getCoreMappedFromLegacy(@RequestParam(name = "iri") String legacyIri) {
		return conceptService.getCoreMappedFromLegacy(legacyIri);
	}

	@GetMapping(value = "/mappedTo")
	public List<TTIriRef> getLegacyMappedToCore(@RequestParam(name = "iri") String coreIri) {
		return conceptService.getLegacyMappedToCore(coreIri);
	}

	@PostMapping(value = "/isWhichType")
	public List<TTIriRef> conceptIsWhichType(@RequestParam(name = "iri") String iri,
			@RequestBody List<String> candidates) {
		return conceptService.isWhichType(iri, candidates);
	}

	@GetMapping(value = "/members")
	public ExportValueSet valueSetMembersJson(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "expanded", required = false) boolean expanded) {
		return conceptService.getValueSetMembers(iri, expanded);
	}

	@GetMapping(value = "/members", produces = { "text/csv" })
	public String valueSetMembersCSV(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "expanded", required = false) boolean expanded) {
		ExportValueSet exportValueSet = conceptService.getValueSetMembers(iri, expanded);

		StringBuilder sb = new StringBuilder();

		sb.append(
				"Inc\\Exc\tValueSetIri\tValueSetName\tMemberIri\tMemberTerm\tMemberCode\tMemberSchemeIri\tMemberSchemeName\n");

		for (ValueSetMember c : exportValueSet.getIncluded()) {
			sb.append("Inc\t").append(exportValueSet.getValueSet().getIri()).append("\t")
					.append(exportValueSet.getValueSet().getName()).append("\t").append(c.getConcept().getIri())
					.append("\t").append(c.getConcept().getName()).append("\t").append(c.getCode()).append("\t");
			if (c.getScheme() != null)
				sb.append(c.getScheme().getIri()).append("\t").append(c.getScheme().getName());

			sb.append("\n");
		}

		if (exportValueSet.getExcluded() != null) {
			for (ValueSetMember c : exportValueSet.getExcluded()) {
				sb.append("Exc\t").append(exportValueSet.getValueSet().getIri()).append("\t")
						.append(exportValueSet.getValueSet().getName()).append("\t").append(c.getConcept().getIri())
						.append("\t").append(c.getConcept().getName()).append("\t").append(c.getCode()).append("\t");
				if (c.getScheme() != null)
					sb.append(c.getScheme().getIri()).append("\t").append(c.getScheme().getName());

				sb.append("\n");
			}
		}

		return sb.toString();
	}

	@GetMapping(value = "/isMemberOf")
	public ValueSetMembership isMemberOfValueSet(@RequestParam(name = "iri") String conceptIri,
			@RequestParam("valueSetIri") String valueSetIri) {
		return conceptService.isValuesetMember(valueSetIri, conceptIri);
	}

	@GetMapping(value = "/referenceSuggestions")
	public List<TTIriRef> getSuggestions(@RequestParam String keyword, @RequestParam String word) {
//    	TODO generate and return suggestions
		return new ArrayList<TTIriRef>(Arrays.asList(new TTIriRef(":961000252104", "method (attribute)"),
				new TTIriRef(":1271000252102", "Hospital inpatient admission"),
				new TTIriRef(":1911000252103", "Transfer event")));
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public TTConcept createConcept(@RequestBody ConceptDto conceptDto) {
//    	TODO convert conceptDto to concept
//    	TODO save concept
		return new TTConcept().setCode(conceptDto.getCode());
	}

	@GetMapping(value = "/roles")
	public List<PropertyValue> getRoles(@RequestParam(name = "iri") String iri) {
		TTConcept concept = conceptService.getConcept(iri);
		List<PropertyValue> roles = new ArrayList<PropertyValue>();

		if (concept.has(IM.ROLE_GROUP)) {
			for (TTValue roleGroup : concept.getAsArray(IM.ROLE_GROUP).getElements()) {
				if (roleGroup.isNode()) {
					HashMap<TTIriRef, TTValue> role = roleGroup.asNode().getPredicateMap();
					role.forEach((key, value) -> {
						if (key.getIri() != "http://endhealth.info/im#counter") {
							PropertyValue pv = new PropertyValue().setProperty(key).setValueType(value.asIriRef());
							roles.add(pv);
						}
					});
				}
			}
		}

		return roles;
	}

	@GetMapping(value = "/properties")
	public List<PropertyValue> getAllProperties(@RequestParam(name = "iri") String iri) {
		TTConcept concept = conceptService.getConcept(iri);
		List<PropertyValue> properties = new ArrayList<PropertyValue>();

		if (concept.has(IM.PROPERTY_GROUP)) {
			for (TTValue propertyGroup : concept.getAsArray(IM.PROPERTY_GROUP).getElements()) {
				if (propertyGroup.isNode()) {
					TTIriRef inheritedFrom = propertyGroup.asNode().has(IM.INHERITED_FROM)
							? propertyGroup.asNode().get(IM.INHERITED_FROM).asIriRef()
							: null;

					if (propertyGroup.asNode().has(SHACL.PROPERTY)) {
						for (TTValue property : propertyGroup.asNode().get(SHACL.PROPERTY).asArray().getElements()) {
							TTIriRef propertyPath = property.asNode().get(SHACL.PATH).asIriRef();
							if (properties.stream()
									.noneMatch(o -> o.getProperty().getIri().equals(propertyPath.getIri()))) {
								PropertyValue pv = new PropertyValue().setInheritedFrom(inheritedFrom)
										.setProperty(propertyPath);

								if (property.asNode().has(SHACL.CLASS))
									pv.setValueType(property.asNode().get(SHACL.CLASS).asIriRef());
								if (property.asNode().has(SHACL.DATATYPE))
									pv.setValueType(property.asNode().get(SHACL.DATATYPE).asIriRef());

								properties.add(pv);
							}
						}
					}
				}
			}
		}
		return properties;
	}

	@GetMapping(value = "/graph")
	public GraphDto getGraphData(@RequestParam(name = "iri") String iri) {
		TTConcept concept = conceptService.getConcept(iri);

		GraphDto graphData = new GraphDto().setIri(concept.getIri()).setName(concept.getName());

		GraphDto graphParents = new GraphDto().setName("Parents");
		GraphDto graphChildren = new GraphDto().setName("Children");
		GraphDto graphProps = new GraphDto().setName("Properties");
		GraphDto graphInheritedProps = new GraphDto().setName("Inherited");
		GraphDto graphDirectProps = new GraphDto().setName("Direct");
		GraphDto graphRoles = new GraphDto().setName("Roles");

		List<TTValue> parents = new ArrayList<TTValue>();

		TTValue value = concept.get(IM.IS_A);
		if (value != null) {
			if (value.isList()) {
				parents = concept.getAsArrayElements(IM.IS_A);
			} else {
				parents.add(value);
			}
		}

		List<ConceptReferenceNode> children = conceptService.getImmediateChildren(iri, null, null, false);
		List<PropertyValue> properties = getAllProperties(iri);
		List<PropertyValue> roles = getRoles(iri);

		if (parents != null) {
			parents.forEach(parent -> {
				GraphDto graphParent = new GraphDto().setIri(parent.asIriRef().getIri())
						.setName(parent.asIriRef().getName()).setPropertyType("is a");
				graphParents.getChildren().add(graphParent);
			});
		}

		children.forEach(child -> {
			GraphDto graphChild = new GraphDto().setIri(child.getIri()).setName(child.getName());
			graphChildren.getChildren().add(graphChild);
		});

		properties.forEach(prop -> {
			if (null != prop.getInheritedFrom()) {
				GraphDto graphProp = new GraphDto().setIri(prop.getProperty().getIri())
						.setName(prop.getProperty().getName()).setInheritedFromName(prop.getInheritedFrom().getName())
						.setInheritedFromIri(prop.getInheritedFrom().getIri())
						.setPropertyType(prop.getProperty().getName()).setValueTypeIri(prop.getValueType().getIri())
						.setValueTypeName(prop.getValueType().getName());
				graphInheritedProps.getChildren().add(graphProp);
			} else {
				GraphDto graphProp = new GraphDto().setIri(prop.getProperty().getIri())
						.setName(prop.getProperty().getName()).setPropertyType(prop.getProperty().getName())
						.setValueTypeIri(prop.getValueType().getIri()).setValueTypeName(prop.getValueType().getName());
				graphDirectProps.getChildren().add(graphProp);
			}
		});

		roles.forEach(role -> {
			GraphDto graphRole = new GraphDto().setIri(role.getProperty().getIri())
					.setName(role.getProperty().getName()).setPropertyType(role.getProperty().getName())
					.setValueTypeIri(role.getValueType().getIri()).setValueTypeName(role.getValueType().getName());
			graphRoles.getChildren().add(graphRole);
		});

		graphProps.getChildren().add(graphDirectProps);
		graphProps.getChildren().add(graphInheritedProps);

		graphData.getChildren().add(graphParents);
		graphData.getChildren().add(graphChildren);
		graphData.getChildren().add(graphProps);
		graphData.getChildren().add(graphRoles);

		return graphData;
	}

	@GetMapping("/synonyms")
	public List<String> getSynonyms(@RequestParam(name = "iri") String iri) {
		return conceptService.getSynonyms(iri);
	}

	public List<String> getFlatParentHierarchy(String iri, List<String> flatParentIris) {
		List<ConceptReferenceNode> parents = conceptService.getImmediateParents(iri, null, null, false);

		if (parents == null) {
			return flatParentIris;
		}

		for (ConceptReferenceNode parent : parents) {
			flatParentIris.add(parent.getIri());
			getFlatParentHierarchy(parent.getIri(), flatParentIris);
		}

		return flatParentIris;
	}
}
