package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateGroup;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateReport;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;
import org.endeavourhealth.imapi.transforms.eqd.VocStandardAuditReportType;
import org.endeavourhealth.interfacemanager.model.IM;
import org.endeavourhealth.interfacemanager.model.NAMESPACE;
import org.endeavourhealth.interfacemanager.model.QueryType;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdAuditToIMQ {
  public static final String POPULATION = "population_";

  public void convertReport(EQDOCReport eqReport, Query query, EqdResources resources) throws EQDException {
    resources.setQueryType(QueryType.AGGREGATE_REPORT);
    for (String popId : eqReport.getAuditReport().getPopulation()) {
      String finalPopId = resources.getNamespace() + popId;
      if (EqdToIMQ.versionMap.containsKey(popId))
        finalPopId = resources.getNamespace() + EqdToIMQ.versionMap.get(popId);
      if (EqdToIMQ.gmsPatients.contains(popId) || EqdToIMQ.gmsPatients.contains(eqReport.getVersionIndependentGUID())) {
        finalPopId = NAMESPACE.IM + "Q_RegisteredGMS";
      }
      Match popQuery = new Match();
      query.addColumnGroup(popQuery);
      popQuery
        .setNode(POPULATION)
        .addIs(Node.iri(finalPopId)
          .setIsCohort(true)
          .setName(resources.reportNames.get(finalPopId)));
      resources.getQueryEntity().addObject(iri(IM.DEPENDENT_ON), iri(finalPopId));
      Return populationReturn = new Return();
      popQuery.addReturn(populationReturn);
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
            if (pathMap.length > 1) {
              Path path = new Path();
              popQuery.addPath(path);
              path.setIri(NAMESPACE.IM + pathMap[1]);
              path.setTypeOf(new Node().setIri(NAMESPACE.IM + pathMap[1]));
              path.setNode(resources.getAcronym(path.getIri()) + "_" + eqColumn);
              for (int i = 2; i < pathMap.length - 1; i++) {
                Path subPath = new Path();
                path.addPath(subPath);
                subPath.setIri(NAMESPACE.IM + pathMap[i]);
                subPath.setNode(resources.getAcronym(path.getIri()));
                subPath.setTypeOf(new Node().setIri(NAMESPACE.IM + pathMap[i + 1]));
                path = subPath;
              }
              popQuery.addReturn(new Return()
                .setNodeRef(path.getNode())
                .as(eqColumn)
                .setIri(pathMap[pathMap.length - 1]));
              popQuery.addGroupBy(new GroupBy().setPropertyRef(eqColumn));
            }
          }
        }
      }
    }
  }
}