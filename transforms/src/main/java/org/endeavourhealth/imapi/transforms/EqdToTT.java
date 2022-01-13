package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.CaseUtils;
import org.endeavourhealth.imapi.cdm.ProvActivity;
import org.endeavourhealth.imapi.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.query.Query;
import org.endeavourhealth.imapi.query.QueryDocument;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCFolder;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;
import org.endeavourhealth.imapi.transforms.eqd.EnquiryDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.PROV;

import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.DataFormatException;

public class EqdToTT {
	private TTIriRef owner;
	private Properties dataMap;
	private Properties criteriaLabels;

	private TTDocument document;

	private final Set<String> users= new HashSet<>();



	public void convertDoc(TTDocument document,
												 TTIriRef mainFolder, EnquiryDocument eqd,
												 TTIriRef graph,
												 TTIriRef owner,
												 Properties dataMap,
												 Properties criteriaLabels,
												 Map<String,String> reportNames) throws DataFormatException, JsonProcessingException {
		this.owner = owner;
		this.dataMap = dataMap;
		this.document= document;
		this.criteriaLabels= criteriaLabels;
		convertFolders(mainFolder,eqd);
		convertReports(eqd,reportNames);
	}



	private void convertReports(EnquiryDocument eqd,Map<String,String> reportNames) throws DataFormatException, JsonProcessingException {
		for (EQDOCReport eqReport : Objects.requireNonNull(eqd.getReport())) {
			if (eqReport.getId() == null)
				throw new DataFormatException("No report id");
			if (eqReport.getName() == null)
				throw new DataFormatException("No report name");

			if (eqReport.getPopulation() != null) {
				Query qry= new Query();
				EqdToQuery eqdToQuery = new EqdToQuery();
				eqdToQuery.convertReport(eqReport,qry,document,dataMap,criteriaLabels,reportNames);
				document.addEntity(qry.asEntity());
				setProvenance(qry.getIri(),"CEG");

			}

		}
	}

	private void convertFolders(TTIriRef mainFolder,EnquiryDocument eqd) throws DataFormatException {
		List<EQDOCFolder> eqFolders= eqd.getReportFolder();
		if (eqFolders!=null){
			for (EQDOCFolder eqFolder:eqFolders) {
				if (eqFolder.getId()==null)
					throw new DataFormatException("No folder id");
				if (eqFolder.getName()==null)
					throw new DataFormatException("No folder name");
				String iri= "urn:uuid:"+ eqFolder.getId();
				TTEntity folder = new TTEntity()
					.setIri(iri)
						.addType(IM.FOLDER)
							.setName(eqFolder.getName())
					.set(IM.IS_CONTAINED_IN,mainFolder);
				document.addEntity(folder);
				if (eqFolder.getAuthor()!=null)
					if (eqFolder.getAuthor().getAuthorName()!=null)
						setProvenance(iri,eqFolder.getAuthor().getAuthorName());
			}
		}
	}

	private void setProvenance(String iri,String authorName) {
		String uir= getPerson(authorName);
		ProvAgent agent= new ProvAgent()
			.setPersonInRole(TTIriRef.iri(uir))
			.setParticipationType(IM.AUTHOR_ROLE);
		agent.setName(authorName);
		agent.setIri(uir.replace("uir.","agent."));
		document.addEntity(agent);
		ProvActivity activity= new ProvActivity()
			.setIri("http://prov.endhealth.info/im#Q_RegisteredGMS")
			.setActivityType(IM.CREATION)
			.setEffectiveDate(LocalDateTime.now().toString())
			.addAgent(TTIriRef.iri(agent.getIri()))
			.setTargetEntity(TTIriRef.iri(iri));
		document.addEntity(activity);

	}

	private String getPerson(String name) {
		return owner.getIri().replace("org.","uir.")+"/personrole#"+
			CaseUtils.toCamelCase(name
					.replace(" ",""),true)
				.replace("(","_")
				.replace(")","_");
	}


}
