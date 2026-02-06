@admin
Feature: Admin plant creation

  Scenario: Admin creates a plant under a valid sub-category
    When the admin creates a plant under a valid sub-category
    Then the plant should be created successfully

  Scenario: Admin attempts to create a duplicate plant
    Given a plant already exists in the sub-category
    When the admin creates a plant with the same name
    Then the request should be rejected
