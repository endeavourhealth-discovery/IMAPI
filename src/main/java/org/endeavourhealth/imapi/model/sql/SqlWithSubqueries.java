package org.endeavourhealth.imapi.model.sql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.endeavourhealth.imapi.model.requests.QueryRequest;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SqlWithSubqueries {
  private String sql;
  private List<QueryRequest> subqueryRequests;

  public SqlWithSubqueries(String sql, List<QueryRequest> subqueryRequests) {
    this.sql = sql;
    this.subqueryRequests = subqueryRequests;
  }

}
