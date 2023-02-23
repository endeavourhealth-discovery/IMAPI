package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.Select;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateGroup;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateReport;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;
import org.endeavourhealth.imapi.vocabulary.IM;


import java.util.zip.DataFormatException;

public class EqdAuditToIMQ {
	private EqdResources resources;

	public void convertReport(EQDOCReport eqReport, Query query, EqdResources resources) throws DataFormatException {
		this.resources= resources;

		if (eqReport.getParent().getSearchIdentifier()!=null) {
			String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
			query
				.from(f->f
				.setSet("urn:uuid:" + id)
				.setName(resources.reportNames.get(id)));
		}
		else
			query.from(f->f
				.setType(IM.NAMESPACE+"Patient")
				.setName("Patient"));
		for (String popId : eqReport.getAuditReport().getPopulation()) {
			Query subQuery = new Query();
			query.addQuery(subQuery);
			subQuery
				.from(f->f
				.setIri("urn:uuid:" + popId)
				.setName(resources.reportNames.get(popId)));
		}
		EQDOCAggregateReport agg = eqReport.getAuditReport().getCustomAggregate();
		Select select = new Select();
		query.addSelect(select);

		String eqTable = agg.getLogicalTable();
		for (
			EQDOCAggregateGroup group : agg.getGroup()) {
			for (String eqColum : group.getGroupingColumn()) {
				String predicate = resources.getPath(eqTable + "/" + eqColum);
				select.addGroupBy(new TTAlias().setId(predicate));
			}
		}
	}

}
