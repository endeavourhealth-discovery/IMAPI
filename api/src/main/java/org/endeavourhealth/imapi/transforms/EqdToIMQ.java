package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.logic.service.QueryDescriptor;
import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.iml.ModelDocument;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCFolder;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;
import org.endeavourhealth.imapi.transforms.eqd.EnquiryDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdToIMQ {
  private static final Logger LOG = LoggerFactory.getLogger(EqdToIMQ.class);
  public static final String URN_UUID = "urn:uuid:";
  private final EqdResources resources = new EqdResources();


  public ModelDocument convertEQD(EnquiryDocument eqd, Properties dataMap,

                                  Properties criteriaLabels) throws IOException, QueryException, EQDException {

      resources.setDataMap(dataMap);
      resources.setDocument(new ModelDocument());
      resources.setLabels(criteriaLabels);
      addReportNames(eqd);
      convertFolders(eqd);
      convertReports(eqd);
      return resources.getDocument();
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
      if (eqReport.getName() == null)
        throw new EQDException("No report name");
      LOG.info(eqReport.getName());
      QueryEntity qry = convertReport(eqReport);
      resources.getDocument().addQuery(qry);
    }
  }

  private void convertFolders(EnquiryDocument eqd) throws EQDException {
    List<EQDOCFolder> eqFolders = eqd.getReportFolder();
    if (eqFolders != null) {
      for (EQDOCFolder eqFolder : eqFolders) {
        if (eqFolder.getId() == null)
          throw new EQDException("No folder id");
        if (eqFolder.getName() == null)
          throw new EQDException("No folder name");
        String iri = URN_UUID + eqFolder.getId();
        Entity folder = new Entity()
          .setIri(iri)
          .addType(iri(IM.FOLDER))
          .setName(eqFolder.getName());
        resources.getDocument().addFolder(folder);
      }
    }
  }


  public QueryEntity convertReport(EQDOCReport eqReport) throws IOException, QueryException, EQDException {

    resources.setActiveReport(eqReport.getId());
    resources.setActiveReportName(eqReport.getName());
    QueryEntity queryEntity = new QueryEntity();
    queryEntity.setIri(URN_UUID + eqReport.getId());
    queryEntity.setName(eqReport.getName());
    queryEntity.setDescription(eqReport.getDescription().replace("\n", "<p>"));
    if (eqReport.getFolder() != null)
      queryEntity.addIsContainedIn(new TTEntity((URN_UUID + eqReport.getFolder())).setName(eqReport.getName()));

    Query qry = new Query();
    qry.setName(queryEntity.getName());
    if (eqReport.getPopulation() != null) {
      queryEntity.addType(iri(IM.COHORT_QUERY));
      new EqdPopToIMQ().convertPopulation(eqReport, qry, resources);
    } else if (eqReport.getListReport() != null) {
      queryEntity.addType(iri(IM.DATASET_QUERY));
      new EqdListToIMQ().convertReport(eqReport, qry, resources);
    } else if (eqReport.getAuditReport()!=null){
        queryEntity.addType(iri(IM.DATASET_QUERY));
        new EqdAuditToIMQ().convertReport(eqReport, qry, resources);
    }
    else if (eqReport.getAggregateReport()!=null){
      System.err.println("Aggregate reports not supported");
    }
    if (qry.getMatch()!=null) {
      flattenQuery(qry);
    }
    queryEntity.setDefinition(qry);
    return queryEntity;
  }


  private void flattenQuery(Query qry) {
    if (qry.getBoolMatch() == Bool.or) {
      return;
    }
    if (qry.getWhere() != null) {
      return;
    }
    List<Match> flatMatches = new ArrayList<>();
    flattenAnds(qry.getMatch(), flatMatches);
    qry.setMatch(flatMatches);
  }

  private void flattenAnds(List<Match> topMatches, List<Match> flatMatches) {
    for (Match topMatch : topMatches) {
      //Top level match, no nested match
      if (topMatch.getMatch() == null) {
        flatMatches.add(topMatch);
      } else if (topMatch.getBoolMatch() != Bool.or) {
        flattenAnds(topMatch.getMatch(),flatMatches);
      } else {
        flatMatches.add(topMatch);
      }
    }

  }


}
