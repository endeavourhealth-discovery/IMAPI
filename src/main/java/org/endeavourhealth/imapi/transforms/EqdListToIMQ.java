package org.endeavourhealth.imapi.transforms;


import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;


import java.io.IOException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdListToIMQ {
  private EqdResources resources;

  public void convertReport(EQDOCReport eqReport, TTDocument document, Query query, EqdResources resources) throws IOException, QueryException, EQDException {
    this.resources = resources;
    this.resources.setQueryType(QueryType.LIST);
    query.setTypeOf(new Node().setIri(Namespace.IM + "Patient"));
    String id;
    if (eqReport.getParent().getSearchIdentifier() != null) {
      id = eqReport.getParent().getSearchIdentifier().getReportGuid();
      id = resources.getNamespace() + EqdToIMQ.versionMap.getOrDefault(id, id);
    } else if (eqReport.getParent().getParentType() == VocPopulationParentType.ACTIVE) {
      id = Namespace.IM + "Q_RegisteredGMS";
    } else throw new EQDException("parent population at definition level");
    query.addIs(Node.iri(id)
      .setIsCohort(true)
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
    subQuery= convertColumns(eqColGroup,eqTable);
    subQuery.setName(eqColGroup.getDisplayName());
    return subQuery;
  }


  private HasPaths getLeafPath(HasPaths hasPaths) {
    if (hasPaths.getPath()!=null){
      if (hasPaths.getPath().getFirst().getPath()!=null){
        return getLeafPath(hasPaths.getPath().getFirst());
      }
      else return hasPaths.getPath().getFirst();
    }
    return hasPaths;
  }

  private String getNodeRef(HasPaths hasPaths,String[] propertyPath,int startIndex){
    String nodeRef=null;
    for (int i=startIndex;i<propertyPath.length-2;i++){
      if (hasPaths.getPath()!=null) {
        for (Path path : hasPaths.getPath()) {
          if (path.getIri() != null && path.getIri().equals(propertyPath[i])) {
            if (i == propertyPath.length - 3) {
              return path.getNode();
            } else {
              nodeRef=getNodeRef(path, propertyPath, i + 2);
              if (nodeRef!=null)
                return nodeRef;
            }
          }
        }
      }
        Path path=new Path();
        hasPaths.addPath(path);
        path.setIri(propertyPath[i]);
        path.setOptional(true);
        path.setNode(resources.getAcronym(propertyPath[i+1]));
        path.setTypeOf(new Node().setIri(propertyPath[i+1]));
        if (i == propertyPath.length - 3)
          return path.getNode();
        else return getNodeRef(path, propertyPath, i + 2);
    }
    return null;
  }

  private Match convertColumns(EQDOCListColumnGroup eqColGroup, String eqTable) throws IOException, QueryException, EQDException {
    resources.setRule(1);
    resources.setSubRule(1);
    String tablePath = resources.getIMPath(eqTable);
    Match subQuery;
    if (eqColGroup.getCriteria()!=null)
      subQuery = resources.convertCriteria(eqColGroup.getCriteria());
    else {
      subQuery = new Match();
    }
    Return aReturn = new Return();
    subQuery.setReturn(aReturn);
    if (eqColGroup.getColumnar() == null) {
      if (eqColGroup.getSummary() != null) {
        if (eqColGroup.getSummary() == VocListGroupSummary.COUNT) {
          aReturn.function(f -> f
            .setIri(IM.COUNT.toString()));
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
      EQDOCListColumns eqCols = eqColGroup.getColumnar();
      for (EQDOCListColumn eqCol : eqCols.getListColumn()) {
        String eqColumn = String.join("/", eqCol.getColumn());
        String eqURL = eqTable + "/" + eqColumn;
        String columnPath = resources.getIMPath(eqURL);
        if (columnPath.contains("$concat(")){
          convertReturnConcatenate(aReturn,subQuery,eqTable,tablePath,columnPath,eqCol.getDisplayName());
          continue;
        }
        else {
          String[] subPath = (tablePath+" "+ columnPath).trim().split(" ");
          String nodeRef = getNodeRef(subQuery, subPath, 0);
          convertColumn(aReturn, subPath[subPath.length - 1], eqCol.getDisplayName(), nodeRef);
        }
      }
    }
    return subQuery;
  }

  private void convertColumn(Return aReturn, String propertyIri,String as,String nodeRef) throws EQDException {
      ReturnProperty property = new ReturnProperty();
      aReturn.addProperty(property);
      property
        .setIri(propertyIri)
        .setNodeRef(nodeRef);
      if (as != null)
        property.setAs(as);
  }

  private void convertReturnConcatenate(Return aReturn, Match match,String eqTable, String tablePath,String eqPaths,String as) throws EQDException {
    FunctionClause function = new FunctionClause();
    ReturnProperty property = new ReturnProperty();
    aReturn.addProperty(property);
    property.setFunction(function);
    if (as!=null)
      property.setAs(as);
    function.setIri(IM.CONCATENATE.toString());
    String concats= eqPaths.substring(eqPaths.indexOf("$concat(") + 8, eqPaths.length() - 1);
    for (String eqPath : concats.split(" ")) {
      String eqURL = eqTable + "/" + eqPath;
      String columnPath = resources.getIMPath(eqURL);
      String[] subPath= (tablePath+" "+ columnPath).trim().split(" ");
      String nodeRef = getNodeRef(match, subPath, 0);
        Argument arg = new Argument();
        function.addArgument(arg);
        Path argPath = new Path();
        argPath.setNodeRef(nodeRef);
        argPath.setIri(subPath[subPath.length-1]);
        arg.setValuePath(argPath);
      }
    }
}
