package org.endeavourhealth.imapi.model.sql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SqlWithSubqueries {
  private String sql;
  private List<String> subqueryIris;

  public SqlWithSubqueries(String sql, List<String> subqueryIris) {
    this.sql = sql;
    this.subqueryIris = subqueryIris;
  }

}
