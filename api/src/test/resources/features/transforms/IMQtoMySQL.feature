Feature: IMQ to MySQL

  Scenario: Get all patients query
    Given a valid IMQ "IMQToMySQLTestQueries\getAllPatients.json"
    When I convert to MySQL
    Then MySQL should not contain error
    And MySQL is valid SQL

  Scenario: Get all patients aged between 65 and 70 query
    Given a valid IMQ "IMQToMySQLTestQueries\getAllPatientsAgedBetween65And75.json"
    When I convert to MySQL
    Then MySQL should not contain error
    And MySQL is valid SQL
