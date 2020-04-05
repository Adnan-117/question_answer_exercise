Feature: Question and Answers

  Scenario: question asked not stored in the database
    Given the user asks a question "What is Peters favourite food?"
    Then verify this question doesn't exist
    And the program prints the default answer

  Scenario: user add a question with answers not stored in the database with question overflows the max characters
    Given the user adds question exceeding maximum characters
    Then the question is not stored in the database

  Scenario: user add a question with answers not stored in the database with answer overflows the max characters
    Given the user adds question with answers exceeding maximum characters
    Then the question with answer is not stored in the database


  Scenario: user adds a question with answers not stored in the database
    Given the user adds the question "What is Peters favourite food?" with answers
      | "Pasta"   |
      | "Rice"    |
      | "Chicken" |
    Then verify this question doesn't exist with answers
    And the question includes atleast one answer
    And both question and answers doesn't exceed the maximum character space of 255
    And the program stores the question into the database


  Scenario: question asked stored in the database
    Given the user asks a question "What is Peters favourite food?" already stored
    Then verify this question exists
    And the program must fetch the following answers
      | "Pasta"   |
      | "Rice"    |
      | "Chicken" |

