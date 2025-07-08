package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
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

public class EqdToIMQ {
  private static final Logger log = LoggerFactory.getLogger(EqdToIMQ.class);
  public static Set<String> gmsPatients = new HashSet<>();
  public static Map<String, TTEntity> definitionToEntity = new HashMap<>();
  public static Map<String, TTEntity> setIriToEntity = new HashMap<>();
  public static Integer setNumber;
  private String namespace;
  private EqdResources resources;
  private TTDocument document;
  private String singleEntity;

  public EqdToIMQ() {
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

  public void convertEQD(TTDocument document, EnquiryDocument eqd, Properties dataMap, Properties criteriaMaps, Graph graph) throws IOException, QueryException, EQDException {
    this.document = document;
    this.resources = new EqdResources(document, dataMap);
    this.resources.setCriteriaMaps(criteriaMaps);
    this.addReportNames(eqd);
    this.convertFolders(eqd);
    this.convertReports(eqd, graph);
  }

  private void addReportNames(EnquiryDocument eqd) {
    for (EQDOCReport eqReport : eqd.getReport()) {
      if (eqReport.getId() != null) {
        this.resources.reportNames.put(eqReport.getId(), eqReport.getName());
      }
    }

  }

  private void convertReports(EnquiryDocument eqd, Graph graph) throws IOException, QueryException, EQDException {
    for (EQDOCReport eqReport : eqd.getReport()) {
      if (eqReport.getId() == null) {
        throw new EQDException("No report id");
      }

      if (this.singleEntity == null || eqReport.getId().equals(this.singleEntity)) {
        if (eqReport.getName() == null) {
          throw new EQDException("No report name");
        }

        log.info(eqReport.getName());
        TTEntity qry = this.convertReport(eqReport, graph);
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

          String var10000 = this.namespace;
          String iri = var10000 + eqFolder.getId();
          TTEntity folder = (new TTEntity()).setIri(iri).addType(TTIriRef.iri(IM.FOLDER)).setName(eqFolder.getName());
          if (eqFolder.getParentFolder() != null) {
            TTIriRef var10001 = TTIriRef.iri(IM.IS_CONTAINED_IN);
            String var10002 = this.namespace;
            folder.addObject(var10001, TTIriRef.iri(var10002 + eqFolder.getParentFolder()));
          }

          this.document.addEntity(folder);
        }
      }
    }

  }

  public TTEntity convertReport(EQDOCReport eqReport, Graph graph) throws IOException, QueryException, EQDException {
    this.resources.setActiveReport(eqReport.getId());
    this.resources.setActiveReportName(eqReport.getName());
    String id = getId(eqReport);
    TTEntity queryEntity = new TTEntity();
    queryEntity.setIri(this.namespace + id);
    queryEntity.setName(eqReport.getName());
    queryEntity.setDescription(eqReport.getDescription().replace("\n", "<p>"));
    if (eqReport.getFolder() != null) {
      TTIriRef var10001 = TTIriRef.iri(IM.IS_CONTAINED_IN);
      String var10002 = this.namespace;
      queryEntity.addObject(var10001, TTIriRef.iri(var10002 + eqReport.getFolder()).setName(eqReport.getName()));
    }

    Query qry = new Query();
    qry.setIri(queryEntity.getIri());
    qry.setName(queryEntity.getName());
    if (eqReport.getPopulation() != null) {
      queryEntity.addType(TTIriRef.iri(IM.COHORT_QUERY));
      qry = (new EqdPopToIMQ()).convertPopulation(eqReport, qry, this.resources, graph);
    } else if (eqReport.getListReport() != null) {
      queryEntity.addType(TTIriRef.iri(IM.DATASET_QUERY));
      (new EqdListToIMQ()).convertReport(eqReport, this.document, qry, this.resources, graph);
    } else if (eqReport.getAuditReport() != null) {
      queryEntity.addType(TTIriRef.iri(IM.QUERY));
      (new EqdAuditToIMQ()).convertReport(eqReport, qry, this.resources);
    } else if (eqReport.getAggregateReport() != null) {
      System.err.println("Aggregate reports not supported");
      return null;
    }

    if (qry == null) {
      return null;
    } else {
      queryEntity.addType(TTIriRef.iri(IM.QUERY));
      if (qry.getDataSet() != null && !eqReport.getName().toLowerCase().contains("report")) {
        queryEntity.setName(eqReport.getName() + " -report");
        eqReport.setName(eqReport.getName() + " -report");
      }

      this.flattenRules(qry);
      (new LogicOptimizer()).resolveLogic(qry, DisplayMode.ORIGINAL);
      queryEntity.set(TTIriRef.iri(IM.DEFINITION), TTLiteral.literal(qry));
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
