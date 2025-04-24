package org.endeavourhealth.imapi.transforms;


import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.DisplayMode;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCFolder;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;
import org.endeavourhealth.imapi.transforms.eqd.EnquiryDocument;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Slf4j
public class EqdToIMQ {
  public static Set<String> gmsPatients = new HashSet<>();
  public static Map<String, TTEntity> definitionToEntity = new HashMap<>();
  ;
  public static Map<String, TTEntity> setIriToEntity = new HashMap<>();
  public static Integer setNumber;
  private String namespace;
  private EqdResources resources;
  private TTDocument document;
  private String singleEntity;

  public static Integer getSetNumber() {
    return setNumber;
  }

  public static void incrementSetNumber() {
    if (setNumber == null)
      setNumber = 1;
    else
      setNumber++;
  }

  public static String getId(EQDOCReport report) {
    return report.getId();
  }

  public String getSingleEntity() {
    return singleEntity;
  }

  public EqdToIMQ setSingleEntity(String singleEntity) {
    this.singleEntity = singleEntity;
    return this;
  }

  public void convertEQD(TTDocument document, EnquiryDocument eqd, Properties dataMap, Properties criteriaMaps) throws IOException, QueryException, EQDException {

    this.document = document;
    this.resources = new EqdResources(document, dataMap);
    this.namespace = document.getGraph().getIri();
    this.resources.setCriteriaMaps(criteriaMaps);
    addReportNames(eqd);
    convertFolders(eqd);
    convertReports(eqd);
  }

  private void addReportNames(EnquiryDocument eqd) {
    for (EQDOCReport eqReport : Objects.requireNonNull(eqd.getReport())) {
      if (eqReport.getId() != null)
        resources.reportNames.put(eqReport.getId(), eqReport.getName());
    }

  }

  private void convertReports(EnquiryDocument eqd) throws IOException, QueryException, EQDException {
    for (EQDOCReport eqReport : Objects.requireNonNull(eqd.getReport())) {
      if (eqReport.getId() == null)
        throw new EQDException("No report id");
      if (singleEntity != null)
        if (!eqReport.getId().equals(singleEntity))
          continue;
      if (eqReport.getName() == null)
        throw new EQDException("No report name");
      log.info(eqReport.getName());
      TTEntity qry = convertReport(eqReport);
      if (qry != null) {
        document.addEntity(qry);
      }
    }
    if (document.getEntities() != null) {
      for (TTEntity report : document.getEntities()) {
        if (report.get(IM.DEFINITION) != null) {
          Query query = report.get(IM.DEFINITION).asLiteral().objectValue(Query.class);
          if (query.getQuery() != null) {
            if (query.getMatch() != null)
              if (query.getInstanceOf() != null) {
                for (Node node : query.getInstanceOf()) {
                  if (gmsPatients.contains(node.getIri())) {
                    node.setIri(IM.NAMESPACE + "Q_RegisteredGMS")
                      .setName("Registered with GP for GMS services on the reference date");
                  }
                }
                report.set(IM.DEFINITION, TTLiteral.literal(query));
              }
          }
        }
      }
    }
  }

  private void convertFolders(EnquiryDocument eqd) throws EQDException {
    List<EQDOCFolder> eqFolders = eqd.getReportFolder();
    if (eqFolders != null) {
      for (EQDOCFolder eqFolder : eqFolders) {
        if (eqFolder.getId() == null)
          throw new EQDException("No folder id");
        if (singleEntity != null)
          if (!eqFolder.getId().equals(singleEntity))
            continue;
        if (eqFolder.getName() == null)
          throw new EQDException("No folder name");
        String iri = namespace + eqFolder.getId();
        TTEntity folder = new TTEntity()
          .setIri(iri)
          .addType(iri(IM.FOLDER))
          .setName(eqFolder.getName());
        if (eqFolder.getParentFolder() != null) {
          folder.addObject(iri(IM.IS_CONTAINED_IN), iri(namespace + eqFolder.getParentFolder()));
        }
        document.addEntity(folder);
      }
    }
  }

  public TTEntity convertReport(EQDOCReport eqReport) throws IOException, QueryException, EQDException {
    resources.setActiveReport(eqReport.getId());
    resources.setActiveReportName(eqReport.getName());
    String id = getId(eqReport);
    TTEntity queryEntity = new TTEntity();
    queryEntity.setIri(namespace + id);
    queryEntity.setName(eqReport.getName());
    queryEntity.setDescription(eqReport.getDescription().replace("\n", "<p>"));
    if (eqReport.getFolder() != null) {
      queryEntity.addObject(iri(IM.IS_CONTAINED_IN), iri(namespace + eqReport.getFolder()).setName(eqReport.getName()));
    }

    Query qry = new Query();
    qry.setIri(queryEntity.getIri());
    qry.setName(queryEntity.getName());
    if (eqReport.getPopulation() != null) {
      queryEntity.addType(iri(IM.COHORT_QUERY));
      qry = new EqdPopToIMQ().convertPopulation(eqReport, qry, resources);
    } else if (eqReport.getListReport() != null) {
      queryEntity.addType(iri(IM.DATASET_QUERY));
      new EqdListToIMQ().convertReport(eqReport, document, qry, resources);
    } else if (eqReport.getAuditReport() != null) {
      queryEntity.addType(iri(IM.DATASET_QUERY));
      new EqdAuditToIMQ().convertReport(eqReport, qry, resources);
    } else if (eqReport.getAggregateReport() != null) {
      System.err.println("Aggregate reports not supported");
      return null;
    }
    if (qry == null)
      return null;
    new LogicOptimizer().resolveLogic(qry, DisplayMode.ORIGINAL);
    queryEntity.set(iri(IM.DEFINITION), TTLiteral.literal(qry));

    return queryEntity;
  }


}
