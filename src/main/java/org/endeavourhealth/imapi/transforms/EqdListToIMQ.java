package org.endeavourhealth.imapi.transforms;


import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;

import java.io.IOException;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdListToIMQ {
  private EqdResources resources;

  public void convertReport(EQDOCReport eqReport, TTDocument document, Query query, EqdResources resources, List<Graph> graphs) throws IOException, QueryException, EQDException {
    this.resources = resources;
    this.resources.setQueryType(QueryType.LIST);
    query.setTypeOf(new Node().setIri(Namespace.IM + "Patient"));
    String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
    if (EqdToIMQ.versionMap.containsKey(id)) id = EqdToIMQ.versionMap.get(id);
    query.addInstanceOf(new Node().setIri(resources.getNamespace() + id).setMemberOf(true)
      .setName(resources.reportNames.get(id)));
    for (EQDOCListReport.ColumnGroups eqColGroups : eqReport.getListReport().getColumnGroups()) {
      EQDOCListColumnGroup eqColGroup = eqColGroups.getColumnGroup();
      Query subQuery = new Query();
      subQuery.setIri(resources.getNamespace() + eqColGroup.getId());
      TTEntity columnGroup = new TTEntity()
        .setIri(subQuery.getIri())
        .setName(eqColGroup.getDisplayName() + " in " + eqReport.getName())
        .addType(IM.FIELD_GROUP.asIri());
      columnGroup.addObject(IM.USED_IN.asIri(), iri(query.getIri()));
      query.addDataSet(subQuery);
      convertListGroup(eqColGroup, subQuery, query.getName(), graphs);
      columnGroup.set(IM.DEFINITION.asIri(), TTLiteral.literal(subQuery));
      document.addEntity(columnGroup);
    }
  }


  private void convertListGroup(EQDOCListColumnGroup eqColGroup, Query subQuery, String reportName, List<Graph> graphs) throws IOException, QueryException, EQDException {
    String eqTable = eqColGroup.getLogicalTableName();
    subQuery.setName(eqColGroup.getDisplayName());
    resources.setColumnGroup(iri(subQuery.getIri()).setName(subQuery.getName() + " in " + reportName));
    if (eqColGroup.getCriteria() == null) {
      convertPatientColumns(eqColGroup, eqTable, subQuery);
    } else {
      convertEventColumns(eqColGroup, eqTable, subQuery, graphs);
    }
    resources.setColumnGroup(null);
  }

  private void convertPatientColumns(EQDOCListColumnGroup eqColGroup, String eqTable, Query subQuery) throws EQDException {
    EQDOCListColumns eqCols = eqColGroup.getColumnar();
    Return select = new Return();
    subQuery.setReturn(select);
    for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
      String eqColumn = String.join("/", eqCol.getColumn());
      String eqULR = eqTable + "/" + eqColumn;
      String propertyPath = resources.getIMPath(eqULR);
      convertColumn(select, propertyPath, eqCol.getDisplayName());
    }
  }


  private void convertEventColumns(EQDOCListColumnGroup eqColGroup, String eqTable, Query subQuery, List<Graph> graphs) throws IOException, QueryException, EQDException {
    if (eqColGroup.getCriteria() != null) {
      resources.setRule(1);
      resources.setSubRule(1);
      Match match = resources.convertCriteria(eqColGroup.getCriteria(), graphs);
      subQuery.addAnd(match);
    }
    Return aReturn = new Return();
    subQuery.setReturn(aReturn);
    String tablePath = resources.getIMPath(eqTable);
    String[] paths = tablePath.split(" ");
    for (int i = 0; i < paths.length - 1; i = i + 2) {
      ReturnProperty property = new ReturnProperty().setIri(paths[i].replace("^", ""));
      aReturn.addProperty(property);
      aReturn = property.setReturn(new Return()).getReturn();
    }

    if (eqColGroup.getColumnar() == null) {
      if (eqColGroup.getSummary() != null) {
        if (eqColGroup.getSummary() == VocListGroupSummary.COUNT) {
          aReturn.function(f -> f
            .setName(Function.count));
        } else if (eqColGroup.getSummary() == VocListGroupSummary.EXISTS) {
          aReturn
            .property(p -> p
              .as("Y-N")
              .case_(c -> c
                .when(w -> w
                  .setExists(true)
                  .setThen("Y"))
                .setElse("N")));
        } else
          throw new QueryException("unmapped summary function : " + eqColGroup.getSummary().value());
      }
    } else {
      aReturn
        .property(p -> p
          .as("Y-N")
          .case_(c -> c
            .when(w -> w
              .setExists(true)
              .setThen("Y"))
            .setElse("N")));
      EQDOCListColumns eqCols = eqColGroup.getColumnar();
      for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
        String eqColumn = String.join("/", eqCol.getColumn());
        String eqURL = eqTable + "/" + eqColumn;
        String subPath = resources.getIMPath(eqURL);
        convertColumn(aReturn, subPath, eqCol.getDisplayName());
      }
    }
  }

  private void convertColumn(Return aReturn, String subPath, String as) {
    String[] elements = subPath.split(" ");
    for (int i = 0; i < elements.length - 1; i = i + 2) {
      ReturnProperty path = new ReturnProperty();
      path.setIri(elements[i]);
      aReturn.addProperty(path);
      aReturn = new Return();
      path.setReturn(aReturn);
    }
    ReturnProperty property = new ReturnProperty();
    aReturn.addProperty(property);
    property
      .setIri(elements[elements.length - 1]);
    if (as != null)
      property.setAs(as);
  }

}
