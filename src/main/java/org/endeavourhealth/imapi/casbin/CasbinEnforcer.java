package org.endeavourhealth.imapi.casbin;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.Getter;
import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;

public class CasbinEnforcer {
  @Getter
  private Enforcer enforcer;
  private MysqlDataSource dataSource = new MysqlDataSource();
  private JDBCAdapter adapter;

  public CasbinEnforcer() throws Exception {
    String mysqlCasdoorUrl = System.getenv().getOrDefault("MYSQL_CASDOOR_URL", "jdbc:mysql://localhost:3306/casbin");
    this.dataSource.setURL(mysqlCasdoorUrl);
    String mysqlUser = System.getenv().getOrDefault("MYSQL_USER", "root");
    this.dataSource.setUser(mysqlUser);
    String mysqlPassword = System.getenv().getOrDefault("MYSQL_PASSWORD", "password");
    this.dataSource.setPassword(mysqlPassword);
    this.adapter = new JDBCAdapter(this.dataSource);
    this.enforcer = new Enforcer("casbin_model.conf", mysqlCasdoorUrl);
  }

}
