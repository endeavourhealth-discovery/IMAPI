package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateGroup;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateReport;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;
import org.endeavourhealth.imapi.transforms.eqd.VocStandardAuditReportType;
import org.endeavourhealth.imapi.vocabulary.IM;

public class EqdAuditToIMQ {
  public static final String POPULATION = "population_";

  public void convertReport(EQDOCReport eqReport, Query query, EqdResources resources) throws EQDException {
    for (String popId : eqReport.getAuditReport().getPopulation()) {
      Query popQuery= new Query();
      query.addQuery(popQuery);
      popQuery
        .match(f -> f
          .setVariable(POPULATION)
          .addInstanceOf(new Node().setIri(resources.getNamespace()+"Query_"+ popId).setMemberOf(true))
          .setName(resources.reportNames.get(popId)));
      Return populationReturn = new Return();
      popQuery.setReturn(populationReturn);
      populationReturn.setNodeRef(POPULATION);
      if (eqReport.getAuditReport().getStandard() != null) {
        if (eqReport.getAuditReport().getStandard() == VocStandardAuditReportType.COUNTS) {
          populationReturn.function(f -> f
            .setName(Function.count));
        }
      }
      else  if (eqReport.getAuditReport().getCustomAggregate() != null) {
        EQDOCAggregateReport agg = eqReport.getAuditReport().getCustomAggregate();
        String eqTable = agg.getLogicalTable();
        for (EQDOCAggregateGroup group : agg.getGroup()) {
          for (String eqColumn : group.getGroupingColumn()) {
            String eqURL = eqTable + "/" + eqColumn;
            String pathString = resources.getPath(eqURL);
            String[] pathMap = pathString.split(" ");
            for (int i = 0; i < pathMap.length-1; i++) {
              ReturnProperty property = new ReturnProperty();
              property.setIri(IM.NAMESPACE + pathMap[i]);
              Return ret = new Return();
              property.setReturn(ret);
              populationReturn = ret;
            }
            populationReturn.addProperty(new ReturnProperty()
              .as(eqColumn)
              .setIri(pathMap[pathMap.length-1]));
            popQuery.addGroupBy(new PropertyRef().setPropertyRef(eqColumn));
              }
            }
          }
        }
   }
}