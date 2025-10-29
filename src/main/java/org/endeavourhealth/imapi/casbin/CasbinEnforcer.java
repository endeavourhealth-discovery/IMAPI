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
import org.endeavourhealth.imapi.model.casbin.Action;
import org.endeavourhealth.imapi.model.casbin.Resource;

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
    this.enforcer.enableAutoSave(true);
  }

  public void enforceWithError(User user, Resource resource, Action action) throws UserAuthorisationException {
    try {
      boolean result = enforce(user, resource, action);
      if (!result) {
        throw new UserAuthorisationException(String.format("User %s not authorised to access resource %s with rights %s", user, resource, action));
      }
    } catch (Exception e) {
      throw new UserAuthorisationException(String.format("User %s not authorised to access resource %s with rights %s", user, resource, action));
    }
  }

  public void enforceWithError(HttpServletRequest request, Resource resource, Action action) throws UserAuthorisationException {
    User user = casdoorService.getUser(request.getSession());
    enforceWithError(user, resource, action);
  }

  public boolean enforce(User user, Resource resource, Action action) throws UserAuthorisationException {
    if (null == this.enforcer) {
      setupEnforcer();
    }
    return enforcer.enforce(user, resource, action);
  }

  public void enforceOr(HttpServletRequest request, Resource resource, List<Action> accessRights) throws UserAuthorisationException {
    User user = casdoorService.getUser(request.getSession());
    List<Boolean> results = new ArrayList<>();
    for (Action accessRight : accessRights) {
      results.add(enforcer.enforce(user, resource, accessRight));
    }
    if (results.stream().noneMatch(r -> r)) {
      throw new UserAuthorisationException(String.format("User %s not authorised to access resource %s with rights %s", user, resource, accessRights));
    }
  }

  public void addPolicy(String userId, Resource resource, Action action) throws UserNotFoundException {
    User user = casdoorService.adminGetUser(userId);
    enforcer.addPolicy(user.toString(), resource.toString(), action.toString());
  }

}
