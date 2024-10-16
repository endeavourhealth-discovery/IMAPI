package org.endeavourhealth.imapi.transforms;


import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;

public class EqdListToIMQ {
  private EqdResources resources;

  public void convertReport(EQDOCReport eqReport, Query query, EqdResources resources) throws IOException, QueryException, EQDException {
    this.resources = resources;
    String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
    query.match(f -> f
      .addInstanceOf(new Node().setIri("urn:uuid:" + id).setMemberOf(true))
      .setName(resources.reportNames.get(id)));
    for (EQDOCListReport.ColumnGroups eqColGroups : eqReport.getListReport().getColumnGroups()) {
      EQDOCListColumnGroup eqColGroup = eqColGroups.getColumnGroup();
      Query subQuery = new Query();
      query.addQuery(subQuery);
      convertListGroup(eqColGroup, subQuery);
    }
  }


  private void convertListGroup(EQDOCListColumnGroup eqColGroup, Query subQuery) throws IOException, QueryException, EQDException {
    String eqTable = eqColGroup.getLogicalTableName();
    subQuery.setName(eqColGroup.getDisplayName());
    if (eqColGroup.getCriteria() == null) {
      convertPatientColumns(eqColGroup, eqTable, subQuery);
    } else {
      convertEventColumns(eqColGroup, eqTable, subQuery);
    }
  }

  private void convertPatientColumns(EQDOCListColumnGroup eqColGroup, String eqTable, Query subQuery) throws EQDException {
    EQDOCListColumns eqCols = eqColGroup.getColumnar();
    Return select = new Return();
    subQuery.addReturn(select);
    for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
      String eqColumn = String.join("/", eqCol.getColumn());
      String eqULR = eqTable + "/" + eqColumn;
      String propertyPath = resources.getPath(eqULR);
      convertColumn(select, propertyPath,eqCol.getDisplayName());
    }
  }


  private void convertEventColumns(EQDOCListColumnGroup eqColGroup, String eqTable, Query subQuery) throws IOException, QueryException, EQDException {
    Return aReturn = new Return();
    subQuery.addReturn(aReturn);

    String tablePath = resources.getPath(eqTable);
    if (tablePath.contains(" ")) {
      String[] paths = tablePath.split(" ");
      for (int i = 0; i < paths.length; i = i + 2) {
        aReturn.addPath(new IriLD().setIri(paths[i].replace("^", "")));
      }
    }
    Match match = resources.convertCriteria(eqColGroup.getCriteria());
    subQuery.addMatch(match);
    if (eqColGroup.getColumnar()==null){
      if (eqColGroup.getSummary()!=null){
        if (eqColGroup.getSummary()==VocListGroupSummary.COUNT) {
          aReturn.function(f -> f
            .setFunction(Function.count));
        }
        else
          throw new QueryException("unmapped summary function : "+ eqColGroup.getSummary().value());
      }
    }
    else {
      EQDOCListColumns eqCols = eqColGroup.getColumnar();
      for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
        String eqColumn = String.join("/", eqCol.getColumn());
        String eqURL = eqTable + "/" + eqColumn;
        String subPath = resources.getPath(eqURL);
        convertColumn(aReturn, subPath, eqCol.getDisplayName());
      }
    }
  }

  private void convertColumn(Return aReturn, String subPath,String displayName) {
    ReturnProperty property = new ReturnProperty();
    aReturn.addProperty(property);
    property.setAs(displayName);
    if (subPath.contains(" ")) {
      String[] elements = subPath.split(" ");
      for (int i = 0; i < elements.length; i = i + 2) {
        property.setIri(IM.NAMESPACE + elements[i]);
        if (i < (elements.length - 2)) {
          property.setReturn(new Return());
          ReturnProperty subProperty = new ReturnProperty();
          property.getReturn().addProperty(subProperty);
          property = subProperty;
        }
      }
    } else {
      aReturn.property(p -> p.setIri(IM.NAMESPACE + subPath));
    }
  }

}
