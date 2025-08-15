package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCCriterion;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCFolder;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;
import org.endeavourhealth.imapi.transforms.eqd.EnquiryDocument;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdToIMQ {
  private static final Logger log = LoggerFactory.getLogger(EqdToIMQ.class);
  public static Set<String> gmsPatients = new HashSet<>();
  public static Map<String, TTEntity> definitionToEntity = new HashMap<>();
  public static Map<String, TTEntity> setIriToEntity = new HashMap<>();
  public static Map<String, String> versionMap = new HashMap<>();
  public static Integer setNumber;
  @Setter
  @Getter
  private static Map<String, EQDOCCriterion> libraryItems;
  private final Map<String, Match> criteriaLibrary = new HashMap<>();
  private final Map<String, Integer> criteriaLibraryCount = new HashMap<>();
  private final ObjectMapper mapper = new ObjectMapper();
  private Namespace namespace;
  private EqdResources resources;
  private TTDocument document;
  private String singleEntity;
  private boolean versionIndependent;
  private List<Graph> graphs;


  public EqdToIMQ(boolean versionIndependent,List<Graph> graphs) {
    this.versionIndependent = versionIndependent;
    this.graphs = graphs;
    gmsPatients.add("c8d3ca80-ba23-418b-8cef-e5afac42764e");
  }

  public static Integer getSetNumber() {
    return setNumber;
  }

  public static void incrementSetNumber() {
    if (setNumber == null) {
      setNumber = 1;
    } else {
      setNumber = setNumber + 1;
    }

  }

  public static String getId(EQDOCReport report) {
    return report.getId();
  }

  public String getSingleEntity() {
    return this.singleEntity;
  }

  public EqdToIMQ setSingleEntity(String singleEntity) {
    this.singleEntity = singleEntity;
    return this;
  }

  public void convertEQD(TTDocument document, EnquiryDocument eqd, Properties dataMap, Properties criteriaMaps, Namespace namespace) throws IOException, QueryException, EQDException {
    this.document = document;
    this.resources = new EqdResources(document, dataMap, namespace);
    this.namespace = namespace;
    this.resources.setCriteriaMaps(criteriaMaps);
    this.resources.setBaseCounter(0);
    this.addReportNames(eqd);
    this.convertFolders(eqd);
    this.setVersionMap(eqd);
    this.convertReports(eqd);
    createLibrary();
    deduplicate();
    // addLibraryEntities();
    // assignLibraryClauses();
  }

  private void setVersionMap(EnquiryDocument eqd) {
    if (!versionIndependent)
      versionMap.clear();
    else {
      for (EQDOCReport eqReport : eqd.getReport()) {
        String id = eqReport.getId();
        String persistentId = eqReport.getVersionIndependentGUID();
        if (persistentId != null) {
          versionMap.put(id, persistentId);
        }
      }
    }
  }

  private void assignLibraryClauses() throws JsonProcessingException {
    for (TTEntity entity : this.document.getEntities()) {
      if (entity.isType(iri(IM.QUERY))) {
        Query query = entity.get(IM.DEFINITION).asLiteral().objectValue(Query.class);
        if (query.getRule() != null) {
          for (Match rule : query.getRule()) {
            assignLibraryClausesToRule(rule);
          }
        }
        entity.set(IM.DEFINITION, TTLiteral.literal(query));
      }
    }
  }

  private void assignLibraryClausesToRule(Match rule) throws JsonProcessingException {
    for (List<Match> matches : Arrays.asList(rule.getAnd(), rule.getOr(), rule.getNot())) {
      if (matches != null) {
        for (Match match : matches) {
          if (match.getInstanceOf() == null) {
            Match logicalMatch = new LogicOptimizer().getLogicalMatch(match);
            String libraryIri = namespace + "Clause_" + (mapper.writeValueAsString(logicalMatch).hashCode());
            if (criteriaLibrary.containsKey(libraryIri) && criteriaLibraryCount.get(libraryIri) > 1) {
              match.setIri(libraryIri);
            }
          }
        }
      }
    }
  }

  private void addLibraryEntities() throws JsonProcessingException {
    for (Map.Entry<String, Match> entries : criteriaLibrary.entrySet()) {
      String iri = entries.getKey();
      if (criteriaLibraryCount.get(iri) > 1) {
        Match match = entries.getValue();
        TTEntity matchClause = new TTEntity()
          .setIri(iri)
          .addType(iri(IM.MATCH_CLAUSE))
          .setName(match.getDescription());
        matchClause.set(IM.DEFINITION, TTLiteral.literal(match));
        document.addEntity(matchClause);
      }
    }
  }

  private void deduplicate() throws JsonProcessingException {
    for (TTEntity entity : this.document.getEntities()) {
      if (entity.isType(iri(IM.QUERY))) {
        Query query = entity.get(IM.DEFINITION).asLiteral().objectValue(Query.class);
        new LogicOptimizer().deduplicateQuery(query, namespace);
        entity.set(IM.DEFINITION, TTLiteral.literal(query));
      }
    }
  }


  private void createLibrary() throws JsonProcessingException {
    for (TTEntity entity : this.document.getEntities()) {
      if (entity.isType(iri(IM.QUERY))) {
        createLibrary(entity.get(IM.DEFINITION).asLiteral().objectValue(Query.class));
      }
    }
    for (Map.Entry<String, Match> entry : criteriaLibrary.entrySet()) {
      String libraryIri = entry.getKey();
      Match match = entry.getValue();
      if (criteriaLibraryCount.get(libraryIri) > 1) {
        TTEntity entity = new TTEntity()
          .setIri(libraryIri)
          .addType(iri(IM.QUERY))
          .setName(match.getDescription())
          .set(iri(IM.DEFINITION), TTLiteral.literal(match));
        document.addEntity(entity);
      }
    }
  }

  private void createLibrary(Query query) throws JsonProcessingException {
    if (query.getRule() == null) return;
    for (Match rule : query.getRule()) {
      if (rule.getInstanceOf() == null) {
        for (List<Match> matches : Arrays.asList(rule.getAnd(), rule.getOr(), rule.getNot())) {
          if (matches != null) {
            for (Match subMatch : matches) {
              if (subMatch.getInstanceOf() == null && !LogicOptimizer.isLinkedMatch(subMatch)) {
                if (subMatch.getDescription() != null) {
                  Match logicalMatch = new LogicOptimizer().getLogicalMatch(subMatch);
                  addLibraryItem(subMatch, logicalMatch);
                }
              }
            }
          }
        }
      }
    }
  }

  private void addLibraryItem(Match match, Match logicalMatch) throws JsonProcessingException {
    String libraryIri = namespace + "Clause_" + (mapper.writeValueAsString(logicalMatch).hashCode());
    criteriaLibrary.putIfAbsent(libraryIri, match);
    criteriaLibraryCount.putIfAbsent(libraryIri, 1);
    criteriaLibraryCount.put(libraryIri, criteriaLibraryCount.get(libraryIri) + 1);
  }


  private void addReportNames(EnquiryDocument eqd) {
    for (EQDOCReport eqReport : eqd.getReport()) {
      if (eqReport.getId() != null) {
        this.resources.reportNames.put(eqReport.getId(), eqReport.getName());
      }
    }

  }

  private void convertReports(EnquiryDocument eqd) throws IOException, QueryException, EQDException {
    for (EQDOCReport eqReport : eqd.getReport()) {
      if (eqReport.getId() == null) {
        throw new EQDException("No report id");
      }

      if (this.singleEntity == null || eqReport.getId().equals(this.singleEntity)) {
        if (eqReport.getName() == null) {
          throw new EQDException("No report name");
        }

        log.info(eqReport.getName());
        TTEntity qry = this.convertReport(eqReport);
        if (qry != null) {
          this.document.addEntity(qry);
        }
      }
    }

    if (this.document.getEntities() != null) {
      for (TTEntity report : this.document.getEntities()) {
        if (report.get(IM.DEFINITION) != null) {
          Query query = (Query) report.get(IM.DEFINITION).asLiteral().objectValue(Query.class);
          this.checkGms(query);
          report.set(IM.DEFINITION.asIri(), TTLiteral.literal(query));
        }
      }
    }

  }

  private void checkGms(Query query) {
    if (query.getRule() != null) {
      for (Match match : query.getRule()) {
        this.checkGms(match);
      }
    }

    if (query.getDataSet() != null) {
      for (Query subQuery : query.getDataSet()) {
        this.checkGms(subQuery);
      }
    }

  }

  private void checkGms(Match match) {
    if (match.getInstanceOf() != null) {
      for (Node node : match.getInstanceOf()) {
        if (gmsPatients.contains(node.getIri())) {
          node.setIri(Namespace.IM + "Q_RegisteredGMS").setName("Registered with GP for GMS services on the reference date");
        }
      }
    }

  }

  private void convertFolders(EnquiryDocument eqd) throws EQDException {
    List<EQDOCFolder> eqFolders = eqd.getReportFolder();
    if (eqFolders != null) {
      for (EQDOCFolder eqFolder : eqFolders) {
        if (eqFolder.getId() == null) {
          throw new EQDException("No folder id");
        }
        if (this.singleEntity == null || eqFolder.getId().equals(this.singleEntity)) {
          if (eqFolder.getName() == null) {
            throw new EQDException("No folder name");
          }

          TTEntity folder = (new TTEntity()).setIri(this.namespace + eqFolder.getId()).addType(iri(IM.FOLDER)).setName(eqFolder.getName());
          folder.setScheme(iri(namespace));
          if (eqFolder.getParentFolder() != null) {
            folder.addObject(TTIriRef.iri(IM.IS_CONTAINED_IN), TTIriRef.iri(this.namespace + eqFolder.getParentFolder()));
          }

          this.document.addEntity(folder);
        }
      }
    }

  }

  public TTEntity convertReport(EQDOCReport eqReport) throws IOException, QueryException, EQDException {
    this.resources.setActiveReport(eqReport.getId());
    this.resources.setActiveReportName(eqReport.getName());
    String id = getId(eqReport);
    if (versionMap.containsKey(id)) {
      id = versionMap.get(id);
    }
    TTEntity queryEntity = new TTEntity();
    queryEntity.setIri(this.namespace + id);
    queryEntity.setName(eqReport.getName());
    queryEntity.setScheme(iri(this.namespace));
    queryEntity.setDescription(eqReport.getDescription().replace("\n", "<p>"));
    if (eqReport.getFolder() != null) {
      queryEntity.addObject(TTIriRef.iri(IM.IS_CONTAINED_IN), TTIriRef.iri(this.namespace + eqReport.getFolder()).setName(eqReport.getName()));
    }

    Query qry = new Query();
    qry.setIri(queryEntity.getIri());
    qry.setName(queryEntity.getName());
    if (eqReport.getPopulation() != null) {
      queryEntity.addType(iri(IM.QUERY));
      qry = (new EqdPopToIMQ()).convertPopulation(eqReport, qry, this.resources);
    } else if (eqReport.getListReport() != null) {
      queryEntity.addType(iri(IM.QUERY));
      (new EqdListToIMQ()).convertReport(eqReport, this.document, qry, this.resources);
    } else if (eqReport.getAuditReport() != null) {
      queryEntity.addType(iri(IM.QUERY));
      (new EqdAuditToIMQ()).convertReport(eqReport, qry, this.resources);
    } else if (eqReport.getAggregateReport() != null) {
      System.err.println("Aggregate reports not supported");
      return null;
    }

    if (qry == null) {
      return null;
    } else {
      queryEntity.addType(iri(IM.QUERY));
      if (qry.getDataSet() != null && !eqReport.getName().toLowerCase().contains("report")) {
        queryEntity.setName(eqReport.getName() + " -report");
        eqReport.setName(eqReport.getName() + " -report");
      }

      this.flattenRules(qry);
      (new LogicOptimizer()).resolveLogic(qry, DisplayMode.ORIGINAL);
      queryEntity.set(iri(IM.DEFINITION), TTLiteral.literal(qry));
      return queryEntity;
    }
  }

  private void flattenRules(Query qry) {
    if (qry.getRule() != null) {
      for (Match match : qry.getRule()) {
        if (match.getAnd() != null) {
          List<Match> flatMatches = new ArrayList<>();
          for (Match subMatch : match.getAnd()) {
            if (subMatch.getAnd() != null) {
              flatMatches.addAll(subMatch.getAnd());
            } else {
              flatMatches.add(subMatch);
            }
          }

          match.setAnd(flatMatches);
        }
      }
    }

  }
}
