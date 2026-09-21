Feature: IMQ to SQL conversion

  @IMQQOFQueriesTest
  Scenario: IMQ converts to SQL without errors for all QOF queries
    When IMQ to SQL conversion is executed for all QOF queries
    Then SQL should be generated successfully for all of them