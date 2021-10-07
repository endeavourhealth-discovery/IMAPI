package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.repository.SetRepository;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.transforms.TTToECL;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.zip.DataFormatException;

public class SetService {
    private SetRepository setRepository;

    public SetService() {
        setRepository = new SetRepository();
    }

    /**
     * Exports a single set definitions and expansions on the database
     * @param path  the output folder to place the output
     * @param setIri  IRI of the concept set
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void exportSingle(String path, String setIri) throws SQLException, ClassNotFoundException, IOException {
        try (FileWriter definitions = new FileWriter(path + "\\ConceptSetDefinitions.txt");
             FileWriter expansions = new FileWriter(path + "\\ConceptSetCoreExpansions.txt");
             FileWriter legacies = new FileWriter(path + "\\ConceptSetLegacyExpansions.txt");
             FileWriter subsets = new FileWriter(path + "\\ConceptSetHierarchy.txt");
             FileWriter im1maps = new FileWriter(path + "\\IM1Map.txt");) {

            subsets.write("Parent set iri\tParent set name\tChild set iri\tChild set name\n");
            definitions.write("Set iri\tSet name\tSet definition ECL\tSet definition json-LD\n");
            expansions.write("Set iri\tSetName\tCore member code\tScheme\tCore member name\tiri\n");
            legacies.write("Set iri\tSet name\tLegacy member code\tScheme\tLegacy member name\tIri\n");
            im1maps.write("Set iri\tIM1 dbid\tMember IM2 iri\n");

            exportSingle(setIri, definitions, expansions, legacies, subsets, im1maps);

        }
    }

    private void exportSingle(String setIri, FileWriter definitions, FileWriter expansions, FileWriter legacies, FileWriter subsets, FileWriter im1maps) throws SQLException, IOException {
        System.out.println("Exporting " + setIri + "..");

        TTEntity conceptSet = setRepository.getSetDefinition(setIri);
        if (conceptSet.get(IM.HAS_MEMBER) != null)
            exportMembers(definitions, expansions, legacies, im1maps, conceptSet);

        if (conceptSet.get(IM.HAS_SUBSET) != null)
            exportSubsetWithExpansion(definitions, expansions, legacies, subsets, im1maps, conceptSet, setIri);
    }

    /**
	 * Exports all set  definitions and expansions on the database
	 * @param path  the output folder to place the output
	 * @param type  IRI of the set type
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void exportAll(String path, TTIriRef type) throws SQLException, ClassNotFoundException, IOException {
        Set<TTEntity> conceptSets = setRepository.getAllConceptSets(type);

        try (FileWriter definitions = new FileWriter(path + "\\ConceptSetDefinitions.txt");
             FileWriter expansions = new FileWriter(path + "\\ConceptSetCoreExpansions.txt");
             FileWriter legacies = new FileWriter(path + "\\ConceptSetLegacyExpansions.txt");
             FileWriter subsets = new FileWriter(path + "\\ConceptSetHierarchy.txt");
             FileWriter im1maps = new FileWriter(path + "\\IM1Map.txt");) {

            subsets.write("Parent set iri\tParent set name\tChild set iri\tChild set name\n");
            definitions.write("Set iri\tSet name\tSet definition ECL\tSet definition json-LD\n");
            expansions.write("Set iri\tSetName\tCore member code\tScheme\tCore member name\tiri\n");
            legacies.write("Set iri\tSet name\tLegacy member code\tScheme\tLegacy member name\tIri\n");
            im1maps.write("Set iri\tIM1 dbid\tMember IM2 iri\n");

            for (TTEntity conceptSet : conceptSets) {
                String setIri = conceptSet.getIri();
                System.out.println("Exporting " + setIri + "..");

                if (conceptSet.get(IM.HAS_MEMBER) != null)
                    exportMembers(definitions, expansions, legacies, im1maps, conceptSet);

                if (conceptSet.get(IM.HAS_SUBSET) != null) {
                    exportSubsets(subsets, conceptSet, setIri);
                }
            }
        }
    }

    private void exportMembers(FileWriter definitions, FileWriter expansions, FileWriter legacies, FileWriter im1maps, TTEntity conceptSet) throws IOException, SQLException {
        exportDefinition(definitions, conceptSet);

        exportExpansion(expansions, conceptSet);
        exportLegacy(legacies, conceptSet);
        exportIMv1(im1maps, conceptSet);
    }

    private void exportExpansion(FileWriter expansions, TTEntity conceptSet) throws SQLException, IOException {
        TTEntity expanded = setRepository.getExpansion(conceptSet);
        for (TTValue value : expanded.get(IM.HAS_MEMBER).asArray().getElements()) {
            TTEntity member = (TTEntity) value.asNode();
            String code = member.getCode();
            String scheme = member.getScheme().getIri();
            expansions.write(
                conceptSet.getIri()+"\t"+ conceptSet.getName()+"\t"+ code + "\t" + scheme + "\t" + member.getName()+"\t"+member.getIri() + "\n");
        }
    }

    private void exportLegacy(FileWriter legacies, TTEntity conceptSet) throws SQLException, IOException {
        TTEntity legacy = setRepository.getLegacyExpansion(conceptSet);
        if (legacy.get(IM.HAS_MEMBER)!=null) {
            for (TTValue value : legacy.get(IM.HAS_MEMBER).asArray().getElements()) {
                TTEntity member = (TTEntity) value.asNode();
                String code = member.getCode();
                String scheme = member.getScheme().getIri();
                legacies.write(
                    conceptSet.getIri()+"\t"+ conceptSet.getName()+"\t"+ code + "\t" + scheme + "\t" + member.getName()+"\t"+member.getIri() + "\n");
            }
        }
    }

    private void exportIMv1(FileWriter im1maps, TTEntity conceptSet) throws SQLException, IOException {
        TTEntity im1 = setRepository.getIM1Expansion(conceptSet);
        if (im1.get(IM.HAS_MEMBER)!=null){
            for (TTValue value : im1.get(IM.HAS_MEMBER).asArray().getElements()) {
                TTEntity member = (TTEntity) value.asNode();
                String code = member.getCode();
                String scheme = member.getScheme().getIri();
                String im1id = member.get(TTIriRef.iri(IM.NAMESPACE + "im1dbid")).asLiteral().getValue();
                im1maps.write(conceptSet.getIri()+"\t" + im1id + "\t"+scheme + code+"\n");
            }
        }
    }

    private void exportDefinition(FileWriter definitions, TTEntity conceptSet) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        String json = objectMapper.writeValueAsString(conceptSet);
        TTToECL eclConverter = new TTToECL();
        //ecl only supports snomed and discovery concepts
        try {
            String ecl = eclConverter.getConceptSetECL(conceptSet, null);
            definitions.write(conceptSet.getIri() + "\t" + conceptSet.getName() + "\t" + ecl + "\t" + json + "\n");
        } catch (DataFormatException e){
            definitions.write(conceptSet.getIri() + "\t" + conceptSet.getName() + "\t" + "" + "\t" + json + "\n");
        }
    }

    private void exportSubsets(FileWriter subsets, TTEntity conceptSet, String setIri) throws IOException {
        System.out.println("Exporting subset " + setIri + "..");

        for (TTValue value : conceptSet.get(IM.HAS_SUBSET).asArray().getElements()) {
            subsets.write(conceptSet.getIri() + "\t" + conceptSet.getName()+"\t"+
                value.asIriRef().getIri()+ "\t"+ value.asIriRef().getName()+"\n");
        }
    }


    private void exportSubsetWithExpansion(FileWriter definitions, FileWriter expansions, FileWriter legacies, FileWriter subsets, FileWriter im1maps, TTEntity conceptSet, String setIri) throws IOException, SQLException {
        System.out.println("Exporting subset " + setIri + "..");

        for (TTValue value : conceptSet.get(IM.HAS_SUBSET).asArray().getElements()) {
            subsets.write(conceptSet.getIri() + "\t" + conceptSet.getName()+"\t"+
                value.asIriRef().getIri()+ "\t"+ value.asIriRef().getName()+"\n");

            exportSingle(value.asIriRef().getIri(), definitions, expansions, legacies, subsets, im1maps);
        }
    }

    public Workbook getExcelDownload(String iri, boolean members, boolean expand, boolean v1) throws JsonProcessingException, SQLException {
        TTEntity set = setRepository.getSetDefinition(iri);

        Workbook workbook = new XSSFWorkbook();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        CellStyle headerStyle = workbook.createCellStyle();
        font.setBold(true);
        headerStyle.setFont(font);

        Sheet sheet = workbook.createSheet("Concept summary");
        addHeaders(sheet, headerStyle, 10000, "Iri", "Name");
        Row row = addRow(sheet);
        addCells(row, set.getIri(), set.getName());

        if (members) {
            addMembersToWorkbook(set, workbook, headerStyle);
        }

        if (expand) {
            addExpandedToWorkbook(set, workbook, headerStyle);
        }

        if (v1) {
            addV1MapsToWorkbook(set, workbook, headerStyle);
        }

        return workbook;
    }

    private void addV1MapsToWorkbook(TTEntity set, Workbook workbook, CellStyle headerStyle) throws SQLException {
        Sheet sheet;
        Row row;
        TTEntity im1 = setRepository.getIM1Expansion(set);
        if (im1.has(IM.HAS_MEMBER)){
            sheet = workbook.createSheet("IM v1 Map");
            addHeaders(sheet, headerStyle, 10000, "IMv2 Code", "IMv2 Scheme", "IMv1 Dbid");

            for (TTValue value : im1.get(IM.HAS_MEMBER).asArray().getElements()) {
                TTEntity member = (TTEntity) value.asNode();
                String code = member.getCode();
                String scheme = member.getScheme().getIri();
                String im1id = member.get(TTIriRef.iri(IM.NAMESPACE + "im1dbid")).asLiteral().getValue();
                row = addRow(sheet);
                addCells(row, code, scheme, im1id);
            }
        }
    }

    private void addExpandedToWorkbook(TTEntity set, Workbook workbook, CellStyle headerStyle) throws SQLException {
        Sheet sheet;
        Row row;
        sheet = workbook.createSheet("Expanded");
        addHeaders(sheet, headerStyle, 10000, "Iri", "Name", "Code", "Scheme");
        TTEntity expanded = setRepository.getExpansion(set);
        if (expanded.has(IM.HAS_MEMBER)) {
            for (TTValue value : expanded.get(IM.HAS_MEMBER).asArray().getElements()) {
                TTEntity member = (TTEntity) value.asNode();
                String code = member.getCode();
                String scheme = member.getScheme().getIri();
                row = addRow(sheet);
                addCells(row, member.getIri(), member.getName(), code, scheme);
            }
        }

        expanded = setRepository.getLegacyExpansion(set);
        if (expanded.has(IM.HAS_MEMBER)) {
            for (TTValue value : expanded.get(IM.HAS_MEMBER).asArray().getElements()) {
                TTEntity member = (TTEntity) value.asNode();
                String code = member.getCode();
                String scheme = member.getScheme().getIri();
                row = addRow(sheet);
                addCells(row, member.getIri(), member.getName(), code, scheme);
            }
        }
    }

    private void addMembersToWorkbook(TTEntity set, Workbook workbook, CellStyle headerStyle) throws JsonProcessingException {
        Sheet sheet;
        Row row;
        sheet = workbook.createSheet("Definitions");
        addHeaders(sheet, headerStyle, 10000, "ECL", "JSON");
        row = addRow(sheet);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        String json = objectMapper.writeValueAsString(set);
        TTToECL eclConverter = new TTToECL();

        try {
            String ecl = eclConverter.getConceptSetECL(set, null);
            addCells(row, ecl, json);
        } catch (DataFormatException e){
            addCells(row, "ERROR", json);

        }
    }

    private void addHeaders(Sheet sheet, CellStyle headerStyle, int size, String... names) {
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

    private Row addRow(Sheet sheet) {
	    return sheet.createRow(sheet.getLastRowNum() + 1);
    }

    private void addCells(Row row, String... values) {
	    for(String value: values) {
            Cell iriCell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum());
            iriCell.setCellValue(value);
	    }
    }
}
