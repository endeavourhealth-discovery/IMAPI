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
					String[] path = pathString.split(" ");
					String nodeVariable = "";
					for (int i = 0; i < path.length - 1; i++) {
						String element = path[i];
						aliasCount++;
						groupMatch.path(p -> p.setId(element).setVariable("alias" + aliasCount));
						groupAlias= "alias"+aliasCount;
					}
				}
			}
			for (Match group : subQuery.getMatch()) {
				if (group.getPath()!=null) {
					subQuery.select(s -> s.setId(group.getPath().get(group.getPath().size() - 1).getVariable()));
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

}
