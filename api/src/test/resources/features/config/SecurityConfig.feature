Feature: Security config

  Scenario: "isPublic" endpoint available in private mode
    Given the environment variable "HOSTING_MODE" is set to "public"
    When the server starts up
    Then the is isPublic endpoint will be available

  Scenario: Protected endpoints are not available in private mode
    Given the environment variable "HOSTING_MODE" is set to "private"
    When the server starts up
    Then protected endpoints will not be available

  Scenario: Protected endpoints are available in public mode
    Given the environment variable "HOSTING_MODE" is set to "public"
    When the server starts up
    Then protected endpoints will be available