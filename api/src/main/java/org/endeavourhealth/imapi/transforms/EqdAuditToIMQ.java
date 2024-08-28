package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateGroup;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCAggregateReport;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;

public class EqdAuditToIMQ {
  public static final String POPULATION = "population";

  public void convertReport(EQDOCReport eqReport, Query query, EqdResources resources) throws EQDException {
    Match match = new Match();
    query.addMatch(match);
    match.setVariable(POPULATION);
    match.setBoolMatch(Bool.or);
    for (String popId : eqReport.getAuditReport().getPopulation()) {
      match
        .match(f -> f
          .addInstanceOf(new Node().setIri("urn:uuid:" + popId).setMemberOf(true))
          .setName(resources.reportNames.get(popId)));
    }
    Return aReturn = new Return();
    query.addReturn(aReturn);
    aReturn.setNodeRef(POPULATION);
    query.addGroupBy(new PropertyRef().setVariable(POPULATION));
    EQDOCAggregateReport agg = eqReport.getAuditReport().getCustomAggregate();
    String eqTable = agg.getLogicalTable();
    String tablePath = resources.getPath(eqTable);
    if (tablePath.contains(" ")) {
      String[] paths = tablePath.split(" ");
      for (int i = 0; i < paths.length; i = i + 2) {
        aReturn.addPath(new IriLD().setIri(paths[i].replace("^", "")));
      }
    }
    for (EQDOCAggregateGroup group : agg.getGroup()) {
      for (String eqColumn : group.getGroupingColumn()) {
        String eqURL = eqTable + "/" + eqColumn;
        String pathString = resources.getPath(eqURL);
        String[] pathMap = pathString.split(" ");
        for (int i = 0; i < pathMap.length - 1; i = i + 2) {
          ReturnProperty path = new ReturnProperty();
          aReturn.addProperty(path);
          path.setIri(pathMap[i]);
          if (i < (pathMap.length - 2)) {
            Return node = new Return();
            path.setReturn(node);
            aReturn = node;
          } else {
            path.setPropertyRef(pathMap[i]);
            query.addGroupBy(new PropertyRef().setVariable(pathMap[i]));
          }
        }

      }
    }
  }

}
