@user @negative @TC_API_USER_PLANT_009
Feature: User Plant API - Edit Plant Tests
  As a normal user
  I should not be able to edit plants
  So that only admins can manage plant inventory

  Background:
    Given user is logged in as a normal user
    And user has a valid authentication token

  @TC_API_USER_PLANT_009
  Scenario: Verify user cannot edit plant
    When user sends PUT request to "/api/plants/{id}" with updated plant data
    Then the response status code should be 403 Forbidden
    And the plant should not be updated
