Feature: Create Category

  @admin
  Scenario: TC_API_ADMIN_CAT_ADD_001 Verify Creating category with valid data
    When admin creates a main category with name "Add1"
    Then category should be created successfully with name "Add1"
