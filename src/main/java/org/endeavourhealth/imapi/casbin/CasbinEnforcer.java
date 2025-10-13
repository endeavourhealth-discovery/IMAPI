package org.endeavourhealth.imapi.casbin;

import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.casbin.adapter.JDBCAdapter;
import org.casbin.casdoor.entity.User;
import org.casbin.jcasbin.main.Enforcer;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;

import java.util.ArrayList;
import java.util.List;

public class CasbinEnforcer {
  @Getter
  private Enforcer enforcer;
  private MysqlDataSource dataSource = new MysqlDataSource();
  private JDBCAdapter adapter;
  private CasdoorService casdoorService = new CasdoorService();

  public CasbinEnforcer() {
  }

  private void setupEnforcer() throws UserAuthorisationException {
    String mysqlCasdoorUrl = System.getenv().getOrDefault("MYSQL_CASDOOR_URL", "jdbc:mysql://localhost:3306/casbin");
    this.dataSource.setURL(mysqlCasdoorUrl);
    String mysqlUser = System.getenv().getOrDefault("MYSQL_USER", "root");
    this.dataSource.setUser(mysqlUser);
    String mysqlPassword = System.getenv().getOrDefault("MYSQL_PASSWORD", "password");
    this.dataSource.setPassword(mysqlPassword);
    try {
      this.adapter = new JDBCAdapter(this.dataSource);
    } catch (Exception e) {
      throw new UserAuthorisationException("Failed to setup enforcer");
    }
    this.enforcer = new Enforcer("casbin_model.conf", this.adapter);
  }

  public void enforce(String user, DataSource dataRequest, UserRole accessRequest) throws UserAuthorisationException {
    if (null == this.enforcer) {
      setupEnforcer();
    }
    try {
      boolean result = enforcer.enforce(user, dataRequest, accessRequest);
      if (!result) {
        throw new UserAuthorisationException(String.format("User %s not authorised to access resource %s with rights %s", user, dataRequest, accessRequest));
      }
    } catch (Exception e) {
      throw new UserAuthorisationException(String.format("User %s not authorised to access resource %s with rights %s", user, dataRequest, accessRequest));
    }
  }

  public void enforce(HttpSession session, DataSource dataRequest, UserRole accessRequest) throws UserAuthorisationException {
    User user = casdoorService.getUser(session);
    enforce(user.name, dataRequest, accessRequest);
  }

  public void enforceOr(HttpSession session, DataSource dataRequest, List<UserRole> accessRequests) throws UserAuthorisationException {
    User user = casdoorService.getUser(session);
    List<Boolean> results = new ArrayList<>();
    for (UserRole accessRequest : accessRequests) {
      results.add(enforcer.enforce(user.name, dataRequest, accessRequest));
    }
    if (results.stream().noneMatch(r -> r)) {
      throw new UserAuthorisationException(String.format("User %s not authorised to access resource %s with rights %s", user, dataRequest, accessRequests));
    }
  }
}
