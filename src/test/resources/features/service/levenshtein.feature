Feature: Levenshtein is calculated

  Scenario Outline: When the value is unchanged, Levenshtein should be 0
    Given start value is <start>
    When the start value is not changed
    Then the Levenshtein value should be 0

    Examples:
      | start      |
      | "Asthma"   |
      | ""         |
      | null       |

  Scenario Outline: Test Levenshtein value for numerous edits
    Given start value is <start>
    When the start value changed to <edited>
    Then the Levenshtein value should be <lev>

    Examples:
      | start      | edited   | lev |
      | "Asthma"   | "Bsthma" | 1   |
      | "Asthma"   | ""       | 6   |
      | ""         | "Asthma" | 6   |
      | "Asthma"   | null     | 0   |
      | null       | "Asthma" | 0   |
      | "Asthma"   | "sAthma" | 2   |
      | null       | null     | 0   |

  Scenario: When the value is unchanged, Levenshtein should be 0
    Given start value is "Rich"
    When the start value is not changed
    Then the Levenshtein value should be 0

  Scenario: Test Levenshtein value for numerous edits
    Given start value is "Start"
    When the start value changed to "End"
    Then the Levenshtein value should be 5
