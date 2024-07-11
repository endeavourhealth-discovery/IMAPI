package org.endeavourhealth.imapi.transforms;


import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdListToIMQ {
	private EqdResources resources;

	public void convertReport(EQDOCReport eqReport, Query query, EqdResources resources) throws DataFormatException, IOException, QueryException {
		this.resources= resources;
		String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
		query.match(f->f
			.addInstanceOf(new Node().setIri("urn:uuid:" + id).setMemberOf(true))
			.setName(resources.reportNames.get(id)));
		for (EQDOCListReport.ColumnGroups eqColGroups : eqReport.getListReport().getColumnGroups()) {
			EQDOCListColumnGroup eqColGroup = eqColGroups.getColumnGroup();
			Query subQuery = new Query();
			query.addQuery(subQuery);
			convertListGroup(eqColGroup, subQuery);
		}
	}


	private void convertListGroup(EQDOCListColumnGroup eqColGroup, Query subQuery) throws DataFormatException, IOException, QueryException {
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
		Return select= new Return();
		subQuery.addReturn(select);
		for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
			String eqColumn= String.join("/",eqCol.getColumn());
			String propertyPath = resources.getPath(eqTable + "/" + eqColumn);
			convertColumn(select,propertyPath);
		}
	}



	private void convertEventColumns(EQDOCListColumnGroup eqColGroup, String eqTable, Query subQuery) throws DataFormatException, IOException, QueryException {
		Return aReturn = new Return();
		subQuery.addReturn(aReturn);

		String tablePath = resources.getPath(eqTable);
		if (tablePath.contains(" ")) {
			String[] paths = tablePath.split(" ");
			for (int i = 0; i < paths.length; i = i + 2) {
				aReturn.addPath(new IriLD().setIri(paths[i].replace("^", "")));
			}
		}
		Match match= resources.convertCriteria(eqColGroup.getCriteria());
		subQuery.addMatch(match);
		EQDOCListColumns eqCols = eqColGroup.getColumnar();
		for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
			String eqColumn = String.join("/", eqCol.getColumn());
			String subPath = resources.getPath(eqTable + "/" + eqColumn);
			convertColumn(aReturn,subPath);
		}
	}

	private void convertColumn(Return aReturn, String subPath) {
	ReturnProperty property= new ReturnProperty();
			aReturn.addProperty(property);
			if (subPath.contains(" ")){
				String elements[]= subPath.split(" ");
				for (int i=0; i<elements.length; i=i+2){
					property.setIri(IM.NAMESPACE+ elements[i]);
					if (i<(elements.length-2)) {
						property.setReturn(new Return());
						ReturnProperty subProperty = new ReturnProperty();
						property.getReturn().addProperty(subProperty);
						property= subProperty;
					}
				}
			}
			else {
				aReturn.property(p->p.setIri(IM.NAMESPACE+subPath));
			}
		}

}
