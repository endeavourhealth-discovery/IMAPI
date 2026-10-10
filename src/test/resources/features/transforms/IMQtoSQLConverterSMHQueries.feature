Feature: IMQ to SQL conversion

  @IMQSMHQueriesTest
  Scenario: IMQ converts to SQL without errors for all SMH queries
    When IMQ to SQL conversion is executed for all SMH queries
    Then SQL should be generated successfully for all of them