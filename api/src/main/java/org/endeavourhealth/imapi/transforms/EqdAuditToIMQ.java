package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateGroup;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateReport;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;
import org.endeavourhealth.imapi.vocabulary.IM;


import java.util.zip.DataFormatException;

public class EqdAuditToIMQ {
	private EqdResources resources;
	private int aliasCount=0;

	public void convertReport(EQDOCReport eqReport, Query query, EqdResources resources) throws DataFormatException {
		this.resources = resources;

		if (eqReport.getParent().getSearchIdentifier() != null) {
			String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
			query
				.match(f -> f
					.setSet("urn:uuid:" + id)
					.setName(resources.reportNames.get(id)));
		}
		else
			query.match(f -> f
				.setType(IM.NAMESPACE + "Patient")
				.setName("Patient"));
		String groupAlias=null;
		for (String popId : eqReport.getAuditReport().getPopulation()) {
			Query subQuery = new Query();
			query.addQuery(subQuery);
			subQuery
				.match(f -> f
					.setSet("urn:uuid:" + popId)
					.setName(resources.reportNames.get(popId)));
			EQDOCAggregateReport agg = eqReport.getAuditReport().getCustomAggregate();
			String eqTable = agg.getLogicalTable();
			String table = resources.getPath(eqTable);
			for (EQDOCAggregateGroup group : agg.getGroup()) {
				Match groupMatch = new Match();
				subQuery.addMatch(groupMatch);
				groupMatch.setType(table);
				groupMatch.setVariable(table);
				for (String eqColum : group.getGroupingColumn()) {
					String pathString = resources.getPath(eqTable + "/" + eqColum);
					String[] pathMap = pathString.split(" ");
					Relationship path= new Relationship();
					groupMatch.setPath(path);
					for (int i = 0; i < pathMap.length - 1; i=i+2) {
						String element = pathMap[i];
						aliasCount++;
						path.setIri(element).setVariable("alias" + aliasCount);
						if (i<pathMap.length-2){
							Node node= new Node();
							path.setNode(node);
							path= new Relationship();
							node.setPath(path);
						}
						groupAlias= "alias"+aliasCount;
					}
				}
			}
			for (Match group : subQuery.getMatch()) {
				if (group.getPath()!=null) {
					addSelects(group.getPath(),subQuery);

				}
			}
			subQuery.select(s -> s
				.setId("id")
				.function(f -> f.setFunction(Function.count))
				.setVariable("counts"));
		}
		query.addSelect(new Select().setVariable(groupAlias));
		query.addSelect(new Select()
			.setVariable("counts"));
	}

	private void addSelects(Relationship path,Query subQuery) {
		subQuery.select(s -> s.setId(path.getVariable()));
		if (path.getNode()!=null)
			if (path.getNode().getPath()!=null)
				addSelects(path.getNode().getPath(),subQuery);
	}

}
