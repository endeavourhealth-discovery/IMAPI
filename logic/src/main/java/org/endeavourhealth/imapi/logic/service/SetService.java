package org.endeavourhealth.imapi.logic.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.EntitySummary;
import org.endeavourhealth.imapi.model.IMv2v1Map;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.valuset.EditSet;
import org.endeavourhealth.imapi.transforms.ECLToTT;
import org.endeavourhealth.imapi.transforms.TTToECL;
import org.endeavourhealth.imapi.transforms.TTToTurtle;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class SetService {
    private static final Logger LOG = LoggerFactory.getLogger(SetService.class);

    private final SetRepository setRepository;
    private final EntityTripleRepository entityTripleRepository;
    private final EntityRepository entityRepository;

    public SetService() {
        setRepository = new SetRepositoryImpl();
        entityTripleRepository = new EntityTripleRepositoryImpl();
        entityRepository= new EntityRepositoryImpl();
    }


    /**
     * @param conceptSetIri
     * Evaluates a concept set
     * @return Set of concepts conforming to the concept sets definition
     */
    public Set<EntitySummary> evaluateConceptSet(String conceptSetIri, boolean includeLegacy) {
        LOG.debug("Load definition");
        TTBundle conceptSetBundle = entityTripleRepository.getEntityPredicates(conceptSetIri, Set.of(RDFS.LABEL.getIri(), IM.DEFINITION.getIri()), EntityService.UNLIMITED);

        return evaluateDefinition(conceptSetBundle.getEntity().get(IM.DEFINITION).asValue(), includeLegacy);
    }

    public Set<EntitySummary> evaluateDefinition(String ecl, boolean includeLegacy) throws DataFormatException {
        TTValue definition = new ECLToTT().getClassExpression(ecl);
        return evaluateDefinition(definition,includeLegacy);
    }

    /**
     * @param definition
     * Evaluates a definition
     * @return  Set of concepts conforming to the definition
     */
    public Set<EntitySummary> evaluateDefinition(TTValue definition, boolean includeLegacy) {
        LOG.debug("Evaluate");
        Set<EntitySummary> result = new HashSet<>();
        EditSet editSet = evaluateConceptSetNode(definition);

        if (editSet.getIncs() != null) result = editSet.getIncs();

        if (includeLegacy) {
            LOG.debug("Fetching legacy concepts for {} members", result.size());
            result.addAll(entityTripleRepository.getLegacyConceptSummaries(result));
        }

        LOG.debug("Found {} total concepts", result.size());

        return result;
    }

    private EditSet evaluateConceptSetNode(TTValue ttValue) throws DALException {
        EditSet result = new EditSet();
        if (ttValue.isNode()) {
            for (Map.Entry<TTIriRef, TTArray> predicateValue : ttValue.asNode().getPredicateMap().entrySet()) {
                if (SHACL.AND.equals(predicateValue.getKey())) {
                    processAND(result, predicateValue);
                } else if (SHACL.OR.equals(predicateValue.getKey())) {
                    processOR(result, predicateValue);
                } else if (SHACL.NOT.equals(predicateValue.getKey())) {
                    processNOT(result, predicateValue);
                } else {
                    result.addAllIncs(entityTripleRepository.getSubjectAndDescendantSummariesByPredicateObjectRelType(predicateValue.getKey().getIri(), predicateValue.getValue().asIriRef().getIri()));
                }
            }
        } else if (ttValue.isIriRef()) {
            result.addAllIncs(entityTripleRepository.getSubclassesAndReplacements(ttValue.asIriRef().getIri()));
        } else {
            throw new IllegalStateException("Unhandled concept set node type");
        }

        if (result.getIncs() != null && result.getExcs() != null) {
            result.getIncs().removeAll(result.getExcs());
            result.setExcs(null);
        }

        return result;
    }

    private void processAND(EditSet result, Map.Entry<TTIriRef, TTArray> ands) throws DALException {
        for (TTValue and : ands.getValue().iterator()) {
            EditSet andNode = evaluateConceptSetNode(and);

            if (andNode.getIncs() != null) {
                if (result.getIncs() == null) {
                    result.addAllIncs(andNode.getIncs());
                } else {
                    result.getIncs().retainAll(andNode.getIncs());
                }
            }

            if (andNode.getExcs() != null) {
                result.addAllExcs(andNode.getExcs());
            }
        }
    }

    private void processOR(EditSet result, Map.Entry<TTIriRef, TTArray> ors) {
        for (TTValue or : ors.getValue().iterator()) {
            EditSet orNode = evaluateConceptSetNode(or);

            if (orNode.getIncs() != null)
                result.addAllIncs(orNode.getIncs());

            if (orNode.getExcs() != null)
                throw new IllegalStateException("'OR NOT' currently unsupported");
        }
    }

    private void processNOT(EditSet result, Map.Entry<TTIriRef, TTArray> nots) {
        if (nots.getValue().isIriRef()) {
            result.addAllExcs(evaluateConceptSetNode(nots.getValue().asIriRef()).getIncs());
        } else {
            for (TTValue not : nots.getValue().iterator()) {
                EditSet notNode = evaluateConceptSetNode(not);

                if (notNode.getIncs() != null)
                    result.addAllExcs(notNode.getIncs());

                if (notNode.getExcs() != null)
                    throw new IllegalStateException("'NOT NOT' currently unsupported");
            }
        }
    }

    /**
     * Exports a single set definitions and expansions on the database
     * @param path  the output folder to place the output
     * @param setIri  IRI of the concept set
     */

    public void exportSingle(String path, String setIri) throws IOException {
        try (FileWriter definitions = new FileWriter(path + "\\ConceptSetDefinitions.txt");
             FileWriter expansions = new FileWriter(path + "\\ConceptSetCoreExpansions.txt");
             FileWriter legacies = new FileWriter(path + "\\ConceptSetLegacyExpansions.txt");
             FileWriter subsets = new FileWriter(path + "\\ConceptSetHierarchy.txt");
             FileWriter im1maps = new FileWriter(path + "\\IM1Map.txt")) {

            subsets.write("Parent set iri\tParent set name\tChild set iri\tChild set name\n");
            definitions.write("Set iri\tSet name\tSet definition ECL\tSet definition json-LD\n");
            expansions.write("Set iri\tSetName\tCore member code\tScheme\tCore member name\tiri\n");
            legacies.write("Set iri\tSet name\tLegacy member code\tScheme\tLegacy member name\tIri\n");
            im1maps.write("Set iri\tIM1 dbid\tMember IM2 iri\n");

            exportSingle(setIri, definitions, expansions, legacies, subsets, im1maps);

        }
    }

    private void exportSingle(String setIri, FileWriter definitions, FileWriter expansions, FileWriter legacies, FileWriter subsets, FileWriter im1maps) throws IOException {
        LOG.debug("Exporting {}...", setIri);

        TTEntity conceptSet = setRepository.getSetDefinition(setIri);
        if (conceptSet.get(IM.DEFINITION) != null)
            exportMembers(definitions, expansions, legacies, im1maps, conceptSet);

    }

     /**
	 * Exports all set  definitions and expansions on the database
	 * @param path  the output folder to place the output
	 * @param type  IRI of the set type
	 */
	public void exportAll(String path, TTIriRef type) throws IOException {
        Set<TTEntity> conceptSets = setRepository.getAllConceptSets(type);

        try (FileWriter definitions = new FileWriter(path + "\\ConceptSetDefinitions.txt");
             FileWriter expansions = new FileWriter(path + "\\ConceptSetCoreExpansions.txt");
             FileWriter legacies = new FileWriter(path + "\\ConceptSetLegacyExpansions.txt");
             FileWriter subsets = new FileWriter(path + "\\ConceptSetHierarchy.txt");
             FileWriter im1maps = new FileWriter(path + "\\IM1Map.txt")) {

            subsets.write("Parent set iri\tParent set name\tChild set iri\tChild set name\n");
            definitions.write("Set iri\tSet name\tSet definition ECL\tSet definition json-LD\n");
            expansions.write("Set iri\tSetName\tCore member code\tScheme\tCore member name\tiri\n");
            legacies.write("Set iri\tSet name\tLegacy member code\tScheme\tLegacy member name\tIri\n");
            im1maps.write("Set iri\tIM1 dbid\tMember IM2 iri\n");

            for (TTEntity conceptSet : conceptSets) {
                String setIri = conceptSet.getIri();
                LOG.debug("Exporting {}...", setIri);

                if (conceptSet.get(IM.DEFINITION) != null)
                    exportMembers(definitions, expansions, legacies, im1maps, conceptSet);


            }
        }
    }

    private void exportMembers(FileWriter definitions, FileWriter expansions, FileWriter legacies, FileWriter im1maps, TTEntity conceptSet) throws IOException {
        exportDefinition(definitions, conceptSet);

        exportExpansion(expansions, conceptSet);
        exportLegacy(legacies, conceptSet);
        exportIMv1(im1maps, conceptSet);
    }

    private void exportExpansion(FileWriter expansions, TTEntity conceptSet) throws IOException {
        TTEntity expanded = setRepository.getExpansion(conceptSet);
        for (TTValue value : expanded.get(IM.DEFINITION).iterator()) {
            TTEntity member = (TTEntity) value.asNode();
            String code = member.getCode();
            String scheme = member.getScheme().getIri();
            expansions.write(
                conceptSet.getIri()+"\t"+ conceptSet.getName()+"\t"+ code + "\t" + scheme + "\t" + member.getName()+"\t"+member.getIri() + "\n");
        }
    }

    private void exportLegacy(FileWriter legacies, TTEntity conceptSet) throws IOException {
        TTEntity legacy = setRepository.getLegacyExpansion(conceptSet);
        if (legacy.get(IM.DEFINITION)!=null) {
            for (TTValue value : legacy.get(IM.DEFINITION).iterator()) {
                TTEntity member = (TTEntity) value.asNode();
                String code = member.getCode();
                String scheme = member.getScheme().getIri();
                legacies.write(
                    conceptSet.getIri()+"\t"+ conceptSet.getName()+"\t"+ code + "\t" + scheme + "\t" + member.getName()+"\t"+member.getIri() + "\n");
            }
        }
    }

    private void exportIMv1(FileWriter im1maps, TTEntity conceptSet) throws IOException {
        TTEntity im1 = setRepository.getIM1Expansion(conceptSet);
        if (im1.get(IM.DEFINITION)!=null){
            for (TTValue value : im1.get(IM.DEFINITION).iterator()) {
                TTEntity member = (TTEntity) value.asNode();
                String code = member.getCode();
                String scheme = member.getScheme().getIri();
                String im1id = member.get(iri(IM.NAMESPACE + "im1dbid")).asLiteral().getValue();
                im1maps.write(conceptSet.getIri()+"\t" + im1id + "\t"+scheme + code+"\n");
            }
        }
    }

    private void exportDefinition(FileWriter definitions, TTEntity conceptSet) throws IOException {

        TTToTurtle turtleConverter = new TTToTurtle();
        List<Namespace> namespaces = entityTripleRepository.findNamespaces();
        TTContext context = new TTContext();
        for(Namespace namespace : namespaces){
            context.add(namespace.getIri(), namespace.getPrefix(), namespace.getName());
        }
        String turtle = turtleConverter.transformEntity(conceptSet, context);
        //ecl only supports snomed and discovery concepts
        try {
            String ecl = TTToECL.getExpressionConstraint(conceptSet.get(IM.DEFINITION),true);
            definitions.write(conceptSet.getIri() + "\t" + conceptSet.getName() + "\t" + ecl + "\t" + turtle + "\n");
        } catch (DataFormatException e){
            definitions.write(conceptSet.getIri() + "\t" + conceptSet.getName() + "\t" + "" + "\t" + turtle + "\n");
        }
    }





    // Excel export

    public Workbook getExcelDownload(String iri, boolean expand, boolean v1) {
        TTEntity set = setRepository.getSetDefinition(iri);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFFont font = workbook.createFont();
        CellStyle headerStyle = workbook.createCellStyle();
        font.setBold(true);
        headerStyle.setFont(font);

        addDefinitionsToWorkbook(set, workbook, headerStyle);

        if (expand) {
            Set<EntitySummary> members = evaluateDefinition(set.get(IM.DEFINITION).asValue(), true);
            addExpandedToWorkbook(set, workbook, headerStyle, members);

            if (v1) {
                addV1MapsToWorkbook(workbook, headerStyle, members);
            }
        }



        return workbook;
    }

    private void addV1MapsToWorkbook(Workbook workbook, CellStyle headerStyle, Set<EntitySummary> members) {
        Sheet sheet;
        Row row;
        Set<IMv2v1Map> im1 = setRepository.getIMv2v1Maps(members);
        if (!im1.isEmpty()){
            sheet = workbook.createSheet("IM v1 Map");
            addHeaders(sheet, headerStyle, 10000, "IMv2 Code", "IMv2 Scheme", "IMv1 Dbid");

            for (IMv2v1Map member : im1) {
                String code = member.getV2Code();
                String scheme = member.getV2Scheme();
                String im1id = member.getV1Dbid().toString();
                row = addRow(sheet);
                addCells(row, code, scheme, im1id);
            }
        }
    }

    private void addExpandedToWorkbook(TTEntity set, Workbook workbook, CellStyle headerStyle, Set<EntitySummary> members) {
        Sheet sheet;

        sheet = workbook.createSheet("Expanded");
        addHeaders(sheet, headerStyle, 10000, "Set Iri", "Set Name", "Member Iri", "Member Name", "Code", "Scheme");


        addEntityMembersToWorkbook(set, sheet, members);
    }

    private void addEntityMembersToWorkbook(TTEntity set, Sheet sheet, Set<EntitySummary> members) {
        Row row;

        for(EntitySummary member: members) {
            row = addRow(sheet);
            String scheme = (member.getScheme() == null) ? null : member.getScheme().getName();
            addCells(row, set.getIri(), set.getName(), member.getIri(), member.getName(), member.getCode(), scheme);
        }
    }

    private void addDefinitionsToWorkbook(TTEntity set, Workbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet("Concept summary");
        addHeaders(sheet, headerStyle, 10000, "Iri", "Name", "ECL", "Turtle");
        Row row = addRow(sheet);

        TTToTurtle turtleConverter = new TTToTurtle();
        List<Namespace> namespaces = entityTripleRepository.findNamespaces();
        TTContext context = new TTContext();
        for(Namespace namespace : namespaces){
            context.add(namespace.getIri(), namespace.getPrefix(), namespace.getName());
        }
        String turtle = turtleConverter.transformEntity(set, context);

        try {
            String ecl = TTToECL.getExpressionConstraint(set.get(IM.DEFINITION),true);
            addCells(row, set.getIri(), set.getName(), ecl, turtle);
        } catch (DataFormatException e){
            addCells(row, set.getIri(), set.getName(), "Error", turtle);

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

    public SearchResponse eclSearch(boolean includeLegacy, Integer limit, String ecl) throws DataFormatException {
        Set<EntitySummary> evaluated = evaluateDefinition(ecl, includeLegacy);
        List<SearchResultSummary> evaluatedAsSummary = evaluated.stream().limit(limit != null ? limit : 1000).map(ttIriRef -> {
            try {
                return entityRepository.getEntitySummaryByIri(ttIriRef.getIri());
            } catch (DALException e) {
                return new SearchResultSummary().setIri(ttIriRef.getIri()).setName(ttIriRef.getName());
            }
        }).collect(Collectors.toList());
        SearchResponse result = new SearchResponse();
        result.setEntities(evaluatedAsSummary);
        result.setCount(evaluated.size());
        result.setPage(1);
        return result;
    }
}
