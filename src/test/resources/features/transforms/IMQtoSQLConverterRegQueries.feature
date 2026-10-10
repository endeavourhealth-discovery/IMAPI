Feature: IMQ to SQL conversion

  @IMQREGQueriesTest
  Scenario: IMQ converts to SQL without errors for all register queries
    When IMQ to SQL conversion is executed for all register queries
    Then SQL should be generated successfully for all of them