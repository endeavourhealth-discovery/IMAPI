package org.endeavourhealth.imapi.transforms;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.sql.IMQtoSQLConverter;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class IMQtoMySQLSteps {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private QueryRequest queryRequest;
  private String mysql;

//  private static final String URL = "jdbc:mysql://localhost:3306/compass?useSSL=false&allowPublicKeyRetrieval=true";
//  private static final String USER = "root";
//  private static final String PASSWORD = "8l0>f4AlADd2";

  public static boolean isValidSQL(String mysql) {
    return !mysql.isEmpty() && !mysql.contains("SQL Conversion Error:");
  }

  @Given("a valid IMQ {string}")
  public void a_valid_query(String path) {
    this.queryRequest = loadQueryRequestFromPath(path);
  }

  @When("I convert to MySQL")
  public void i_convert_to_MySQL() {
    try {
      this.mysql = new IMQtoSQLConverter(queryRequest).IMQtoSQL().getSql();
    } catch (SQLConversionException e) {
      this.mysql = e.getMessage();
    }
  }

  @Then("MySQL should not contain error")
  public void the_mysql_should_not_contain_error() {
    boolean isError = mysql.contains("SQL Conversion Error:");
    Assertions.assertFalse(isError);
  }

  @And("MySQL is valid SQL")
  public void mysql_is_valid() {
    boolean isValid = isValidSQL(mysql);
    Assertions.assertTrue(isValid);
  }

  private QueryRequest loadQueryRequestFromPath(String path) {
    try {
      ClassPathResource resource = new ClassPathResource(path);
      Path filePath = resource.getFile().toPath();
      String content = Files.readString(filePath);
      System.out.println(content);
      return objectMapper.readValue(content, QueryRequest.class);
    } catch (Exception e) {
      return null;
    }
  }
//    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//         PreparedStatement stmt = conn.prepareStatement(mysql)) {
//      return true;  // If no exception, query is valid
//    } catch (SQLException e) {
//      System.out.println("Invalid SQL: " + e.getMessage());
//      return false;  // If SQLException occurs, query is invalid
//    }
//  }
}
