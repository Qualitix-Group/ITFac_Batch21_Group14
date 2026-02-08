
Feature: Delete already deleted category (Admin)


  @API @Admin @Delete
  Scenario: Admin deletes a category that no longer exists
    Given a valid admin bearer token is available for deleting category
    When I send a DELETE request for an already deleted category id 9
    Then the response status code for deleted category should be 404
    And the response should contain category not found error message
