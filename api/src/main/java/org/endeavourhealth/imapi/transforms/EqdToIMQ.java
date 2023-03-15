package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.logic.query.QuerySummariser;
import org.endeavourhealth.imapi.model.iml.ConceptSet;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.iml.ModelDocument;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCFolder;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;
import org.endeavourhealth.imapi.transforms.eqd.EnquiryDocument;
import org.endeavourhealth.imapi.vocabulary.IM;


import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;

public class EqdToIMQ {
	private final EqdResources resources = new EqdResources();
	private static final Set<String> roles = new HashSet<>();
	public Map<String, ConceptSet> valueSets;

	public Map<String, ConceptSet> getValueSets() {
		return valueSets;
	}


	public ModelDocument convertEQD(EnquiryDocument eqd, Properties dataMap,

																	Properties criteriaLabels) throws DataFormatException, IOException {

		resources.setDataMap(dataMap);
		resources.setDocument(new ModelDocument());
		resources.setLabels(criteriaLabels);
		addReportNames(eqd);
		convertFolders(eqd);
		convertReports(eqd);
		this.valueSets = resources.getValueSets();
		return resources.getDocument();
	}

	private void addReportNames(EnquiryDocument eqd) {
		for (EQDOCReport eqReport : Objects.requireNonNull(eqd.getReport())) {
			if (eqReport.getId() != null)
				resources.reportNames.put(eqReport.getId(), eqReport.getName());
		}

	}

	private void convertReports(EnquiryDocument eqd) throws DataFormatException, IOException {
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
					.addType(IM.FOLDER)
					.setName(eqFolder.getName());
				resources.getDocument().addFolder(folder);
			}
		}
	}


	public QueryEntity convertReport(EQDOCReport eqReport) throws DataFormatException, IOException {

		resources.setActiveReport(eqReport.getId());
		resources.setActiveReportName(eqReport.getName());
		QueryEntity queryEntity = new QueryEntity();
		queryEntity.setIri("urn:uuid:" + eqReport.getId());
		queryEntity.setName(eqReport.getName());
		queryEntity.setDescription(eqReport.getDescription().replace("\n", "<p>"));
		if (eqReport.getFolder() != null)
			queryEntity.addIsContainedIn(TTIriRef.iri("urn:uuid:" + eqReport.getFolder()));
		queryEntity.addType(IM.QUERY);
		Query qry = new Query();

		if (eqReport.getPopulation() != null) {
			new EqdPopToIMQ().convertPopulation(eqReport, qry, resources);
		}
		else if (eqReport.getListReport() != null) {
			new EqdListToIMQ().convertReport(eqReport, qry, resources);
		}
		else
			new EqdAuditToIMQ().convertReport(eqReport, qry, resources);
		flattenQuery(qry);

		QuerySummariser summariser = new QuerySummariser(qry);
		summariser.summarise(false);
		queryEntity.setDefinition(qry);
		return queryEntity;
	}

	private void flattenQuery(Query qry) {
		From from= qry.getFrom();
		List<Where> oldWhereList= from.getWhere();
		if (oldWhereList!=null) {
			List<Where> newWhereList = new ArrayList<>();
			for (Where where : oldWhereList) {
				if (where.getIri() == null) {
					if (where.getBool() == Bool.and) {
						for (Where and : where.getWhere())
							newWhereList.add(and);
					}
					else
						newWhereList.add(where);
				}
				else
					newWhereList.add(where);
			}
			from.setWhere(newWhereList);
		}
	}

}
