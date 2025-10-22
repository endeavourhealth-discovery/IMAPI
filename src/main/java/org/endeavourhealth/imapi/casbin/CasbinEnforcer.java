package org.endeavourhealth.imapi.casbin;

import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.model.admin.User;
import org.endeavourhealth.imapi.model.casbin.AccessRequest;

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

  public void enforce(User user, String dataRequest, AccessRequest accessRequest) throws UserAuthorisationException {
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

  public void enforce(HttpServletRequest request, AccessRequest accessRequest) throws UserAuthorisationException {
    User user = casdoorService.getUser(request.getSession());
    String path = request.getRequestURI();
    enforce(user, path, accessRequest);
  }

  public void enforceOr(HttpServletRequest request, List<AccessRequest> accessRequests) throws UserAuthorisationException {
    User user = casdoorService.getUser(request.getSession());
    String path = request.getRequestURI();
    List<Boolean> results = new ArrayList<>();
    for (AccessRequest accessRequest : accessRequests) {
      results.add(enforcer.enforce(user, path, accessRequest));
    }
    if (results.stream().noneMatch(r -> r)) {
      throw new UserAuthorisationException(String.format("User %s not authorised to access resource %s with rights %s", user, path, accessRequests));
    }
  }

  public void addPolicy(String userId, String dataSource, AccessRequest accessRequest) throws UserNotFoundException {
    User user = casdoorService.adminGetUser(userId);
    enforcer.addPolicy(user.toString(), dataSource, accessRequest.toString());
  }
}
