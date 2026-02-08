Feature: Admin Add Plant

  @Smoke @AdminPlantAdd
  Scenario: Verify add plant with valid mandatory data
    Given I am logged in as admin and on Plants page
    When I add a plant with valid data
    Then the plant should be added successfully

  @Validation @AdminPlantAdd
  Scenario: Verify submit without plant name
    Given I am logged in as admin and on Plants page
    When I try to add a plant without a name
    Then I should see plant name required validation message

  @Validation @AdminPlantAdd
  Scenario: Verify enter plant name more than 25 characters
    Given I am logged in as admin and on Plants page
    When I try to add a plant with name longer than allowed
    Then I should see plant name length validation message

  @Validation @AdminPlantAdd
  Scenario: Verify leave price empty
    Given I am logged in as admin and on Plants page
    When I try to add a plant without price
    Then I should see price required validation message
