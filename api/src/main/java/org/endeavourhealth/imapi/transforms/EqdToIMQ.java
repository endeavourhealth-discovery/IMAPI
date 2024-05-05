package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.iml.ConceptSet;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.iml.ModelDocument;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCFolder;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;
import org.endeavourhealth.imapi.transforms.eqd.EnquiryDocument;
import org.endeavourhealth.imapi.vocabulary.IM;


import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdToIMQ {
	private final EqdResources resources = new EqdResources();




	public ModelDocument convertEQD(EnquiryDocument eqd, Properties dataMap,

																	Properties criteriaLabels) throws DataFormatException, IOException, QueryException {

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

	private void convertReports(EnquiryDocument eqd) throws DataFormatException, IOException, QueryException {
		for (EQDOCReport eqReport : Objects.requireNonNull(eqd.getReport())) {
			if (eqReport.getId() == null)
				throw new DataFormatException("No report id");
			if (eqReport.getName() == null)
				throw new DataFormatException("No report name");
			System.out.println(eqReport.getName());
			QueryEntity qry = convertReport(eqReport);
			resources.getDocument().addQuery(qry);
		}
	}

	private void convertFolders(EnquiryDocument eqd) throws DataFormatException {
		List<EQDOCFolder> eqFolders = eqd.getReportFolder();
		if (eqFolders != null) {
			for (EQDOCFolder eqFolder : eqFolders) {
				if (eqFolder.getId() == null)
					throw new DataFormatException("No folder id");
				if (eqFolder.getName() == null)
					throw new DataFormatException("No folder name");
				String iri = "urn:uuid:" + eqFolder.getId();
				Entity folder = new Entity()
					.setIri(iri)
					.addType(iri(IM.FOLDER))
					.setName(eqFolder.getName());
				resources.getDocument().addFolder(folder);
			}
		}
	}


	public QueryEntity convertReport(EQDOCReport eqReport) throws DataFormatException, IOException, QueryException {

		resources.setActiveReport(eqReport.getId());
		resources.setActiveReportName(eqReport.getName());
		QueryEntity queryEntity = new QueryEntity();
		queryEntity.setIri("urn:uuid:" + eqReport.getId());
		queryEntity.setName(eqReport.getName());
		queryEntity.setDescription(eqReport.getDescription().replace("\n", "<p>"));
		if (eqReport.getFolder() != null)
			queryEntity.addIsContainedIn(new TTEntity(("urn:uuid:" + eqReport.getFolder())).setName(eqReport.getName()));

		Query qry = new Query();

		if (eqReport.getPopulation() != null) {
			queryEntity.addType(iri(IM.COHORT_QUERY));
			new EqdPopToIMQ().convertPopulation(eqReport, qry, resources);
		}
		else if (eqReport.getListReport() != null) {
			queryEntity.addType(iri(IM.DATASET_QUERY));
			new EqdListToIMQ().convertReport(eqReport, qry, resources);
		}
		else {
			queryEntity.addType(iri(IM.DATASET_QUERY));
			new EqdAuditToIMQ().convertReport(eqReport, qry, resources);
		}
		flattenQuery(qry);
		queryEntity.setDefinition(qry);
		return queryEntity;
	}



	private void flattenQuery(Query qry) throws QueryException {
		List<Match> flatMatches= new ArrayList<>();
		flattenAnds(qry.getMatch(),flatMatches);
		qry.setMatch(flatMatches);
	}

	private void flattenAnds(List<Match> topMatches, List<Match> flatMatches) throws QueryException {
		for (Match topMatch:topMatches) {
			if (topMatch.getMatch()==null){
				flatMatches.add(topMatch);
			}
			else if (topMatch.getBoolMatch()==Bool.or) {
				flatMatches.add(topMatch);
				for (Match orMatch : topMatch.getMatch()) {
					if (orMatch.getMatch() != null) {
						List<Match> newMatchList = new ArrayList<>();
						flattenAnds(orMatch.getMatch(), newMatchList);
						orMatch.setMatch(newMatchList);
					}
				}
			}
			else {
					flattenAnds(topMatch.getMatch(),flatMatches);
				}
			}

	}




}
