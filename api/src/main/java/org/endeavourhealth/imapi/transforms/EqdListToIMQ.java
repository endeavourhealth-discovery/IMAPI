package org.endeavourhealth.imapi.transforms;


import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class EqdListToIMQ {
	private EqdResources resources;

	public void convertReport(EQDOCReport eqReport, Query query, EqdResources resources) throws DataFormatException, IOException, QueryException {
		this.resources= resources;
		String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
		query.match(f->f
			.addIs(new Node().setIri("urn:uuid:" + id))
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
		for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
			Return select= new Return();
			subQuery.addReturn(select);
			String eqColumn= String.join("/",eqCol.getColumn());
			String[] propertyPath = resources.getPath(eqTable + "/" + eqColumn).split(" ");
			if (propertyPath.length==1){
				select.property(p->p.setIri(IM.NAMESPACE+propertyPath[0]));
			}
			else {
				for (int i=0; i<propertyPath.length; i++){
					ReturnProperty property= new ReturnProperty();
					property.setIri(IM.NAMESPACE+propertyPath[i]);
					select.addProperty(property);
					select= new Return();
					property.setReturn(select);
				}
			}
		}
	}

	private String getLastNode(Match match){
		if (match.getProperty()!=null){
			if (match.getProperty().get(0).getMatch()!=null)
				return getLastNode(match.getProperty().get(0).getMatch());
			else
				return match.getVariable();
		}
		else
			return match.getVariable();
	}


	private void convertEventColumns(EQDOCListColumnGroup eqColGroup, String eqTable, Query subQuery) throws DataFormatException, IOException, QueryException {
		Return aReturn = new Return();
		subQuery.addReturn(aReturn);
		Match match = new Match();
		subQuery.addMatch(match);
		resources.convertCriteria(eqColGroup.getCriteria(), match);
		String node= getLastNode(match);
		aReturn.setNodeRef(node);
		EQDOCListColumns eqCols = eqColGroup.getColumnar();
		for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
			String eqColumn = String.join("/", eqCol.getColumn());
			String subPath = resources.getPath(eqTable + "/" + eqColumn);
			if (subPath.contains(" ")){
				String elements[]= subPath.split(" ");
				for (int i=0; i<elements.length; i=i+2){
					ReturnProperty property= new ReturnProperty();
					aReturn.addProperty(property);
					property.setIri(IM.NAMESPACE+ elements[i]);
					if (i<(elements.length-2)) {
						property.setReturn(new Return());
						aReturn = property.getReturn();
					}
				}
			}
			else {
				aReturn.property(p->p.setIri(IM.NAMESPACE+subPath));
			}
		}
	}

}
