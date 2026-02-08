@api @security
Feature: Security API
  As an API consumer
  I expect proper authentication enforcement

  @API-SEC-ADMIN-01
  Scenario: Access without token returns 401
    When I send a GET request to "/api/categories" without authentication
    Then the response status should be 401

  @API-SEC-ADMIN-02
  Scenario: Invalid token returns 401
    When I send a GET request to "/api/plants" with invalid token
    Then the response status should be 401
