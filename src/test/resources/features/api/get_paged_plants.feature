@user @positive @TC_API_USER_PLANT_010
Feature: Plant API - Retrieve Plants with Pagination
  As an authorized user
  I want to retrieve plants using pagination
  So that large datasets are manageable

  Background:
    Given application server is running
    And user is logged in as an authorized user
    And plant data exists in the database

  @TC_API_USER_PLANT_010
  Scenario: Verify retrieve plants with pagination and sorting
    When user sends GET request to "/api/plants/paged" with page=0 and size=5
    Then the response status code should be 200 OK
    And response should contain paginated plant data
    And number of returned records should be less than or equal to page size
