package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateGroup;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateReport;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;


import java.util.zip.DataFormatException;

public class EqdAuditToIMQ {
	private EqdResources resources;
	private int aliasCount=0;

	public void convertReport(EQDOCReport eqReport, Query query, EqdResources resources) throws DataFormatException, QueryException {
		this.resources = resources;
		Match match = new Match();
		query.addMatch(match);
		match.setVariable("population");
		match.setBoolMatch(Bool.or);
		for (String popId : eqReport.getAuditReport().getPopulation()) {
			match
				.match(f -> f
					.setSet("urn:uuid:" + popId)
					.setName(resources.reportNames.get(popId)));
		}
		Return aReturn = new Return();
		query.addReturn(aReturn);
		aReturn.setNodeRef("population");
		query.addGroupBy(new Property().setVariable("population"));
		EQDOCAggregateReport agg = eqReport.getAuditReport().getCustomAggregate();
		String eqTable = agg.getLogicalTable();
		String table = resources.getPath(eqTable);
		for (EQDOCAggregateGroup group : agg.getGroup()) {
			for (String eqColum : group.getGroupingColumn()) {
				String pathString = resources.getPath(eqTable + "/" + eqColum);
				String[] pathMap = pathString.split(" ");
				for (int i = 0; i < pathMap.length - 1; i = i + 2) {
					ReturnProperty path = new ReturnProperty();
					aReturn.addProperty(path);
					path.setIri(pathMap[i]);
					if (i < (pathMap.length - 2)) {
						Return node = new Return();
						path.setNode(node);
						aReturn = node;
					}
					else{
						path.setPropertyRef(pathMap[i]);
						query.addGroupBy(new Property().setVariable(pathMap[i]));
					}
				}

			}
		}
	}

}
