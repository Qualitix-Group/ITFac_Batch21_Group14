@user @negative @TC_API_USER_PLANT_008
Feature: User Plant API - Create Plant Tests
  As a normal user
  I should not be able to create plants
  So that only admins can manage plant inventory

  Background:
    Given user is logged in as a normal user
    And user has a valid authentication token

  @TC_API_USER_PLANT_008
  Scenario: Verify user cannot add plant
    When user sends POST request to "/api/plants/category/{categoryId}" with plant data
    Then the response status code should be 403 Forbidden
    And no new plant should be created