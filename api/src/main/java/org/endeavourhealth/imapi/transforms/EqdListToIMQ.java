package org.endeavourhealth.imapi.transforms;


import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.Select;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class EqdListToIMQ {
	private EqdResources resources;

	public void convertReport(EQDOCReport eqReport, Query query, EqdResources resources) throws DataFormatException, IOException {
		this.resources= resources;
		String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
		query.match(f->f
			.setSet("urn:uuid:" + id)
			.setName(resources.reportNames.get(id)));
		for (EQDOCListReport.ColumnGroups eqColGroups : eqReport.getListReport().getColumnGroups()) {
			EQDOCListColumnGroup eqColGroup = eqColGroups.getColumnGroup();
			Query subQuery = new Query();
			query.addQuery(subQuery);
			convertListGroup(eqColGroup, subQuery);
		}
	}


	private void convertListGroup(EQDOCListColumnGroup eqColGroup, Query subQuery) throws DataFormatException, IOException {
		String eqTable = eqColGroup.getLogicalTableName();
		subQuery.setName(eqColGroup.getDisplayName());
		if (eqColGroup.getCriteria() == null) {
			convertPatientColumns(eqColGroup, eqTable, subQuery);
		} else {
			convertEventColumns(eqColGroup, eqTable, subQuery);
		}
	}

	private void convertPatientColumns(EQDOCListColumnGroup eqColGroup, String eqTable, Query subQuery) throws DataFormatException {
		EQDOCListColumns eqCols = eqColGroup.getColumnar();
		for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
			Select select= new Select();
			subQuery.addSelect(select);
			String eqColumn= String.join("/",eqCol.getColumn());
			String property = resources.getPath(eqTable + "/" + eqColumn);
			select.setIri(IM.NAMESPACE+property);
		}

	}

	private void convertEventColumns(EQDOCListColumnGroup eqColGroup, String eqTable, Query subQuery) throws DataFormatException, IOException {
		Select select = new Select();
		subQuery.addSelect(select);
		Match match = new Match();
		subQuery.addMatch(match);
		resources.convertCriteria(eqColGroup.getCriteria(), match);
		select.setIri(IM.NAMESPACE+resources.getPath(eqTable));
		EQDOCListColumns eqCols = eqColGroup.getColumnar();
		for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
			String eqColumn = String.join("/", eqCol.getColumn());
			String subPath = resources.getPath(eqTable + "/" + eqColumn);
			String[] subPaths= subPath.split(" ");
			if (subPath.contains(" ")) {
				for (int i = 0; i < subPaths.length - 1; i++) {
					select.setIri(IM.NAMESPACE+subPaths[i]);
					Select subSelect= new Select();
					select.addSelect(subSelect);
					select= subSelect;
				}
			}
			select.setIri(IM.NAMESPACE+subPaths[subPaths.length-1]);
		}
	}

}
