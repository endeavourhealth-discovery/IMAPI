Feature: Public mode

  Scenario: Hosting mode is null
    Given the environment variable "HOSTING_MODE" is unset
    Then the server runs in "private" mode

  Scenario: Hosting mode is "public"
    Given the environment variable "HOSTING_MODE" is set to "public"
    Then the server runs in "public" mode

  Scenario: Hosting mode is "private"
    Given the environment variable "HOSTING_MODE" is set to "private"
    Then the server runs in "private" mode

  Scenario: Hosting mode is nonsense/corrupt
    Given the environment variable "HOSTING_MODE" is set to "foo"
    Then the server runs in "private" mode