@api @categories
Feature: Categories API
  As an API consumer
  I want to manage categories via REST API

  # ========== Admin Tests ==========

  @API-CAT-ADMIN-01
  Scenario: Admin can get category by ID
    Given I am authenticated as admin via API
    When I send a GET request to the existing category
    Then the response status should be 200
    And the response should contain the category id

  @API-CAT-ADMIN-02
  Scenario: Admin gets error for invalid category ID
    Given I am authenticated as admin via API
    When I send a GET request to "/api/categories/999999"
    Then the response status should be 404 or 400

  @API-CAT-ADMIN-03
  Scenario: Admin gets error for category ID as string
    Given I am authenticated as admin via API
    When I send a GET request to "/api/categories/abc"
    Then the response status should be 400 or 404

  @API-CAT-ADMIN-04
  Scenario: Admin gets error for empty category ID
    Given I am authenticated as admin via API
    When I send a GET request to "/api/categories/"
    Then the response status should be 400 or 404

  @API-CAT-ADMIN-05
  Scenario: Category summary returns aggregated data
    Given I am authenticated as admin via API
    When I send a GET request to "/api/categories/summary"
    Then the response status should be 200

  @API-CAT-ADMIN-06
  Scenario: Admin can create category with valid data
    Given I am authenticated as admin via API
    When I send a POST request to "/api/categories" with a valid name
    Then the response status should be 200 or 201
    And the response should contain an id

  @API-CAT-ADMIN-07
  Scenario: Create category with missing name
    Given I am authenticated as admin via API
    When I send a POST request to create a category with missing name
    Then the response status should be 400 or 422

  @API-CAT-ADMIN-08
  Scenario: Create sub-category with invalid parent
    Given I am authenticated as admin via API
    When I send a POST request to create a category with invalid parent
    Then the response status should be 400 or 404

  @API-CAT-ADMIN-09
  Scenario: Duplicate category name
    Given I am authenticated as admin via API
    When I send a POST request to "/api/categories" with a valid name
    And I send a POST request to create a duplicate category
    Then the response status should be 400 or 422

  @API-CAT-ADMIN-10
  Scenario: Update category with valid name and valid parentId
    Given I am authenticated as admin via API
    When I send a PUT request to update the category with parentId
    Then the response status should be 200 or 204

  @API-CAT-ADMIN-11
  Scenario: Update category with parentId null
    Given I am authenticated as admin via API
    When I send a PUT request to update the category with null parentId
    Then the response status should be 400 or 422

  @API-CAT-ADMIN-12
  Scenario: Category cannot be its own parent
    Given I am authenticated as admin via API
    When I send a PUT request to set the category as its own parent
    Then the response status should be 400 or 422

  @API-CAT-ADMIN-13
  Scenario: Update category with invalid parentId
    Given I am authenticated as admin via API
    When I send a PUT request to update the category with invalid parentId
    Then the response status should be 404 or 400

  @API-CAT-ADMIN-14
  Scenario: Admin delete already deleted category
    Given I am authenticated as admin via API
    And I have created a category for testing
    When I send a DELETE request to delete the category
    Then the response status should be 200 or 202 or 204
    When I send a DELETE request to delete the category
    Then the response status should be 404 or 400

  # ========== User Tests ==========

  @API-CAT-USER-01
  Scenario: User can retrieve all categories
    Given I am authenticated as user via API
    When I send a GET request to "/api/categories"
    Then the response status should be 200
    And each category should contain required fields

  @API-CAT-USER-02
  Scenario: Validate response structure fields
    Given I am authenticated as user via API
    When I send a GET request to "/api/categories"
    Then the response status should be 200
    And each category should contain required fields

  @API-CAT-USER-03
  Scenario: Parent category mapping
    Given a main category and a sub category exist
    Given I am authenticated as user via API
    When I send a GET request to "/api/categories"
    Then the response status should be 200
    And parent category mapping should be valid

  @API-CAT-USER-04
  Scenario: Category search by name
    Given I am authenticated as user via API
    When I send a GET request to categories with name "NonExistingCategory"
    Then the response status should be 200
    And the category list response should be empty

  @API-CAT-USER-05
  Scenario: Filter categories by parentId
    Given I am authenticated as user via API
    When I send a GET request to categories with parentId 99999
    Then the response status should be 200
    And the category list response should be empty

  @API-CAT-USER-06
  Scenario: Paginated category list is retrieved with valid parameters
    Given I am authenticated as user via API
    When I send a GET request to categories page with page 0
    Then the response status should be 200

  @API-CAT-USER-07
  Scenario: Category search by name on paged endpoint
    Given I am authenticated as user via API
    When I send a GET request to categories page with name "NonExistingCategory"
    Then the response status should be 200
    And the categories page response should be empty

  @API-CAT-USER-08
  Scenario: Categories filtered by parent category ID on paged endpoint
    Given I am authenticated as user via API
    When I send a GET request to categories page with parentId 99999
    Then the response status should be 200
    And the categories page response should be empty

  @API-CAT-USER-09
  Scenario: Response when page number contains no category data
    Given I am authenticated as user via API
    When I send a GET request to categories page with page 999
    Then the response status should be 200
    And the categories page response should be empty

  @API-CAT-USER-10
  Scenario: User cannot update a category
    Given I am authenticated as user via API
    When I send a PUT request to update the existing category
    Then the response status should be 403

  @API-CAT-USER-11
  Scenario: User cannot add a category
    Given I am authenticated as user via API
    When I send a POST request to "/api/categories" with a valid name
    Then the response status should be 403

  @API-CAT-USER-12
  Scenario: User cannot delete a category
    Given I am authenticated as user via API
    When I send a DELETE request to the existing category
    Then the response status should be 403
