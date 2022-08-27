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
      | start      | edited   | lev | comment
      | "Asthma"   | "Bsthma" | 1   | Single change
      | "Asthma"   | ""       | 6   | Edit to empty
      | ""         | "Asthma" | 6   | Start with empty
      | "Asthma"   | null     | 0   | Edit to null
      | null       | "Asthma" | 0   | Start with null
      | "Asthma"   | "sAthma" | 2   | Character swap
      | null       | null     | 0   | Null check

  Scenario: When the value is unchanged, Levenshtein should be 0
    Given start value is "Rich"
    When the start value is not changed
    Then the Levenshtein value should be 0

  Scenario: Test Levenshtein value for numerous edits
    Given start value is "Start"
    When the start value changed to "End"
    Then the Levenshtein value should be 5
