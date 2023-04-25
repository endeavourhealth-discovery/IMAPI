package org.endeavourhealth.imapi.transforms;


import org.endeavourhealth.imapi.model.imq.*;
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
			Return select= new Return();
			subQuery.addReturn(select);
			String eqColumn= String.join("/",eqCol.getColumn());
			String property = resources.getPath(eqTable + "/" + eqColumn);
			select.property(p->p
				.setIri(IM.NAMESPACE+property));
		}
	}

	private String getLastNode(Match match){
		if (match.getPath()!=null){
			return getLastNode(match.getPath());
		}
		else
			return match.getVariable();
	}

	private String getLastNode(Path path){
			if (path.getNode().getPath()==null) {
				return path.getNode().getVariable();
			}
			else
				return getLastNode(path.getNode().getPath());
	}

	private void convertEventColumns(EQDOCListColumnGroup eqColGroup, String eqTable, Query subQuery) throws DataFormatException, IOException {
		Return aReturn = new Return();
		subQuery.addReturn(aReturn);
		Match match = new Match();
		subQuery.addMatch(match);
		resources.convertCriteria(eqColGroup.getCriteria(), match);
		String node= getLastNode(match);
		aReturn.setNodeRef(node);
		ReturnProperty property= new ReturnProperty();
		EQDOCListColumns eqCols = eqColGroup.getColumnar();
		for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
			String eqColumn = String.join("/", eqCol.getColumn());
			String subPath = resources.getPath(eqTable + "/" + eqColumn);
			String field= subPath.substring(subPath.lastIndexOf(" ")+1);
			aReturn.property(p->p
				.setIri(IM.NAMESPACE+field));
		}
	}

}
