package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.logic.query.QuerySummariser;
import org.endeavourhealth.imapi.model.iml.ConceptSet;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.iml.ModelDocument;
import org.endeavourhealth.imapi.model.imq.Bool;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryEntity;
import org.endeavourhealth.imapi.model.imq.Where;
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
	public Map<TTIriRef, ConceptSet> valueSets;

	public Map<TTIriRef, ConceptSet> getValueSets() {
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
		flatten(qry);

		QuerySummariser summariser = new QuerySummariser(qry);
		summariser.summarise(false);
		queryEntity.setDefinition(qry);
		return queryEntity;
	}

	private void flatten(Query query) throws DataFormatException {
		if (query.getFrom() != null) {
			if (query.getFrom().getWhere() != null) {
				query.getFrom().setWhere(flattenRoot(query.getFrom().getWhere()));
			}
		}
	}

	private Where flattenRoot(Where oldWhere) throws DataFormatException {
		if (oldWhere.getWhere()==null){
			return oldWhere;
		}
		Where flatWhere = new Where();
		flatWhere.setBool(oldWhere.getBool());
		if (oldWhere.getBool() == Bool.and) {
			if (oldWhere.getWhere().size()==1){
				flatWhere.setWhere(oldWhere.getWhere());
			}
			else {
				for (Where oldAnd : oldWhere.getWhere()) {
					if (oldAnd.getId() == null) {
						if (oldAnd.getBool() == Bool.and) {
							for (Where oldSubWhere : oldAnd.getWhere()) {
								flatWhere.addWhere(oldSubWhere);
							}
						}
						else
							flatWhere.addWhere(oldAnd);
					}
					else
						flatWhere.addWhere(oldAnd);
				}
			}
		}
		else throw new DataFormatException("unsupported where pattern");
		return flatWhere;

	}
}