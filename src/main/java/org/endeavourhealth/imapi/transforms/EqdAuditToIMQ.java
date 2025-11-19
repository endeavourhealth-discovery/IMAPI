package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateGroup;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateReport;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;
import org.endeavourhealth.imapi.transforms.eqd.VocStandardAuditReportType;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdAuditToIMQ {
  public static final String POPULATION = "population_";

  public void convertReport(EQDOCReport eqReport, Query query, EqdResources resources) throws EQDException {
    resources.setQueryType(QueryType.AGGREGATE_REPORT);
    for (String popId : eqReport.getAuditReport().getPopulation()) {
      if (EqdToIMQ.versionMap.containsKey(popId)) popId = EqdToIMQ.versionMap.get(popId);
      Match popQuery = new Match();
      query.addColumnGroup(popQuery);
      String finalPopId = popId;
      popQuery
        .setVariable(POPULATION)
        .addIs(Node.iri(resources.getNamespace() + finalPopId)
          .setName(resources.reportNames.get(finalPopId)));
      resources.getQueryEntity().addObject(iri(IM.DEPENDENT_ON), iri(resources.getNamespace() + finalPopId));
      Return populationReturn = new Return();
      popQuery.setReturn(populationReturn);
      populationReturn.setNodeRef(POPULATION);
      if (eqReport.getAuditReport().getStandard() != null) {
        if (eqReport.getAuditReport().getStandard() == VocStandardAuditReportType.COUNTS) {
          populationReturn.function(f -> f
            .setIri(IM.COUNT.toString()));
        }
      } else if (eqReport.getAuditReport().getCustomAggregate() != null) {
        EQDOCAggregateReport agg = eqReport.getAuditReport().getCustomAggregate();
        String eqTable = agg.getLogicalTable();
        for (EQDOCAggregateGroup group : agg.getGroup()) {
          for (String eqColumn : group.getGroupingColumn()) {
            String eqURL = eqTable + "/" + eqColumn;
            String pathString = resources.getIMPath(eqURL);
            String[] pathMap = pathString.split(" ");
            for (int i = 0; i < pathMap.length - 1; i++) {
              ReturnProperty property = new ReturnProperty();
              property.setIri(Namespace.IM + pathMap[i]);
              Return ret = new Return();
              property.setReturn(ret);
              populationReturn = ret;
            }
            populationReturn.addProperty(new ReturnProperty()
              .as(eqColumn)
              .setIri(pathMap[pathMap.length - 1]));
            popQuery.addGroupBy(new GroupBy().setPropertyRef(eqColumn));
          }
        }
      }
    }
  }
}