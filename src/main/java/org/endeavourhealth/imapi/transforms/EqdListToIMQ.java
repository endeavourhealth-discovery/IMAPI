package org.endeavourhealth.imapi.transforms;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdListToIMQ {
  private EqdResources resources;

  public void convertReport(EQDOCReport eqReport, TTDocument document, Query query, EqdResources resources) throws IOException, QueryException, EQDException {
    this.resources = resources;
    this.resources.setQueryType(QueryType.LIST);
    query.setTypeOf(new Node().setIri(Namespace.IM + "Patient"));
    String id;
    if (eqReport.getParent().getSearchIdentifier() != null){
      id = eqReport.getParent().getSearchIdentifier().getReportGuid();
      id = resources.getNamespace() + EqdToIMQ.versionMap.getOrDefault(id, id);
    }
    else if (eqReport.getParent().getParentType()==VocPopulationParentType.ACTIVE){
      id= Namespace.IM+"Q_RegisteredGMS";
    }
    else throw new EQDException("parent population at definition level");
    query.setIsCohort(iri(id)
      .setName(resources.reportNames.get(id)));
    for (EQDOCListReport.ColumnGroups eqColGroups : eqReport.getListReport().getColumnGroups()) {
      EQDOCListColumnGroup eqColGroup = eqColGroups.getColumnGroup();
      Match subQuery = new Match();
      subQuery.setIri(resources.getNamespace() + eqColGroup.getId());
      query.addColumnGroup(convertListGroup(eqColGroup));
    }
  }


  private Match convertListGroup(EQDOCListColumnGroup eqColGroup) throws IOException, QueryException, EQDException {
    String eqTable = eqColGroup.getLogicalTableName();
    Match subQuery;

    if (eqColGroup.getCriteria() == null) {
      subQuery= convertPatientColumns(eqColGroup, eqTable);
      subQuery.setName(eqColGroup.getDisplayName());
      return subQuery;
    } else {
      subQuery= convertEventColumns(eqColGroup, eqTable);
      subQuery.setName(eqColGroup.getDisplayName());
      return subQuery;
    }
  }

  private Match convertPatientColumns(EQDOCListColumnGroup eqColGroup, String eqTable) throws EQDException {
    Match subQuery = new Match();
    EQDOCListColumns eqCols = eqColGroup.getColumnar();
    Return select = new Return();
    subQuery.setReturn(select);
    for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
      String eqColumn = String.join("/", eqCol.getColumn());
      String eqULR = eqTable + "/" + eqColumn;
      String propertyPath = resources.getIMPath(eqULR);
      convertColumn(select, propertyPath, eqCol.getDisplayName());
    }
    return subQuery;
  }

  private Match convertEventColumns(EQDOCListColumnGroup eqColGroup, String eqTable) throws IOException, QueryException, EQDException {
    resources.setRule(1);
    resources.setSubRule(1);
    Match subQuery = resources.convertCriteria(eqColGroup.getCriteria());
    Return aReturn = new Return();
    subQuery.setReturn(aReturn);
    String nodeRef = resources.getNodeRef(subQuery);
    String tablePath = resources.getIMPath(eqTable);
    String[] paths = tablePath.split(" ");
    for (int i = 2; i < paths.length - 1; i = i + 2) {
      ReturnProperty property = new ReturnProperty();
      property.setNodeRef(nodeRef);
      property.setIri(paths[i].replace("^", ""));
      aReturn.addProperty(property);
      aReturn = property.setReturn(new Return()).getReturn();
    }

    if (eqColGroup.getColumnar() == null) {
      if (eqColGroup.getSummary() != null) {
        if (eqColGroup.getSummary() == VocListGroupSummary.COUNT) {
          aReturn.function(f -> f
            .setName(IM.COUNT.toString()));
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
        if (!subPath.contains("notSupported"))
          convertColumn(aReturn, subPath, eqCol.getDisplayName());
      }
    }
    return subQuery;
  }

  private void convertColumn(Return aReturn, String subPath, String as) throws EQDException {
    if (subPath.contains("$concat")) {
      convertReturnConcatenate(aReturn, subPath, as);
      return;
    }
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

  private void convertReturnConcatenate(Return aReturn, String subPath, String as) throws EQDException {
    FunctionClause function = new FunctionClause();
    ReturnProperty property = new ReturnProperty();
    aReturn.addProperty(property);
    property.setFunction(function);
    if (as!=null)
      property.setAs(as);
    function.setIri(IM.CONCATENATE.toString());
    subPath = subPath.substring(subPath.indexOf("$concat(") + 8, subPath.length() - 1);
    ObjectMapper mapper = new ObjectMapper();
    try {
      List<Path> valuePaths = mapper.readValue(subPath, new TypeReference<List<Path>>() {
      });
      
      for (Path valuePath : valuePaths) {
        Argument arg = new Argument();
        function.addArgument(arg);
        arg.setParameter("text");
        arg.setValuePath(valuePath);
      }
    } catch (JsonProcessingException e) {
      throw new EQDException(e.getMessage(), e);
    }
  }

}
